<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>	
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%
response.sendRedirect("http://time.csp.taobao.net:9999/time/index_table.jsp");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<%
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
String collectTime1 = parseLogFormatDate.format(new Date());
AppInfoPo appInfopo = AppCache.get().getKey("list");
%>
<script type="text/javascript">

	function openTrade(tradeType){
		window.open("<%=request.getContextPath () %>/time/key_time.jsp?action=trade&type="+tradeType+"&collectTime1=<%=collectTime1%>");
	}

	
	function goToDetailMonitor(){
		var appName = $("#appNameSelect").val();

		location.href="./time/app_time.jsp?appId="+appName;
	}
	function goToDateIndex(){
		location.href="./index.jsp";
	}
	
	function reinitIframe(down){
		var pTar = null;
		if (document.getElementById){
		pTar = document.getElementById(down);
		}
		else{
		eval('pTar = ' + down + ';');
		}
		if (pTar && !window.opera){
		//begin resizing iframe
		pTar.style.display="block"
		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
		//ns6 syntax
		pTar.height = pTar.contentDocument.body.offsetHeight +20;
		pTar.width = pTar.contentDocument.body.scrollWidth;
		}
		else if (pTar.Document && pTar.Document.body.scrollHeight){
		//ie5+ syntax
		pTar.height = pTar.Document.body.scrollHeight;
		pTar.width = pTar.Document.body.scrollWidth;
		}
		} 
	}
	
</script>

</head>
<body>
<center>
<jsp:include page="head.jsp"></jsp:include>
</center>
<%
List<String> appNameList = new ArrayList<String>();
appNameList.add("login");
appNameList.add("detail");
appNameList.add("hesper");
appNameList.add("shopsystem");
appNameList.add("tf-tm");
appNameList.add("tf-buy");
appNameList.add("cart");
//appNameList.add("tradeplatform");
//appNameList.add("ic");
//appNameList.add("uicfinal");
//appNameList.add("shopcenter");
//appNameList.add("ump");


List<AppInfoPo> appList = AppInfoAo.get().findAllTimeApp();
%>
<center>
<div >
<select id="parentGroupSelect" onchange="groupChange(this)">	
</select>
<select id="appNameSelect">	
</select>
<button  onclick="goToDetailMonitor()">实时监控详细</button><button  onclick="goToDateIndex()">返回日报首页</button>
</div>
</center>




<script type="text/javascript">
var groupMap ={}

function addAppGroup(groupName,appName,appId){
		
		if(!groupMap[groupName]){
			groupMap[groupName]={};
		}
		if(!groupMap[groupName][appName]){
			groupMap[groupName][appName]=appId;
		}			
}
	
	function groupChange(selectObj){
		var groupName = selectObj.options[selectObj.selectedIndex].value;
		var group = groupMap[groupName];
		if(group){
			clearSubSelect();
			fillSubSelect(groupName);
		}
	}
	
	function clearSubSelect(){
		 document.getElementById("appNameSelect").options.length=0;		
		
	}
	function fillSubSelect(groupName,value){
		var group = groupMap[groupName];
	
		var ops = document.getElementById("appNameSelect").options;
		var len = ops.length;
		for (name in group){
			document.getElementById("appNameSelect").options[len++]=new Option(name,group[name]);
			if(name == value){
				document.getElementById("appNameSelect").options[len-1].selected=true;
			}
		}
	}
	
	function initParentSelect(gname,gvalue){
		clearSubSelect();
		var len = document.getElementById("parentGroupSelect").options.length;
		for (name in groupMap){
			document.getElementById("parentGroupSelect").options[len++]=new Option(name,name);
			if(name == gname){
				document.getElementById("parentGroupSelect").options[len-1].selected=true;
			}
		}
				
		if(gname&&gvalue){
			fillSubSelect(gname,gvalue);
		}else{
			groupChange(document.getElementById("parentGroupSelect"));
		}
	
	}


	<%
	for(AppInfoPo app:appList){
		if(appNameList.contains(app.getAppName())){
	%>
	addAppGroup("核心系统","<%=app.getAppName()%>","<%=app.getAppId()%>")
	<%				
	}}
	%>
	
	<%
	Map<String,List<AppInfoPo>> appInfoGroupMap = new HashMap<String,List<AppInfoPo>>();
	for(AppInfoPo app:appList){
	%>
	addAppGroup("<%=app.getGroupName()%>","<%=app.getAppName()%>","<%=app.getAppId()%>")
	<%				
	}
	%>
	 initParentSelect("核心系统","item");
</script>
<table width="100%">
	<tr>
		<td>
			<center>
				<iframe algin="center" id="showTradeIframe" onload="reinitIframe('showTradeIframe')" src="trade_time_show.jsp" frameborder="0" height="0" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			<iframe algin="center" id="showmian_login" onload="reinitIframe('showmian_login')" src="index_time_show.jsp?appName=login&appId=16" frameborder="0" height="0" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			<iframe algin="center" id="showmian_detail" onload="reinitIframe('showmian_detail')" src="index_time_show.jsp?appName=detail&appId=1" frameborder="0" height="0" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			<iframe algin="center" id="showmian_hesper" onload="reinitIframe('showmian_hesper')" src="index_time_show.jsp?appName=hesper&appId=2" frameborder="0" height="0" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			<iframe algin="center" id="showmian_shopsystem" onload="reinitIframe('showmian_shopsystem')" src="index_time_show.jsp?appName=shopsystem&appId=3" frameborder="0" height="0" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			<iframe algin="center" id="showmian_tf_tm" onload="reinitIframe('showmian_tf_tm')" src="index_time_show.jsp?appName=tf_tm&appId=323" frameborder="0" height="0" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			<iframe algin="center" id="showmian_tf_buy" onload="reinitIframe('showmian_tf_buy')" src="index_time_show.jsp?appName=tf_buy&appId=330" frameborder="0" height="0" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			<iframe algin="center" id="showmian_cart" onload="reinitIframe('showmian_cart')" src="index_time_show.jsp?appName=cart&appId=341" frameborder="0" height="0" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			shopcenter
			<iframe algin="center" id="showmian_shopcenter" onload="reinitIframe('showmian_shopcenter')" src="http://time.csp.taobao.net:9999/time/index.do?method=queryIndexTableForHSf&appId=4" frameborder="0" height="400" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			tradeplatform
			<iframe algin="center" id="showmian_tradeplatform" onload="reinitIframe('showmian_tradeplatform')" src="http://time.csp.taobao.net:9999/time/index.do?method=queryIndexTableForHSf&appId=322" frameborder="0" height="400" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			itemcenter
			<iframe algin="center" id="showmian_itemcenter" onload="reinitIframe('showmian_itemcenter')" src="http://time.csp.taobao.net:9999/time/index.do?method=queryIndexTableForHSf&appId=8" frameborder="0" height="400" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			uicfinal
			<iframe algin="center" id="showmian_uicfinal" onload="reinitIframe('showmian_uicfinal')" src="http://time.csp.taobao.net:9999/time/index.do?method=queryIndexTableForHSf&appId=21" frameborder="0" height="400" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
			<center>
			ump
			<iframe algin="center" id="showmian_ump" onload="reinitIframe('showmian_ump')" src="http://time.csp.taobao.net:9999/time/index.do?method=queryIndexTableForHSf&appId=338" frameborder="0" height="400" width="98%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
			</center>
		</td>
	</tr>
	<tr>
      <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
    </tr>
</table>
<jsp:include page="bottom.jsp"></jsp:include>
</body>
</html>