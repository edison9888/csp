<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/dracular/raphael-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/dracular/dracula_graffle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/dracular/dracula_graph.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/dracular/dracula_algorithms.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/showchat.js"></script>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<title>ͼ����ʾ����</title>
<%
	String opsName = (String) request.getParameter("opsName"); //Ӧ������
	String selectDate = (String) request.getParameter("selectDate"); //ʱ��
	String dependAppType = (String) request
			.getParameter("dependAppType"); //Ӧ�õ����
	String showType = (String) request.getParameter("showType"); //��ʾ��ʽ
	String optType = (String) request.getParameter("optType"); //�Ա�����

	opsName = opsName != null ? opsName : "";
	selectDate = selectDate != null ? selectDate : "";
	dependAppType = dependAppType != null ? dependAppType : "all";
	showType = showType != null ? showType : "list";
	optType = optType != null ? optType : "same";
%>

<script type="text/javascript">
	//ȫ�ֱ���,showchat.js����ʹ��
	var contextPath = '<%=request.getContextPath()%>'; 
	var opsName = '<%=opsName%>';
	var selectDate = '<%=selectDate%>';
	var optType = '<%=optType%>';
</script>
</head>
<body>
	<!-- style="border:5px solid red;width: 600px;height: 400px" -->
	<div id="canvas" style="width: 100%;height: 100%"></div>
</body>
</html>