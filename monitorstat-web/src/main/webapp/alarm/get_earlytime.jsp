<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="java.text.SimpleDateFormat"%>
<div class="note_msg">���ϵͳ�����˴�<span class="edate"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(MonitorAlarmAo.get().getRecordEarlistDate()) %></span>�����ڵĸ澯����</div>