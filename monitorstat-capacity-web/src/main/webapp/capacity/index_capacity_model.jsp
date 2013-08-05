<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityModelPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@page import="com.taobao.csp.capacity.util.LocalUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>容量规划-预测模型列表</title>
</head>
<body class="span20">
<%@ include file="../top.jsp"%>
<%
List<CapacityModelPo> modelList =  new ArrayList<CapacityModelPo>();

Object obj = request.getAttribute("modelList");
if(obj != null){
	modelList = (List<CapacityModelPo>)obj;
}
%>
	<div class="span20">
		<table class="condensed-table" cellpadding="0" cellspacing="0">
			<tr>
				<td style="border-left: 0px solid #ddd; border-top: 0px solid #ddd;"><input
					type="button" value=" 新  增  " onclick="add()" /></td>
			</tr>
		</table>
		<table class="bordered-table" id="mytable">
			<thead align="center">
				<tr>
					<th>序号</th>
					<th>res_from</th>
					<th>res_to</th>
					<th>rel_ratio</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<%
      for(int i=0;i<modelList.size();i++){
    	  CapacityModelPo po = modelList.get(i);
      %>
				<tr>
					<td><%=i+1 %></td>
					<td><%=po.getResFrom() %></td>
					<td><%=po.getResTo() %></td>
					<td><%=po.getRelRatio()%></td>
					<td width="200"><a href="#" onclick='editModel("<%=po.getId() %>")'>修改</a>
						<a href="#" onclick='delModel("<%=po.getId() %>")'>删除</a>
						<a href="#" onclick='autoCompute("<%=po.getId() %>")'>自动计算</a></td>
				</tr>
				<%} %>
			</tbody>
		</table>
	</div>
	<script type="text/javascript">
		function add() {
			window.open('./model.do?method=addCapacityModel', 'newwindow',
					'height=400, width=900, top=150, left=150');
		}

		function editModel(id) {
			var urlDest = "./model.do?method=editCapacityModel&modelId=" + id;
			window.open(urlDest, 'newwindow',
					'height=400, width=900, top=150, left=150');
		}
		
		function autoCompute(id) {
			var urlDest = "./model.do?method=autoComputeModel&modelId=" + id;
			window.open(urlDest, 'newwindow',
					'height=400, width=900, top=150, left=150');
		}

		function delModel(id) {
			if (!confirm("确定删除? ")) {
				return;
			}
			var urlDest = "./model.do?method=delCapacityModel";
			var parameters = "modelId=" + id;
			$.ajax({
				url : urlDest,
				async : false,
				type : "POST",
				dataType : "String",
				data : parameters,
				cache : false,
				success : function(data) {
					alert(data);
					if (data) {
						window.location.reload();
					} else {
						alert("删除信息失败！");
					}
				}
			});
		}
	</script>
</body>
</html>