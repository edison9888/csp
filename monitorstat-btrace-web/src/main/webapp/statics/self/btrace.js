/**
 * author: ��ͤ
 * 
 */
//һЩ��������

//�Ѿ�ע���class
var injectClassStore = null;	
var injectClassGrid = null;
//��JVM��ǰ���̺߳��б�
var pidsStore = null;
var pidsGrid = null;
var errorCode = -1;

/******************************************��������****************************************************/

/****************************************************************************************************/

/**
 * ��ʼ����
 */
function injectTrace(ip, userName, password) {
	if(pidsGrid == null) {
		alert("ҳ��δ��ʼ����ϣ�ˢ��ҳ������~");
		return;
	}
	
	var rowArray = pidsGrid.getSelectionModel().getSelections();
	if(rowArray[0] == undefined) {
		alert("��ѡ��һ������");
		return;
	}
	
	var pid = rowArray[0].get("processId");	//�߳�Id
	
	var params = {
			ip:			ip, 
			userName:	userName, 
			password:	password, 
			pid:		pid 
		};	

	Ext.getBody().mask("����ע�����.���Ե�...","x-mask-loading");
	Ext.Ajax.request({
		url: 'control.do?method=injectTrace',
		success: function(resp, opts) {
			Ext.getBody().unmask();
			var respTextObj = Ext.util.JSON.decode(resp.responseText);                                                 
			if(respTextObj.message == 'success' || respTextObj.message == 'already') {
				alert("����ע��ɹ���");    	   
			} else {
				alert("����ע��ʧ�ܣ�"); 
			}
		},
		failure: function(resp,opts) {
		   Ext.getBody().unmask();
		   alert("���ӷ�����ʧ�ܣ�" + resp.responseText);
		},
			params: params
		});	
	
}

/**
 * �Ͽ�����
 */
function stopBtrace(ip) {
	if(pidsGrid == null) {
		alert("ҳ��δ��ʼ����ϣ�ˢ��ҳ������~");
	}
	var params = {
			ip:			ip
		};	
	Ext.getBody().mask("���ڶϿ�����.���Ե�...","x-mask-loading");
	Ext.Ajax.request({
		url: 'control.do?method=stopBtrace',
		success: function(resp, opts) {
			Ext.getBody().unmask();
			var respTextObj = Ext.util.JSON.decode(resp.responseText);                                                 
			if(respTextObj.message == 'success') {
				alert("ֹͣ���Գɹ���");    	   
			} else {
				alert("ֹͣ����ʧ�ܣ�"); 
			}
		},
		failure: function(resp,opts) {
		   Ext.getBody().unmask();
		   alert("���ӷ�����ʧ�ܣ�" + resp.responseText);
		},
			params: params
		});	
	
}

var windowSetting = "height=800,width=1000,resizable=yes,status=no,z-look=yes";	//�������ڲ���
/**
 * ע��ĳһ��Class
 */
function tranformClass(ip) {

	var params = null;
	var url = null;

	var tarFieldname = document.getElementById("tarFieldname").value;
	if(tarFieldname == "") {	//ֻ�鿴����
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
		if(selectStatic == "true") {	//��̬��
			params.methodName = "";
		}
	}

	var windowName = "newwindow";	//windows �Լ���ָ����ܸĵġ�
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
			alert("���ӷ�����ʧ�ܣ�" + resp.responseText);
		},
//		headers: {
//		'my-header': 'foo'
//		},
		params: params
	});
}

/**
 * ��ʾ��ǰ�Ѿ�ע���Class����Ϣ
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
 * ���¼����߳�����
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
 * ��ѡ��Ϊ��̬���ʱ�򣬲�ѡ��Method
 * @param obj
 */
function selectChange(obj) {
	var methodObj = Ext.get("tarMethodname");
	if(obj.value == "true") {	//�Ǿ�̬��
		methodObj.setVisible(false);		
	} else {
		methodObj.setVisible(true);
	}
}

/**
 * �Ƴ�ĳһ���Ѿ�ע�����
 * @param �������������IP
 */
function removeClass(ip) {
	if(injectClassGrid == null) {
		alert("ҳ��δ��ʼ����ϣ�ˢ��ҳ������~");
		return;
	}
	
	var rowArray = injectClassGrid.getSelectionModel().getSelections();
	if(rowArray[0] == undefined) {
		alert("��ѡ��һ����");
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
			alert("���ӷ�����ʧ�ܣ�" + resp.responseText);
		},
		params: params
	});	
	
}
