<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.ao.center.TimeConfAo"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="com.taobao.monitor.common.po.TimeConfPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-ʵʱģ����Ϣ����</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<style type="text/css">
body {
	font-size: 62.5%;
}

table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "����";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
}
.report_on{background:#bce774;}


</style>
<script type="text/javascript">
function obtainChange(type){
	if(type == 1){
		$("#obtainId").text("�ļ�·��:");
		$("#tailId").show();
	}
	if(type == 2){
		$("#obtainId").text("shell�ű�:");
		$("#tailId").hide();
	}
	if(type == 3){
		$("#obtainId").text("http-url:");
		$("#tailId").hide();
	}
}

function analyseChange(type){
	if(type == 1){
		$("#classId").show();
		$("#scriptId").show();
		$("#scriptContentId").text("�������ĸ�������:");
		
	}
	if(type == 2){
		$("#classId").hide();
		$("#scriptId").show();
		$("#scriptContentId").text("�ű�����:");
	}
}



$(document).ready(function(){
	obtainChange($("#obtainTypeid").val());
	analyseChange($("#analyseTypeid").val());
	}); 

</script>
</head>
<%
request.setCharacterEncoding("gbk");
String appName = request.getParameter("appName");
String appId = request.getParameter("appId");	//����һ��checkҳ�洫�ݹ���
String tmpId = request.getParameter("tmpId");
%>
<script type="text/javascript">
function goToCheck(){
	
	var url = "<%=request.getContextPath () %>/center/rel_app_timeConf_check.jsp?appId=<%=appId%>&appName=<%=appName%>";
	location.href=url;
}
</script>
<body>




<jsp:include page="../head.jsp"></jsp:include>
<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
%>


<%


String action = request.getParameter("action");
if("add".equals(action)){
	
	String tmpConfigId = request.getParameter("tmpConfigId");
	TimeConfTmpPo tmp = TimeConfAo.get().findTimeConfTmpById(Integer.parseInt(tmpConfigId));
	
	String appId1 = request.getParameter("appId");
	String filePath =request.getParameter("filePath");
	String future = request.getParameter("future");
	String frequency = request.getParameter("frequency");
	String className = request.getParameter("analyseClass");
	String split = request.getParameter("split");
	String tailType = request.getParameter("tailType");
	String analyseType = request.getParameter("analyseType");
	String analysedesc = request.getParameter("analyseDesc");
	String obtainType = request.getParameter("obtainType");
	
	
	
	TimeConfPo po = new TimeConfPo();
	po.setAppId(Integer.parseInt(appId1));
	po.setAliasLogName(tmp.getAliasLogName());
	po.setClassName(className);
	po.setSplitChar(split);
	po.setFilePath(filePath);
	po.setAnalyseFuture(future);
	po.setAnalyseFrequency(Integer.parseInt(frequency));
	po.setTailType(tailType);
	po.setAnalyseType(Integer.parseInt(analyseType));
	po.setObtainType(Integer.parseInt(obtainType));
	boolean b = TimeConfAo.get().addTimeConfData(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("��ӳɹ�");}else{out.print("ʧ��!�����쳣");} %></font>
	<input type="hidden" name="appId" value="<%=appId %>">
	<input type="hidden" name="appName" value="<%=appName %>">
	<input type="button" onclick="goToCheck()" name="����" value="���� ">
	<%
}else{
	
	
	List<TimeConfTmpPo> timeConfTmpList = TimeConfAo.get().findAllAppTimeConfTmp();
	
	TimeConfTmpPo tmp = null;
	if(tmpId != null){
		tmp = TimeConfAo.get().findTimeConfTmpById(Integer.parseInt(tmpId));
	}
	//List<DataBaseInfoPo>  dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
	//List<ServerInfoPo> serverInfoList = ServerInfoAo.get().findAllServerInfo();
%>

<%
List<TimeConfPo> timeConfPoList = TimeConfAo.get().findTimeConfByAppId(Integer.parseInt(appId));
if(timeConfPoList.size() != 0) {
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">����ʵʱ������Ϣ  : �� <%=timeConfPoList.size() %> ��</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td align="left">Ӧ����</td>
		<td align="left">ʵʱ�����ļ���</td>
		<td align="left">ʵʱ�����ļ�·��</td>
		<td align="left">ʵʱ�����ļ�������</td>
	</tr>
	<%
		for(TimeConfPo po : timeConfPoList) {
	%>
	<tr>
	
		<td align="left"><%=appName %></td>
		<td align="left"><%=po.getAliasLogName() %></td>
		<td align="left"><%=po.getFilePath() %></td>
		<td align="left"><%=po.getClassName() %></td>		
	</tr>
	
	<%
		}
	%>
		
</table>
</div>
</div>
<%} %>


<form action="./rel_app_timeConf_add.jsp" method="post">
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<select name="tmpId">
	<%
	
	for(TimeConfTmpPo tmpPo : timeConfTmpList){ 		
		
	%>
	<option value="<%=tmpPo.getTmpId() %>"<%if(tmp!=null&&tmpPo.getTmpId() == tmp.getTmpId()){out.print("selected");} %>><%=tmpPo.getAliasLogName() %></option>
	<%} %>
</select>
<input type="submit"  value="��ȡģ��">
<input type="hidden" name="appId" value="<%=appId %>">
<input type="hidden" name="appName" value="<%=appName %>">
</div>
</form>
<%if(tmpId != null){ %>
<form action="./rel_app_timeConf_add.jsp" method="post">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">���ʵʱ���õĻ�����Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table align="center" width="400" border="1" class="ui-widget ui-widget-content">
	
	<tr>
		<td>ģ������: </td>
		<td><%=tmp.getAliasLogName() %><input type="hidden" name="tmpConfigId" value="<%=tmpId %>"/></td>
	</tr>
	<tr>
		<td>�����зָ���: </td>
		<td>
			<input type="text"  name="split" value="<%=tmp.getSplitChar() %>" >
		</td>
	</tr>	
	<tr>
		<td>�ռ�Ƶ��: </td>
		<td><input type="text" name="frequency" value="<%=tmp.getAnalyseFrequency() %>"></td>
	</tr>	
	<tr>
		<td>���ݻ�ȡ: </td>
		<td>
			<select id="obtainTypeid" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(tmp.getObtainType()==1){out.print("selected");} %>>��־�ļ�</option>
				<option value="2" <%if(tmp.getObtainType()==2){out.print("selected");} %>>shell��ʽ</option>
				<option value="3" <%if(tmp.getObtainType()==3){out.print("selected");} %>>http get��ʽ</option>
				<option value="41" <%if(tmp.getObtainType()==41){out.print("selected");} %>>http post��ʽ</option>
				<option value="4" <%if(tmp.getObtainType()==4){out.print("selected");} %>>jmx��ʽ</option>
				<option value="5" <%if(tmp.getObtainType()==5){out.print("selected");} %>>configserver����</option>
				<option value="44" <%if(tmp.getObtainType()==44){out.print("selected");} %>>json</option>
			</select>
		</td>
	</tr>
	<tr>
		<td id="obtainId">�ļ�·��: </td>
		<td><input type="text" name="filePath" size="100" value="<%=tmp.getFilePath()==null?"":tmp.getFilePath() %>">�������������Ϊִ��ssh��ִ�з��������뽫sshָ��д����</td>
	</tr>
	<tr id="tailId">
		<td>tailģʽ: </td>
		<td>
			<select name="tailType">
				<option value="line" <%if(tmp.getTailType().equals("line")){out.print("selected");} %>>��</option>
				<option value="char" <%if(tmp.getTailType().equals("char")){out.print("selected");} %>>�ֽ�</option>
			</select>
		</td>
	</tr>	
	<tr>
		<td>������ʽ: </td>
		<td>
			<select id="analyseTypeid" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(tmp.getAnalyseType()==1){out.print("selected");} %>>java-class</option>
				<option value="2" <%if(tmp.getAnalyseType()==2){out.print("selected");} %>>javscript</option>
				<option value="11" <%if(tmp.getAnalyseType()==11){out.print("selected");} %>>dynamic-java</option>
			</select>
		</td>
	</tr>	
	<tr id="classId">
		<td>java������: </td>
		<td><input type="text" name="analyseClass" value="<%=tmp.getClassName() %>" size="100"></td>
	</tr>	
	<tr id="scriptId">
		<td id="scriptContentId">�������ĸ�������: </td>
		<td><textarea rows="10" cols="100" name="future"><%=tmp.getAnalyseFuture() %></textarea>����Ƕ�ȡ��־��ִ��javascript������,�뽫javascriptд����</td>
	</tr>
	<tr>
		<td>����˵��: </td>
		<td>
			<textarea rows="5" cols="80" readOnly ><%=tmp.getAnalyseDesc() %></textarea>
		</td>
	</tr>
</table>
</div>
</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" value="add" name="action">
		<input type="hidden" value="<%=appId %>" name="appId">
		<input type="hidden" value="<%=appName %>" name="appName">
		<input type="submit" value="�����ʵʱ���ù���"><input type="button" value="�ر�" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>