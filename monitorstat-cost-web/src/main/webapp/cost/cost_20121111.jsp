<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/highcharts.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<title>˫11�ɱ�����</title>
</head>
<body style="padding-top:50px" class="span20">
<%@ include file="../../top.jsp" %>

<div style="color:red;">
˵����<br/>
1.��ͼ��ʾ����Ӧ����6�·������ĳɱ��仯����Ҫ�ǻ����ɱ��仯<br/>
2.�ɱ��ļ��㷽ʽ��ο���
<a href="<%=request.getContextPath() %>/cost_form.jsp" target="_blank">�ɱ����㹫ʽ</a><br/>
3.���κ���������ϵ:������С��   <br/>
<br/>

</div>

<a href="./cost20121111.do?type=tm" style="font-size:12px;">
        �Ա�Ӧ�óɱ��仯</a>
<a href="./cost20121111.do?type=tm" style="font-size:12px;">
        tmallӦ�óɱ��仯</a>
<hr/>
<c:forEach items="${msg}" var="tinfo" varStatus="status">
	<a href="<%=request.getContextPath() %>/appCost.do?method=showApp&appName=${tinfo.key}" target="_blank">����鿴${tinfo.key}��ϸ�ɱ�</a>

	<jsp:include page="innerPic.jsp" >
			<jsp:param name="app" value="${tinfo.key}" />
			<jsp:param name="vinfo" value="${tinfo.value}" />
			<jsp:param name="dinfo" value='["06-02","06-23","07-07","07-21","08-04","08-18","09-01","09-29","10-06","10-20","11-03","11-11"]' />
	</jsp:include>

</c:forEach>

</body>
</html>