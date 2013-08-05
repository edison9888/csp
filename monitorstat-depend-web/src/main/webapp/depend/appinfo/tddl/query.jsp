<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>

<html>
	<head>
		<title>TDDL查询</title>

		<%
			//设置base属性，生成绝对路径
			String serverUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort();

			String base = serverUrl + request.getContextPath();

			request.setAttribute("base", base);
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
</style>
	</head>

	<body>
		<%@ include file="/header.jsp"%>
		<div class="container-fluid">
			<div class="row-fluid" style="text-align: center">
			</div>
			<div class="row-fluid">

				<div class="span12">
					<div style="text-align: center;">
						<h3>
							TDDL查询
						</h3>
						<div style="float: right; padding-right: 20px; font-size: 14px">
							<a href="<%=request.getContextPath()%>/tddl/show.do?method=list">返回Tddl首页</a>
						</div>
					</div>
					<hr>

					<div class="row-fluid">
						<div class="span12">
							<div class="row" style="padding-bottom: 10px">
								<div class="span8 offset2">
									<form action="${base}/tddl/show.do" id="form1" method="get">
										<table
											class="table table-striped table-condensed table-bordered">
											<tr>
												<td>
													<input type="hidden" value="${query1Value }"
														name="query1Value" />
													<input type="radio" value="1" name="query1Type"
														<c:if test="${query1Type=='1' }"> checked </c:if> />
													按天查
													<input style="width: 80px" type="text" id="day" value=""
														name="day"
														onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" /> 
												</td>
												<td colspan="2">
													<input type="radio" value="2" name="query1Type"
														<c:if test="${query1Type=='2' }"> checked </c:if> />
													按小时查&nbsp;
													<span style="color: red">(查询当天)</span>
													<select style="width: 100px" name="hour">
														<%
															Date now = new Date();
															int hours = now.getHours() - 1;
															request.setAttribute("hours", hours);
															for (int i = 0; i <= hours; i++) {
																request.setAttribute("i", i);
														%>
														<option value="<%=i%>"
															<c:if test="${i==hours}">selected="selected"</c:if>><%=i%>点~<%=i + 1%>点
														</option>
														<%
															}
														%>

													</select>
													<span style="color: red">只显示到前一小时</span>
												</td>
											</tr>
											<tr>
												<td colspan="3">
													<input type="radio" value="1" name="query2Type"
														<c:if test="${query2Type==1 }" >checked </c:if> />
													按应用名查
													<input type="radio" value="2" name="query2Type"
														<c:if test="${query2Type==2}" >checked </c:if> />
													按数据库名查
												</td>
											</tr>
											<tr>
												<td id="query4" colspan="3">
												</td>
											</tr>

											<tr>

												<td colspan="3">
													<input type="radio" value="1" name="query3Type"
														<c:if test="${query3Type==1 }" >checked </c:if> />
													按
													<span id="query3Type_1">数据库名</span>展示
													<input type="radio" value="2" name="query3Type"
														<c:if test="${query3Type==2 }" >checked </c:if> />
													按SQL展示
												</td>

											</tr>

										</table>
										<div style="text-align: center">
											<input type="button" value="查询" class="btn btn-primary"
												id="button1" />
										</div>

										<input type="hidden" name="method" value="query" />
										<input type="hidden" name="pageNo" id="hidden1"
											value="${pagination.pageNo}" />

										<input type="hidden" name="pageSize" id="hidden2"
											value="${pagination.pageSize}" />
												排序：<select id="sortType" name="sortType">
														<option value="execNum" <c:if test="${sortType=='execNum'}">selected="selected"</c:if>>执行量排序</option>
														<option value="elaTime" <c:if test="${sortType=='elaTime'}">selected="selected"</c:if>>平均时间排序</option>
													</select>										
									</form>


								</div>
								<!-- end span8 -->
							</div>
							<!-- end row -->
							<div class="row">
								<div class="span8 offset2">
									<table
										class="table table-striped table-condensed table-bordered"
										id="table1" style="table-layout: fixed；">
										<thead>
											<tr>
												<th style="text-align: center;" width="15%">
													名称
												</th>
												<!-- class用于指定要显示的一些列，并按序显示 ；（so即show order的缩写，加前缀是为了和普通的class区分开来，这个是有逻辑意义的）-->
												<th style="width: 14%; text-align: center;">
													执行量
												</th>

												<th style="width: 14%; text-align: center;">
													平均时间
												</th>

												<th style="width: 29%; text-align: center;">
													最大时间
												</th>
												<th style="width: 29%; text-align: center;">
													最小时间
												</th>

											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pagination.list}" var="item">
												<tr>
													<td style="text-align: center;" title="${item.name}">
														<c:if test="${query2Type==1}">
															<c:set var="passAppName" value="${name}" />
															<c:set var="passDBName" value="${item.name}" />
														</c:if>
														<c:if test="${query2Type!=1}">
															<c:set var="passAppName" value="${item.name}" />
															<c:set var="passDBName" value="${name}" />
														</c:if>

														<c:if test="${query3Type==1}">
														<a target="_blank"
															href="<%=request.getContextPath()%>/tddl/show.do?method=itemDetail&query1Type=${query1Type}&query1Value=${query1Value}&appName=${passAppName}&dbName=${passDBName}">${item.name}</a>
														
														</c:if>
													<c:if test="${query3Type==2}">${item.name}
													</c:if>
													</td>
													<td style="text-align: center;">
														${ item.executeSumStr}
													</td>

													<td style="text-align: center;">
														${ item.timeAverage}
													</td>


													<td style="text-align: center;">

														<c:if test="${query3Type==1}">
														${ item.maxTime} 
														</c:if>
														<c:if test="${query3Type==2}">

															<c:forTokens items="${item.maxTime}" delims="#"
																var="token" varStatus="status">
																<c:if test="${status.index==0}">
																	<c:set var="maxTimeValue" value="${token}" />

																</c:if>
																<c:if test="${status.index==1}">
																	<c:set var="maxTimePoint" value="${token}" />
																</c:if>
															</c:forTokens>
															${maxTimeValue }(时间点:${maxTimePoint })
														</c:if>
													</td>

													<td style="text-align: center;">
														<c:if test="${query3Type==1}">
														${ item.minTime} 
														</c:if>
														<c:if test="${query3Type==2}">
															<c:forTokens items="${item.minTime}" delims="#"
																var="token" varStatus="status">
																<c:if test="${status.index==0}">
																	<c:set var="minTimeValue" value="${token}" />
																</c:if>
																<c:if test="${status.index==1}">
																	<c:set var="minTimePoint" value="${token}" />
																</c:if>
															</c:forTokens>
															${minTimeValue }(时间点:${minTimePoint })
														</c:if>
													</td>

												</tr>


											</c:forEach>
											<tr id="groupResult">
												<td style="text-align: center;">
													单页汇总结果:
												</td>
												<td style="text-align: center;">
													${ groupResult.executeSumStr}
												</td>

												<td style="text-align: center;">


													${ groupResult.timeAverage}
												</td>


												<td style="text-align: center;">


													${ groupResult.maxTime}
												</td>

												<td style="text-align: center;">


													${ groupResult.minTime}
												</td>
											</tr>
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
		</div>
		<div align="center">
		如有疑问请联系:<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=中亭&site=cntaobao&s=2&charset=gbk" >
    <img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=中亭&site=cntaobao&s=2&charset=gbk" />中亭</a>				
		</div>				
		<br />
		<br />
		<br />
		<!-- query2Type=='1' -->
		<div id="copy_source_1" style="z-index: 1003; display: none">
			应用名:&nbsp;&nbsp;
			<input name="name" value="${name}" />
		</div>
		<!-- query2Type=='2' -->
		<div id="copy_source_2" style="z-index: 1003; display: none">
			数据库名:&nbsp;&nbsp;
			<input name="name" value="${name}" />
			&nbsp;&nbsp; IP:
			<input name="ip" value="${ip}" />
			&nbsp;&nbsp; PORT:
			<input name="port" value="${port}" />
		</div>
		<script type="text/javascript">
		function gotoPage(page) {
			$("#hidden1").attr("value", page);
			$("#form1").submit();		
		}		
	$(function() {
		//因为initElementStatus()用到query2AddChangeEvent()，所以query2AddChangeEvent()放前面
		query2AddChangeEvent();
		initElementStatus();
		form1Submit();

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

		$("#button1").click(function() {
			$("#hidden1").attr("value", 1);
			$("#form1").submit();
			return false;

		});
		
		$("#sortType").change(function() {
			$("#form1").submit();
			return false;
		});

	});
	function form1Submit(){
		$("#form1").get(0).onsubmit = function() {
			var q1v = $("input[name='query1Value']");
			var q1t_v =getRadioV('query1Type'); 
			var d = $("input[name='day']");
			var h = $("select[name='hour']");
			var kn = $("input[name='name']");
			var ip = $("input[name='ip']");
			var pt = $("input[name='port']");
			/**表单验证 */
			if (q1t_v == '1' && $.trim(d.val()) == '') {
				d.focus();
				alert("请输入日期!");
				return false;
			}
			
			if ( $.trim(kn.val()) == '') {
				kn.focus();
				var query2Type444 = getRadioV('query2Type');
				if(query2Type444 == '1')
					alert("请输入应用名!"); 
				else 
					alert("请输入数据库名!");	
				return false;
			}
			//alert(1);
			//	alert("currItem(h).value: "+currItem(h).value+"\t q1t_v: "+q1t_v );
			/**提交前，赋值给hidden*/
			if (q1t_v == '1') {
				q1v.val(d.val());
			} else {
				q1v.val(currOption(h.get(0)).value);
				
			}

			/**提交前，进行trim*/
			kn.val($.trim(kn.val()));
			ip.val($.trim(ip.val()));
			pt.val($.trim(pt.val()));
		//	return false;
			
		}
	}
	/**query2条件变化时，触发条件3（结果按xx展示）、条件4（关键字）变化*/
	function query2AddChangeEvent(){
		$("input[name='query2Type']").click(function(){
			//获取当前元素的值
			var v = this.value;
			var q4 = $("#query4");
			var q3t1 = $("#query3Type_1");
			var cs1 = $("#copy_source_1");
			var cs2 = $("#copy_source_2");
			if(v=='1'){
				//条件4的td的innerHTML为copy_source_1的innerHTML
				//"#query3Type_1"的innerHTML为“数据库名”
				q4.html(cs1.html());	
				q3t1.html("数据库名");
				
			}else{
				//条件4的td的innerHTML为copy_source_2的innerHTML
				//"#query3Type_1"的innerHTML为“应用名”
				q4.html(cs2.html());	
				q3t1.html("应用名");
			}
		});
	}
	
	
	function initElementStatus(){
		var q1v = $("input[name='query1Value']");
		var q1t_v =getRadioV('query1Type'); 
		//alert("q1t_v: "+q1t_v);
		var d = $("input[name='day']");
		var h = $("select[name='hour']");

		//根据query1Type、query1Value来初始化条件1相关元素
		if (q1t_v == '1'){ 
			d.val(q1v.val());
		}else{
			//选中指定的小时
			chooseOption(h.get(0),q1v.val());
		}
		
		//根据query2Type的值，触发对应query2Type元素click事件，来初始化query3、query4
		var q2t = currRadio("query2Type");
		q2t.click();
	}
	
	/**sel pure js object*/
	function currOption( sel){
		var options = sel.options;
        var currIndex =  sel.selectedIndex;
        var currOpt = options[currIndex];
        return currOpt;
	}
	
	function chooseOption(sel, v){
		//alert
     	 var options = sel.options;
      	 for (var i = 0; i < options.length; i++)
          	if (options[i].value == v){
            	options[i].selected = true;
         }
	}
	/**eleName元素name值*/
	function getRadioV(eleName){
		return currRadio(eleName).value;
	}
	
	function currRadio(eleName){
			var rs = document.getElementsByName(eleName);
         	for(var i=0;i<rs.length;i++){
				var item = rs[i];
				if(item.checked)
					return item;
			}
	}

	
</script>
	</body>
</html>
