<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.alarm.trade.realtime.RealTimeTradeAo"%>
<%@page import="com.taobao.monitor.alarm.trade.realtime.po.RealTimeTradePo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	try{
	    String startTimeString = (String)request.getParameter("startTime");
	    Date startTime;
	    if( StringUtils.isBlank(startTimeString) ){
	    	startTime = new Date();
	    } else {
	    	startTime = sdf.parse(startTimeString);
	    }
		List<RealTimeTradePo> poList = RealTimeTradeAo.get().getMultiRealTimeTradeNow();
%>
<h4><span class="sharereport">�������ϵͳ����ʵʱ���ϵͳ</span></h4>
<table width="100%">
	<thead>
	<tr>
		<th><img src="pics/taobao.png" width="64" height="64" /><br>���н���<br>����</th>
		<th><img src="pics/money.png" width="64" height="64" /><br>���</th>
		<th><img src="pics/taobao.png" width="64" height="64" /><br>���н���<br>����</th>
		<th><img src="pics/money.png" width="64" height="64" /><br>���</th>
		<th><img src="pics/tmall.png" width="64" height="64" /><br>��è����<br>����</th>
		<th><img src="pics/money.png" width="64" height="64" /><br>���</th>
		<th><img src="pics/tmall.png" width="64" height="64" /><br>��è����<br>����</th>
		<th><img src="pics/money.png" width="64" height="64" /><br>���</th>
		<th><img src="pics/jhs.png" width="64" height="64" /><br>�ۻ��㽻��<br>����</th>
		<th><img src="pics/money.png" width="64" height="64" /><br>���</th>
		<th><img src="pics/jhs.png" width="64" height="64" /><br>�ۻ��㽻��<br>����</th>
		<th><img src="pics/money.png" width="64" height="64" /><br>���</th>
		<th width="154"><img src="pics/clock.png" width="64" height="64" /><br>�ɼ�ʱ��</th>
	</tr>
	</thead>
	<tbody>
<%
		for(RealTimeTradePo po:poList){
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
	</tbody>
</table>