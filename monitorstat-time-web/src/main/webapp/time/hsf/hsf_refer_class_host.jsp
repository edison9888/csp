<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
<head>
<title>��ԴӦ���������</title>
<%@ include file="/time/common/base.jsp"%>

<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/raphael.js" type="text/javascript"></script>


<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>

<script>

</script>
<style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
</style>
</head>
<body >
<%@ include file="/header.jsp"%>
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
	<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	

</div>
	<div class="row-fluid">
        <div class="span2">
			<%@include file="/leftmenu.jsp"%>
		</div>
		<div class="span12">
		<%--���� --%>
			<%request.setAttribute("s_sperator", Constants.S_SEPERATOR);%>
			<c:forEach var="item" items="${fn:split(param.key,  s_sperator)}" varStatus="status">
				<c:if test="${status.index==1}">
					<c:set var="referAppName" value="${item}"/>
				</c:if>
				<c:if test="${status.index==2}">
					<c:set var="className" value="${item}"/>
				</c:if>
			</c:forEach>
			<a href="<%=request.getContextPath()%>/index.jsp">ʵʱ��ҳ</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}" >${appInfo.appName}</a>  
			-><a href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex&appId=${appInfo.appId}" >HSf Provider ������Ϣ </a>
			->  <a href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfRefer&appId=${appInfo.appId}">��ԴӦ�õ���</a>
			-><a href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfReferClass&appId=${appInfo.appId}&key=<%=KeyConstants.HSF_REFER+ Constants.S_SEPERATOR %>${referAppName}">${referAppName}  �ӿ��б� </a>
			-> ${className }�����б�
			<hr>
			<div class="row-fluid">
				<h4>��ԴӦ�õ��� </h4>
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td  style="text-align:center;">IP</td>
							<td style="text-align:center;" id="time1">10:21</td>
							<td style="text-align:center;" id="time2">10:22</td>
							<td style="text-align:center;" id="time3">10:23</td>
							<td style="text-align:center;" id="time4">10:24</td>
							<td style="text-align:center;" id="time5">10:25</td>
							<td style="text-align:center;" id="time6">10:26</td>
							<td style="text-align:center;" id="time7">10:27</td>
							<td style="text-align:center;" id="time8">10:28</td>
							<td style="text-align:center;" id="time9">10:29</td>
							<td style="text-align:center;" id="time10">10:30</td>
							<td style="text-align:center;">��ʷ����</td>
						</tr>
					</thead>
					<tbody id="exctbody">
						<c:forEach items="${eMap }" var="ex">
							<tr id="tr_${ex.key }">
								<td width="250">${ex.key }</td>
								<td style="text-align:center;" id="${ex.key }_time1">0</td>
								<td style="text-align:center;" id="${ex.key }_time2">0</td>
								<td style="text-align:center;" id="${ex.key }_time3">0</td>
								<td style="text-align:center;" id="${ex.key }_time4">0</td>
								<td style="text-align:center;" id="${ex.key }_time5">0</td>
								<td style="text-align:center;" id="${ex.key }_time6">0</td>
								<td style="text-align:center;" id="${ex.key }_time7">0</td>
								<td style="text-align:center;" id="${ex.key }_time8">0</td>
								<td style="text-align:center;" id="${ex.key }_time9">0</td>
								<td style="text-align:center;" id="${ex.key }_time10">0</td>
								<td style="text-align:center;"><a href="">�鿴</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>			
    </div>
</div>
   
</body>
<script type="text/javascript">
	function query(){
		
		var d  = new Date();
		var time = {};
		var tarray = [];
		for(var i=0;i<10;i++){
			var h = d.getHours()>9?d.getHours():"0"+d.getHours();
			var m = d.getMinutes()>9?d.getMinutes():"0"+d.getMinutes();
			var t = h+":"+m;
			time[t] = (10-i);
			tarray[tarray.length] = t;
			var f = d.getTime()-60*1000
			d = new Date(f);
			$("#time"+(10-i)).text(h+":"+m);
		}
		
		
		$.ajax({
			url : "<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=queryhsfReferClassHost&appId=${appInfo.appId}&key=${param.key}",
			success : function(data) {
				var tbody = "";				
				var  for_popover_map = {};
				//console.log(data.length);
				for(var n=0;n<data.length;n++){
					var row = data[n];
					var tmp = {};
					var objectMap = row.objectMap;
					for(var ftk in objectMap){
						var ft = objectMap[ftk].ftime;
						tmp[ft] = objectMap[ftk];
					}
					
					
					var tr="<tr id='tr_"+n+"' style='width:250px'><td >"+row.ip+"</td>";
					for(var i=10;i>=1;i--){
						var k = tarray[(i-1)]
						if(tmp[k]){
							//��Ϊkey��url��url�а���.�����js��.�������ͻ������id�в��ܰ���key
							var for_popover_id = "popover_a_tr"+ n +"td" + i;
							
							tr+="<td style='text-align:center;' id='"+n+"_time'"+i+" title='"+k+"' ><a data-content='1234' href=''  rel='popover' id='"+for_popover_id+"'>"+tmp[k].mainValue+""+tmp[k].mainValueRate+"</a></td>";
						
							var h = "<table class='table table-striped table-bordered table-condensed'><tr>";
							//���������б�  ips[i].propertyMap.prop
							var propNames = "", propValues="";
							var propertyMap = tmp[k].propertyMap;
							for(var prop in propertyMap){
								propNames +="<td>"+ prop +"</td>";	
								propValues +="<td>"+  propertyMap[prop] +"</td>"
							}
							h += propNames+"</tr><tr>"+propValues+"</tr></table>";
							for_popover_map[for_popover_id] = h;
						}else{
							tr+="<td style='text-align:center;' id='"+n+"_time'"+i+" title='"+k+"'>0</td>";
						}
					}
					
					tr+="<td  style='text-align:center;'><a href='<%=request.getContextPath()%>/app/detail/history.do?method=showHistoryByip&appName=${appInfo.appName}&keyName="+row.keyName+"&ip="+row.ip+"' target='_blank'>�鿴</a></td></tr>";
					tbody+=tr;
				}
				$("#exctbody").html(tbody);
				$("a[rel='popover']").popover({placement:'left'}).click(function(e) {
					e.preventDefault()
    			});
				//��popoverԪ�����data-content����
				for(var i in for_popover_map){
					$("#"+ i).attr('data-content', for_popover_map[i]);
				}
			}
		});
		window.setTimeout("query()",60000);
	}
	query();
</script>

</html>
