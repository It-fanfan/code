window.UIConfig = {
    $: layui,
    // 格式化
    formaterSex: (d) => {
        if (d == 0)
            return '女';
        else if (d == 1)
            return '男';
        else
            return '其他';
    },
    formaterBool: (d) => {
        if (d == 1)
            return '小程序';
        else if (d == 0)
            return '小游戏';
        else if (d == 2)
            return '公众号';
        else
            return '其他';
    },
    //时间判断
    formatViewEffectTime: (startTime, endTime, real) => {
        let start = new Date(Date.parse(startTime)).getTime();
        let end = new Date(Date.parse(endTime)).getTime();
        let now = new Date().getTime();
        if (end < now)
            return '<span style="color:red">失效</span>';
        if (end - now > 365 * 24 * 60 * 60 * 1000)
            return '<span style="color: blue">永久</span>';
        return real;
    }, // 刷新缓存
    //非正整数，保留2位小数点
    divideByDecimal:(a,b)=>{
        if(a == 0 || b == 0 || a == undefined || b == undefined)
            return '-';
        return (a/b).toFixed(2);
    },
    //百分比
    divideByPercent:(a,b)=>{
        if(a == 0 || b == 0 || a == undefined || b == undefined)
            return '--';
        return (a*100/b).toFixed(1) + "%";
    },
    flushCache: () => {
        UIConfig.$.post(operatorurl + "?type=flush", {
            type: 'flush'
        }, function (data) {
            UIConfig.$.messager.alert("提示信息", data);
        })
    },
    formatImage: (image) => {
        if (image == null || image.search('http') == -1)
            return image;
        return `<div><img src="${image}"></div>`;
    },

    formatData: (data) => {
        let val = {};
        UIConfig.$.each(data, (index, element) => {
            if (typeof element === "object") {
                UIConfig.$.each(element, (i, e) => {
                    val[`${index}$${i}`] = e;
                });
            } else {
                val[index] = element;
            }
        });
        return val;
    },

    formatSelect: (map) => {
        let from = "";
        console.log("Map值", map);
        console.log("Map长度", map.size);
        if (map.get("form") === "select") {
            from += ("<select name=\"" + map.get("select") + "\" id=\"" + map.get("select") + "\" lay-filter=\"pSelect\">");
            for (let i = 0; i < map.size - 2; i++) {
                from += ("<option value=\"" + map.get(i)[1] + "\">" + map.get(i)[0] + "</option>")
            }
            from += ("</select>");
        } else if (map.get("form") === "input") {
            layui.$("#" + map.get("input")).remove();
            console.log(map.get("input"));
            let typevalue = map.get("parType") === undefined ? 'text' : map.get("parType").toString();
            from = "<input type=\"" + typevalue + "\" name=\"" + map.get("input") + "\" id=\"" + map.get("input") + "\" autocomplete=\"off\" class=\"layui-input\">";
        }
        console.log("from", from);
        return from;
    },
    cascade: (master, field) => {
        let val = layui.$(master).val();
        for (let i = 0; i < field.length; i++) {
            if (val === field[i]) {
                layui.$(field[i]).show();
            } else {
                layui.$(field[i]).hide();
            }
        }
    },


    sumbitFormatData: (data) => {
        let val = {};
        let field = data.field;
        UIConfig.$.each(field, (index, element) => {
            if (index.indexOf('$') >= 0) {
                let _v = index.split('$');
                let k = _v[0],
                    v = _v[1];
                let _obj = val[k];
                if (_obj == undefined)
                    _obj = val[k] = {};
                _obj[v] = element;
            } else {
                val[index] = element;
            }
        });
        return JSON.stringify(val);
    }
}

layui.config({
    base: '../layuiadmin/' //静态资源所在路径
}).extend({
    index: 'lib/index' //主入口模块
}).use(['index', 'table', 'form'], function () {

    /**
     * 监听事件
     */
    var $ = layui.jquery,
        form = layui.form,
        element = layui.element,
        table = layui.table;

    // 表格重载
    function table_reload(where_data) {
        if (where_data instanceof Object) {
            where_data = JSON.stringify(where_data);
        }
        table.reload('table-page', {
            where: { //设定异步数据接口的额外参数，任意设
                'searchData': where_data,
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    }


    // 搜索查询
    form.on('submit(front-search)', function (data) {
        var field = data.field;
        //执行重载
        table_reload(field);
        return false;
    });


    //监听排序事件
    table.on('sort(table-page)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        table.reload('table-page', {
            initSort: obj, //记录初始排序，如果不设的话，将无法标记表头的排序状态。
            where: { //请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
                sort: obj.field, //排序字段
                order: obj.type //排序方式
            },
        });
    });


    table.on('toolbar(table-page)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        var l = checkStatus.data.length;
        var row = checkStatus.data[0];

        switch (obj.event) {
            case 'add':
                add();
                break;
            case 'delete':
                if (l == 0) {
                    layer.msg('请选择一行');
                } else if (l > 1) {
                    layer.msg('只能删除一行哦');
                } else {
                    deleteRow(row);
                }
                break;
            case 'update':
                if (l == 0) {
                    layer.msg('请选择一行');
                } else if (l > 1) {
                    layer.msg('只能选择一行');
                } else {
                    layer.msg('编辑');
                    update();
                }
                break;


        }

        // 修改
        function update() {
            $('#form_table').removeClass('layui-hide').addClass('layui-show');
            layui.layer.open({
                type: 1,
                title: '编辑',
                content: $('#form_table'),
                maxmin: true,
                shade: 0,
                area: ['auto', '450px'],
                btn: ['确定', '取消'],
                success: (layero, index) => {
                    // id
                    let checkStatus = table.checkStatus('table-page');
                    let data = UIConfig.formatData(checkStatus.data[0]);
                    form.val("form_table", data);

                },
                yes: (index, layero) => {
                    // 提交监听
                    form.on('submit(front-submit)', function (data) {
                        var field = UIConfig.sumbitFormatData(data); //获取提交的字段
                        var url = operatorurl + "/edit";
                        $.ajax({
                            url: url,
                            data: field,
                            contentType: "application/json; charset=utf-8",
                            type: 'post',
                            dataType: "json",
                            success: function (result) {
                                if (result.code === 200) {
                                    layer.msg('修改成功');
                                    layer.close(index);
                                    table_reload();
                                }else if (result.code === 408){
                                    layer.msg('AppID重复，操作失败')
                                }else if (result.code === 409){
                                    layer.msg('产品名称重复，操作失败')
                                }else if (result.code === 410){
                                    layer.msg('游戏代号重复，操作失败')
                                }else{
                                    layer.msg('修改失败')
                                }
                            }
                        });
                    });

                    $('#front-submit').trigger('click');
                },
                end: () => {
                    $("#form_table")[0].reset();
                    $('#form_table').removeClass('layui-show').addClass('layui-hide')
                    return false;
                }
            });
        }

        // 新增
        function add() {
            // 表单元素
            $('#form_table').removeClass('layui-hide').addClass('layui-show');

            layui.layer.open({
                type: 1,
                content: $('#form_table'),
                title: '添加',
                maxmin: true,
                area: ['auto', '450px'],
                btn: ['确定', '取消'],
                success: function (layero, index) {
                    var checkStatus = table.checkStatus('#table-page');
                    let data = UIConfig.formatData(checkStatus.data[0]);
                    form.val("form_table", data);
                },
                yes: function (index, layero) {
                    // 提交监听
                    form.on('submit(front-submit)', data => {
                        var field = UIConfig.sumbitFormatData(data); //获取提交的字段
                        var url = operatorurl + "/new";
                        $.ajax({
                            url: url,
                            data: field,
                            contentType: "application/json; charset=utf-8",
                            type: 'post',
                            dataType: "json",
                            success: function (result) {
                                if (result.code === 200) {
                                    layer.msg('新增成功');
                                    layer.close(index);
                                    table_reload();
                                }else if (result.code === 408){
                                    layer.msg('AppID重复，操作失败')
                                }else if (result.code === 409){
                                    layer.msg('产品名称重复，操作失败')
                                }else if (result.code === 410){
                                    layer.msg('游戏代号重复，操作失败')
                                } else {
                                    layer.msg('新增失败')
                                }
                            }
                        });

                    });

                    $('#front-submit').trigger('click');
                },
                end: function () {
                    $("#form_table")[0].reset();
                    $('#form_table').removeClass('layui-show').addClass('layui-hide')
                    return false;
                }
            });
        }

        // 删除行
        function deleteRow(row) {
            layer.confirm('真的删除行么', function (index) {
                //向服务端发送删除指令
                var url = operatorurl + "/delete";
                $.ajax({
                    url: url,
                    data: JSON.stringify(row),
                    contentType: "application/json; charset=utf-8",
                    type: 'post',
                    dataType: "json",
                    success: function (result) {
                        if (result.code === 200) {
                            layer.msg('删除成功');
                            layer.close(index);
                            table_reload();
                        } else {
                            layer.msg('删除失败')
                        }
                    }
                });
            });
        }
    });
})

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[
            k]).substr(("" + o[k]).length)));
    return fmt;
}