<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.taobao.monitor.web.poc.PocAO" %>
<%@page import="com.taobao.monitor.web.core.po.JprofClassMethod"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.web.vo.HostConfigPo"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.web.core.po.LoadRunHost"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>应用性能详情</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/tablesort.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/i18n/ui.datepicker-zh-CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath() %>/statics/js/jquery.1.3.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.tablesorter1024.js"></script> 
</head>
<body>
    <%
        String appId = request.getParameter("appId");
        String date = request.getParameter("date");
        if (appId == null) {
            appId = "1";
        }
        if (date == null) {
            Calendar cd = Calendar.getInstance();
            cd.add(Calendar.DAY_OF_YEAR, -1);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(cd.getTime());
        }
        HostConfigPo po = PocAO.instance.getLoadrunAppConfig(Integer.parseInt(appId));
        List<LoadRunHost> hostList = PocAO.instance.getAppList();
    %>
<table cellpadding="0" cellspacing="0" border="1" id="headerTable" style="width:100%;align:center">
    <tbody>
        <tr>
        <td align="center">应用:<select id="selectedapp">
	    <%
	    for(LoadRunHost host : hostList){
	    %>
	    <option value="<%=host.getAppId()%>" 
	    <%if (host.getAppId() == Integer.parseInt(appId)) { %>selected<% }%>
	    ><%=host.getAppName()%></option>
	    <%  
	    } 
	    %>
		</select>
        日期:<input type="text" class="Wdate" value="<%=date%>" id="collectTime" onfocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})">
<input type="submit" id="lookbutton" value="查看" onclick="clicklookbutton();"></td>
        </tr>
    </tbody>
</table>
<table cellpadding="0" cellspacing="0" border="1" width="100%">
<tbody>
    <tr>
        <td width="50%" valign="top">
                                机器配置：
            <table cellpadding="1" cellspacing="1" border="1" width="100%">
                <tbody>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">OS</td><td><%=po.getOsVersion()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">Processor</td><td><%=po.getPlatform()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">Uptime</td><td><%=po.getUptime()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">Domain</td><td><%=po.getDomainIp()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">CPU</td><td><%=po.getCpu()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">Disk</td><td><%=po.getDiskHome()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">Memory</td><td><%=po.getMemory()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">Java Version</td><td><%=po.getJavaVersion()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">Jboss</td><td><%=po.getJbossVersion()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">Apache</td><td><%=po.getApacheVersion()%></td>
                </tr>
                <tr>
                <td class="ui-widget-header" width="25%" align="right">JVM Parameters</td>
                <td><%=po.getJvmParameters()%>
                </td>
                </tr>
                </tbody>
            </table>
        </td>
        <td width="50%" valign="top">
	          压测表现：
	    <div id="relationdiv">
	    </div>
        </td>
    </tr>
    <tr>
        <td width="50%" valign="top">
	    垃圾回收：
	    <div id="gcdiv">
	    </div>
        </td>
        <td width="50%" valign="top">
	           内存使用：
	    <div id="pagediv">
	    </div>
        </td>
    </tr>
</tbody>
</table>
	<%
	    String date1 = PocAO.instance.dayAdd1(date);
		String result1 = PocAO.instance.getSystemResourceRelation(Integer.parseInt(appId), date, date1);
		String result2 = PocAO.instance.getMinorGcInfo(Integer.parseInt(appId), date, date1);
		String result3 = PocAO.instance.getPageMemoryInfo(Integer.parseInt(appId), date, date1);
	%>

<div id="hotspotdiv">
    <%
    List<JprofClassMethod> listClass = PocAO.instance.findJprofClassMethod(Integer.parseInt(appId), date);
    if (listClass != null && listClass.size() > 0) {
    %>
代码热点列表：
<table id="jprofTable" class="tablesorter">
    <thead>
    <tr>
        <td >类信息</td>   
        <td >平均时间(ms)</td>        
        <td >总次数</td>
        <td >总时间(ms)</td>
    </tr>
    </thead>
    <tbody>
    <%
    for(JprofClassMethod m : listClass){
    %>
        <tr >
            <td ><%=m.getClassName()+":"+m.getMethodName()+":"+m.getLineNum() %></td>
            <td ><%=Arith.div(m.getUseTime(),m.getExcuteNum(),2) %></td>
            <td ><%=m.getExcuteNum() %></td>
            <td ><%=m.getUseTime() %></td>
        </tr>
    <%  
    } 
    %>
    </tbody>
</table>
    <%  
    } 
    %>
</div>

<div id="externalhotspotdiv">
    <%
    List<JprofClassMethod> externalClass = PocAO.instance.findJprofTopExternalMethod(Integer.parseInt(appId), date);
    if (externalClass != null && externalClass.size() > 0) {
    %>
 外部调用排行：
<table class="tablesorter">
    <thead>
    <tr>
        <td >类信息</td>   
        <td >平均时间(ms)</td>        
        <td >总次数</td>
        <td >总时间(ms)</td>
    </tr>
    </thead>
    <tbody>
    <%
    for(JprofClassMethod m : externalClass){
    %>
        <tr >
            <td ><%=m.getClassName()+":"+m.getMethodName()+":"+m.getLineNum() %></td>
            <td ><%=Arith.div(m.getUseTime(),m.getExcuteNum(),2) %></td>
            <td ><%=m.getExcuteNum() %></td>
            <td ><%=m.getUseTime() %></td>
        </tr>
    <%  
    }
    %>
    </tbody>
    </table>
    <%  
    }
    %>
</div>

<div id="daohotspotdiv">
    <%
    List<JprofClassMethod> daoClass = PocAO.instance.findJprofTopDaoMethod(Integer.parseInt(appId), date);
    if (daoClass != null && daoClass.size() > 0) {
    %>
DAO方法排行：
<table class="tablesorter">
    <thead>
    <tr>
        <td >类信息</td>   
        <td >平均时间(ms)</td>        
        <td >总次数</td>
        <td >总时间(ms)</td>
    </tr>
    </thead>
    <tbody>
    <%
    for(JprofClassMethod m : daoClass){
    %>
        <tr >
            <td ><%=m.getClassName()+":"+m.getMethodName()+":"+m.getLineNum() %></td>
            <td ><%=Arith.div(m.getUseTime(),m.getExcuteNum(),2) %></td>
            <td ><%=m.getExcuteNum() %></td>
            <td ><%=m.getUseTime() %></td>
        </tr>
    <%  
    }
    %>
    </tbody>
    </table>
    <%  
    }
    %>
</div>
</body>

<script type="text/javascript">
	var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "600", "300", "8", "#FFFFFF");
	so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
	so.addVariable("chart_id", "amline");   
	so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
	so.addVariable("chart_data", encodeURIComponent("<%=result1%>"));
	so.write("relationdiv");			

	var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "600", "300", "8", "#FFFFFF");
	so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
	so1.addVariable("chart_id", "amline");   
	so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
	so1.addVariable("chart_data", encodeURIComponent("<%=result2%>"));
	so1.write("gcdiv");    

	var so2 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "600", "300", "8", "#FFFFFF");
    so2.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
    so2.addVariable("chart_id", "amline");   
    so2.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
    so2.addVariable("chart_data", encodeURIComponent("<%=result3%>"));
    so2.write("pagediv");

    $(document).ready(function() {
    	$("#jprofTable").tablesorter();
    });
function clicklookbutton(){
	$("#lookbutton")[0].disabled = true;
	document.location = '<%=request.getContextPath() %>/poc/appdetail.jsp?appId='+$("#selectedapp")[0].value+'&date='+$("#collectTime")[0].value;
}
</script>
</html>