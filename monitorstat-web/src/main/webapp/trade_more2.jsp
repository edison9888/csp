<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>

<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>


<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
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
border:0;
}
</style>


<%
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
String collectTime1 = parseLogFormatDate.format(new Date());
AppInfoPo appInfopo = AppCache.get().getKey("list");
%>
<script type="text/javascript">

	function openTrade(tradeType){
		window.open("<%=request.getContextPath () %>/time/key_time.jsp?action=trade&type="+tradeType+"&collectTime1=<%=collectTime1%>");
	}

	
	function goToDetailMonitor(){
		var appName = $("#appNameSelect").val();

		location.href="./time/app_time.jsp?appId="+appName;
	}
	function goToDateIndex(){
		location.href="./index.jsp";
	}
	
	function reinitIframe(down){
		var pTar = null;
		if (document.getElementById){
		pTar = document.getElementById(down);
		}
		else{
		eval('pTar = ' + down + ';');
		}
		if (pTar && !window.opera){
		//begin resizing iframe
		pTar.style.display="block"
		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
		//ns6 syntax
		pTar.height = pTar.contentDocument.body.offsetHeight +20;
		pTar.width = pTar.contentDocument.body.scrollWidth+20;
		}
		else if (pTar.Document && pTar.Document.body.scrollHeight){
		//ie5+ syntax
		pTar.height = pTar.Document.body.scrollHeight;
		pTar.width = pTar.Document.body.scrollWidth;
		}
		} 
	}
	
</script>

</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" ><a href="">交易数据</a></font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
 //|        0  c2c | 
  // |        1  b2c | 
  // |        2  wap | 
  // |        9  3c  | 
  // |      100 全网 | 
   
  List<String[]> listServiceType = new ArrayList<String[]>();
  listServiceType.add(new String[]{"100","全网"});
  listServiceType.add(new String[]{"9","3c"});
  listServiceType.add(new String[]{"2","wap"});
  listServiceType.add(new String[]{"1","b2c"});
  listServiceType.add(new String[]{"0","c2c"});
  
  Calendar cal = Calendar.getInstance();
  cal.add(Calendar.MINUTE,-30);
  Date start = cal.getTime();
  
  cal.add(Calendar.DAY_OF_MONTH,-7);
  Date pStart = cal.getTime();
  
  cal.add(Calendar.MINUTE,30);
  
  Date pEnd =  cal.getTime();
  
for(String[] type:listServiceType){
	Integer serviceType = Integer.parseInt(type[0]);
%>


<table width="100%" border="1">
  <tr>
    <td width="5%" height="18">&nbsp;</td>
    <td colspan="4" align="center"><%=type[1] %></td>
  </tr>
  <tr>
    <td align="center">时间</td>
    <td colspan="2" align="center">创建订单笔数</td>
    <td colspan="2" align="center">支付笔数</td>
  </tr>
   <%
  List<TradeVo[]> listVo100 = MonitorTradeAo.get().findTcCreateAndPay_new(start,new Date(),serviceType,10);
  List<TradeVo[]> listVo100p = MonitorTradeAo.get().findTcCreateAndPay_new(pStart,pEnd,serviceType,10);
  
  Map<String,TradeVo[]> map100p = new HashMap<String,TradeVo[]>();
  for(TradeVo[] t:listVo100p){
	  TradeVo create = t[0];
	  map100p.put(create.getCollectTime(),t);
  }
  for(TradeVo[] v:listVo100){
	  TradeVo create = v[0];
	  TradeVo pay =  v[1];
	  
	  TradeVo[] p = map100p.get(create.getCollectTime());
	  TradeVo createp = null;
	  TradeVo payp =  null;
	  if(p==null){
		  createp = new TradeVo();
		  payp =  new TradeVo();
	  }else{
		   createp = p[0];
		   payp =  p[1];
	  }
	
	  
  %>
  <tr>
    <td><%=create.getCollectTime() %></td>
    <td align="center"><%=create.getTraderCount() %><%=Utlitites.scale(create.getTraderCount(),createp.getTraderCount()) %></td>
    <td align="center"><%=create.getTraderAmount() %><%=Utlitites.scale(create.getTraderAmount(),createp.getTraderAmount()) %></td>
    <td align="center"><%=pay.getTraderCount() %><%=Utlitites.scale(pay.getTraderCount(),payp.getTraderCount()) %></td>
    <td align="center"><%=pay.getTraderAmount()%><%=Utlitites.scale(pay.getTraderAmount(),payp.getTraderAmount()) %></td>
  </tr>
  <%} %>
</table>

<%} %>
</div>
</div>
<jsp:include page="buttom.jsp"></jsp:include>
</body>
</html>