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
			<h2>ģ��鿴</h2>
		</div>
		<table class="bordered-table zebra-striped">	
			<tr>
				<td>ģ������: </td>
				<td><%=po.getAliasLogName() %></td>
			</tr>	
			<tr>
		
				<td>�����зָ���: </td>
				<td>
					<%=po.getSplitChar() %>
				</td>
			</tr>	
			<tr>
				<td>�ռ�Ƶ��: </td>
				<td><%=po.getAnalyseFrequency() %>s</td>
			</tr>
			<tr>
		
				<td>tailģʽ: </td>
				<td>
					<%=po.getTailType() %>
				</td>
			</tr>
			<tr>
				<td>���ݻ�ȡ:  </td>
				<td>
					<%if(po.getAnalyseType()==1){out.print("��־�ļ�");}
					if(po.getAnalyseType()==2){out.print("shell��ʽ");} 
					if(po.getAnalyseType()==3){out.print("http��ʽ");}
					%>
				</td>
			</tr>
			<tr>
				<td>�ļ�·��: </td>
				<td><%=po.getFilePath() %></td>
			</tr>
			<tr>
				<td>������ʽ: </td>
				<td>
					<%  if(po.getObtainType()==1){out.print("java-class");}
						if(po.getObtainType()==2){out.print("javscript");} 
					%>
				</td>
			</tr>
			<tr>
				<td>������: </td>
				<td><%=po.getClassName() %></td>
			</tr>	
			<tr>
				<td>�������ĸ�������: </td>
				<td><%=po.getAnalyseFuture() %></td>
			</tr>
			<tr>
				<td>����˵��: </td>
				<td>
					<%=po.getAnalyseDesc() %>
				</td>
			</tr>
		</table>
		<div class="well" style="padding: 14px 19px;">
			<center>
				<input class="btn primary" type="button" value="����" onclick="goToTmpPage()">
			</center>
      	</div>
      </div>
	  <footer>
		  <p>&copy; TaoBao 2011</p>
	  </footer>
	</div>
</div>
</body>
</html>