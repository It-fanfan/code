<%

	String tab = request.getParameter("tab");
	String type = request.getParameter("type");
	String sub = request.getParameter("sub");

%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title></title>
    <link href="libui/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="libui/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" /> 
    <link href="libui/ligerUI/skins/ligerui-icons.css" rel="stylesheet" />
    <script src="libui/jquery/jquery-1.9.0.min.js" type="text/javascript"></script> 
    <script src="libui/ligerUI/js/core/base.js" type="text/javascript"></script>
	<script src="libui/ligerUI/js/ligerui.all.js" type="text/javascript"></script> 
	<script src="libui/ligerUI/js/plugins/ligerForm.js"></script>
    <script src="libui/ligerUI/js/plugins/ligerGrid.js" type="text/javascript"></script> 
    <script src="libui/ligerUI/js/plugins/ligerTextBox.js" type="text/javascript"></script>
    <script src="libui/ligerUI/js/plugins/ligerCheckBox.js" type="text/javascript"></script>
     <script src="libui/ligerUI/js/plugins/ligerMenu.js" type="text/javascript"></script>
    <script src="libui/ligerUI/js/plugins/ligerComboBox.js" type="text/javascript"></script>
    <script src="libui/ligerUI/js/plugins/ligerMenuBar.js" type="text/javascript"></script>
    <script src="libui/ligerUI/js/plugins/ligerToolBar.js" type="text/javascript"></script>
    <script src="libui/ligerUI/js/plugins/ligerButton.js" type="text/javascript"></script>
    <script src="libui/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
    <script src="libui/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
    <script src="libui/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
    <script type="text/javascript">

	function reset_page_pos() {
		var pagealtObject = document.getElementById('pagealt');
		pagealtObject.style.left = document.body.clientWidth - 
				(pagealtObject.clientWidth | pagealtObject.offsetWidth) - 10;
	}
	
	function list_edit(url , tab , name) {
		let tabObject = parent.document.tabObject;
		if(tabObject.isTabItemExist(tab)) {
			$.ligerDialog.warn("请关闭已经打开的窗口 -> " + name + "。");
			return;
		}
		
		tabObject.addTabItem({
			tabid: tab,
			text: name,
			url: url,
			callback: function ()
			{
			}
		});
	}
	
	function list_del(url) {
		$.ligerDialog.confirm("即将删除对应列表项，确定吗？" , function(yes) {
			if(yes) {
				let httpRequest = new XMLHttpRequest();
				let jsonString = JSON.stringify({});
				let reqTab = "<%=tab%>";
				
				httpRequest.open("POST" , url , true);
				httpRequest.onreadystatechange = function() {
					if(httpRequest.readyState == 4) {
						let tabObject = parent.document.tabObject;
						tabObject.reload(reqTab);
					}
				};
				httpRequest.send(jsonString);
			}
		});
	}
	
	function list_click(url) {
		let httpRequest = new XMLHttpRequest();
		let jsonString = JSON.stringify({});
		let reqTab = "<%=tab%>";
		
		httpRequest.open("POST" , url , true);
		httpRequest.onreadystatechange = function() {
			if(httpRequest.readyState == 4) {
				let tabObject = parent.document.tabObject;
				tabObject.reload(reqTab);
			}
		};
		httpRequest.send(jsonString);
	}
	
        function load_datas(result) {
		var list = result.list;
		var header = result.header;
		var hasSearch = result.hasSearch;
		var totalPage = result.totalPage;
		var currentPage = result.currentPage;
		
		if(hasSearch) {
			var formSearchObject = document.getElementById('formSearch');
			formSearchObject.style.display = "block";
			var searchbtnObject = document.getElementById('searchbtn');
			searchbtnObject.style.display = "block";
			
			var formSearch = $("#formSearch").ligerForm({});
			formSearch.set({
				inputWidth: 120,
				labelWidth: 60,
				space: 20,
				fields: [
					{ label: "开始日期", name: "date0", width: 160, labelWidth: 60, space: 20, newline: false, type: "date" },
					{ label: "结束日期", name: "date1", width: 160, labelWidth: 60, space: 20, newline: false, type: "date" },
					{ label: "关键字", name: "keyword", width: 160, labelWidth: 60, space: 20, newline: false, type: "text"}
				]
			});
			
			$("#searchbtn").ligerButton({ click: function ()
				{
					
				}
			}); 
		}
		
		if(null != totalPage && null != currentPage &&
			totalPage > 0 && currentPage >= 0) {
			var pminObject = document.getElementById('pmin');
			var pmaxObject = document.getElementById('pmax');
			
			if(currentPage <= 0) {
				pminObject.removeAttribute('href');
			} else if(currentPage >= totalPage - 1) {
				pmaxObject.removeAttribute('href');
			}
			
			if(totalPage < 5) {
				for(let i = 4 ; i >= totalPage ; i --) {
					var pObject = document.getElementById('p' + i);
					pObject.style.display = "none";
				}
			}
			
			var startPage = currentPage - 2;
			var endPage = currentPage + 2;
			if(endPage > totalPage) {
				startPage -= (endPage - totalPage);
				endPage = totalPage;
			}
			if(startPage < 0) {
				startPage = 0;
			}
			
			for(let i = 0 , j = startPage ; i < 5 ; i ++ , j ++) {
				var pObject = document.getElementById('p' + i);
				pObject.innerHTML = (j + 1);
				if(j == currentPage) {
					pObject.removeAttribute('href');
				}
			}
			
			reset_page_pos();
		} else {
			var pagealtObject = document.getElementById('pagealt');
			pagealtObject.style.display = "none";
		}
		
		var customersData = {
			Rows: list,
			Total: list.length
		};
		
		$("#maingrid").ligerGrid({
			columns: header, 
			dataAction: 'server', 
			data: customersData, 
			width: '100%', 
			height: '100%', 
			rownumbers:true,
			cssClass: 'l-grid-gray'
		});
	}
		
        $(function ()
        {
		$.ligerDialog.waitting('正在加载中，请稍候...');
	
		let reqType = "<%=type%>";
		let reqSub = "<%=sub%>";
		let httpRequest = new XMLHttpRequest();
		let jsonString = JSON.stringify({});
		
		httpRequest.open("POST" , "config?type=" + reqType + "&sub=" + reqSub , true);
		httpRequest.onreadystatechange = function() {
			if(httpRequest.readyState == 4) {
				let result = httpRequest.responseText;
				let status = httpRequest.status;
				
				if(200 == status) {
					result = JSON.parse(result);
					if(result.result == "success") {
						load_datas(result);
						 $.ligerDialog.closeWaitting();
					} else {
						status = -1;
					}
				}
				
				if(200 != status) {
					 $.ligerDialog.closeWaitting();
					 $.ligerDialog.error("加载失败，请联系管理员。");
				}
			}
		};
		httpRequest.send(jsonString);
	});

    </script>
</head>

<body style="padding:0px; overflow:hidden;">
	<div id="formSearch" style="display:none;"></div>
	<div id="searchbtn" style="position:absolute;left:725px;top:10px;display:none;">查询</div>
	<div id="pagealt" style="position:absolute;left:-1024px;top:12px;font-size:15px;">
		<a id="pmin" href='http://www.ligerui.com/go.aspx?id=case'>首页</a>&nbsp;
		<a id="p0" href='http://www.ligerui.com/go.aspx?id=case'>1</a>&nbsp;
		<a id="p1" href='http://www.ligerui.com/go.aspx?id=case'>2</a>&nbsp;
		<a id="p2" href='http://www.ligerui.com/go.aspx?id=case'>3</a>&nbsp;
		<a id="p3" href='http://www.ligerui.com/go.aspx?id=case'>4</a>&nbsp;
		<a id="p4" href='http://www.ligerui.com/go.aspx?id=case'>5</a>&nbsp;
		<a id="pmax" href='http://www.ligerui.com/go.aspx?id=case'>尾页</a>
	</div>
	<div id="maingrid" style="margin:0; padding:0"></div>
</body>
</html>

