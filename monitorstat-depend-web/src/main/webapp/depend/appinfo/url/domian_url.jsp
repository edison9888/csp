<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GB18030"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.util.page.Pagination"%>
<%@page import="com.taobao.csp.depend.po.url.UrlUv"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.util.SQLPreParser"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>


<html>
	<head>
		<title>域名、网址PV、UV查询</title>
		<%
		  //设置base属性，生成绝对路径
		  String serverUrl = request.getScheme() + "://" + request.getServerName()
		      + ":" + request.getServerPort();

		  String base = serverUrl + request.getContextPath();

		  request.setAttribute("base", base);

		  String totalExecNum = (String) request.getAttribute("totalExecNum");
		  String totalElaTime = (String) request.getAttribute("totalElaTime");
		  Pagination pageObj = (Pagination) request.getAttribute("pagination");
		  List<UrlUv> list = (List<UrlUv>) pageObj.getList();
		%>

		<script type="text/javascript">
	var base = "${base}";
</script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
			rel="stylesheet">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
		<script language="JavaScript"
			src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<style type="text/css">
body {
	padding-top: 45px;
}
td {
	text-overflow: ellipsis;
	overflow: hidden;
	white-space: nowrap;
}
span {
	display: block;
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis; /*just for IE */
}
</style>
</head>
	<body>
	<%@ include file="../../header.jsp"%>
			<div class="row-fluid">
				<div class="span12" style="text-align: center;">
				<h3>PV、UV查询</h3>
					<div class="row-fluid">
						<div class="span12" style="text-align: center">
							<div class="row">
								<div class="span12" align="center">
										<form id="form1"
											action="<%=request.getContextPath()%>/show/urlorigin.do?method=queryDomainAndUrl" class="well form-search">
											<input type="hidden" name="method" value="queryDomainAndUrl" />
											<input type="hidden" name="pageNo" id="hidden1"
												value="${pagination.pageNo}" />
											<input type="hidden" name="pageSize" id="hidden2"
												value="${pagination.pageSize}" />
											<input type="hidden" name="total" id="total"
												value="${pagination.totalCount}" />
												<input type="text" placeholder="URL(http://www.taobao.com/)或域名(www.taobao.com),支持模糊查询" name="queryValue" value="${queryValue}" id="queryValue" class="span5"/>
												日期: <input type="text" id="startDate" value="${startDate}" name="startDate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" class="span2"/>
												<select id="type" name="type" class="span1">
														<option value="url" <c:if test="${type=='url'}">selected="selected"</c:if>>链接</option>
														<option value="domain" <c:if test="${type=='domain'}">selected="selected"</c:if>>域名</option>
													</select>
												<input type="submit" value="查询" class="btn btn-success"/>
												<input type="button" value="清空sql" class="btn btn-success" id="clearSqlBtn"/>
												<span>数据来自云梯分析（有3天延迟，默认查询3天前数据）</span>
										</form>
										<div>
									</div>
								</div>
							</div>
							<div class="row" style="padding-bottom: 10px">
								<!-- end span8 -->
							</div>
							<!-- end row -->
							<div class="row">
								<div class="span12">
									<table width="100%" 
										class="table table-striped table-condensed table-bordered">
										<thead>
											<tr>
												<th style="text-align: center;" width="5%">
												</th>
												<th style="text-align: center;" width="45%">
													名称												
												</th>
												<th style="text-align: center;" width="25%">
													uv(汇总)
												</th>

												<th style="text-align: center;" width="25%">
													ipv(汇总)
												</th>
											</tr>
										</thead>
										<tbody>
												<%
												int i=1;
												  for (UrlUv item : list) {
												%>
												<tr>
													<td style="text-align:center;">
													<%=i++%>
													</td>
													<td style="text-align:left;" title="<%=item.getUrl()%>">
														<%=item.getUrl()%>
													</td>
													<td style="text-align: center;">
													<%=Utlitites.fromatLong(item.getUv() + "")%>
													</td>
													<td style="text-align: center;">
													<%=Utlitites.fromatLong(item.getIpv() + "")%>
													</td>
												</tr>													  
													  <%
  													    }
 													  %>
										</tbody>
									</table>
									当前页：${pagination.pageNo}&nbsp;
									<a onclick="gotoPage(1)" href="#">1</a>
									<a onclick="gotoPage(2)" href="#">2</a>
									<a onclick="gotoPage(3)" href="#">3</a>
									<a onclick="gotoPage(4)" href="#">4</a>
									<a onclick="gotoPage(5)" href="#">5</a>
									<a onclick="gotoPage(6)" href="#">6</a>
									<a onclick="gotoPage(7)" href="#">7</a>
									<a onclick="gotoPage(8)" href="#">8</a>
									<a onclick="gotoPage(9)" href="#">9</a>
									<a onclick="gotoPage(10)" href="#">10</a>
									<c:if test="${pagination.pageNo > 1}">
										<a href="${pagination.pageNo-1 }" id="link1">上一页</a>
									</c:if>
									<c:if test="${pagination.pageNo < pagination.totalPage}">
										<a href="${pagination.pageNo+1 }" id="link2">下一页</a>
									</c:if>
								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		<div align="center">
		如有疑问请联系:<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=中亭&site=cntaobao&s=2&charset=gbk" >
    <img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=中亭&site=cntaobao&s=2&charset=gbk" />中亭</a>				
		</div>			
		<br />
		<br />
		<br />
		<script>
	function gotoPage(page) {
		$("#hidden1").attr("value", page);
		$("#form1").submit();		
	}
	$(function() {
		
		// 点击翻页链接，提交表单
		$("#link1").click(function() {
			$("#hidden1").attr("value", $("#link1").attr("href"));
			$("#form1").submit();
			return false;

		});
		$("#link2").click(function() {
			$("#hidden1").attr("value", $("#link2").attr("href"));
			$("#form1").submit();
			return false;

		});
		
		$("#select3").change(function() {
			$("#hidden1").attr("value", $("#select3").attr("value"));
			$("#form1").submit();
			return false;
		});
		
		// 点击翻页链接，提交表单
		$("#clearSqlBtn").click(function() {
			$("#queryValue").attr("value", "");
			return true;
		});
	});
	</script>

	</body>
</html>
