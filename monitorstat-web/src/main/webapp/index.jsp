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
<title>���</title>
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
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�ձ�ģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="./index_day.jsp" target="_blank">�����ձ�������</a>
			</span>
			</div>
			</div>			
			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">ʵʱģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="http://time.csp.taobao.net:9999/time/index_table.jsp" target="_blank">����ʵʱ������</a>
			</span>
			<span>
			<a href="http://time.csp.taobao.net:9999/time/index.jsp" target="_blank">����ʵʱ������</a>
			</span>
			</div>
			</div>
			
			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�ܱ�ģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="./week_report.jsp" target="_blank">�����ܱ�������</a>
			</span>
			</div>
			</div>
			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">����Ӧ������ģ��</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<span>
					<a href="http://depend.csp.taobao.net:9999/depend/show.do?method=showMeDependTable" >Ӧ������</a>
					</span>
					<br/>	
					<span>
					<a href="<%=request.getContextPath () %>/tair/tair_show.jsp" >Ӧ������Tair�ֲ�</a>
					</span>
					<br/>	
					<span>
					<a href="<%=request.getContextPath () %>/tair/tair_top_new.jsp" >Tair���ֲܷ�</a>
					</span>
					<br/>	
					<span>
					<a href="http://depend.csp.taobao.net:9999/depend/show/hsfprovide.do?method=showAppCenterHsfInfo&opsName=itemcenter&selectDate=<%=sdf.format(cal.getTime()) %>" >HSF�����ֲ�</a>
					</span>
					<br/>
					<span>
					<a href="http://depend.csp.taobao.net:9999/depend/tddl/show.do?method=list" >TDDL�����ֲ�</a>
					</span>
				</div>
			</div>
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">���������滮ģ��</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<br/>
					<span>
					<a href="http://autoload.taobao.net:9999/autoload/loadrun/show.do?method=list" >���������Զ���־ѹ��</a>
					</span>
					<br/>
					<span>
					<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit" >��������<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">�¹���</span></a>
					</span>
					<br/>
					<span>
					<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityApp" >����Ӧ��ά��<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">�¹���</span></a>
					</span>
					<br/>
					<span>
					<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showMachine" >������ʹ��Ԥ��<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">�¹���</span></a>
					</span>
					<br/>	
					<span>
					<a href="http://capacity.taobao.net:9999/capacity/capacity/CapacityDenpend.jsp" >��������ͼ<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">�¹���</span></a>
					</span>
				</div>
			</div>			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��������ģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="./center/manage_center.jsp" target="_blank">�������Ľ���</a>
			</span>
			<br/>
			<span>
			</span>
			<br/>
			<br/>	
			</div>
			</div>	
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ع���ģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
			<span>
			<a href="./alarm/manage_key.jsp" target="_blank">����澯key�������</a>
			</span>
			<br/>
			<span>
			<a href="./user/manage_user.jsp" target="_blank">����澯�û��������</a>
			</span>
			<br/>			
			<span>
			<a href="./alarm/alarm_record_exception1.jsp" target="_blank">�����쳣��Ϣ�鿴����</a>
			</span>
			<br/>
			<span>
			<a href="./alarm/alarm_filter.jsp" target="_blank">����Ӧ���Ƿ�澯����</a>
			</span>
			<br/>
			<span>
			<a href="<%=request.getContextPath()%>/reportConfig.do" target="_blank">���뱨�����ý���</a>
			</span>
			<br/>
			<span>
			<a href="<%=request.getContextPath()%>/impUrl.jsp" target="_blank">������ҪURL���ƽ���</a>
			</span>
			</div>
			</div>
		</td>
		<td width="10%" valign="top" align="center">
			&nbsp;
			
			
		</td>
		<td width="40%" valign="top">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><span style="top: 0pt; color: rgb(255, 0, 0);">====��Ҫ����====</span></div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<span>
					<a href="http://time.csp.taobao.net:9999/time/index.jsp" target="_blank"><span style="top: 0pt; font-size: 20px; color: rgb(255, 0, 0);">��ʵʱ���ϵͳ</span></a>
					</span>
				</div>
			</div>
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">���õ�ַ����</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<span>
					<a href="http://app.ops.taobao.net/javahost">�Ա����������б�</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://10.232.35.87:8080/depend/appAppDepend.do">Ӧ��������ϵ����ϵͳ</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://dba.tools.taobao.com:9999/workspace.htm">�Ա�Web SQL Plus</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://code.taobao.org/">�����-��Դ����</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://baike.corp.taobao.com/index.php/TBOSS/charge">�Ա��շ�(���)�ٿ�</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://baike.corp.taobao.com/index.php/TBOSS/charge/Monitor">�Ա��շ�(���)��ذٿ�</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://cm.taobao.net:9999/monitorstat/webww.html">web����ʵʱ��������</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://cm.taobao.net:9999/monitorstat/mpp.html">�Ա���½�û�ʵʱ��������</a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://121.0.25.45:9999/repository.htm">���ؿ�������<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">�¹���</span></a>
					</span>
					<br/>
					<br/>
					<span>
					<a href="http://hotspot.taobao.net:9999/hotspot">JprofӦ��<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">�¹���</span></a>
					</span>
				</div>
			</div>
			
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix" >���¸���</div>
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