<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<!doctype html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<title>交易关联监控系统之——用户购买链路监控系统</title>
	<link href="<%=request.getContextPath() %>/statics/css/alarm-index.css" rel="stylesheet" type="text/css" />
	<!-- JQuery is the public JS -->
	<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/alarm/flash/total.js"></script>
</head>
<body>
<!-- start flashCharts -->
<div id="container">
	<!-- start header -->
		<div id="header">
			<div class="title"><a class="hidelink" href="" onClick="return false;" title="回到监控首页">交易平台总监控系统</a></div>
			<div class="menulist">
               <ul>
                   <li><a class="selected" href="./index.jsp" target="_blank">首页</a></li>
                   <li><a href="./trade_alarm_relate.jsp" target="_blank">关联监控</a></li>
                   <li><a href="./tradeplatform_relate.jsp" target="_blank">tp监控页</a></li>
                   <li><a href="./tf_tm_relate.jsp" target="_blank">tm监控页</a></li>
                   <li><a href="./tf_buy_relate.jsp" target="_blank">buy监控页</a></li>
                   <li><a href="./cart_relate.jsp" target="_blank">cart监控页</a></li>
                   <li><a href="./auctionplatform_relate.jsp" target="_blank">拍卖ap监控</a></li>
                   <li class="menutail"><a href="./tradeapi_relate.jsp" target="_blank">Tradeapi</a></li>
               </ul>
		  	</div>
		</div>
	<!-- end header -->
	<div id="taobaoapp">
		<div class="stat-space">
			<div id="flashdiv">
				<object id="flashobj" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0" width="1024" height="1100">
					<param name="allowScriptAccess" value="sameDomain">
					<param name="movie" value="flash/total.swf">
					<param name="quality" value="high">
					<param name="menu" value="true">
					<embed src="flash/total.swf" menu="false" quality="high" width="1024" height="1100" allowscriptaccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" name="flashobj">
				</object>
			</div>
		</div>
	</div>
</div>
</body>
</html>
