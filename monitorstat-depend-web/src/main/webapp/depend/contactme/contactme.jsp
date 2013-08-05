<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/statics/css/main.css" type="text/css" />
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet" />
<link
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css"
	rel="stylesheet" />
<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>	
<title>依赖模块</title>
</head>
<body>
<%@ include file="../header.jsp"%>
	<h1>强弱依赖系统资料（beta补充中...）</h1>
	<div class="container">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<th width="20%">
			序号：
			</th>
			<th width="80%">
			说明：
			</th>
		</thead>
		<tr>
			<td>1:</td>
			<td>
			对淘宝网核心系统（detail,hesper,login,cart,shopsystem,shopcenter,tradeplatform,tradeplatform,tf_buy,tf_tm,uicfinal,itemcenter,ump）进行了自动化检测，得出了对其他系统的强弱依赖关系。<br/>
			<a href="http://10.232.135.198:8080/depend/show/reportaction.do?method=dependAutoCheckReport&selectDate=" target="blank">强弱依赖报表地址</a><br/>
			<a href="http://baike.corp.taobao.com/index.php/File:%E5%BC%BA%E5%BC%B1%E4%BE%9D%E8%B5%96%E6%A3%80%E6%B5%8B%E5%8E%9F%E7%90%86%E8%AF%B4%E6%98%8E.pdf" target="_blank">强弱依赖检测的原理和检测流程的介绍(pdf下载)</a></td>
		</tr>
		<tr>
			<td>2:</td>
			<td>
			<a href="http://baike.corp.taobao.com/index.php/File:%E4%BE%9D%E8%B5%96%E7%B3%BB%E7%BB%9F%E4%BB%8B%E7%BB%8D.pdf" target="_blank">依赖系统史上最强介绍(pdf下载)</a></td>
		</tr>
		<tr>
			<td>3:</td>
			<td>新增功能：<br/>
			a.首页改版。访问<a href="http://depend.csp.taobao.net:9999/depend/" target="_blank">依赖系统（http://depend.csp.taobao.net:9999/depend/）</a><br/>
			b.增加应用首页。<br/>
			c.增加按接口、URL查询调用路径、调用比例。<br/>
			d.查看各个接口的历史图。<br/>
			e.Tddl查询。
			</td>
		</tr>
	</table>	
	</div>
    问问题，提需求，请联系:<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=中亭&site=cntaobao&s=2&charset=gbk" >
    <img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=中亭&site=cntaobao&s=2&charset=gbk" />中亭</a>
</body>
</html>