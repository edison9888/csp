<%@ page import="java.util.*"%>
<%@ page import="com.taobao.moinitor.matrix.dataobject.*"%>
<%@ page import="com.taobao.moinitor.matrix.enumtype.*"%>
<%@ page import="com.taobao.moinitor.matrix.util.*"%>
<%@ page import="com.taobao.moinitor.matrix.*"%>
<%@ page import="com.taobao.moinitor.matrix.parser.MatrixAppParser"%>
<ul id="navigation">
	<% for(AppGroupPo appGroup : AppGroupPo.getAllAppGroups()) { %>
	<li>
		<a href="index.jsp?app-group=<%= java.net.URLEncoder.encode(java.net.URLEncoder.encode(appGroup.getGroupName(), "utf-8"), "utf-8") %>"><%=appGroup.getGroupName() %></a>
	<ul>
		<%for (AppPo app : appGroup.getAllApps()) { %>
		<li><a href="app_detail.jsp?name=<%= app.getName()%>"><%=app.getName() %></a></li>
		<%} %>
	</ul>
	</li>
	<% } %>
</ul>

<script type="text/javascript">
	$(document).ready(function() {
		$("#navigation").treeview( {
			persist : "cookie",
			collapsed : false,
			unique : false
		});
	});
</script>