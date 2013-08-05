<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.alarm.source.constants.CommonConstants"%>
<%@page import="com.taobao.monitor.web.util.DateFormatUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date endTime = new Date();
	Date startTime = DateFormatUtil.getTime(endTime, CommonConstants.TIME_TYPE, CommonConstants.STARTTIME_BEFORE);
%>
<!doctype html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<title>����ƽ̨����Tradeplatform���ͼ</title>
	<link href="<%=request.getContextPath() %>/statics/css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/statics/css/app-alarm.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-ui-timepicker-addon.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/app-effect.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/tp-init.js"></script>
</head>
<body>
<!-- start container -->
<div id="container">
	<!-- start banner -->
	<div id="banner">
	<div id="movingboard">
		<div id="header">
			<div class="title"><a class="hidelink" href="./index.jsp" title="�ص������ҳ">����ƽ̨�������ϵͳ</a></div>
			<div class="menulist">
               <ul>
                   <li><a href="./index.jsp" target="_blank">��ҳ</a></li>
                   <li><a id="ascreen" href="#" onClick="return false;">�����</a></li>
                   <li><a id="ainfo" href="#" onClick="return false;">������Ϣ</a></li>
                   <li><a href="./trade_alarm_relate.jsp">�������</a></li>
                   <li><a class="selected" href="./tradeplatform_relate.jsp">tp���ҳ</a></li>
                   <li><a href="./tf_tm_relate.jsp">tm���ҳ</a></li>
                   <li><a href="./tf_buy_relate.jsp">buy���ҳ</a></li>
                   <li><a href="./cart_relate.jsp">cart���ҳ</a></li>
                   <li><a href="./auctionplatform_relate.jsp">����ap���</a></li>
                   <li class="menutail"><a href="./tradeapi_relate.jsp">Tradeapi</a></li>
               </ul>
		  	</div>
		</div>
		<div id="date-selected">
		  <div class="picker">
		     <div class="btnarea">
		     <form action="tradeplatform_relate.jsp" id="date-picker" method="get">
		      <div id="date2">
		      <label>��ѯĳ��ʱ���ϵͳ״�� From</label>
		        <input class="tpicker" id="fromtimepicker" type="text" name="picktime" value="" />
		      	<label>To</label>
		        <input class="tpicker"  id="totimepicker" type="text" name="picktime" value="" />
		        <select id="opselect">
					<option value="0">P0������</option>
					<option value="2">P2������</option>
					<option value="-1">���и澯</option>
				</select>
		        <input id="timesearch" type="submit" value="�ύ"/>
		      </div>
		    </form>
		    <a id="dragscreen" href="#" onClick="return false;"><img src="pics/down.gif" title="������������Ļ" alt="����Ļ" /></a>
		    </div>
		  </div>
		</div>
	</div>
<!-- start alarminfo-->
	<div id="alarminfo">
		<div class="scrollboard">
			<div class="scrollwrapper">
				<a id="view-btn" href="#" onClick="return false;" >View All</a>
				<div id="scrolltext">
					<div id="tab1">
						<div class='loading'><img src='pics/loading.gif' /></div>
						<div id="content">
							<div id="starttime"><%=sdf.format(startTime)%></div>
							<div id="endtime"><%=sdf.format(endTime)%></div>
						</div>
					</div>
				</div>
			</div>
			<div class="arrow-board">	
				<a id="sear-left" href="#" onClick="return false;"><img title="չʾǰ20���ӱ�����Ϣ" src="pics/left.png" /></a>
				<a id="sear-now" href="#" onClick="return false;"><img title="չʾ��ǰ������Ϣ" src="pics/now.png" /></a>
				<a id="sear-right" href="#" onClick="return false;"><img title="չʾ��20���ӱ�����Ϣ" src="pics/right.png" /></a>
			</div>
		</div>
	</div>
	<!-- end alarminfo absolute-->
	</div>
	<!-- start banner -->	
	<div class="flashwrapper">
		<div id="flashdiv">
			<object id="flashobj" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0" width="1024" height="768">
				<param name="allowScriptAccess" value="sameDomain">
				<param name="movie" value="flash/tp.swf">
				<param name="quality" value="high">
				<param name="menu" value="false">
				<param name="wmode" value="transparent">
				<embed src="flash/tp.swf" wmode="transparent" menu="false" quality="high" width="1024" height="768" allowscriptaccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" name="flashobj">
			</object>
		
		</div>
	</div>
</div>
<!-- end container -->
<!-- start datachanged absolute-->
<div id="datachanged"></div>
<!-- end datachanged absolute-->
<!-- start moving buttn -->
<div id="wrapall"><a href="#" onClick="return false;"><img src="pics/wrap.png" /></a></div>
<!-- end moving buttn -->
<!-- start help board -->
<div id="help">
	<div id="tip"> ��Ȧ���� ��ʾ�ýڵ㷢���˱��������������� ��ʾ�ñ����ڵ����Ϊ����Դͷ ��ɫ����ڵ� ���Ӽ��ҳ�棬����˵�����ѡ��鿴���ݣ������ͷչʾ������Ϣ�����ȫ��չʾ������ť</div>
	<a id="help-close" href="#" onClick="return false;"></a>
</div>
<div id="selecttip"></div>
<div id="etimetip"></div>
<div id="tairinfo" onClick="closeKeyDesc();"></div>
<!-- end help board -->
<div id="appId"><%=AppInfoAo.get().getTfBuyRelateAppMap().get("tradeplatform") %></div>
<script type="text/javascript">
	$('#fromtimepicker').datetimepicker({
		showButtonPanel: false,
		showSecond: true,
		timeFormat: 'hh:mm:ss'
	});
	$('#totimepicker').datetimepicker({
		showButtonPanel: false,
		showSecond: true,
		timeFormat: 'hh:mm:ss'
	});
</script>
</body>
</html>
