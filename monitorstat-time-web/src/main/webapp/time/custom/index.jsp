<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo" %> 
<%@page import="com.taobao.monitor.common.po.AppInfoPo" %> 
<!doctype html>
<html>
<head>
<title>导航操作</title>
<%@ include file="/time/common/base.jsp"%>
<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}
</style>
<script>
function autoResize(id){
    var newheight;
    var newwidth;
    if(document.getElementById){
        newheight=document.getElementById(id).contentWindow.document.body.scrollHeight;
    }
    document.getElementById(id).height= (newheight) + "px";
    window.setTimeout("autoResize("+id+")",1000);
}
</script>
</head>
<body>
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
				<div class="container-fluid">
					<div >
						<c:forEach items="${iframes }" var="item" varStatus="cur">
							<div  style="text-align: center width:${item.size};height:100%" id ="iframe${cur.index }">
							<script>$("#iframe${cur.index }").load(base+"${item.url }");</script>	
							</div>	
						</c:forEach>
					</div>
				</div>
		</div>
	</div>

</body>



<script type="text/javascript">
	
	$(function(){
		$("#cb1").click(function(){
				checked = this.checked;
				stateChange();
		});
		
		
		var deleteButtons = document.getElementsByName("deleteButton");
		$(deleteButtons).click(function(){
			deleteChoosedItem();
		});
		
		var addButtons = document.getElementsByName("addButton");
		$(addButtons).click(function(){
			addNaviName();
		});
		
	});
	

var checked;

function stateChange() {
	if (checked)
		chooseAll();
	else
		unChooseAll();

}

function chooseAll() {
	var c = document.getElementsByName("naviId");

	for (var i = 0; i < c.length; i++) {
		c[i].checked = true;
	}
}

function unChooseAll() {
	var c = document.getElementsByName("naviId");

	for (var i = 0; i < c.length; i++) {
		c[i].checked = false;
	}

}


            
            
    function deleteChoosedItem(){
    	var a = confirm("确定要删除吗？");
    	if(!a)
    		return;
		$("#form2").submit();
    			
    	
    
    }   
    function addNaviName(){
    	var naviName = $("input[name='addNaviName']").val();
    	window.location.href = base+"/app/detail/custom/show.do?method=addNavi&naviName="+naviName;
    }
            
            
        </script>

</html>
