<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Ӧ����������</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../top.jsp" %>

<form action="/capacity/show.do?method=showCapacityDetail" method="post">
<div align="right">
<strong><font color="#870A08" size = "2">
��${appName}�����������ٷ�֮&nbsp;<input type="text" id="raise" name="raise" class="small" value="${percentage}"/> &nbsp;&nbsp
<input type="submit" value="��������ˮλ "/>
<input type="hidden" value="${appId}" id="appId" name="appId" />
</font></strong>
</div>
<table class="bordered-table"  id="detail">  
     <thead align="center">
      <tr align="center">
      	<th style="width:20%">����Ӧ����</th>
      	<th style="width:30%">����������</th>
        <th style="width:30%">����Ӱ���</th>
        <th style="width:20%">����ˮλ</th>
      </tr>
      </thead>
      
      <tbody>
     
      <c:forEach items="${list}" var="o">
      <tr>
      	<td align="center">${o.depApp}</td>
      	<td align="center">${o.formatDepPv}</td>
      	<td align="center">${o.formatPercent}</td>
      	<td align="center">${o.formatLevel}</td>
	  </tr>
	 </c:forEach>
 </tbody>

</table>

<table class="bordered-table">
	<tr>
      <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1200" height="1"></td>
	</tr>
	<tr>
	  <td>
	  <div id="tabs">
			<ul>
					<li><a href="#tabs-1">��������</a></li>
			</ul>
	    <div id="tabs-1">		
		</div>
		<div id="tabs-2">		
		</div>
		</div>
		</td>
	</tr>
	<tr>
	  <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1200" height="1"></td>
	</tr>
	<tr><td></td></tr>
</table>

</form>

<script type="text/javascript">

 $(function() {
	$("#tabs").tabs();
}); 

var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amcolumn.swf", "amline", "100%", "400", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amcolum_setting.xml?2");
so.addVariable("chart_data", encodeURIComponent("${flashString}"));
so.write("tabs-1");

</script>

</body>
</html>