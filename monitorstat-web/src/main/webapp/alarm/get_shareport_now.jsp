<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.alarm.trade.realtime.RealTimeTradeAo"%>
<%@page import="com.taobao.monitor.alarm.trade.realtime.po.RealTimeTradePo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	try{
	    Date startTime = new Date();
		RealTimeTradePo po = RealTimeTradeAo.get().getReadlTimeTradeByTime(startTime);
		if(po != null){
%>
<tr>
	<td><%=po.getC2cCreateCnt()%></td>
	<td><%=(po.getC2cCreateSum()!=null)?po.getC2cCreateSum():"0.00"%></td>
	<td><%=po.getC2cPaidCnt()%></td>
	<td><%=(po.getC2cPaidSum()!=null)?po.getC2cPaidSum():"0.00"%></td>
	<td><%=po.getB2cCreateCnt()%></td>
	<td><%=(po.getB2cCreateSum()!=null)?po.getB2cCreateSum():"0.00"%></td>
	<td><%=po.getB2cPaidCnt()%></td>
	<td><%=(po.getB2cPaidSum()!=null)?po.getB2cPaidSum():"0.00"%></td>
	<td><%=po.getJhsCreateCnt()%></td>
	<td><%=(po.getJhsCreateSum()!=null)?po.getJhsCreateSum():"0.00"%></td>
	<td><%=po.getJhsPaidCnt()%></td>
	<td><%=(po.getJhsPaidSum()!=null)?po.getJhsPaidSum():"0.00"%></td>
	<td><%=sdf.format(po.getTime())%></td>
</tr>
<%
		}
	} catch( Exception e){
		out.print(e.getStackTrace());
	}
%>		