let config = require('./config.js');
var http = require('http');
let process = require('child_process');
var https = require('https');
let dispatchObject = null;
let workInObject = null;
let workOutObject = null;
let pipeObject = null;

//
// 描述一个房间类
//
module.exports = class VpRoom {
    //
    // 构造函数
    //
    constructor(socketService, dispatch, workIn, workOut, pipe) {
        config.log('pid',socketService.pid);
        this.socketService = socketService;
        this.roomSeq = 0;
        this.state = 'idle';
        this.matchId = null;
        this.selectCounter = config.timeSelectRole;
        this.idleCounter = config.timeMatchMaxTime;
        this.loadingCounter = config.timeLoadingMaxTime;
        this.resetData();
        socketService.vpInRoom = this;
        dispatchObject = dispatch;
        workInObject = workIn;
        workOutObject = workOut;
        pipeObject = pipe;
    }

    //
    //重置房间数据
    //
    resetData() {
        this.maxPlayer = -1;
        this.isPk = false;
        this.gameCode = -1;
        this.gameMode = 0;
        this.matchKey = '';
        this.server = '';
        this.successCounter = -1;
        this.finishCounter = config.timeFinishMaxTime;
        this.pkPlayers = [];
        this.vpRecords = [];
        this.roomClients = [];
        this.emptyCounter = 0;
        this.hangUpCounter = 0;
        this.emptyFinishCounter = 0;
        this.type = -1;
        this.roomNo = null;
        this.masterFlag = false;
        this.allFinished = false;
        this.hangUpState = false;
        this.matchId = null;
        this.upload = false;
        //结算结果信息
        this.finishResult = null;
    }

    //
    // 设置一个房间状态转为加载
    //
    setRoom2Loading(message) {
        let gameInfo = pipeObject.getGame(message.gameCode);
        if (null == gameInfo) {
            config.log('无法获取游戏信息:'+JSON.stringify(message));
            return;
        }
        this.finishResult = null;
        this.state = 'loading';
        this.roomNo = message.roomNo;
        this.masterFlag = message.masterFlag;
        this.roomSeq = message.pkRoomSeq;
        this.maxPlayer = message.maxPlayer;
        this.gameCode = message.gameCode;
        this.gameMode = message.gameMode;
        this.matchKey = message.matchKey;
        this.server = message.server;
        this.pkPlayers = message.pkPlayers;
        this.isPk = gameInfo.isPk;
        this.roleCount = gameInfo.roleCount;
        //是否自动开始
        this.autoSelect = gameInfo.autoSelect;
        //是否复活自动开始
        this.canSelect = gameInfo.canSelect;
        if (this.isPk) {
            this.loadingCounter = config.timeLoadingMaxTime * 2;
        }else{
            this.loadingCounter = config.timeLoadingMaxTime;
        }

        workInObject.sendInMessage(this.socketService, {
            type: 'launch',
            gameCode: this.gameCode,
            gameMode : this.gameMode,
            maxPlayer: message.maxPlayer
        });
    }

    //
    // 设置一个房间状态转为选角色
    //
    setRoom2Selecting() {
        this.sendRoomMessage('loading-status');
        this.state = 'selecting';
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            roomClient.vpGameState = 'selecting';
        }
        this.sendRoomMessage('selecting-status');
    }

    //
    // 设置一个房间状态转为游戏
    //
    setRoom2Playing() {
        this.state = 'playing';
        this.notifyStartGame(null, true);

        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            roomClient.changeClientState('playing');
        }
    }

    //
    // 设置一个房间状态转为成功
    //
    setRoom2Success() {
        this.state = 'success';
        this.makeRoomFinishAccount(null);
    }

    //
    // 设置一个房间状态转为结束
    //
    setRoom2Killed() {       
        //房间关闭
        this.killInRoom();
        if(!this.upload)
        {
            let statusMessage = this.gainRoomFinishMessage(null);
            this.finishResult = statusMessage;
            this.makeRoomFinishAccount(null);
        }
        this.state = 'killed';        
        //房间解散
        this.serviceDisconnected();
        this.resetData();
    }

    set finishResult(result)
    {
        this._finishResult = result;
        if(result != null)
        {
            //通知内服该玩家已经结算
            pipeObject && pipeObject.notifyRoomChanage(this);
        }
    }

    get finishResult()
    {
        return this._finishResult;
    }


    set state(state)
    {
        this._state = state;
        //通知内服该玩家已经结算
        pipeObject && pipeObject.notifyRoomChanage(this);
    }

    get state()
    {
        return this._state;
    }

    //
    // 检查一个客户端是否属于本房间
    //
    clientEntryRoom(socket, message) {
        if (this.roomSeq == message.roomSeq) {
            if (this.type < 0) {
                this.type = message.type;
            }
            //用户Id
            let uid = message.uid;
            let roomClient = null;
            //进行获取当前房间位置信息
            let positions = [0, 1, 2, 3];
            for (let i = 0; i < this.roomClients.length; i++) {
                let client = this.roomClients[i];
                if (!client) continue;
                if (client.vpMatchParams.uid == uid) {
                    roomClient = client;
                }
                positions[client.vpRoomPosition] = -1;
            }
            //移除存在位置
            for (let i = positions.length - 1; i >= 0; i--) {
                if (positions[i] < 0) {
                    positions.splice(i, 1);
                }
            }
            //检测新用户进入
            if (null == roomClient) {
                //没有空位
                if (positions.length < 0) {
                    return false;
                }
                this.matchId = message.matchId;
                //设置位置
                let position = positions[0];
                let clientObject = socket.clientObject;
                clientObject.vpSeqClient = message.clientSeq;
                //优化模式开启状态
                clientObject.optimize = message.optimize;
                clientObject.vpInRoom = this;
                clientObject.vpMatchParams = message;
                clientObject.vpRoomPosition = position;                
                clientObject.socketClient = socket;
                clientObject.vpFirstEntry = true;
                clientObject.vpStart = true;
                this.roomClients.push(clientObject);
                //用户添加记录
                this.vpRecords.push(message.uid);
                pipeObject.notifyPkState(dispatchObject);
                this.vpFirstEntry = true;
                return true;
            }
            if (roomClient.socketClient != null) {
                this.vpFirstEntry = true;
                return true;
            }
            let position = roomClient.vpRoomPosition;
            roomClient.socketClient = socket;
            roomClient.vpPalInited = false;
            roomClient.vpFirstEntry = true;
            socket.clientObject = roomClient;
            workInObject.sendInMessage(this.socketService, { type: 'reconnect', index: position });
            pipeObject.notifyPkState(dispatchObject);
            return true;
        }
        return false;
    }

    //
    // 通知内部服务器开始游戏
    //
    notifyStartGame(singleClient, isStart) {
        let sels = [];
        let singleIndex = -1;

        if (this.isPk) {
            for (let i = 0; i < this.roomClients.length && i < 2; i++) {
                let roomClient = this.roomClients[i];
                if (!roomClient) continue;
                roomClient.vpRoleSelect = i;
            }
        }
        if (singleClient) {
            singleIndex = singleClient.vpRoomPosition;
            for (let i = 0; i < this.roomClients.length; i++) {
                let roomClient = this.roomClients[i];
                if (!roomClient) {
                    continue;
                }
                sels[roomClient.vpRoomPosition] = roomClient.vpRoleSelect;
            }
            // sels[singleClient.vpRoomPosition] = singleClient.vpRoomPosition;
        } else {
            for (let i = 0; i < this.roomClients.length; i++) {
                let roomClient = this.roomClients[i];
                if (!roomClient) {
                    continue;
                }
                sels[roomClient.vpRoomPosition] = roomClient.vpRoleSelect;
            }
        }
        let data = {
            type: 'start',
            isPk: this.isPk ? 1 : 0,
            sel0: null == sels[0] ? -1 : sels[0],
            sel1: null == sels[1] ? -1 : sels[1],
            sel2: null == sels[2] ? -1 : sels[2],
            sel3: null == sels[3] ? -1 : sels[3],
            singleIndex: singleIndex,
            isStart: isStart ? 1 : 0
        };

        //通知游戏开始
        workInObject.sendInMessage(this.socketService, data);
    }

    //
    // 获取用户变更消息
    //
    gainPlayerMessage() {
        let playerInfos = [];
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            if ('playing' != roomClient.vpGameState) {
                continue;
            }
            let info = { uid: roomClient.vpMatchParams.uid };
            if (this.isPk) {
                info.hangUp = false;
            } else {
                info.hangUp = roomClient.vpHangUpCount <= 0;
            }
            playerInfos[roomClient.vpRoomPosition] = info;
        }
        this.vpFirstEntry = false;
        return playerInfos;
    }

    //
    // 获取选角色消息
    //
    gainSelectingMessage(singleClient, isStart, counter) {
        let statusMessage = {};
        let allSelected = true;

        statusMessage.state = this.state;
        statusMessage.selectCounter = counter || this.selectCounter;
        statusMessage.players = [];
        statusMessage.haltCount = 0;

        if (null != singleClient) {
            statusMessage.selectCounter = counter || singleClient.vpSelectingCounter;
        }
        if (this.autoSelect) {
            for (let i = 0; i < this.roomClients.length; i++) {
                let roomClient = this.roomClients[i];
                if (!roomClient) continue;
                roomClient.vpRoleSelect = roomClient.vpRoomPosition;
            }
        }else{
            if(null != singleClient && this.canSelect)
            {
                singleClient.vpRoleSelect = singleClient.vpRoomPosition;
            }
        }
       

        if (statusMessage.selectCounter <= 0) {
            let roleValues = [0, 1, 2, 3, 4, 5, 6, 7, 8];
            for (let i = 0; i < this.roomClients.length && i < this.roleCount; i++) {
                let roomClient = this.roomClients[i];
                if (!roomClient) continue;
                if (roomClient.vpRoleSelect >= 0 && roomClient.vpRoleSelect < this.roleCount) {
                    roleValues[roomClient.vpRoleSelect] = -1;
                }
            }
            for (let i = roleValues.length - 1; i >= 0; i--) {
                if (roleValues[i] < 0) {
                    roleValues.splice(i, 1);
                }
            }
            for (let i = 0, j = 0; i < this.roomClients.length && i < this.roleCount; i++) {
                let roomClient = this.roomClients[i];
                if (roomClient && roomClient.vpRoleSelect < 0) {
                    roomClient.vpRoleSelect = roleValues[j];
                    j++;
                }
            }
            statusMessage.haltCount = 100;
        }

        for (let i = 0; i < this.roomClients.length; i++) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            let params = roomClient.vpMatchParams;
            if (params == null) continue;
            let player = {};
            if (roomClient.vpRoleSelect < 0) {
                allSelected = false;
            }
            player.uid = params.uid;
            player.name = params.name;
            player.avatarUrl = params.avatarUrl;
            player.avatarFrame = params.avatarFrame;
            player.roleSelect = roomClient.vpRoleSelect;
            statusMessage.players.push(player);
        }
        //单人模式下，用户选择手柄后，操作
        if (singleClient && singleClient.vpRoleSelect >= 0) {
            if(singleClient.vpGameState != 'playing')
            {
                statusMessage.selectCounter = 0;
                singleClient.vpSelectingCounter = config.timeSelectRole;
                statusMessage.haltCount = 5;
                singleClient.changeClientState('playing');
                singleClient.vpPalInited = false;
                this.notifyStartGame(singleClient, isStart);
                return statusMessage;
            }
            return null;           
        }
        //多人模式，全部选中手柄
        if (allSelected) {
            this.selectCounter = config.timeSelectRole;
            if (statusMessage.haltCount <= 0) {
                statusMessage.haltCount = 40;
            }
            statusMessage.selectCounter = 0;
            this.setRoom2Playing();
        }
        return statusMessage;
    }

    //
    // 发送房间消息
    //
    sendRoomMessage(type, message) {
        switch (type) {
            case 'loading-status':
                {
                    let statusMessage = {};
                    statusMessage.state = this.state;
                    statusMessage.gameMode = this.gameMode;
                    statusMessage.process = this.loadingCounter;
                    statusMessage.isSingle = false;
                    //加载进度条为0，不进行下发
                    if(this.loadingCounter < 0)
                        break;
                    for (let i = this.roomClients.length - 1; i >= 0; i--) {
                        let roomClient = this.roomClients[i];
                        if (roomClient) workOutObject.sendOutMessage(roomClient, type, statusMessage);
                    }
                }
                break;
            case 'selecting-status':
                {
                    if (this.selectCounter <= 0) {
                        //重置
                        this.selectCounter = 0;
                    }
                    let statusMessage = this.gainSelectingMessage(null, true);
                    if(statusMessage == null) break;
                    for (let i = this.roomClients.length - 1; i >= 0; i--) {
                        let roomClient = this.roomClients[i];
                        if (!roomClient) continue;
                        workOutObject.sendOutMessage(roomClient, type, statusMessage);
                    }
                }
                break;
            case 'talk':
                {
                    for (let i = this.roomClients.length - 1; i >= 0; i--) {
                        let roomClient = this.roomClients[i];
                        if (!roomClient) continue;
                        roomClient.sendClientMessage(type, message);
                    }
                }
                break;
        }
    }

    //
    //内服游戏结束，并上报游戏结果
    //
    killInRoom() {
        config.log('#vp# room no player killed --> ', this.roomSeq);

        workInObject.sendInMessage(this.socketService, {
            type: 'kill',
            roomSeq: this.roomSeq
        });
        if(this.socketService.pid)
        {
            process.exec("taskkill /F /pid "+this.socketService.pid,function(err, stdout, stderr){});
        }
        if(this.socketService.processId)
        {
            process.exec("taskkill /F /pid "+this.socketService.processId,function(err, stdout, stderr){});
        }
        //检测玩家是否上报数据
        this.makeRoomFinish();       
    }

    //
    //检测房间玩家托管时间过长进行解散房间
    //
    checkRoomHangWait()
    {
        let canRecover = true;
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            if (!roomClient.vpHangUpWaitState) {
                canRecover = false;
                break;
            }
        }
        if(!canRecover) {
            this.hangUpCounter = 0;
            this.hangUpState = false;
            return;
        }
        this.hangUpCounter++;      
        if(this.hangUpCounter < config.timeHangWaitMaxTime) return;
        if(this.hangUpState)
        {
            config.log('#vp# room hangUpState no player killed --> ', this.roomSeq);
            //进行内部关闭
            this.setRoom2Killed();
            return;
        } 
        this.hangUpState = true;

    }

    //
    //检测房间是否完成
    //
    checkRoomAllFinish() {
        //检测用户都已经完成，并且已经断开连接
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            //未断socket
            if (roomClient.socketClient != null) return;
            //玩家拥有未存在玩家
            if (roomClient.vpGameState != 'finish') return;
        }
        this.emptyFinishCounter++;
        if (this.emptyFinishCounter > 3) {
            this.emptyFinishCounter = 0;
            config.log('#vp# room emptyFinishCounter no player killed --> ', this.roomSeq);
            //进行内部关闭
            this.setRoom2Killed();
        }
    }
    //
    // 检测房间是否可以回收的函数
    //
    checkRoomRecover() {
        if ('loading' == this.state) {
            return;
        }

        let len = this.roomClients.length;
        if (len <= 0) {
            //进行内部关闭
            config.log('#vp# room checkRoomRecover no player killed --> '+this.state);
            this.setRoom2Killed();
            return;
        }
        this.checkRoomHangWait();
        this.checkRoomAllFinish();
        let canRecover = true;
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            if (null != roomClient.socketClient) {
                canRecover = false;
                break;
            }
        }

        if (canRecover) {
            this.emptyCounter++;
            if (this.emptyCounter >= config.timeRetainWithRoomEmpty) {
                config.log('#vp# room emptyCounter no player killed --> ', this.roomSeq);
                //进行内部关闭
                this.setRoom2Killed();
            }
        }else{
            this.emptyCounter = 0;
        }
    }

    //
    // 进行一次 http 请求的函数
    //
    doHttpRequest(url, message) {
        try {
            let content = JSON.stringify(message);
            let object = this;
            let callback = res => {
                let _data = '';
                res.on('data', chunk => {
                    _data += chunk;
                });
                res.on('end', () => {
                    this.httpFinish(res.statusCode, _data);
                });
            };
            let options = {
                host: config.httpHost,
                port: config.httpPort,
                path: url,
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Content-Length': content.length
                }
            };
            let req = http.request(options, callback);
            req.write(content);
            req.end();
            return;
        } catch (e) {
            config.log('#doHttpRequest# err --> ', e);
        }
    }

    //
    // 联网结束的函数
    //
    httpFinish(statusCode, dataResult) {
        if (200 == statusCode) {
            config.log('http:',dataResult);
            if(!dataResult) return;
            let resultObject = JSON.parse(dataResult);
            if (resultObject.result == 'success') {
                for (let i = resultObject.users.length - 1; i >= 0; i--) {
                    let user = resultObject.users[i];
                    for (let j = this.roomClients.length - 1; j >= 0; j--) {
                        let roomClient = this.roomClients[j];
                        if (!roomClient) continue;
                        if (
                            roomClient.vpFinishPosition < 0 &&
                            roomClient.vpMatchParams &&
                            roomClient.vpMatchParams.uid == user.uid
                        ) {
                            roomClient.vpFinishPosition = user.index;
                            roomClient.vpHighstMark = user.highMark;
                            roomClient.vpUserMark = user.mark;
                            break;
                        }
                    }
                }
            }
        }
    }

    //
    // 获取房间结算消息
    //
    gainRoomFinishMessage(singleClient) {
        let message = {};
        let users = [];

        message.roundBrief = '???';
        message.allFinish = this.allFinished;
        message.gameMode = this.gameMode;
        let len = 0;
        if (this.isPk || null == singleClient) {
            for (let i = this.roomClients.length - 1; i >= 0; i--) {
                let roomClient = this.roomClients[i];
                if (!roomClient) continue;
                len++;
                message.roundBrief = roomClient.vpMatchParams.roundBrief;
                users[i] = roomClient.gainClientFinishMessage();
            }
        } else {
            for (let i = this.roomClients.length - 1; i >= 0; i--) {
                let roomClient = this.roomClients[i];
                if (!roomClient) continue;
                if (singleClient == roomClient) {
                    message.roundBrief = roomClient.vpMatchParams.roundBrief;
                    users[0] = roomClient.gainClientFinishMessage();
                    break;
                }
            }
        }
        //容错判断，pk
        if (this.isPk && this.gameMode == 0)
        {
            message.allFinish = false;
        }
        message.isPk = this.isPk;
        if (singleClient) {
            message.single = true;
        } else {
            message.single = len <= 1;
        }
        message.users = users;
        return message;
    }

    //中断结算分数
    makeRoomFinish() {
        //检测已经上报
        if(this.upload){
            config.log('makeRoomFinish upload');
            return;
        }
         
        //中断上报分数，PK模式:不需要上传
        if (this.isPk && this.gameMode == 0) return;
        if (this.state == 'success') {
            config.log('makeRoomFinish success');
            return;
        }
        let upload = false;
        let message = {};
        let reqUsers = [];
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            if (roomClient.vpGameState != 'finish' && roomClient.vpMatchParams) {
                upload = true;
                let userInfo = {};
                userInfo.uid = roomClient.vpMatchParams.uid;
                userInfo.lose = roomClient.vpRevive;
                userInfo.mark = roomClient.getUserFinishMark(this.isPk, this.state);
                reqUsers.push(userInfo);
            }
        }
        if (upload) {
            message.matchKey = this.matchKey;
            message.gameCode = this.gameCode;
            message.gameMode = this.gameMode;
            message.users = reqUsers;
            message.type = this.type;
            message.allFinish = this.allFinished;
            message.matchId = this.matchId;
            this.doHttpRequest('/persieService/doranking', message);
            this.upload = true;
        }
    }
    //
    // 进行房间结算的函数
    //
    makeRoomFinishAccount(singleClient) { 
        let message = {};
        let reqUsers = [];
        if (null == singleClient) {
            for (let i = this.roomClients.length - 1; i >= 0; i--) {
                let roomClient = this.roomClients[i];
                //客户端不存在
                if (!roomClient) continue;
                //非PK模式，客户端断开
                if (!this.isPk && roomClient.socketClient == null) continue;
                if (roomClient.vpMatchParams) {
                    let userInfo = {};
                    userInfo.uid = roomClient.vpMatchParams.uid;
                    userInfo.mark = roomClient.getUserFinishMark(this.isPk, this.state);
                    userInfo.lose = roomClient.vpRevive;
                    reqUsers.push(userInfo);
                }
            }
        } else {
            for (let i = this.roomClients.length - 1; i >= 0; i--) {
                let roomClient = this.roomClients[i];
                if (!roomClient) continue;
                if (singleClient == roomClient) {
                    let userInfo = {};
                    userInfo.uid = roomClient.vpMatchParams.uid;
                    userInfo.mark = roomClient.getUserFinishMark(this.isPk, this.state);
                    userInfo.lose = roomClient.vpRevive;
                    reqUsers.push(userInfo);
                    break;
                }
            }
        }

        message.matchKey = this.matchKey;
        message.gameCode = this.gameCode;
        message.gameMode = this.gameMode;
        message.users = reqUsers;
        message.type = this.type;
        message.matchId = this.matchId;
        //是否通关
        message.allFinish = this.allFinished;
        this.doHttpRequest('/persieService/doranking', message);
        this.upload = true;
    }

    //
    // 处理 loading 状态
    //
    handleUpdateLoading() {
        this.loadingCounter--;
        if (this.loadingCounter < 0) {
            if (this.isPk) {
                this.setRoom2Playing();
            } else {
                this.setRoom2Selecting();
            }
        } else {
            this.sendRoomMessage('loading-status');
        }
    }

    //
    // 处理 selecting 状态
    //
    handleUpdateSelecting() {
        this.selectCounter--;
        this.sendRoomMessage('selecting-status');
    }

    //
    // 处理 playing 状态
    //
    handleUpdatePlaying() {
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            roomClient.handleUpdate();
        }
    }

    //
    // 处理 success 状态
    //
    handleUpdateSuccess() {
        this.successCounter--;
        if (this.successCounter > 0) {
            return;
        }
        //下发客户端结算数据
        let statusMessage = this.gainRoomFinishMessage(null);
        this.finishResult = statusMessage;
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            workOutObject.sendOutMessage(roomClient, 'finish-status', statusMessage);
        }       
        this.state = 'finish';
        //结算转移到等待服
        dispatchObject.changeRoom(this);
    }

    //
    //发送用户详情数据状态
    //
    handlePlayerState() {
        let playerInfos = [];
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            let params = roomClient.vpMatchParams;
            let info = {
                uid: params.uid,
                name: params.name,
                avatarUrl: params.avatarUrl,
                avatarFrame: params.avatarFrame,
                position: roomClient.vpRoomPosition
            };
            playerInfos.push(info);
        }
        let type = 'player-state';
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            workOutObject.sendOutMessage(roomClient, type, playerInfos);
        }
    }

    //
    //处理完成状态
    //
    handleUpdateFinish() {
        this.finishCounter--;
        if (this.finishCounter < 0) {
            config.log('#vp# room handleUpdateFinish no player killed --> ', this.roomSeq);
            this.setRoom2Killed();
            return;
        }
        let status = 'wait';
        if (this.isPk && this.roomClients.length == this.maxPlayer) {
            let ok = 0;
            for (let i = this.roomClients.length - 1; i >= 0; i--) {
                let roomClient = this.roomClients[i];
                if (!roomClient || roomClient.socketClient == null || roomClient.againStatus == 'fail') {
                    status = 'fail';
                    break;
                }
                //玩家还未确认
                if (roomClient.againStatus == 'success') {
                    ok++;
                }
            }
            if (status != 'fail') {
                if (ok == this.maxPlayer) status = 'finish';
                else if (ok > 0) status = 'success';
            }
        } else {
            status = 'fail';
        }
        let statusMessage = {
            counter: this.finishCounter,
            userCount: this.roomClients.length,
            status: status
        };
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient) continue;
            statusMessage.status = status;
            if (status == 'success' && roomClient.againStatus == 'success') {
                statusMessage.status = 'wait';
            }
            workOutObject.sendOutMessage(roomClient, 'save-status', statusMessage);
        }
        if (status == 'finish' || status == 'fail') {
            config.log('#vp# room handleUpdateFinish 2 no player killed --> ', this.roomSeq);
            //直接转移房间
            this.successCounter++;
            if(this.successCounter > 1)
            {
                this.successCounter = 0;
                this.setRoom2Killed();
            }            
        }
    }

    //
    // 处理定时操作
    //
    handleUpdate() {
        switch (this.state) {
            case 'loading': {
                this.handleUpdateLoading();
                return;
            }
            case 'selecting': {
                this.handleUpdateSelecting();
                return;
            }
            case 'playing': {
                this.handleUpdatePlaying();
                return;
            }
            case 'success': {
                this.handleUpdateSuccess();
                return;
            }
            case 'finish': {
                this.handleUpdateFinish();
                return;
            }
        }
    }

    //
    // 删除一个客户端的函数
    //
    removeClient(clientObject) {
        clientObject.socketClient = null;
        if (this.isPk) {
            //PK模式游戏中，不进行删除客户端
            if (this.state == 'playing') return;
        } else {
            //闯关模式，玩家未输不进行删除客户端
            if ('finish' != clientObject.vpGameState || !clientObject.vpUserLost) return;
        }
        //移除房间内链接
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (roomClient == clientObject) {
                this.roomClients.splice(i, 1);
                break;
            }
        }
        //通知内服该玩家已经结算
        pipeObject.notifyPkState(dispatchObject);
        this.handlePlayerState();
    }

    //
    // 房间对应的游戏服务已经断开
    //
    serviceDisconnected() {
        for (let i = this.roomClients.length - 1; i >= 0; i--) {
            let roomClient = this.roomClients[i];
            if (!roomClient || null == roomClient.socketClient) {
                continue;
            }
            roomClient.socketClient.close();
        }
        this.roomClients = [];
    }
};
