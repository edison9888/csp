<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.taobao.monitor.common.util.TBProductCache"%>
<%@page import="com.taobao.monitor.common.po.ProductLine"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@page import="java.util.ArrayList"%>

<%@page import="java.util.Collections"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.weekreport.WeekReportDataProvider"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="com.taobao.monitor.common.po.ReportBasicDataPo"%>
<%@page import="com.taobao.monitor.common.po.ReportInvokeDataPo"%>
<%@page import="com.taobao.monitor.common.util.CommonUtil"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>

<html xmlns="http://www.w3.org/1999/xhtml">



<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>����ϵͳ����</title>
</head>

<body>
<%
	Date dateTime = null;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String current = request.getParameter("dateTime");
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	if(current!=null){
		dateTime = sdf.parse(current);
		cal.setTime(dateTime);
	}else{		
		dateTime = cal.getTime();
	}
	//cal.add(Calendar.DAY_OF_MONTH,-1);
	
	dateTime = cal.getTime();
	
	
	
	List<Date> dayList = new ArrayList<Date>();
	
	int week = cal.get(Calendar.DAY_OF_WEEK);
	int offest = (week-1)==0?7:(week-1);		
	for(int i=0;i<offest;i++){
		dayList.add(cal.getTime());	
		cal.add(Calendar.DAY_OF_MONTH, -1);
	}	
	
	Collections.sort(dayList);
	Date fristDate = dayList.get(0);
%>

<%
request.setCharacterEncoding("gbk");

String companyName = request.getParameter("companyName");
String groupName = request.getParameter("groupName");


List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();

// Ĭ�����óɾۻ���
if(companyName ==null || companyName.length() == 0 || groupName == null || groupName.length() ==0){
	companyName = "�ۻ���";
	groupName = "�ۻ���";
}

List<AppInfoPo> needReportApp = new ArrayList<AppInfoPo>();

// ������Ʒ��
for(AppInfoPo po:appList){
	ProductLine p = TBProductCache.getProductLineByAppName(po.getOpsName());
	
	if(companyName.equals(p.getDevelopGroup()) && groupName.equals(p.getProductline())){
		needReportApp.add(po);
	}
}


%>
<center>
<jsp:include page="top.jsp"></jsp:include>

<form action="week_report_new.jsp">	
	<select id="companyName" name="companyName" onChange="companyChange(this)">	 </select>
	<select id="groupName" name="groupName">	</select>

	<input type="text" name="dateTime" value="<%=sdf.format(dateTime)%>"/> <input type="submit" value="�鿴���ʱ��һ�����"/>
</form>

  <%
  
  	WeekReportDataProvider dataProvider = new WeekReportDataProvider(fristDate, dateTime, needReportApp);
  
  %>
  
<table width="1000" align="center" style="margin:0 auto;" >
  <p></p>
  <tr>
    <td colspan="15" align="center"  class="tt2">��ǰӦ��ˮλ���[<%=sdf.format(dateTime)%>]</td>
  </tr> 
    <tr><td><table class="datalist" width="1000">
  <tr class="headcon ">
    <td>Ӧ��</td>
    <td>������</td>
    <td  align="center">��ǰPV</td>
    <td  align="center">��ǰˮλ</td>
    <td  align="center">��һ��ˮλ</td>
    <td  align="center">���ε��óɱ�</td>
  </tr>
  
  <%
   	
     for(AppInfoPo appPo:needReportApp){
   %>
  
  <tr>
  	<%

    	String appName = appPo.getOpsName();
    	ReportBasicDataPo po = dataProvider.latestBasicDataPo(appName);
    	
	%>   
    <td align="center"><a target="_blank" href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityMore"><%=appPo.getAppName()%></a></td>
    
    <td align="center">
    	<%=po.getMachineInfo()%>
    </td>
    <td align="center">
    	<%=Utlitites.fromatLong(po.getPv() + "")%>
    </td>
    <td align="center">
    	<%=po.getCapacityLevel()%>
    </td>
    <td align="center">
    	<%=CommonUtil.getCapacityStandard(appName)%>%
    </td>
    <td align="center">
    	<%=po.getSingleCost()%>
    </td>
  
  <%
  	}
  %>
  </table></td></tr>
  
</table>


<table width="1000" align="center" style="margin:0 auto;" >
  <p></p>
  <tr>
    <td colspan="15" align="center"  class="tt2">����Ӧ�����[<%=sdf.format(fristDate)+"-"+sdf.format(dateTime)%>]</td>
  </tr> 
    <tr><td><table class="datalist" width="1000">
  <tr class="headcon ">
    <td>Ӧ��</td>
    <td colspan="2" align="center">PV</td>
    <td colspan="2" align="center">UV</td>
    <td colspan="2" align="center">QPS</td>
    <td colspan="2" align="center">RT</td>
    <td colspan="2" align="center">LOAD</td>
    <td colspan="2" align="center">FullGC</td>
    <td colspan="2" align="center">СGC����</td>

  </tr>
  <tr class="ui-widget-header ">
    <td>&nbsp;</td>
    <td align="center">���ֵ</td>    
    <td align="center">����</td>
    <td align="center">���ֵ</td>   
    <td align="center">����</td>
    <td align="center">���ֵ</td>   
    <td align="center">����</td>
    <td align="center">���ֵ</td>    
    <td align="center">����</td>
    <td align="center">���ֵ</td>   
    <td align="center">����</td>
     <td align="center">����</td>   
    <td align="center">ʱ��</td>
    <td align="center">���ֵ</td>   
    <td align="center">����</td>
  </tr> 
 
  <%
   	
     for(AppInfoPo appPo:needReportApp){
   %>
  
  <tr>
    <td align="center"><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/app_detail.jsp?selectAppId=<%=appPo.getAppDayId() %>"><%=appPo.getAppName()%></a></td>
    <%
    	int appDayId = appPo.getAppDayId();
    	String appName = appPo.getOpsName();
	%>    
    
    <td align="center">
    	<%=Utlitites.fromatLong(dataProvider.maxKeyValue(appName, "pv"))%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "pv")%>    
    </td>
    
    <td align="center">
    	<%=Utlitites.fromatLong(dataProvider.maxKeyValue(appName, "uv"))%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "uv")%>    
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "qps")%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "qps")%>    
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "rt")%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "rt")%>    
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "load")%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "load")%>    
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "fGcCount")%>
    </td>
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "fGcTime")%>
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "yGcCount")%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "yGcCount")%>    
    </td>
  
  <%
  	}
  %>
  </table></td></tr>
  
</table>
<table width="1000" border="1" align="center" style="margin:0 auto;"  >
	<p></p>
	<tr>
		<td>���ֵ��</td>
		<td>
			ǰ��Ӧ��pv��ʾÿ�����ֵ�ڱ������������ֵ��CӦ�ñ�ʾ ���нӿ�һ������ܼ���һ������������ֵ��<br/>
			ǰ��Ӧ��QPS ��RT ���㷽ʽΪ �Ƚ�һ���20:30 ��22:30 ƽ����Ȼ��ȡ�������������죬<br/>
		<br/>
		</td>
	</tr>	
	<tr>
		<td>���ƣ�</td>
		<td>���������ܵ�һ���Ա������ȡ���ܵ�ƽ��ֵ�뱾��ƽ��ֵ���жԱ� </td>
	</tr>
	<tr>
		<td>IN��</td>
		<td>��ʾ�ⲿϵͳ���ñ�ϵͳ�����ģ�һ����CӦ�� </td>
	</tr>
	<tr>
		<td>OUT��</td>
		<td>��ʾ��ϵͳ�����ⲿӦ��һ����ǰ��Ӧ�� </td>
	</tr>
</table>


<table width="1000" align="center" style="margin:0 auto;" >
	 <%
	 for(AppInfoPo appPo:needReportApp){
	   	String appName = appPo.getOpsName();
	 %>
  <p></p>
  <tr class="tt2">
    <td align="center"><%=appPo.getAppName()%>�������</td>
  </tr>   
  <tr><td><table class="datalist" width="1000">
   
    	  <%
     	  		List<ReportInvokeDataPo> urls = dataProvider.invokeInfosThisWeek(appName, "URL");
     	  		List<ReportInvokeDataPo> hsfIns = dataProvider.invokeInfosThisWeek(appName, "HSF_IN");
     	  		List<ReportInvokeDataPo> hsfOuts = dataProvider.invokeInfosThisWeek(appName, "HSF_OUT");
     	  		
     	  		List<ReportInvokeDataPo> lastUrls = dataProvider.invokeInfosLastWeek(appName, "URL");
     	  		List<ReportInvokeDataPo> lastHsfIns = dataProvider.invokeInfosLastWeek(appName, "HSF_IN");
     	  		List<ReportInvokeDataPo> lastHsfOuts = dataProvider.invokeInfosLastWeek(appName, "HSF_OUT");
     	  %>  
     	  
     	  <% 
     	  		if (urls.size() > 0 || lastUrls.size() > 0) {
     	  %>
	      <tr class="headcon ">
	        <td align="center" colspan="3"> url����ǰ��λ</td>
	      </tr>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > ���ܷ��ʵ�url </td>
	      	<td align="center" >����</td>
	      	<td align="center" >ʱ��(ms)</td>
	      </tr>
	      
	      <% 
	      		for (int i = 0; i < urls.size(); i++) {
	      			ReportInvokeDataPo po = urls.get(i);
	      %>
	      			 <tr>
	      				<td align="left"><%=po.getResouceName() %></td>
	      				<td align="left"><%=po.getCount() %></td>
	      				<td align="left"><%=po.getTime() %></td>   
	      			 </tr>	  
	      <%
	      		}
	      %>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > ���ܷ��ʵ�url </td>
	      	<td align="center" >����</td>
	      	<td align="center" >ʱ��(ms)</td>
	      </tr>
	      
	      <% 
	      		for (int i = 0; i < lastUrls.size(); i++) {
	      			ReportInvokeDataPo po = lastUrls.get(i);
	      %>
	      			 <tr>
	      				<td align="left"><%=po.getResouceName() %></td>
	      				<td align="left"><%=po.getCount() %></td>
	      				<td align="left"><%=po.getTime() %></td>   
	      			 </tr>	  
	      <%
	      		}
	      	}
	      %>
	      
	      
	      <% 
     	  		if (hsfIns.size() > 0 || lastHsfIns.size() > 0) {
     	  %>
	      <tr class="headcon ">
	        <td align="center" colspan="3"> HSF�ӿ��ṩǰ��λ</td>
	      </tr>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > ����HSF�ӿ��ṩ </td>
	      	<td align="center" >����</td>
	      	<td align="center" >ʱ��(ms)</td>
	      </tr>
	      
	      <% 
	      		for (int i = 0; i < hsfIns.size(); i++) {
	      			ReportInvokeDataPo po = hsfIns.get(i);
	      %>
	      			 <tr>
	      				<td align="left"><%=po.getResouceName() %></td>
	      				<td align="left"><%=po.getCount() %></td>
	      				<td align="left"><%=po.getTime() %></td>   
	      			 </tr>	  
	      <%
	      		}
	      %>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > ����HSF�ӿ��ṩ </td>
	      	<td align="center" >����</td>
	      	<td align="center" >ʱ��(ms)</td>
	      </tr>
	      
	      <% 
	      		for (int i = 0; i < lastHsfIns.size(); i++) {
	      			ReportInvokeDataPo po = lastHsfIns.get(i);
	      %>
	      			 <tr>
	      				<td align="left"><%=po.getResouceName() %></td>
	      				<td align="left"><%=po.getCount() %></td>
	      				<td align="left"><%=po.getTime() %></td>   
	      			 </tr>	  
	      <%
	      		}
	      	}
	      %>
	      
	      
	      <% 
     	  		if (hsfOuts.size() > 0 || lastHsfOuts.size() > 0) {
     	  %>
	      <tr class="headcon ">
	        <td align="center" colspan="3"> HSF�ӿڵ���ǰ��λ</td>
	      </tr>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > ����HSF�ӿڵ��� </td>
	      	<td align="center" >����</td>
	      	<td align="center" >ʱ��(ms)</td>
	      </tr>
	      
	      <% 
	      		for (int i = 0; i < hsfOuts.size(); i++) {
	      			ReportInvokeDataPo po = hsfOuts.get(i);
	      %>
	      			 <tr>
	      				<td align="left"><%=po.getResouceName() %></td>
	      				<td align="left"><%=po.getCount() %></td>
	      				<td align="left"><%=po.getTime() %></td>   
	      			 </tr>	  
	      <%
	      		}
	      %>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > ����HSF�ӿڵ��� </td>
	      	<td align="center" >����</td>
	      	<td align="center" >ʱ��(ms)</td>
	      </tr>
	      
	      <% 
	      		for (int i = 0; i < lastHsfOuts.size(); i++) {
	      			ReportInvokeDataPo po = lastHsfOuts.get(i);
	      %>
	      			 <tr>
	      				<td align="left"><%=po.getResouceName() %></td>
	      				<td align="left"><%=po.getCount() %></td>
	      				<td align="left"><%=po.getTime() %></td>   
	      			 </tr>	  
	      <%
	      		}
	      	}
	      %>
	      
	</table>
<%} %> 
 </td></tr>  
 <tr>
      <td><img src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"/></td>
    </tr> 
</table>


<script type="text/javascript">

	var groupMap ={};

	function addAppGroup(company,groupName,appName){
	
		if(!groupMap[company]){
			groupMap[company] = {};
		}
		
		if(!groupMap[company][groupName]){
			groupMap[company] [groupName]={};
		}		
	}



	function companyChange(selectObj){
		renderGroup("");
	}
	function renderCompany(name){
		
		clearSelect("companyName");
		fillSelect("companyName",groupMap,name);
		
	}
	
	
	function renderGroup(name){
		var companyObj = document.getElementById("companyName");
		var company = companyObj.options[companyObj.selectedIndex].value;
		var group = groupMap[company];
		if(group){
			clearSelect("groupName");
			fillSelect("groupName",group,name);
		}
	}
	
	
	function fillSelect(id,group,value){
		var ops = document.getElementById(id).options;
		var len = ops.length;
		for (name in group){
			if(typeof group[name]=== 'object'){
				document.getElementById(id).options[len++]=new Option(name,name);
				if(name == value){
					document.getElementById(id).options[len-1].selected=true;
				}
			}else{
				document.getElementById(id).options[len++]=new Option(name,group[name]);
				if(group[name] == value){
					document.getElementById(id).options[len-1].selected=true;
				}
			}
		}
	}
	
	function clearSelect(id){
		document.getElementById(id).options.length=0;		
	}
	
	function initParentSelect(com,gname){
		renderCompany(com);
		renderGroup(gname);
	}
	<%
	Map<String,List<AppInfoPo>> appInfoGroupMap = new HashMap<String,List<AppInfoPo>>();
	for(AppInfoPo app:appList){
		ProductLine p = TBProductCache.getProductLineByAppName(app.getOpsName());
	%>
	addAppGroup("<%=p.getDevelopGroup()%>","<%=p.getProductline()%>","<%=app.getOpsName()%>");
	<%				
	}
	
	%>
	 initParentSelect("<%=companyName%>","<%=groupName%>");

</script>

<jsp:include page="bottom.jsp"></jsp:include>
</center>

</body>
</html>
