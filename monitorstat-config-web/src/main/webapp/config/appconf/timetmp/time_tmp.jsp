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
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/jquery.tablesorter.js"></script>
<%
List<TimeConfTmpPo> tmpList = (ArrayList<TimeConfTmpPo>)request.getAttribute("tmpList");
Boolean issuccess = (Boolean)request.getAttribute("issuccess");
%>
<script type="text/javascript">
$(function() {
	$("#sortTable").tablesorter( { sortList: [[ 0, 0 ]] } );
});

function deleteTmp(tmpId) {
	var con = confirm("�Ƿ�ȷ��ɾ��");
	if (con) {
		location.href = "<%=request.getContextPath()%>/show/appconfig.do?method=deleteTimeTmp&tmpId="+tmpId;
	}
}

function gotoAddTmp() {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=gotoAddTmpPage";
	//alert(str);
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
			<h2>
				ʵʱ����ģ������
				<%
				if (issuccess != null) {
					if (issuccess) {
						%>
							<span class="label success">ģ����³ɹ�</span>
						<%	
						} else {
						%>
							<span class="label warning">ģ�����ʧ��</span>
						<%
						}
				}
				%>
				<button class="btn primary pull-right" onclick="gotoAddTmp()">����ʵʱģ��</button>
			</h2>
		
		
		</div>
		<table class="bordered-table zebra-striped condensed-table" id="sortTable">
			<thead>
				<tr>
					<th class="blue">Id</th>
					<th class="blue">��־�ļ�����</th>
					<th class="blue">������</th>
					<th class="blue">�ָ�</th>
					<th class="blue">Ƶ��</th>
					<th class="blue">tail����</th>
					<th class="blue">���ݻ�ȡ</th>
					<th class="blue">������ʽ</th>
					<th class="blue">����</th>
				</tr>
			</thead>
			<tbody>
			<%for(TimeConfTmpPo po: tmpList){ %>
				<tr >
					<td><%=po.getTmpId() %></td>
					<td><%=po.getAliasLogName() %></td>
					<td><%=po.getClassName()%></td>
					<!-- <td><%=po.getFilePath()%></td> -->
					<td><%=po.getSplitChar() %></td>
					<!--<td><%=po.getAnalyseFuture()%></td> -->
					<td><%=po.getAnalyseFrequency()%></td>
					<td><%=po.getTailType()%></td>
					<td>
						<%if(po.getObtainType()==1){out.print("��־�ļ�");}
						if(po.getObtainType()==2){out.print("shell��ʽ");} 
						if(po.getObtainType()==3){out.print("http��ʽ");}
						%>
					</td>
					<td>
						<%  if(po.getAnalyseType()==1){out.print("java-class");}
							if(po.getAnalyseType()==2){out.print("javscript");} 
						%>
					</td>
					<td>
						<a href="<%=request.getContextPath()%>/show/appconfig.do?method=gotoTimeTmpUpdate&tmpId=<%=po.getTmpId()%>">�޸�</a>&nbsp;&nbsp;
						<a id="deleteAction" class="deleteAction" href="javascript:deleteTmp(<%=po.getTmpId() %>)">ɾ��</a> &nbsp;&nbsp;
						<a href="<%=request.getContextPath()%>/show/appconfig.do?method=gotoTimeTmpCheck&tmpId=<%=po.getTmpId()%>">�鿴</a> &nbsp;&nbsp;
					</td>
				</tr>
				<%} %>
			</tbody>
		</table>
		<footer>
		  <p>&copy; TaoBao 2011</p>
		</footer>
	</div>
</div>
</body>
</html>