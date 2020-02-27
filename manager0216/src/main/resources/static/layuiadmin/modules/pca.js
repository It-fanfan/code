layui.define(['table', 'form', 'layer', 'jquery', 'element'], function (exports) {
    var $ = layui.$;
    var form = layui.form;

    var pca = {};
    pca.keys = {};
    pca.ckeys = {};

    pca.init = function (province, city, area, initprovince, initcity, initarea, form) {//jQuery选择器, 省-市-区
        var form = layui.form;
        if (!province || !$(province).length) return;
        $(province).html('');
        $(province).append('<option selected>全部</option>');
        for (var i in citys) {
            $(province).append('<option>' + citys[i].name + '</option>');
            pca.keys[citys[i].name] = citys[i];
        }
        form.render('select');
        if (initprovince) $(province).next().find('[lay-value="' + initprovince + '"]').click();
        if (!city || !$(city).length) return;
        pca.formRender(city);
        form.on('select(province)', function (data) {
            var cs = pca.keys[data.value];
            $(city).html('');
            $(city).append('<option>全部</option>');
            if (cs) {
                cs = cs.city;
                for (var i in cs) {
                    $(city).append('<option>' + cs[i].name + '</option>');
                    pca.ckeys[cs[i].name] = cs[i];
                }
                $(city).find('option:eq(1)').attr('selected', true);
            }
            form.render('select');
            $(city).next().find('.layui-this').removeClass('layui-this').click();
            pca.formHidden('province', data.value);
            $('.pca-label-province').html(data.value);//此处可以自己修改 显示的位置, 不想显示可以直接去掉
        });
        if (initprovince) $(province).next().find('[lay-value="' + initprovince + '"]').click();
        if (initcity) $(city).next().find('[lay-value="' + initcity + '"]').click();
        if (!area || !$(area).length) return;
        pca.formRender(area);
        form.on('select(city)', function (data) {
            var cs = pca.ckeys[data.value];
            $(area).html('');
            $(area).append('<option>全部</option>');
            if (cs) {
                cs = cs.area;
                for (var i in cs) {
                    $(area).append('<option>' + cs[i] + '</option>');
                }
                $(area).find('option:eq(1)').attr('selected', true);
            }
            form.render('select');
            $(area).next().find('.layui-this').removeClass('layui-this').click();
            pca.formHidden('city', data.value);
            $('.pca-label-city').html(data.value);	//此处可以自己修改 显示的位置, 不想显示可以直接去掉
        });
        form.on('select(area)', function (data) {
            pca.formHidden('area', data.value);
            $('.pca-label-area').html(data.value);	//此处可以自己修改 显示的位置, 不想显示可以直接去掉
        });
        if (initprovince) $(province).next().find('[lay-value="' + initprovince + '"]').click();
        if (initcity) $(city).next().find('[lay-value="' + initcity + '"]').click();
        if (initarea) $(area).next().find('[lay-value="' + initarea + '"]').click();
    }

    pca.formRender = function (obj) {
        $(obj).html('');
        $(obj).append('<option>全部</option>');
        form.render('select');
    }

    pca.formHidden = function (obj, val) {
        if (!$('#pca-hide-' + obj).length)
            $('body').append('<input id="pca-hide-' + obj + '" type="hidden" value="' + val + '" />');
        else
            $('#pca-hide-' + obj).val(val);
    }

    var citys =
        [
            {
                "name": "北京",
                "city": [{
                    "name": "北京",
                    "area": ["东城区", "西城区", "崇文区", "宣武区", "朝阳区", "丰台区", "石景山区", "海淀区", "门头沟区", "房山区", "通州区", "顺义区", "昌平区", "大兴区", "平谷区", "怀柔区", "密云县", "延庆县"]
                }]
            },
            {
                "name": "天津",
                "city": [{
                    "name": "天津",
                    "area": ["和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "塘沽区", "汉沽区", "大港区", "东丽区", "西青区", "津南区", "北辰区", "武清区", "宝坻区", "宁河县", "静海县", "蓟县"]
                }]
            },
            //
            // //中间部分忽略，demo中已给出
            // ......

            {"name": "钓鱼岛", "city": [{"name": "钓鱼岛", "area": ["钓鱼岛"]}]}
        ];


    exports('pca', pca);

});
