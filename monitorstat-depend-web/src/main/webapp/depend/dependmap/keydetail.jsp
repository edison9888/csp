<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.*"%>
<%@page import=" com.taobao.csp.depend.util.JspFormatUtil"%>
<%@page import="com.taobao.monitor.common.po.CspMapKeyInfoPo"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<%%>
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<style type="text/css">
td {
	word-break:break-all;
}
</style>
<title>�ӿڵ��ñ���(beta)</title>
<%
	String appName = (String)request.getAttribute("appName");
	String keyName = (String)request.getAttribute("keyName");
	String id = (String)request.getAttribute("id");
	CspMapKeyInfoPo info = (CspMapKeyInfoPo)request.getAttribute("info");
%>
</head>
<body>
<%
	if(info == null) {
		out.println("��ѯ��key�����ڣ�");
	} else {
%>
<form class="form-horizontal">
  <input type="hidden" id="id" value="<%=info.getId()%>">
  <div class="control-group">
    <label class="control-label" for="appName">Ӧ����</label>
    <div class="controls">
      <input type="text" id="appName" value="<%=info.getAppname()%>" readonly="readonly">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="keyname">key</label>
    <div class="controls">
      <input type="text" id="keyname" value="<%=info.getKeyname()%>" readonly="readonly">
    </div>
  </div>
  <div class="control-group">
  	<label class="control-label" for="controlType">��������</label>
    <div class="controls">
      <input id="controlType" value="<%=JspFormatUtil.getControlString(info.getControlType())%>" readonly="readonly">
      <input id="controlType" value="<%=info.getControlType()%>" readonly="readonly" type="hidden">
    </div>
  </div>  
  <div class="control-group">
    <label class="control-label" for="keyLevel">key����</label>
    <div class="controls">
      <select id="keyLevel" name="keyLevel">
      	<%
      		for(int i=0; i<4; i++) {
      			String selected = "";
      			if(i == info.getKeyLevel()) {
      				selected = "selected='selected'";
      			}
      			out.println("<option value='" + i + "' " + selected + ">P" + i + "��key</option>");
      		}
      	%>
      </select>    
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="isBlack">�Ƿ������</label>
    <div class="controls">
      <select id="isBlack" name="isBlack">
      	<%
      		int isBlack = info.getIsBlack();
      	%>
        <option value="1" <% if(isBlack == 1) out.println("selected='selected'");%>>��</option>
      	<option value="0" <% if(isBlack == 0) out.println("selected='selected'");%>>��</option>
      </select>
    </div>
  </div>        
  <div class="control-group">
    <div class="controls">
      <button type="submit" class="btn">�ύ�޸�</button>
    </div>
  </div>
</form>
<%		
	}
%>
</body>
</html>