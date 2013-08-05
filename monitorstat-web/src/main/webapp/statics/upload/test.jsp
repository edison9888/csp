<%@page contentType="text/plain; charset=UTF-8"%>
RequestURI: <%=request.getRequestURI()%>
ContextPath: <%=request.getContextPath()%>
ServletPath: <%=request.getServletPath()%>
PathInfo: <%=request.getPathInfo()%>
QueryString: <%=request.getQueryString()%>
Request: <%=request%>
Response: <%=response%>
======================================================
<%
RequestDispatcher dispatcher = request.getRequestDispatcher("/world/aaa/bbb.jhtml?c=111");
dispatcher.include(request, response);
%>