/**
 * author: 中亭
 * 
 */
//一些变量声明

//已经注入的class
var injectClassStore = null;	
var injectClassGrid = null;
//该JVM当前的线程号列表
var pidsStore = null;
var pidsGrid = null;
var errorCode = -1;

/******************************************基础函数****************************************************/

/****************************************************************************************************/

/**
 * 开始调试
 */
function injectTrace(ip, userName, password) {
	if(pidsGrid == null) {
		alert("页面未初始化完毕！刷新页面试试~");
		return;
	}
	
	var rowArray = pidsGrid.getSelectionModel().getSelections();
	if(rowArray[0] == undefined) {
		alert("请选择一个进程");
		return;
	}
	
	var pid = rowArray[0].get("processId");	//线程Id
	
	var params = {
			ip:			ip, 
			userName:	userName, 
			password:	password, 
			pid:		pid 
		};	

	Ext.getBody().mask("正在注入程序.请稍等...","x-mask-loading");
	Ext.Ajax.request({
		url: 'control.do?method=injectTrace',
		success: function(resp, opts) {
			Ext.getBody().unmask();
			var respTextObj = Ext.util.JSON.decode(resp.responseText);                                                 
			if(respTextObj.message == 'success' || respTextObj.message == 'already') {
				alert("程序注入成功！");    	   
			} else {
				alert("程序注入失败！"); 
			}
		},
		failure: function(resp,opts) {
		   Ext.getBody().unmask();
		   alert("连接服务器失败：" + resp.responseText);
		},
			params: params
		});	
	
}

/**
 * 断开调试
 */
function stopBtrace(ip) {
	if(pidsGrid == null) {
		alert("页面未初始化完毕！刷新页面试试~");
	}
	var params = {
			ip:			ip
		};	
	Ext.getBody().mask("正在断开程序.请稍等...","x-mask-loading");
	Ext.Ajax.request({
		url: 'control.do?method=stopBtrace',
		success: function(resp, opts) {
			Ext.getBody().unmask();
			var respTextObj = Ext.util.JSON.decode(resp.responseText);                                                 
			if(respTextObj.message == 'success') {
				alert("停止调试成功！");    	   
			} else {
				alert("停止调试失败！"); 
			}
		},
		failure: function(resp,opts) {
		   Ext.getBody().unmask();
		   alert("连接服务器失败：" + resp.responseText);
		},
			params: params
		});	
	
}

var windowSetting = "height=800,width=1000,resizable=yes,status=no,z-look=yes";	//弹出窗口参数
/**
 * 注入某一个Class
 */
function tranformClass(ip) {

	var params = null;
	var url = null;

	var tarFieldname = document.getElementById("tarFieldname").value;
	if(tarFieldname == "") {	//只查看方法
		url = 'control.do?method=tranformMethod';
		params = {
				ip:			ip, 
				className:	document.getElementById("tarClassname").value, 
				methodName:	document.getElementById("tarMethodname").value
		};
	} else {
		url = 'control.do?method=tranformField';
		params = {
				ip:			ip, 
				className:	document.getElementById("tarClassname").value, 
				methodName:	document.getElementById("tarMethodname").value, 
				fieldName:	document.getElementById("tarFieldname").value, 
				isStatic:	document.getElementById("tarIsStatic").value
		};
		var selectStatic = document.getElementById("selectStatic").value;
		if(selectStatic == "true") {	//静态类
			params.methodName = "";
		}
	}

	var windowName = "newwindow";	//windows 自己的指令，不能改的。
	Ext.Ajax.request({
		url: url,
		success: function(resp,opts) {
			var respTextObj = Ext.util.JSON.decode(resp.responseText);                                                 
			if(respTextObj.message == 'success') {
				injectClassStore.reload();
				window.open (respTextObj.pageURL, windowName, windowSetting);    	   
			} else {
				alert(respTextObj.message); 
			}
		},
		failure: function(resp,opts) {
			alert("连接服务器失败：" + resp.responseText);
		},
//		headers: {
//		'my-header': 'foo'
//		},
		params: params
	});
}

/**
 * 显示当前已经注入的Class的信息
 */
function showInjectClass(ip) {
	//document.getElementById("divInjectClass").style.display = "";
	//document.getElementById("divPids").style.display = "none";
	var params = {
		ip:	ip
	};
	if(injectClassStore != null) {	
		injectClassStore.load({
			params : params
		});		
	}
}

/**
 * 重新加载线程数据
 */
function showPids(ip, userName, password) {
	//document.getElementById("divInjectClass").style.display = "none";
	//document.getElementById("divPids").style.display = "";
	
	var params = {
		ip:	ip,
		userName: userName,
		password: password
	};
	if(pidsStore != null) {	
		pidsStore.load({
			params : params
		});		
	}
}

/**
 * 当选择为静态类的时候，不选择Method
 * @param obj
 */
function selectChange(obj) {
	var methodObj = Ext.get("tarMethodname");
	if(obj.value == "true") {	//是静态类
		methodObj.setVisible(false);		
	} else {
		methodObj.setVisible(true);
	}
}

/**
 * 移出某一个已经注入的类
 * @param 关联的虚拟机的IP
 */
function removeClass(ip) {
	if(injectClassGrid == null) {
		alert("页面未初始化完毕！刷新页面试试~");
		return;
	}
	
	var rowArray = injectClassGrid.getSelectionModel().getSelections();
	if(rowArray[0] == undefined) {
		alert("请选择一个类");
		return;
	}	
	
	var tranformId = rowArray[0].get("tranformId");
	
	var params = {
		ip: ip,
		tranformId: tranformId
	};
	
	Ext.Ajax.request({
		url: 'control.do?method=deleteTranformClass',
		success: function(resp,opts) {
			var respTextObj = Ext.util.JSON.decode(resp.responseText);                                                 
			if(respTextObj.message == 'success') {
				injectClassStore.reload();
			} else {
				alert(respTextObj.message); 
			}
		},
		failure: function(resp,opts) {
			alert("连接服务器失败：" + resp.responseText);
		},
		params: params
	});	
	
}
