<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.*"%>
<%@page import=" com.taobao.csp.depend.job.CallEagleeyeApiJob"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/themes/icon.css">
<%%>

<title>接口调用比例(beta)</title>
<%
	String appName = (String)request.getAttribute("appName");
	String sourceKey = (String)request.getAttribute("sourceKey");
	List<String> urlList = (List<String>)request.getAttribute("urlList");
	if(urlList == null)
		urlList = new ArrayList<String>();
%>
<script type="text/javascript">
$(document).ready(function(){
	  $("#appName").change(function(){
		  changeKeyList();
	  });
	  $("#type").change(function(){
		  changeKeyList();
	  });	  
});
	
	function changeKeyList() {
		var appName = $("#appName").val();
		var type = $("#type").val();
		var collectTime = $("#collectTime").val();
		var actionUrl = "<%=request.getContextPath()%>/rate/callrate.do?method=getSourceKeyList";
		//var value = $('#appNameSelect').val();
		//var selectDate = $('#selectDate').val();
		$.getJSON(
				actionUrl,
				{ 
					appName: appName,
					collectTime: collectTime,
					type: type
				},
				function(json){
					$("#sourceKey").empty();
					var i=0;
					for(i=0;i<json.length;i++) {
						$("#sourceKey").append("<option value='" + json[i] + "'>" + json[i] + "</option>");
					}
				} 
		);
	}
</script>
</head>
<body>
<%@ include file="../../header.jsp"%>
		<div align="center">
			<h1>应用接口依赖关系查询</h1>
			<span style="color: red;"><strong>最早查询当前时间的前一天数据,数据来自鹰眼(Eagleeye)日志数据.增加依赖监控，请联系"中亭"
			</strong>
			</span>
		</div>
		<form action="<%=request.getContextPath()%>/rate/callrate.do" method="get">
			<table>
				<tr>
					<td>
						<input type="hidden" name="method" value="gotoTopoPage">
						<select name="appName" id="appName" value="${appName}">
						<%
							for(String appNameTmp : CallEagleeyeApiJob.getApplist()) {
								String selected = "";
								if(appNameTmp.equals(appName))
									selected = "selected='selected'";
								out.println("<option " + selected + " value='" + appNameTmp + "'>" + appNameTmp + "</option>");
							}	
						%>
						</select>			
					</td>
					<td>查询时间<input type="text" id="collectTime" value="${collectTime}"
						name="collectTime"
						onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"
						class="span2" />
					<td>
				</tr>
				<tr>
					<td>
						<select name="type" id="type">
							<option value="child_key">我依赖</option>
							<option value="father_key">依赖我</option>
						</select>			
					</td>
					<td>
						<select name="sourceKey" id="sourceKey" class="input-xxlarge">
						<%
							for(String sourceKeyTmp : urlList) {
								String selected = "";
								if(sourceKeyTmp.equals(sourceKey))
									selected = "selected='selected'";
								out.println("<option " + selected + " value='" + sourceKeyTmp + "'>" + sourceKeyTmp + "</option>");
							}	
						%>
						</select>							
					<td>
				</tr>				
				<tr>
					<td><input type="submit" value="查询"
						class="btn btn-success"></td>
				</tr>
			</table>
		</form>
		<table id="topoAll" title="调用路径（汇总）" style="width:1300px;"	>
			
		</table>
		<script>
		$(function(){
	
		    $('#topoAll').treegrid({  
		        url:'<%=request.getContextPath() %>/rate/callrate.do?method=getEagleeyeTopoByType&sourceKey=${sourceKey}&appName=${appName}&collectTime=${collectTime}&type=${type}',  
		        idField:'id',  
		        treeField:'keyName',  
		        columns:[[  
		            {title:'名称',field:'keyName',width:1100},  
		            {title:'应用',field:'appName',width:100},  
		            {title:'次数',field:'callnum',width:100}
		        ]],
		        onLoadSuccess:function(){
		        	var data = $(this).treegrid('getData'); 
		        	var childrenLen = 0;
		        	if(data.length>0 && typeof data[0].children !='undefined' && typeof data[0].children.length !='undefined' ){
		        		childrenLen = data[0].children.length;
		       		}
		        	if(data.length < 2 && childrenLen <2 ){
		   				$(this).treegrid('resize',{height:130}); 
		        	}
		        }
		    }); 
			
		});
		
		</script>
</body>
</html>