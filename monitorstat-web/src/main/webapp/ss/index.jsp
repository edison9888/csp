<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.ss.SsdetailReportManager"%>
<%@page import="com.taobao.monitor.web.ss.StableSwitch"%>
<%@page import="com.taobao.monitor.web.ss.StableSwitchGroup"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>CSP-SS</title>
</head>
<body>
<style type="text/css">
TABLE {
	border-collapse: collapse;
	border-left: solid 1 #000000;
	border-top: solid 1 #000000;
	padding: 5px;
	font-family: arial, helvetica, sans-serif;
	border-color: #666666;
}

TH {
	border-right: solid 1 #000000;
	border-bottom: solid 1 #000000;
	background-color: #20D0D0;
	border-color: #666666;
}

TD {
	border-right: solid 1 #000000;
	border-bottom: solid 1 #000000;
	border-color: #666666;
	padding: 2px;
	height: 15px;
	font-size: 12px;
}

h3 {
	background: none repeat scroll 0 0 #E8E8D0;
	color: #B00040;
	font-family: helvetica, arial;
	font-size: 20px;
	font-weight: bold;
	margin-bottom: 0;
	margin-top: 0
}
</style>
<%
String app = request.getParameter("app");
if (app == null) {
    app = "detail";
}
%>
<h3>&nbsp;&nbsp;App:<%=app %> &nbsp;&nbsp;&nbsp;&nbsp;switch to:
<a href="?app=detail">detail</a>
<a href="?app=login">login</a>
<a href="?app=shopsystem">shopsystem</a>
<a href="?app=tf_buy">tf_buy</a>
<a href="?app=hesper">hesper</a>
<a href="?app=tf_tm">tf_tm</a> 
&nbsp;
&nbsp;<a href="">脚本控制台</a>
</h3><br>

<table >
	
	<tr>
	
		<td width="480" align="center">
		
		<table border="1" >
			<tr align="center">
				 <th scope="col">&nbsp;hosts</th>
				<th scope="col" width="40">&nbsp;key</th>
				<th scope="col" width="40">cv</th>
				<th scope="col" width="40">count</th>
				<th scope="col" width="40">pass</th>
				<th scope="col" width="40">qps</th>
				<th scope="col" width="40">block</th>
				<th scope="col" width="40">avgvalve</th>
				<th scope="col" width="40">avg</th>
				<th scope="col" width="40">type</th>
			</tr>
			<%
				Long startTime = System.currentTimeMillis();
			    List<StableSwitchGroup> ssgs = SsdetailReportManager.report(app, "7001", "/command.sph?command=show");
						    List<StableSwitch> sss = null;
						    String collectIPs="";
						    for (StableSwitchGroup ssg : ssgs) {
						        sss = ssg.getStableSwitchs();
			%>
		 


			<%
						if(sss.isEmpty())sss.add(new StableSwitch());
		 			    boolean rowspan = true;
		 						    for (StableSwitch ss : sss) {
		 			%>
			<tr bgcolor="#C0FFC0">
			    <%
			        if(rowspan) {rowspan=false;collectIPs+=ssg.getAddress()+",";
			    %>
				<td   rowspan="<%=ssg.getStableSwitchs().size() %>"><%=ssg.getAddress()%></td><%
				    }
			    
				%>
				<td ><%=ss.getKey()%></td>
				<td><%=ss.getCountValve()%></td>
				<td><%=ss.getCount()%></td>
				<td><%=ss.getPass()%></td>
				<td><%=ss.getQps()%></td>
				<td <%if(ss.getBlock() > 0){ %>bgcolor="#FFCC00"<%} %>><%=ss.getBlock() %></td>
				<td><%=ss.getAvgValve()%></td>
				<td><%=ss.getAvg()%></td>
				<td><%=ss.getType()%></td>
			</tr>
			<%
			    }
			%>

			<%
			    }
				Long endTime = System.currentTimeMillis();
				System.out.println("report on index.jsp:"+(endTime-startTime));
			%>
		</table>
		</td>
		<td valign="top" width="500">
		<table border="1" width="400">

			<tr>

				<th scope="col">&nbsp;key</th>
				<th scope="col">countvalve</th>

				 
				<th scope="col">avgvalve</th>

				<th scope="col">type</th>
			</tr>
			<form action="action.jsp" method="post">
			<%
			    for (StableSwitch ss : sss) {
			%>
			<tr>
				<td width="50"><%=ss.getKey()%><input size="10" type="hidden"  name="key"
					value="<%=ss.getKey()%>" /></td>
				<td><input size="10" type="text" name="countvalve"
					value="" />(数字)</td>

				 
				<td><input size="10" type="text" name="avgvalve"
					value="" />(数字)</td>

				<td><input size="10" type="text" name="type"
					value="" />(0 or 2 or 3)</td>
			</tr>
			<%
			    }
			%>
			<tr>
				<td colspan="5" valign="top">apply to hosts:多个ip使用","分隔 <br>
				<textarea name="hosts" cols="50" rows="6"><%=collectIPs%></textarea> <br>
				apply to hosts group：
				<br><input type="radio" name="hostgroup" value="0" /> ALL 
				<br><input type="radio" name="hostgroup" value="3" /> CM3
				<br><input type="radio" name="hostgroup" value="4" /> CM4</td>
			</tr>
			<tr>
				<td colspan="5" align="center">
				<input type="hidden" name="app" value="<%=app %>" />
				checkCode:<input size="10" type="password" name="checkcode" value="" />&nbsp;&nbsp;
				<input type="submit" name="Submit" value="提交" /></td>
			</tr>
			</form>
		</table>
		</td>
		<td valign="top" width="500">
			<table border="1">
			<tr>
			<td>
			<% 
				collectIPs=collectIPs.replaceAll(","," ");
				String shell="hosts = ("+collectIPs+")\nfor host in ${hosts[*]}\n do\n curl \"http://$host:7001/command.sph?command=set&AsynchronousCall.cv=1\" \n done";
			%>
			<textarea name="shellscript" cols="80" rows="30"><%=shell %></textarea>
			</td>
			</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>