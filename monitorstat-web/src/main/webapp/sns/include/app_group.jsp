<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.moinitor.matrix.dataobject.*" %>
<%@ page import="com.taobao.moinitor.matrix.enumtype.*" %>
<%@ page import="com.taobao.moinitor.matrix.util.*" %>
<%@ page import="com.taobao.moinitor.matrix.*" %>
<%@ page import="com.taobao.moinitor.matrix.parser.MatrixAppParser" %>
<%@ page import="java.net.URLDecoder"%>

<%	// 初始化数据
	Date viewDate = Utils.zerolizedTime(Utils.toDate(request.getParameter("date"), "yyyy-MM-dd", new Date()));
	String appGroupName = request.getParameter("app-group");
	
	appGroupName = appGroupName == null ? null : URLDecoder.decode(appGroupName, "utf-8");
	
	AppGroupPo appGroup = AppGroupPo.get(appGroupName) == null ? AppGroupPo.getIndexShowAppGroup() : AppGroupPo.get(appGroupName);
	
	List<AnalyzeResultPo> loads = MatrixAO.instance.showIndex(appGroup, HaBoSrvEnum.LOAD_1, viewDate, true);
	List<AnalyzeResultPo> qpses = MatrixAO.instance.showIndex(appGroup, HaBoSrvEnum.QPS, viewDate, true);
%>

<div class="input-area">
	<form name="input-form" method="get">
		<select name="app-group">
			<% for (AppGroupPo app : AppGroupPo.getAllAppGroups()) {%>
				<option value="<%=java.net.URLEncoder.encode(app.getGroupName(), "utf-8") %>" <%=app.getGroupName().equals(appGroup.getGroupName()) ? "selected" : "" %> ><%=app.getGroupName() %></option>
			<% } %>
		</select>
		<span>日期:</span>
		<input type="text" id="datepicker" name="date" value="<%= Utils.toString(viewDate, "yyyy-MM-dd") %>" />
		<input type="submit" value="查看详情"></input>
	</form>
</div>
<div class="app">
    <div>
   		<div class="qps-table-wrapper"> 
   			<div class="srv-type">QPS</div>       	
			<table class="qps-table" border="1px">
              <tr>
              	<th>应用</th>
                <th>Max</th>
                <th>Min</th>
                <th>Average</th>
                <th>Current</th>
              </tr>
              
              <% for (AnalyzeResultPo result : qpses) {	%>
              <tr>
              	<td><img src="<%=request.getContextPath () %>/statics/images/report.png" graph-src="<%=Utils.getGraphUrl(result.getHaBoPo(), HaBoSrvEnum.QPS, viewDate, Utils.addDate(viewDate, 1,Calendar.DAY_OF_MONTH)) %>"></img><a href="app_detail.jsp?name=<%=result.getHaBoPo().getName() %>&date=<%= Utils.toString(viewDate, "yyyy-MM-dd") %>"><%= result.getHaBoPo().getName() %></a></td>
                <td><label title="<%=Utils.formatDate(result.getMax().getTime(), "HH:mm:ss") %>"><%= result.getMax() %></label></td>
                <td><label title="<%=Utils.formatDate(result.getMin().getTime(), "HH:mm:ss") %>"><%= result.getMin() %></label></td>
                <td><%= result.getAverage() %></td>
                <td><label title="<%=Utils.formatDate(result.getCurrent().getTime(), "HH:mm:ss") %>"><%= result.getCurrent()%></label></td>
              </tr>
              <%} %>
            </table>
        </div><!-- end of qps-table-wrapper-->
        
        <div class="load-table-wrapper">
        	<div class="srv-type">Load</div>
            <table class="load-table" border="1px">
              <tr>
              	<th>应用</th>
                <th>Max</th>
                <th>Min</th>
                <th>Average</th>
                <th>Current</th>
                <th>机器数量</th>
              </tr>
             
              	<% for (AnalyzeResultPo result : loads) { %>
              	<tr>
	                <td><img src="<%=request.getContextPath () %>/statics/images/report.png" graph-src="<%=Utils.getGraphUrl(result.getHaBoPo(), HaBoSrvEnum.LOAD_1, viewDate, Utils.addDate(viewDate, 1,Calendar.DAY_OF_MONTH))%>"></img><a href="app_detail.jsp?name=<%= result.getHaBoPo().getName() %>&date=<%= Utils.toString(viewDate, "yyyy-MM-dd") %>"><%= result.getHaBoPo().getName() %></a></td>
	                <td><label title="<%=Utils.formatDate(result.getMax().getTime(), "HH:mm:ss") %>"><%=result.getMax()%></label></td>
	                <td><label title="<%=Utils.formatDate(result.getMin().getTime(), "HH:mm:ss") %>"><%=result.getMin()%></label></td>
	                <td><%=result.getAverage()%></td>
	                <td><label title="<%=Utils.formatDate(result.getCurrent().getTime(), "HH:mm:ss") %>"><%=result.getCurrent()%></label></td>
	                <td><%=result.getHaBoPo().getHostCount() %></td>
                </tr>
                <%} %>  
          </table>
      </div><!-- end of load-table-wrapper -->
    </div>
</div><!-- end of app -->

<div id="dialog_report" title="Basic modal dialog">
	<iframe id="iframe_report" src="" frameborder="0" height="450" width="800" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>


<script type="text/javascript">
$(function() {
	$('#datepicker').datepicker({
		changeMonth: true,
		changeYear: true
	});
	$('#datepicker').datepicker('option', {dateFormat: 'yy-mm-dd'});
	$("#datepicker").datepicker($.datepicker.regional['zh-CN']);
});

$(document).ready(function() {
	$('img').click(function(){
		document.getElementById("iframe_report").setAttribute("src", this.getAttribute('graph-src'));
		$("#dialog_report").dialog("open");
	});
});

$(function() {
	$("#dialog_report").dialog({
		bgiframe: true,
		height: 400,
		width:800,
		modal: true,
		draggable:true,
		resizable:false,
		autoOpen:false
	});
});
</script>