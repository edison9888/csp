<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityRankingPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@page import="com.taobao.csp.capacity.util.LocalUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Tair�����滮</title>

</head>
<body style="padding-top:45px" class="span20">
<%@ include file="../top.jsp" %>
  
<%
List<CapacityRankingPo> limitList = new ArrayList<CapacityRankingPo>();

Object obj = request.getAttribute("limitList");
Object flashString = request.getAttribute("flashString");
Object forestFlashString = request.getAttribute("forestFlashString");
Object year = request.getAttribute("year");
if(obj != null){
	limitList = (List<CapacityRankingPo>)obj;
}

%>
<table   id="mytable" class="bordered-table">
	  <thead align="center">
      <tr>
        <th colspan="4">������Ϣ</th>
        <th colspan="6">��ǰ����</th>
        <th colspan="5">����Ԥ��</th>    
        <th colspan="1">������Ϣ</th>            
      </tr>
	  <tr>
	  	<th>����</th>
        <th>����</th>
        <th>������</th>
        <th>������</th>
        
        <th>��׼</th>
        <th>��������</th>
        <th>��������</th>
        <th>��Ⱥ����</th>
        <th>��Ⱥ����</th>
        <th>ˮλ</th>
        
        <th>������</th>
        <th>��Ⱥ����</th>
        <th>ˮλ</th>
        <th>�������</th>
        <th>��������</th>
        
        <th>PE</th>
        
      </tr>
      </thead>
      
      <tfoot>
      </tfoot>

	  <tbody>
      <%
      for(int i=0;i<limitList.size();i++){
    	  CapacityRankingPo po = limitList.get(i);
    	  int appId = po.getAppId();
      %>
      <tr>
      	<!--<td width="30" align="center"><%=i+1 %></td>-->
      	<td><a href="<%=request.getContextPath() %>/show.do?method=showCapacityDetail&year=<%=year %>&appId=<%=po.getAppId() %>">
      	<%=po.getAppName() %></a></td>
      	<td><%=po.getAppType() %></td> 
      	<td><%=po.getFeatureData("������") %></td>
      	<td><%=po.getFeatureData("������") %></td>
      	
      	<td><%=po.getFeatureData("������׼") %></td>
      	<td><a href="" id="single_<%=po.getAppName() %>" rel="popover" 
      		data-content="<table class='table table-striped table-bordered table-condensed'>
      			<tr><td>ip:</td><td><%=po.getFeatureData("�ɼ�����") %></td></tr>
      			<tr><td>time:</td><td><%=po.getFeatureData("�ɼ�ʱ��") %></td></tr></table>" data-original-title="">
      	<%=po.getFeatureData("��̨����") %> </a></td>
      	<td><a href="http://autoload.taobao.net:9999/autoload/loadrun/show.do?method=show&appId=<%=po.getAppId()%>&collectTime=<%=po.getFeatureData("ѹ��ʱ��") %>" target="_blank"><%=po.getFeatureData("��̨����") %></a></td>
      	<td><%=po.getFeatureData("��Ⱥ����") %></td>
      	<td><%=po.getFeatureData("��Ⱥ����") %></td>
      	<td><%=po.getFeatureData("����ˮλ") %></td>
      	
      	<td><%=po.getFeatureData("ҵ��������") %></td>
      	<td><%=po.getFeatureData("Ԥ�⼯Ⱥ����") %></td>
      	<td><%=po.getFeatureData("Ԥ������ˮλ") %></td>
      	<td><%=po.getFeatureData("Ԥ�������") %></td>
      	<td><%=po.getFeatureData("Ԥ���������") %></td>
      	
      	<td><%=LocalUtil.getPe(po.getAppId()) %></td>
      	
	</tr>
 <%} %>

	  </tbody>
	  </table>
<table class="bordered-table">
	<tr>
      <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1200" height="1"></td>
	</tr>
	<tr>
	  <td>
	  	  <div class="tabbable"> <!-- Only required for left/right tabs -->
 			<ul class="nav nav-tabs">
    			<li class="active"><a href="#tab1" data-toggle="tab">��ǰˮλ</a></li>
    			<li><a href="#tab2" data-toggle="tab">Ԥ��ˮλ</a></li>
  			</ul>
  			<div class="tab-content">
    			<div class="tab-pane active" id="tab1">
      				<table width="100%" border="1" class="ui-widget ui-widget-content">			
						<tr >
							<td id="tabs-1"></td>
						</tr>
					</table>
    			</div>
    			<div class="tab-pane" id="tab2">
      				<table width="100%" border="1" class="ui-widget ui-widget-content">			
						<tr >
							<td id="tabs-2"></td>
						</tr>
					</table>
    			</div>
  			</div>
		 </div>
		</td>
	</tr>
	<tr>
	  <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1200" height="1"></td>
	</tr>
	<tr><td></td></tr>
</table>


<script type="text/javascript">
 $(function() {
	$("#tabs").tabs();
}); 

var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amcolumn.swf", "amline", "100%", "400", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amcolum_setting.xml?2");
so.addVariable("chart_data", encodeURIComponent("<%=flashString.toString()%>"));
so.write("tabs-1");

var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amcolumn.swf", "amline", "100%", "400", "8", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amcolum_setting.xml?2");
so1.addVariable("chart_data", encodeURIComponent("<%=forestFlashString.toString()%>"));
so1.write("tabs-2");

$("#mytable tr:eq(0)").find("th:eq(0)").css("background-color", "#ECECEC");
$("#mytable tr:eq(1)").find("th:eq(0),th:eq(1),th:eq(2),th:eq(3)").css("background-color", "#ECECEC");

$("#mytable tr:eq(0)").find("th:eq(1)").css("background-color", "#E5E5E5");
$("#mytable tr:eq(1)").find("th:eq(4),th:eq(5),th:eq(6),th:eq(7),th:eq(8),th:eq(9)").css("background-color", "#E5E5E5");

$("#mytable tr:eq(0)").find("th:eq(2)").css("background-color", "#FCFBD1");
$("#mytable tr:eq(1)").find("th:eq(10),th:eq(11),th:eq(12),th:eq(13),th:eq(14)").css("background-color", "#FCFBD1");

$(function(){
      $("#mytable tr td").mouseover(function(){
         $(this).parent().children("td").addClass("report_on");
      })
      $("#mytable tr td").mouseout(function(){
         $(this).parent().children("td").removeClass("report_on");
      })
   })
   
$(document).ready(function() { 
   $("#mytable").fixedtableheader({ 
   ����headerrowsize:2
   }); 
});

$("a[rel=popover]")  .popover({placement:'right'}) .click(function(e) {
     e.preventDefault()
})


</script>
</body>
</html>