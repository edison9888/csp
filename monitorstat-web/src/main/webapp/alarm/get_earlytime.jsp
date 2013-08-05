<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="java.text.SimpleDateFormat"%>
<div class="note_msg">监控系统保存了从<span class="edate"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(MonitorAlarmAo.get().getRecordEarlistDate()) %></span>到现在的告警数据</div>