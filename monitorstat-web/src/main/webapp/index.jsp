<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.ServerInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.ServerInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>


<style type="text/css">

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

Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, -1);

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

%>

<jsp:include page="head.jsp"></jsp:include>

<table>
	<tr>
		<td width="40%"  valign="top">			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">日报模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="./index_day.jsp" target="_blank">进入日报主界面</a>
			</span>
			</div>
			</div>			
			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">实时模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="http://time.csp.taobao.net:9999/time/index_table.jsp" target="_blank">进入实时主界面</a>
			</span>
			<span>
			<a href="http://time.csp.taobao.net:9999/time/index.jsp" target="_blank">进入实时主界面</a>
			</span>
			</div>
			</div>
			
			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">周报模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="./week_report.jsp" target="_blank">进入周报主界面</a>
			</span>
			</div>
			</div>
			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">线上应用依赖模块</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<span>
					<a href="http://depend.csp.taobao.net:9999/depend/show.do?method=showMeDependTable" >应用依赖</a>
					</span>
					<br/>	
					<span>
					<a href="<%=request.getContextPath () %>/tair/tair_show.jsp" >应用消耗Tair分布</a>
					</span>
					<br/>	
					<span>
					<a href="<%=request.getContextPath () %>/tair/tair_top_new.jsp" >Tair汇总分布</a>
					</span>
					<br/>	
					<span>
					<a href="http://depend.csp.taobao.net:9999/depend/show/hsfprovide.do?method=showAppCenterHsfInfo&opsName=itemcenter&selectDate=<%=sdf.format(cal.getTime()) %>" >HSF流量分布</a>
					</span>
					<br/>
					<span>
					<a href="http://depend.csp.taobao.net:9999/depend/tddl/show.do?method=list" >TDDL流量分布</a>
					</span>
				</div>
			</div>
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">线上容量规划模块</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<br/>
					<span>
					<a href="http://autoload.taobao.net:9999/autoload/loadrun/show.do?method=list" >进入线上自动日志压测</a>
					</span>
					<br/>
					<span>
					<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit" >容量排行<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">新功能</span></a>
					</span>
					<br/>
					<span>
					<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityApp" >容量应用维护<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">新功能</span></a>
					</span>
					<br/>
					<span>
					<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showMachine" >服务器使用预测<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">新功能</span></a>
					</span>
					<br/>	
					<span>
					<a href="http://capacity.taobao.net:9999/capacity/capacity/CapacityDenpend.jsp" >容量依赖图<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">新功能</span></a>
					</span>
				</div>
			</div>			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">控制中心模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="./center/manage_center.jsp" target="_blank">控制中心界面</a>
			</span>
			<br/>
			<span>
			</span>
			<br/>
			<br/>	
			</div>
			</div>	
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">监控管理模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="./alarm/manage_key.jsp" target="_blank">进入告警key管理界面</a>
			</span>
			<br/>
			<span>
			<a href="./user/manage_user.jsp" target="_blank">进入告警用户管理界面</a>
			</span>
			<br/>			
			<span>
			<a href="./alarm/alarm_record_exception1.jsp" target="_blank">进入异常信息查看界面</a>
			</span>
			<br/>
			<span>
			<a href="./alarm/alarm_filter.jsp" target="_blank">进入应用是否告警控制</a>
			</span>
			<br/>
			<span>
			<a href="<%=request.getContextPath()%>/reportConfig.do" target="_blank">进入报表配置界面</a>
			</span>
			<br/>
			<span>
			<a href="<%=request.getContextPath()%>/impUrl.jsp" target="_blank">进入重要URL走势界面</a>
			</span>
			</div>
			</div>
		</td>
		<td width="10%" valign="top" align="center">
			&nbsp;
			
			
		</td>
		<td width="40%" valign="top">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><span style="top: 0pt; color: rgb(255, 0, 0);">====重要更新====</span></div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<span>
					<a href="http://time.csp.taobao.net:9999/time/index.jsp" target="_blank"><span style="top: 0pt; font-size: 20px; color: rgb(255, 0, 0);">新实时监控系统</span></a>
					</span>
				</div>
			</div>
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">常用地址链接</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<span>
					<a href="http://app.ops.taobao.net/javahost">淘宝生产主机列表</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://10.232.35.87:8080/depend/appAppDepend.do">应用依赖关系管理系统</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://dba.tools.taobao.com:9999/workspace.htm">淘宝Web SQL Plus</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://code.taobao.org/">淘蝌蚪-开源代码</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://baike.corp.taobao.com/index.php/TBOSS/charge">淘宝收费(汇金)百科</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://baike.corp.taobao.com/index.php/TBOSS/charge/Monitor">淘宝收费(汇金)监控百科</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://cm.taobao.net:9999/monitorstat/webww.html">web旺旺实时在线人数</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://cm.taobao.net:9999/monitorstat/mpp.html">淘宝登陆用户实时在线人数</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://121.0.25.45:9999/repository.htm">开关控制中心<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">新功能</span></a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://hotspot.taobao.net:9999/hotspot">Jprof应用<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">新功能</span></a>
					</span>
				</div>
			</div>
			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix" >最新更新</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content" style="overflow:scroll;height:620px" >
					<table width="200" border="1">
						<thead>
							<tr>
								<td></td>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</td>
	</tr>
</table>
<jsp:include page="buttom.jsp"></jsp:include>
</body>
</html>