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
<title>本周系统报告</title>
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

// 默认设置成聚划算
if(companyName ==null || companyName.length() == 0 || groupName == null || groupName.length() ==0){
	companyName = "聚划算";
	groupName = "聚划算";
}

List<AppInfoPo> needReportApp = new ArrayList<AppInfoPo>();

// 二级产品线
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

	<input type="text" name="dateTime" value="<%=sdf.format(dateTime)%>"/> <input type="submit" value="查看这个时间一周情况"/>
</form>

  <%
  
  	WeekReportDataProvider dataProvider = new WeekReportDataProvider(fristDate, dateTime, needReportApp);
  
  %>
  
<table width="1000" align="center" style="margin:0 auto;" >
  <p></p>
  <tr>
    <td colspan="15" align="center"  class="tt2">当前应用水位情况[<%=sdf.format(dateTime)%>]</td>
  </tr> 
    <tr><td><table class="datalist" width="1000">
  <tr class="headcon ">
    <td>应用</td>
    <td>机器数</td>
    <td  align="center">当前PV</td>
    <td  align="center">当前水位</td>
    <td  align="center">归一化水位</td>
    <td  align="center">单次调用成本</td>
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
    <td colspan="15" align="center"  class="tt2">本周应用情况[<%=sdf.format(fristDate)+"-"+sdf.format(dateTime)%>]</td>
  </tr> 
    <tr><td><table class="datalist" width="1000">
  <tr class="headcon ">
    <td>应用</td>
    <td colspan="2" align="center">PV</td>
    <td colspan="2" align="center">UV</td>
    <td colspan="2" align="center">QPS</td>
    <td colspan="2" align="center">RT</td>
    <td colspan="2" align="center">LOAD</td>
    <td colspan="2" align="center">FullGC</td>
    <td colspan="2" align="center">小GC次数</td>

  </tr>
  <tr class="ui-widget-header ">
    <td>&nbsp;</td>
    <td align="center">最大值</td>    
    <td align="center">趋势</td>
    <td align="center">最大值</td>   
    <td align="center">趋势</td>
    <td align="center">最大值</td>   
    <td align="center">趋势</td>
    <td align="center">最大值</td>    
    <td align="center">趋势</td>
    <td align="center">最大值</td>   
    <td align="center">趋势</td>
     <td align="center">次数</td>   
    <td align="center">时间</td>
    <td align="center">最大值</td>   
    <td align="center">趋势</td>
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
		<td>最大值：</td>
		<td>
			前端应用pv表示每天汇总值在本周最大的那天的值，C应用表示 所有接口一天调用总计在一周中最大那天的值。<br/>
			前端应用QPS 和RT 计算方式为 先将一天的20:30 到22:30 平均，然后取本周中最大的那天，<br/>
		<br/>
		</td>
	</tr>	
	<tr>
		<td>趋势：</td>
		<td>本周与上周的一个对比情况，取上周的平均值与本周平均值进行对比 </td>
	</tr>
	<tr>
		<td>IN：</td>
		<td>表示外部系统调用本系统产生的，一般是C应用 </td>
	</tr>
	<tr>
		<td>OUT：</td>
		<td>表示本系统调用外部应用一般是前端应用 </td>
	</tr>
</table>


<table width="1000" align="center" style="margin:0 auto;" >
	 <%
	 for(AppInfoPo appPo:needReportApp){
	   	String appName = appPo.getOpsName();
	 %>
  <p></p>
  <tr class="tt2">
    <td align="center"><%=appPo.getAppName()%>调用情况</td>
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
	        <td align="center" colspan="3"> url访问前五位</td>
	      </tr>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > 本周访问的url </td>
	      	<td align="center" >次数</td>
	      	<td align="center" >时间(ms)</td>
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
	      	<td align="center" > 上周访问的url </td>
	      	<td align="center" >次数</td>
	      	<td align="center" >时间(ms)</td>
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
	        <td align="center" colspan="3"> HSF接口提供前五位</td>
	      </tr>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > 本周HSF接口提供 </td>
	      	<td align="center" >次数</td>
	      	<td align="center" >时间(ms)</td>
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
	      	<td align="center" > 上周HSF接口提供 </td>
	      	<td align="center" >次数</td>
	      	<td align="center" >时间(ms)</td>
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
	        <td align="center" colspan="3"> HSF接口调用前五位</td>
	      </tr>
	      
	      <tr class="ui-widget-header ">
	      	<td align="center" > 本周HSF接口调用 </td>
	      	<td align="center" >次数</td>
	      	<td align="center" >时间(ms)</td>
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
	      	<td align="center" > 上周HSF接口调用 </td>
	      	<td align="center" >次数</td>
	      	<td align="center" >时间(ms)</td>
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
