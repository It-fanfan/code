<%

	String tab = request.getParameter("tab");
	String type = request.getParameter("type");
	String sub = request.getParameter("sub");
	String seq = request.getParameter("id");
	if(null == seq)
	{
		seq = "0";
	}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>配置页面</title>
    <link href="libui/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="libui/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" /> 
    <link href="libui/ligerUI/skins/ligerui-icons.css" rel="stylesheet" />
    <script src="libui/jquery/jquery-1.9.0.min.js"></script>
    <script src="libui/ligerUI/js/ligerui.all.js" type="text/javascript"></script> 
    <script src="libui/ligerUI/js/plugins/ligerForm.js"></script>
	<script src="libui/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
    <script src="libui/json2.js" type="text/javascript"></script>
     <style type="text/css">
        .middle input {
            display: block;width:30px; margin:2px;
        }
    </style>
</head>
<body style="padding:0px;">   
	<div id="layout1" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
		<div id="formDesign" style="clear:both;"></div>
		<div id="btnSaveToFile" style="margin-top:15px; margin-left:145px;margin-bottom:20px;float:left;display:none;"></div>
    </div> 

    <script type="text/javascript">
	
        var demoData = [], demoGrid = { columns: [] };
        for (var i = 0; i < 9; i++) demoData.push({ id: i, value: i, text: '[数据' + i + ']' });
        demoGrid.columns.push({ display: 'ID', name: 'id' });
        demoGrid.columns.push({ display: 'Text', name: 'text' });
        demoGrid.data = { Rows: demoData };
	
	function f_loadFile_demo()
        {
            formDesign.set({
                inputWidth: 280,
                labelWidth: 120,
                space: 40,
                fields: [
			{ name: "hiddenTest", type: "hidden" },
			{ label: "文本测试", name: "textTest", width: 280, labelWidth: 120, space: 40, newline: true, type: "text"},
			{
				label: "组合框测试", name: "comboTest", width: 280, labelWidth: 120, space: 40, newline: true, type: "select", textField: "comboTest", 
				editor: {
					data: demoData
				}
			},
			{ label: "日期测试", name: "dateTest", width: 280, labelWidth: 120, space: 40, newline: true, type: "date" },
			{ label: "小数测试", name: "floatTest", width: 280, labelWidth: 120, space: 40, newline: true, type: "number" },
			{ label: "整数测试", name: "integerTest", width: 280, labelWidth: 120, space: 40, newline: true, type: "digits" },
			{ label: "文本区域测试", name: "editTest", labelWidth: 120, space: 40, newline: true, type: "textarea", width: 280 }
                ]
            });
			
	   formDesign.setData({
		textTest: "111"
	   });
        }
		
	function f_loadFile(result)
        {
		formDesign.set({
			inputWidth: 280,
			labelWidth: 160,
			space: 40,
			fields: result.content
		});
		
		let datas = {};
		for(let i = 0 ; i < result.content.length ; i ++) {
			let name = result.content[i].name;
			let data = result.content[i].data;
			if(name == null || data == null) {
				continue;
			}
			datas[name] = data;
		}
		formDesign.setData(datas);
		
		for(let i = 0 ; i < result.content.length ; i ++) {
			let name = result.content[i].name;
			let enable = result.content[i].enable;
			if(enable == null) {
				continue;
			}
			formDesign.setEnabled(name , enable);
		}
        }

        function f_submit()
        {
		let httpRequest = new XMLHttpRequest();
		let formData = formDesign.getData();
		let jsonString = JSON.stringify(formData);
		let reqTab = "<%=tab%>";
		let reqType = "<%=type%>";
		let reqSeq = "<%=seq%>";
		
		$.ligerDialog.waitting('提交数据中，请稍候...');
		
		httpRequest.open("POST" , "config?type=" + reqType + "&sub=" + formData.sub + "&id=" + reqSeq , true);
		httpRequest.onreadystatechange = function() {
			if(httpRequest.readyState == 4) {
				let result = httpRequest.responseText;
				let status = httpRequest.status;
				
				if(200 == status) {
					result = JSON.parse(result);
					if(result.result == "success") {
						$.ligerDialog.waitting('提交成功，即将关闭窗口。');
						setTimeout(function() {
							let tabObject = parent.document.tabObject;
							tabObject.removeTabItem(reqTab);
							
							let refreshTab = result.refreshTab;
							if(null != refreshTab && tabObject.isTabItemExist(refreshTab)) {
								tabObject.reload(refreshTab);
							}
						} , 1000);
					} else {
						status = -1;
					}
				}
				
				if(200 != status) {
					 $.ligerDialog.closeWaitting();
					 $.ligerDialog.error("提交失败，请检查数据合法性。");
				}
			}
		};
		httpRequest.send(jsonString);
        }

        $("#layout1").ligerLayout({ 
            rightWidth: 250,
            height: '100%',
            heightDiff: -4,
            space: 4 
        }); 
      
        var formDesign = $("#formDesign").ligerForm({});
        var btnSaveToFile = $("#btnSaveToFile").ligerButton({
            click: f_submit, text: '编辑完毕，保存并提交数据' , width:170
        });
		
	$.ligerDialog.waitting('正在加载中，请稍候...');
	
	let reqType = "<%=type%>";
	let reqSub = "<%=sub%>";
	let reqSeq = "<%=seq%>";
	let httpRequest = new XMLHttpRequest();
	let jsonString = JSON.stringify({});
        httpRequest.open("POST" , "config?type=" + reqType + "&sub=" + reqSub + "&id=" + reqSeq , true);
	httpRequest.onreadystatechange = function() {
		if(httpRequest.readyState == 4) {
			let result = httpRequest.responseText;
			let status = httpRequest.status;
			
			if(200 == status) {
				result = JSON.parse(result);
				if(result.result == "success") {
					f_loadFile(result);
					document.getElementById("btnSaveToFile").style.display = "block";
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
	 
     </script> 
</body>
</html>
