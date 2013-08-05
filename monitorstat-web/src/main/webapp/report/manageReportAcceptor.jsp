<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>���ļ��-��ӱ���ģ��</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/global.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery-ui-1.8.custom.min.js"></script> 
<link type="text/css" href="<%=request.getContextPath() %>/statics/datatable/css/demo_page.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/datatable/css/demo_table.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/datatable/js/jquery.dataTables.js"></script>
<style>
td{
  height:34px;
}
table#example{
  bordercolor:#4f81bd;
}
</style>
<script type="text/javascript">
if("${add}"=="add"){
 window.parent.location.reload(true);
}
</script>
</head>
<body style="top:0px;">
<form id="addReportAcceptorForm" name="addReportAcceptorForm" action="addReportAcceptor.do" method="post">
<input type="hidden" name="addressList" id="addressList"/>
<input type="hidden" name="reportId" id="reportId"/>
<input type="hidden" name="type" id="type" value="email"/>
<input type="hidden" name="paramValue" id="paramValue"/>
<input type="hidden" name="reportType" id="reportType" value="${report.reportType}"/>
<table style="width:100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#B0C4DE">
 <tr>
  <td colspan="3" width="20%" align="left" style="height:20px;">
   <div class="small_title" style="margin:0;padding:0;">
   <font style="color:blue;font-weight:900;font-size:14px;">��ӱ�������ˣ�</font>
   </div>
  </td>
 </tr>
 <tr>
      <td width="15%" valign="top">
      <strong>�ʼ���ַ��</strong>
      <div style="color:blue;align:center">
      <br></br>
        <img align="absmiddle" src="<%=request.getContextPath()%>/statics/images/laba.jpg"/> ������ʾ��<br></br>
        1. ��ӽ��ձ��������ַ.<br/>
        <c:choose>
         <c:when test="${report.reportType == 1}">
         2. ѡ�������ַ���ò����������ݵ�Ӧ��.
         </c:when>
         <c:otherwise>
         2. ѡ�������ַ�����û��Զ������.
         </c:otherwise>
        </c:choose>
      </div>
      </td>
      <td width="70%">
      <input type="text" name="email" id="email" size="47" />&nbsp;
	  <input type="button" name="emailadd" value="�ֹ����" onclick="addEmail()" class="macButton"/>
		<span id="emailMessage" style='color:red'></span><span id="errorMsg"></span><br/>
		<select name="emailList" id="emailList" multiple="multiple" style="width:294px;height:300px">
			<c:forEach items="${acceptors}" var="value">
			  <option value="${value}"
			   <c:forEach items="${addressArray}" var="selectAddress">
			     <c:if test="${selectAddress == value}">
			     selected="selected"
			     </c:if>
			   </c:forEach>
			  >${value}</option>
			</c:forEach>
		</select>
		&nbsp;<input type="button" name="emailremove" value="ɾ   ��" onclick="removeEmail()" class="macButton"/>
		<div id="emailListMsg" style="font-size:12px;color:red;"></div>
      </td>
      <td width="10%" align="center">
       <input type="button" value="�� &nbsp;&nbsp;��" onclick="subForm();" style="width:100%;height:320px;"/>
      </td>
 </tr>
</table>
<br/>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width:99%;">
<c:choose>
<c:when test="${report.reportType == 1}"><!-- 1:����Ӧ��  0:�û��Զ���-->
<div id="dialog">
<table align="center" id="example" border="1">
	<thead>
	<tr>
	  	<th width="50" align="center">
	  	<input name='chkAll' type='checkbox' id='chkAll' value='chkAll'/></th>
		<th align="center">Ӧ����</th>
	    <th align="center">Ӧ��������</th>
	    <th align="center">Ӧ��OPS����</th>
  </tr>  
  </thead>
  <tbody>
	<c:forEach items="${listApp}" var="valueEntry">
	<!--  <c:set var="appId" value="${valueEntry.appId}+','"></c:set> -->
       	<tr>
       	    <td align="center">
       	    <input type="checkbox" id="selectId" name='selectId' class='selectId' value="${valueEntry.appId}"/>      
       	    </td>
			<td align="center">${valueEntry.appName}</td>
			<td align="center">${valueEntry.groupName}</td>
			<td align="center">${valueEntry.opsName}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</div>
</c:when>
<c:otherwise>
<table style="width:100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#B0C4DE">
 <tr>
  <td colspan="2" width="20%" align="left" style="height:20px;">
   <div class="small_title" style="margin:0;padding:0;">
   <font style="color:blue;font-weight:900;font-size:14px;">�û��Զ��屨���������</font>
   </div>
  </td>
 </tr>
 <tr><td colspan="2"><font style="color:blue;font-size:12px;">&nbsp;&nbsp;&nbsp;&nbsp;����ģ��Url��${report.path}</font></td></tr>
 <tr>
 <td colspan="2">
    <font style="color:blue;font-weight:900;font-size:12px;">&nbsp;&nbsp;&nbsp;&nbsp;�û��Զ��������ֵ��</font>
    <c:if test="${empty params}"><font style="color:red;">���û��Զ������</font></c:if>
    <c:if test="${!empty params}"><font style="color:red;">��ʾ������ֵȫ��Ϊ�գ���ɾ���ʼ������˺ţ�</font></c:if>
 </td>
 </tr>
<c:forEach items="${params}" var="entity">
 <tr>
  <td style="width:10%;align:center;color:blue;font-weight:bold">${entity.key}=</td>
  <td style="width:90%;"><input type="text" id="paramTextValue" name="paramTextValue" value="${entity.value}" style="width:80%"/></td>
 </tr>
</c:forEach>
</table>
</c:otherwise>
</c:choose>
</div>
<div align="center" style="padding-top:2px;">
  <input type="button" value="�� &nbsp;&nbsp;��" onclick="subForm();" style="width:50px;height:20px;" class="macButton"/>
</div>
</form>
<script type="text/javascript">
$(document).ready(function() {
    $('#example').dataTable( {//����
        "bPaginate": false,//��ҳ��ť
        "bLengthChange": true,//ÿ����ʾ��¼��
        "bFilter": true,//������
        "bSort": true,//����
        "bInfo": false,//Showing 1 to 10 of 23 entries  �ܼ�¼��ûҲ��ʾ���ٵ���Ϣ
        "bAutoWidth": true } );
});
function subForm(){
	var addressList = [];
	$("#emailList option:selected").each(function(){
������	  addressList.push($(this).val());
��     })
    if(addressList.length == 0){
        $("#errorMsg").html("��ѡ�񱨱���յ������ַ!");
        $("#emailMessage").html("");
        $("#errorMsg").css("color", "red");
    	return ;
    }else{
    	$("#errorMsg").html("");
    }
	$("#addressList").val(addressList.join(","));
	$("#reportId").val(${reportId});
	if(${report.reportType}==0){//�û��Զ���
		var value = "";
		var tag = true;//����ȫ��Ϊ�յı�־
		$("input[type=text][name=paramTextValue]").each(function(){
			if($(this).val() == ''){
				value += "null,"
			}else{
				tag = false;
				value += $(this).val()+","
			}��
	����})
	   if(tag){
		   $("#paramValue").val("");	
	   }else{
		   $("#paramValue").val(value);	
	   }
	}else{
		var flag = false;
		$("input[type=checkbox][name=selectId]").each(function(){
			if($(this).attr("checked") == true){
				flag = true;
			}
		});
		if(!flag){
			 if(confirm('ȷ����ѡ��Ӧ��?�����˽���������!')){
			 }else return;
		}
	}
	document.addReportAcceptorForm.submit();	
}

$(function(){
	// ����һ����ѡ��������ȫѡ/ȫ��ѡ
	$("#chkAll").click(function(){
		if(this.checked){ //�����ǰ����Ķ�ѡ��ѡ��
			$('input[type=checkbox][name=selectId]').attr("checked", true );
		}else{ 
			$('input[type=checkbox][name=selectId]').attr("checked", false );
		}
	})
})
var emailValid = /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
function addEmail(){
	$("#errorMsg").html("");
	var email = $("#email").val();
	var duplicate = false;
	if(email == ''){
		$("#emailMessage").html("���Ʋ���Ϊ��!");
	}else if (!emailValid.test(email)){
		$("#emailMessage").html("email��ʽ����ȷ!");
	}else {
		$("#emailList option").each(function(){
			if($(this).val() == email){
				$("#emailMessage").html("�ʼ��Ѵ���!");
				duplicate = true;
			}
		});
		if(!duplicate){
			$("#emailMessage").html("");
			$("#emailList").append("<option value='"+email+"'>"+email+"</option>");
			$("#emailList option:last").attr("selected" , true);  
			$("#email").val('');
		}
	}
}
function removeEmail(){
	ajaxRemoveAcceptorByAddress();
	$("#emailList option:selected").each(function(){
		$(this).remove();��
����})
}

$("#emailList option:selected").each(function(){
	if(${report.reportType}==1){//����Ӧ��
		ajaxCheckApp($(this));
	}else{
		ajaxUserParamsByAddress($(this));
	}
})

$("#emailList").change(function(){
	if(${report.reportType}==1){//����Ӧ��
		ajaxCheckApp($(this));
	}else{
		ajaxUserParamsByAddress($(this));
	}
})
function ajaxCheckApp(obj){
	jQuery.ajax({
		url:"ajaxRepAppsByAddress.do",
	    type: 'POST',
	    dataType: 'html',
	    data:"reportId=${reportId}&addressList="+obj.val(),
	    timeout: 200000,
	    error: function(){showError()},
	    success: function(data){
		    if(data != ''){
			    var appIds = data.split(",");
		    	$("input[type=checkbox][name=selectId]").each(function(){
			    	if(appIds != ''){
				    	var flag = true;
				    	for(var i = 0; i < appIds.length; i++) {
			    		    if(appIds[i] == $(this).val()){
			    		    	flag = false;
			    		    	$(this).attr("checked", true);
			    		    }else{
			    		    	if(flag)$(this).attr("checked", false);
			    		    }
			    		}
			    	}
		    	}) 
		    }else{
		    	$("input[type=checkbox][name=selectId]").each(function(){
			    	$(this).attr("checked", false);
		    	}) 
		    }
	    }
	})
}
function ajaxUserParamsByAddress(obj){
	jQuery.ajax({
		url:"ajaxUserParamsByAddress.do",
	    type: 'POST',
	    dataType: 'html',
	    data:"reportId=${reportId}&addressList="+obj.val(),
	    timeout: 200000,
	    error: function(){showError()},
	    success: function(data){
		    if(data != ''){
			    var appIds = data.split(",");
			    var index = 0;
		    	$("input[type=text][name=paramTextValue]").each(function(){
			    	if(appIds != ''){
		    		    if(appIds[index] != "null"){
		    		    	$(this).val(appIds[index]);
		    		    }else{
		    		    	$(this).val("");
		    		    }
			    	}
			    	index ++;
		    	}) 
		    }else{
		    	$("input[type=text][name=paramTextValue]").each(function(){
			    	$(this).val("");
		    	}) 
		    }
	    }
	})
}
function ajaxRemoveAcceptorByAddress(){
	if(confirm('ȷ��Ҫɾ��ѡ�е��ʼ���ַ?')){
		var addressList = [];
		$("#emailList option:selected").each(function(){
	������	  addressList.push($(this).val());
	��     })
	    if(addressList.length == 0){
	        $("#errorMsg").html("��ѡ����Ҫɾ���������ַ!");
	        $("#emailMessage").html("");
	        $("#errorMsg").css("color", "red");
	    	return ;
	    }else{
	    	$("#errorMsg").html("");
	    }
		$("#addressList").val(addressList.join(","));
	    jQuery.ajax({
			url:"ajaxRemoveAcceptorByAddress.do",
		    type: 'POST',
		    dataType: 'html',
		    data:"reportId=${reportId}&addressList="+addressList,
		    timeout: 200000,
		    error: function(){},
		    success: function(data){
			    if(${report.reportType} == 1){//1:����Ӧ��  0:�û��Զ���
			    	$("input[type=checkbox][name=selectId]").each(function(){
			    		$(this).attr("checked", false);
			    	});
			    }else{
				    if(${!empty params}){
						$("input[type=text][name=paramTextValue]").each(function(){
						   $(this).val("");
					����});
				    }
			    }
		    }
		})
	}
	return false;
}
function showError(){
	alert("����ajax�����쳣!");
}
//Ĭ��ȫѡ
/*
$("input[type=checkbox][name=selectId]").each(function(){
	$(this).attr("checked", true);
});
*/
</script>
</body>
</html>