<%@page import="com.taobao.csp.time.util.DataUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.taobao.csp.time.web.po.RealTimeTradePo"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<%
	List<RealTimeTradePo> poList = (List<RealTimeTradePo>) request
			.getAttribute("poList");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
%>
<table width="100%" align="center" style="margin:0 auto;" >
	<tr>
		<td align="center">
			<font color="#FF0000">交易数据</font>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%"   class="table table-striped table-bordered table-condensed">
			  <tr>
			    <td align="center">时间</td>
			    <td align="center">全网创建订单</td>
			    <td align="center">全网交易额</td>
			    <td align="center">天猫创建订单</td>
			    <td align="center">天猫交易额</td>
			  </tr>
			  <%
			  	for (RealTimeTradePo po : poList) {
			  		if(po.getTime() == null)
			  			continue;
			  %>
				<tr>
					<td><%=sdfTime.format(po.getTime())%></td>
					<td><%=po.getC2cCreateCnt()%></td>
					<td><%=po.getC2cPaidCnt()%></td>
					<td><%=po.getB2cCreateCnt()%></td>
					<td><%=po.getB2cPaidCnt()%></td>
				</tr>
				<%
					}
				%>
			</table>
		</td>
	</tr>
	</table>
