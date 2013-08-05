<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Collections"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV=″refresh″ content=″60″>
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

</head>
<body>
<table width="1000" align="center" style="margin:0 auto;" >
	<div align="center">
	  <font color="#FF0000">交易数据</font>
	  <a href="./trade_more.jsp" target="_blank">详情(不合并时间)</a>
	  <a href="./trade_more2.jsp" target="_blank">详情(合并时间)</a>
	</div>
	<tr><td><table width="1000" >
		<td>
			<table width="500" border="1">
			  <tr>
			    <td width="5%" height="18">&nbsp;</td>
			    <td colspan="2" align="center">全网</td>
			  </tr>
			  <tr>
			    <td align="center">时间</td>
			    <td colspan="2" align="center">创建订单笔数</td>
			  </tr>
			   <%
			  
			  Calendar cal = Calendar.getInstance();
			   cal.add(Calendar.MINUTE,-1);
			   Date end = cal.getTime();
			   
			  cal.add(Calendar.MINUTE,-20);
			  Date start = cal.getTime();
			  
			  cal.add(Calendar.DAY_OF_MONTH,-7);
			  Date pStart = cal.getTime();
			  
			  cal.add(Calendar.MINUTE,20);
			  
			  Date pEnd =  cal.getTime();
			  
			  Map<String,TradeVo> mapCreate100 = MonitorTradeAo.get().findTcCreateSumNew(start,end,100);
			  Map<String,TradeVo> mapCreate100p = MonitorTradeAo.get().findTcCreateSumNew(pStart,pEnd,100);
			  
			  
			  Set<String> timeSet = new HashSet<String>();
			  timeSet.addAll(mapCreate100.keySet());
			  List<String> timeList = new ArrayList<String>();
			  timeList.addAll(timeSet);
			  Collections.sort(timeList);
			  
			  for(int i=timeList.size()-1;i>=0;i--){
				  String time = timeList.get(i);
				  
				  TradeVo create = mapCreate100.get(time);
				  
				  
				  TradeVo createp = mapCreate100p.get(time);
				  if(create==null){
					  create = new TradeVo();
				  }
				  if(createp==null){
					  createp = new TradeVo();
				  }
				  
			  %>
			  <tr>
			    <td><%=time %></td>
			    <td align="center"><%=create.getTraderCount() %><%=Utlitites.scale(create.getTraderCount(),createp.getTraderCount()) %></td>
			    <td align="center"><%=create.getTraderAmount() %><%=Utlitites.scale(create.getTraderAmount(),createp.getTraderAmount()) %></td>
			  </tr>
			  <%} %>
			</table>
		</td>
		<td>
			<table width="500" border="1" align="center" style="margin:0 auto;">
			  <tr>
			    <td width="5%" height="18">&nbsp;</td>
			    <td colspan="2" align="center">B2C</td>
			  </tr>
			  <tr>
			    <td align="center">时间</td>
			    <td colspan="2" align="center">创建订单笔数</td>
			  </tr>
			   <%
			   timeSet.clear();
			   timeList.clear();
			   Map<String,TradeVo> mapCreate1 = MonitorTradeAo.get().findTcCreateSumNew(start,end,1);
				  Map<String,TradeVo> mapCreate1p = MonitorTradeAo.get().findTcCreateSumNew(pStart,pEnd,1);
				  
				  
				  timeSet.addAll(mapCreate1.keySet());
				  timeList.addAll(timeSet);
				  Collections.sort(timeList);
				  
				  for(int i=timeList.size()-1;i>=0;i--){
					  String time = timeList.get(i);
					  
					  TradeVo create = mapCreate1.get(time);
					  
					  
					  TradeVo createp = mapCreate1p.get(time);
					  if(create==null){
						  create = new TradeVo();
					  }
					  if(createp==null){
						  createp = new TradeVo();
					  }
					  
				  %>
				  <tr>
				    <td><%=time %></td>
				    <td align="center"><%=create.getTraderCount() %><%=Utlitites.scale(create.getTraderCount(),createp.getTraderCount()) %></td>
				    <td align="center"><%=create.getTraderAmount() %><%=Utlitites.scale(create.getTraderAmount(),createp.getTraderAmount()) %></td>
				  </tr>
				  <%} %>
			</table>
		
		</td></table></td>
	</tr>
</table>
</body>

</html>