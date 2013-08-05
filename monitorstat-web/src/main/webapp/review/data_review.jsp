<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>

<%@page import="com.taobao.monitor.common.ao.center.DataBaseInfoAo"%>
<%@page import="com.taobao.monitor.common.po.DataBaseInfoPo"%>
<%@page import="com.taobao.monitor.other.review.*"%>
<%@page import="com.taobao.monitor.common.po.AppMysqlInfo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.db.impl.center.AppInfoDao"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>监控</title>
<%
	DataReview dr = DataReview.getInstance();
	List<ServerCurrentInfo> serverList = dr.getServerInfo();// 获得所有服务器信息对象
	List<DataBaseInfoPo> dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo(); // 获得所有数据库 
	//Map<Integer,AppMysqlInfo> map = new HashMap<Integer, AppMysqlInfo>();// 某个数据库中app的情况
	List<AppMysqlInfo> appMysqlList = new ArrayList<AppMysqlInfo>();// 数据库中所有应用的AppMysqlInfo
	int flag = 0; // 标记首行输出 ,0
	int id;// app的id
	//AppMysqlInfo appInfo;
	AppCache appCache = AppCache.get(); // 通过app的id获取name 
	List<AppHostPo> ahpList = DataReview.getInstance().getResult();// 返回的每个app验证结果对象
	int sumHostCount = 0;
	int sumMonitorHostCount = 0;
	int monitorAppCount = 0;
%>
</head>
<body>
<center>
	<h1>服务器空间查询</h1>
	<table border='1'>
		<tr>
			<td width='150'>IP 地址</td>
			<td width='200'>Server名</td>
			<td width='80'>磁盘空间</td>
			<td width='80'>占用空间</td>
            <td width='80'>日报监控app数量</td>
            <td width='80'>实时监控app数量</td>
            <td width='80'>mysql数量</td>
		</tr>
		<%
		if(serverList != null) {
			for (ServerCurrentInfo sci : serverList) {
		%><tr>
				<td><%=sci.getServerIp()%></td>
				<td><%=sci.getServerName()%></td>
                <td><%=sci.getAllSpace() %></td>
				<td><%=sci.getStorage()%></td>
                <td><%=sci.getDayAppCount() %></td>
                <td><%=sci.getTimeAppcount() %></td>
                <td><%=sci.getMysqlCount() %></td>
				</tr>
				<%
					}
		}
				%>
	</table>
	<h1>数据库使用情况</h1>
	<table width="750" border="1">
   <tr>
         <td colspan="5"><div align="center">日期：<%=dr.getYesterday() %></div></td>
       </tr>
       <tr>
         <td width="150">数据库名</td>
         <td width="150">应用名</td>
         <td width="150">key数量</td>
         <td width="150">实时监控数据总量</td>
         <td width="150">日报数据总量</td>
       </tr>
       <%
       	for(DataBaseInfoPo po:dbInfoList)
       	{
       		if(po.getDbName().indexOf("Branch_") == -1) {
       			continue;
       		}
       		appMysqlList = dr.getAppMysqlList(po.getDbId());
       		if(appMysqlList.size()==0)
       		{
       			%>       			
      				<tr>
      					<td><%=po.getDbName() %></td>
      					<td bgcolor="#66FFCC">0</td>
                        <td bgcolor="#66FFCC">0</td>
		                <td bgcolor="#66FFCC">0</td>
                        <td bgcolor="#66FFCC">0</td>
      				</tr>
       			<%
       		}
       		else
       		{
        		flag=1;        		
        		out.print("<tr>");
        		for(AppMysqlInfo am: appMysqlList){
        			String color="black";// 字体颜色
        			if(am.getDataNum() < 10000){
        				color="red";
        			}
        			if(flag == 1){
        				%>
         				<td rowspan="<%=appMysqlList.size() %>"><%=po.getDbName() %></td>
        				<%
        				flag=0;
        			}
        			
        			%>
    				<td><font color="<%=color %>"><%=appCache.getKey(am.getAppId())!=null?appCache.getKey(am.getAppId()).getAppName():"" %></font></td>
		        	<td><font color="<%=color %>"><%=am.getKeyNum() %></font></td>
		        	<td><font color="<%=color %>"><%=am.getDataNum() %></font></td>
		        	<td><font color="<%=color %>"><%=am.getDayDataNum() %></font></td>
		        		        	
        			<%
        			out.print("</tr>");
        		}
       		}	              		              		
       	}
       %>
    </table>
    
  <h1>应用运行主机监控</h1>
   <table border='1'>
	   	<tr>
	    	<td width="200">应用名称</td>
	    	<td width="140">主机总数/监控数</td>
	    	<td width="130">实时账户通过/未通过验证</td>
	    	<td width="140">日报账户通过/未通过验证<font size="2.0"><a href="FailHostList.txt">(查看失败主机列表)</a></font></td>
   	 </tr>
	   	
	   	<%
	   	for(int i=0;i<ahpList.size();i++)
		{
			AppHostPo ahp=ahpList.get(i);
			%>
				<tr><td><%=ahp.getAppName()%></td>
				<td><%=ahp.getHostAcount()+"/"+ahp.getMonitorAcount() %></td>
				<%
				sumHostCount += ahp.getHostAcount();
				sumMonitorHostCount += ahp.getMonitorAcount();
				if (ahp.getMonitorAcount() > 0) {
					monitorAppCount++;
				}
				%>
				<td><%=ahp.getOnline()+"/"+ahp.getOffline() %></td>
				<td><%=ahp.getSuccess()+"/"+ahp.getFail()%></td></tr>
			<%
		}
	   	%>          	         	
   </table>
	<h1>汇总信息统计</h1>
	<table border='1'>
		<tr>
			<td width="200">日报应用总数</td> 
			<td width="200">监控主机总数</td>
			<td width="200">实时应用总数</td>
			<td width="200">实时主机总数（临时表）</td>
		</tr>
		<tr>
			<td><%=ahpList.size() %></td>
			<td><%=sumHostCount %></td>
			<td><%=monitorAppCount %></td>
			<td><%=sumMonitorHostCount %></td>
		</tr>
	</table>          
</center>
</body>
</html>