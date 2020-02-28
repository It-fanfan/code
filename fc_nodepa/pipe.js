//
// 工作对象
//
let fs = require('fs');
let ws = require('ws');
let net = require('net');
let config = require('./config.js');
let matching = require('./matching.js');
var work = require('./work.js');
let pipeObjectClass = null;
let pipeObject = null;

//
// 获取本地 ip 地址
//
function getLocalIp() {
    var interfaces = require('os').networkInterfaces();
    for (var devName in interfaces) {
        var iface = interfaces[devName];
        for (var i = 0; i < iface.length; i++) {
            var alias = iface[i];
            if (alias.family === 'IPv4' && alias.address !== '127.0.0.1' && !alias.internal) {
                return alias.address;
            }
        }
    }
    return 'localhost';
}

//
// 创建一个管道套接字工作对象的函数
//
function createPipeObject() {
    pipeObjectClass = function () {
        this.app = null;
        this.games = null;
        this.serverInfo = null;
        this.workIn = null;
        this.pkServices = {};
    };

    //
    // 处理发送的消息的函数
    //
    pipeObjectClass.prototype.sendPipeMessage = function (socket, message) {
        let content = JSON.stringify(message);
        let contentBuffer = Buffer.from(content,'utf8');
        let headBuffer = Buffer.alloc(4);
        headBuffer.writeUInt32BE(contentBuffer.length);
        let sendBuffer = Buffer.concat([headBuffer, contentBuffer]);
        socket.write(sendBuffer);
    };

    //
    // 获取一份游戏数据
    //
    pipeObjectClass.prototype.getGame = function (code) {
        if (null == this.games) {
            return null;
        }
        return this.games[code];
    };

    //获取demon接受数据
    pipeObjectClass.prototype.handleDemonMessage = function (message) {
        switch (message.type) {
            //告知目前的游戏合集信息
            case 'games': {
                this.games = message.games;
                for(let key  in  this.pkServices) {
                    this.notifyGameSets(this.pkServices[key]);
                }
                config.log('pipe gamesSet length', Object.keys(this.games).length);
                //告知当前匹配服情况
                this.sendDemonMessage();
                return;
            }
            //告知服务器需要交互的service服信息
            case 'serverInfo': {
                this.serverInfo = message;
                return;
            }
        }
    };

    pipeObjectClass.prototype.initWorkIn = function (workIn) {
        this.workIn = workIn;
    }

    //通知发送给demon服务器消息
    pipeObjectClass.prototype.sendDemonMessage = function () {

        let message = [];
        for(let key  in  this.pkServices) {
            let socket = this.pkServices[key];
            message.push({
                addr: socket.addr,
                port: socket.port,
                weight: socket.weight,
                allow: socket.allow,
                maxPlayer: socket.maxPlayer,
                idleCount: socket.idleCount,
                buzyCount: socket.buzyCount,
                buzyRooms: socket.buzyRooms
            });
        }
        let roomInfo = {
            type: "pkGame",
            addr: config.matchHostUrl,
            message: message
        };
        if (this.workIn) this.workIn.sendPipeMessage(roomInfo);
    }

    //
    // 处理收到的消息的函数
    //
    pipeObjectClass.prototype.handleMessage = function (socket, message) {
        switch (message.type) {
            case 'pkInit': {
                socket.addr = message.addr;
                socket.port = message.port;
                socket.weight = message.weight;
                socket.allow = message.allow;
                socket.maxPlayer = message.maxPlayer;
                socket.idleCount = 0;
                socket.buzyCount = 0;
                socket.buzyRooms = [];
                this.pkServices[socket.addr] = socket;
                this.notifyGameSets(socket);
                //通知demon服务器，游戏服准备             
                this.sendDemonMessage();
                config.log('#pipe# new pk service --> ', message.addr);
                return;
            }
            case 'pkState': {
                socket.idleCount = message.idleCount;
                socket.buzyCount = message.buzyCount;
                socket.buzyRooms = message.buzyRooms;
                //告知匹配服玩家，可以转移游戏服
                if (this.app != null) {
                    let dispatch = this.app.dispatchObject;
                    if (dispatch != null) {
                        //已经开启房间信息
                        dispatch.changeRoomStateClose(message.buzyRooms);
                    }
                }
                //通知demon服务器，游戏服准备
                this.sendDemonMessage();
                return;
            }
            case 'pkRoom':{
                let roomInfo = {
                    type: "pkRoom",
                    message: message.roomInfo
                };
                //进行转发数据
                if (this.workIn) this.workIn.sendPipeMessage(roomInfo);
                return;
            }
        }
    };

    //
    // 通知 pk 服务器集群信息
    //
    pipeObjectClass.prototype.notifyServerInfo = function (socket) {
        if (null == this.serverInfo) {
            return;
        }

        this.sendPipeMessage(socket, this.serverInfo);
    };

    //
    // 通知 pk 服务器游戏合集
    //
    pipeObjectClass.prototype.notifyGameSets = function (socket) {
        if (null == this.games) {
            return;
        }

        let gameSetMessage = {};
        gameSetMessage.type = 'gameSet';
        gameSetMessage.games = this.games;

        this.sendPipeMessage(socket, gameSetMessage);
    };

    //
    // 通知 pk 服务器匹配成功的房间
    //
    pipeObjectClass.prototype.notifyNewPkRoom = function (roomObject) {
        let finishMessage = {};
        //设置匹配成功，房间参数消息
        finishMessage.type = 'newPk';
        finishMessage.pkRoomSeq = roomObject.roomSeq;
        finishMessage.maxPlayer = roomObject.maxPlayer;
        finishMessage.gameCode = roomObject.gameCode;
        finishMessage.gameMode = roomObject.gameMode;
        finishMessage.matchKey = roomObject.matchKey;
        finishMessage.server = roomObject.server;
        finishMessage.roomNo = roomObject.roomNo;
        finishMessage.masterFlag = roomObject.masterFlag;
        finishMessage.pkPlayers = [];

        for (let i = roomObject.socketClients.length - 1; i >= 0; i--) {
            let socketClient = roomObject.socketClients[i];
            let playerInfo = {};
            playerInfo.pkPosition = socketClient.vpRoomPosition;
            playerInfo.pkClientSeq = socketClient.vpSeqClient;
            playerInfo.pkMatchParams = socketClient.vpMatchParams;
            finishMessage.pkPlayers[i] = playerInfo;
        }        
        this.sendPipeMessage(roomObject.socketService, finishMessage);
    };

    //
    // 清除 pk 服务套接字的函数
    //
    pipeObjectClass.prototype.clearPkService = function (socket) {
        delete(this.pkServices[socket.addr]);
        config.log('#pipe# pk service cleared --> ', socket.addr);
    };

    //
    // 创建一个套接字的函数
    //
    pipeObjectClass.prototype.createPipeSocket = function (app) {
        let object = this;
        let dispatch = app.dispatchObject;

        object.app = app;

        net.createServer(function (socket) {
            let sendHeartBeatPackage = ()=>{
                let heartBeat = {};
                heartBeat.type = 'heartbeat';      
                object.sendPipeMessage(socket, heartBeat);
            };
            //10s进行心跳检测
            socket.interval = setInterval(sendHeartBeatPackage, 10000);
            // 套接字收到数据处理
            //
            socket.on('data', function (data) {
                let buffer = socket.dataBuffer;
                if (null == buffer) {
                    buffer = data;
                } else {
                    buffer = Buffer.concat([buffer, data]);
                }

                try {
                    while (buffer.length >= 4) {
                        let size = buffer.readUInt32BE(0);
                        let end = size + 4;
                        if (end > buffer.length) {
                            break;
                        }

                        let message = buffer.slice(4, end);
                        message = message.toString('utf8');
                        message = JSON.parse(message);
                        object.handleMessage(socket, message);

                        buffer = buffer.slice(end);
                    }
                } catch (e) {
                    config.log('#pipe# receive data error: ', e);
                }

                socket.dataBuffer = buffer;
            });

            // 套接字关闭处理
            //
            socket.on('close', function (data) {
                config.log('#pipe# pipe-socket close',data);
                clearInterval(socket.interval);
                object.clearPkService(socket);
                dispatch.serviceDisconnected(socket);              
            });

            // 套接字出错处理
            //
            socket.on('error', function (data) {
                config.log('#pipe# pipe-socket shutdown:', data);
                clearInterval(socket.interval);
            });
        }).listen(config.pipePort);
    };

    //
    // 创建当前工作对象
    //
    pipeObject = new pipeObjectClass();
}

//
// 建立工作环境
//
(function () {
    createPipeObject();
})();

//
// 模块声明
//
module.exports = pipeObject;
