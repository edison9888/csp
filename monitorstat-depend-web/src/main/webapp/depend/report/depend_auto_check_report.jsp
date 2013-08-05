<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.csp.depend.po.report.StrongWeakAutoCheckReport"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" >
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>ǿ�������Զ���ⱨ��</title>
<style type="text/css">
*{
	font-size:13px;
}
html,body{
	margin:0;
	padding:0;
}
body{
    font-family:Arial,Helvetica,"Nimbus Sans L",sans-serif;
	background:#fafafa;
	text-align:center;
}
h3{
	font-size:20px;
	font-weight:bold;
	margin:12px 0;
}
h4{
	font-size:16px;
	font-weight:bold;
	color:#CC0000;
	margin:10px 0;
}
h5{
	font-size:14px;
	font-weight:bold;
	color:#990000;
	margin:5px 0;
}

.mian_body {
		width:100%;
		float: left;
		background-color: #E7EEFE;
		border: 1px solid #ccc;
		border-radius: 5px;
		-moz-border-radius: 5px;
		-webkit-border-radius: 5px;
	}
	
	.table_comm {
		width:100%;
		float: left;
		background-color: #E7EEFE;
		border: 1px solid #ccc;
		border-radius: 5px;
		-moz-border-radius: 5px;
		-webkit-border-radius: 5px;
	}
	
	.table_comm  td {
	border:1px solid #99BBE8;
	background: #fff;
	color: #4f6b72;
}

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
border: 1px solid #DDDDDD;
}

h2{
	padding-left: 50px;
}

thead{
	background: #fff;
	color: #4f6b72;
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
	font-family: "����";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath()%>/statics/images/4_17.gif);
}
img {
cursor:pointer;
}
.report_on{background:#bce774;}

.firstTitleTd {
	font-weight: bold;	
	background: #1FB5D0;
	font-size: 100%;
}

.secondTitleTd {
	background: #1FB5D0;
}
</style>

<%
Map<String, List<StrongWeakAutoCheckReport>> dependMap = (Map<String, List<StrongWeakAutoCheckReport>>) request
      .getAttribute("dependMap");
String[] appArray = (String[]) request.getAttribute("appArray");
if(dependMap == null) {
  dependMap =  new HashMap<String, List<StrongWeakAutoCheckReport>>();
}
if(appArray == null) {
  appArray = new String[0];
}
%>
<%!

//����status ������ɫ
String getColorByStatus(String status) {
  
  if(status != null && status.equals("ǿ")) {
    return "red";
  } 
  return "green";
}

//����status ������ɫ
String getTextByConfigFlag(String text, boolean isConfig) {

  if(isConfig) {
	return text;
  } else {
    return "-";	//"-"��ʾ�ű�δ����
  }
}

%>
</head>
<body>
	<h3>����˵��</h3>
	<table width="90%" class="table table-striped table-bordered table-condensed">
	<thead>
	<tr>
		<td class="firstTitleTd" style="background-color: #A8E0ED;"><h2>&nbsp;���˵����<a href="http://10.232.135.198:8080/depend/dailycheck.do?method=gotoIndexPage">ǿ���������ϵͳ��2�׻�����</a></h2></td>
		<td class="firstTitleTd" style="background-color: #A8E0ED;"><h2>&nbsp;����</h2></td>
	</tr>
	<tr>
		<td>�����</td>
		<td align="left">&nbsp;"<span style='color:red;'>�쳣</span>"��ʾ"<strong>Ԥ��ǿ��</strong>"��ʵ��"<strong>ǿ����֤���</strong>"��һ�¡�"<strong>ǿ����֤���</strong>"��һ��Ϊ"ǿ"���ʾ<span style="color: red;">ǿ����</span>��
		</td>
	</tr>		
	<tr>
		<td>��������</td>
		<td align="left">&nbsp;��Ӧ������ǰ�����ε�Ӧ�ü������ͨ�š�</td>
	</tr>
	<tr>
		<td>��������</td>
		<td align="left">&nbsp;��Ӧ�óɹ����������ε�Ӧ�ü������ͨ�š�</td>
	</tr>
	<tr>
		<td>�ӳ�����</td>
		<td align="left">&nbsp;������Ӧ��ǰ������Ӧ�ü������ͨ�ţ�ģ�����ٽϲ�����绷����</td>
	</tr>
	<tr>
		<td>�ӳ�����</td>
		<td align="left">&nbsp;������Ӧ��ǰ������Ӧ�ü������ͨ�ţ�ģ�����ٽϲ�����绷����</td>
	</tr>
	<tr>
		<td>������</td>
		<td align="left">&nbsp;����ͨ��������"<strong>�鿴��ϸ</strong>"�ĳ����Ӳ鿴���ľ�����̣��м���ͼ�;��岽��������
		</td>
	</tr>		
	<tr>
		<td class="firstTitleTd" style="background-color: #A8E0ED;"><h2>��ⷽʽ</h2></td>
		<td class="firstTitleTd" style="background-color: #A8E0ED;"><h2>����</h2></td>
	</tr>	
	<tr>
		<td>URL���</td>
		<td align="left">&nbsp;���ʱ�����url�������ض���������⡣</td>
	</tr>
	<tr>
		<td>Selenium�ű����</td>
		<td align="left">&nbsp;ͨ��Selenium�����������ģ���˻����̣�����������������Ϊ���ɹ���
		<a href="<%=request.getContextPath()%>/show/reportaction.do?method=showStandardCheckPicture" target="_blank" style="display: none;">�鿴ȫ�����ű�</a></td>
	</tr>
	<tr>
		<td>����׼</td>
		<td align="left">&nbsp;�����λ��ӳٲ�����ͨ��URL��Selenium�ű�������������ļ�⣬����ΪӦ��֮��������ϵΪ<span style="color:green;">������</span>������Ϊ<span style="color: red;">ǿ����</span>��Ĭ��ֱ��DB��Ϊ<span style="color: red;">ǿ����</span>������顣
		</td>
	</tr>
	</thead>
	</table>
	<br/>
	<h3>Ӧ��������ϵΪ${strDaysBefore}&nbsp;~&nbsp;${selectDate}�Ļ������ݣ��Աȵ�����Ϊ${strPreDate}�����ݣ�</h3>
	<%
		for(String appName: appArray) {	//map�б�
		  int i =1;
		  List<StrongWeakAutoCheckReport> poList = dependMap.get(appName); 
	%>
	<table width="90%" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<td colspan="11" class="firstTitleTd" style="background-color: #A8E0ED;">
				<h2>Ӧ������:<%=appName%></h2>
				</td>
			</tr>
			<tr>
				<td rowspan="2" align="center" width="4%">���</td>
				<td rowspan="2" align="center" width="15%">����Ӧ��</td>
				<td rowspan="2" align="center" width="7.5%">Ӧ������</td>
				<td rowspan="2" align="center" width="7.5%">�����</td>
				<td rowspan="2" align="center" width="10%">Ԥ��ǿ��</td>
				<td colspan="4" align="center" width="36%">ǿ����֤���</td>
				<td rowspan="2" align="center" width="10%">���ű�</td>
				<td rowspan="2" align="center" width="10%" title="��ͼ������"><strong>������</strong></td>
			</tr>
			<tr>
				<td align="center" width="9%">��������</td>
				<td align="center" width="9%">��������</td>
				<td align="center" width="9%">�ӳ�����</td>
				<td align="center" width="9%">�ӳ�����</td>
			</tr>
		</thead>
		<tbody>
			<%
				for(StrongWeakAutoCheckReport po: poList) {
				  String startPreventIntensity = po.getCurCheckResult().get("startPreventIntensity");
				  String runPreventIntensity = po.getCurCheckResult().get("runPreventIntensity");
				  String startDelayIntensity = po.getCurCheckResult().get("startDelayIntensity");
				  String runDelayIntensity = po.getCurCheckResult().get("runDelayIntensity");
				  
				  boolean isReduce = po.getTargetAppStatus() == StrongWeakAutoCheckReport.STATUS_REDUCE;
				  boolean isAdd = po.getTargetAppStatus() == StrongWeakAutoCheckReport.STATUS_ADD;
				  boolean isCheckConfig = po.isCheckConfig();	//�ű��Ƿ�����
			%>	  
				<tr>
					<td align="center"><%=i++%></td>
					<td 
					<%
						if(isReduce)
							out.print("style='background-color:#C0C0C0'");
					 %>
					 ><%=po.getTargetOpsName()%>
					<% 
						if(isAdd)
						  out.print("<font color='red'>����</font>");
						else if(isReduce)
					  	out.print("<font color='red'>����</font>");
					%>
					</td>
					<td align="center">
					<%
					if(po.getTargetAppType() != null)
					  out.print(po.getTargetAppType());
					else 
					  out.print("<strong>δ֪</strong>");
					%>
					</td>
					<td align="center">
					<%
					String normal = "<span style='color:green;'>����</span>";
					String valid = "<span style='color:red;'>�쳣</span>";
					if(!isCheckConfig) {
						out.println(normal);
					} else {
					  String expectDepIntensity = po.getExpectDepIntensity(); 
					  if(expectDepIntensity.equals("��")) {
					    if("��".equals(startPreventIntensity) && "��".equals(runPreventIntensity) && "��".equals(startDelayIntensity) && "��".equals(runDelayIntensity)) {
					      out.println(normal);
					    } else {
					      out.println(valid);
					    }
					  } else {
					    if("ǿ".equals(startPreventIntensity) || "ǿ".equals(runPreventIntensity) || "ǿ".equals(startDelayIntensity) || "ǿ".equals(runDelayIntensity)) {
					      out.println(normal);
					    } else {
					      out.println(valid);
					    }
					  }
					}					
					%>
					</td>
					<td align="center"><font color="<%=getColorByStatus(getTextByConfigFlag(po.getExpectDepIntensity(),isCheckConfig))%>"><%=getTextByConfigFlag(po.getExpectDepIntensity(),isCheckConfig)%></font></td>
					<td align="center">
						<font color="<%=getColorByStatus(startPreventIntensity)%>"><%=getTextByConfigFlag(startPreventIntensity,isCheckConfig)%></font>
					</td>
					<td align="center">
						<font color="<%=getColorByStatus(runPreventIntensity)%>"><%=getTextByConfigFlag(runPreventIntensity,isCheckConfig)%></font>
					</td>
					<td align="center">
						<font color="<%=getColorByStatus(startDelayIntensity)%>"><%=getTextByConfigFlag(startDelayIntensity,isCheckConfig)%></font>
					</td>
					<td align="center">
						<font color="<%=getColorByStatus(runDelayIntensity)%>"><%=getTextByConfigFlag(runDelayIntensity,isCheckConfig)%></font>
					</td>
					<td align="center">
					<%
						if(isCheckConfig) {
						  %>
						  <a href="http://10.232.135.198:8080/depend/config.do?method=gotoCheckupConfig&opsName=<%=appName%>&targetOpsName=<%=po.getTargetOpsName()%>" target="_blank">������</a>
						  <%						  
						} else {
						  %>
						  <a style="color: red" href="http://10.232.135.198:8080/depend/config.do?method=gotoCheckupConfig&opsName=<%=appName%>&targetOpsName=<%=po.getTargetOpsName()%>" target="_blank">δ����</a>
						  <%
						}
					%>
					</td>
					<td align="center">
						<%
						if(!isCheckConfig) {
						  out.println("-");
						} else {
						  %>
						<a href="http://10.232.135.198:8080/depend/checkupdepend.do?method=showCheckResult&opsName=<%=appName%>&targetOpsName=<%=po.getTargetOpsName()%>" target="_blank">�鿴��ϸ</a>						  
						  <%
						}
						%>
					</td>
				</tr>
		<%
			}	//end of inside for
		%>
		</tbody>
	</table>
		<%}	//end of outside for%>
	<br/>&nbsp;
	<br/>&nbsp;
</body>
</html>