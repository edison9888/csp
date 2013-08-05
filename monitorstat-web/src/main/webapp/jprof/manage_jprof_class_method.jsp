<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.core.po.JprofHost"%>
<%@page import="com.taobao.monitor.web.core.po.JprofClassMethod"%>
<%@page import="com.taobao.monitor.common.util.Arith"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.accordion.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<style type="text/css">
div {
	font-size: 12px;
}
table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "宋体";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>  
  
  
  <script type="text/javascript">

  	$.ui.dialog.defaults.bgiframe = true;
	$(function() {
		$("#stack_dialog").dialog({ autoOpen: false ,width:800,modal:true});
		$("#stack_msg_dialog").dialog({ autoOpen: false ,width:1000,modal:true});
	});


	function showMethodRootStackMessage(appName,className,methodName,collectDay){
		var url = "<%=request.getContextPath() %>/ajax/monitor.json?action=jprof_stack_root&appName="+appName+"&className="+className+"&methodName="+methodName+"&collectDay="+collectDay+"&time="+Math.random();
		$.getJSON(url,function(json){
			$("#root_stack_msg").empty();
			var html = "";
			for(i in json){
				html+="<tr><td>"+i+"</td><td align='center'><a href='#' onclick=\"showMethodStackMessage('"+appName+"','"+json[i]+"','"+collectDay+"')\">查看详细</a> </td></tr>";				
			}
			html+="<div>";
			$("#root_stack_msg").html(html);
			$("#stack_dialog" ).dialog("option","title",(className+"中"+methodName+"方法是在的stack信息"));
	        $("#stack_dialog" ).dialog( 'open' );
		})
	}


	function showMethodStackMessage(appName,md5,collectDay){
		var url = "<%=request.getContextPath() %>/ajax/monitor.json?action=jprof_stack_msg&md5="+md5+"&appName="+appName+"&collectDay="+collectDay+"&time="+Math.random();
		$.getJSON(url,function(json){
			$("#stack_msg_panel").empty();
			var html = "";
			for(var i=0;i<json.length;i++){
				var stack = json[i];
				var stacknum = stack.stackNum;
				for(var j=0;j<i;j++){
					html+="-";
				}
				html+=stack.className+":"+stack.methodName+":"+stack.lineNum+"&nbsp;&nbsp;&nbsp;&nbsp;"+stack.excuteNum+"&nbsp;&nbsp;&nbsp;&nbsp;"+stack.useTime;
				html+="<br/>";
			}
			$("#stack_msg_panel").html(html);
			$("#stack_msg_dialog" ).dialog("option","title",("stack信息"));
	        $("#stack_msg_dialog" ).dialog( 'open' );
		})
	}
  </script>

</head>
<%

String orderType = request.getParameter("orderType");

if(orderType==null){
	orderType = "1";
}

String appName = request.getParameter("appName");
String collectDay = request.getParameter("collectDay");
String classNamelike = request.getParameter("classNamelike");
if(classNamelike==null){
	classNamelike="com/";
}
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

if(collectDay==null){
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
	collectDay = sdf.format(cal.getTime());
}

%>
<body class="example"> 
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<form action="./manage_jprof_class_method.jsp" method="get">
  <table>
  	<tr>
  		<td>
  			应用名:<input type="text" name="appName" value="<%=appName==null?"":appName %>">
  			时间:<input type="text" name="collectDay" value="<%=collectDay %>">
  			类名匹配:<input type="text" name="classNamelike" value="<%=classNamelike==null?"":classNamelike %>" size="60"> 格式com/taobao/
  		</td>
  	</tr>
  	<tr>
  		<td>
  			<input type="submit" value="查看jprof信息">   <input type="button" onclick="location.href='./manage_jprof_host.jsp'" value="返回jprof机器列表">
  		</td>
  	</tr>
  </table>
 </form>
  <%
  
  if(appName != null&&collectDay != null){
  
  List<JprofClassMethod> listClass = MonitorJprofAo.get().findJprofClassMethod(appName,collectDay);
  
  if("1".equals(orderType)){	  
	  Collections.sort(listClass,new Comparator<JprofClassMethod>(){
		  public int compare(JprofClassMethod o1, JprofClassMethod o2){
			  long e1 = o1.getExcuteNum();
			  double t1 = o1.getUseTime();
			  
			  long e2 = o2.getExcuteNum();
			  double t2 = o2.getUseTime();
			  
			  if(t1/e1 >t2/e2){
				  return -1;
			  }else if(t1/e1 == t2/e2){
				  return 0;
			  }			  
			  return 1;
		  }
	  });	  
  }
  
  
  if("2".equals(orderType)){	  
	  Collections.sort(listClass,new Comparator<JprofClassMethod>(){
		  public int compare(JprofClassMethod o1, JprofClassMethod o2){
			  long e1 = o1.getExcuteNum();
			  long e2 = o2.getExcuteNum();
			  
			  if(e1 >e2){
				  return -1;
			  }else if(e1 == e2){
				  return 0;
			  }			  
			  return 1;
		  }
	  });	  
  }
  
  if("3".equals(orderType)){	  
	  Collections.sort(listClass,new Comparator<JprofClassMethod>(){
		  public int compare(JprofClassMethod o1, JprofClassMethod o2){
			  double t1 = o1.getUseTime();
			  double t2 = o2.getUseTime();
			  
			  if(t1 >t2){
				  return -1;
			  }else if(t1 == t2){
				  return 0;
			  }			  
			  return 1;
		  }
	  });	  
  }
  
 // Map<String,List<JprofClassMethod>> mapClass = new HashMap<String,List<JprofClassMethod>>();
  
 // for(JprofClassMethod method:listClass){
//	  String className = method.getClassName();
//	  
//	  if(classNamelike!=null&&className.indexOf(classNamelike)<0){
//		  continue;
//	  }
//	  
//	  List<JprofClassMethod> list =  mapClass.get(className);
//	  if(list == null){
//		  list = new ArrayList<JprofClassMethod>();
//		  mapClass.put(className,list);
//	  }	  
//	  list.add(method);	  
 // }
  
  
  
  
  %>


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >jprof 信息列表</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content">	
	<tr class="headcon ">
		<td >类信息</td>	
		<td >平均时间(<a href="./manage_jprof_class_method.jsp?orderType=1&appName=<%=appName %>&collectDay=<%=collectDay %>&classNamelike=<%=classNamelike %>">降序</a>)</td>		
		<td >总次数(<a href="./manage_jprof_class_method.jsp?orderType=2&appName=<%=appName %>&collectDay=<%=collectDay %>&classNamelike=<%=classNamelike %>">降序</a>)</td>
		<td >总时间(<a href="./manage_jprof_class_method.jsp?orderType=3&appName=<%=appName %>&collectDay=<%=collectDay %>&classNamelike=<%=classNamelike %>">降序</a>)</td>
		<td >操作</td>
	</tr>
	<%
	for(JprofClassMethod m:listClass){
		%>
		<tr >
			<td ><%=m.getClassName()+":"+m.getMethodName()+":"+m.getLineNum() %></td>
			<td ><%=Arith.div(m.getUseTime(),m.getExcuteNum(),2) %></td>
			<td ><%=m.getExcuteNum() %></td>
			<td ><%=m.getUseTime() %></td>
			<td ><a onclick="showMethodRootStackMessage('<%=m.getAppName() %>','<%=m.getClassName() %>','<%=m.getMethodName() %>','<%=collectDay %>')" href="#">stack信息</a>&nbsp;<a href="">class信息</a></td>
		</tr>
		<%	
		} 
	%>
</table>
</div>
</div>  
  <%} %>
  
  
  
<div id="stack_dialog" title="stack 信息">
	<table border="1" width="100%" class="ui-widget ui-widget-content" style="font-size: 12px;" >
		<tr class="ui-widget-header ">
			<td align="center">root stack </td>
			<td align="center">操作 </td>			
		</tr>
		<tbody id="root_stack_msg">		
		</tbody>
	</table>
</div>
 
 
<div id="stack_msg_dialog" title="stack 信息">
	<table border="1" width="100%" class="ui-widget ui-widget-content" style="font-size: 12px;" >
		<tr class="ui-widget-header ">
			<td align="center" width="700">类名 </td>
			<td align="center">总执行次数 </td>
			<td align="center">总执行时间 </td>			
		</tr>		
	</table>
	<span id="stack_msg_panel"></span>
</div> 
  <jsp:include page="../buttom.jsp"></jsp:include>
</body>


</html>