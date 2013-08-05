<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="com.taobao.monitor.common.ao.center.TimeConfAo"%>
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
<body>



<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./timeConfTmp_update.jsp" method="post">
<%

request.setCharacterEncoding("gbk");
String id = request.getParameter("id");
String action = request.getParameter("action");
if("update".equals(action)){
	String aliasLogName = request.getParameter("aliasLogName");
	String className = request.getParameter("analyseClass");
	String split = request.getParameter("split");
	String filePath =request.getParameter("filePath");
	String future = request.getParameter("future");
	String frequency = request.getParameter("frequency");
	String tailType = request.getParameter("tailType");
	String analyseType = request.getParameter("analyseType");
	String analysedesc = request.getParameter("analyseDesc");
	String obtainType = request.getParameter("obtainType");
	
	TimeConfTmpPo po = new TimeConfTmpPo();
	po.setTmpId(Integer.parseInt(id));
	po.setAliasLogName(aliasLogName);
	po.setClassName(className);
	po.setSplitChar(split);
	po.setFilePath(filePath);
	po.setAnalyseFuture(future);
	po.setAnalyseFrequency(Integer.parseInt(frequency));
	po.setTailType(tailType);
	po.setAnalyseDesc(analysedesc);
	po.setAnalyseType(Integer.parseInt(analyseType));
	po.setObtainType(Integer.parseInt(obtainType));
	boolean b = TimeConfAo.get().updateTimeConfTmp(po);
	
	%>
	<font size="+3" color="red"><%if(b){out.print("���³ɹ�");}else{out.print("ʧ��!�����쳣");} %></font>	
	<a href="./timeConfTmp_center.jsp">����</a>
	<%
}else{
	
	TimeConfTmpPo po = TimeConfAo.get().findTimeConfTmpById(Integer.parseInt(id));
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�����ձ�����ģ��Ļ�����Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table align="center" width="400" border="1" class="ui-widget ui-widget-content">	
	<tr>
		<td>ģ������: </td>
		<td><input type="text" name="aliasLogName"  value="<%=po.getAliasLogName() %>"></td>
	</tr>
	
	<tr>
		<td>�����зָ���: </td>
		<td>
			<select name="split">
				<option value="\n" <%if(po.getSplitChar().equals("\\n")){out.print("selected");} %>>\n</option>
				<option value="\02" <%if(po.getSplitChar().equals("\\02")){out.print("selected");} %>>\02</option>
			</select>
		</td>
	</tr>	
	<tr>
		<td>�ռ�Ƶ��: </td>
		<td><input type="text" name="frequency" value="<%=po.getAnalyseFrequency() %>">s</td>
	</tr>
	
	<tr>
		<td>���ݻ�ȡ: </td>
		<td>
			<select id="obtainTypeid" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(po.getObtainType()==1){out.print("selected");} %>>��־�ļ�</option>
				<option value="2" <%if(po.getObtainType()==2){out.print("selected");} %>>shell��ʽ</option>
				<option value="3" <%if(po.getObtainType()==3){out.print("selected");} %>>http get��ʽ</option>
				<option value="41" <%if(po.getObtainType()==41){out.print("selected");} %>>http post��ʽ</option>
				<option value="4" <%if(po.getObtainType()==4){out.print("selected");} %>>jmx��ʽ</option>
				<option value="5" <%if(po.getObtainType()==5){out.print("selected");} %>>configserver����</option>
			</select>
		</td>
	</tr>
	<tr>
		<td id="obtainId">�ļ�·��: </td>
		<td><input type="text" name="filePath" size="100" value="<%=po.getFilePath()==null?"":po.getFilePath() %>">�������������Ϊִ��ssh��ִ�з��������뽫sshָ��д����</td>
	</tr>
	<tr id="tailId">
		<td>tailģʽ: </td>
		<td>
			<select name="tailType">
				<option value="line" <%if(po.getTailType().equals("line")){out.print("selected");} %>>��</option>
				<option value="char" <%if(po.getTailType().equals("char")){out.print("selected");} %>>�ֽ�</option>
			</select>
		</td>
	</tr>	
	<tr>
		<td>������ʽ: </td>
		<td>
			<select id="analyseTypeid" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
				<option value="1" <%if(po.getAnalyseType()==1){out.print("selected");} %>>java-class</option>
				<option value="2" <%if(po.getAnalyseType()==2){out.print("selected");} %>>javscript</option>
				<option value="11" <%if(po.getAnalyseType()==11){out.print("selected");} %>>dynamic-java</option>
			</select>
		</td>
	</tr>	
	<tr id="classId">
		<td>java������: </td>
		<td><input type="text" name="analyseClass" value="<%=po.getClassName() %>" size="100"></td>
	</tr>
	<tr id="scriptId">
		<td id="scriptContentId">�������ĸ�������: </td>
		<td><textarea rows="10" cols="100" name="future"><%=po.getAnalyseFuture()==null?"":po.getAnalyseFuture() %></textarea>����Ƕ�ȡ��־��ִ��javascript������,�뽫javascriptд����</td>
	</tr>
	<tr>
		<td>����˵��: </td>
		<td>
			<textarea rows="5" cols="80" name="analyseDesc"><%=po.getAnalyseDesc()==null?"":po.getAnalyseDesc() %></textarea>
		</td>
	</tr>
</table>



</div>
</div>

<table width="100%">
	<tr>
		<td align="center">
		<input type="hidden" value="<%=id %>" name="id">
		<input type="hidden" value="update" name="action">
		<input type="submit" value="����ģ����Ϣ">
		<input type="button" value="�ر�" onclick="window.close()"></td>
	</tr>
</table>
</form>
˵����javascrip �ű���Ҫʵ��function analyseOneLine(line){}������<br/>
����function putCountData(time,title,key,value){	}<br/>
function putAverageData(time,title,key,value){}<br/>
function putTextData(time,title,key,value){}����analyseOneLine������Ϣ<br/>
����:<br/>
function analyseOneLine(line){<br/>	
&nbsp;&nbsp;&nbsp;var _tmpLines = line.split(',');<br/>	
&nbsp;&nbsp;&nbsp;var calls = _tmpLines[0];	<br/>
&nbsp;&nbsp;&nbsp;var responseTime = _tmpLines[1];<br/>	
&nbsp;&nbsp;&nbsp;var dateTime = _tmpLines[2];<br/>
&nbsp;&nbsp;&nbsp;putCountData(dateTime,'IC�ӿڷ���','getItemById',calls);<br/>
&nbsp;&nbsp;&nbsp;putAverageData(dateTime,'IC�ӿڷ���','getItemById',responseTime);<br/>
};����dateTime�ĸ�ʽ������yyyy-MM-dd HH:mm<br/>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>