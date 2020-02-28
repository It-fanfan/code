let config = require('./config.js');
let dispatchObject = null;
let workInObject = null;
let workOutObject = null;
let pipeObject = null;

//
// 描述一个客户端类
//
module.exports = class VpClient {
    //
    // 构造函数
    //
    constructor(socketClient, dispatch, workIn, workOut, pipe) {
        this.socketClient = socketClient;
        this.vpSeqInner = Math.floor(Math.random() * 100000000);
        this.resetData();
        socketClient.clientObject = this;
        dispatchObject = dispatch;
        workInObject = workIn;
        workOutObject = workOut;
        pipeObject = pipe;
    }

    //重置数据
    resetData() {
        this.vpSeqClient = 0;
        this.vpInRoom = null;
        this.vpMatchParams = null;
        this.vpGameState = 'loading';
        //用户索引
        this.vpUserIndex = -1;
        //是否通关
        this.vpUserLost = false;
        //游戏得分
        this.vpUserMark = 0;
        //复活次数
        this.vpRevive = 0;
        this.vpRoleSelect = -1;
        this.vpRoomPosition = -1;
        this.vpFinishPosition = -1;
        this.vpHighstMark = -1;
        this.vpLosingCounter = config.timeLosingMaxTime;
        this.vpSelectingCounter = config.timeSelectRole;
        this.vpFinishHalt = true;
        this.vpPalInited = false;
        this.vpAlreadyRevived = false;
        this.vpHangUpCount = config.timeHangUpMaxTime;
        this.vpHangUpWaitState = false;
        this.vpFirstEntry = true;
        this.vpFirstEntryTimer = 1;  
        this.vpStatFlowTotal = 0;
        this.vpStatFlowCount = 0;
        this.loadingCounter = config.timeLoadingMaxTime * 2;
        this.selectCounter = config.timeSelectRole;
        this.historyState = 'none';      
        this.relive = 100;
        //当前玩家的结算信息
        this.finishResult = null;
        this.isSingle = false;
        //设置是否启动优化模式
        this.optimize = false;
    }
    
    set finishResult(result)
    {
        this._finishResult = result;
        if(result != null)
        {
            //通知内服该玩家已经结算
            pipeObject && pipeObject.notifyRoomChanage(this.vpInRoom);
        }
    }

    get finishResult()
    {
        return this._finishResult;
    }
    set vpStart(state)
    {
        this._vpStart = state;
    }

    get vpStart(){
        return this._vpStart;
    }

    set vpFirstEntry(state)
    {
        this._vpFirstEntry = state;
        if(state){
            this.vpFirstEntryTimer = 1;
        }
    }
    get vpFirstEntry(){
        return this._vpFirstEntry;
    }

    //客户端复活
    changeRevived() {
        this.vpAlreadyRevived = false;
        //前3个
        if(this.vpRevive < 10)
        {
            this.relive = 100 + this.vpRevive * 10;
        }else
        {
            this.relive = 200;
        }
        this.vpRevive++;       
        config.log('user revived',this.vpMatchParams.uid, this.vpRevive, this.vpUserIndex);
    }

    //
    // 客户端状态切换
    //
    changeClientState(state) {
        if('ad' == state)
        {
            this.vpGameState = 'ad';
            this.vpAdCounter = config.videoPauseTime;
            return;
        }
        if ('losing' == state) {
            this.vpGameState = 'losing';
            this.vpLosingCounter = config.timeLosingMaxTime;
            return;
        }
        if ('success' == state) {
            this.vpGameState = 'success';
            return;
        }
        if ('finish' == state) {
            this.vpGameState = 'finish';
            this.vpFinishHalt = true;
            if(this.vpInRoom) this.vpInRoom.makeRoomFinishAccount(this);
            return;
        }
        if ('killed' == state) {
            this.vpGameState = 'killed';
            if (null != this.socketClient) {
                this.socketClient.close();
            }
            return;
        }
        if ('selecting' == state) {
            this.vpRoleSelect = -1;
            this.vpGameState = 'selecting';
            this.vpSelectingCounter = config.timeSelectRole;
            return;
        }
        if ('playing' == state) {
            this.vpGameState = 'playing';
            this.vpStart = false;
            this.vpAlreadyRevived = true;
            return;
        }
    }

    //
    // 获取结算消息
    //
    gainClientFinishMessage() {
        let message = {};
        message.uid = this.vpMatchParams.uid;
        message.name = this.vpMatchParams.name;
        message.avatarUrl = this.vpMatchParams.avatarUrl;
        message.avatarFrame = this.vpMatchParams.avatarFrame;
        message.position = this.vpFinishPosition;
        message.lost = this.vpUserLost;
        message.mark = this.vpUserMark;
        message.vpRevive = this.vpRevive;
        message.highstMark = this.vpHighstMark;
        return message;
    }

    //
    // 发送客户端消息
    //
    sendClientMessage(type, message = null) {
        let roomObject = this.vpInRoom;
        if (null == roomObject) {
            return;
        }
        switch (type) {
            //客户端结算过程
            case 'finish-status': {
                if (this.vpFinishHalt) {
                    this.vpFinishHalt = false;
                    return;
                }
                if(this.socketClient == null)return;
                let statusMessage = roomObject.gainRoomFinishMessage(this);
                this.finishResult = statusMessage;
                workOutObject.sendOutMessage(this, type, statusMessage);
                //检测房间内是否存在玩家
                let len = roomObject.roomClients.length;
                if(len <= 1)
                {
                    this.vpUserLost = true;
                }
                //用户退出该房间
                this.clientDisconnected();
                return;
            }
            //客户端个体复活结果
            case 'revive-result': {
                let reviveResult = {
                    result: 'ok'
                };

                workOutObject.sendOutMessage(this, type, reviveResult);
                this.vpHangUpCount = config.timeHangUpMaxTime;
                this.vpHangUpWaitState = false;
                return;
            }
            // 客户端个体选角色
            case 'singlesel-status': {
                if (this.vpSelectingCounter < 0) {
                    this.vpSelectingCounter = 0;
                }
                let statusMessage = roomObject.gainSelectingMessage(this,this.vpStart,this.vpSelectingCounter);
                workOutObject.sendOutMessage(this, type, statusMessage);
                return;
            }
            //客户端单个玩家加载
            case 'loading-status':{
                this.loadingCounter--;
                if(this.loadingCounter >= 0)
                {
                    let statusMessage = {};
                    statusMessage.state = this.state;
                    statusMessage.isSingle = true;
                    this.isSingle = true;
                    statusMessage.gameMode = this.vpInRoom.gameMode;
                    statusMessage.process = this.loadingCounter;
                    workOutObject.sendOutMessage(this, 'loading-status', statusMessage);
                    this.changeClientState('selecting');
                }
                return;
            }
            //客户端个体出局
            case 'losing-status': {
                if (this.vpLosingCounter < 0) {
                    this.vpLosingCounter = 0;
                    this.changeClientState('finish');
                    return;
                }
                let statusMessage = {};
                statusMessage.loseCount = this.vpLosingCounter;
                statusMessage.relive = this.relive;
                statusMessage.reliveCount = this.vpRevive;
                workOutObject.sendOutMessage(this, type, statusMessage);
                return;
            }
            //客户端发送talk
            case 'talk': {
                workOutObject.sendOutMessage(this, type, message);
                return;
            }
        }
    }

    getUserFinishMark(isPk,state)
    {
        if(!isPk)
        {
          return this.vpUserMark;
        }
        if(state == 'playing') return 0;
        return this.vpUserLost ? 0 : 1;
        
    }

    //
    // 选择一个角色
    //
    selectPlayer(message) {        
        let roomObject = this.vpInRoom;
        if (null == roomObject) {
            return;
        }
        if (roomObject.selectCounter <= 0) {
            return;
        }
        let selectIndex = message.selectIndex;
        for (let i = 0; i < roomObject.roomClients.length; i++) {
            let otherClient = roomObject.roomClients[i];
            if(!otherClient) continue;
            if (otherClient.vpSeqClient == this.vpSeqClient) {
                continue;
            }
            if (selectIndex == otherClient.vpRoleSelect) {
                selectIndex = -1;
                break;
            }
        }
        this.vpRoleSelect = selectIndex;
        if (message.isSingle) {
            this.sendClientMessage('singlesel-status');
        } else {
            roomObject.sendRoomMessage('selecting-status');
        }       
    }

    //
    // 进行一次按键
    //
    keyInput(message) {
        let roomObject = this.vpInRoom;
        if (null == roomObject) {
            return;
        }
        if (roomObject.state != 'playing') {
            return;
        }
        if (this.vpRoomPosition < 0) {
            return;
        }

        let keyMessage = {
            type: 'key',
            index: this.vpRoomPosition,
            up: message.up ? 1 : 0,
            down: message.down ? 1 : 0,
            left: message.left ? 1 : 0,
            right: message.right ? 1 : 0,
            a: message.a ? 1 : 0,
            b: message.b ? 1 : 0,
            c: message.c ? 1 : 0,
            d: message.d ? 1 : 0,
            e: message.e ? 1 : 0,
            f: message.f ? 1 : 0
        };

        this.vpHangUpCount = config.timeHangUpMaxTime;
        this.vpHangUpWaitState = false;

        workInObject.sendInMessage(roomObject.socketService, keyMessage);

        if (!roomObject.isPk) {
            for (let i = roomObject.roomClients.length - 1; i >= 0; i--) {
                let roomClient = roomObject.roomClients[i];
                if (roomClient == this) {
                    continue;
                }
                if (roomClient.vpHangUpCount > 0) {
                    continue;
                }
                keyMessage.index = roomClient.vpRoomPosition;
                workInObject.sendInMessage(roomObject.socketService, keyMessage);
            }
        }
    }

    //
    // 玩家出局后操作
    //
    loseThen(message) {
        let cmd = message.cmd;
        switch(cmd)
        {
            case 'quit':{
                this.changeClientState('finish');
            }break;
            case 'revive':{
                this.changeClientState('selecting');
                this.sendClientMessage('revive-result');
                this.sendClientMessage('singlesel-status');
            }break;
            case 'pause':{
                this.historyState = this.vpGameState;
                this.changeClientState('ad');       
            }break;
            case 'pause-stop':{
                if(this.historyState != 'none')
                    this.vpGameState = this.historyState;
            }break;
        }
       
    }
    get vpGameState()
    {
        return this._vpGameState;
    }

    set vpGameState(state){
        this._vpGameState = state;
    }

    //
    //设置玩家死亡状态
    //
    settingPlayerLose(index,offset,datas)
    {
        let j = this.vpUserLost;
        this.vpUserLost = datas[offset + (5 + index)] > 0;
        if(j != this.vpUserLost && this.vpUserLost){
            config.log('玩家死亡',this.vpMatchParams.uid,Date.now());
        }
    }

    //
    // 玩家数值设置
    //
    settingPlayerDatas(index, offset, datas) {
        let i = this.vpUserIndex,e= this.vpUserMark;
        this.vpUserIndex = index;
        this.settingPlayerLose(index,offset,datas);
        this.vpUserMark = datas[offset + (1 + index)];
    }

    //检测socket是否断开
    clearSocket()
    {
        if(this.vpUserLost && this.socketClient == null)
        {       
            this.vpInRoom.removeClient(this);
        }
    }
    
    //
    // 处理客户端消息
    //
    handleClient(type, message) {
        switch (type) {     
             
            case 'select': {
                this.selectPlayer(message);
                return;
            }
            case 'key': {
                this.keyInput(message);
                return;
            }
            case 'lose-then': {
                this.loseThen(message);
                return;
            }
            case 'talk': {
                this.vpInRoom && this.vpInRoom.sendRoomMessage('talk',message);
            }
            return;
            case 'again':{
                this.againStatus = message.status ? "success":"fail";
                return;
            }        
        }
    }

    //
    // 处理定时操作
    //
    handleUpdate() {
        switch(this.vpGameState)
        {
            case 'finish':{
                this.vpHangUpCount--;
                if (this.vpHangUpCount < 0) {
                    this.vpHangUpCount = 0;
                    this.vpHangUpWaitState = true;
                }
                this.sendClientMessage('finish-status');
            }return; 
            case 'ad':{
                this.vpAdCounter--;
                if(this.vpAdCounter <= 0)
                {
                    this.vpGameState = 'losing';
                }
            }return;
            case 'losing':{
                this.vpLosingCounter--; 
                this.sendClientMessage('losing-status');
            }return;
            case 'loading':{
                this.sendClientMessage('loading-status');
            }return;
            case 'selecting':{
                this.vpSelectingCounter--;
                this.sendClientMessage('singlesel-status');
            }return;
            case 'playing':{
                this.vpHangUpCount--;
                if (this.vpHangUpCount < 0) {
                    this.vpHangUpCount = 0;
                    this.vpHangUpWaitState = true;
                }
                if(this.vpFirstEntry)
                {
                    this.vpFirstEntryTimer--;
                    if(this.vpFirstEntryTimer < 0)
                    {
                        this.vpInRoom.handlePlayerState();
                        this.vpFirstEntry = false;
                    }
                }          
            }return;
        }
       
    }

    //
    // 用户已经断开连接
    //
    clientDisconnected() {
        let roomObject = this.vpInRoom;
        if (null == roomObject) {
            return;
        }
        roomObject.removeClient(this);
    }
};
