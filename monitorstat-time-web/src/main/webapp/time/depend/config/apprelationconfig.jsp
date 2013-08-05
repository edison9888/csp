<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<%@ page language="java" import="java.util.TreeMap"%>
<%@ page language="java" import="java.util.HashMap"%>
<%@ page language="java" import=" com.taobao.monitor.common.po.CspKeyDependInfoPo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<HEAD>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/index.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<STYLE>
a {
	color: blue
}
</STYLE>
<SCRIPT>
	/*
		�ƶ�Listbox
	*/
	function listbox_moveacross(sourceID, destID) {
		var src = document.getElementById(sourceID);
		var dest = document.getElementById(destID);
		for ( var count = 0; count < src.options.length; count++) {
			if (src.options[count].selected == true) {
				var option = src.options[count];
				var newOption = document.createElement("option");
				newOption.value = option.value;
				newOption.text = option.text;
				newOption.id = option.id;
				newOption.selected = true;
				try {
					src.remove(count, null);
					dest.add(newOption, null); //Standard
				} catch (error) {
					dest.add(newOption); // IE only
					src.remove(count);
				}
				count--;
			}
		}
	}
	function listbox_selectall(listID, isSelect) {
		var listbox = document.getElementById(listID);
		for ( var count = 0; count < listbox.options.length; count++) {
			listbox.options[count].selected = isSelect;
		}
	}
</SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<TITLE>Ӧ��������ϵ����ҳ��</TITLE>
<%
	TreeMap<String, CspKeyDependInfoPo> parentMap = (TreeMap<String, CspKeyDependInfoPo>)request.getAttribute("parentMap");
	TreeMap<String, CspKeyDependInfoPo> otherMap = (TreeMap<String, CspKeyDependInfoPo>)request.getAttribute("otherMap");
	HashMap<String, String> typeMap = (HashMap<String, String>)request.getAttribute("typeMap");	//���ͣ�δ����Ҫ�õ���
	
	String appName = (String)request.getAttribute("appName");
	String errorMsg = (String)request.getAttribute("errorMsg");
	if(appName == null) {
	  appName = "";
	}
	
	if(parentMap == null) {
	  parentMap = new TreeMap<String, CspKeyDependInfoPo>();
	}
	if(otherMap == null) {
	  otherMap = new TreeMap<String, CspKeyDependInfoPo>();
	}
	
	if(typeMap == null) {
	  typeMap = new HashMap<String, String>();
	}
	
	String showMsg = "";
	String searchType =  (String)request.getAttribute("searchType");
	if("1".equals(searchType)) {
	  showMsg = "�����ҵ�Ӧ���б�";
	} else {
	  searchType = "0";
	  showMsg = "��������Ӧ���б�";	//Ĭ������������Ӧ�ñȽϷ�������ϰ��	  
	}
%>
</HEAD>
<body>
<div align="center">
	<h2>Ӧ��������ϵ����ҳ��</h2>
	<br/>
	<table>
		<tr style="margin-bottom: 5px;">
			<td colspan="3">
				<form action="<%=request.getContextPath()%>/searchConfigList.do">
					<input type="text" id="appName" name="appName" 
					<%
					if(!"".equals(appName)) {
					  out.print(" value='" + appName  + "' ");
					}
					%>
					 placeholder="������Ӧ������"></input>
					<select id="searchType" name="searchType">
						<option id="dependMeAppType" value="0"
						<% 
						if("0".equals(searchType)) {
							out.print(" selected ");						  
						}
						%>
						>��������Ӧ��</option>					
						<option id="meDependAppType" value="1"
						<% 
						if("1".equals(searchType)) {
							out.print(" selected ");						  
						}
						%>						
						>�����ҵ�Ӧ��</option>					
					</select>
					<input type="submit" id="btnSearch" name="btnSearch" value="��ѯ"></input>
					<input type="button" id="btnSave" name="btnSave" value="����" onClick="saveRelation()"></input>
				</form>
			</td>
		</tr>
		<%
			if(errorMsg != null && !"".equals(errorMsg.trim())) {
			  %>
			<tr>
				<td align="left" colspan="3"><h3><strong style="color: red;"><%=errorMsg%></strong></h3></td>
			</tr>		  
			  <%
			}
		%>
		<tr>
			<td align="left"><strong style="color: red;"><%=showMsg%></strong></td>
			<td align="center"></td>
			<td align="left">��ѡӦ���б�</td>
		</tr>
		<tr valign="top">
			<td align="left">
				<SELECT id="parentList" size="10" multiple style="width: 250px;height: auto;">
				<%
					for(String tmpAppName : parentMap.keySet()) {
					  String appHtml = tmpAppName;
					  String style = "";
					  System.out.println("----" + parentMap.get(tmpAppName).getConfigType());
					  if("mannual".equals(parentMap.get(tmpAppName).getConfigType())) {
					    appHtml += "(�ֶ�����)";
					    style = " style='color: red;' ";
					  } else {
					    appHtml += "(�ɼ�����)";
					  }
					  out.print("<option " + style + " id='" + parentMap.get(tmpAppName).getId() + "' value='" + tmpAppName + "'>" + appHtml + "</option>");
					}
				%>				
				</SELECT>
			</td>
			<td valign="middle" align="center" style="width: 50px">
				<a href="#" onclick="listbox_moveacross('parentList', 'otherList')">&gt;&gt;</a> <br/> 
				<a href="#" onclick="listbox_moveacross('otherList', 'parentList')">&lt;&lt;</a>
			</td>
			<td align="left">
				<SELECT id="otherList" size="10" multiple style="width: 250px;height: auto;">
				<%
					for(String tmpAppName : otherMap.keySet()) {
					  out.print("<option id='" + otherMap.get(tmpAppName).getId() + "' value='" + tmpAppName + "'>" + tmpAppName + "</option>");
					}
				%>
				</SELECT>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
function saveRelation() {
	var appName = '<%=appName%>';
	if(appName == "") {
		alert("�����²�ѯ");
		return;
	} else {
		$("#appName").attr('value',appName);
	}
	var targetAppNames = "";
	var dest = $("#parentList")[0];
	for ( var count = 0; count < dest.options.length; count++) {
		var option = dest.options[count];
		targetAppNames += "," + option.value;
	}
	if(targetAppNames.indexOf(",") == 0) {
		targetAppNames = targetAppNames.substr(1);
	}
	
	var requestUrl = '';
	var params = {
			targetAppNames: targetAppNames,
			appName: '<%=appName%>'
	};
	var searchType = "<%=searchType%>";
	if(searchType == 0) {	//������
		requestUrl = '<%=request.getContextPath()%>/saveMeDependApp.do';
	} else {	//������
		requestUrl = '<%=request.getContextPath()%>/saveAppDependMe.do';
	}
	
	$.getJSON(
			requestUrl,
			params,
			function(json){
				if(json.message == "success") {
					alert("����ɹ�");
				} else {
					alert("����ʧ��");
				}
			} 
	); 
}
</script>
</BODY>
</HTML>