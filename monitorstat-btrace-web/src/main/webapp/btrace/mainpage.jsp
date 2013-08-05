<%@ page language="java" contentType="text/html; charset=GB18030" isELIgnored="false"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">  

<title>CSP�Ų�ҳ��</title>
<link rel="stylesheet" type="text/css" href="../statics/ext3.2.0/resources/css/ext-all.css"/> 
<script type="text/javascript" src="../statics/ext3.2.0/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="../statics/ext3.2.0/ext-all.js"></script>
<script type="text/javascript" src="../statics/self/btrace.js"></script>
<style type="text/css">
.textfield {
	width: 400px 
}
</style>
</head>
<body>
<div>
Ŀ������IP��${ip} &nbsp;&nbsp;  ��¼�û���${userName}
</div>
<div>
	<h4 align="center">���Բ�������</h4>
</div>
<div style="padding-top: 5px; padding-bottom: 5px;" id="formDiv">
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:99%;align:center; background-color:#DFE8F6" >
	<tr class="ui-widget-header">
		<td align="center" width="400">�Ƿ��Ǿ�̬��</td>
		<td align="center" width="500">
		<select onchange="selectChange(this)" class="textfield" id="selectStatic">
			<option value="false">��</option>
			<option value="true">��</option>
		</select>
		</td>
	</tr>
	<tr class="ui-widget-header">
		<td align="center" width="400">ClassName</td>
		<td align="center" width="500"><input type="text" id="tarClassname" name="tarClassname" value="com.taobao.test.TestFieldThread2" class="textfield"/></td>
	</tr>
	<tr class="ui-widget-header ">
		<td align="center" width="400">�Ƿ�Ϊ��̬����</td>
		<td align="center" width="500">
		<select class="textfield" id="tarIsStatic">
			<option value="true">��</option>
			<option value="false">��</option>
		</select>
		</td>
	</tr>		
	<tr class="ui-widget-header ">
		<td align="center" width="400">FieldName</td>
		<td align="center" width="500"><input type="text" id="tarFieldname" name="tarFieldname" value="strField" class="textfield"/></td>
	</tr>
	<tr class="ui-widget-header ">
		<td align="center" width="400">MethodName</td>
		<td align="center" width="500"><input type="text" id="tarMethodname" name="tarMethodname" value="say" class="textfield"/></td>
	</tr>		
	<tr>
		<td align="center" width="20"><input type="submit" value="�ύ" onclick="tranformClass('${ip}')"/></td>
		<td align="center" width="20"><input type="reset" value="���"/></td>
	</tr>
</table>
</div>
<input type="button" value="ˢ�½���" name="btnShowPids" id="btnShowPids" onclick="showPids('${ip}','${userName}','${password}')"/>
<input type="submit" value="��ʼ����" onclick="injectTrace('${ip}','${userName}','${password}')"/>
<input type="submit" value="�Ͽ�����" onclick="stopBtrace('${ip}')"/>
<table style="padding-top: 5px; width: 100%">
	<tr>
		<td>
			<div id="divPids"></div>
			<input type="button" value="ˢ��ע����" name="btnShowInjectClass" id="btnShowInjectClass" onclick="showInjectClass('${ip}')"/>
			<input type="submit" value="�Ƴ�ע�����" onclick="removeClass('${ip}')"/>
			<div id="divInjectClass" style="padding-top: 5px"></div>
		</td>
	</tr>
</table>

<script type="text/javascript">
Ext.onReady(function() {
	injectClassStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
					url : 'control.do?method=showTransformerClass'
				}),
		reader : new Ext.data.JsonReader({
					root : 'root',		
					totalProperty : 'total',
					successProperty : 'success',
					fields : [{
								name : 'className',
								mapping : 'className'
							},{
								name : 'tranformId',
								mapping : 'tranformId'
							},{
								name : 'changed',
								mapping : 'changed'
							}]
				}),
		baseParams : {
			ip: '${ip}'
		}  
	});		
	
	var injectClassCm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : false		//������
		},
		columns : [new Ext.grid.RowNumberer(), 
		           new Ext.grid.CheckboxSelectionModel(),
		           /*{
						header : '������',
						dataIndex : 'tranformId',
						align : 'tranformId',
						width : 500
					}, */
					{
						header : '������',
						dataIndex : 'className',
						align : 'className',
						width : 500
					},{
						header : '״̬',
						dataIndex : 'changed',
						align : 'changed',
						width : 500,
						renderer: function renderName(value, metadata, record){		//����Զ��������⣬�Ƚϱ��ķ���
							if(value) {
								return "ע��ɹ�";
							}
							return "ע��ʧ��";
						}
					}]
	});		

			
	
	
	//�Ѿ�ע���Class�б�
	injectClassGrid = new Ext.grid.GridPanel({
	    autoHeight: true,
	    autoWidth: true,
	    border : true,
	    renderTo: 'divInjectClass',
	    store: injectClassStore,
	    cm: injectClassCm,
	    sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	    loadMask: true
	});		
	
	pidsStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
					url : 'control.do?method=showPids'
				}),
		reader : new Ext.data.JsonReader({
					root : 'root',		
					totalProperty : 'total',
					successProperty : 'success',
					fields : [{
								name : 'processId',
								mapping : 'processId'
							},{
								name : 'processUserName',
								mapping : 'processUserName'
							}, {
								name : 'processTime',
								mapping : 'processTime'
							}, {
								name : 'processDesc',
								mapping : 'processDesc'
							}]
				}),
		baseParams : {
			ip: '${ip}',
			userName: '${userName}',
			password: '${password}'
		}  				
	});
	
	var pidsCm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : false		//������
		},
		columns : [new Ext.grid.RowNumberer(), 
		           new Ext.grid.CheckboxSelectionModel(),
		           	{
						header : '�̺߳�',
						dataIndex : 'processId',
						align : 'center',
						width : 100
					}, {
						header : '�߳�����',
						dataIndex : 'processUserName',
						align : 'center',
						width : 100
					},/* {
						header : '����ʱ��',
						dataIndex : 'processTime',
						align : 'center',
						width : 100
					},*/{
						header : '�߳�����',
						dataIndex : 'processDesc',
						align : 'center',
						width : 300,
						renderer: function renderName(value, metadata, record){		//����Զ��������⣬�Ƚϱ��ķ���
							metadata.attr = 'style="white-space:normal;"';
						 	return value;
						}
					}
				]
	});			
	
	//��ǰ�Ľ��̺�
	pidsGrid = new Ext.grid.GridPanel({
	    autoHeight: true,
	    autoWidth: true,
	    border : true,
	    renderTo: 'divPids',
	    store: pidsStore,
	    sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
		cm : pidsCm,
		loadMask: true,
		viewConfig: {
		     forceFit:true
		}
	});	
	
	//�״ν���ʱ��loadingһ��
	injectClassStore.load();
	pidsStore.load();

});
</script>
</body>
</html>