//
// 工作对象
//
const VpRoom = require('./VpRoom.js');
const VpClient = require('./VpClient.js');

let config = require('./config.js');
let process = require('child_process');
let dispatchObjectClass = null;
let dispatchObject = null;
let workInObject = null;
let workOutObject = null;
let pipeObject = null;

//
// 创建一个派发房间工作对象的函数
//
function createDispatchObject() {
    dispatchObjectClass = function() {
        //游戏进程编号
        this.execPids = [];
        //空闲房间
        this.idleRooms = [];
        //忙碌房间
        this.buzyRooms = [];
        //已经成功关闭游戏房间,#该房间只做保留
        this.overRooms = [];
        this.roomCheckCounter = 0;
    };

    //
    // 设置管道信息
    //
    dispatchObjectClass.prototype.setPipeObject = function(pipe) {
        pipeObject = pipe;
    };

    //
    // 设置工作对象
    //
    dispatchObjectClass.prototype.setWorkObject = function(workIn, workOut) {
        workInObject = workIn;
        workOutObject = workOut;
    };

    //
    // 新的空闲房间已连接
    //
    dispatchObjectClass.prototype.newRoomConnected = function(socketService) {
        let pid = this.execPids.splice(0,1);
        socketService.pid = pid;
        let roomObject = new VpRoom(socketService, this, workInObject, workOutObject, pipeObject);

        this.idleRooms.push(roomObject);
        pipeObject.notifyPkState(this);
    };

    //
    // 新的客户端已连接
    //
    dispatchObjectClass.prototype.newClientConnected = function(socketClient) {
        new VpClient(socketClient, this, workInObject, workOutObject, pipeObject);
    };

    //
    // 新的客户端进入房间
    //
    dispatchObjectClass.prototype.clientEntryRoom = function(dispatchObject, socket, message) {
        let buzyRooms = this.buzyRooms;

        for (let i = buzyRooms.length - 1; i >= 0; i--) {
            let buzyRoom = buzyRooms[i];
            if(message.roomSeq == buzyRoom.roomSeq && message.gameCode == buzyRoom.gameCode){
                if (buzyRoom.clientEntryRoom(socket, message)) {
                    return;
                }
            }           
        }
        socket.close();
    };

    //
    // 处理及转发消息
    //
    dispatchObjectClass.prototype.handleMessage = function(socket, message) {
        if ('entry' == message.type) {
            this.clientEntryRoom(this, socket, message.data);
            return;
        }
        let clientObject = socket.clientObject;
        if (null == clientObject) {
            return;
        }
        clientObject.handleClient(message.type, message.data);
    };

    //
    // 服务端断开连接
    //
    dispatchObjectClass.prototype.serviceDisconnected = function(socketService) {
        let roomObject = socketService.vpInRoom;
        if (null == roomObject) {
            return;
        }
        roomObject.serviceDisconnected();

        for (let i = this.idleRooms.length - 1; i >= 0; i--) {
            let roomTemp = this.idleRooms[i];
            if (roomObject.roomSeq == roomTemp.roomSeq) {
                this.idleRooms.splice(i, 1);
                break;
            }
        }

        for (let i = this.buzyRooms.length - 1; i >= 0; i--) {
            let roomTemp = this.buzyRooms[i];
            if (roomObject.roomSeq == roomTemp.roomSeq) {
                this.buzyRooms.splice(i, 1);
                break;
            }
        }
        pipeObject.notifyPkState(this);
    };

    //
    // 客户端断开连接
    //
    dispatchObjectClass.prototype.clientDisconnected = function(socket) {
        let clientObject = socket.clientObject;
        if (null == clientObject) {
            return;
        }
        clientObject.clientDisconnected();
    };

    //
    //将忙碌房间转移到等待房间
    //
    dispatchObjectClass.prototype.changeRoom = function(room)
    {
        //从忙碌中移除
        for (let i = this.buzyRooms.length - 1; i >= 0; i--) {
            let roomObject = this.buzyRooms[i];
            if(roomObject ==room) 
            {
                this.buzyRooms.splice(i,1);
                break;
            }
        }
        //添加到等待列表
        this.overRooms.push(room);
    }

    //
    // 遍历房间状态
    //
    dispatchObjectClass.prototype.roomLooping = function() {
        //
        // 遍历忙碌中的房间
        //
        for (let i = this.buzyRooms.length - 1; i >= 0; i--) {
            let roomObject = this.buzyRooms[i];
            roomObject.handleUpdate();
            roomObject.checkRoomRecover();
        }

        //
        // 回收忙碌中的房间
        //
        for (let i = this.buzyRooms.length - 1; i >= 0; i--) {
            let roomObject = this.buzyRooms[i];
            if ('killed' == roomObject.state) {
                this.roomCheckCounter = 0;
                this.buzyRooms.splice(i, 1);                
            }
        }

        //收回等待中的房间
        for(let i = this.overRooms.length - 1;i>=0;i--)
        {
            let roomObject = this.overRooms[i];
            if ('killed' == roomObject.state) {
                this.overRooms.splice(i, 1);           
            }
            else
            {
                //进行更新频率
                roomObject.handleUpdate();
            }
        }

        //
        // 检查并创建空闲房间
        //
        if (this.roomCheckCounter > config.timeNewRoomCheck) {
            this.roomCheckCounter = 0;

            let roomCount = this.idleRooms.length + this.buzyRooms.length;
            if (roomCount < config.countMaxRooms) {
                config.log('#vp# checking --> roomCount < maxRooms: ', roomCount, config.countMaxRooms);

                let pri = process.exec(config.newRoomCmd, function(err, stdout, stderr) {
                   
                 });
            //     config.log('exec',pri.pid);
                this.execPids.push(pri.pid);                
            }
        }
        this.roomCheckCounter++;
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
