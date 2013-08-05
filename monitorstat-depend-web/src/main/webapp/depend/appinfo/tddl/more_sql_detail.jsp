<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="java.util.*"%>
<%@ page import="com.taobao.csp.depend.po.tddl.MainResultPo"%>
<%@ page import="com.taobao.csp.depend.po.tddl.TablePo"%>
<%@ page import="com.taobao.csp.depend.po.tddl.IndexPo"%>
<%@ page import="com.taobao.csp.depend.po.tddl.ColumnPo"%>
<%@page import="com.taobao.csp.depend.po.tddl.ConsumeTDDLDetail"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<!DOCTYPE html>
<style>
<!--
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
		<title>��ѯ������Ϣ</title>
<%
  MainResultPo mainResultPo = (MainResultPo) request
      .getAttribute("mainResultPo");
  String tablename = (String) request.getAttribute("tablename");
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  List<ConsumeTDDLDetail> list = (List<ConsumeTDDLDetail>)request.getAttribute("list");
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
		<div>
		<div class="row-fluid" align="center">
		<%
		  if (mainResultPo == null) {
		    out.println("<h3 style='color: red;'>�鲻����" + tablename + "����Ϣ��</h3>");
		  } else if (mainResultPo != null && mainResultPo.isStatus()) {
		    out.println("<h3 aligin='left'>��" + tablename + "��������Ϣ</h3>");
		    TablePo[] tableArray = mainResultPo.getData();
		    if (tableArray != null) {
		      for (TablePo po : tableArray) {
			                IndexPo[] indexPoArray = po.getIndexes();
			                if (indexPoArray != null) {
			        %>
				<table width="100%" class="table table-striped table-condensed table-bordered">
			        	<tr>
			        		<th>��������</th>
			        		<th>����</th>
			        		<th>��</th>
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
			  <h1 style="color: red;">������Ϣ:<%=mainResultPo.getMsg()%></h1>
			  <%
			    }
			  %>
		<div align="center">
		<h3>SQL���:</h3><span>${sql}</span><br/>
		<strong style="color: red;">������ʾ��SQLΪ����ĵ���������Ϊ�ο���</strong><br/>
		</div>
		<div>
		<table width="100%" 
		class="table table-striped table-condensed table-bordered">
			<thead>
			  <tr>
			    <th style="text-align: center;" width="10%">
			    ʱ��
			    </th>
			    <th style="text-align: center;" width="15%">
			      ִ����
			    </th>
			
			    <th style="text-align: center;" width="10%">
			      ƽ��ʱ��
			    </th>
			    <th style="text-align: center;" width="15%">
			      ��ʱ��
			    </th>
			  </tr>
			</thead>  	
			<tbody>
			<%
				  for (ConsumeTDDLDetail item : list) {
				%>
				<tr>
				  <td style="text-align:center;">
				  <%=item.getCollect_time_str()%>
				  </td>
				  <td style="text-align: center;">
				  <%=Utlitites.fromatLong(item.getExecuteSum() + "")%>
				  </td>
				  <td style="text-align: center;">
				  <%=item.getTimeAverage()%>
				  </td>
				  <td style="text-align: center;">
				  <%=Utlitites.fromatLong(item.getTimeAverage()*item.getExecuteSum() + "")%>
				  </td>
				</tr>                           
				    <%
				        }
				    %>
			</tbody>
			</table>    		
		</div>			  		
		</div>
		</div>
		<br />
		<br />
		<br />
	</body>
</html>
