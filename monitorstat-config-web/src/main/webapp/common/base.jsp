<%@ page  contentType="text/html; charset=GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
//����base���ԣ����ɾ���·��
	String serverUrl = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort();

	String base = serverUrl + request.getContextPath();

	request.setAttribute("base", base);
 %>
 
 <script>
 	var base="${base}";
 </script>