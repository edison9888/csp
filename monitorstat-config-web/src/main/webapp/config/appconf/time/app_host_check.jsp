<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�鿴�������</title>
<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>

<%
AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("appInfoPo");
List<HostPo> hostList = (ArrayList<HostPo>)request.getAttribute("hostList");
%>
<script>
function goToSingleAdd() {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=gotoSingleAdd&appId=<%=appInfoPo.getAppId()%>";
	location.href = str;
}

function goToCenter() {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=getTimeConfig&appId=<%=appInfoPo.getAppId()%>";
	location.href = str;
}
$(function(){

	// ����һ����ѡ��������ȫѡ/ȫ��ѡ
		$("#chkAll").click(function(){
	  
			if(this.checked){     //�����ǰ����Ķ�ѡ��ѡ��
		   		$('input[type=checkbox][name=selectId]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][name=selectId]').attr("checked", false );
		 	}
		})
})
function ck(b)
{
	var input = document.getElementsByTagName("input");

    for (var i=0;i<input.length ;i++ )
    {
        if(input[i].type=="checkbox")
            input[i].checked = b;
    }
}

function deleteAppHost() {
	var hostName = $("#hostName").val();
	var hostSite = $("#hostSite").val();
	var hostIp=$("#hostIp").val();
	
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=singleAddAppHost&appId=<%=appInfoPo.getAppId()%>&hostName="+hostName+"&hostSite="+hostSite+"&hostIp="+hostIp+"&saveData="+saveData;

	location.href = str;
}

function checkAlarm(){
	var objs=document.getElementsByName('selectId');
	var isSel = false;
	for(var i=0;i<objs.length;i++)
	{
	  if(objs[i].checked==true)
	   {
	    isSel=true;
	    break;
	   }
	}
	if(isSel==false){
		alert("�Բ���!��û��ѡ���κε�����!"); 
		return false;
	}else{
		if(confirmDel()) {
			return true;
		} else {
			return false;
		}
	} 
}

function confirmDel(){
	return confirm("�����Ҫɾ����û�а�");
}

function goToAdd() {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=addAppHost&appId=<%=appInfoPo.getAppId()%>";
	location.href = str;
}
</script>
</head>
<body>
	<jsp:include page="../../../header.jsp"></jsp:include>
	<div class="container" style="padding-top: 60px;">
      <div class="content">
        <div class="page-header">
          <h1>Ӧ�ã�<%=appInfoPo.getOpsName() %> <small>�Ѿ���������   : �� <%=(hostList == null) ? 0 : hostList.size() %> ̨</small></h1>
        </div>
        <form action="<%=request.getContextPath()%>/show/appconfig.do?method=deleteAppHost&appId=<%=appInfoPo.getAppId() %>" onsubmit="return checkAlarm()" method="post" name="myform" id="myform">
        <table class="zebra-striped condensed-table bordered-table">
        	<thead>
				<tr>
					<th><input name='chkAll' type='checkbox' id='chkAll' value='checkbox'></th>
					<th class="blue">Ӧ����</th>
					<th class="blue">������</th>
					<th class="blue">����IP</th>
					<th class="blue">����</th>
					<th class="blue">��ʱ��</th>
					<th class="blue">�־ñ�</th>
				</tr>
			</thead>
			<tbody>
				<%
					for(HostPo po : hostList) {
				%>
				<tr>
					<td><input type="checkbox" id='selectId' name="selectId" value="<%=po.getHostId() %>"></td>
					<td><%=appInfoPo.getOpsName() %></td>
					<td id="hostName"><%=po.getHostName()%></td>
					<td id="hostIp"><%=po.getHostIp() %></td>
					<td id="hostSite"><%=po.getHostSite() %></td>
					<td id="saveData1"><%=((po.getSavedata().charAt(0)) == '1') ? "��" : "��"  %></td>
					<td id="saveData2"><%=((po.getSavedata().charAt(1)) == '1') ? "��" : "��"  %></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
		
		<div class="well" style="padding: 14px 19px;">
		<center>
        	<input class="btn primary" type="button" value="�������" onclick="goToSingleAdd()">
			<input class="btn primary" type="button" value="�������" onclick="goToAdd()">
			<input class="btn primary" type="button" value="����" onclick="goToCenter()">
			<input class="btn primary" type="button" onclick="ck(true)" value="ȫѡ">
			<input class="btn primary" type="button" onclick="ck(false)" value="ȡ��ȫѡ">
			<input class="btn primary" type="submit" value="ɾ��">
		</center>
      	</div>
		</form>
      </div>

      <footer>
        <p>&copy; Taobao 2011</p>
      </footer>

    </div> <!-- /container -->
</body>
</html>