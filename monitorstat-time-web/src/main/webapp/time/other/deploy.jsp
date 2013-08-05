<%@page import="com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>ʵʱ���ϵͳ</title>
<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
</head>
<body>
	<div class="container-fluid">
			<div class="span6">
			<table>
				<tr>
					<td>�������</td>
					<td>ԭ��</td>
					<td>�������</td>
					<td>������Ա</td>
					<td>��ʼʱ��</td>
					<td>����ʱ��</td>
				</tr>
				<tbody id="changefreeId">
					
				</tbody>
			</table>
			</div>
			<div class="span6">
			<table>
				
				<tr>
					<td>Ӧ������</td>
					<td>����ʱ��</td>
					<td>��������</td>
					<td>����״̬</td>
					<td>������</td>
				</tr>
				<tbody id="deployId">
					
				</tbody>
			</table>
			</div>
	</div>
	<script type="text/javascript">
	//��ȡ������Ϣ
	function queryDeploy(){
		$.ajax({
			dataType:"json",
			url : "http://artoo.taobao.net:9999/appops-deploy/api/getDeployList.htm",
			success : function(data) {
				if(data.length >0){
					var body="<tr>";
					for(var i=0;i<data.length;i++){
						body+="<td>"+data[i]['appName']+"</td><td>"+data[i]['deployTime']+"</td><td>"+data[i]['planType']+"</td><td>"+data[i]['state']+"</td><td>"+data[i]['creator']+"</td>";
					}
					body+="</tr>";
					$("#deployId").html(body);
				}else{
					$("#deployId").html("<tr><td colspan='4'>&nbsp;</td></tr>");
				}
			}});
	}
	queryDeploy();
	
	function queryChangeFree(){
		var d = new Date();
		var c = d.getMilliseconds();
		var y1 = d.getFullYear();
		var m1 = d.getMonth() + 1;
		if(m1<9){
			m1="0"+m1;
		}
		var d1 = d.getDate();
		if(d1<9){
			d1="0"+d1;
		}
		
		c = d.getMilliseconds() - 60*60*24*1000*3;
		d.setMilliseconds(c);
		
		var y2 = d.getFullYear();
		var m2 = (d.getMonth() + 1);
		if(m2<9){
			m2="0"+m2;
		}
		var d2 = d.getDate();
		if(d2<9){
			d2="0"+d2;
		}
		
		$.ajax({
			data:{starttime:y2+"-"+m2+"-"+d2+" 00:00:00",endtime:y1+"-"+m1+"-"+d1+" 23:59:59"},
			dataType:"json",
			type:"POST",
			url : "http://changefree.taobao.net/v2_changefree/app/index.php/outer/get_change_free_data",
			success : function(data) {
				if(data.length >0){
					var body="<tr>";
					for(var i=0;i<data.length;i++){
						body+="<td>"+data[i]['title']+"</td><td>"+data[i]['change_reason']+"</td><td>"+data[i]['change_type']+"</td><td>"+data[i]['username']+"</td><td>"+data[i]['start_time']+"</td><td>"+data[i]['end_time']+"</td>";
					}
					body+="</tr>";
					$("#changefreeId").html(body);
				}else{
					$("#changefreeId").html("<tr><td colspan='5'>&nbsp;</td></tr>");
				}
			}});
	}
	queryChangeFree();
	</script>
	
</body>
</html>
