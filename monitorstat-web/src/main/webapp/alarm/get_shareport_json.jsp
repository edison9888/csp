<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.alarm.trade.realtime.RealTimeTradeAo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Date"%>
<%
String intiStr = (String)request.getParameter("init");
if( StringUtils.isNotBlank(intiStr) ){
		out.print( RealTimeTradeAo.get().getMultiRealTimeTradeJson(5, new Date()) );
} else {
	out.print( RealTimeTradeAo.get().getReadlTimeTradeJson() );
}
%>
