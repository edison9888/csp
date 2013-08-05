<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>���ļ��-��������������</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/js/mult/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/js/mult/assets/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/js/mult/assets/prettify.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/js/mult/jquery-ui.css" />
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/global.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery-1.4.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/mult/assets/prettify.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/mult/jquery.multiselect.js"></script>
<style>
td{
  height:29px;
  font-weight:bold;
}
</style>
<script type="text/javascript">
$(function(){
	$("#rcIdArray").multiselect();
});
</script>
</head>

<body onload="prettyPrint();">
<form id="useCaseForm" name="useCaseForm" 
<c:choose><c:when test="${operate=='update'}">action="updateUseCase.do" </c:when>
<c:otherwise>action="addUseCase.do"</c:otherwise>
</c:choose>method="post">
<c:if test="${operate == 'update'}">
<input type="hidden" id="useCaseId" name="useCaseId" value="${useCase.useCaseId}"/>
</c:if> 
<table style="width:99%;margin-top:10px;margin-left:auto;margin-right:auto;" border="1" cellspacing="0" cellpadding="0" bordercolor="#4F81BD">
 <tr>
  <td width="10%" align="right">��������<font color="red">(*)</font>��</td>
  <td><input type="text" id="useCaseAlias" name="useCaseAlias" value="${useCase.useCaseAlias}"/>
  <span id="useCaseAliasError"><font color="blue">�磺��¼</font></span>
  </td>
 </tr>
 <tr>
  <td width="10%" align="right">������������<font color="red">(*)</font>��</td>
  <td><input type="text" id="useCaseName" name="useCaseName" value="${useCase.useCaseName}" readonly="readonly"/>
  <span id="useCaseNameError"><font color="blue">������д,ϵͳ�Զ�����</font></span>
  </td>
 </tr>
 <tr><td align="right">Selenium RC<font color="red">(*)</font>��</td>
  <td>
    <!--  <select id="rcId" name="rcId" onchange="ajaxRcBrowsers(this.value);"> -->
    <select title="Basic example" multiple="multiple" name="rcIdArray"  id="rcIdArray" size="5">
      <c:forEach items="${rcList}" var="entity">
        <option value="${entity.id}" 
         <c:forEach items="${useCase.rcIdArray}" var="rcar">
          <c:if test="${entity.id == rcar}">selected="true"
          </c:if>
         </c:forEach>>
        ${entity.seleniumRcIp}:${entity.seleniumRcPort}</option>
      </c:forEach>
    </select>
    <span id="rcIdError"><font color="blue">ѡ���������е�SeleniumRc�ͻ���</font></span>
  </td>
 </tr>
 <tr><td align="right">���������<font color="red">(*)</font>��</td>
  <td>
    <select id="browser" name="browser">
     <option value="Mozilla Firefox">Mozilla Firefox</option>
    </select>
    <span id="browserError"><font color="blue"></font></span>
  </td>
 </tr>
 <tr>
  <td align="right">Base Url<font color="red">(*)</font>��</td>
  <td>
    <input type="text" style="width:400px;" id="baseUrl" name="baseUrl" value="${useCase.baseUrl}" onblur="checkBaseUrl();"/>
    <span id="baseUrlError"><font color="blue">������վ��Base Url�磺http://www.taobao.com</font></span>
  </td>
 </tr>
 <tr>
  <td align="right">��������<font color="red">(*)</font>��</td>
  <td>
    <textarea rows="20" cols="80" id="useCaseSource" name="useCaseSource" 
    style="width:100%;height:480px" onblur="inputUseCaseName();">${useCase.useCaseSource}</textarea>
    <span id="useCaseSourceError"><font color="blue">����д����������</font></span>
  </td>
 </tr>
</table>
<div align="center" style="padding-top:2px;">
  <c:choose>
  <c:when test="${operate=='view'}"></c:when>
  <c:otherwise>
  <input type="button" value="�� &nbsp;&nbsp;��" onclick="subForm();" style="width:50px;height:20px;" class="macButton"/>
  </c:otherwise>
  </c:choose>
</div>
</form>
<script type="text/javascript">
$("#reportName").focus();
function subForm(){
	
	if(checkUseCaseAlias() && checkUseCaseName() && checkRcServer()
			 && checkBrowser() && checkBaseUrl() && checkUseCaseSource()){
		if("${operate}"=='update'){
			document.useCaseForm.submit();
		}else{
			ajaxCheckUCAdaptName(className);	
		}
	}
}

function checkUseCaseAlias(){
	var reportName = $("#useCaseAlias").val();
	if(reportName == ''){
		$("#useCaseAliasError").html("�������Ʋ���Ϊ��");
		$("#useCaseAliasError").css("color", "red");
		return false;
	}else{
		$("#useCaseAliasError").html("");
		return true;
	}
}

function checkUseCaseName(){
	var reportName = $("#useCaseName").val();
	if(reportName == ''){
		$("#useCaseNameError").html("���������Ʋ���Ϊ��");
		$("#useCaseNameError").css("color", "red");
		return false;
	}else{
		$("#useCaseNameError").html("");
		return true;
	}
}
function checkRcServer(){
	var rcId = $("#rcIdArray").val();
	if(rcId == ''){
		$("#rcIdError").html("Selenium Server����Ϊ��");
		$("#rcIdError").css("color", "red");
		return false;
	}else{
		$("#rcIdError").html("");
		return true;
	}
}
function checkBrowser(){
	var browser = $("#browser").val();
	if(browser == ''){
		$("#browserError").html("���������Ϊ��");
		$("#browserError").css("color", "red");
		return false;
	}else{
		$("#browserError").html("");
		return true;
	}
}
function checkBaseUrl(){
	var url = $("#baseUrl").val();
	if(url == ''){
		$("#baseUrlError").html("Base Url����Ϊ��");
		$("#baseUrlError").css("color", "red");
		return false;
	}else{
		$("#baseUrlError").html("");
		return true;
	}
}
function checkUseCaseSource(){
	var useCaseSource = $("#useCaseSource").val();
	if(useCaseSource == ''){
		$("#useCaseSourceError").html("���������ݲ���Ϊ��");
		$("#useCaseSourceError").css("color", "red");
		return false;
	}else{
		$("#useCaseSourceError").html("");
		return true;
	}
}
function ajaxRcBrowsers(rcId, selectKey){
    jQuery.ajax({
		url:"ajaxRcBrowsers.do",
	    type: 'POST',
	    dataType: 'html',
	    data:"rcId="+rcId+"&selectKey="+selectKey,
	    timeout: 200000,
	    error: function(){},
	    success: function(data){
		    eval(data);
	    }
	})

}
//ajaxRcBrowsers($("#rcId").val(), "${useCase.browser}");
function inputUseCaseName(){
	var content = $("#useCaseSource").val();
	begIndex = content.indexOf("class");
	braceIndex = content.indexOf("{");
	impleIndex = content.indexOf("implements");
	extendIndex = content.indexOf("extends");
	endIndex = -1;
	if(impleIndex != -1)endIndex = impleIndex;
	if(extendIndex != -1 && extendIndex < endIndex)endIndex = extendIndex;
	if(braceIndex != -1 &&  braceIndex < endIndex)endIndex = braceIndex;
	if(begIndex != -1 && endIndex != -1){//��ȡ����
		className = content.substring(begIndex+5, endIndex);
		className = className.replace(/\s/ig,'');
	}
	if(className == ''){
		$("#useCaseSourceError").html("��������﷨����");
		$("#useCaseSourceError").css("color", "red");
	}else{
		//����ajax���������Ƿ����ʹ��
		//ajaxCheckUCAdaptName(className);
		$("#useCaseName").val(className);
		$("#useCaseNameError").html("");
		$("#useCaseSourceError").html("");
	}
}
function ajaxCheckUCAdaptName(useCaseName){
    jQuery.ajax({
		url:"ajaxCheckUCAdaptName.do",
	    type: 'POST',
	    dataType: 'html',
	    data:"useCaseName="+useCaseName,
	    timeout: 200000,
	    error: function(){},
	    success: function(data){
		    if(data==0){
		    	document.useCaseForm.submit();
		    }else{
		    	$("#useCaseSourceError").html("�������������ѱ�ʹ�ã���ѡ���������֣�");
		    	$("#useCaseSourceError").css("color", "red");
		    	return false;
		    }
	    }
	})
}

</script>
</body>
</html>