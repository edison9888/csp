<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../../taglibs.jsp" %>
<%@page import="java.net.URLEncoder"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-添加交易巡警告警配置</title>
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
<form id="addSeleniumAlarmAcceptorForm" name="addSeleniumAlarmAcceptorForm" action="addSeleniumAlarmAcceptor.do" method="post">
<table style="width:100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#B0C4DE">
 <tr>
  <td colspan="3" width="20%" align="left" style="height:20px;">
   <div class="small_title" style="margin:0;padding:0;">
   <font style="color:blue;font-weight:900;font-size:14px;">添加交易巡警告警接收人：</font>
   </div>
  </td>
 </tr>
 <tr><td><strong>告警类型：</strong></td>
 <td>
 <select id="type" name="type">
 <c:forEach items="${alarmTypeMap}" var="alarmTypeEntity">
 <option value="${alarmTypeEntity.key}" <c:if test="${type== alarmTypeEntity.key}">selected="selected"</c:if>>${alarmTypeEntity.value}</option>
 </c:forEach>
 </select>
 </td>
 </tr>
 <tr>
      <td width="15%" valign="top">
      <strong>地址：</strong>
      <div style="color:blue;align:center">
      <br></br>
        <img align="absmiddle" src="<%=request.getContextPath()%>/statics/images/laba.jpg"/> 操作提示：<br></br>
        1. 添加交易巡警告警接收人地址.<br/>
        2. 选择需要配置告警的用例.
      </div>
      </td>
      <td width="70%">
      <input type="text" name="address" id="address" size="47" />&nbsp;
	  <input type="button" name="emailadd" value="手工添加" onclick="addAddress()" class="macButton"/>
		<span id="addressMessage" style='color:red'></span><span id="errorMsg"></span><br/>
		<select name="addressList" id="addressList" multiple="multiple" style="width:294px;height:300px">
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
		&nbsp;<input type="button" name="emailremove" value="删   除" onclick="removeAddress()" class="macButton"/>
		<div id="addressListMessage" style="font-size:12px;color:red;"></div>
      </td>
      <td width="10%" align="center">
       <input type="button" value="保 &nbsp;&nbsp;存" onclick="subForm();" style="width:100%;height:320px;"/>
      </td>
 </tr>
</table>
<br/>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width:99%;">
<div id="dialog">
<table align="center" id="example" border="1">
	<thead>
	<tr>
	  	<th width="50" align="center">
	  	<input name='chkAll' type='checkbox' id='chkAll' value='chkAll'/></th>
		<th align="center">用例名称</th>
	    <th align="center">适配器类名称</th>
	    <th align="center">Base Url</th>
  </tr>  
  </thead>
  <tbody>
	<c:forEach items="${ucMap}" var="valueEntry">
       	<tr>
       	    <td align="center">
       	    <input type="checkbox" id="useCaseIdArr" name='useCaseIdArr' class='selectId' value="${valueEntry.value.useCaseId}"/>      
       	    </td>
			<td align="center">${valueEntry.value.useCaseAlias}</td>
			<td align="center">${valueEntry.value.useCaseName}</td>
			<td align="center">${valueEntry.value.baseUrl}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</div>
</div>
<div align="center" style="padding-top:2px;">
  <input type="button" value="保 &nbsp;&nbsp;存" onclick="subForm();" style="width:50px;height:20px;" class="macButton"/>
</div>
</form>
<script type="text/javascript">
$(document).ready(function() {
    $('#example').dataTable( {//加载
        "bPaginate": false,//分页按钮
        "bLengthChange": true,//每行显示记录数
        "bFilter": true,//搜索栏
        "bSort": true,//排序
        "bInfo": false,//Showing 1 to 10 of 23 entries  总记录数没也显示多少等信息
        "bAutoWidth": true } );
});
$(function(){
	$("#chkAll").click(function(){
		if(this.checked){ //如果当前点击的多选框被选中
			$('input[type=checkbox][name=useCaseIdArr]').attr("checked", true );
		}else{ 
			$('input[type=checkbox][name=useCaseIdArr]').attr("checked", false );
		}
	})
})
var emailValid = /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
function addAddress(){
	$("#errorMsg").html("");
	var address = $("#address").val();
	var duplicate = false;
	if(address == ''){
		$("#addressMessage").html("名称不能为空!");
	//}else if (!emailValid.test(email)){
	//	$("#addressMessage").html("email格式不正确!");
	}else {
		$("#addressList option").each(function(){
			if($(this).val() == address){
				$("#addressMessage").html("地址已存在!");
				duplicate = true;
			}
		});
		if(!duplicate){
			$("#addressMessage").html("");
			$("#addressList").append("<option value='"+address+"'>"+address+"</option>");
			$("#addressList option:last").attr("selected" , true);  
			$("#address").val('');
		}
	}
}
function subForm(){
	document.addSeleniumAlarmAcceptorForm.submit();	
}
function removeAddress(){
	ajaxRemoveAcceptorByAddress();
	$("#addressList option:selected").each(function(){
		$(this).remove();　
　　})
}
$("#addressList").change(function(){
	ajaxCheckUseCase($(this));
})
function ajaxCheckUseCase(obj){
	jQuery.ajax({
		url:"ajaxCheckUseCaseAlarmByAddress.do",
	    type: 'GET',
	    dataType: 'html',
	    data:"addressList="+encodeURIComponent(obj.val()),
	    timeout: 200000,
	    error: function(){showError()},
	    contentType: "application/x-www-form-urlencoded;charset=gbk",
	    success: function(data){
		    if(data != ''){
			    var appIds = data.split(",");
		    	$("input[type=checkbox][name=useCaseIdArr]").each(function(){
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
		    	$("input[type=checkbox][name=useCaseIdArr]").each(function(){
			    	$(this).attr("checked", false);
		    	}) 
		    }
	    }
	})
}
function ajaxRemoveAcceptorByAddress(){
	if(confirm('确定要删除选中的邮件地址?')){
		var addressList = [];
		$("#addressList option:selected").each(function(){
	　　　	  addressList.push($(this).val());
	　     })
	    if(addressList.length == 0){
	        $("#errorMsg").html("请选择需要删除的地址!");
	        $("#addressMessage").html("");
	        $("#errorMsg").css("color", "red");
	    	return ;
	    }else{
	    	$("#errorMsg").html("");
	    }
		$("#addressList").val(addressList.join(","));
	    jQuery.ajax({
			url:"ajaxRemoveAcceptorByAddress.do",
		    type: 'GET',
		    data:"addressList="+encodeURIComponent(addressList),
		    timeout: 200000,
		    contentType: "application/x-www-form-urlencoded;charset=gbk",
		    error: function(){},
		    success: function(data){
		    	$("input[type=checkbox][name=useCaseIdArr]").each(function(){
		    		$(this).attr("checked", false);
		    	});
		    }
		})
	}
	return false;
}
function showError(){
	alert("调用ajax出现异常!");
}
</script>
</body>
</html>