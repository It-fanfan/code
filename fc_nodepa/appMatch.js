//
// 初始化相关信息
//
var express = require('express');
var config = require('./config.js');

//加载配置文件
config.initConfig(()=>{
var work = require('./work.js');
//
// 构建 express 对象
//
var app = express();

//
// HTTP 服务
//
app.get('/index.html', function (req, res) {
    res.send('fengsong@blazefire.com');
});

//
// SOCKET 服务
//
app.dispatchObject = work.dispatchObject;
work.pipeObject.createPipeSocket(app);
work.outObject.createWorkOutSocket(app, config.matchPort);
//
// 模块声明
//
module.exports = app;    
});

