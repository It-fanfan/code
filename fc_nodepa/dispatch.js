//
// 工作对象
//
let config = require('./config.js');
let matching = require('./matching.js');

let dispatchObjectClass = null;
let dispatchObject = null;
let workOutObject = null;
let pipeObject = null;
let index = 0;
//
// 创建一个派发房间工作对象的函数
//
function createDispatchObject() {
    dispatchObjectClass = function() {
        //等待中
        //系统匹配房间
        this.idleRooms = [];
        //邀请匹配房间
        this.friendRooms = [];
        //随机匹配队列
        this.pkUidQueue = {};
    };

    //
    // 设置管道信息
    //
    dispatchObjectClass.prototype.setPipeObject = function(pipe) {
        pipeObject = pipe;
        matching.setPipeObject(pipe);
    };

    //
    // 设置工作对象
    //
    dispatchObjectClass.prototype.setWorkObject = function(workOut) {
        workOutObject = workOut;
        matching.setWorkObject(this, workOut);
    };

    //
    // 建立新的空闲房间
    //
    dispatchObjectClass.prototype.newRoomCreated = function(socketService, roomNo) {
        let roomInfo = {};
        roomInfo.roomSeq = Math.floor(Math.random() * 100000000);
        //游戏状态
        roomInfo.state = 'idle';
        //对应的游戏服
        roomInfo.socketService = socketService;
        //socket索引
        roomInfo.socketIndex = -1;
        //游戏客户端
        roomInfo.socketClients = [];
        //游戏 匹配时间
        roomInfo.idleCounter = config.timeMatchMaxTime;
        //游戏匹配结束后，倒计时
        roomInfo.successCounter = -1;
        //游戏最大人数
        roomInfo.maxPlayer = -1;
        //游戏编号
        roomInfo.gameCode = -1;
        roomInfo.matchKey = undefined;
        //房间号
        roomInfo.roomNo = roomNo;

        if (null == roomNo) {
            this.idleRooms.push(roomInfo);

            config.log('#vp# newRoomConnected random seq: ', roomInfo.roomSeq);
        } else {
            this.friendRooms.push(roomInfo);
            config.log('#vp# newRoomConnected friend seq: ', roomNo);
        }

        return roomInfo;
    };

    //
    // 新的客户端已连接
    //
    dispatchObjectClass.prototype.newClientConnected = function(socketClient) {
        socketClient.vpSeqClient = Math.floor(Math.random() * 100000000);
        socketClient.connectState = 1;
        socketClient.vpInRoom = null;
        socketClient.vpMatchParams = null;
        socketClient.vpRoomPosition = -1;
        config.log('#vp# newClientConnected seq: ', socketClient.vpSeqClient, socketClient.connectState);
    };

    //
    // 房间由加载转结束
    //
    dispatchObjectClass.prototype.changeRoomStateClose = function(buzyRooms)
    {
        for(let i = buzyRooms.length-1;i>=0;i--)
        {
            let buzy = buzyRooms[i];
            let loopRooms = this.idleRooms;
            if (null != buzy.roomNo) {
                loopRooms = this.friendRooms;
            }
            for (let i = loopRooms.length - 1; i >= 0; i--)
            {
                let idleRoom = loopRooms[i];
                if (idleRoom.roomSeq == buzy.roomSeq) {
                    config.log('#vp# idle room cleared seq: ', buzy.roomSeq);
                     //通知转游戏服
                     matching.sendMatchFinishMessage(this, idleRoom);
                     //进行移除等待服房间
                    loopRooms.splice(i, 1);
                    return;
                }
            }
        }
       
    }

    //
    // 房间由匹配转换为加载中
    //
    dispatchObjectClass.prototype.changeRoomStateLoading = function(roomObject) {
        let loopRooms = this.idleRooms;

        if (null != roomObject.roomNo) {
            loopRooms = this.friendRooms;
        }

        for (let i = loopRooms.length - 1; i >= 0; i--) {
            let idleRoom = loopRooms[i];

            if (idleRoom == roomObject) {
                idleRoom.state = 'finish';
                if (roomObject.socketClients.length > 0) {
                    let gameInfo = pipeObject.getGame(roomObject.gameCode);
                    if(!gameInfo){                        
                        continue;
                    }
                    idleRoom.gameMode = 0;
                    let clientNum = roomObject.socketClients.length;
                    if(gameInfo.isPk)
                    {
                        //单人模式，并且不允许单人
                        if(1 == clientNum && !gameInfo.isSingle)
                        {
                            //匹配失败,移除
                            config.log('fail 匹配失败,移除');
                            matching.sendMatchFailedMessage(this, roomObject.socketClients[0]);
                            loopRooms.splice(i, 1);
                            return;
                        }
                        if(gameInfo.isSingle && 1== clientNum) idleRoom.gameMode = 1;
                        
                    }
                    pipeObject.notifyNewPkRoom(idleRoom);
                }                
                return;
            }
        }
    };

    //
    // 移除房间逻辑
    //
    dispatchObjectClass.prototype.removeRoomByService = function(socketService, rooms) {
        for (let i = rooms.length - 1; i >= 0; i--) {
            let room = rooms[i];
            if (room.socketService == socketService) {
                for (let j = room.socketClients.length - 1; j >= 0; j--) {
                    let clientObject = room.socketClients[j];
                    clientObject.close();
                }
                rooms.splice(i, 1);
            }
        }
    };

    //
    // 服务端断开连接
    //
    dispatchObjectClass.prototype.serviceDisconnected = function(socketService) {
        //等待房间移除
        this.removeRoomByService(socketService, this.idleRooms);
        //好友房间移除
        this.removeRoomByService(socketService, this.friendRooms);
        pipeObject.sendDemonMessage();
    };

    //
    // 客户端断开连接
    //
    dispatchObjectClass.prototype.clientDisconnected = function(socketClient) {
        matching.clientDisconnected(this, socketClient);
    };

    //
    // 处理及转发消息
    //
    dispatchObjectClass.prototype.handleMessage = function(socket, message) {
        if ('match' == message.type) {
            matching.clientMatching(this, socket, message.data);
            return;
        }
        if ('start' == message.type) {
            matching.clientStart(this, socket, message.data);
            return;
        }
        if ('talk' == message.type) {
            matching.roomTalk(this, socket, message.data);
            return;
        }
    };

    //
    // 遍历房间状态
    //
    dispatchObjectClass.prototype.roomLooping = function() {
        //
        // 遍历等待中的房间
        //
        for (let i = this.idleRooms.length - 1; i >= 0; i--) {
            let roomObject = this.idleRooms[i];
            matching.roomLooping(this, roomObject, i);
        }
        for (let i = this.friendRooms.length - 1; i >= 0; i--) {
            let roomObject = this.friendRooms[i];
            matching.roomLooping(this, roomObject, i);
        }
        //遍历等待中的用户信息
        matching.matchQueueChangeRoom(this,this.pkUidQueue);

    };

    //
    // 创建当前工作对象
    //
    dispatchObject = new dispatchObjectClass();

    //
    // 房间定时器启动
    //
    setInterval(function() {
        dispatchObject.roomLooping();
    }, 1000);
}

//
// 建立工作环境
//
(function() {
    createDispatchObject();
})();

//
// 模块声明
//
module.exports = dispatchObject;
