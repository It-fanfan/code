window.UIConfig = {
    $: layui,
    /**
     * 性别转换
     * @param d
     * @returns {string}
     */
    formaterSex: (d) => {
        if (d == 0) {
            return '女';
        } else if (d == 1) {
            return '男';
        } else {
            return '其他';
        }
    },

    formaterBool: (d) => {
        if (d == 1) {
            return '小程序';
        } else if (d == 0) {
            return '小游戏';
        } else if (d == 2) {
            return '公众号';
        } else {
            return '其他';
        }
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
    divideByDecimal: (a, b) => {
        if (a == 0 || b == 0 || a == undefined || b == undefined) {
            return '-';
        }
        return (a / b).toFixed(2);
    },

    //百分比
    divideByPercent: (a, b) => {
        if (a == 0 || b == 0 || a == undefined || b == undefined) {
            return '--';
        }
        return (a * 100 / b).toFixed(1) + "%";
    },

    flushCache: () => {
        UIConfig.$.post(operatorurl + "?type=flush", {type: 'flush'}, function (data) {
            UIConfig.$.messager.alert("提示信息", data);
        })
    },

    formatImage: (image) => {
        if (image == null || image.search('http') == -1) {
            return image;
        }
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
    },

    /**
     * 服务器返回的提示信息
     * @param result
     */
    postMsg(result) {
        result && result.msg && layer.msg(result.msg, {icon: result.successed ? 1 : 2});
    },

    /**
     * 弹出警告信息
     * @param msg
     */
    warnMsg(msg) {
        msg && layer.msg(msg, {icon: 0});
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
    const $ = layui.jquery, form = layui.form, element = layui.element, table = layui.table;

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
        //执行重载
        table_reload(data.field);
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

    /**
     * table上操作按钮监听
     */
    table.on('toolbar(table-page)', function (obj) {
        console.log('table.on ->obj:', obj)
        const checkStatus = table.checkStatus(obj.config.id);
        const l = checkStatus.data.length;
        const editData = checkStatus.data[0];
        switch (obj.event) {
            case 'add':
                edit(editData, 'new');
                break;
            case 'delete':
                if (l == 0) {
                    UIConfig.warnMsg('删除：请至少选择一行!');
                } else {
                    deleteData(checkStatus.data);
                }
                break;
            case 'update':
                if (l == 0) {
                    UIConfig.warnMsg('修改：请选择一行！');
                } else if (l > 1) {
                    UIConfig.warnMsg('修改：只能选择一行！');
                } else {
                    UIConfig.warnMsg('修改：开始修改！');
                    edit(editData, 'edit');
                }
                break;
            default:
                break;
        }

        /**
         * 修改
         */
        function edit(editData, type) {
            console.log(operatorurl, type, JSON.stringify(editData))
            $('#form_table').removeClass('layui-hide').addClass('layui-show');
            layui.layer.open({
                type: 1,
                title: type == 'new' ? '新增' : '编辑',
                content: $('#form_table'),
                maxmin: true,
                shade: 0,
                area: ['auto', '600px;'],
                btn: ['确定', '取消'],
                success: (layero, index) => {
                    // 可以在<script></script>内自定义私有的数据处理方法，如果不定义，则按照默认方式初始化数据
                    const existPrivateFunction = typeof privateLoadEditData !== "undefined" && privateLoadEditData !== null;
                    if (existPrivateFunction) {
                        privateLoadEditData(editData, type)
                    } else {
                        form.val("form_table", editData);
                    }
                },
                yes: (index, layero) => {
                    // 提交监听
                    form.on('submit(front-submit)', function (data) {
                        const existPrivateFunction = typeof privateSumbitFormatData !== "undefined" && privateSumbitFormatData !== null;
                        const field = existPrivateFunction ? privateSumbitFormatData(data) : UIConfig.sumbitFormatData(data); //获取提交的字段
                        $.ajax({
                            url: operatorurl + "/" + type,
                            data: field,
                            contentType: "application/json; charset=utf-8",
                            type: 'post',
                            dataType: "json",
                            success: function (result) {
                                UIConfig.postMsg(result);
                                if (result.successed) {
                                    layer.close(index);
                                    table_reload();
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

        /**
         * 删除行
         * @param dataArray
         */
        function deleteData(dataArray) {
            // 在提示操作者删除数据之前先处理好需要删除的数据，建议在自定义删除内容时，累加一下deleteCount
            let deleteObj = {deleteCount: 0};
            // 如果有自定义的方法，则按照自定义的方式处理，否则获取删除数据的ID，且用“,”分隔，在数据库中直接使用in方法删除
            const existPrivateFunction = typeof privateGetDeleteData !== "undefined" && privateGetDeleteData !== null;
            if (existPrivateFunction) {
                privateGetDeleteData(dataArray, deleteObj);
            } else {
                dataArray.forEach(data => {
                    if (deleteObj.deleteIds) {
                        deleteObj.deleteIds += ',' + data.id;
                    } else {
                        deleteObj.deleteIds = data.id;
                    }
                    deleteObj.deleteCount++;
                });
            }
            console.log('deleteObj:', deleteObj);

            if (deleteObj.deleteCount == 0) {
                UIConfig.warnMsg("当前删除操作未选中任何可删除数据！")
                return false;
            }

            layer.confirm('当前选中' + deleteObj.deleteCount + '行，确认删除？', function (index) {
                //向服务端发送删除指令
                $.ajax({
                    url: operatorurl + '/delete',
                    data: JSON.stringify(deleteObj),
                    contentType: "application/json; charset=utf-8",
                    type: 'post',
                    dataType: "json",
                    success: function (result) {
                        UIConfig.postMsg(result);
                        if (result.successed) {
                            layer.close(index);
                            table_reload();
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