<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
  


<!DOCTYPE html>

<html>
	<head>
		<title>告警记录查询</title>

		<%
			//设置base属性，生成绝对路径
			String serverUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort();

			String base = serverUrl + request.getContextPath();

			request.setAttribute("base", base);
		%>

		<script>
 	var base="${base}";

 </script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="${base }/statics/css/bootstrap.css" rel="stylesheet">

		<script src="${base }/statics/js/jquery/jquery.min.js"></script>

		<script src="${base }/statics/js/bootstrap.js"></script>
		<script src="${base}/statics/js/date.js"></script>

		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/My97DatePicker/WdatePicker.js"></script>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<style type="text/css">
body {
	padding-top: 60px;
}

#table1 td {
	word-wrap: break-word
}
</style>
	</head>

	<body>
		<%@ include file="/header.jsp"%>
		<div class="container-fluid">
			<div class="row-fluid" style="text-align: center">
			<div class="span12" id="page_nav"></div>
	<script>
			$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
	</script>	
			</div>
			<div class="row-fluid">
				<div class="span2">
					<%@include file="/leftmenu.jsp"%>
				</div>
				<div class="span12">
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -> 告警记录查询
					&nbsp;${appInfo.appName }
					<hr>
					<div class="row-fluid">
						<div class="span12">
							<div class="row" style="padding-bottom: 10px">

								<form action="${base}/app/alarm/show.do" id="form1" method="POST">
									<input type="hidden" name="method" value="showIndex" />
									
									<input type="hidden" name="appId" id="hidden4"
										value="${param.appId}" />
									<span style="margin-left:100px">key名称:</span><input type="text" name="keyNamePart"
										value="${param.keyNamePart }" />
									开始时间:<input style="width: 160px" type="text"  
														name="from" value="${param.from }"
														onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />	
									结束时间:<input style="width: 160px" type="text" value="${param.to }" 
														name="to" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								
								
									<input class="btn  primary" type="button" value="查询"
										id="button1" />
							
									<input type="hidden" name="pageNo" id="hidden1"
										value="1" />
									<input type="hidden" name="pageSize" id="hidden2"
										value="30" />

									

								</form>



							</div>
							<div class="row">

								<div class="span12">


									<table
										class="table table-striped table-condensed table-bordered"
										id="table1" style="table-layout: fixed" >
										<thead>
											<tr>
												<!-- class用于指定要显示的一些列，并按序显示 ；（so即show order的缩写，加前缀是为了和普通的class区分开来，这个是有逻辑意义的）-->
												<th  style="text-align: center; " >
													Key名称
												</th>
											<th  style="width: 15%;text-align: center;width:100px;">
													属性名称
												</th>
												<th  style="width: 6%;text-align: center;width:100px;">
													告警模型
												</th>
												<th style="width: 6%;text-align: center;width:50px;">
													告警值
												</th>
												<th style="width: 6%;text-align: center;width:150px;">
													告警时间
												</th>
												<th style="width: 6%;text-align: center;width:200px">
													告警原因
												</th>
													
											</tr>
										</thead>
										<tbody>

											<c:forEach items="${pagination.list}" var="item">
												<tr>
													<td style="text-align: center;" >

														${ item.key_name}

													</td>
													

												<td style="text-align: center;">
														${ item.property_name}
													</td>

													<td style="text-align: center;">


														${ item.mode_name}
													</td>
													
												
													<td style="text-align: center;">
														${ item.alarm_value}
													</td>
															<td style="text-align: center;">
														<fmt:formatDate value="${ item.alarm_time}" pattern="yyyy-MM-dd HH:mm:ss"/>  
													</td>
															<td style="text-align: center;">
														${ item.alarm_cause}
													</td>
													
												</tr>


											</c:forEach>
									</table>

									<c:if test="${pagination.pageNo > 1}">
										<a href="${pagination.pageNo-1 }" id="link1">上一页</a>
									</c:if>
									当前页：${pagination.pageNo}
									<c:if test="${pagination.pageNo < pagination.totalPage}">
										<a href="${pagination.pageNo+1 }" id="link2">下一页</a>
									</c:if>

									共${pagination.totalPage}页 跳转：
									<select name="id" id="select3" style="width: 50px">
										<c:forEach begin="1" end="${pagination.totalPage}"
											var="number">
											<option value="${number}"
												<c:if test="${number==pagination.pageNo }">selected="selected"</c:if>>
												${number }
											</option>
										</c:forEach>
									</select>

								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
 //alert("${appInfo.appId}");
	//var nw = new NavigateWidget({appId:"${appInfo.appId}"});
	
	
	$(function() {

			
			// 点击翻页链接，提交表单
			$("#link1").click(function() {

						$("#hidden1").attr("value",
								$("#link1").attr("href"));
						$("#form1").submit();
						return false;

					});
			$("#link2").click(function() {
						$("#hidden1").attr("value",
								$("#link2").attr("href"));
						$("#form1").submit();
						return false;

					});

			$("#select3").change(function() {
						$("#hidden1").attr("value",
								$("#select3").attr("value"));
						$("#form1").submit();
						return false;
					});
					
			$("#button1").click(function() {
						$("#hidden1").attr("value",1);
						var f = $("input[name='from']");
						var t = $("input[name='to']");
						/**表单验证 */
						if ( $.trim(f.val()) == '') {
							//f.focus();
							alert("请输入开始日期!");
							return;
						}
						if ( $.trim(t.val()) == '') {
							//t.focus();
							alert("请输入结束日期!");
							return;
						}
						$("#form1").submit();

					});

		});
	</script>
</html>
