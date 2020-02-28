const fs = require("fs");
const jsonminify = require("jsonminify");
//
// 工作对象
//
let configObjectClass = null;
let configObject = null;
var lastStat = null;
let filename = "./config.json";

//
// 创建一个派发房间工作对象的函数
//
function createConfigObject() {
    //初始配置文件
    configObjectClass = function() {
        this.out = true;
        this.initConfig=(callback)=>{
            var stat = fs.statSync(filename);
            if(lastStat != null &&  lastStat.mtime.getTime()  == stat.mtime.getTime())
            {
               return;
            }
            this.log('进行读取配置文件',lastStat && lastStat.mtime,stat.mtime);
            lastStat = stat;
            fs.readFile(filename, "utf-8", (error, data)=>{
                //  用error来判断文件是否读取成功
                if (error){
                    this.log("读取文件失败,内容是" + error.message);
                    return false;
                }
                let min = JSON.minify(data);
                let json = JSON.parse(min);
                for(var key in json){
                    this[key] = json[key];           
                }
                this.updateConfig = true;
                callback && callback();          
            });
        };
        //日志打印
        this.log =(message, ...optionalParams)=>{
            if(!this.out)return;
            let time = new Date().toLocaleString('cn',{hour12:false});            
            console.log(time,message,optionalParams);
        }
        this.log('初始构造逻辑');
        setInterval(this.initConfig, 3000);
    };

    //
    // 创建当前工作对象
    //
    configObject = new configObjectClass();
}

//
// 建立工作环境
//
(function() {
    createConfigObject();
})();

//
// 模块声明
//
module.exports = configObject;
