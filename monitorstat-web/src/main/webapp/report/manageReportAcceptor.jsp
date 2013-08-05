<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-添加报表模板</title>
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
   <font style="color:blue;font-weight:900;font-size:14px;">添加报表接收人：</font>
   </div>
  </td>
 </tr>
 <tr>
      <td width="15%" valign="top">
      <strong>邮件地址：</strong>
      <div style="color:blue;align:center">
      <br></br>
        <img align="absmiddle" src="<%=request.getContextPath()%>/statics/images/laba.jpg"/> 操作提示：<br></br>
        1. 添加接收报表邮箱地址.<br/>
        <c:choose>
         <c:when test="${report.reportType == 1}">
         2. 选择邮箱地址配置产生报表数据的应用.
         </c:when>
         <c:otherwise>
         2. 选择邮箱地址配置用户自定义参数.
         </c:otherwise>
        </c:choose>
      </div>
      </td>
      <td width="70%">
      <input type="text" name="email" id="email" size="47" />&nbsp;
	  <input type="button" name="emailadd" value="手工添加" onclick="addEmail()" class="macButton"/>
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
		&nbsp;<input type="button" name="emailremove" value="删   除" onclick="removeEmail()" class="macButton"/>
		<div id="emailListMsg" style="font-size:12px;color:red;"></div>
      </td>
      <td width="10%" align="center">
       <input type="button" value="保 &nbsp;&nbsp;存" onclick="subForm();" style="width:100%;height:320px;"/>
      </td>
 </tr>
</table>
<br/>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width:99%;">
<c:choose>
<c:when test="${report.reportType == 1}"><!-- 1:区分应用  0:用户自定义-->
<div id="dialog">
<table align="center" id="example" border="1">
	<thead>
	<tr>
	  	<th width="50" align="center">
	  	<input name='chkAll' type='checkbox' id='chkAll' value='chkAll'/></th>
		<th align="center">应用名</th>
	    <th align="center">应用组名称</th>
	    <th align="center">应用OPS名称</th>
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
   <font style="color:blue;font-weight:900;font-size:14px;">用户自定义报表参数管理：</font>
   </div>
  </td>
 </tr>
 <tr><td colspan="2"><font style="color:blue;font-size:12px;">&nbsp;&nbsp;&nbsp;&nbsp;报表模板Url：${report.path}</font></td></tr>
 <tr>
 <td colspan="2">
    <font style="color:blue;font-weight:900;font-size:12px;">&nbsp;&nbsp;&nbsp;&nbsp;用户自定义参数赋值：</font>
    <c:if test="${empty params}"><font style="color:red;">无用户自定义参数</font></c:if>
    <c:if test="${!empty params}"><font style="color:red;">提示：参数值全部为空，将删除邮件接收账号！</font></c:if>
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
function subForm(){
	var addressList = [];
	$("#emailList option:selected").each(function(){
　　　	  addressList.push($(this).val());
　     })
    if(addressList.length == 0){
        $("#errorMsg").html("请选择报表接收的邮箱地址!");
        $("#emailMessage").html("");
        $("#errorMsg").css("color", "red");
    	return ;
    }else{
    	$("#errorMsg").html("");
    }
	$("#addressList").val(addressList.join(","));
	$("#reportId").val(${reportId});
	if(${report.reportType}==0){//用户自定义
		var value = "";
		var tag = true;//参数全部为空的标志
		$("input[type=text][name=paramTextValue]").each(function(){
			if($(this).val() == ''){
				value += "null,"
			}else{
				tag = false;
				value += $(this).val()+","
			}　
	　　})
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
			 if(confirm('确定不选择应用?接收人将不被保存!')){
			 }else return;
		}
	}
	document.addReportAcceptorForm.submit();	
}

$(function(){
	// 用另一个复选框来控制全选/全不选
	$("#chkAll").click(function(){
		if(this.checked){ //如果当前点击的多选框被选中
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
		$("#emailMessage").html("名称不能为空!");
	}else if (!emailValid.test(email)){
		$("#emailMessage").html("email格式不正确!");
	}else {
		$("#emailList option").each(function(){
			if($(this).val() == email){
				$("#emailMessage").html("邮件已存在!");
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
		$(this).remove();　
　　})
}

$("#emailList option:selected").each(function(){
	if(${report.reportType}==1){//区分应用
		ajaxCheckApp($(this));
	}else{
		ajaxUserParamsByAddress($(this));
	}
})

$("#emailList").change(function(){
	if(${report.reportType}==1){//区分应用
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
	if(confirm('确定要删除选中的邮件地址?')){
		var addressList = [];
		$("#emailList option:selected").each(function(){
	　　　	  addressList.push($(this).val());
	　     })
	    if(addressList.length == 0){
	        $("#errorMsg").html("请选择需要删除的邮箱地址!");
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
			    if(${report.reportType} == 1){//1:区分应用  0:用户自定义
			    	$("input[type=checkbox][name=selectId]").each(function(){
			    		$(this).attr("checked", false);
			    	});
			    }else{
				    if(${!empty params}){
						$("input[type=text][name=paramTextValue]").each(function(){
						   $(this).val("");
					　　});
				    }
			    }
		    }
		})
	}
	return false;
}
function showError(){
	alert("调用ajax出现异常!");
}
//默认全选
/*
$("input[type=checkbox][name=selectId]").each(function(){
	$(this).attr("checked", true);
});
*/
</script>
</body>
</html>