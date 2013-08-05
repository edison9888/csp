<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataPo"%>
<%@page import="com.taobao.monitor.web.util.UrlCode"%>
<%@page import="com.taobao.monitor.web.cache.KeyCache"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>应用监控系统-查询应用key</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>


<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
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
}
.report_on{background:#bce774;}

</style>
<script type="text/javascript">

function submitMessage(){

	if(window.confirm('确认更新吗?')){
		return true;
	}
	return false;
}

</script>
</head>

<body>
<jsp:include page="../head.jsp"></jsp:include>
<%
request.setCharacterEncoding("gbk");
String keyId = request.getParameter("keyId"); 
KeyPo po = KeyCache.get().getKey(Integer.parseInt(keyId));
String alias = request.getParameter("alias");
String action = request.getParameter("action");
String keyType = request.getParameter("keyType");
String desc = request.getParameter("desc");
if("update".equals(action)){
	po.setAliasName(alias);
	po.setKeyType(keyType);
	po.setFeature(desc);
	boolean b = KeyAo.get().updateKeyInfo(po);
	
%>
<font size="+2" color="red"><%if(b){out.print("成功");}else{out.print("失败!");} %></font>
<input type="button" value="返回列表" onclick="location.href='../alarm/manage_key.jsp'">
<%
}else{
%>
<jsp:include page="../left.jsp"></jsp:include>
<form action="./keyInfo_update.jsp" method="post" onsubmit="return submitMessage()">
<table width="100%">
	<tr >
		<td align="center">
			<table width="500">
				<tr>
					<td >key名称:</td>
					<td ><%=po.getKeyName() %></td>
				</tr>
				<tr>
					<td>别名:</td>
					<td><input type="text" value="<%=po.getAliasName()==null?"":po.getAliasName() %>" name="alias" size="100"/></td>
				</tr>
				<tr>
					<td>类型:</td>
					<td><input type="text" value="<%=po.getKeyType()==null?"":po.getKeyType() %>" name="keyType" size="100"/></td>
				</tr>
				<tr>
					<td>说明:</td>
					<td><textarea rows="10" cols="50" name="desc"><%=po.getFeature()==null?"":po.getFeature() %></textarea></td>
				</tr>
				
				<tr><td></td>
					<td>
					<input type="hidden" value="update" name="action"/>
					<input type="hidden" value="<%=po.getKeyId() %>" name="keyId"/>
					<input type="submit" value="提交"/><input type="button" value="关闭" onclick="window.close()"/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
	
</form>
<jsp:include page="../buttom.jsp"></jsp:include>
<%} %>
</body>
</html>