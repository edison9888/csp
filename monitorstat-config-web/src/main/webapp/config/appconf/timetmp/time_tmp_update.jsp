<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ʵʱģ�����</title>

<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<style type="text/css">
	body {
	  padding-top: 60px;
	}
</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<%
TimeConfTmpPo po = (TimeConfTmpPo)request.getAttribute("timeConfTmpPo");
%>
<script type="text/javascript">

function goToTmpPage() {
	var str="<%=request.getContextPath() %>/show/appconfig.do?method=gotoTimeTmp";
	location.href = str;
}
</script>
</head>
<body>
<jsp:include page="../../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2>ʵʱ����ģ�����</h2>
		</div>
		<form action="<%=request.getContextPath() %>/show/appconfig.do?method=updateTimeTmp" method="post">
		<table class="bordered-table zebra-striped condensed-table">	
			<tr>
				<td>ģ������: </td>
				<td><input type="text" name="aliasLogName"  value="<%=po.getAliasLogName() %>"></td>
			</tr>
			
			<tr>
				<td>�����зָ���: </td>
				<td>
					<select name="splitChar">
						<option value="\n" <%if(po.getSplitChar().equals("\\n")){out.print("selected");} %>>\n</option>
						<option value="\02" <%if(po.getSplitChar().equals("\\02")){out.print("selected");} %>>\02</option>
					</select>
				</td>
			</tr>	
			<tr>
				<td>�ռ�Ƶ��: </td>
				<td><input type="text" name="frequency" value="<%=po.getAnalyseFrequency() %>">&nbsp;&nbsp;��</td>
			</tr>
			
			<tr>
				<td>���ݻ�ȡ: </td>
				<td>
					<select id="obtainTypeid" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
						<option value="1" <%if(po.getObtainType()==1){out.print("selected");} %>>��־�ļ�</option>
						<option value="2" <%if(po.getObtainType()==2){out.print("selected");} %>>shell��ʽ</option>
						<option value="3" <%if(po.getObtainType()==3){out.print("selected");} %>>http��ʽ</option>
					</select>
				</td>
			</tr>
			<tr>
				<td id="obtainId">�ļ�·��: </td>
				<td><input type="text" name="filePath" value="<%=po.getFilePath()==null?"":po.getFilePath() %>" style="width:350px;"><p>�������������Ϊִ��ssh��ִ�з��������뽫sshָ��д����</td>
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
					</select>
				</td>
			</tr>	
			<tr id="classId">
				<td>java������: </td>
				<td><input type="text" name="className" value="<%=po.getClassName() %>" size="100" style="width:350px;"></td>
			</tr>
			<tr id="scriptId">
				<td id="scriptContentId">�������ĸ�������: </td>
				<td><textarea style="width:350px;" rows="2" name="future"><%=po.getAnalyseFuture()==null?"":po.getAnalyseFuture() %></textarea><p>����Ƕ�ȡ��־��ִ��javascript������,�뽫javascriptд����</td>
			</tr>
			<tr>
				<td>����˵��: </td>
				<td>
					<textarea style="width:350px;" rows="2" name="analyseDesc"><%=po.getAnalyseDesc()==null?"":po.getAnalyseDesc() %></textarea>
				</td>
			</tr>
		</table>
		<div class="well" style="padding: 14px 19px;">
			<center>
				<input type="hidden" value="<%=po.getTmpId() %>" name="tmpId">
	        	<input class="btn primary" type="submit" value="�޸�ģ������"">
				<input class="btn primary" type="button" value="����" onclick="goToTmpPage()">
			</center>
      	</div>
      	</form>
      </div>
	  <footer>
		  <p>&copy; TaoBao 2011</p>
	  </footer>
	</div>
</div>
</body>
</html>