<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.time.ao.AppHSFQueryAo"%>
<!doctype html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<title>���׹������ϵͳ֮�����û�������·���ϵͳ</title>
	<link href="<%=request.getContextPath() %>/statics/css/alarm-index.css" rel="stylesheet" type="text/css" />
	<!-- JQuery is the public JS -->
	<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/FusionCharts.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/alarm/flash/swfobject.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/alarm/flash/common.js"></script>
</head>
<body>
<!-- start flashCharts -->
<div id="container">
	<!-- start header -->
		<div id="header">
			<div class="title"><a class="hidelink" href="" onClick="return false;" title="�ص������ҳ">����ƽ̨�ܼ��ϵͳ</a></div>
			<div class="menulist">
               <ul>
                   <li><a class="selected" href="./index.jsp" target="_blank">��ҳ</a></li>
                   <li><a href="./trade_alarm_relate.jsp" target="_blank">�������</a></li>
                   <li><a href="./tradeplatform_relate.jsp" target="_blank">tp���ҳ</a></li>
                   <li><a href="./tf_tm_relate.jsp" target="_blank">tm���ҳ</a></li>
                   <li><a href="./tf_buy_relate.jsp" target="_blank">buy���ҳ</a></li>
                   <li><a href="./cart_relate.jsp" target="_blank">cart���ҳ</a></li>
                   <li><a href="./auctionplatform_relate.jsp" target="_blank">����ap���</a></li>
                   <li class="menutail"><a href="./tradeapi_relate.jsp" target="_blank">Tradeapi</a></li>
               </ul>
		  	</div>
		</div>
	<!-- end header -->
	<div id="taobaoapp">
		<div id="shareport" class="table-space">
			<h4><span class="sharereport">�������ϵͳ����ʵʱ���ϵͳ</span></h4>
			<div class="loadingimg"><img src="pics/ajax-loading.gif" alt="�����͸�����Ϣ����" /></div>
		</div>
		<div class="chart-space">
			<div id="flashdiv">
				<object id="flashobj" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0" width="1200" height="400">
					<param name="allowScriptAccess" value="sameDomain">
					<param name="movie" value="flash/chart.swf">
					<param name="quality" value="high">
					<param name="menu" value="false">
					<embed src="flash/chart.swf" menu="false" quality="high" width="1200" height="400" allowscriptaccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" name="flashobj">
				</object>
			
			</div>
		</div>
		<div class="chart">
			<div id="chartdiv" align="center">Chart will load here</div>
			<div id="tmallchartdiv" align="center">Chart will load here</div>
		</div>
		<div id="tb-login" class="table-space">
			<h4><a target="_blank" href="http://time.csp.taobao.net:9999/time/app/detail/apache/show.do?method=showIndex&appId=16">login</a>PV</h4>
			<div class="loadingimg"><img src="pics/ajax-loading.gif" alt="����pv��Ϣ" /></div>
			<div class="line-chart">
				<div id="loginchart" app-name="login" key-name="PV`http://login.taobao.com/member/login.jhtml">�Ա��û���¼ͬ�ȵ�����ͼ</div>
			</div>
		</div>
		<div id="tb-detail" class="table-space">
			<h4><a target="_blank" href="http://time.csp.taobao.net:9999/time/app/detail/apache/show.do?method=showIndex&appId=1">detail</a>PV</h4>
			<div class="loadingimg"><img src="pics/ajax-loading.gif" alt="����pv��Ϣ" /></div>
			<div class="line-chart">
				<div id="detail" app-name="detail" key-name="PV`http://item.taobao.com/item.htm">��Ʒ����ҳͬ������ͼ</div>
			</div>
		</div>
		<div id="tb-malldetail" class="table-space">
			<h4><a target="_blank" href="http://time.csp.taobao.net:9999/time/app/detail/apache/show.do?method=showIndex&appId=369">mallDetail</a>PV</h4>
			<div class="loadingimg"><img src="pics/ajax-loading.gif" alt="����pv��Ϣ" /></div>
			<div class="line-chart">
				<div id="tmalldetail" app-name="malldetail" key-name="PV`http://detail.tmall.com/item.htm">��è��Ʒ����ҳͬ�ȵ�����ͼ</div>
			</div>
		</div>
		<div id="tb-cart" class="table-space">
			<h4><a target="_blank" href="http://time.csp.taobao.net:9999/time/app/detail/apache/show.do?method=showIndex&appId=341">cart</a>����·���ϵ�PV</h4>
			<div class="loadingimg"><img src="pics/ajax-loading.gif" alt="����pv��Ϣ" /></div>
			<div class="line-chart">
				<div id="cart" app-name="cart" key-name="PV`http://cart.taobao.com/my_cart.htm">�Ա����ﳵͬ�ȵ�����ͼ</div>
			</div>
		</div>
		<div id="tb-tfbuy" class="table-space">
			<h4><a target="_blank" href="http://time.csp.taobao.net:9999/time/app/detail/apache/show.do?method=showIndex&appId=330">tf_buy</a>����·���ϵ�PV</h4>
			<div class="loadingimg"><img src="pics/ajax-loading.gif" alt="����pv��Ϣ" /></div>
			<div class="line-chart">
				<div id="tf_buy" app-name="tf_buy" key-name="PV`http://buy.taobao.com/auction/buy_now.jhtml">�Ա���������ͬ�ȵ�����ͼ</div>
			</div>
		</div>
		<div id="tb-tmallbuy" class="table-space">
			<h4><a target="_blank" href="http://time.csp.taobao.net:9999/time/app/detail/apache/show.do?method=showIndex&appId=379">tmallBuy</a>����·���ϵ�PV</h4>
			<div class="loadingimg"><img src="pics/ajax-loading.gif" alt="����pv��Ϣ" /></div>
			<div class="line-chart">
				<div id="tmallbuy" app-name="tmallbuy" key-name="PV`http://buy.tmall.com/order/confirm_order.htm">��è��������ͬ�ȵ�����ͼ</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var dataString = "<%=AppHSFQueryAo.get().getTaobaoFlashXML(2)%>";
	var chart = new FusionCharts("flash/Pyramid.swf", "ChartId", "600", "468", "0", "1" );
	chart.setXMLData( dataString );
	chart.render("chartdiv");
	var tmallDataString = "<%=AppHSFQueryAo.get().getTmallFlashXML(2)%>";
	var tmall = new FusionCharts("flash/Pyramid.swf", "tmallChartId", "600", "468", "0", "1" );
	tmall.setXMLData( tmallDataString );
	tmall.render("tmallchartdiv");
</script>
</body>
</html>
