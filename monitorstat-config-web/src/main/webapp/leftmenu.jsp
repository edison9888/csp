<%@ page language="java" contentType="text/html; charset=GBK"  isELIgnored="false" 
    pageEncoding="GBK"%>
    
    <%
    	String contextPath = request.getContextPath();
     %>
<div class="sidebar">
        <div class="well">
          <h5>应用配置</h5>
          <ul>
            <li><a href="<%=request.getContextPath() %>/show/appconfig.do?method=showAppInfo&groupName=AllApp">应用管理</a></li>
            <li><a href="<%=request.getContextPath() %>/show/appconfig.do?method=showAppInfo&groupName=AllApp">日报配置模板管理</a></li>
            <li><a href="<%=request.getContextPath() %>/show/appconfig.do?method=gotoTimeTmp">实时配置模板管理</a></li>
          </ul>

          <h5>数据库、服务器配置</h5>
          <ul>
            <li><a href="<%=contextPath %>/config/dbconf/list.jsp">数据库配置</a></li>
            <li><a href="<%=contextPath %>/config/hostconf/list.jsp">服务器配置</a></li>
          <!--   <li><a href="#">Link</a></li>
            <li><a href="#">Link</a></li>
            <li><a href="#">Link</a></li>
            <li><a href="#">Link</a></li> -->
          </ul>

           <h5>Key配置</h5>
          <ul>
           <li><a href="<%=contextPath %>/config/keyconf/list.jsp">Key配置管理</a></li>
          <!--   <li><a href="#">Link</a></li>
            <li><a href="#">Link</a></li> -->
          </ul>

		   <h5>用户配置</h5>
          <ul>
            <li><a href="<%=contextPath %>/show/UserConf.do?method=gotoUserConf">用户配置管理</a></li>
          </ul>
        </div>
      </div>