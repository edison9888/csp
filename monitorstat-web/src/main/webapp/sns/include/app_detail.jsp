<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.taobao.moinitor.matrix.dataobject.*" %>
<%@ page import="com.taobao.moinitor.matrix.enumtype.*" %>
<%@ page import="com.taobao.moinitor.matrix.util.*" %>
<%@ page import="com.taobao.moinitor.matrix.*" %>
<%@ page import="com.taobao.moinitor.matrix.parser.MatrixAppParser" %>

<%
	// 初始化数据
	Date viewDate = Utils.zerolizedTime(Utils.toDate(request.getParameter("date"), "yyyy-MM-dd", new Date()));
    AppPo app = AppPo.get(request.getParameter("name"));
    
    Map<HostGroupPo, List<AnalyzeResultPo>> loads = MatrixAO.instance.showAppDetail(app.getName(), HaBoSrvEnum.LOAD_1, viewDate, true);
    Map<HostGroupPo, List<AnalyzeResultPo>> qpses = MatrixAO.instance.showAppDetail(app.getName(), HaBoSrvEnum.QPS, viewDate, true);
    BigDecimal	summary =MatrixAO.instance.getTotalVisite(app,HaBoSrvEnum.QPS,viewDate);
    HostGroupPo hostGroupPo = null;
    
%>

<%@page import="com.taobao.monitor.applog.domain.HsfDayLogDO"%><div class="input-area">
	<form name="input-form" method="get">
		<input type="hidden" name="name" value="<%= app.getName() %>" />
		<span>日期:</span>
		<input type="text" id="datepicker" name="date" value="<%= Utils.toString(viewDate, "yyyy-MM-dd") %>" />
		<input type="submit" value="查看详情"></input>
	</form>
</div>
<div class="groups">
	<%
	for (HostGroupPo group : app.getAllHostGroups()) {
		List<AnalyzeResultPo> load = loads.get(group);
		List<AnalyzeResultPo> qps = qpses.get(group);
	%>
	<div class="group">
  	<div class="group-name"><%= group.getName() %></div>
    <div>
   		<div class="qps-table-wrapper">
        	<span>QPS</span>
			<table class="qps-table" border="1px">
              <tr>
              	<th>机器</th>
                <th>Max</th>
                <th>Min</th>
                <th>Average</th>
                <th>Current</th>
                <th title="一周最大访问">Max Visit</th>
                <th title="一周最大QPS">Max QPS</th>
              </tr>                 
              <% for (AnalyzeResultPo result : qps) {%>
              <tr>
              	<td><img src="<%=request.getContextPath () %>/statics/images/report.png" graph-src="<%=Utils.getGraphUrl(result.getHaBoPo(), HaBoSrvEnum.QPS, viewDate, Utils.addDate(viewDate, 1,Calendar.DAY_OF_MONTH))%>" />
              		<%
              			if (result.getHaBoPo() instanceof HostGroupPo) {
              		    hostGroupPo =(HostGroupPo)result.getHaBoPo();
              		%>
              			Average
              		<%  } else { %>
              			<a target="_blank" href="http://monitor.taobao.com/monitor-report/secitcapacity/opsFreeAnalysis/productTree/hostNode/norm/hostInfo.htm?hostName=<%= result.getHaBoPo().getName() %>"><%= result.getHaBoPo().getName() %></a>
              		<% } %>
              	</td>
                <td><label title="<%=Utils.formatDate(result.getMax().getTime(), "HH:mm:ss") %>"><%= result.getMax() %></label></td>
                <td><label title="<%=Utils.formatDate(result.getMin().getTime(), "HH:mm:ss") %>"><%= result.getMin() %></label></td>
                <td><%= result.getAverage() %></td>
                <td><label title="<%=Utils.formatDate(result.getCurrent().getTime(), "HH:mm:ss") %>"><%= result.getCurrent()%></label></td>
              	<td>
              	<% 
              		VisitPerDay visitPerDay = VisitPerDayAO.instance.getBigestInWeek(viewDate,result.getHaBoPo().getName(),result.getHaBoPo().getType());
              		if(visitPerDay == null){
              			out.println("NoData");
              		}else{
              	%>
              		<label title="<%=Utils.formatDate(visitPerDay.getDayDate(),"yyyy-MM-dd") %>">	<%=visitPerDay.getCount()%></label>
                <%
              		}
              	%>
              	</td>
              	<td>
              		<%
              			MaxQpsEveryDay maxQPS = MaxQpsEveryDayAO.instance.getBigestMaxQps(viewDate,result.getHaBoPo().getName(),result.getHaBoPo().getType());
              			if(maxQPS == null){
              				out.println("NoData");
              			}else{
              		%>
              			<label title="<%=Utils.formatDate(maxQPS.getDayDate(),"yyyy-MM-dd") %>"><%=maxQPS.getCount()%></label>
              		<%
              			}
              		%>
              	</td>
              </tr>
              <%} %>
            </table>
        </div><!-- end of qps-table-wrapper-->
        
        <div class="load-table-wrapper"><span>Load</span>
            <table class="load-table" border="1px">
              <tr>
              	<th>机器</th>
                <th>Max</th>
                <th>Min</th>
                <th>Average</th>
                <th>Current</th>
              </tr>
              <% for (AnalyzeResultPo result : load) { %>
              <tr>
             	<td><img src="<%=request.getContextPath () %>/statics/images/report.png" graph-src="<%=Utils.getGraphUrl(result.getHaBoPo(), HaBoSrvEnum.LOAD_1, viewDate, Utils.addDate(viewDate, 1,Calendar.DAY_OF_MONTH))%>" />
              		<%
              			if (result.getHaBoPo() instanceof HostGroupPo) {
              		%>
              			Average
              		<%  } else { %>
              			<a target="_blank" href="http://monitor.taobao.com/monitor-report/secitcapacity/opsFreeAnalysis/productTree/hostNode/norm/hostInfo.htm?hostName=<%= result.getHaBoPo().getName() %>"><%= result.getHaBoPo().getName() %></a>
              		<% } %>
              	</td>
                <td><label title="<%=Utils.formatDate(result.getMax().getTime(), "HH:mm:ss") %>"><%= result.getMax() %></label></td>
                <td><label title="<%=Utils.formatDate(result.getMin().getTime(), "HH:mm:ss") %>"><%= result.getMin() %></label></td>
                <td><%= result.getAverage() %></td>
                <td><label title="<%=Utils.formatDate(result.getCurrent().getTime(), "HH:mm:ss") %>"><%= result.getCurrent()%></label></td>
              </tr>
              <%}%>              
          </table>
      </div><!-- end of load-table-wrapper -->
    </div>
  </div><!-- end of group -->
  <div style="clear:both;"> </div>
  <div class="summary">
  	<span>全天的访问量：</span><font color="red"><%=summary.toString()%></font>
  </div>
  <div class="graph">
	  <div class="qps-graph">
	                <span>QPS走势图：</span>
	                <div style="clear:both;"> </div>
	        <img style="float:left;" src="<%=Utils.getGraphUrl(hostGroupPo, HaBoSrvEnum.QPS, viewDate, Utils.addDate(viewDate, 1,Calendar.DAY_OF_MONTH))%>" graph-src="<%=Utils.getGraphUrl(hostGroupPo, HaBoSrvEnum.QPS, viewDate, Utils.addDate(viewDate, 1,Calendar.DAY_OF_MONTH))%>"></img>
	  </div>
	                <div style="clear:both;margin-bottom:10px;"> </div>
	  <div class="load-graph">
	                <span>LOAD走势图：</span>
	                <div style="clear:both;"> </div>
	                <img style="float:left;" src="<%=Utils.getGraphUrl(hostGroupPo, HaBoSrvEnum.LOAD_1, viewDate, Utils.addDate(viewDate, 1,Calendar.DAY_OF_MONTH))%>" graph-src="<%=Utils.getGraphUrl(hostGroupPo, HaBoSrvEnum.LOAD_1, viewDate, Utils.addDate(viewDate, 1,Calendar.DAY_OF_MONTH))%>"></img>
		</div>
	</div><div style="clear:both;margin-bottom:20px;"> </div>
  <%
	}
  %>

  <div class="hsf">
  	<%
  	try{
  	 for(Map.Entry<String, Set<HsfDayLogDO>> entry : MatrixAO.instance.getDayLogList(app, viewDate).entrySet()) {
  		String service = entry.getKey();  	
  		Set<HsfDayLogDO> logs = entry.getValue();
  	%>
  	<div class="hsf-interface"><%=service %></div>
  	<table class="hsf-method-table" border="1px">
  		<thead>
  			<tr>
  				<th rowspan="2">方法</th>
  				<th colspan="3">响应次数</th>
  				<th colspan="3">响应时间</th>
  				<th rowspan="2">总响应<br />次数</th>
  				<th rowspan="2">总响应<br />时间</th>
  				<th rowspan="2">平均响<br />应时间</th>
  			</tr>
  			<tr>
  				<th>max1</th>
  				<th>avg1</th>
  				<th>min1</th>
  				
  				<th>max2</th>
  				<th>avg2</th>
  				<th>min2</th>
  			</tr>
  		</thead>
  		
  		<tbody>
  			<%for (HsfDayLogDO log : logs) { %>
  			<tr>
  				<td><img graph-src="<%=request.getContextPath () %>/sns/hsf_detail.jsp?appId=<%=log.getAppId() %>&childAppId=<%=log.getChildAppId() %>&date=<%=Utils.formatDate(viewDate, "yyyy-MM-dd") %>&level1=<%=log.getLevel1Id() %>&level2=<%=log.getLevel2Id() %>&level3=<%=log.getLevel3Id() %>" src="<%=request.getContextPath () %>/statics/images/report.png"><%=log.getLevel2Name() %></td>
  				<td><%=Utils.round(log.getMaxValue1(), 2) %></td>
  				<td><%=Utils.round(log.getAvgValue1(), 2) %></td>
  				<td><%=Utils.round(log.getMinValue1(), 2) %></td>
  				<td><%=Utils.round(log.getMaxValue2(), 2) %></td>
  				<td><%=Utils.round(log.getAvgValue2(), 2) %></td>
  				<td><%=Utils.round(log.getMinValue2(), 2) %></td>
  				<td><%=Utils.round(log.getTotalValue1(), 2) %></td>
  				<td><%=Utils.round(log.getTotalValue2(), 2) %></td>
  				<td><%=log.getTotalValue1() > 0 ? Utils.round(log.getTotalValue2() / log.getTotalValue1(), 2) : "N/A"%></td>
  			</tr>
  			<%} %>
  		</tbody>
  	</table>
  	<%}}catch(Exception e){
  		out.println("获得HSF信息出错了");
  	} %>
  </div>
</div>

<div id="dialog_report" title="Basic modal dialog">
	<iframe id="iframe_report" src="" frameborder="0" height="800px" width="800px" marginheight="0" marginwidth="0" scrolling="no"></iframe>
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
		height: 550,
		width:800,
		modal: true,
		draggable:true,
		resizable:false,
		autoOpen:false
	});
});
</script>
  	