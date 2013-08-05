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
<title>���</title>
<%
	DataReview dr = DataReview.getInstance();
	List<ServerCurrentInfo> serverList = dr.getServerInfo();// ������з�������Ϣ����
	List<DataBaseInfoPo> dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo(); // ����������ݿ� 
	//Map<Integer,AppMysqlInfo> map = new HashMap<Integer, AppMysqlInfo>();// ĳ�����ݿ���app�����
	List<AppMysqlInfo> appMysqlList = new ArrayList<AppMysqlInfo>();// ���ݿ�������Ӧ�õ�AppMysqlInfo
	int flag = 0; // ���������� ,0
	int id;// app��id
	//AppMysqlInfo appInfo;
	AppCache appCache = AppCache.get(); // ͨ��app��id��ȡname 
	List<AppHostPo> ahpList = DataReview.getInstance().getResult();// ���ص�ÿ��app��֤�������
	int sumHostCount = 0;
	int sumMonitorHostCount = 0;
	int monitorAppCount = 0;
%>
</head>
<body>
<center>
	<h1>�������ռ��ѯ</h1>
	<table border='1'>
		<tr>
			<td width='150'>IP ��ַ</td>
			<td width='200'>Server��</td>
			<td width='80'>���̿ռ�</td>
			<td width='80'>ռ�ÿռ�</td>
            <td width='80'>�ձ����app����</td>
            <td width='80'>ʵʱ���app����</td>
            <td width='80'>mysql����</td>
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
	<h1>���ݿ�ʹ�����</h1>
	<table width="750" border="1">
   <tr>
         <td colspan="5"><div align="center">���ڣ�<%=dr.getYesterday() %></div></td>
       </tr>
       <tr>
         <td width="150">���ݿ���</td>
         <td width="150">Ӧ����</td>
         <td width="150">key����</td>
         <td width="150">ʵʱ�����������</td>
         <td width="150">�ձ���������</td>
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
        			String color="black";// ������ɫ
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
    
  <h1>Ӧ�������������</h1>
   <table border='1'>
	   	<tr>
	    	<td width="200">Ӧ������</td>
	    	<td width="140">��������/�����</td>
	    	<td width="130">ʵʱ�˻�ͨ��/δͨ����֤</td>
	    	<td width="140">�ձ��˻�ͨ��/δͨ����֤<font size="2.0"><a href="FailHostList.txt">(�鿴ʧ�������б�)</a></font></td>
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
	<h1>������Ϣͳ��</h1>
	<table border='1'>
		<tr>
			<td width="200">�ձ�Ӧ������</td> 
			<td width="200">�����������</td>
			<td width="200">ʵʱӦ������</td>
			<td width="200">ʵʱ������������ʱ��</td>
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