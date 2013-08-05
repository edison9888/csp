<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="java.util.*"%>
<!DOCTYPE html>


<html>
	<head>
		<title>TDDL��ѯ</title>

		<%
			//����base���ԣ����ɾ���·��
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

#table1 td { /*word-wrap: break-word*/
	
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
					<div class="row">

						<div class="span2"
							style="height: 50px; padding-top: 10px; font-size: xx-large;">
							TDDL��ѯ
						</div>
						<div style="float: left">
							<%
								//Ĭ��ֵ1
								String type = request.getParameter("type") != null ? request
										.getParameter("type") : "1";
								request.setAttribute("type", type);
							%>
							<ul class="nav nav-tabs">
								<li <c:if test="${type==1}">class="active"</c:if>>
									<a
										href="<%=request.getContextPath()%>/tddl/show.do?method=list&day=${day}">Ӧ���б�</a>
								</li>
								<li <c:if test="${type==2}">class="active"</c:if>>
									<a
										href="<%=request.getContextPath()%>/tddl/show.do?method=list&type=2&day=${day}">DB�б�</a>
								</li>
							</ul>


						</div>

						<div style="float: right; padding-right: 20px; font-size: 14px">
							<a href="<%=request.getContextPath()%>/tddl/show.do?method=query">�����ѯ</a>
						</div>
					</div>
						<span>
						ƽ��ʱ���ֵ����SQL��ƽ������ʱ�䣬ʱ�䵥λΪ����
						</span>					
					<div class="row-fluid">
						<div class="span12">
							<div class="row">
								<div class="span8 offset2" style="text-align: center">
									<div>
										<form id="form1"
											action="<%=request.getContextPath()%>/tddl/show.do?method=list">

											����:
											<input style="width: 100px;" type="text" name="name"
												value="${name }" />
											&nbsp;&nbsp;&nbsp;&nbsp; ����:
											<input type="text" style="width: 100px; padding-left: 5px"
												value="${day}" name="day"
												onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
											&nbsp;&nbsp;&nbsp;
											����:
											<select id="sortType" name="sortType">
													<option value="execNum" <c:if test="${sortType=='execNum'}">selected="selected"</c:if>>ִ��������</option>
													<option value="elaTime" <c:if test="${sortType=='elaTime'}">selected="selected"</c:if>>ƽ��ʱ������</option>
													<option value="itemName" <c:if test="${sortType=='itemName'}">selected="selected"</c:if>>��������</option>
											</select>																								
											<input type="button" value="��ѯ" class="btn btn-primary"
												id="button1" />

											<input type="hidden" name="method" value="list" />
											<input type="hidden" name="type" value="${type }" />

											<input type="hidden" name="pageNo" id="hidden1"
												value="${pagination.pageNo}" />

											<input type="hidden" name="pageSize" id="hidden2"
												value="${pagination.pageSize}" />


										</form>
									</div>
								</div>
							</div>
							<div class="row" style="padding-bottom: 10px">


								<!-- end span8 -->
							</div>
							<!-- end row -->
							<div class="row">
								<div class="span8 offset2">
									<table
										class="table table-striped table-condensed table-bordered"
										id="table1" style="">
										<thead>
											<tr>
												<th style="text-align: center;">
													����
												</th>

												<%--�����DB����ʾip��port --%>
												<c:if test="${type==2}">
													<th style="text-align: center;">
														DB IP
													</th>
													<th style="text-align: center;">
														DB PORT
													</th>
												</c:if>


												<!-- class����ָ��Ҫ��ʾ��һЩ�У���������ʾ ����so��show order����д����ǰ׺��Ϊ�˺���ͨ��class���ֿ�������������߼�����ģ�-->
												<th style="text-align: center;">
													ִ����
												</th>

												<th style="text-align: center;">
													ƽ��ʱ��
												</th>
												<th style="text-align: center;">
												</th>


											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pagination.list}" var="item">
												<tr>
													<td style="text-align: center;">
														<c:if test="${type==1}">
														<a
															href="<%=request.getContextPath()%>/tddl/show.do?method=query&query1Type=1&query1Value=${day }&name=${item.name}&query2Type=${type}&query3Type=1">${
															item.name}</a>
														</c:if>
														<c:if test="${type==2}">
														<a
															href="<%=request.getContextPath()%>/tddl/show.do?method=query&query1Type=1&query1Value=${day }&name=${item.name}&query2Type=${type}&query3Type=1&ip=${item.dbIp}&port=${item.dbPort}">${
															item.name}</a>
														</c:if>
													</td>
													<c:if test="${type==2}">
														<td style="text-align: center;">
															${ item.dbIp}
														</td>
														<td style="text-align: center;">

															${ item.dbPort}
														</td>
													</c:if>
													<td style="text-align: center;">
													
														${ item.executeSumStr}
													</td>
													<td style="text-align: center;">
														${ item.timeAverage}
													</td>
													<td style="text-align: center;">
														<a href="<%=request.getContextPath()%>/tddl/show.do?method=gotoTddlHistoryGraph&name=${item.name}&type=${type}&endDate=${day}" target="_blank;">�鿴��ʷ����</a>
													</td>													
												</tr>

											</c:forEach>

										</tbody>
									</table>
									��ǰҳ��${pagination.pageNo}&nbsp;
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
										<a href="${pagination.pageNo-1 }" id="link1">��һҳ</a>
									</c:if>
									��ǰҳ��${pagination.pageNo}
									<c:if test="${pagination.pageNo < pagination.totalPage}">
										<a href="${pagination.pageNo+1 }" id="link2">��һҳ</a>
									</c:if>
									<a href="<%=request.getContextPath()%>/depend/appinfo/tddl/guide.txt" target="_blank" style="float:right">ʹ��ָ��</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div align="center">
		������������ϵ:<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=��ͤ&site=cntaobao&s=2&charset=gbk" >
    <img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=��ͤ&site=cntaobao&s=2&charset=gbk" />��ͤ</a>				
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
		// �����ҳ���ӣ��ύ��
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

	});
	
	</script>

	</body>
</html>
