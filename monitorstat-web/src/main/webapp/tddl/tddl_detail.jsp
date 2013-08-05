<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.tddl.TddlPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTddlAo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>TDDL 详细流量监控</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript">


</script>
<style type="text/css">
div {
	font-size: 12px;
}
#content {
	margin-top: 10px;
	margin-right: 10px;
	margin-bottom: 10px;
	margin-left: 10px;
	width:1300px
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
	font-family: "宋体";
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
</style>
</head>
<body>
<%
request.setCharacterEncoding("gbk");

String opsName = request.getParameter("opsName");
String dbName = request.getParameter("dbName");
String type = request.getParameter("type");
String collectTime = request.getParameter("collectTime");

String strPage = request.getParameter("intPage");

List<TddlPo> tddlList = MonitorTddlAo.getInstence().findTddlListByInfo(opsName, dbName, type, collectTime);

int intPageSize = 25;      //一页显示的记录数

int intRowCount = tddlList.size();      //记录的总数

int intPageCount = intRowCount/intPageSize + 1; // 总页数

if (strPage == null) {
	strPage = "1";
}

int intPage = Integer.parseInt(strPage);         //待显示的页码

int startRow = (intPage - 1) * intPageSize;
if (startRow > intRowCount) {
	out.print("请求数据错误");
	return;
}

int endRow = intPage * intPageSize - 1;
if (endRow >= intRowCount) {
	endRow = intRowCount - 1;
}



%>

<div id="main">
	<jsp:include page="../head.jsp"></jsp:include>
	<div id="content">
		<table width="100%" align="center">
			<tr>
			   <td align="center"><font style="color:red" size="5">【<%=opsName%>】</font></td>
			</tr>
		</table>
		
		<table width="300px" align="center">
			<tr align="center">
			<%
			if (intPage != 1) {
			%>
				<td width="33%">
					<a href="tddl_detail.jsp?opsName=<%=opsName %>&dbName=<%=dbName %>&type=<%=type %>&collectTime=<%=collectTime %>&intPage=<%=intPage-1 %>"> 《《上一页</a>
				</td>
			<%	
			} else {
				%>
				<td width="33%">
					首      页
				</td>
			<%	
			}
			%>
				<td width="33%">
					第<%=intPage%>页          共<%=intPageCount%>页
				</td>
			<%
			if (intPage != intPageCount) {	
				%>
				<td width="33%">
					<a href="tddl_detail.jsp?opsName=<%=opsName %>&dbName=<%=dbName %>&type=<%=type %>&collectTime=<%=collectTime %>&intPage=<%=intPage+1 %>"> 下一页》》</a>
				</td>
			<%	
			} else {
				%>
				<td width="33%">
				最 后  一页  
				</td>
			<%	
			}
			%>
			
			</tr>
		</table>
		
		
		<table width="100%" align="center"  border="1"  class="ui-widget ui-widget-content">
			<tr align="center" class="ui-widget-header ">
				<td width="5%">应用名</td>
				<td width="5%">数据库名</td>
				<td width="50%">执行 SQL</td>
				<td width="5%">类型</td>
				<td width="10%">执行次数</td>
				<td width="4%">响应时间</td>
				<td width="4%">主机IP</td>
				<td width="5%">主机机房</td>
				<td width="5%">时间</td>
			</tr>
			<%
			for (int i = startRow; i <= endRow; i++) {
				TddlPo po = tddlList.get(i);
			%>
			<tr>
				<td><%=po.getOpsName() %></td>
				<td><%=po.getDbName() %></td>
				<%
				if (po.getSqlText().length() > 80) {
					%>
					<td height='35' title='<%=po.getSqlText() %>'><%=po.getSqlText().substring(0,80)+"..." %></td>
					<%
				} else {
					%>
					<td height='35' title='<%=po.getSqlText() %>'><%=po.getSqlText() %></td>
					<%	
				}
				%>
				<td><%=po.getExecuteType() %></td>
				<td align="center"><%=Utlitites.fromatLong(po.getExecuteSum()+"") %></td>
				<td align="center"><%=po.getTimeAverage() %></td>
				<td align="center"><%=po.getHostIp() == null ? "-" : po.getHostIp() %></td>
				<td align="center"><%=po.getHostSite() == null ? "-" : po.getHostIp() %></td>
				<td><%=po.getCollectTime() %></td>
			</tr>
			<%
			}
			%>
		</table>
	</div>
</div>

</body>
</html>