<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>显示检查过程结果</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<%
	String opsName = (String)request.getParameter("opsName");
	String targetOpsName = (String)request.getParameter("targetOpsName");
	String flag = (String)request.getParameter("flag");
%>
</head>
<body >
<div>
	<h4><span>执行<font><%=opsName%></font>依赖的应用<font><%=targetOpsName%></font>强弱检查</span></h4>
</div>
<div id="message" class='table_comm'>
	
</div>
<script type="text/javascript">
String.prototype.replaceAll  = function(s1,s2){    
	return this.replace(new RegExp(s1,"gm"),s2);    
} 
function queryAutoMessage(){
	var url = "<%=request.getContextPath() %>/checkupdepend.do";
	$.getJSON(url, 
			{
			method:'showAutoMessage',
			opsName:'<%=opsName%>',
			targetOpsName: '<%=targetOpsName%>',
			time: '<%=new Date()%>'
			}, 
			function(json){
		$("#message").empty();
		var msg = ""; 
		msg += "<table class='table table-striped table-bordered table-condensed' align='center'>";
		
		$("#message").append();
		for(var i=0;i<json.length;i++){
			msg += "<tr><td width='80px'>类型:"+json[i].isolationType+"</td><td width='140px'>步骤:<strong>"+json[i].stepType+"</strong></td><td>结果:"+json[i].stepStatus+"</td></tr>"; 
			var descMsg = json[i].stepMessage.replaceAll("@@","<br/>");
			msg += "<tr><td>描述:</td><td colspan='3'>"+ descMsg +"</td></tr>";
			msg += "<tr><td colspan='4'>&nbsp;</td></tr>";
		}
		msg += "</table>";
		$("#message").append(msg);
	}); 
	<%
		if(flag == null) {
		  %>
		  window.setTimeout("queryAutoMessage()",5000);		  
		  <%
		}
	%>
}
queryAutoMessage(); 

<!-- 
function   KeyDown(){ //禁止页面刷新  鼠标\键盘\F5
    if   ((window.event.altKey)&&((window.event.keyCode==37)||(window.event.keyCode==39))){   
          event.returnValue=false; 
      } 
    if((event.keyCode==116)||(event.ctrlKey   &&   event.keyCode==82)){ 
          event.keyCode=0; 
          event.returnValue=false; 
      } 
    if   ((event.ctrlKey)&&(event.keyCode==78)){       
          event.returnValue=false; 
      } 
    if   ((event.shiftKey)&&(event.keyCode==121)){   
          event.returnValue=false; 
      } 
    if   (window.event.srcElement.tagName   ==   "A "   &&   window.event.shiftKey){   
            window.event.returnValue   =   false;   
      } 
    if   ((window.event.altKey)&&(window.event.keyCode==115)){ 
            window.showModelessDialog( "about:blank ", " ", "dialogWidth:1px;dialogheight:1px "); 
            return   false; 
    } 
} 
document.onkeydown=KeyDown; 

function   Click(){ 
	window.event.returnValue=false; 
} 
document.oncontextmenu=Click; 
//--> 
</SCRIPT>

</body>
</html>