<%@ page language="java" contentType="text/html; charset=GBK"  isELIgnored="false" 
    pageEncoding="GBK"%>
    
    <%
    	String contextPath = request.getContextPath();
     %>
<div class="sidebar">
        <div class="well">
          <h5>Ӧ������</h5>
          <ul>
            <li><a href="<%=request.getContextPath() %>/show/appconfig.do?method=showAppInfo&groupName=AllApp">Ӧ�ù���</a></li>
            <li><a href="<%=request.getContextPath() %>/show/appconfig.do?method=showAppInfo&groupName=AllApp">�ձ�����ģ�����</a></li>
            <li><a href="<%=request.getContextPath() %>/show/appconfig.do?method=gotoTimeTmp">ʵʱ����ģ�����</a></li>
          </ul>

          <h5>���ݿ⡢����������</h5>
          <ul>
            <li><a href="<%=contextPath %>/config/dbconf/list.jsp">���ݿ�����</a></li>
            <li><a href="<%=contextPath %>/config/hostconf/list.jsp">����������</a></li>
          <!--   <li><a href="#">Link</a></li>
            <li><a href="#">Link</a></li>
            <li><a href="#">Link</a></li>
            <li><a href="#">Link</a></li> -->
          </ul>

           <h5>Key����</h5>
          <ul>
           <li><a href="<%=contextPath %>/config/keyconf/list.jsp">Key���ù���</a></li>
          <!--   <li><a href="#">Link</a></li>
            <li><a href="#">Link</a></li> -->
          </ul>

		   <h5>�û�����</h5>
          <ul>
            <li><a href="<%=contextPath %>/show/UserConf.do?method=gotoUserConf">�û����ù���</a></li>
          </ul>
        </div>
      </div>