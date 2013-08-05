<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import="com.taobao.monitor.web.schedule.CollectReportDataJob" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    
	   CollectReportDataJob job = new CollectReportDataJob();
	   
	   String date = request.getParameter("date");
	   
	   out.print(date);
		
		if (date != null && date.length() > 0) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				job.setDate(sf.parse(date));
			} catch (ParseException e) {
				
			}
		}
		job.execute(null);

%>