<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.alarm.trade.realtime.RealTimeTradeAo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
	try{
		String from = request.getParameter("from");
		if(from == null){
	    	Date startTime = new Date();
			out.print(RealTimeTradeAo.get().getReadlTimeTradeByThird(startTime));
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = sdf.parse(from);
			out.print(RealTimeTradeAo.get().getReadlTimeTradeByThird2(startTime));
		}
	} catch( Exception e){
		out.print(e.getStackTrace());
	}
%>		