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
<title>����������ҳ��</title>
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
				<span>ִ��<font><%=opsName%></font>������Ӧ��<font><%=targetOpsName%></font>ǿ������������
				</span>
			</h3>
		</div>
		<table class="table table-striped table-condensed table-bordered"
			id="table1" style="table-layout: fixed��">
			<tbody>
				<tr>
					<th colspan="2" style="font-size: small;">���˵��</th>
				</tr>
				<tr>
					<td style="width: 15%; text-align: center; color: #0088CC;">Ӧ������</td>
					<td style="width: 85%; text-align: left;">ִ�м���Ӧ��<%=opsName%>,������Ӧ��<%=targetOpsName%></td>
				</tr>
				<tr>
					<td style="width: 15%; text-align: center; color: #0088CC;">�����Լ�鷽ʽ</td>
					<td style="width: 85%; text-align: left;">
						<%=checkupType%> &nbsp;&nbsp;
						<%
						  if (checkupType != null && checkupType.equals("selenium")) {
						%>
								<a href="#" onclick="showGallery()" title="�����ʾͼƬ">��ʾ��׼��ⲽ��ͼƬ</a>&nbsp;&nbsp;<a href="#" onclick="closeGallery()">�رս�ͼ</a>							  
							  <%
							  							    }
							  							  %>
					</td>
				</tr>
				<tr>
					<td style="width: 15%; text-align: center; color: #0088CC;">URl˵��</td>
					<td style="width: 85%; text-align: left;">�����ⲽ���е�URL��daily����URL��Ϊǿ��������⻷��URL���޸ı���Host�ļ�����Է��ʡ�
					<a href="http://baike.corp.taobao.com/index.php/12%E4%B8%AA%E5%BA%94%E7%94%A8%E5%8F%8A%E5%85%B6%E5%88%97%E8%A1%A8#Host.E4.BF.AE.E6.94.B9" target="_blank">�鿴host</a></td>
				</tr>				
				<tr>
					<td style="width: 15%; text-align: center; color: #0088CC;">�����˵��</td>
					<td style="width: 85%; text-align: left;">�Ƽ�ʹ��Chrome��IE8���������</td>
				</tr>				
			</tbody>
		</table>
		<%
		  if (checkupType != null && checkupType.equals("selenium")) {	//ֻ��Selenium��ʱ����ʾ���
		%>
			<div align="center" id="gallery_div">
				<h2>��׼��ⲽ�裺<a href="#" onclick="closeGallery()">�رս�ͼ</a></h2>
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
			id="table1" style="table-layout: fixed��">
			<tbody>
				<%
				  for (IsolationType isolationType : IsolationType.values()) {
				    if (!map.containsKey(isolationType.toString()))
				      continue;
				%>
					  <tr><th colspan="4" style="font-size: small;">��ⷽʽ:<%=isolationType.getDesc()%></th></tr>
					  <tr>
						<td style="width:15%; text-align:center;color:#0088CC;">����</td>
						<td style="width:60%; text-align:center;color:#0088CC;" title="ͨ���鿴ͼƬ�鿴������">��������</td>
						<td style="width:15%; text-align:center;color:#0088CC;">���</td>
						<td style="width:10%; text-align:center;color:#0088CC;">���ʱ��</td>
					  </tr>
				<%
				  List<AutoCheckDependResult> tmpList = map.get(isolationType.toString());
				    for (AutoCheckDependResult result : tmpList) {
				      String stepStatus = result.getStepStatus();
				      if (stepStatus.equals("true")) {
				        stepStatus = "�ɹ�";
				      } else if (stepStatus.equals("false")) {
				        stepStatus = "<strong style='color:red'>ʧ��</strong>";
				      }

				      String stepMsg = result.getStepMessage();
				      if (stepMsg.indexOf(ConstantParameters.IMAGE_PLACEHODER_END) >= 0) { //������ʾ����־
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
				          //��ʱû�����û������룬δ��������
				          String url = "ftp://" + StartUpParamWraper.getFtpIp() + "/"
				              + StartUpParamWraper.getFtpPath() + "/" + picName;
				          //dependself ��һ�������ڵ�css��js��ȡ����ʱʹ��
				          stepMsg = stepMsg.substring(0, startIndex)
				              + "<a href='"
				              + url
				              + "' class='dependself'>�鿴ͼƬ</a>"
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