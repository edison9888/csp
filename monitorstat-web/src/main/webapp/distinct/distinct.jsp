<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.web.distinct.TimeDistinctManage"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.distinct.DistinctInterface"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%

String appIds = request.getParameter("appId");
String[] ids = appIds.split(",");
for(String appId : ids){
try{	
String appName = AppCache.get().getKey(Integer.parseInt(appId)).getAppName();

TimeDistinctManage manage = TimeDistinctManage.get(Integer.parseInt(appId));

double maxPv = manage.findMaxPv();
double samePv = manage.findSameMaxPv();
double yaceQps = manage.findAppLoadRunQps();
double qps = Arith.div(maxPv,60,2);
double rest = manage.findPeakRestPvValue();
double someRest = manage.findSameRestPvValue();


double m_pv = MonitorTimeAo.get().getKeyMaxHistoryValue(Integer.parseInt(appId),175);
if(m_pv == 0){
	MonitorTimeAo.get().addMaxHistoryValue(Integer.parseInt(appId),175,(float)maxPv);
	m_pv = maxPv;
}else{
	if(m_pv < maxPv){
		MonitorTimeAo.get().updateMaxHistoryValue(Integer.parseInt(appId),175,(float)maxPv);
		m_pv = maxPv;
	}
}


double m_rt = MonitorTimeAo.get().getKeyMaxHistoryValue(Integer.parseInt(appId),176);
if(m_rt == -1){
	MonitorTimeAo.get().addMaxHistoryValue(Integer.parseInt(appId),176,(float)rest);
	m_rt = rest;
}else{
	if(m_rt > rest){
		MonitorTimeAo.get().updateMaxHistoryValue(Integer.parseInt(appId),176,(float)rest);
		m_rt = rest;
	}
}


%>

<table>
	<tr class="ui-widget-header ">
		<td align="center"><font size="4" >Ӧ��:[<%=appName%>]</font></td>		
	</tr>
</table>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >ǰ�������Ա�</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content">
  <tr class="ui-widget-header ">
    <td colspan="6">��ʷ�������</td>
  </tr>
  <tr>
    <td>ÿ�������pv����</td>
    <td><%=m_pv %></td>
    <td>qps</td>
    <td><%=Arith.div(m_pv,60,2) %>����
     <%if(yaceQps == -1){
     %>
     	��֧��
     <%
    	}else{    		
     %>
     <a href="../load/app_loadrun_detail.jsp?appId=<%=appId %>"><%=Arith.sub(Arith.div(m_pv,60,2),qps)%></a>
     <%
     } 
     %></td>
    <td>��õ���Ӧʱ�䣺</td>
    <td><%=Arith.div(m_rt,1000,2) %></td>
  </tr>
  <tr>
    <td colspan="6" class="ui-widget-header ">��ǰ�������</td>
  </tr>
  <tr>
    <td>ÿ�������pv����</td>
    <td><%=maxPv%>(<%=Utlitites.scale(maxPv,samePv)%>)</td>
    <td>qps</td>
    <td><%=qps %> ����
     <%if(yaceQps == -1){
     %>
     	��֧��
     <%
    	}else{    		
     %>
     <a href="../load/app_loadrun_detail.jsp?appId=<%=appId %>"><%=Arith.sub(yaceQps,qps)%></a>
     <%
     } 
     %> </td>
    <td>��Ӧʱ�䣺</td>
    <td><%=Arith.div(rest,1000,2) %>(<%=Utlitites.scale(rest,someRest) %>) (����<%=Arith.div(someRest,1000,2) %>)</td>
  </tr> 
</table>
</div>
</div>


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >��ʾ��ص���������������½�����ƽ��ÿ���ӣ�</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="800" border="1"> 
	<tr>
    <td>����</td>
    <td>��ǰ������</td>
    <td>�ϴη�����</td>
    <td>��ǰ��Ӧʱ��</td>
    <td>�ϴ���Ӧʱ��</td>
  </tr> 
  <%
  Map<String,String> monitorKeyMap = new HashMap<String,String>();
  
  monitorKeyMap.put("OUT_SearchEngine","����");
  monitorKeyMap.put("OUT_TairClient","tair");
  monitorKeyMap.put("OUT_HSF-Consumer","OUT_HSF-Consumer");
  monitorKeyMap.put("IN_HSF-ProviderDetail","IN_HSF-ProviderDetail");
  monitorKeyMap.put("OUT_PageCache","OUT_PageCache");
  monitorKeyMap.put("OUT_forest_client","OUT_forest_client");
  
  for(Map.Entry<String,String> entry:monitorKeyMap.entrySet()){
	  
  List<DistinctInterface> listSearch = manage.findDistinctCommon(entry.getKey());
  %>
  <%
  	if(listSearch.size() > 0){
  		
  %>
  <tr>
    <td colspan="5" class="ui-widget-header "><%=entry.getValue() %></td>
  </tr>
  
  <%for(DistinctInterface dis:listSearch){ %>
  <tr>
    <td><%=dis.getKeyName() %></td>
    <td><%=dis.getCount()==-1?"�ڸ߷���û�г���":dis.getCount() %>(<%=Utlitites.scale(dis.getCount(),dis.getSameCount()) %>)</td>
    <td><%=dis.getSameCount()==-1?"�ڸ߷���û�г���":dis.getSameCount()  %></td>
    <td><%=dis.getResponseTime()==-1?"�ڸ߷���û�г���":dis.getResponseTime()  %>(<%=Utlitites.scale(dis.getResponseTime(),dis.getSameResponseTime()) %>)</td>
    <td><%=dis.getSameResponseTime()==-1?"�ڸ߷���û�г���":dis.getSameResponseTime()  %></td>
  </tr>
  <%} }}%>
</table>

<%

List<DistinctInterface> exceptionList = manage.findExceptionDifferent();
if(exceptionList.size() > 0){
%>

<table width="800" border="1">
  <tr>
    <td colspan="3" class="ui-widget-header ">�쳣��Ϣ�����ı仯</td>
  </tr> 
  <tr>
    <td>����</td>
    <td>��ǰƽ���쳣����</td>
    <td>�ϴ�ƽ���쳣����</td>
  </tr>
  <%for(DistinctInterface dis : exceptionList){ %>
  <tr>
    <td><%=dis.getKeyName() %></td>
    <td><%=dis.getCount() %>(<%=Utlitites.scale(dis.getCount(),dis.getSameCount()) %>)</td>
    <td><%=dis.getSameCount() %></td>
  </tr>
  <%}%>
</table>
<%} %>
<%
  
  List<DistinctInterface> listSearch = manage.findDistinctOther();
if(listSearch.size() > 0){
  %>
  

<table width="800" border="1">
  <tr>
    <td colspan="5" class="ui-widget-header ">�����ӿ���Ϣ</td>
  </tr>
  <tr>
    <td>����</td>
    <td>������</td>
    <td>�ϴη�����</td>
    <td>��Ӧʱ��</td>
    <td>�ϴ���Ӧʱ��</td>
  </tr>
  
  <%
  	if(listSearch.size() > 0){
  		
  %>  
  <%for(DistinctInterface dis:listSearch){ %>
  <tr>
     <td><%=dis.getKeyName() %></td>
    <td><%=dis.getCount()==-1?"�ڸ߷���û�г���":dis.getCount() %>(<%=Utlitites.scale(dis.getCount(),dis.getSameCount()) %>)</td>
    <td><%=dis.getSameCount()==-1?"�ڸ߷���û�г���":dis.getSameCount()  %></td>
    <td><%=dis.getResponseTime()==-1?"�ڸ߷���û�г���":dis.getResponseTime()  %>(<%=Utlitites.scale(dis.getResponseTime(),dis.getSameResponseTime()) %>)</td>
    <td><%=dis.getSameResponseTime()==-1?"�ڸ߷���û�г���":dis.getSameResponseTime()  %></td>
  </tr>
  <%} }%>
</table>
<%} %>
</div>
</div>
<%! public Map<String,String> g(List<KeyPo> newKeyList){
	Map<String,String> mapKey = new HashMap<String,String>();
	for(KeyPo po:newKeyList){
		if(po.getKeyName().indexOf("SELF_Thread") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[2];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"�߳�");
			}
		}
		if(po.getKeyName().indexOf("OUT_forest_client") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[1]+"_"+keys[2]+"_"+keys[3];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"forest�ͻ��˷���");
			}
		}
		if(po.getKeyName().indexOf("SELF_DataSource") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[2];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"����Դ");
			}
		}
		if(po.getKeyName().indexOf("SELF_ThreadPool") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[2];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"�̳߳�");
			}
		}
		if(po.getKeyName().indexOf("OUT_SearchEngine") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[2]+"_"+keys[3];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"��������");
			}
		}
		if(po.getKeyName().indexOf("OUT_TairClient") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[1]+"_"+keys[2];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"TairClient");
			}
		}
		if(po.getKeyName().indexOf("OUT_HSF-Consumer") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[2]+"_"+keys[3];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"HSF-Consumer");
			}
		}
		if(po.getKeyName().indexOf("IN_HSF-ProviderDetail") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[2]+"_"+keys[3];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"HSF-ProviderDetail");
			}
		}
		if(po.getKeyName().indexOf("OUT_PageCache") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[1]+"_"+keys[2];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"PageCache");
			}
		}
		if(po.getKeyName().indexOf("OUT_PageCache") > -1){
			String[] keys = po.getKeyName().split("_");
			String k = keys[1]+"_"+keys[2];
			String m = mapKey.get(k);
			if(m == null){
				mapKey.put(k,"PageCache");
			}
		}
	}
	return mapKey;
} %>

<%
List<KeyPo> lostKeyList = manage.getLostKeyPoList();
List<KeyPo> newKeyList = manage.getNewKeyPoList();

Map<String,String> mapnewKey = g(newKeyList);
Map<String,String> maplostKey = g(lostKeyList);


if(lostKeyList.size() >0 && newKeyList.size() > 0){
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >�Ա�������һ��ļ�ص�ı仯</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<%if(maplostKey.size() >0){ %>
<table width="800" border="1">
  <tr>
    <td colspan="2" class="ui-widget-header ">����û�г���</td>     
  </tr>
  <tr>
    <td>����</td> 
     <td>˵��</td>       
  </tr> 
  <%for(Map.Entry<String,String> entry:maplostKey.entrySet()){ %>
  <tr>
    <td><%=entry.getKey() %></td>  
    <td><%=entry.getValue() %></td>  
  </tr>
  <%} %>
</table>
<%} %>
<%if(mapnewKey.size() >0){ %>
<table width="800" border="1">
  <tr>
    <td  colspan="2" class="ui-widget-header ">�����³���</td>
  </tr>
  <tr>
    <td>����</td>  
    <td>˵��</td>       
  </tr> 
   <%for(Map.Entry<String,String> entry:mapnewKey.entrySet()){ %>
  <tr>
    <td><%=entry.getKey() %></td>  
    <td><%=entry.getValue() %></td>  
  </tr>
  <%} %>
</table>
<%} %>
</div>
</div>
<%} }catch(Exception e){
	e.printStackTrace();
}}%>

