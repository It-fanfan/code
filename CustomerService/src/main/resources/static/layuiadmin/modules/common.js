/** layuiAdmin.std-v1.2.1 LPPL License By http://www.layui.com/admin/ */
;layui.define(function (e) {
    var i = (layui.$, layui.layer, layui.laytpl, layui.setter, layui.view, layui.admin);
    i.events.logout = function () {
        i.exit(function () {
			layui.$.get("../user/logout", {}, function(res) {
				layer.msg(res.msg);
				location.href = "../index.html";					
			})            
        })
    }, e("common", {})
});
