<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%><%@page import="com.taobao.monitor.alarm.trade.realtime.RealTimeTradeAo"%><%@page import="java.util.Date"%>
<%
	try{
	    Date startTime = new Date();
		out.print(RealTimeTradeAo.get().getReadlTimeTradeBySecond(startTime));
	} catch( Exception e){
		out.print(e.getStackTrace());
	}
%>		