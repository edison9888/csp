<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.Set"%>
<%@page import="com.taobao.csp.depend.po.AppDepApp"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.taobao.csp.depend.po.hsf.AppProviderSummary"%>
<%@page import="com.taobao.csp.depend.po.url.UrlOriginSummary"%>
<%@page import="com.taobao.csp.depend.po.url.RequestUrlSummary"%>
<%@page import=" com.taobao.csp.depend.po.tair.TairConsumeSummaryPo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
<title>依赖模块</title>
<%
  Set<String> meDependSet = (Set<String>) request
      .getAttribute("meDependSet");
  Set<String> dependMeSet = (Set<String>) request
      .getAttribute("dependMeSet");
  List<AppProviderSummary> appProviderList = (List<AppProviderSummary>) request
      .getAttribute("appProviderList");
  List<AppProviderSummary> appConsumerList = (List<AppProviderSummary>) request
      .getAttribute("appConsumerList");
  
  List<UrlOriginSummary> originList = (List<UrlOriginSummary>) request
      .getAttribute("originList");
  List<RequestUrlSummary> requestList = (List<RequestUrlSummary>) request
      .getAttribute("requestList");  
  List<TairConsumeSummaryPo> tairConsumerList = (List<TairConsumeSummaryPo>) request
      .getAttribute("tairConsumerList");  
  
  String hsfProviderTotal = (String) request
      .getAttribute("hsfProviderTotal");
  String hsfConsumerTotal = (String) request
      .getAttribute("hsfConsumerTotal");
  String urlOriginTotal = (String) request
      .getAttribute("urlOriginTotal");
  String urlRequestTotal = (String) request
      .getAttribute("urlRequestTotal");
  String tairConsumerTotal = (String) request
      .getAttribute("tairConsumerTotal");
  
  //格式化比例使用
  java.text.DecimalFormat df = new java.text.DecimalFormat("0.0000");
%>
</head>
<body>
<%@ include file="../header.jsp"%>
			<form action="<%=request.getContextPath()%>/main/appmain.do" method="get">
		<input type="hidden" value="showAppIndexMain" name="method">
		<div style="text-align: center">
				<div id="page_nav"></div>
			</div>
		<script>
					$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${opsName}', selectDate:'${selectDate}'});
				</script>
			</form>		
		<div class="container">
			<div class="row">
				<span style="font-weight:bold">${opsName}--应用首页</span>
				<!-- 端口信息不做历史比较 -->
				<div id="portDiv">
				<table class="table table-striped table-bordered table-condensed">
						<tr>
							<td><strong>我依赖的服务的端口 &nbsp;&nbsp;
							<a href='<%=request.getContextPath()%>/show/dependmap.do?method=index&opsName=${opsName}&selectDate=${selectDate}'>查看依赖地图</a></strong></td>
						</tr>		
						<%
						  if (meDependSet == null || meDependSet.size() == 0) {
						    out.println("<tr><td>没有依赖其他应用</td><tr>");
						  } else {
						    out.println("<tr><td>");
						    for (String port : meDependSet) {
						      out.print(port + "&nbsp;&nbsp;");
						    }
						    out.println("</td><tr>");
						  }
					%>
						<tr>
							<td><strong>我提供的服务的端口： &nbsp;&nbsp;</strong></td>
						</tr>		
						<%
						  if (dependMeSet == null || dependMeSet.size() == 0) {
						    out.println("<tr><td>没有依赖我的应用</td><tr>");
						  } else {
						    out.println("<tr><td>");
						    for (String port : dependMeSet) {
						      out.print(port + "&nbsp;&nbsp;");
						    }
						    out.println("</td><tr>");
						  }
					%>					
				</table>
				</div>
				<div id="hsfDataDiv">
					<table class="table table-striped table-bordered table-condensed">
						<tr>
							<td colspan="2"><strong>提供HSF总量：</strong><%=Utlitites.fromatLong(hsfProviderTotal + "")%>&nbsp;<a
								href='<%=request.getContextPath()%>/show/hsfprovide.do?method=showAppCenterHsfInfo&opsName=${opsName}&showType=provide&selectDate=${selectDate}'>我提供HSF</a></td>
						</tr>					
						<tr>
							<td>应用名称</td>
							<td>百分比</td>
						</tr>
						<%
						  int i = 0;
						  for (AppProviderSummary po : appProviderList) {
						    if(i == 10)
						       break;
						    i++;
						    double callNum = po.getCallAllNum();
						    //double rateTmp = callNum / Double.parseDouble(hsfProviderTotal);
						    //String rateStr = df.format(rateTmp);						    
						%>
						<tr>
							<td><%=po.getOpsName()%></td>
							<td><%=Utlitites.fromatLong(po.getCallAllNum()+"")%></td>
							<td>
							<%=Arith.mul(Arith.div(callNum, Double.parseDouble(hsfProviderTotal),4), 100)%>%
							</td>
						</tr>
						<%
						  }
						%>
					</table>
					<table class="table table-striped table-bordered table-condensed">
						<tr>
							<td colspan="2"><strong>消费HSF总量：</strong><%=Utlitites.fromatLong(hsfConsumerTotal + "")%>&nbsp;<a
								href='<%=request.getContextPath()%>/show/hsfconsume.do?method=showAppCenterConsumeHsfInfo&opsName=${opsName}&showType=consume&selectDate=${selectDate}'>我消费的HSF</a></td>
						</tr>
						<tr>
							<td>应用名称(Top10)</td>
							<td>调用量</td>
							<td>百分比</td>
						</tr>
						<%
						  for (AppProviderSummary po : appConsumerList) {
						    double callNum = po.getCallAllNum();
						    //double rateTmp = callNum / Double.parseDouble(hsfConsumerTotal);
						    //String rateStr = df.format(rateTmp);
						%>
						<tr>
							<td><%=po.getOpsName()%></td>
							<td><%=Utlitites.fromatLong(po.getCallAllNum()+"")%></td>
							<td>
							<%=Arith.mul(Arith.div(callNum, Double.parseDouble(hsfConsumerTotal),4), 100)%>%
							</td>
						</tr>
						<%
						  }
						%>
					</table>
				</div>
				<div id="urlData">
					<div id="urlOriginDiv">
					<table class="table table-striped table-bordered table-condensed">
						<tr>
							<td colspan="2"><strong>URL来源总量:</strong><%=Utlitites.fromatLong(urlOriginTotal + "")%>&nbsp;&nbsp;<a
								href='<%=request.getContextPath()%>/show/urlorigin.do?method=showUrlOriginMain&opsName=${opsName}&showType=origin&selectDate=${selectDate}'>查看URL来源全部>></a></td>
						</tr>
						<tr>
							<td>URL名称(Top10)</td>
							<td>调用量</td>
							<td>占总来源总量百分比</td>
						</tr>
						<%
						  for (UrlOriginSummary po : originList) {
						    if(i == 10)
						       break;
						    i++;
						    double callNum = po.getOriginUrlNum();
						    double rateTmp = callNum / Double.parseDouble(urlOriginTotal);
						%>
						<tr>
							<td><%=po.getOriginUrl()%></td>
							<td><%=Utlitites.fromatLong(po.getOriginUrlNum() + "") %></td>
							<td>
							<%=Arith.mul(Arith.div(callNum, Double.parseDouble(urlOriginTotal),4), 100)%>%
							</td>
						</tr>
						<%
						  }
						%>
					</table>					
					</div>
					<div id="urlRequestDiv">
					<table class="table table-striped table-bordered table-condensed">
						<tr>
							<td colspan="2"><strong>URL去向总量:</strong><%=Utlitites.fromatLong(urlRequestTotal + "")%>&nbsp;&nbsp;<a
								href='<%=request.getContextPath()%>/show/urlorigin.do?method=showUrlOriginMain&opsName=${opsName}&showType=origin&selectDate=${selectDate}'>查看URL去向全部>></a></td>
						</tr>
						<tr>
							<td>URL名称(Top10)</td>
							<td>调用量</td>
							<td>调用比例<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
						</tr>
						<%
						  i = 0;
						  for (RequestUrlSummary po : requestList) {
						    if(i == 10)
						       break;
						    i++;
						    double callNum = po.getRequestNum();
						    double rateTmp = callNum / Double.parseDouble(urlRequestTotal);
						    String rateStr = df.format(rateTmp);
						%>
						<tr>
							<td><%=po.getRequestUrl()%></td>
							<td><%=Utlitites.fromatLong(po.getRequestNum() + "") %></td>
							<td>
						<%=Arith.mul(Arith.div(callNum, Double.parseDouble(urlRequestTotal),4), 100)%>%
						&nbsp;&nbsp;<a href='<%=request.getContextPath()%>/rate/callrate.do?method=gotoTopoPage&sourceKey=<%=po.getRequestUrl()%>' target="_blank">查看调用路径</a></td>
						</tr>
						<%
						  }
						%>
					</table>					
					</div>
				</div>
				<div id="tairData">
					<div id="tairConsumerDiv">
					<table class="table table-striped table-bordered table-condensed">
						<tr>
							<td colspan="2"><strong>消费Tair总量:</strong><%=Utlitites.fromatLong(tairConsumerTotal + "")%>&nbsp;&nbsp;<a
								href='<%=request.getContextPath()%>/show/tairconsume.do?method=showTairConsumeMain&opsName=${opsName}&showType=consume&selectDate=${selectDate}'>查看Tair相关信息>></a></td>
						</tr>
						<tr>
							<td>TairGourpName(Top10)</td>
							<td>调用量</td>
							<td>百分比</td>
						</tr>
						<%
						  i = 0;
						  for (TairConsumeSummaryPo po : tairConsumerList) {
						    if(i == 10)
						       break;
						    i++;
						    //double callNum = po.getInvoking_all_num();
						    //double rateTmp = callNum / Double.parseDouble(tairConsumerTotal);
						    //String rateStr = df.format(rateTmp);
						%>
						<tr>
							<td><%=po.getTair_group_name()%></td>
							<td><%=Utlitites.fromatLong(po.getInvoking_all_num() + "") %></td>
							<td>
							<%=Arith.mul(Arith.div(po.getInvoking_all_num(), Double.parseDouble(tairConsumerTotal),4), 100)%>%</td>
						</tr>
						<%
						  }
						%>
					</table>						
					</div>
				</div>
				<div id="notifyData">
					<div id="notifyConsumerDiv">
					<!-- to do -->
					</div>
				</div>
			</div><!-- row -->
		</div><!-- container -->
</body>
</html>