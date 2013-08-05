<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="java.util.*"%>
<%@ page import="com.taobao.csp.depend.po.tddl.MainResultPo"%>
<%@ page import="com.taobao.csp.depend.po.tddl.TablePo"%>
<%@ page import="com.taobao.csp.depend.po.tddl.IndexPo"%>
<%@ page import="com.taobao.csp.depend.po.tddl.ColumnPo"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<style>
<!--
body {
	padding-bottom: 40px;
	padding-top: 35px;
}
td {
	word-break:break-all;
}
th {
	word-break:break-all;
}
-->
</style>

<html>
	<head>
		<title>查询索引信息</title>
<%
  MainResultPo mainResultPo = (MainResultPo) request
      .getAttribute("mainResultPo");
  String tablename = (String) request.getAttribute("tablename");
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
%>
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
	<body>
		<%@ include file="/header.jsp"%>
		<div>
		<div class="row-fluid" align="center">
			<form action="<%=request.getContextPath()%>/tddl/show.do"
				class="well form-search">
				<input type="hidden" name="method" value="showIndex" />	
				<input name="tablename" value="${tablename}" placeholder="输入表的名称"/>	<input type="submit" value="查询" class="btn btn-primary"/><br/>
				<span>如果sql解析后的表名称不正确，可以输入table名称查询(不区分大小写)。<br>
				因为表名称可能重复，故这里按照顺序全部列出</span>
			</form>
		</div>		
		<div class="row-fluid" align="center">
		<%
		  if (mainResultPo == null) {
		    out.println("<h1 style='color: red;'>查不到表" + tablename + "的信息。</div>");
		  } else if (mainResultPo != null && mainResultPo.isStatus()) {
		    out.println("<h1>表" + tablename + "的信息</h1>");
		    TablePo[] tableArray = mainResultPo.getData();
		    if (tableArray != null) {
		      for (TablePo po : tableArray) {
		        ColumnPo[] columnPoArray = po.getColumns();
		        if (columnPoArray != null) {
		          out.println("<strong>表信息</strong>");
		%>
				<table class="table table-striped table-bordered">
			        	<tr>
					        <th width="10px" title="name">name</th>
							<th width="5px" title="autoIncrement">autoIncrement</th>
							<th width="10px" title="createdAt">createdAt</th>
							<th width="5px" title="dataType">dataType</th>
							<th width="5px" title="defaultLength">defaultLength</th>
							<th width="5px" title="defaultValue">defaultValue</th>
							<th width="10px" title="description">description</th>
							<th width="5px" title="id">id</th>
							<th width="5px" title="length">length</th>
							<th width="5px" title="nullable">nullable</th>
							<th width="5px" title="position">position</th>
							<th width="5px" title="precision">precision</th>
							<th width="5px" title="scale">scale</th>
							<th width="5px" title="securityDesc">securityDesc</th>
							<th width="5px" title="securityLevel">securityLevel</th>
							<th width="5px" title="status">status</th>
							<th width="5px" title="tableId">tableId</th>
							<th width="10px" title="updatedAt">updatedAt</th>
						</tr>
			        <%
			          for (ColumnPo column : columnPoArray) {
			        %>
			        	<tr>
			        		<td><%=column.getName()%></td>
			        		<td><%=column.getAutoIncrement()%></td>
			        		<td><%=sdf.format(column.getCreatedAt())%></td>
			        		<td><%=column.getDataType()%></td>
			        		<td><%=column.getDefaultLength()%></td>
			        		<td><%=column.getDefaultValue()%></td>
			        		<td><%=column.getDescription()%></td>
			        		<td><%=column.getId()%></td>
			        		<td><%=column.getLength()%></td>
			        		<td><%=column.getNullable()%></td>
			        		<td><%=column.getPosition()%></td>
			        		<td><%=column.getPrecision()%></td>
			        		<td><%=column.getScale()%></td>
			        		<td><%=column.getSecurityDesc()%></td>
			        		<td><%=column.getSecurityLevel()%></td>
			        		<td><%=column.getStatus()%></td>
			        		<td><%=column.getTableId()%></td>
			        		<td><%=sdf.format(column.getUpdatedAt())%></td>			        	
			        	</tr>
			        <%
			          }
			        %>
			        	</table>
			        	<br/>
			        <%
			          }

			                IndexPo[] indexPoArray = po.getIndexes();
			                if (indexPoArray != null) {
			                  out.println("<strong>索引信息</strong>");
			        %>
				<table width="100%" class="table table-striped table-condensed table-bordered">
			        	<tr>
			        		<th>索引名称</th>
			        		<th>类型</th>
			        		<th>列</th>
			        	</tr>
			        <%
			          for (IndexPo indexpo : indexPoArray) {
			        %>
			        	<tr>
			        		<td><%=indexpo.getName()%></td>
			        		<td><%=indexpo.getType()%></td>
			        		<td><%=indexpo.getColumns()%></td>
			        	</tr>
			        <%
			          }
			        %>
			        	</table>
			        	<br/>
			        <%
			          }
			              }
			            }
			          } else {
			        %>
			  <h1 style="color: red;">错误信息:<%=mainResultPo.getMsg()%></h1>
			  <%
			    }
			  %>		
		</div>
		</div>
		<div align="center">
		如有疑问请联系:<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=中亭&site=cntaobao&s=2&charset=gbk" >
    <img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=中亭&site=cntaobao&s=2&charset=gbk" />中亭</a>				
		</div>				
		<br />
		<br />
		<br />
	</body>
</html>
