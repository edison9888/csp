<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.csp.depend.po.report.StrongWeakAutoCheckReport"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" >
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>强弱依赖自动检测报表</title>
<style type="text/css">
*{
	font-size:13px;
}
html,body{
	margin:0;
	padding:0;
}
body{
    font-family:Arial,Helvetica,"Nimbus Sans L",sans-serif;
	background:#fafafa;
	text-align:center;
}
h3{
	font-size:20px;
	font-weight:bold;
	margin:12px 0;
}
h4{
	font-size:16px;
	font-weight:bold;
	color:#CC0000;
	margin:10px 0;
}
h5{
	font-size:14px;
	font-weight:bold;
	color:#990000;
	margin:5px 0;
}

.mian_body {
		width:100%;
		float: left;
		background-color: #E7EEFE;
		border: 1px solid #ccc;
		border-radius: 5px;
		-moz-border-radius: 5px;
		-webkit-border-radius: 5px;
	}
	
	.table_comm {
		width:100%;
		float: left;
		background-color: #E7EEFE;
		border: 1px solid #ccc;
		border-radius: 5px;
		-moz-border-radius: 5px;
		-webkit-border-radius: 5px;
	}
	
	.table_comm  td {
	border:1px solid #99BBE8;
	background: #fff;
	color: #4f6b72;
}

div {
	font-size: 12px;
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
border: 1px solid #DDDDDD;
}

h2{
	padding-left: 50px;
}

thead{
	background: #fff;
	color: #4f6b72;
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
	background: url(<%=request.getContextPath()%>/statics/images/4_17.gif);
}
img {
cursor:pointer;
}
.report_on{background:#bce774;}

.firstTitleTd {
	font-weight: bold;	
	background: #1FB5D0;
	font-size: 100%;
}

.secondTitleTd {
	background: #1FB5D0;
}
</style>

<%
Map<String, List<StrongWeakAutoCheckReport>> dependMap = (Map<String, List<StrongWeakAutoCheckReport>>) request
      .getAttribute("dependMap");
String[] appArray = (String[]) request.getAttribute("appArray");
if(dependMap == null) {
  dependMap =  new HashMap<String, List<StrongWeakAutoCheckReport>>();
}
if(appArray == null) {
  appArray = new String[0];
}
%>
<%!

//根据status 返回颜色
String getColorByStatus(String status) {
  
  if(status != null && status.equals("强")) {
    return "red";
  } 
  return "green";
}

//根据status 返回颜色
String getTextByConfigFlag(String text, boolean isConfig) {

  if(isConfig) {
	return text;
  } else {
    return "-";	//"-"表示脚本未配置
  }
}

%>
</head>
<body>
	<h3>报表说明</h3>
	<table width="90%" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<td class="firstTitleTd" style="background-color: #A8E0ED;"><h2>&nbsp;表格说明，<a href="http://10.232.135.198:8080/depend/dailycheck.do?method=gotoIndexPage">强弱依赖检查系统（2套环境）</a></h2></td>
		<td class="firstTitleTd" style="background-color: #A8E0ED;"><h2>&nbsp;描述</h2></td>
	</tr>
	<tr>
		<td>检测结果</td>
		<td align="left">&nbsp;"<span style='color:red;'>异常</span>"表示"<strong>预想强弱</strong>"和实际"<strong>强弱验证结果</strong>"不一致。"<strong>强弱验证结果</strong>"有一项为"强"则表示<span style="color: red;">强依赖</span>。
		</td>
	</tr>		
	<tr>
		<td>屏蔽启动</td>
		<td align="left">&nbsp;在应用启动前，屏蔽掉应用间的网络通信。</td>
	</tr>
	<tr>
		<td>屏蔽运行</td>
		<td align="left">&nbsp;在应用成功启动后，屏蔽掉应用间的网络通信。</td>
	</tr>
	<tr>
		<td>延迟启动</td>
		<td align="left">&nbsp;在启动应用前，干扰应用间的网络通信，模拟网速较差的网络环境。</td>
	</tr>
	<tr>
		<td>延迟运行</td>
		<td align="left">&nbsp;在启动应用前，干扰应用间的网络通信，模拟网速较差的网络环境。</td>
	</tr>
	<tr>
		<td>检测过程</td>
		<td align="left">&nbsp;可以通过报表中"<strong>查看详细</strong>"的超链接查看检测的具体过程（有检测截图和具体步骤描述）
		</td>
	</tr>		
	<tr>
		<td class="firstTitleTd" style="background-color: #A8E0ED;"><h2>检测方式</h2></td>
		<td class="firstTitleTd" style="background-color: #A8E0ED;"><h2>描述</h2></td>
	</tr>	
	<tr>
		<td>URL检测</td>
		<td align="left">&nbsp;访问被检测的url，查找特定文字来检测。</td>
	</tr>
	<tr>
		<td>Selenium脚本检测</td>
		<td align="left">&nbsp;通过Selenium调用浏览器，模拟人机过程，交互流程正常则认为检测成功。
		<a href="<%=request.getContextPath()%>/show/reportaction.do?method=showStandardCheckPicture" target="_blank" style="display: none;">查看全部检测脚本</a></td>
	</tr>
	<tr>
		<td>检测标准</td>
		<td align="left">&nbsp;在屏蔽或延迟操作后，通过URL或Selenium脚本可以完成正常的检测，则认为应用之间依赖关系为<span style="color:green;">弱依赖</span>，否则为<span style="color: red;">强依赖</span>，默认直连DB都为<span style="color: red;">强依赖</span>，不检查。
		</td>
	</tr>
	</thead>
	</table>
	<br/>
	<h3>应用依赖关系为${strDaysBefore}&nbsp;~&nbsp;${selectDate}的汇总数据（对比的数据为${strPreDate}的数据）</h3>
	<%
		for(String appName: appArray) {	//map列表
		  int i =1;
		  List<StrongWeakAutoCheckReport> poList = dependMap.get(appName); 
	%>
	<table width="90%" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<td colspan="11" class="firstTitleTd" style="background-color: #A8E0ED;">
				<h2>应用名称:<%=appName%></h2>
				</td>
			</tr>
			<tr>
				<td rowspan="2" align="center" width="4%">序号</td>
				<td rowspan="2" align="center" width="15%">依赖应用</td>
				<td rowspan="2" align="center" width="7.5%">应用类型</td>
				<td rowspan="2" align="center" width="7.5%">检测结果</td>
				<td rowspan="2" align="center" width="10%">预想强弱</td>
				<td colspan="4" align="center" width="36%">强弱验证结果</td>
				<td rowspan="2" align="center" width="10%">检测脚本</td>
				<td rowspan="2" align="center" width="10%" title="有图有真相"><strong>检测过程</strong></td>
			</tr>
			<tr>
				<td align="center" width="9%">屏蔽启动</td>
				<td align="center" width="9%">屏蔽运行</td>
				<td align="center" width="9%">延迟启动</td>
				<td align="center" width="9%">延迟运行</td>
			</tr>
		</thead>
		<tbody>
			<%
				for(StrongWeakAutoCheckReport po: poList) {
				  String startPreventIntensity = po.getCurCheckResult().get("startPreventIntensity");
				  String runPreventIntensity = po.getCurCheckResult().get("runPreventIntensity");
				  String startDelayIntensity = po.getCurCheckResult().get("startDelayIntensity");
				  String runDelayIntensity = po.getCurCheckResult().get("runDelayIntensity");
				  
				  boolean isReduce = po.getTargetAppStatus() == StrongWeakAutoCheckReport.STATUS_REDUCE;
				  boolean isAdd = po.getTargetAppStatus() == StrongWeakAutoCheckReport.STATUS_ADD;
				  boolean isCheckConfig = po.isCheckConfig();	//脚本是否配置
			%>	  
				<tr>
					<td align="center"><%=i++%></td>
					<td 
					<%
						if(isReduce)
							out.print("style='background-color:#C0C0C0'");
					 %>
					 ><%=po.getTargetOpsName()%>
					<% 
						if(isAdd)
						  out.print("<font color='red'>新增</font>");
						else if(isReduce)
					  	out.print("<font color='red'>减少</font>");
					%>
					</td>
					<td align="center">
					<%
					if(po.getTargetAppType() != null)
					  out.print(po.getTargetAppType());
					else 
					  out.print("<strong>未知</strong>");
					%>
					</td>
					<td align="center">
					<%
					String normal = "<span style='color:green;'>正常</span>";
					String valid = "<span style='color:red;'>异常</span>";
					if(!isCheckConfig) {
						out.println(normal);
					} else {
					  String expectDepIntensity = po.getExpectDepIntensity(); 
					  if(expectDepIntensity.equals("弱")) {
					    if("弱".equals(startPreventIntensity) && "弱".equals(runPreventIntensity) && "弱".equals(startDelayIntensity) && "弱".equals(runDelayIntensity)) {
					      out.println(normal);
					    } else {
					      out.println(valid);
					    }
					  } else {
					    if("强".equals(startPreventIntensity) || "强".equals(runPreventIntensity) || "强".equals(startDelayIntensity) || "强".equals(runDelayIntensity)) {
					      out.println(normal);
					    } else {
					      out.println(valid);
					    }
					  }
					}					
					%>
					</td>
					<td align="center"><font color="<%=getColorByStatus(getTextByConfigFlag(po.getExpectDepIntensity(),isCheckConfig))%>"><%=getTextByConfigFlag(po.getExpectDepIntensity(),isCheckConfig)%></font></td>
					<td align="center">
						<font color="<%=getColorByStatus(startPreventIntensity)%>"><%=getTextByConfigFlag(startPreventIntensity,isCheckConfig)%></font>
					</td>
					<td align="center">
						<font color="<%=getColorByStatus(runPreventIntensity)%>"><%=getTextByConfigFlag(runPreventIntensity,isCheckConfig)%></font>
					</td>
					<td align="center">
						<font color="<%=getColorByStatus(startDelayIntensity)%>"><%=getTextByConfigFlag(startDelayIntensity,isCheckConfig)%></font>
					</td>
					<td align="center">
						<font color="<%=getColorByStatus(runDelayIntensity)%>"><%=getTextByConfigFlag(runDelayIntensity,isCheckConfig)%></font>
					</td>
					<td align="center">
					<%
						if(isCheckConfig) {
						  %>
						  <a href="http://10.232.135.198:8080/depend/config.do?method=gotoCheckupConfig&opsName=<%=appName%>&targetOpsName=<%=po.getTargetOpsName()%>" target="_blank">已配置</a>
						  <%						  
						} else {
						  %>
						  <a style="color: red" href="http://10.232.135.198:8080/depend/config.do?method=gotoCheckupConfig&opsName=<%=appName%>&targetOpsName=<%=po.getTargetOpsName()%>" target="_blank">未配置</a>
						  <%
						}
					%>
					</td>
					<td align="center">
						<%
						if(!isCheckConfig) {
						  out.println("-");
						} else {
						  %>
						<a href="http://10.232.135.198:8080/depend/checkupdepend.do?method=showCheckResult&opsName=<%=appName%>&targetOpsName=<%=po.getTargetOpsName()%>" target="_blank">查看详细</a>						  
						  <%
						}
						%>
					</td>
				</tr>
		<%
			}	//end of inside for
		%>
		</tbody>
	</table>
		<%}	//end of outside for%>
	<br/>&nbsp;
	<br/>&nbsp;
</body>
</html>