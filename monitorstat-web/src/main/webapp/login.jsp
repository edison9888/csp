<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%><html>
<%@ page isELIgnored="false"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
<meta HTTP-EQUIV="pragma" CONTENT="no-cache"/>
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"/>
<link rel="stylesheet" href="statics/css/global.css" type="text/css"/>
<title>监控</title>
<style>

</style>
</head>
<%

String action = request.getParameter("action");
if(action!=null&&action.equals("login")){
	String userName = request.getParameter("userName");
	String pwd = request.getParameter("pwd");
	LoginUserPo po = MonitorUserAo.get().getLoginUserPo(userName,pwd);
	
	if(po!=null){
		session.setAttribute("_USER_",po);
		response.sendRedirect("./index.jsp");
	}	
}
%>


<body>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all b1">
  <div class="b2">
	<form action="./login.jsp" method="post" id="login-form">	
	<input type="hidden" name="action" value="login"/>
	<div style="align:center;FONT-SIZE: 14px" align="center">请登录</div>
	<div style="clear:both;float:left;height:50px;width:450px;">
		<div>
		  <label>花名：</label><input type="text" name="userName" value=""/>
		</div>
		<div>
		  <label>密码：</label><input type="password" name="pwd" value=""/>
		</div>        
	    <div style="margin-left: 100px;">
	      <input type="submit" value="登录" class="macButton" name="event_submit_do_login"/>
	    </div>
		<div id="errmsg" style="color:red;text-align:left;">${powerMsg}</div>
		<div align="center">
	      <span style="margin-left:6px;line-height:26px;">联系我们:&nbsp;小赌
          <a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=小赌&site=cntaobao&s=2&charset=gbk" >
          <img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=小赌&site=cntaobao&s=2&charset=gbk" /></a>  
          &nbsp;斩飞
          <a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=斩飞&site=cntaobao&s=2&charset=gbk" >
          <img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=斩飞&site=cntaobao&s=2&charset=gbk" /></a>  
	      </span>
		</div>
	</div>	
	
	</form>
  </div>
</div>
</body>
</html>

