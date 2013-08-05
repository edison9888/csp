<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.util.page.Pagination"%>
<%@page import="com.taobao.csp.depend.po.tddl.ConsumeTDDLDetail"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.util.SQLPreParser"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>


<html>
	<head>
		<title>TDDL查询</title>

		<%
		  //设置base属性，生成绝对路径
		  String serverUrl = request.getScheme() + "://" + request.getServerName()
		      + ":" + request.getServerPort();

		  String base = serverUrl + request.getContextPath();

		  request.setAttribute("base", base);

		  String totalExecNum = (String) request.getAttribute("totalExecNum");
		  String totalElaTime = (String) request.getAttribute("totalElaTime");
		  Pagination pageObj = (Pagination) request.getAttribute("pagination");
		  List<ConsumeTDDLDetail> list = (List<ConsumeTDDLDetail>) pageObj
		      .getList();
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
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
		<script
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"
			type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/statics/js/bootstrap.js"
			type="text/javascript"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>	
<style type="text/css">
body {
	padding-top: 45px;
}

input,select {
	padding: 0px;
	margin: 0px;
}

select {
	height: 20px;
}

#groupResult {
	font-weight: bolder;
}

#form1 {
	
}

table {
	table-layout: fixed
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
	.fancybox-custom .fancybox-skin {
		box-shadow: 0 0 50px #222;
	}
</style>
</head>

	<body>
		<%@ include file="/header.jsp"%>
		<div class="container">
			<div class="row-fluid">
				<div class="span12" style="text-align: center;">
				<h3>TDDL查询</h3>
				<div style="float: right; padding-right: 20px; font-size: 14px">
					<a href="<%=request.getContextPath()%>/tddl/show.do?method=list">返回Tddl首页</a>
				</div>
				<hr>
					<div class="row" >
					应用名:	${appName }&nbsp;&nbsp;&nbsp;&nbsp; DB名: ${dbName }&nbsp;&nbsp;&nbsp;&nbsp;时间：${query1Value} &nbsp;&nbsp; <a href="<%=request.getContextPath()%>/tddl/show.do?method=tableMerge&appName=${appName }&dbName=${dbName }&selectDate=${query1Value}">导出excel:sql分表合并 </a>&nbsp;&nbsp; <a href="<%=request.getContextPath()%>/tddl/show.do?method=readWriteRate&appName=${appName }&dbName=${dbName }&selectDate=${query1Value}">导出excel:单表读写比例 </a>
					</div>
					<div class="row-fluid">
						<div class="span12" style="text-align: center">
							<div class="row">
								<div class="span12" align="center">
										<form id="form1"
											action="<%=request.getContextPath()%>/tddl/show.do?method=itemDetail" class="well form-search">
											<input type="hidden" name="method" value="itemDetail" />
											<input type="hidden" name="query1Type" value="${query1Type }" />
											<input type="hidden" name="query1Value" value="${query1Value}" />
											<input type="hidden" name="dbName" value="${dbName}" />
											<input type="hidden" name="appName" value="${appName}" />
											<input type="hidden" name="pageNo" id="hidden1"
												value="${pagination.pageNo}" />
											<input type="hidden" name="pageSize" id="hidden2"
												value="${pagination.pageSize}" />
												SQL:
												<input type="text" placeholder="查询的SQL,支持模糊查询" name="searchsql" value="${searchsql}" id="searchsql"/>
													排序:
												<select id="sortType" name="sortType">
														<option value="execNum" <c:if test="${sortType=='execNum'}">selected="selected"</c:if>>执行量排序</option>
														<option value="elaTime" <c:if test="${sortType=='elaTime'}">selected="selected"</c:if>>平均时间排序</option>
														<option value="totalelaTime" <c:if test="${sortType=='totalelaTime'}">selected="selected"</c:if>>总时间排序</option>
													</select>
												<input type="submit" value="查询" class="btn btn-primary"/>
												<input type="button" value="清空sql" class="btn btn-primary" id="clearSqlBtn"/>
												<input name="totalExecNum" value="${totalExecNum}" type="hidden"/>
												<input name="totalElaTime" value="${totalElaTime}"  type="hidden"/>													
										</form>
										<div>
										<strong>
												总执行次数:<%=Utlitites.fromatLong(totalExecNum)%> &nbsp;&nbsp;&nbsp;
												总执行时间(ms):<%=Utlitites.fromatLong(totalElaTime)%>
												</strong>
										</div>
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
												<th style="text-align: center;" width="65%" title="显示Full SQL">
													SQL(鼠标移到超链接上默认显示sql，单击超链接弹出显示索引及SQL<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" />)
												</th>
												<!-- class用于指定要显示的一些列，并按序显示 ；（so即show order的缩写，加前缀是为了和普通的class区分开来，这个是有逻辑意义的）-->
												<th style="text-align: center;" width="15%">
													执行量
												</th>

												<th style="text-align: center;" width="10%">
													平均时间
												</th>
												<th style="text-align: center;" width="15%">
													总时间(比例)
												</th>
												<th style="text-align: center;" width="10%">
													查看索引
												</th>
											</tr>
										</thead>
										<tbody>
												<%
												int i=1;
												  for (ConsumeTDDLDetail item : list) {
												%>
												<tr>
													<td style="text-align:center;">
													<%=i++%>
													</td>
													<td style="text-align:left;" title="<%=item.getSqlText()%>">
														<a href="<%=request.getContextPath()%>/tddl/show.do?method=seeMoreDetailFromSql&sql=<%=item.getSqlText()%>&appName=${appName}&dbName=${dbName}" class="dependself"><%=item.getSqlText()%></a>
													</td>
													<td style="text-align: center;">
													<%=Utlitites.fromatLong(item.getExecuteSum() + "")%>
													(<%=Arith.mul(Arith.div(item.getExecuteSum(),Double.parseDouble(totalExecNum), 4), 100)%>%)
													</td>
													<td style="text-align: center;">
													<%=item.getTimeAverage()%>
													</td>
													<td style="text-align: center;">
													<%=Utlitites.fromatLong(item.getTimeAverage()*item.getExecuteSum() + "")%>
													(<%=Arith.mul(Arith.div(item.getTimeAverage()*item.getExecuteSum(),Double.parseDouble(totalElaTime), 4), 100)%>%)
													</td>
													<td style="text-align: center;">
														<a href="<%=request.getContextPath()%>/tddl/show.do?method=showIndex&tablename=<%=SQLPreParser.findTableName(item.getSqlText())%>" target="_blank">查看表信息</a>
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
									当前页：${pagination.pageNo}
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
		$("#sortType").change(function() {
			$("#form1").submit();
			return false;
		});
		
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
			$("#searchsql").attr("value", "");
			return true;
		});
		
		$('.fancybox').fancybox();
		
		$("a.dependself").fancybox({
			width			: '75%',
			height			: '40%',
	        autoScale     	: true,
	        transitionIn	: 'none',
			transitionOut	: 'none',
			type			: 'iframe'
		});	
	});
	</script>

	</body>
</html>
