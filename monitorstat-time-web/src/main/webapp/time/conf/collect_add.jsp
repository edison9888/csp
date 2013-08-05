<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>

<!DOCTYPE html>

<html>
	<head>
		<title>���ʵʱ����</title>
		<%
			//����base���ԣ����ɾ���·��
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
						<!-- <input type="text" class="input-small" placeholder="��ѯӦ������"  id="app_search"> -->
						
						&nbsp;&nbsp;&nbsp;
						<input class="btn btn-success" type="button" onclick="javscript:location.href='<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId='+getSelectAppId()" value="�鿴Ӧ��">
						
						<input type="text"  id="appname_text" style="width:150px;margin-left:100px;"/>
						<input class="btn btn-success" type="button"  onclick="javscript:location.href='<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId='+getInputAppId()" value="Ӧ��ֱ��">
			
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<%@include file="/leftmenu.jsp"%>
				</div>

				<div class="span12">
					<a href="<%=request.getContextPath()%>/index.jsp">ʵʱ��ҳ</a>->
					${appInfo.appName }���ʵʱ����

					<hr style="margin-bottom: 0px">
					
					<<<a href="<%=request.getContextPath() %>/app_info.do?method=collect&appId=${appInfo.appId}" style="" onclick="return confirm('ȷ��Ҫ�뿪�༭ҳ�����㵱ǰ�༭�����ݲ��ᱻ���棡')">�����б�ҳ</a><<
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
											<input type="submit" value="��ȡģ��">
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
													ģ������:
												</td>
												<td id="aliasLogName"><%=tmp.getAliasLogName() %>
										
												</td>
												
											</tr>
											<tr>
												<td >
													�����зָ���:
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
													�ռ�Ƶ��:
												</td>
												<td>
													<input type="text" name="frequency" id="frequency"
														value="<%=tmp.getAnalyseFrequency() %>">
												</td>
											</tr>
											<tr>
												<td >
													���ݻ�ȡ:
												</td>
												<td >
													<select id="obtainTypeid" name="obtainType"
														onchange="obtainChange(this.options[this.selectedIndex].value)">
														<option value="1"
															<%if(tmp.getObtainType()==1){out.print("selected");} %>>
															��־�ļ�
														</option>
														<option value="2"
															<%if(tmp.getObtainType()==2){out.print("selected");} %>>
															shell��ʽ
														</option>
														<option value="3"
															<%if(tmp.getObtainType()==3){out.print("selected");} %>>
															http��ʽ
														</option>
														<option value="4"
															<%if(tmp.getObtainType()==4){out.print("selected");} %>>
															jmx��ʽ
														</option>
														<option value="5"
															<%if(tmp.getObtainType()==5){out.print("selected");} %>>
															configserver����
														</option>
													</select>
												</td>
											</tr>
											<tr>
												<td  id="obtainId">
													�ļ�·��:
												</td>
												<td>
													<input type="text" name="filePath" id="filePath" size="100"
														value="<%=tmp.getFilePath()==null?"":tmp.getFilePath() %>">
													&nbsp;&nbsp;�������������Ϊִ��ssh��ִ�з��������뽫sshָ��д����
												</td>
											</tr>
											<tr id="tailId">
												<td>
													tailģʽ:
												</td>
												<td>
													<select name="tailType" id="tailType">
														<option value="line"
															<%if(tmp.getTailType().equals("line")){out.print("selected");} %>>
															��
														</option>
														<option value="char"
															<%if(tmp.getTailType().equals("char")){out.print("selected");} %>>
															�ֽ�
														</option>
													</select>
												</td>
											</tr>
											<tr>
												<td >
													������ʽ:
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
													java������:
												</td>
												<td>
													<input type="text" id="analyseClass" name="analyseClass"
														value="<%=tmp.getClassName() %>"  class="span10">
												</td>
											</tr>
											<tr id="scriptId">
												<td  id="scriptContentId">
													�������ĸ�������:
												</td>
												<td>
													<textarea rows="10"class="span10" name="future" id="future"><%=tmp.getAnalyseFuture() %></textarea>
													<br/>����Ƕ�ȡ��־��ִ��javascript������,�뽫javascriptд����
												</td>
											</tr>
											<tr>
												<td >
													����˵��:
												</td>
												<td id="desc" >
													<textarea rows="3"  class="span10" readonly="readonly" name="analyseDesc"><%=tmp.getAnalyseDesc() %></textarea>
												</td>
											</tr>
										</table>
										<div style="text-align:center">
										<input type="submit" value="&nbsp;��&nbsp;&nbsp;��&nbsp;" id="btnAdd" class="btn-primary">
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
