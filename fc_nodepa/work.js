//
// 工作对象
//
let fs = require('fs');
let ws = require('ws');
let net = require('net');
let dispatch = require('./dispatch.js');
let config = require('./config.js');
let pipe = require('./pipe.js');
let lzstring = require('./lz-string.min.js');

let workOutObjectClass = null;
let workOutObject = null;
var workInSocket = {
    //交互socket
    socket: null,
    //是否链接
    pipeConnected: false,
    //发送数据
    sendPipeMessage: (message) =>{
        //当前是否链接
        if(!workInSocket.pipeConnected)
            return;
        let content = JSON.stringify(message);
        let buffer = Buffer.from(content,'utf8');
        let base64 = buffer.toString('base64');
        let contentBuffer = Buffer.from(base64,'utf8');
        let headBuffer = Buffer.alloc(4);        
        headBuffer.writeUInt32BE(contentBuffer.length);
        let sendBuffer = Buffer.concat([headBuffer, contentBuffer]);
        workInSocket.socket.write(sendBuffer);        
    },
    //接受数据
    handleMessage: (message)=>{
        pipe.handleDemonMessage(message);
    }
};

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

//进行链接内网交互协议
function createWorkInObject() {
    config.log('启动createWorkInObject');
    if(workInSocket.pipeConnected)
    {
        return;
    }
    let socket = new net.Socket();
    let object = workInSocket;
    
    socket.setEncoding('binary');

    //发送初始协议
    let sendPkInitMessage = ()=>{
        //配置未更新不进行同步
        if(config.updateConfig)
        {
            config.updateConfig = false;
            let message = {
                type:'pkInit',
                ares:config.matchAres, //地区
                port:config.matchPort,//端口
                addr:config.matchHostUrl, //域名
                checkType:config.checkType,//检测类型
                weight:config.weight//权重
            };   
            object.sendPipeMessage(message);
        }       
    }

    // 连接到服务端
    //
    socket.connect(config.demonServicePort, config.demonServiceAddr, function () {
        config.log('#pipe# demon service connected');
        config.updateConfig = true;
        object.pipeConnected = true;
        sendPkInitMessage();
         //3S检测一次配置变更，进行同步服务器
        setInterval(sendPkInitMessage, 3000);
        pipe.initWorkIn(object);
    });

    
   
    // 接收数据
    //
    socket.on('data', function (data) {       
        let buffer = socket.dataBuffer;
        let buf = Buffer.from(data,'ascii');
        if (null == buffer) {            
            buffer = buf;
        } else {
            buffer = Buffer.concat([buffer,buf]);
        }
        try {
            while (buffer.length >= 2) {
                let size = buffer.readUInt16BE(0);
                let end = size + 2;
                if (end > buffer.length) {
                    break;
                }
                let message = buffer.slice(2, end);
                message = message.toString('utf8');
                message = JSON.parse(message);
                object.handleMessage(message);

                buffer = buffer.slice(end);
            }
        } catch (e) {
            config.log('#pipe# error json error: ', e);
        }
        socket.dataBuffer = buffer;        
    });

    // 连接出错
    //
    socket.on('error', function (error) {
        config.log('#pipe# socket error:' + error);
    });

    // 套接字关闭
    //
    socket.on('close', function () {
        config.log('#pipe# socket connection closed');
        object.pipeConnected = false;
         //
        // demon定时器启动
        //
        setTimeout(createWorkInObject, 1000);
    });
    workInSocket.socket = socket;

   
}

//
// 创建一个外部套接字工作对象的函数
//
function createWorkOutObject() {
    workOutObjectClass = function () {
        this.app = null;
    };

    //
    // 处理收到的消息的函数
    //
    workOutObjectClass.prototype.handleMessage = function (socket, message) {
        dispatch.handleMessage(socket, message);
    };

    //
    // 发送一条套接字消息的函数
    //
    workOutObjectClass.prototype.sendOutMessage = function (socket, type, content, frameInfo = null, extra = null) {
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
            if (socket.vpMatchParams.isWebVersion) {
                extraLength = extra.length;
            }
        }

        let totalLength = 4 + 4 + 4 + contentLength + frameInfoLength + extraLength;
        let buffer1 = new ArrayBuffer(totalLength);
        let buffer2 = new Uint16Array(buffer1);
        let i, j, start, end, value;

        totalLength /= 2;
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
            start = contentLength + 6 + frameInfoLength;
            end = totalLength;
            for (i = start, j = 0; i < totalLength; i++ , j += 2) {
                buffer2[i] = extra.readUInt16LE(j);
            }
        }
        if (socket.connectState == 1) {
            try {
                socket.send(buffer2);
            } catch (e) {
                config.log('receive data error: ', e);
            }
        }
        else {
            config.log('error socket', type, socket.connectState, socket.vpSeqClient);
        }
    };

    //
    // 创建一个工作套接字的函数
    //
    workOutObjectClass.prototype.createWorkOutSocket = function (app,port) {
        let object = this;
        let workWSS = null;

        object.app = app;
        let ip = getLocalIp();
        config.log('监控IP',ip);
        workWSS = new ws.Server({ host: ip, port: port });
        workWSS.on('connection', socket => {
            //
            // 客户端已经连接
            //
            dispatch.newClientConnected(socket);

            //
            // 服务端收到消息
            //
            socket.on('message', function (message) {
                let originMessage = JSON.parse(message);
                object.handleMessage(socket, originMessage);
            });

            //
            // 客户端断开
            //
            socket.on('close', function () {
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
(function () {
    createWorkOutObject();
    createWorkInObject();
    dispatch.setPipeObject(pipe);
    dispatch.setWorkObject(workOutObject);   
})();

//
// 模块声明
//
module.exports = { dispatchObject: dispatch, outObject: workOutObject, pipeObject: pipe, workIn: workInSocket };
