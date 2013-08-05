<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>

<!DOCTYPE html>

<html>
	<head>
		<title>添加实时配置</title>
		<%
			//设置base属性，生成绝对路径
			String serverUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort();

			String base = serverUrl + request.getContextPath();

			request.setAttribute("base", base);
		%>
		<script>
			var base ='${base}';
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

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
		<!-- 		<script src="${base}/time/conf/key_list.js"></script> -->
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>

		<style type="text/css">
body {
	padding-top: 60px;
}
</style>
<script>
	$(function(){
		var nw = new NavigateWidget2({appId:${appInfo.appId}});
	});
</script>
	</head>

	<body>
		<%@ include file="/header.jsp"%>

		<div class="container-fluid">
			<div class="row-fluid" style="text-align: center">
				<div class="span12">
					<select id="companySelect"></select>
						<select id="groupSelect"></select>
						<select id="appSelect"></select>
						<!-- <input type="text" class="input-small" placeholder="查询应用名称"  id="app_search"> -->
						
						&nbsp;&nbsp;&nbsp;
						<input class="btn btn-success" type="button" onclick="javscript:location.href='<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId='+getSelectAppId()" value="查看应用">
						
						<input type="text"  id="appname_text" style="width:150px;margin-left:100px;"/>
						<input class="btn btn-success" type="button"  onclick="javscript:location.href='<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId='+getInputAppId()" value="应用直达">
			
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<%@include file="/leftmenu.jsp"%>
				</div>

				<div class="span12">
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a>->
					${appInfo.appName }添加实时配置

					<hr style="margin-bottom: 0px">
					
					<<<a href="<%=request.getContextPath() %>/app_info.do?method=collect&appId=${appInfo.appId}" style="" onclick="return confirm('确定要离开编辑页面吗，你当前编辑的内容不会被保存！')">返回列表页</a><<
					<div class="row-fluid">
						<div class="span12">
							<div class="row">
								<div class="span10 ">
								
									<form
										action="<%=request.getContextPath()%>/app_info.do?method=gotoCollectAdd&appId=${appInfo.appId}"
										method="post">
										<div class=""
											style="font-size: 1.2em; margin: .6em 0;">
										<select name="tmpId">
												<%
													List<TimeConfTmpPo> timeConfTmpList = (List<TimeConfTmpPo>) request
															.getAttribute("timeConfTmpList");
													TimeConfTmpPo tmp = (TimeConfTmpPo) request.getAttribute("tmp");
													String tmpId =(String) request.getAttribute("tmpId");
													
													for (TimeConfTmpPo tmpPo : timeConfTmpList) {
												%>
												<option value="<%=tmpPo.getTmpId()%>"
													<%if (tmp != null && tmpPo.getTmpId() == tmp.getTmpId()) {
					out.print("selected");
				}%>><%=tmpPo.getAliasLogName()%></option>
												<%
													}
												%>
											</select>
											<input type="submit" value="获取模板">
										</div>
									</form>


									<%if(tmpId != null){ %>
									<form action="<%=request.getContextPath()%>/app_info.do?method=collectAdd" method="post">
										<input type="hidden" name="appId" value="${appInfo.appId }"/>
										<input type="hidden" name="aliasLogName" value="<%=tmp.getAliasLogName() %>" />
										<input type="hidden" name="tmpConfigId" value="<%=tmpId %>" />
										<table align="center" 
											class="table table-striped table-condensed table-bordered">

											<tr>
												<td width="200">
													模版名称:
												</td>
												<td id="aliasLogName"><%=tmp.getAliasLogName() %>
										
												</td>
												
											</tr>
											<tr>
												<td >
													数据行分隔符:
												</td>
												<td>
													<select name="split" id="split">
														<option value="\n"
															<%if(tmp.getSplitChar().equals("\\n")){out.print("selected");} %>>
															\n
														</option>
														<option value="\02"
															<%if(tmp.getSplitChar().equals("\\02")){out.print("selected");} %>>
															\02
														</option>
													</select>
												</td>
											</tr>
											<tr>
												<td >
													收集频率:
												</td>
												<td>
													<input type="text" name="frequency" id="frequency"
														value="<%=tmp.getAnalyseFrequency() %>">
												</td>
											</tr>
											<tr>
												<td >
													数据获取:
												</td>
												<td >
													<select id="obtainTypeid" name="obtainType"
														onchange="obtainChange(this.options[this.selectedIndex].value)">
														<option value="1"
															<%if(tmp.getObtainType()==1){out.print("selected");} %>>
															日志文件
														</option>
														<option value="2"
															<%if(tmp.getObtainType()==2){out.print("selected");} %>>
															shell方式
														</option>
														<option value="3"
															<%if(tmp.getObtainType()==3){out.print("selected");} %>>
															http方式
														</option>
														<option value="4"
															<%if(tmp.getObtainType()==4){out.print("selected");} %>>
															jmx方式
														</option>
														<option value="5"
															<%if(tmp.getObtainType()==5){out.print("selected");} %>>
															configserver推送
														</option>
													</select>
												</td>
											</tr>
											<tr>
												<td  id="obtainId">
													文件路径:
												</td>
												<td>
													<input type="text" name="filePath" id="filePath" size="100"
														value="<%=tmp.getFilePath()==null?"":tmp.getFilePath() %>">
													&nbsp;&nbsp;如果分析器类型为执行ssh后执行分析器，请将ssh指令写这里
												</td>
											</tr>
											<tr id="tailId">
												<td>
													tail模式:
												</td>
												<td>
													<select name="tailType" id="tailType">
														<option value="line"
															<%if(tmp.getTailType().equals("line")){out.print("selected");} %>>
															行
														</option>
														<option value="char"
															<%if(tmp.getTailType().equals("char")){out.print("selected");} %>>
															字节
														</option>
													</select>
												</td>
											</tr>
											<tr>
												<td >
													分析方式:
												</td>
												<td>
													<select id="analyseTypeid" name="analyseType"
														onchange="analyseChange(this.options[this.selectedIndex].value)">
														<option value="1"
															<%if(tmp.getAnalyseType()==1){out.print("selected");} %>>
															java-class
														</option>
														<option value="2"
															<%if(tmp.getAnalyseType()==2){out.print("selected");} %>>
															javscript
														</option>
													</select>
												</td>
											</tr>
											<tr id="classId">
												<td >
													java分析器:
												</td>
												<td>
													<input type="text" id="analyseClass" name="analyseClass"
														value="<%=tmp.getClassName() %>"  class="span10">
												</td>
											</tr>
											<tr id="scriptId">
												<td  id="scriptContentId">
													分析器的附带参数:
												</td>
												<td>
													<textarea rows="10"class="span10" name="future" id="future"><%=tmp.getAnalyseFuture() %></textarea>
													<br/>如果是读取日志后执行javascript分析器,请将javascript写这里
												</td>
											</tr>
											<tr>
												<td >
													描述说明:
												</td>
												<td id="desc" >
													<textarea rows="3"  class="span10" readonly="readonly" name="analyseDesc"><%=tmp.getAnalyseDesc() %></textarea>
												</td>
											</tr>
										</table>
										<div style="text-align:center">
										<input type="submit" value="&nbsp;添&nbsp;&nbsp;加&nbsp;" id="btnAdd" class="btn-primary">
										</div>
									</form>
									<%} %>
								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">

	</script>
</html>
