//
// 工作对象
//
let config = require('./config.js');

let matchingObjectClass = null;
let matchingObject = null;
let workOutObject = null;
let workInSocket = null;
let pipeObject = null;

//
// 创建一个工作对象的函数
//
function createMatchingObject() {
    matchingObjectClass = function () { };

    //
    // 设置管道信息
    //
    matchingObjectClass.prototype.setPipeObject = function (pipe) {
        pipeObject = pipe;
    };

    //
    // 设置工作对象
    //
    matchingObjectClass.prototype.setWorkObject = function (dispatchObject, workOut, workIn) {
        workOutObject = workOut;
        workInSocket = workIn;
    };

    //
    // 发送房间消息
    //
    matchingObjectClass.prototype.sendRoomMessage = function (dispatchObject, roomObject, type, message = null) {
        if ('match-status' == type) {
            let statusMessage = {};
            statusMessage.state = 'idle';
            statusMessage.idleCounter = roomObject.idleCounter;
            statusMessage.maxPlayer = roomObject.maxPlayer;
            statusMessage.players = [];
            for (let i = 0; i < roomObject.socketClients.length; i++) {
                let params = roomObject.socketClients[i].vpMatchParams;
                let player = {};

                player.uid = params.uid;
                player.name = params.name;
                player.avatarUrl = params.avatarUrl;
                player.avatarFrame = params.avatarFrame;
                player.rank = params.rank;
                player.position = params.position;
                statusMessage.players.push(player);
            }
            for (let i = roomObject.socketClients.length - 1; i >= 0; i--) {
                let socket = roomObject.socketClients[i];
                workOutObject.sendOutMessage(socket, type, statusMessage);
            }
            return;
        }
    };

    //
    // 发送匹配失败消息
    //
    matchingObjectClass.prototype.sendMatchFailedMessage = function (clientObject) {
        let statusMessage = {};
        statusMessage.state = 'fail';
        workOutObject.sendOutMessage(clientObject, 'match-status', statusMessage);
        clientObject.connectState = 2;
    };

    //发送单匹配成功消息
    matchingObjectClass.prototype.sendMatchFinishSingleMessage = function (room, socketClient) {
        let finishMessage = {};
        finishMessage.state = 'finish';
        //pk地址
        finishMessage.pkAddr = room.pkAddr;
        finishMessage.pkPort = room.pkPort;
        finishMessage.pkRoomSeq = room.roomSeq;
        finishMessage.resInfo = pipeObject.getGame(room.gameCode);
        //位置暂无
        finishMessage.pkPosition = socketClient.vpRoomPosition;
        finishMessage.pkClientSeq = socketClient.vpSeqClient;
        workOutObject.sendOutMessage(socketClient, 'match-status', finishMessage);
    };

    //
    // 发送匹配完成消息
    //
    matchingObjectClass.prototype.sendMatchFinishMessage = function (dispatchObject, roomObject) {
        let finishMessage = {};
        finishMessage.state = 'finish';
        finishMessage.pkAddr = roomObject.socketService.addr;
        finishMessage.pkPort = roomObject.socketService.port;
        finishMessage.pkRoomSeq = roomObject.roomSeq;
        finishMessage.resInfo = pipeObject.getGame(roomObject.gameCode);

        for (let i = roomObject.socketClients.length - 1; i >= 0; i--) {
            let socketClient = roomObject.socketClients[i];
            finishMessage.pkPosition = socketClient.vpRoomPosition;
            finishMessage.pkClientSeq = socketClient.vpSeqClient;
            workOutObject.sendOutMessage(socketClient, 'match-status', finishMessage);
            socketClient.connectState = 2;
        }
    };

    //
    // 客户端断开连接
    //
    matchingObjectClass.prototype.clientDisconnected = function (dispatchObject, socketClient) {

        let uid = socketClient.vpMatchParams && socketClient.vpMatchParams.uid;
        //进行移除队列
        let queue = dispatchObject.pkUidQueue;
        Object.keys(queue).forEach(key => {
            let gameQueue = queue[key];
            for (let i = gameQueue.length - 1; i >= 0; i--) {
                let client = gameQueue[i];
                if (client.uid == uid) {
                    gameQueue.splice(i, 1);
                    return;
                }
            }
        });

        let roomObject = socketClient.vpInRoom;
        if (null == roomObject) {
            return;
        }
        socketClient.connectState = 2;
        if (roomObject.state != 'idle') {
            return;
        }
        //房主出去,移除房主标签
        if (uid == roomObject.roomNo) {
            roomObject.masterFlag = false;
        }
        //游戏匹配，移除房间
        for (let i = roomObject.socketClients.length - 1; i >= 0; i--) {
            let client = roomObject.socketClients[i];
            if (client == socketClient) {
                roomObject.socketClients.splice(i, 1);
                roomObject.socketIndex--;
                this.sendRoomMessage(dispatchObject, roomObject, 'match-status');
                break;
            }
        }


        config.log('#vp# matching clientDisconnected uid: ', uid);
    };

    //
    //进行队列等待
    //
    matchingObjectClass.prototype.clientQueueWaiting = function (dispatchObject, socket, message) {
        //分数计算
        let mark = message.total * message.success;
        //队列等待时间
        let waiting = config.timeWaitingMaxTime;
        //添加队列
        let userInfo = {
            uid: message.uid,
            mark: mark,
            start: waiting,
            socket: socket,
            message: message
        };

        let fun = (timer) => {
            let queue = dispatchObject.pkUidQueue;
            if (queue.unLock) return;
            let gameQueue = queue[message.gameCode];
            if (gameQueue == null) gameQueue = [];
            gameQueue.push(userInfo);
            //进行排序
            gameQueue.sort((a, b) => { return a.mark <= b.mark; });
            //赋值
            dispatchObject.pkUidQueue[message.gameCode] = gameQueue;
            clearInterval(timer);
        }
        let timer = setInterval(() => {
            fun(timer);
        }, 100);

    }

    /**
     * 进行用户转房间
     */
    matchingObjectClass.prototype.matchQueueChangeRoom2 = function (dispatchObject, user1, user2) {
        //检查房间
        user1.select = true;
        if(user2 != null)  user2.select = true;
        let message = user1.message;
        //游戏服务器
        let pkServices = pipeObject.pkServices;
        //查询空闲的房间
        let pkAvailable = this.searchPkServices(pkServices, message);
        if (null == pkAvailable) {
            config.log('fail searchPkServices');
            this.sendMatchFailedMessage(user1.socket);
            if(user2 != null) this.sendMatchFailedMessage(user2.socket);
            return;
        }
        // 进入目标房间
        //
        let roomObject = dispatchObject.newRoomCreated(pkAvailable, null);
        roomObject.matchKey = message.matchKey;
        if(user2 == null)  roomObject.idleCounter = 1;
        // roomObject.idleCounter = 1;
        //设置房主标签
        this.entryMatchRoom(dispatchObject, user1.socket, roomObject);
        if(user2 != null) this.entryMatchRoom(dispatchObject, user2.socket, roomObject);
    }
    //
    //PK匹配队列转房间
    //
    matchingObjectClass.prototype.matchQueueChangeRoom = function (dispatchObject, queue) {
        queue.unLock = true;
        Object.keys(queue).forEach(key => {
            let gameQueue = queue[key];
            //PK队列等待
            for (let i = gameQueue.length - 1; i >= 0; i--) {
                let userInfo = gameQueue[i];
                if (userInfo.select) continue;
                let lastInfo = gameQueue[i + 1];
                let nextInfo = gameQueue[i - 1];
                //当前玩家
                if (userInfo)
                    userInfo.start--;
                //进行命中
                if (userInfo.start <= 0) {
                    let lastSub = -1, nextSub = -1;
                    if (lastInfo) lastSub = Math.abs(lastInfo.mark - userInfo.mark);
                    if (nextInfo) nextSub = Math.abs(nextInfo.mark - userInfo.mark);
                    //分差最小进行匹配
                    if (lastInfo && (lastSub >= nextSub || nextSub == -1)) {
                        this.matchQueueChangeRoom2(dispatchObject, lastInfo, userInfo);
                        break;
                    }
                    if (nextInfo && (nextSub >= lastSub || lastSub == -1)) {
                        this.matchQueueChangeRoom2(dispatchObject, nextInfo, userInfo);
                        break;
                    }
                }
                let socket = userInfo.socket;
                let statusMessage = {};
                statusMessage.state = 'idle';
                statusMessage.idleCounter = config.timeMatchMaxTime + userInfo.start;
                statusMessage.maxPlayer = 2;
                statusMessage.players = [];
                let player = {};
                player.uid = userInfo.message.uid;
                player.name = userInfo.message.name;
                player.avatarUrl = userInfo.message.avatarUrl;
                player.avatarFrame = userInfo.message.avatarFrame;
                player.rank = userInfo.message.rank;
                player.position = userInfo.message.position;
                statusMessage.players.push(player);
                workOutObject.sendOutMessage(socket, 'match-status', statusMessage);
            }
           
            //进行移除已经存储匹配，以及等待
            for (let i = gameQueue.length - 1; i >= 0; i--) {
                let userInfo = gameQueue[i];
                if (userInfo.select) {
                    gameQueue.splice(i, 1);
                    continue;
                }
                if (config.timeMatchMaxTime + userInfo.start <= 0) {                   
                    let gameInfo = pipeObject.getGame(userInfo.message.gameCode);
                    if(gameInfo.isSingle && !userInfo.message.isMiniPro)
                    {
                        this.matchQueueChangeRoom2(dispatchObject, userInfo, null);
                    }else
                    {
                        gameQueue.splice(i, 1);
                        config.log("移除队列", gameQueue.length);
                        //等待倒计时整体结束
                        this.sendMatchFailedMessage(userInfo.socket);
                    }                 
                }
            }
        });
        queue.unLock = false;
    }

    //
    // 匹配后进入房间设置
    //
    matchingObjectClass.prototype.entryMatchRoom = function (dispatchObject, socketClient, roomObject) {
        let curParams = socketClient.vpMatchParams;
        let isFirstEntry = roomObject.socketClients.length <= 0;
        //设置当前socket状态
        socketClient.connectState = 1;
        //进行检测该玩家是否已经存在该房间内
        let insert = true;
        if (!isFirstEntry) {
            for (let i = 0; i < roomObject.socketClients.length; i++) {
                let clientObject = roomObject.socketClients[i];
                if (clientObject.vpMatchParams.uid == curParams.uid) {
                    clientObject = roomObject.socketClients[i] = socketClient;
                    clientObject.vpRoomPosition = i;
                    insert = false;
                    break;
                }
            }
        } else {
            roomObject.socketIndex = 0;
        }
        if (insert) {
            roomObject.socketClients.push(socketClient);
            socketClient.vpRoomPosition = roomObject.socketIndex++;
        }
        socketClient.vpInRoom = roomObject;
        //首次进入游戏
        if (isFirstEntry) {
            roomObject.maxPlayer = curParams.maxPlayer;
            roomObject.gameCode = curParams.gameCode;
            roomObject.matchKey = curParams.matchKey;
            roomObject.server = curParams.server;

        } else {
            if (roomObject.socketClients.length >= roomObject.maxPlayer) {
                roomObject.successCounter = 1;
            }
            this.sendRoomMessage(dispatchObject, roomObject, 'match-status');
        }
    };

    //
    //按照规则进行获取对应服务
    //
    matchingObjectClass.prototype.searchPkServices = function (pkServices, message) {
        let pkAvailable = null;
        //获取
        let matchService = [];
        let allWeight = 0;
        Object.keys(pkServices).forEach(addr => {
            let service = pkServices[addr];
            //游戏是否允许
            if (service.allow != -1) {
                let game = pipeObject.getGame(message.gameCode);
                switch (service.allow) {
                    //闯关模式
                    case 0: {
                        if (game.isPk) return;
                    } break;
                    //PK模式
                    case 1: {
                        if (!game.isPk) return;
                    } break;
                }
            }
            if (service.idleCount <= 0) return;
            //权重 = 分配权重*剩余房间数 
            //第二版：权重 = （承载玩家数 - 在线玩家数）* 分配权重
            try {
                let weight = 0;
                switch(config.checkType){
                    case "room":{
                        weight = service.idleCount;
                       if(weight <= 0) return;      
                    }break;
                    case "user":{
                        //获取当前在线人数
                        let onlineUser = 0;
                        if(service.buzyRooms)
                        {
                            for(let i = 0;i<service.buzyRooms.length;i++)
                            {
                                let buzy = service.buzyRooms[i];
                                onlineUser += buzy.userCount;
                            }
                        }
                        let maxPlayer = service.maxPlayer || (service.idleCount + service.buzyCount);                
                        weight = maxPlayer - onlineUser;
                        if(maxPlayer <= 0 || weight <= 0) return;                        
                    }break;
                }
                weight = weight * service.weight;               
                if (weight <= 0) return;
                service.tempValue = weight;
                allWeight += weight;
                //符合赛制
                matchService.push(service);
            } catch (error) {
                config.log(error);
            }       
        });
        //权重分配
        if (allWeight > 0) {
            let random = Math.round(Math.random() * allWeight);
            let temp = 0;
            for (let i = 0; i < matchService.length; i++) {
                let service = matchService[i];
                if (random >= temp && random <= service.tempValue + temp) {
                    return service;
                }
                temp += service.tempValue;
            }
            config.log('权限分配为空', allWeight, random);
        }

        return pkAvailable;
    }

    //
    //发送房间无法使用信息
    //
    matchingObjectClass.prototype.sendNotAvailableRoom=function(roomMaster,socketClient)
    {
        let msg = { msg: "房间人数已满，请在大厅匹配" };
        if(roomMaster)
        {
            msg.msg = "上一场比赛未结束，请稍后再试。";
        }
        workOutObject.sendOutMessage(socketClient, 'match-error', msg);
    }

    //
    // 进行一次指定房间匹配
    //
    matchingObjectClass.prototype.clientMatchingFixed = function (dispatchObject, socketClient, message, roomNo) {
        //是否为房主
        let roomMaster = message.uid == roomNo;
        config.log('uid master', message.uid, roomMaster);
        // 查询匹配的房间
        //
        let friendRooms = dispatchObject.friendRooms;
        for (let i = 0; i < friendRooms.length; i++) {
            let friendRoom = friendRooms[i];
            //拥有房间特殊标志
            let againRoomSeq = friendRoom.againRoomSeq;
            if (againRoomSeq) continue;
            if (friendRoom.roomNo != roomNo) continue;
            if (friendRoom.gameCode != message.gameCode) continue;
            let current = friendRoom.socketClients.length;
            let sub = friendRoom.maxPlayer - current;
            //房主存在标签
            if (!friendRoom.masterFlag && !roomMaster) {
                sub -= 1;
            }
            if (sub <= 0) {
                this.sendNotAvailableRoom(roomMaster,socketClient);
                return;
            }
            //房主重新进入房间
            if (roomMaster) {
                friendRoom.masterFlag = roomMaster;
            }
            //游戏 匹配时间
            friendRoom.idleCounter = config.timeMatchMaxTime;
            this.entryMatchRoom(dispatchObject, socketClient, friendRoom);
            return;
        }

        let pkServices = pipeObject.pkServices;
        for(let key  in pkServices) {
            let service = pkServices[key];
            //无游戏中房间
            if (service.buzyCount <= 0) continue;
            let buzyRooms = service.buzyRooms;
            let uid = socketClient.vpMatchParams.uid;
            for (let j = 0; j < buzyRooms.length; j++) {
                let room = buzyRooms[j];
                //当前房间游戏不匹配
                if (room.gameCode != message.gameCode) continue;
                //房间号是否匹配
                if (room.roomNo != roomNo) continue;
                let sub = room.maxPlayer - room.userCount;
                //房主存在标签
                if (!room.masterFlag && !roomMaster) {
                    sub -= 1;
                }
                //当前房间满员
                if (sub <= 0) {
                    this.sendNotAvailableRoom(roomMaster,socketClient);
                    return;
                }
                //当前房间已经参与过
                if (room.vpRecords.indexOf(uid) >= 0) {
                    this.sendNotAvailableRoom(roomMaster,socketClient);
                    return;
                }
                //通知玩家房间匹配成功，进入游戏,设置游戏手柄
                for (let k = 0; k < room.maxPlayer; k++) {
                    //手柄已存在
                    if (room.vpUserIndexs.indexOf(k) >= 0) continue;
                    socketClient.vpRoomPosition = k;
                    break;
                }
                //房主重新进入房间
                if (roomMaster) {
                    room.masterFlag = roomMaster;
                }
                room.pkAddr = service.addr;
                room.pkPort = service.port;
                this.sendMatchFinishSingleMessage(room, socketClient);
                return;
            }
        }

        // 再查询空闲的房间
        //

        let pkAvailable = this.searchPkServices(pkServices, message);
        if (null == pkAvailable) {
            config.log('fail searchPkServices');
            this.sendMatchFailedMessage(socketClient);
            return;
        }

        // 进入目标房间
        //
        let roomObject = dispatchObject.newRoomCreated(pkAvailable, roomNo);
        //设置房主标签
        roomObject.masterFlag = roomMaster;
        //房间赛场标志
        roomObject.matchKey = message.matchKey;
        this.entryMatchRoom(dispatchObject, socketClient, roomObject);
    };



    //
    // 进行一次随机匹配
    //
    matchingObjectClass.prototype.clientMatchingRandom = function (dispatchObject, socketClient, message) {

        let gameInfo = pipeObject.getGame(message.gameCode);
        if (gameInfo.isPk) {
            this.clientQueueWaiting(dispatchObject, socketClient, message);
            return;
        }
        config.log('data', JSON.stringify(message));
        // 先查询正在匹配的房间
        //
        let idleRooms = dispatchObject.idleRooms;
        for (let i = 0; i < idleRooms.length; i++) {
            let idleRoom = idleRooms[i];
            //成功倒计时开始，暂不允许进入
            if (idleRoom.successCounter >= 0) continue;
            //拥有房间特殊标志
            let againRoomSeq = idleRoom.againRoomSeq;
            if (againRoomSeq) continue;

            if (idleRoom.socketClients.length <= 0 || idleRoom.gameCode != message.gameCode) continue;
            let current = idleRoom.socketClients.length;
            if (current >= idleRoom.maxPlayer) continue;
            //是否有指定赛场号
            if ((idleRoom.matchKey || message.matchKey) && message.matchKey != idleRoom.matchKey) continue;
            //正常加入房间
            this.entryMatchRoom(dispatchObject, socketClient, idleRoom);
            return;
        }

        // 再查询空闲的房间
        //
        let pkServices = pipeObject.pkServices;
        let pkAvailable = this.searchPkServices(pkServices, message);
        //无空闲房间
        if (null == pkAvailable) {
            for(let key  in pkServices) {
                let service = pkServices[key];
                //无游戏中房间
                if (service.buzyCount <= 0) continue;
                let buzyRooms = service.buzyRooms;
                let uid = socketClient.vpMatchParams.uid;
                for (let j = 0; j < buzyRooms.length; j++) {
                    let room = buzyRooms[j];
                    //当前房间游戏不匹配
                    if (room.gameCode != message.gameCode) continue;
                    //当前房间满员
                    if (room.userCount >= room.maxPlayer) continue;
                    //当前房间已经参与过
                    if (room.vpRecords.indexOf(uid) >= 0) continue;
                    //是否有指定赛场号
                    if ((room.matchKey || message.matchKey) && message.matchKey != room.matchKey) continue;
                    //通知玩家房间匹配成功，进入游戏,设置游戏手柄
                    for (let k = 0; k < room.maxPlayer; k++) {
                        //手柄已存在
                        if (room.vpUserIndexs.indexOf(k) >= 0) continue;
                        socketClient.vpRoomPosition = k;
                        break;
                    }
                    room.pkAddr = service.addr;
                    room.pkPort = service.port;
                    this.sendMatchFinishSingleMessage(room, socketClient);
                    return;
                }
            }
            config.log('fail pkAvailable');
            this.sendMatchFailedMessage(socketClient);
            return;
        }
        // 进入目标房间
        //
        let roomObject = dispatchObject.newRoomCreated(pkAvailable, null);
        roomObject.matchKey = message.matchKey;
        this.entryMatchRoom(dispatchObject, socketClient, roomObject);
    };

    //
    // 快捷语消息
    //
    matchingObjectClass.prototype.roomTalk = function (dispatchObject, socketClient, message) {
        let roomObject = socketClient.vpInRoom;
        if (null == roomObject) {
            return;
        }

        for (let i = roomObject.socketClients.length - 1; i >= 0; i--) {
            workOutObject.sendOutMessage(roomObject.socketClients[i], 'talk', message);
        }
    };

    //进行匹配再来一局逻辑
    matchingObjectClass.prototype.matchAgain = function (dispatchObject, socketClient, message) {
        //进行检测存在房间号
        let againRoomSeq = message.againRoomSeq;
        if (!againRoomSeq) return false;
        let curParams = socketClient.vpMatchParams;
        let fixRoom = curParams.fixRoom;
        //房间存在则进行匹配
        let idleRooms = !fixRoom ? dispatchObject.idleRooms : dispatchObject.friendRooms;
        for (let i = 0; i < idleRooms.length; i++) {
            let idleRoom = idleRooms[i];
            //拥有房间特殊标志
            let roomSeq = idleRoom.againRoomSeq;
            if (againRoomSeq == roomSeq) {
                //正常加入房间
                this.entryMatchRoom(dispatchObject, socketClient, idleRoom);
                return true;
            }
        }
        // 再查询空闲的房间
        //
        let pkServices = pipeObject.pkServices;
        let pkAvailable = null;
        for(let key  in pkServices) {
            let service = pkServices[key];
            if (service.idleCount > 0) {
                if (null == pkAvailable) {
                    pkAvailable = service;
                } else if (service.buzyCount < pkAvailable.buzyCount) {
                    pkAvailable = service;
                }
            }
        }
        if (pkAvailable == null) {
            config.log('fail pkAvailable matchAgain');
            this.sendMatchFailedMessage(socketClient);
        }
        //创建新的房间
        let roomObject = dispatchObject.newRoomCreated(pkAvailable, fixRoom);
        roomObject.againRoomSeq = againRoomSeq;
        this.entryMatchRoom(dispatchObject, socketClient, roomObject);
        return true;
    }

    //
    // 进行一次客户端匹配
    //
    matchingObjectClass.prototype.clientMatching = function (dispatchObject, socketClient, message) {
        socketClient.vpMatchParams = message;
        let curParams = socketClient.vpMatchParams;
        if (null == curParams) {
            return;
        }
        if (null != socketClient.vpInRoom) {
            return;
        }
        //匹配再来一局逻辑
        if (this.matchAgain(dispatchObject, socketClient, message)) {
            return;
        }
        //指定房间为空，进行随机匹配
        if (null == curParams.fixRoom) {
            this.clientMatchingRandom(dispatchObject, socketClient, message);
        } else {
            this.clientMatchingFixed(dispatchObject, socketClient, message, curParams.fixRoom);
        }
    };

    //
    // 客户端开始游戏
    //
    matchingObjectClass.prototype.clientStart = function (dispatchObject, socketClient, message) {
        let roomObject = socketClient.vpInRoom;
        if (null == roomObject) {
            return;
        }
        //检测游戏内玩家人数小于正常开局人数
        if (roomObject.socketClients.length < roomObject.maxPlayer) {
            let gameInfo = pipeObject.getGame(roomObject.gameCode);
            if (null == gameInfo) {
                return;
            }
            //为pk模式游戏，禁止开局
            if (gameInfo.isPk) {
                return;
            }
        }
        //客户端点击游戏开始
        dispatchObject.changeRoomStateLoading(roomObject);
    };

    //
    // 遍历房间状态
    //
    matchingObjectClass.prototype.roomLooping = function (dispatchObject, roomObject, index) {
        //当前房间内用户为0，进行移除
        if (roomObject.socketClients.length <= 0) {
            if (null == roomObject.roomNo) {
                dispatchObject.idleRooms.splice(index, 1);
            } else {
                dispatchObject.friendRooms.splice(index, 1);
            }
            return;
        }
        //当前已经匹配成功
        if (null == roomObject.roomNo && roomObject.successCounter >= 0) {
            roomObject.successCounter--;
            if (roomObject.successCounter <= 0) {
                //通知游戏开始
                dispatchObject.changeRoomStateLoading(roomObject);
                return;
            }
        }
        //匹配等待时间
        roomObject.idleCounter--;
        if (null == roomObject.roomNo && roomObject.idleCounter <= 0) {
            //通知游戏开始
            dispatchObject.changeRoomStateLoading(roomObject);
            return;
        }
        //通知游戏继续等待
        this.sendRoomMessage(dispatchObject, roomObject, 'match-status');
    };

    //
    // 创建当前工作对象
    //
    matchingObject = new matchingObjectClass();
}

//
// 建立工作环境
//
(function () {
    createMatchingObject();
})();

//
// 模块声明
//
module.exports = matchingObject;
