//
// 工作对象
//
let fs = require('fs');
let ws = require('ws');
let net = require('net');
const zlib = require('zlib');
let dispatch = require('./dispatch.js');
let config = require('./config.js');
let pipe = require('./pipe.js');
let lzstring = require('./lz-string.min.js');

let workOutObjectClass = null;
let workOutObject = null;
let workInObjectClass = null;
let workInObject = null;

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
// 创建一个内部套接字工作对象的函数
//
function createWorkInObject() {
    workInObjectClass = function() {
        this.app = null;
        this.frameIndex = 0;
    };

    //
    // 处理收到的消息的函数
    //
    workInObjectClass.prototype.handleMessage = function(socket, message, extra) {
        let object = this;
        let roomObject = socket.vpInRoom;
        if (null != roomObject) {
            let isPk = roomObject.isPk && roomObject.gameMode == 0;
            let frameMessage = message;

            let headerCount = 5;
            let allFinished = false;
            let baseCount = frameMessage.readUInt32LE(0);
            let patternCount = frameMessage.readUInt32LE(4);
            let tileNewTotalCount = frameMessage.readUInt32LE(8);
            let colorCount = frameMessage.readUInt32LE(12);
            let colorNewCount = frameMessage.readUInt32LE(16);
            let basePatternCount = headerCount + baseCount + patternCount;

            // 基础消息构建
            //
            let basePatternArray = new Uint32Array(basePatternCount);
            let tileNewTotalArray = new Uint32Array(tileNewTotalCount);
            let colorArray = new Uint32Array(colorCount);
            let colorNewArray = new Uint32Array(colorNewCount);
            let i,
                j,
                readOffset = 0;

            for (i = 0; i < basePatternCount; i++, readOffset += 4) {
                basePatternArray[i] = frameMessage.readUInt32LE(readOffset);
            }
            for (i = 0; i < tileNewTotalCount; i++, readOffset += 4) {
                tileNewTotalArray[i] = frameMessage.readUInt32LE(readOffset);
            }
            for (i = 0; i < colorCount; i++, readOffset += 4) {
                colorArray[i] = frameMessage.readUInt32LE(readOffset);
            }
            for (i = 0; i < colorNewCount; i++, readOffset += 4) {
                colorNewArray[i] = frameMessage.readUInt32LE(readOffset);
            }

            let resourceUserMax = tileNewTotalArray[0];
            let offsetResourceIndex = 1;
            let offsetResourceSize = offsetResourceIndex + resourceUserMax;
            let offsetTileNewInfo = offsetResourceSize + resourceUserMax;
            let clientLength = roomObject.roomClients.length;

            // 构建全部成功的逻辑
            //
            if ('playing' == roomObject.state) {
                if (isPk) {
                    let allsuccess = basePatternArray[headerCount + 10];
                    if (allsuccess > 0) {
                        allFinished = true;
                        roomObject.allFinished = allFinished;
                        allsuccess = 0;
                        if (clientLength > 1) {
                            let lost0 = basePatternArray[headerCount + (5 + 0)] > 0;
                            let lost1 = basePatternArray[headerCount + (5 + 1)] > 0;
                            if (lost0 || lost1) {
                                allsuccess = 1;
                                for (i = 0; i < clientLength; i++) {
                                    let roomClient = roomObject.roomClients[i];
                                    if (roomClient.vpRoomPosition == 0) roomClient.vpUserLost = lost0;
                                    else if (roomClient.vpRoomPosition == 1) roomClient.vpUserLost = lost1;
                                }
                                if (roomObject.successCounter < 0) {
                                    roomObject.successCounter = 2;
                                    roomObject.setRoom2Success();
                                }
                            }
                        }
                        basePatternArray[headerCount + 10] = allsuccess;
                    }else if(roomObject.hangUpState)
                    {
                        allFinished = true;
                        allsuccess = 1;
                        roomObject.allFinished = allFinished;
                        for (i = 0; i < clientLength; i++) {
                            let roomClient = roomObject.roomClients[i];
                            roomClient.vpUserLost = 1;
                        }
                        if (roomObject.successCounter < 0) {
                            roomObject.successCounter = 2;
                            roomObject.setRoom2Success();
                        }
                    }
                } else {
                    let allsuccess = basePatternArray[headerCount + 10];
                    if (allsuccess > 0) {
                        allFinished = true;
                        roomObject.allFinished = allFinished;
                        if (roomObject.successCounter < 0) {
                            roomObject.successCounter = 2;
                            roomObject.setRoom2Success();
                        }
                    }
                }
            }

            // 用户有调整消息构建
            //
            let playerInfos = roomObject.gainPlayerMessage();
            for (i = 0; i < clientLength; i++) {
                let roomClient = roomObject.roomClients[i];
                if (!roomClient) continue;
                let index = roomClient.vpRoomPosition;

                //检测玩家已经结束,非PK模式下调整
                if ('finish' == roomClient.vpGameState && !roomObject.isPk) {
                    //获取玩家死亡信息，并不在进行设置分数
                    // 玩家数值设置
                    //
                    let lose = roomClient.vpUserLost;
                    roomClient.settingPlayerLose(index, headerCount, basePatternArray);
                    //客户端结算后，游戏角色死亡
                    if (lose != roomClient.vpUserLost && roomClient.vpUserLost) {
                        roomClient.clearSocket();
                    }
                    continue;
                }

                // 状态过滤
                //
                if ('playing' != roomClient.vpGameState) {
                    continue;
                }

                let jsonMessage = {};
                let sizeNewTile = tileNewTotalArray[offsetResourceSize + index];
                let offsetNewTile = tileNewTotalArray[offsetResourceIndex + index];
                let palleteCount = colorNewCount;
                let palleteArray = colorNewArray;

                if (sizeNewTile <= 0 && !roomClient.vpPalInited) {
                    continue;
                }

                // 玩家数值设置
                //
                roomClient.settingPlayerDatas(index, headerCount, basePatternArray);

                // 第一帧初始化调色板
                //
                if (!roomClient.vpPalInited) {
                    palleteCount = colorCount;
                    palleteArray = colorArray;
                    roomClient.vpPalInited = true;
                }

                // 构造字节消息体
                //
                let moveOffset = 0;
                let byteMessage = new Uint32Array(basePatternCount + sizeNewTile + palleteCount);
                for (j = 0; j < basePatternCount; j++, moveOffset++) {
                    byteMessage[moveOffset] = basePatternArray[j];
                }
                if (sizeNewTile > 0) {
                    offsetNewTile += offsetTileNewInfo;
                    for (j = 0; j < sizeNewTile; j++, moveOffset++) {
                        byteMessage[moveOffset] = tileNewTotalArray[offsetNewTile + j];
                    }
                }
                for (j = 0; j < palleteCount; j++, moveOffset++) {
                    byteMessage[moveOffset] = palleteArray[j];
                }
                // 消息体设置
                //
                jsonMessage.allFinished = allFinished;
                jsonMessage.userIndex = roomClient.vpUserIndex;
                jsonMessage.userLost = roomClient.vpUserLost;
                jsonMessage.reliveCount = roomClient.vpRevive;
                jsonMessage.relive = roomClient.relive;
                jsonMessage.colorCount = palleteCount;
                jsonMessage.tileNewCount = sizeNewTile;
                jsonMessage.playerInfos = playerInfos;

                // 为用户复活打补丁
                //
                if (roomClient.vpAlreadyRevived) {
                    if (jsonMessage.userLost) {
                        jsonMessage.userLost = false;
                    } else {
                        roomClient.changeRevived();
                    }
                }
                //PK模式不进行转场
                if (isPk)
                {
                    jsonMessage.userLost = false;
                }
                // 实际帧消息发出
                //
                if(roomClient.optimize)
                {                   
                    let buf = Buffer.from(byteMessage.buffer);
                    // let uint32Array =  new Uint32Array(buf.buffer, buf.byteOffset, buf.byteLength / Uint32Array.BYTES_PER_ELEMENT);                  
                    //进行压缩
                    zlib.gzip(buf,function(err,res){  
                        zipRes = res;
                        // config.log('压缩帧结果',buf.length,zipRes.length,Math.floor(zipRes.length*100/buf.length)+"%");
                        // let buffer = new Uint32Array(zipRes);
                        config.writeFile('frame'+object.frameIndex++,byteMessage,buf,zipRes); 
                        workOutObject.sendOutMessage(roomClient, 'frame', jsonMessage, zipRes, extra);
                        // zlib.unzip(zipRes,function(err,b){
                        //     let uint32Array =  new Uint32Array(b.buffer, b.byteOffset, b.byteLength / Uint32Array.BYTES_PER_ELEMENT);                  
                        //     console.log('解压结果：',uint32Array,byteMessage);
                        // });
                    });
                }else{
                    workOutObject.sendOutMessage(roomClient, 'frame', jsonMessage, byteMessage, extra);          
                }           

                // 用户状态操作
                //
                if (jsonMessage.userLost) {
                    roomClient.changeClientState('losing');
                }
            }
        }
    };

    //
    // 处理发送的消息的函数
    //
    workInObjectClass.prototype.sendInMessage = function(socket, message) {
        let content = JSON.stringify(message);
        let contentBuffer = Buffer.from(content);
        let headBuffer = Buffer.alloc(4);
        headBuffer.writeUInt32LE(content.length);
        let sendBuffer = Buffer.concat([headBuffer, contentBuffer]);

        socket.write(sendBuffer);
    };

    //
    // 创建一个工作套接字的函数
    //
    workInObjectClass.prototype.createWorkInSocket = function(app) {
        let object = this;
        object.app = app;
        net.createServer(function(socket) {
            // 新空闲房间连接成功
            //
            dispatch.newRoomConnected(socket);
            socket.vpDataBuffer = null;

            // 套接字收到数据处理
            //
            socket.on('data', function(data) {
                let buffer = socket.vpDataBuffer;
                if (null == buffer) {
                    buffer = data;
                } else {
                    buffer = Buffer.concat([buffer, data]);
                }

                try {
                    while (buffer.length >= 4) {
                        let size = buffer.readUInt32LE(0);
                        let end = size + 4;
                        if (end > buffer.length) {
                            break;
                        }
						let processId = buffer.readUInt32LE(4);
                        let messageEnd = 12 + buffer.readUInt32LE(8);
                        let message = buffer.slice(12, messageEnd);
                        let extraEnd = messageEnd + 4 + buffer.readUInt32LE(messageEnd);
                        let extra = buffer.slice(messageEnd + 4, extraEnd);

						socket.processId = processId;
                        object.handleMessage(socket, message, extra);
                        buffer = buffer.slice(end);
                    }
                } catch (e) {
                    config.log('receive data error: ', e);
                }

                socket.vpDataBuffer = buffer;
            });

            // 套接字关闭处理
            //
            socket.on('close', function(data) {
                if (null != socket.vpInRoom) {
                    config.log('in-socket close:', socket.vpInRoom.roomSeq);
                }

                // dispatch.serviceDisconnected(socket);
            });

            // 套接字出错处理
            //
            socket.on('error', function(data) {
                if (null != socket.vpInRoom) {
                    config.log('in-socket shutdown:', socket.vpInRoom.roomSeq, data);
                }
            });
        }).listen(8888, '127.0.0.1');
    };

    //
    // 创建当前工作对象
    //
    workInObject = new workInObjectClass();
}

//
// 创建一个外部套接字工作对象的函数
//
function createWorkOutObject() {
    workOutObjectClass = function() {
        this.app = null;
    };

    //
    // 发送一条套接字消息的函数
    //
    workOutObjectClass.prototype.sendOutMessage = function(
        clientObject,
        type,
        content,
        frameInfo = null,
        extra = null
    ) {
        let socket = clientObject.socketClient;
        if (null == socket) {
            return;
        }

        let message = {};
        message.type = type;
        message.data = content;
        message = JSON.stringify(message);

        let contentLength = message.length * 2;
        let frameInfoLength = 0;
        let extraLength = 0;

        if (null != frameInfo) {
            frameInfoLength = frameInfo.length * 4;
        }
        if (null != extra) {
            if (clientObject.vpMatchParams && clientObject.vpMatchParams.isWebVersion) {
                extraLength = extra.length / 2;
            }
        }

        let totalLength = 4 + 4 + 4 + contentLength + frameInfoLength + extraLength;
        let buffer1 = new ArrayBuffer(totalLength);
        let buffer2 = new Uint16Array(buffer1);
        let i, j, start, end, value;

        contentLength /= 2;
        frameInfoLength /= 2;
		extraLength /= 2;

        buffer2[0] = (contentLength >> 16) & 0xffff;
        buffer2[1] = contentLength & 0xffff;
        buffer2[2] = (frameInfoLength >> 16) & 0xffff;
        buffer2[3] = frameInfoLength & 0xffff;
        buffer2[4] = (extraLength >> 16) & 0xffff;
        buffer2[5] = extraLength & 0xffff;

        for (i = contentLength + 5; i >= 6; i--) {
            buffer2[i] = message.charCodeAt(i - 6);
        }

        if (frameInfoLength > 0) {
            start = contentLength + 6;
            end = start + frameInfoLength;
            for (i = start, j = 0; i < end; i += 2, j++) {
                value = frameInfo[j];
                buffer2[i] = (value >> 16) & 0xffff;
                buffer2[i + 1] = value & 0xffff;
            }
        }

        if (extraLength > 0) {
            start = 6 + contentLength + frameInfoLength;
			extraLength *= 2;

			for (i = 0 , j = start ; i < extraLength ; i += 2 , j ++) {
				let offset = i * 2;
				let v0 = extra.readUInt16LE(offset) >> 8;
				let v1 = extra.readUInt16LE(offset + 2) >> 8;

				buffer2[j] = ((v0 << 8) & 0xff00) | (v1 & 0xff);
			}
        }

        //socket.vpStatFlowCount ++;
        //socket.vpStatFlowTotal += buffer2.length * 2;
        //if(socket.vpStatFlowCount % 60 == 59) {
        //	config.log("#flow info# average per frame bytes --> " ,
        //			Math.floor(socket.vpStatFlowTotal / socket.vpStatFlowCount));
        //}
        try {
            socket.send(buffer2);
        } catch (error) {
            console.error('暂移除socket:', error);
            clientObject.socketClient = null;
        }
    };

    //
    // 创建一个工作套接字的函数
    //
    workOutObjectClass.prototype.createWorkOutSocket = function(app,port) {
        let object = this;
        let workWSS = null;

        object.app = app;
        workWSS = new ws.Server({ host: getLocalIp(), port: port });
        workWSS.on('connection', socket => {
            //
            // 客户端已经连接
            //
            dispatch.newClientConnected(socket);

            //
            // 服务端收到消息
            //
            socket.on('message', function(message) {
                let originMessage = JSON.parse(message);
                dispatch.handleMessage(socket, originMessage);
            });

            //
            // 客户端断开
            //
            socket.on('close', function() {
                dispatch.clientDisconnected(socket);
            });
        });
    };

    //
    // 创建当前工作对象
    //
    workOutObject = new workOutObjectClass();
}

//
// 建立工作环境
//
(function() {
    createWorkInObject();
    createWorkOutObject();
    dispatch.setPipeObject(pipe);
    dispatch.setWorkObject(workInObject, workOutObject);
})();

//
// 模块声明
//
module.exports = { dispatchObject: dispatch, inObject: workInObject, outObject: workOutObject, pipeObject: pipe };
