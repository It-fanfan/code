//
// 工作对象
//
let fs = require('fs');
let ws = require('ws');
const zlib = require('zlib');
let net = require('net');
let config = require('./config.js');

let pipeObjectClass = null;
let pipeObject = null;

//
// 创建一个管道套接字工作对象的函数
//
function createPipeObject() {
    pipeObjectClass = function() {
        this.app = null;
        this.socket = null;
        this.games = [];
    };

    //
    // 获取一份游戏数据
    //
    pipeObjectClass.prototype.getGame = function(code) {
        if (null == this.games) {
            return null;
        }
        return this.games[code];
    };

    //
    //通知房间变更信息
    //
    pipeObjectClass.prototype.notifyRoomChanage = function(buzyRoom)
    {
        if(!buzyRoom || buzyRoom.state == 'idle')
        {
            return;
        }
        let message = {};
        message.type = 'pkRoom';
        let roomInfo = {};
         //针对玩家单人结算数据
         roomInfo.vpUserFinish = [];
         roomInfo.isSingle = {};
         let userCount = 0;
         for (let j = buzyRoom.roomClients.length - 1; j >= 0; j--) {
             let client = buzyRoom.roomClients[j];
             if (!client) continue;
             if (null == client.socketClient) {
                 continue;
             }
             userCount++;
             let uid = client.vpMatchParams.uid;
             let finishResult = client.finishResult;
             if(finishResult != null)
             {
                 roomInfo.vpUserFinish.push({uid:uid,finishResult:finishResult});
             }
             roomInfo.isSingle[uid] = client.isSingle;
         }
         roomInfo.userCount = userCount;
         //房间索引
         roomInfo.roomSeq = buzyRoom.roomSeq;         
         //获取当前房间状态
         roomInfo.status = buzyRoom.state;
         //获取当前结算进度
         roomInfo.finishResult = buzyRoom.finishResult;
         //设置房间属性
         message.roomInfo = roomInfo;
        //通知匹配已经成功接受到房间请求
        this.sendPipeMessage(message);
    }

    //
    // 处理收到的消息的函数
    //
    pipeObjectClass.prototype.notifyPkState = function(dispatch) {
        let message = {};

        message.type = 'pkState';
        message.idleCount = dispatch.idleRooms.length;
        message.buzyCount = dispatch.buzyRooms.length;
        message.buzyRooms = [];
        for (let i = message.buzyCount - 1; i >= 0; i--) {
            let buzyRoom = dispatch.buzyRooms[i];
            let roomInfo = {};
            let userCount = 0; 
            roomInfo.gameMode = buzyRoom.gameMode;
            roomInfo.vpUserIndexs = [];
            roomInfo.vpRecords = buzyRoom.vpRecords;
            roomInfo.isSingle = {};
            for (let j = buzyRoom.roomClients.length - 1; j >= 0; j--) {
                let client = buzyRoom.roomClients[j];
                if (!client) continue;
                //添加索引编号
                if (!buzyRoom.isPk) roomInfo.vpUserIndexs.push(client.vpRoomPosition);
                if (null == client.socketClient) {
                    continue;
                }
                userCount++;
            }
            //房间号
            roomInfo.roomNo = buzyRoom.roomNo;
            roomInfo.userCount = userCount;
            roomInfo.masterFlag = buzyRoom.masterFlag;
             //获取当前房间状态
            roomInfo.status = buzyRoom.state;
            roomInfo.roomSeq = buzyRoom.roomSeq;
            roomInfo.gameCode = buzyRoom.gameCode;
            roomInfo.maxPlayer = buzyRoom.maxPlayer;          
            roomInfo.matchKey = buzyRoom.matchKey;            
            message.buzyRooms[i] = roomInfo;
        }
        //通知匹配已经成功接受到房间请求
        this.sendPipeMessage(message);
    };

    //
    // 处理收到的消息的函数
    //
    pipeObjectClass.prototype.handleMessage = function(dispatch, socket, message) {
        if (message.type == 'gameSet') {
            this.games = message.games;

            config.log('#pipe# gain gameSet count --> ', Object.keys(this.games).length);
            return;
        }
        switch (message.type) {
            //处理匹配成功消息
            case 'newPk': {
                let roomObject = dispatch.idleRooms[0];
                if (null == roomObject) {
                    return;
                }
                let pkRoomSeq = message.pkRoomSeq;
                //存在历史房间
                for(let i = 0; i< dispatch.buzyRooms; i++)
                {
                    let buzy = dispatch.buzyRooms[i];
                    if(buzy.roomSeq == pkRoomSeq)
                    {
                        return;
                    }
                }

                dispatch.idleRooms.splice(0, 1);
                dispatch.buzyRooms.push(roomObject);
                roomObject.setRoom2Loading(message);
                this.notifyPkState(dispatch);
                return;
            }
        }
    };

    //
    // 处理发送的消息的函数
    //
    pipeObjectClass.prototype.sendPipeMessage = function(message) {
        let content = JSON.stringify(message);

        let sizeInfo = Buffer.alloc(4);
        let stringInfo = Buffer.from(content,'utf8');

        sizeInfo.writeUInt32BE(stringInfo.length);

        let messageInfo = Buffer.concat([sizeInfo, stringInfo]);

        this.socket.write(messageInfo);
    };

    //
    // 创建一个套接字的函数
    //
    pipeObjectClass.prototype.createPipeSocket = function(app) {
        config.log('pipe connect ~');
        let socket = new net.Socket();
        let object = this;
        let dispatch = app.dispatchObject;

        let gameInit = () => {
            if (!config.updateConfig) {
                return;
            }
            config.updateConfig = false;
            object.sendPipeMessage({
                type: 'pkInit',
                addr: config.gameAddr,
                port: config.gamePort,
                weight: config.weight,
                maxPlayer : config.maxPlayer,
                allow: config.allow
            });
        };

        socket.setEncoding('binary');

        // 连接到服务端
        //
        socket.connect(config.matchServerPort, config.matchServerAddr, function() {
            config.log('#pipe# match service connected');
            config.updateConfig = true;
            object.pipeConnected = true;
            gameInit();
            setInterval(gameInit, 3000);
            object.notifyPkState(dispatch);
        });

        // 接收数据
        //
        socket.on('data', function(data) {
            let buf = Buffer.from(data, 'ascii');
            let buffer = socket.dataBuffer;
            if (buffer == null) buffer = buf;
            else {
                buffer = Buffer.concat([buffer, buf]);
            }
            try {
                while (buffer.length >= 4) {
                    let size = buffer.readInt32BE(0);
                    let end = size + 4;
                    if (end > buffer.length) {
                        break;
                    }
                    let message = buffer.slice(4, end);
                    message = message.toString('utf8');
                    message = JSON.parse(message);
                    object.handleMessage(dispatch, socket, message);
                    buffer = buffer.slice(end);
                }
            } catch (e) {
                config.log('#pipe# error json error: ', e);
            }
            socket.dataBuffer = buffer;
        });

        // 连接出错
        //
        socket.on('error', function(error) {
            config.log('#pipe# socket error:' + error);
        });

        // 套接字关闭
        //
        socket.on('close', function() {
            config.log('#pipe# socket connection closed');
            object.pipeConnected = false;
            setTimeout(() => {
                object.createPipeSocket(app);
            }, 2000);
        });

        this.app = app;
        this.socket = socket;
    };

    //
    // 断连后循环重复检测
    //
    pipeObjectClass.prototype.loopCheckPipeSocket = function(app) {
        this.createPipeSocket(app);
    };

    //
    // 创建当前工作对象
    //
    pipeObject = new pipeObjectClass();
}

//
// 建立工作环境
//
(function() {
    createPipeObject();
})();

//
// 模块声明
//
module.exports = pipeObject;
