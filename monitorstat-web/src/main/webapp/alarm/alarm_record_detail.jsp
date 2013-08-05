<%@page import="com.taobao.monitor.web.vo.AlarmDescPo"%>
<%@page import="org.omg.PortableServer.POA"%>
<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmRecordPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Calendar"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>应用监控系统-告警历史记录</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/statics/js/jscript.isselect.js"></script>
 <style type="text/css">
.fc {
display:block;
margin-top:-15px;
overflow:hidden;
position:absolute;
z-index:1000;
}

.fci {
border:1px solid #7589C3;
margin-left:9px;
position:relative;
width:325px;
}

.fcc {
background:none repeat scroll 0 0 #FFFFFF;
border:4px solid #C1CFF3;
max-height:260px;
overflow-x:hidden;
overflow-y:auto;
padding:10px 15px;
width:auto;
}
 
.jj8 {
background-position:-30px -50px;
display:block;
height:25px;
left:1px;
position:absolute;
top:8px;
width:11px;
z-index:999;
}

 
.jj {
background-image:url("<%=request.getContextPath () %>/statics/images/s.gif");
overflow:hidden;
}

</style>
 
<script type="text/javascript">
function checkAlarm(){
var objs=document.getElementsByName('alarm');
var isSel=false;//判断是否有选中项，默认为无
for(var i=0;i<objs.length;i++)
{
  if(objs[i].checked==true)
   {
    isSel=true;
    break;
   }
}
if(isSel==false){
	alert("对不起!您没有选择任何的告警名称!"); 
	return false;
}else{
	return true;
} 
}

function noshow(obj){
	var el = obj.getElementsByTagName("div")[0];
	el.style.display='none';
}

function show(obj){
	var el = obj.getElementsByTagName("div")[0];
	el.style.display='block';
}
</script>

</head>
<body>
<center>
 <jsp:include page="../top.jsp"></jsp:include>
  	
	<jsp:include page="../left.jsp"></jsp:include>
<%
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH,-1);
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
String startTime = request.getParameter("startTime");
if(startTime==null){
	startTime = sdf.format(cal.getTime());
};
cal.add(Calendar.DAY_OF_MONTH,1);
String endTime = request.getParameter("endTime");
if(endTime==null){
	endTime = sdf.format(cal.getTime());
}

String appId = request.getParameter("appId");

List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
if(appId==null){
	appId = listApp.get(0).getAppId()+"";
}
%>
<form action="alarm_record_detail.jsp" method="post">
<table width="1000"  >
  <tr>
    <td colspan="4" align="center">
    	应用名称:<select name="appId">
			<%for(AppInfoPo app:listApp){%> 
				<option value="<%=app.getAppId() %>" <%if(app.getAppId()==Integer.parseInt(appId)){out.print("selected");} %>><%=app.getAppName() %></option>
			<%} %>
			</select>
 
    	起始日期:<input type="text" name="startTime"  value="<%=startTime%>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm'})" />
    	截止日期:<input type="text" name="endTime" value="<%=endTime%>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm'})" />
	</td>
  </tr>
  <tr>
  	<td colspan="5" align="center">
  		<input type="submit" value="查询告警信息" />&nbsp;&nbsp;&nbsp;<input type="button" value="提交优化记录" onclick="window.open('http://cm.taobao.net:9999/monitorstat/health/manage_optimize_record.jsp?appid=<%=appId %>')"/>
  	</td>
  </tr>
</table>
</form >
<tr>
<form action="desc.jsp" onsubmit="return checkAlarm() " method="post">
<table class="datalist"  width="1000" >
  <tr class="ui-widget-header ">
  	<td width="80" align="center"><input type="checkbox" onClick="uncheckAll(this.checked,'alarm')"/></td>
    <td width="120" align="center">告警名称</td>
    <td width="200" align="center">告警站点</td>
    <td width="190" align="center">告警值</td> 
    <td width="190" align="center">告警原因</td> 
    <td width="220" align="center">告警时间</td>   
  </tr>  
  <%
  List<AlarmRecordPo> list = MonitorAlarmAo.get().findAlarmRecordByAppAndTime(Integer.parseInt(appId),sdf.parse(startTime),sdf.parse(endTime));
  for(AlarmRecordPo po:list){
	 SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	 String collectTime = sdf2.format(po.getCollectTime());
	 AlarmDescPo descpo = MonitorAlarmAo.get().findAlarmDescById(po.getId());
	 
  %>
  	
	<tr>
		<td align="center">
		<input type="checkbox" name="alarm" id="alarm" value="<%=po.getId()%>&<%=po.getAlarmKeyName() %>" <%if(descpo.getAlarmReason()!= null){out.print("disabled");}%>/></td>
    	<td align="center" ><%=po.getAlarmKeyName() %></td>
    	<td align="center"><%=po.getSiteName()%></td>
		<td align="center"><%=po.getAlarmValue()%></td>
		<td align="center" onmouseover="show(this)" onmouseout="noshow(this)" >
		
		<div class="fc" id="div_1" style="margin-left: 150px; display: none;" > 
		   <div class="fci">
		     <div class="fcc" style="text-align: left">
		            <%if(descpo.getAlarmDesc()!= null ) out.print("告警描述信息:<br />"+descpo.getAlarmDesc());else out.print("没有任何告警描述信息");%>
		     </div> 
		   </div> 
		<div class="jj jj8" id="1_divicon"></div> </div>
		
		 
		<%
		    if(descpo.getAlarmDesc()!= null)
			    out.print(descpo.getAlarmReason());
		    else
		    	out.print("");
		%> 
 
		</td>
		<td align="center"><%=collectTime %></td>
  	</tr>	
    
  <%
  }
  %>  
  <%
  //////////////////显示北斗的监控告警数据
  
  %>  
  </table> 

  <tr>
  	   
  	    
  	   <td align="center" colspan="5"><input type="hidden" value="<%=appId%>" name="appId" Id="appId"/>
  	   <input type="hidden" value="<%=startTime%>" name="startTime" id="startTime" />
  	   <input type="hidden" value="<%=endTime%>" name="endTime" id="endTime" /><input type="submit" value="告警描述填写"/></td>
  </tr>
  
<jsp:include page="../bottom.jsp"></jsp:include>
</form>

</tr>
</center>
</body>
</html>