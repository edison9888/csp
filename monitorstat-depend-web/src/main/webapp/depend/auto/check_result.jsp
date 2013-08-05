<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.taobao.csp.depend.po.AutoCheckDependResult"%>
<%@ page import="com.taobao.csp.depend.checkup.job.IsolationType"%>
<%@ page import="com.taobao.csp.depend.util.ConstantParameters"%>
<%@ page import="com.taobao.csp.depend.util.StartUpParamWraper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>检测过程描述页面</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">

<script
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/statics/js/bootstrap.js"
	type="text/javascript"></script>
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
	
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>	
<script src="<%=request.getContextPath()%>/statics/userjs/fancyboxwraper.js" type="text/javascript"></script>	

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery.galleryview-3.0-dev.css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.galleryview-3.0-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.timers-1.2.js"></script>
<%
  String opsName = (String) request.getParameter("opsName");
  String targetOpsName = (String) request.getParameter("targetOpsName");
  Map<String, List<AutoCheckDependResult>> map = (Map<String, List<AutoCheckDependResult>>) request
      .getAttribute("map");
  if (map == null) {
    map = new HashMap<String, List<AutoCheckDependResult>>();
  }
  List<String[]> gallerylist = (List<String[]>) request
      .getAttribute("gallerylist");
  if (gallerylist == null)
    gallerylist = new ArrayList<String[]>();

  String checkupType = (String)request.getAttribute("checkupType");
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<style type="text/css">
	.fancybox-custom .fancybox-skin {
		box-shadow: 0 0 50px #222;
	}
</style>
<script type="text/javascript">
function closeGallery() {
	$("#gallery_div").hide();
} 
function showGallery() {
	$("#gallery_div").show();
} 
</script>
</head>
<body>

	<div class="container-fluid">
		<div style="text-align: center;">
			<h3>
				<span>执行<font><%=opsName%></font>依赖的应用<font><%=targetOpsName%></font>强弱检查过程描述
				</span>
			</h3>
		</div>
		<table class="table table-striped table-condensed table-bordered"
			id="table1" style="table-layout: fixed；">
			<tbody>
				<tr>
					<th colspan="2" style="font-size: small;">检测说明</th>
				</tr>
				<tr>
					<td style="width: 15%; text-align: center; color: #0088CC;">应用名称</td>
					<td style="width: 85%; text-align: left;">执行检测的应用<%=opsName%>,被检测的应用<%=targetOpsName%></td>
				</tr>
				<tr>
					<td style="width: 15%; text-align: center; color: #0088CC;">可用性检查方式</td>
					<td style="width: 85%; text-align: left;">
						<%=checkupType%> &nbsp;&nbsp;
						<%
						  if (checkupType != null && checkupType.equals("selenium")) {
						%>
								<a href="#" onclick="showGallery()" title="点击显示图片">显示标准检测步骤图片</a>&nbsp;&nbsp;<a href="#" onclick="closeGallery()">关闭截图</a>							  
							  <%
							  							    }
							  							  %>
					</td>
				</tr>
				<tr>
					<td style="width: 15%; text-align: center; color: #0088CC;">URl说明</td>
					<td style="width: 85%; text-align: left;">下面检测步骤中的URL非daily环境URL，为强弱依赖检测环境URL，修改本机Host文件后可以访问。
					<a href="http://baike.corp.taobao.com/index.php/12%E4%B8%AA%E5%BA%94%E7%94%A8%E5%8F%8A%E5%85%B6%E5%88%97%E8%A1%A8#Host.E4.BF.AE.E6.94.B9" target="_blank">查看host</a></td>
				</tr>				
				<tr>
					<td style="width: 15%; text-align: center; color: #0088CC;">浏览器说明</td>
					<td style="width: 85%; text-align: left;">推荐使用Chrome或IE8浏览器访问</td>
				</tr>				
			</tbody>
		</table>
		<%
		  if (checkupType != null && checkupType.equals("selenium")) {	//只有Selenium的时候，显示相册
		%>
			<div align="center" id="gallery_div">
				<h2>标准检测步骤：<a href="#" onclick="closeGallery()">关闭截图</a></h2>
				<ul id="gallery" >
					<%
						for(String[] rowStr: gallerylist) {
						  if(rowStr.length < 2)
						    continue;
						  out.println("<li><img src='ftp://" + StartUpParamWraper.getFtpIp() + "/"
		              + StartUpParamWraper.getFtpPath() + "/standard/" + opsName + "/" +  rowStr[1] + "' alt='" + rowStr[0] + "'/></li>");
						}
					%>
				</ul>
			</div>
		<%
		  }
		%>
		<table class="table table-striped table-condensed table-bordered"
			id="table1" style="table-layout: fixed；">
			<tbody>
				<%
				  for (IsolationType isolationType : IsolationType.values()) {
				    if (!map.containsKey(isolationType.toString()))
				      continue;
				%>
					  <tr><th colspan="4" style="font-size: small;">检测方式:<%=isolationType.getDesc()%></th></tr>
					  <tr>
						<td style="width:15%; text-align:center;color:#0088CC;">步骤</td>
						<td style="width:60%; text-align:center;color:#0088CC;" title="通过查看图片查看检测过程">过程描述</td>
						<td style="width:15%; text-align:center;color:#0088CC;">结果</td>
						<td style="width:10%; text-align:center;color:#0088CC;">检测时间</td>
					  </tr>
				<%
				  List<AutoCheckDependResult> tmpList = map.get(isolationType.toString());
				    for (AutoCheckDependResult result : tmpList) {
				      String stepStatus = result.getStepStatus();
				      if (stepStatus.equals("true")) {
				        stepStatus = "成功";
				      } else if (stepStatus.equals("false")) {
				        stepStatus = "<strong style='color:red'>失败</strong>";
				      }

				      String stepMsg = result.getStepMessage();
				      if (stepMsg.indexOf(ConstantParameters.IMAGE_PLACEHODER_END) >= 0) { //处理显示的日志
				        while (stepMsg.indexOf(ConstantParameters.IMAGE_PLACEHODER_START) >= 0
				            || stepMsg.indexOf(ConstantParameters.IMAGE_PLACEHODER_END) >= 0) {
				          int startIndex = stepMsg
				              .indexOf(ConstantParameters.IMAGE_PLACEHODER_START);
				          int endIndex = stepMsg
				              .indexOf(ConstantParameters.IMAGE_PLACEHODER_END);
				          System.out.println(stepMsg.substring(startIndex, endIndex
				              + ConstantParameters.IMAGE_PLACEHODER_END.length()));
				          System.out.println(stepMsg.substring(0, startIndex)
				              + stepMsg.substring(endIndex
				                  + ConstantParameters.IMAGE_PLACEHODER_END.length(),
				                  stepMsg.length()));
				          String picName = stepMsg.substring(startIndex
				              + ConstantParameters.IMAGE_PLACEHODER_START.length(),
				              endIndex);
				          //url = "http://hiphotos.baidu.com/baidu/pic/item/8546bd003af33a8727f50057c65c10385243b566.jpg";
				          //暂时没加入用户名密码，未来会加入的
				          String url = "ftp://" + StartUpParamWraper.getFtpIp() + "/"
				              + StartUpParamWraper.getFtpPath() + "/" + picName;
				          //dependself 是一个不存在的css，js获取对象时使用
				          stepMsg = stepMsg.substring(0, startIndex)
				              + "<a href='"
				              + url
				              + "' class='dependself'>查看图片</a>"
				              + stepMsg.substring(endIndex
				                  + ConstantParameters.IMAGE_PLACEHODER_END.length(),
				                  stepMsg.length());
				        }
				      }
							String strDate = sdf.format(result.getCollectTime());
				%>
					    <tr>
					    	<td style="width:15%; text-align:center;"><%=result.getStepType().getDesc()%></td>
							<td style="width:60%; text-align:left;word-break:break-all"><%=stepMsg%></td>
							<td style="width:15%; text-align:center;"><%=stepStatus%></td>
							<td style="width:10%; text-align:center;"><%=strDate%></td>
					    </tr>
					    <%
					      } //second for
					    %>
						<tr>
							<td style="text-align: center;" colspan="4">&nbsp;</td>
						</tr>					  
					  <%
					  					    }
					  					  %>
			</tbody>
		</table>
	</div>
</body>
</html>