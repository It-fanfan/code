<%@ page contentType="text/html; charset=utf-8" import="servlet.config.CmServletConfig" %>
<%

	if(!CmServletConfig.checkLogined(request))
	{
		response.sendRedirect("login.jsp");
		return;
	}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>热血街机 - 管理后台</title>
    <link href="libui/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />  
    <link rel="stylesheet" type="text/css" id="mylink"/>   
    <script src="libui/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>    
    <script src="libui/ligerUI/js/ligerui.all.js" type="text/javascript"></script>  
    <script src="libui/jquery.cookie.js"></script>
    <script src="libui/json2.js"></script>
    <script src="indexdata.js" type="text/javascript"></script>
	<script type="text/javascript">
 
            var tab = null;
            var accordion = null;
            var tree = null;
            var tabItems = [];
            $(function ()
            {

                //布局
                $("#layout1").ligerLayout({
                    leftWidth: 190,
                    height: '100%',
                    heightDiff: -34,
                    space: 4,
                    onHeightChanged: f_heightChanged,
                    onLeftToggle: function ()
                    {
                        tab && tab.trigger('sysWidthChange');
                    },
                    onRightToggle: function ()
                    {
                        tab && tab.trigger('sysWidthChange');
                    }
                });

                var height = $(".l-layout-center").height();

		//Tab
		tab = $("#framecenter").ligerTab({
			height: height,
			showSwitchInTab : true,
			showSwitch: true,
			onAfterAddTabItem: function (tabdata)
			{
				tabItems.push(tabdata);
				saveTabStatus();
			},
			onAfterRemoveTabItem: function (tabid)
			{ 
				for (var i = 0; i < tabItems.length; i++)
				{
					var o = tabItems[i];
					if (o.tabid == tabid)
					{
						tabItems.splice(i, 1);
						saveTabStatus();
						break;
					}
				}
			},
			onReload: function (tabdata)
			{
			}
		});
		document.tabObject = tab;


                //面板
                $("#accordion1").ligerAccordion({
                    height: height - 24, speed: null
                });

                $(".l-link").hover(function ()
                {
                    $(this).addClass("l-link-over");
                }, function ()
                {
                    $(this).removeClass("l-link-over");
                });
                //树
                $("#tree1").ligerTree({
                    data : indexdata,
                    checkbox: false,
                    slide: false,
                    nodeWidth: 120,
                    attribute: ['nodename', 'url'],
                    render : function(a){
                        if (!a.isnew) return a.text;
                        return '<a href="' + a.url + '" target="_blank">' + a.text + '</a>';
                    },
                    onSelect: function (node)
                    {
                        if (!node.data.url) return;
                        if (node.data.isnew)
                        { 
                            return;
                        }
                        var tabid = $(node.target).attr("tabid");
                        if (!tabid)
                        {
                            tabid = new Date().getTime();
                            $(node.target).attr("tabid", tabid)
                        } 
                        f_addTab(tabid, node.data.text, node.data.url);
                    }
                });

                function openNew(url)
                { 
                    var jform = $('#opennew_form');
                    if (jform.length == 0)
                    {
                        jform = $('<form method="post" />').attr('id', 'opennew_form').hide().appendTo('body');
                    } else
                    {
                        jform.empty();
                    } 
                    jform.attr('action', url);
                    jform.attr('target', '_blank'); 
                    jform.trigger('submit');
                };


                tab = liger.get("framecenter");
                accordion = liger.get("accordion1");
                tree = liger.get("tree1");
                $("#pageloading").hide();

                css_init();
                pages_init();
            });
            function f_heightChanged(options)
            {  
                if (tab)
                    tab.addHeight(options.diff);
                if (accordion && options.middleHeight - 24 > 0)
                    accordion.setHeight(options.middleHeight - 24);
            }
            function f_addTab(tabid, text, url)
            {
                tab.addTabItem({
                    tabid: tabid,
                    text: text,
                    url: url,
                    callback: function ()
                    {
                    }
                });
            }
            function showCodeView(src)
            {
                $.ligerDialog.open({
                    title : '源码预览',
                    url: 'dotnetdemos/codeView.aspx?src=' + src,
                    width: $(window).width() *0.9,
                    height: $(window).height() * 0.9
                });

            }
            var skin_links = {
                "aqua": "libui/ligerUI/skins/Aqua/css/ligerui-all.css",
                "gray": "libui/ligerUI/skins/Gray/css/all.css",
                "silvery": "libui/ligerUI/skins/Silvery/css/style.css",
                "gray2014": "libui/ligerUI/skins/gray2014/css/all.css"
            };
            function pages_init()
            {
                var tabJson = $.cookie('liger-home-tab'); 
                if (tabJson)
                { 
                    var tabitems = JSON2.parse(tabJson);
                    for (var i = 0; tabitems && tabitems[i];i++)
                    { 
                        f_addTab(tabitems[i].tabid, tabitems[i].text, tabitems[i].url);
                    } 
                }
            }
            function saveTabStatus()
            { 
                $.cookie('liger-home-tab', JSON2.stringify(tabItems));
            }
            function css_init()
            {
                var css = $("#mylink").get(0), skin = getQueryString("skin");
                $("#skinSelect").val(skin);
                $("#skinSelect").change(function ()
                { 
                    if (this.value)
                    {
                        location.href = "index.htm?skin=" + this.value;
                    } else
                    {
                        location.href = "index.htm";
                    }
                });

               
                if (!css || !skin) return;
                skin = skin.toLowerCase();
                $('body').addClass("body-" + skin); 
                $(css).attr("href", skin_links[skin]); 
            }
            function getQueryString(name)
            {
                var now_url = document.location.search.slice(1), q_array = now_url.split('&');
                for (var i = 0; i < q_array.length; i++)
                {
                    var v_array = q_array[i].split('=');
                    if (v_array[0] == name)
                    {
                        return v_array[1];
                    }
                }
                return false;
            }
            function attachLinkToFrame(iframeId, filename)
            { 
                if(!window.frames[iframeId]) return;
                var head = window.frames[iframeId].document.getElementsByTagName('head').item(0);
                var fileref = window.frames[iframeId].document.createElement("link");
                if (!fileref) return;
                fileref.setAttribute("rel", "stylesheet");
                fileref.setAttribute("type", "text/css");
                fileref.setAttribute("href", filename);
                head.appendChild(fileref);
            }
            function getLinkPrevHref(iframeId)
            {
                if (!window.frames[iframeId]) return;
                var head = window.frames[iframeId].document.getElementsByTagName('head').item(0);
                var links = $("link:first", head);
                for (var i = 0; links[i]; i++)
                {
                    var href = $(links[i]).attr("href");
                    if (href && href.toLowerCase().indexOf("ligerui") > 0)
                    {
                        return href.substring(0, href.toLowerCase().indexOf("libui") );
                    }
                }
            }
     </script> 
<style type="text/css"> 
    body,html{height:100%;}
    body{ padding:0px; margin:0;   overflow:hidden;}  
    .l-link{ display:block; height:26px; line-height:26px; padding-left:10px; text-decoration:underline; color:#333;}
    .l-link2{text-decoration:underline; color:white; margin-left:2px;margin-right:2px;}
    .l-layout-top{background:#102A49; color:White;}
    .l-layout-bottom{ background:#E5EDEF; text-align:center;}
    #pageloading{position:absolute; left:0px; top:0px; background:white url('loading.gif') no-repeat center; width:100%; height:100%;z-index:99999;}
    .l-link{ display:block; line-height:22px; height:22px; padding-left:16px;border:1px solid white; margin:4px;}
    .l-link-over{ background:#FFEEAC; border:1px solid #DB9F00;} 
    .l-winbar{ background:#2B5A76; height:30px; position:absolute; left:0px; bottom:0px; width:100%; z-index:99999;}
    .space{ color:#E7E7E7;}
    /* 顶部 */ 
    .l-topmenu{ margin:0; padding:0; height:31px; line-height:31px; background:url('libui/images/top.jpg') repeat-x bottom;  position:relative; border-top:1px solid #1D438B;  }
    .l-topmenu-logo{ color:#E7E7E7; padding-left:35px; line-height:26px;background:url('libui/images/topicon.gif') no-repeat 10px 5px;}
    .l-topmenu-welcome{  position:absolute; height:24px; line-height:24px;  right:30px; top:2px;color:#070A0C;}
    .l-topmenu-welcome a{ color:#E7E7E7; text-decoration:underline} 
     .body-gray2014 #framecenter{
        margin-top:3px;
    }
      .viewsourcelink {
         background:#B3D9F7;  display:block; position:absolute; right:10px; top:3px; padding:6px 4px; color:#333; text-decoration:underline;
    }
    .viewsourcelink-over {
        background:#81C0F2;
    }
    .l-topmenu-welcome label {color:white;
    }
    #skinSelect {
        margin-right: 6px;
    }
 </style>
</head>
<body style="padding:0px;background:#EAEEF5;">  
<div id="pageloading"></div>  
	<div id="topmenu" class="l-topmenu">
		<div class="l-topmenu-logo">热血街机 - 管理后台</div>
		<div class="l-topmenu-welcome">
			<label> 皮肤切换：</label>
			<select id="skinSelect">
				<option value="aqua">默认</option> 
				<option value="silvery">Silvery</option>
				<option value="gray">Gray</option>
				<option value="gray2014">Gray2014</option>
			</select>
			<a href="index.aspx" class="l-link2">服务器版本</a>
			<span class="space">|</span>
			<a href="http://www.ligerui.com/pay.html" class="l-link2" target="_blank">捐赠</a> 
					<span class="space">|</span>
			<a href="http://www.ligerui.com/server.html" class="l-link2" target="_blank">服务支持</a> 
		</div> 
	</div>
	<div id="layout1" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
        <div position="left"  title="功能菜单" id="accordion1">
			<div title="服务器 & 管理" class="l-scroll">
				<div style=" height:7px;"></div>
					<a class="l-link" href="javascript:f_addTab('newserver','添加服务器','module_config.jsp?tab=newserver&type=server&sub=add_mod')">添加服务器</a>
					<a class="l-link" href="javascript:f_addTab('listserver','服务器列表','module_list.jsp?tab=listserver&type=server&sub=list_mod')">服务器列表</a>
					<a class="l-link" href="javascript:f_addTab('newmanager','添加管理员','module_config.jsp?tab=newmanager&type=manager&sub=add_mod')">添加管理员</a>
					<a class="l-link" href="javascript:f_addTab('listmanager','管理员列表','module_list.jsp?tab=listmanager&type=manager&sub=list_mod')">管理员列表</a>
			</div>
			<div title="用户 & 游戏">
				<div style=" height:7px;"></div>
					<a class="l-link" href="javascript:f_addTab('newgame','添加游戏','module_config.jsp?tab=newgame&type=game&sub=add_mod')">添加游戏</a>
					<a class="l-link" href="javascript:f_addTab('listgame','游戏列表','module_list.jsp?tab=listgame&type=game&sub=list_mod')">游戏列表</a>
					<a class="l-link" href="javascript:f_addTab('newgameset','添加游戏合集','module_config.jsp?tab=newgameset&type=gameset&sub=add_mod')">添加游戏合集</a>
					<a class="l-link" href="javascript:f_addTab('listgameset','游戏合集列表','module_list.jsp?tab=listgameset&type=gameset&sub=list_mod')">游戏合集列表</a>
					<a class="l-link" href="javascript:f_addTab('newround','添加比赛日','module_config.jsp?tab=newround&type=round&sub=add_mod')">添加比赛日</a>
					<a class="l-link" href="javascript:f_addTab('listround','比赛日列表','module_list.jsp?tab=listround&type=round&sub=list_mod')">比赛日列表</a>
			</div>
			<div title="参数配置">
				<div style=" height:7px;"></div>
					<a class="l-link" href="javascript:f_addTab('configcommon','基本配置','module_config.jsp?tab=configcommon&type=config&sub=common_mod')">基本配置</a>
					<a class="l-link" href="javascript:f_addTab('configpatch','匹配服务器配置','module_config.jsp?tab=configpatch&type=config&sub=patch_mod')">匹配服务器配置</a>
					<a class="l-link" href="javascript:f_addTab('configpk','对战服务器配置','module_config.jsp?tab=configpk&type=config&sub=pk_mod')">对战服务器配置</a>
					<a class="l-link" href="javascript:f_addTab('configtitles','称号配置','module_config.jsp?tab=configtitles&type=config&sub=titles_mod')">称号配置</a>
					<a class="l-link" href="javascript:f_addTab('configseason','赛季配置','module_config.jsp?tab=configseason&type=config&sub=season_mod')">赛季配置</a>
			</div>
			
        </div>
        <div position="center" id="framecenter"> 
            <div tabid="home" title="概况" style="height:300px" >
                <iframe frameborder="0" name="home" id="home" src="module_home.jsp"></iframe>
            </div>
        </div> 
        
    </div>
    <div  style="height:32px; line-height:32px; text-align:center;">
            Copyright @ 2010-2019 上海野火网络科技有限公司
    </div>
    <div style="display:none"></div>
</body>
</html>
