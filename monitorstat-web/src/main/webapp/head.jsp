<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%

LoginUserPo po = SessionUtil.getUserSession(request);

%>
<%

Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, -1);

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="com.taobao.monitor.web.util.SessionUtil"%>
<link rel="stylesheet" href="http://a.tbcdn.cn/p/header/header-min.css?t=20100913.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/css/headmenu.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/headmenu.js"></script>
<div id="header">
	<div class="usertips">
		<p class="login-info"> &nbsp;&nbsp; </p>
		<p class="login-info"> ���ã���ӭ�����ϵͳ��
		<%if(po == null){ %>
		<a target="_top" href="<%=request.getContextPath() %>/login.jsp" class="user-nick">���¼</a>
		<%}else{ %>
		<a href="<%=request.getContextPath() %>/user/user_info2.jsp?id=<%=po.getId() %>" target="_blank"><%=po.getName() %></a>&nbsp;&nbsp;<a href="<%=request.getContextPath() %>/user/user_info_update.jsp?id=<%=po.getId() %>" target="_blank">�޸�</a>&nbsp;&nbsp;<a href="<%=request.getContextPath() %>/logout.jsp">ע��</a>
		<%} %>
		</p>
	</div>
  <ul id="nav" class="quick-menu">
  <li><span><a target="_top" href="<%=request.getContextPath() %>/index.jsp"> &nbsp;&nbsp;��ҳ&nbsp;&nbsp;</a></span></li>
    <li><span><a target="_top" href="<%=request.getContextPath() %>/index_day.jsp"> &nbsp;&nbsp;�ձ�ϵͳ&nbsp;&nbsp;</a></span></li>
    <li> <span>[<a target="_top" href="http://depend.csp.taobao.net:9999/depend/app_info.do?method=gotoIndexPage"> &nbsp;&nbsp;����ϵͳ &nbsp;&nbsp;</a>]</span> </li>
    <li> <span>[<a target="_top" href="http://time.csp.taobao.net:9999/time/index_table.jsp"> &nbsp;&nbsp;ʵʱ��� &nbsp;&nbsp;</a>]</span> </li>
    <li> <span><a target="_top" href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit"> &nbsp;&nbsp;���������滮 &nbsp;&nbsp;</a></span></li>
     <li> <span><a target="_blank" href="<%=request.getContextPath() %>/alarm/index.jsp"> &nbsp;&nbsp;����ƽ̨���� &nbsp;&nbsp;</a></span></li>
     <li> &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp; 
     </li>
  </ul>
</div>
