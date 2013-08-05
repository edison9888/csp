<%@ page language="java" contentType="text/html; charset=gb2312"
	pageEncoding="gb2312"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.po.AuctionsHot"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link
	href="<%=request.getContextPath()%>/assets/css/bootstrap-responsive.css"
	rel="stylesheet">
<link href="<%= request.getContextPath()%>/assets/css/bootstrap.css"
	rel="stylesheet">
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<%
List<AuctionsHot> auctionHotList = (List<AuctionsHot>) request.getAttribute("auctionHotList");
if(auctionHotList == null)
	auctionHotList = new ArrayList<AuctionsHot>();
%>
<body>
	<br/>
	<table class="table table-striped">
		<thead>
			<tr>
				<th width="30%">访问量</th>
				<th width="55%">商品名</th>
				<th width="15%">去看商品</th>
			</tr>
		</thead>
		<tbody>
		<%
			for(AuctionsHot po: auctionHotList) {
		%>
			<tr>
				<td><%=po.getvNumber()%></td>
				<td><a href="<%=request.getContextPath()%>/showdata.do?method=showHotWordsByItemList&id=<%=po.getAuctionId()%>" target="hotitem_frame_search"><%=po.getTitle()%></a></td>
				<td>
				<a href="http://item.taobao.com/item.htm?id=<%=po.getAuctionId()%>" target="_blank">去商品</a>
				<a href="show.html" target="_blank">去论坛</a>
				</td>
			</tr>		
		<%				
			}
		%>
		</tbody>
	</table>
	<iframe src="index.jsp" width="100%" style="min-height: 500px;"
			frameborder="no" border="0" marginwidth="0" marginheight="0"
					scrolling="yes" allowtransparency="yes" name="hotitem_frame_search"></iframe>	
</body>
</html>