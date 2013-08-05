<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>

<html>
	<head>
		<title>新增告警key</title>

		<%
			//设置base属性，生成绝对路径
			String serverUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort();

			String base = serverUrl + request.getContextPath();

			request.setAttribute("base", base);
		%>

		<script>
 	var base="${base}";
 	var CK_CM_SEPERATOR = '^';

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
		<c:choose>
			<c:when test="${empty keyMode}">
				<c:set var="actionLabel" value="添加" />
			</c:when>
			<c:otherwise>
				<c:set var="actionLabel" value="更新" />
			</c:otherwise>
		</c:choose>
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
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> ->
					<a
						href="<%=request.getContextPath()%>/app/conf/key/show.do?method=showIndex&appId=${appInfo.appId}">
						Key管理 &nbsp;${appInfo.appName }</a> ->
					<a
						href="<%=request.getContextPath()%>/app/conf/key/show.do?method=keyPropsList&keyId=${keyInfo.keyId}&appId=${appInfo.appId}&keyName=${keyInfo.keyName}">${keyInfo.keyName
						} 查看属性</a> -> ${propertyName } ${actionLabel }告警
					<hr>
					<div class="row-fluid">
						<div class="span12">
							<div class="row" style="padding-bottom: 10px">





							</div>



							<div class="row">

								<div class="span8 ">

									<c:choose>
										<c:when test="${empty keyMode}">
											<c:set var="actionUrl"
												value="${base}/app/conf/key/show.do?method=addAlarmKey" />
										</c:when>
										<c:otherwise>
											<c:set var="actionUrl"
												value="${base}/app/conf/key/show.do?method=updateAlarmKey" />
										</c:otherwise>
									</c:choose>
									<form method="post" action="${actionUrl }"
										onsubmit="return checkForm()">

										<%--如果是更新，增加 id hidden --%>
										<c:if test="${! empty keyMode}">
											<input type="hidden" name="id" value="${keyMode.id }" />
										</c:if>

										<table style="table-layout: fixed; width: 100% ；"
											class="table table-striped table-bordered table-condensed">
											<tr>
												<td style="width: 100px; text-align: right;">
													应用名:
												</td>
												<td style="">
													${appInfo.appName }
												</td>
											</tr>
											<tr>
												<td style="text-align: right;">
													key名称:
												</td>
												<td>
													${keyInfo.keyName }
												</td>
											</tr>
											<tr>
												<td style="text-align: right;">
													key级别:
												</td>
												<td>
												<select id="keyLevel" name = "keyLevel" style="width: 100px;">
														<option value="0"
															<c:if test="${ 0==keyMode.keyLevel}">selected="selected"</c:if>>
															p0
														</option>
														<option value="1"
															<c:if test="${ 1==keyMode.keyLevel}">selected="selected"</c:if>>
															p1
														</option>
														<option value="2"
															<c:if test="${ 2==keyMode.keyLevel}">selected="selected"</c:if>>
															p2
														</option>
														<option value="3"
															<c:if test="${ 3==keyMode.keyLevel}">selected="selected"</c:if>>
															p3
														</option>																																										
													</select>													
												</td>
											</tr>

											<tr>
												<td style="text-align: right;">
													属性名称:
												</td>
												<td>
													${propertyName }
												</td>
											</tr>
											<!-- <tr>
												<td style="text-align: right;">
													策略:
												</td>
												<td style="">
													
													<br />
													
												</td>

											</tr> -->
											<tr>
												<td style="width: 100px; text-align: right;">
													配置值:
												</td>
												<td>
												<%--将hostModeConfig拆分为策略、配置值--%>
												
												<c:forTokens items="${keyMode.hostModeConfig}" delims="^" var="token" varStatus="status">
														<c:if test="${status.index==0}">
															<c:set var="hostCheckMode" value="${token}"/>
															
														</c:if>
														<c:if test="${status.index==1}">
															<c:set var="hostModeConfigSuffix" value="${token}"/>
														</c:if>
												</c:forTokens>
												
													<input type="checkbox" value="HOST" name="hostScope" 
													<c:if test="${keyMode.keyScope=='ALL' || keyMode.keyScope=='HOST' }"> checked="checked" </c:if> />&nbsp;配置主机&nbsp;
													
													<select id="hostCheckMode" style="width: 100px;">

														<option value="Threshold"
															<c:if test="${ 'Threshold'==hostCheckMode}">selected="selected"</c:if>>
															阀值
														</option>
														<option value="History"
															<c:if test="${ 'History'==hostCheckMode}">selected="selected"</c:if>>
															历史
														</option>
														<option value="baseline"
															<c:if test="${ 'baseline'==hostCheckMode}">selected="selected"</c:if>>
															基线
														</option>
														<option value="RecentlyRate"
															<c:if test="${ 'RecentlyRate'==hostCheckMode}">selected="selected"</c:if>>
															最近对比
														</option>
													</select>
													<!-- 因为要在输入的modeConfig加策略，所以这个只是suffix -->
													<input type="text" name="hostModeConfigSuffix"
														value="${hostModeConfigSuffix }" />
													<input type="hidden" name="hostModeConfig" />
													
													
													<br />
												<%--将appModeConfig拆分为策略、配置值--%>
												
												<c:forTokens items="${keyMode.appModeConfig}" delims="^" var="token" varStatus="status">
														<c:if test="${status.index==0}">
															<c:set var="appCheckMode" value="${token}"/>
															
														</c:if>
														<c:if test="${status.index==1}">
															<c:set var="appModeConfigSuffix" value="${token}"/>
														</c:if>
												</c:forTokens>
													<input type="checkbox" value="APP" name="appScope" <c:if test="${keyMode.keyScope=='ALL' || keyMode.keyScope=='APP' }"> checked="checked" </c:if> />&nbsp;配置应用&nbsp;
													<select  id="appCheckMode" style="width: 100px;">

														<option value="Threshold"
															<c:if test="${ 'Threshold'==appCheckMode}">selected="selected"</c:if>>
															阀值
														</option>
														<!-- 暂时去掉历史对比
														<option value="History"
															<c:if test="${ 'History'==appCheckMode}">selected="selected"</c:if>>
															历史
														</option>
														 -->
														<option value="baseline"
															<c:if test="${ 'baseline'==appCheckMode}">selected="selected"</c:if>>
															基线
														</option>
														<option value="RecentlyRate"
															<c:if test="${ 'RecentlyRate'==appCheckMode}">selected="selected"</c:if>>
															最近对比
														</option>
													</select>
													<input type="text" name="appModeConfigSuffix"
														value="${appModeConfigSuffix }" />
														<input type="hidden" name="appModeConfig" />
														<input  type="hidden" name="keyScope" />
													<br />
													<font color="red">阀值:表示以上下限为报警条件。最近对比：表示以当前值与前面2个值的平均的百分比。基线对比：当前值与基线数据做比较判断比例。</font>
													<br />
													<br />
													<font color="red">
														阀值结构如:-1#2500$00:10#23:59;-1#2500$10:10#23:59; -1#2500
														表示下限和上限，00:10#10:59 表示开始时间和结束时间 -1表示无上限或下限。<br /> 设置百分比波动
														是 如:-1#150$00:10#10:59 表示 波动超过150%是告警 <br /> 基线对比 是 如：80$120$00:00#23:59;
														表示当前与基线比值低于80%或者高于120%需要告警。如果-1$120$00:00#23:59; 表示下限不做判断</font>
												</td>
											</tr>

											<tr>
												<td style="text-align: right;">
													告警描述:
												</td>
												<td>
													<input type="text" value="${keyMode.keyAlias }" size="100"
														name="keyAlias" />
												</td>
											</tr>
										</table>



										<div style="text-align: center">
											<input class="btn btn-primary" type="submit"
												value="${actionLabel }" id="submitBtn" />

										</div>
										<input type="hidden" name="appName"
											value="${appInfo.appName }" />
										<input type="hidden" name="keyName"
											value="${keyInfo.keyName }" />
										<input type="hidden" name="propertyName"
											value="${propertyName }" />
										<input type="hidden" name="appId" value="${appInfo.appId }" />

										<!-- for update -->
										<c:if test="${!empty keyMode}">
											<input type="hidden" name="baseline"
												value="${keyMode.baseline}" />

											<input type="hidden" name="alarm" value="${keyMode.alarm}" />

									
										</c:if>
										<!-- 从告警列表页点击修改后，返回用的 -->
										<input type="hidden" name="from" value="${param.from}" />
									</form>
								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">

	function checkForm(){  
	//alert(0.1);
		var hmc_s = $("input[name='hostModeConfigSuffix']");
		var amc_s = $("input[name='appModeConfigSuffix']");
	//	alert(0.2);
		var hmc_s_v = $.trim(hmc_s.val());
		var amc_s_v = $.trim(amc_s.val());
	//	alert(0.3);
		var hmc = $("input[name='hostModeConfig']");
		var amc = $("input[name='appModeConfig']");
	//	alert(0.4);
		
		
	//	alert(1);
		//处理scope的值
		var hs = $("input[name='hostScope']");
		var as = $("input[name='appScope']");
	
		
	//	alert(2);
		var sv='';
		if(hs.attr('checked') && as.attr('checked')){
			sv = 'ALL';
		}else if(hs.attr('checked')){
			sv = 'HOST';
		}else if(as.attr('checked')){
			sv = 'APP';
		}

		if(sv=='' ){
			alert("必须选择HOST或APP其中一种配置!");
			hs.focus();
			return  false;
		}
		
		if (hs.attr('checked') && hmc_s_v =="")  {  
			alert("你把HOST配置值给忘填了!");
			hmc_s.focus();
			return  false;
		}
		if ( as.attr('checked') && amc_s_v=="")  {  
			alert("你把APP配置值给忘填了!");
			amc_s.focus();
			return  false;
		}
	///alert(3);
	//	alert($("input[name='keyScope']").val());
		$("input[name='keyScope']").val(sv);
	///	alert($("input[name='keyScope']").val());
		//获取host策略值
		var hCheckMode = currSelectItem(document.getElementById("hostCheckMode")); 
		//获取app策略值
		var aCheckMode = currSelectItem(document.getElementById("appCheckMode"));
		
		//alert(4);
		//处理策略值+ 配置值
		if(sv=='ALL' || sv=='HOST'){
			//拼装host策略、配置值
			hmc.val(hCheckMode +CK_CM_SEPERATOR +  hmc_s_v);
		//	console.log('hCheckMode: '+hCheckMode+'\t hmc_s_v: '+hmc_s_v);
		}
		if(sv=='ALL' || sv=='APP'){
			//拼装app策略、配置值
			amc.val(aCheckMode +CK_CM_SEPERATOR +  amc_s_v);
		//	console.log('aCheckMode: '+aCheckMode+'\t amc_s_v: '+amc_s_v);
		}		
	//	alert(5);
	
		
	}
	//sel jQuery select
	function currSelectItem(sel){
		var options = sel.options;
        var currIndex = sel.selectedIndex;
        var currOpt = options[currIndex];
        var currV = currOpt.value;
        return currV;
	}
	</script>
</html>
