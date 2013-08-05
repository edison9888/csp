<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<table class="span12" class="table table-striped table-bordered table-condensed">
	<!-- 
	<thead align="center">
      <tr>
        <th style="width:25%">依赖应用</th>
        <th style="width:25%">当前QPS</th>
        <th style="width:25%">安全QPS</th>
        <th style="width:25%">压测QPS</th>           
      </tr>
     </thead>
     
     <tbody>
      <c:forEach items="${map}" var="o">
      <tr>
      	<td align="center">${o.key}</td>
      	<td align="center">${o.value[0]}</td>
      	<td align="center">${o.value[1]}</td>
      	<td align="center">${o.value[2]}</td>
      </tr>
      </c:forEach>

	<tr>
      <td colspan="4">&nbsp;</td>
	</tr>

	<tr><td></td></tr>
	 -->
	<tr>
      <td colspan="4" id="chartdivLoad"></td>
	</tr>
 </tbody>
</table>


<script type="text/javascript">

var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amcolumn.swf", "amline", "100%", "400", "8", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline1");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amcolum_setting1.xml");
so1.addVariable("chart_data", encodeURIComponent("${chartLoad}"));
so1.write("chartdivLoad");
</script>