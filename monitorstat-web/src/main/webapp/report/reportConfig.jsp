<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>���ļ��-�������</title>
<style type="">
.btn, .btn span {
    cursor: pointer;
    overflow: hidden;
    text-align: center;
    white-space: nowrap;
}
.btn-big {
    background-position: -504px 0;
    color: #000000;
    font-size: 14px;
    font-weight: bold;
    height: 46px;
    line-height: 46px;
    text-decoration: none;
    width: 61px;
}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.1.3.2.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/js/jquery.tablesorter.pager.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/toolTip.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/global.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.tablesorter.js"></script>
<script type="text/javascript">
function addReport(){
	$.layerSetup({
		 id:"toolTip",
		 title:'��������',
		 width:500,//���ÿ��
		 height:290,
		 content:'<iframe id="frm_sentence" src="goAddReport.do" frameborder="0" height="99%" width="99%" ></iframe>',
		 isbg:false
		 });
    $.layershow();
}
function updateReport(reportId){
	$.layerSetup({
		 id:"toolTip",
		 title:'�޸ı���',
		 width:500,//���ÿ��
		 height:290,
		 content:'<iframe id="frm_sentence" src="forwardUpdateReport.do?reportId='+reportId+'" frameborder="0" height="99%" width="99%" ></iframe>',
		 isbg:false
		 });
    $.layershow();
}
function showsentence(reportId){
	$.layerSetup({
		 id:"toolTip",
		 title:'����Ӧ�ñ���',
		 width:762,//���ÿ��
		 height:650,
		 content:'<iframe id="frm_sentence" src="goAddReportAcceptor.do?reportId='+reportId+'" frameborder="0" height="99%" width="99%" ></iframe>',
		 isbg:false
		 });
	$.layershow();
}
function deleteReport(reportId){
	if(confirm('ȷ��Ҫɾ���ñ���?')){
	    jQuery.ajax({
			url:"ajaxDeleteReport.do",
		    type: 'POST',
		    dataType: 'html',
		    data:"reportId="+reportId,
		    timeout: 200000,
		    error: function(){},
		    success: function(data){
			    window.location.reload(true);
		    }
		})
	}
	return false;
}
function resumeJob(reportId){
	 if(confirm('ȷ��Ҫ�����ñ�����?')){
		 jQuery.ajax({
				url:"ajaxResumeJob.do",
			    type: 'POST',
			    dataType: 'html',
			    data:"reportId="+reportId,
			    timeout: 200000,
			    error: function(){},
			    success: function(data){
				    if(data == '������'){
				    	$("#job_"+reportId).html("<a onclick='pauseJob("+reportId+");' href='#'><font style='color:green;font-weight:bold'>��ͣ����</font></a>&nbsp;");
				    	$("#state_"+reportId).html("<font style='color:blue;font-weight:bold'>"+data+"</font>");
				    }
			    }
			})
	 }return false;
}
function pauseJob(reportId){
	 if(confirm('ȷ��Ҫ��ͣ�ñ�����?')){
		 jQuery.ajax({
				url:"ajaxPauseJob.do",
			    type: 'POST',
			    dataType: 'html',
			    data:"reportId="+reportId,
			    timeout: 200000,
			    error: function(){},
			    success: function(data){
			    	if(data == '��ͣ��'){
			    		$("#job_"+reportId).html("<a onclick='resumeJob("+reportId+");' href='#'><font style='color:blue;font-weight:bold'>��������</font></a>&nbsp;");
			    		$("#state_"+reportId).html("<font style='color:green;font-weight:bold'>"+data+"</font>");
			    	}
			    }
			})
	 }return false;
}
function testSendJob(reportId){
	 if(confirm('ȷ��Ҫ���Ըñ�����?')){
		 jQuery.ajax({
				url:"ajaxTestSendJob.do",
			    type: 'POST',
			    dataType: 'html',
			    data:"reportId="+reportId,
			    timeout: 200000,
			    error: function(){},
			    success: function(data){
				    alert("��鿴�Ƿ���յ�����");
			    }
			})
	 }return false;
}
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<div align="center">
<div class="hd" align="left"><font class="title">&nbsp;��������</font>
<span id="opt">
<c:if test="${reportconfig}">
<font style='font-weight:bold'><a href="javascript:addReport();">��������</a></font>
</c:if>
</span>
</div>
<div id="dialog" class="ui-dialog-content ui-widget-content" style="align:center;width:100%" align="center">
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:99%;align:center">
   <thead>
   <tr>
   <th width="12%" align="center">������</th>
   <th width="40%" align="center">����·��</th>
   <th width="6%" align="center">��������</th>
   <th width="8%" align="center">Quartz Cron</th>
   <th width="6%" align="center">����״̬</th>
   <th width="30%" align="center">����</th>
   </tr>
   </thead>
   <tbody>
	<c:forEach items="${listReportList}" var="valueEntry">
     <c:choose>
       <c:when test="${valueEntry.type == ''}">
       	<tr>
		  <td>
			${valueEntry.name}:<input type="checkbox" name="report_${valueEntry.id}"  value="${valueEntry.id}"/>
		  </td>	
		</tr>
       </c:when>
       <c:otherwise>
		<tr>
			<td align="center">${valueEntry.reportName}</td>
			<td align="center">${valueEntry.path}</td>
			<td align="center">${valueEntry.type}</td>
			<td align="center">${valueEntry.quartzCron}</td>
			<td id="state_${valueEntry.id}" align="center">
			<c:choose>
			<c:when test="${valueEntry.jobState == 0}">
			<font style="color:blue;font-weight:bold">${valueEntry.jobStateMsg}</font>
			</c:when>
			<c:when test="${valueEntry.jobState == 1}">
			<font style="color:green;font-weight:bold">${valueEntry.jobStateMsg}</font>
			</c:when>
			<c:when test="${valueEntry.jobState == -1}">
			<font style="color:red;font-weight:bold">${valueEntry.jobStateMsg}</font>
			</c:when>
			</c:choose>
			</td>
			<td align="center">
			<span style="width:100px;"><a onclick="showsentence('${valueEntry.id}');" href="#"><font style='font-weight:bold'>���ñ���</font></a>&nbsp;</span>	
			<!--  
			<span style="width:200px;"><a onclick="showsentence('${valueEntry.id}');" href="#"><font style='font-weight:bold'>�鿴�����ñ���</font></a>&nbsp;</span>
			-->
			<c:if test="${reportconfig}">
			    <span style="width:200px;"><a onclick="testSendJob('${valueEntry.id}');" href="#"><font style='font-weight:bold'>���Խ��ձ���</font></a>&nbsp;</span>
			    <span style="width:100px;">
				<a onclick="updateReport('${valueEntry.id}');" href="#"><font style='font-weight:bold'>�޸ı���</font></a>&nbsp;
				</span>
				<span style="width:80px;" id="job_${valueEntry.id}">
				<c:choose>
				<c:when test="${valueEntry.jobState == -1 || valueEntry.jobState == 1}">
				<a onclick="resumeJob('${valueEntry.id}');" href="#"><font style='color:blue;font-weight:bold'>��������</font></a>&nbsp;
				</c:when>
				<c:otherwise>
				<a onclick="pauseJob('${valueEntry.id}');" href="#"><font style='color:green;font-weight:bold'>��ͣ����</font></a>&nbsp;
				</c:otherwise>
				</c:choose>
				</span>
				<span style="width:60px;">
			    <a href="#" onclick="javascript:deleteReport('${valueEntry.id}')"><font style='color:green;font-weight:bold'>ɾ��</font></a>&nbsp;
			    </span>
			</c:if>
			</td>
		</tr>
       </c:otherwise>
     </c:choose>
	</c:forEach>
	</tbody>
</table>
</div>
</div>
<script type="text/javascript">
$("#myTable").tablesorter();
</script>
</body>
</html>