<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>没有用</title>
<link href="<%=request.getContextPath() %>/statics/main.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/></head>
<body>
<div id="container">
<div id="head">
	<div id="logo"></div>
	<!-- 开头 -->
	<div id="nav">
		<li id="li_1" style="width:48px;"><a href="index.php"><span>九点</span></a></li>		
		<li id="li_2" style="width:48px;"><a href="alert.php"><span>告警</span></a></li>		
		<li id="li_3" style="width:60px;"><a href="mysql.php"><span>MySQL</span></a></li>		
		<li id="li_4" style="width:108px;"><a href="dashboard.php"><span>Dashboard</span></a></li>		
		<li id="li_5" style="width:48px;"><a href="group.php"><span>群组</span></a></li>		
		<li id="li_6" style="width:60px;"><a href="redis.php"><span>Redis</span></a></li>		
		<li id="li_7" style="width:96px;"><a href="vertical.php"><span>垂直业务</span></a></li>		
		<li id="li_8" style="width:48px;"><a href="tddl.php?envId=1"><span>JADE</span></a></li>		
		<li id="li_9" style="background-color: rgb(242, 123, 4); width: 72px; border-top-left-radius: 5px 5px; border-top-right-radius: 5px 5px; "><a href="oracle.php"><span style="color:white">Oracle</span></a></li>		
		<li id="li_10" style="width:60px;"><a href="hbase.php"><span>HBase</span></a></li>		
		<li id="li_11" style="width:48px;"><a href="autoins.php"><span>安装</span></a></li>		
		<li id="li_12" style="width:48px;"><a href="other.php"><span>Labs</span></a></li>		
		<li id="li_13" style="width:48px;"><a href="/wiki/index.php"><span>Wiki</span></a></li>		
		<li id="li_14" style="width:72px;"><a href="http://beidou.corp.taobao.com"><span>回北斗</span></a></li>	
	</div>
	<div id="logininfo">
                <span style="font-weight:bold">xiaodu</span> |
                <a href="http://beidou.corp.taobao.com/index.php?controller=users&amp;action=logout&amp;menuid=909">
                      <span style="color:blue">登出</span></a> |
                <a href="http://storagemonitor.taobao.org:9999/cacti/index.php"><span style="color:blue">存储</span></a> |
                <a href="http://10.232.31.221/"><span style="color:blue">开发</span></a> |
                <a href="http://dba.taobao.org:9999/blog"><span style="color:blue">Blog</span></a> |
                <a href="http://beidou.corp.taobao.com/dbnav.htm"><font color="red">导航</font></a>
                <p>
                </p>
	</div>
</div>
<!-- 左边导航 -->
<div id="leftnav" style="border-top-left-radius: 3px 3px; border-top-right-radius: 3px 3px; border-bottom-left-radius: 3px 3px; border-bottom-right-radius: 3px 3px; ">
    <ul>
	        <li>
            <div id="oracle_icon_1" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=1">主机</a>
        </li>        <li>
            <div id="oracle_icon_2" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=2" style="color:red">数据库</a>
        </li>        <li>
            <div id="oracle_icon_3" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=3">RAC</a>
        </li>        <li>
            <div id="oracle_icon_4" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=4">主机状态</a>
        </li>        <li>
            <div id="oracle_icon_5" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=5">实例状态</a>
        </li>        <li>
            <div id="oracle_icon_6" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=6">ops2db</a>
        </li>        <li>
            <div id="oracle_icon_7" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=7">report</a>
        </li>        <li>
            <div id="oracle_icon_8" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=8">weekreport</a>
        </li>        <li>
            <div id="oracle_icon_9" class="li_icon">&nbsp;</div><a href="oracle.php?leftshow=9">new report</a>
        </li>    </ul>
</div>

<div id="right" style="border-top-left-radius: 3px 3px; border-top-right-radius: 3px 3px; border-bottom-left-radius: 3px 3px; border-bottom-right-radius: 3px 3px; ">
	    <div id="searchbox" style="height: 26px; width: 673px; padding-left: 10px; float: left; border-top-left-radius: 3px 3px; border-top-right-radius: 3px 3px; border-bottom-left-radius: 3px 3px; border-bottom-right-radius: 3px 3px; ">
		<div style="margin-top:6px;">
			<span>应用--${opsName}--</span>
		</div>    
		</div>
		<div id="listtable" style="width: 950px; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; border-top-style: solid; border-right-style: solid; border-bottom-style: solid; border-left-style: solid; border-top-color: rgb(242, 123, 4); border-right-color: rgb(242, 123, 4); border-bottom-color: rgb(242, 123, 4); border-left-color: rgb(242, 123, 4); background-color: white; border-top-left-radius: 3px 3px; border-top-right-radius: 3px 3px; border-bottom-left-radius: 3px 3px; border-bottom-right-radius: 3px 3px; ">
		<div style="height:26px;width:100%;background-color:#EDF0F4;">
			<div style="float:left;margin-top:8px;width:55%;"><span style="font-weight:bold">&nbsp;&nbsp;主机列表</span></div>
			<div style="float:right;margin-right:5px;width:40%;font-size:12px;height:30px;"></div>
		</div>
	<div  class="table table-striped table-condensed table-bordered" style="width:100%">
		<table class="showtable">
		<thead>
			<tr>
				<td rowspan="2" align="center"  width="100">依赖应用</td>
				<td rowspan="2" align="center" width="100">应用类型</td>
				<td rowspan="2" align="center" width="100">强弱关系</td>
				<td height="16" colspan="4" align="center">强弱验证结果</td>
				<td rowspan="2" align="center" width="100">自动执行</td>
				<td rowspan="2" align="center" width="100">时间设定</td>
				<td rowspan="2" align="center"  width="160">操作</td>
			</tr>
			<tr>
			  <td align="center"  width="60">屏蔽启动</td>
				<td  align="center" width="60">屏蔽运行</td>
				<td align="center" width="60">延迟启动</td>
				<td  align="center" width="60">延迟运行</td>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="config">
			<tr>
				<td>${config.targetOpsName}</td>
				<td>${config.targetAppType}</td>
				<td>${config.expectDepIntensity}</td>
				<td>${config.startPreventIntensity}</td>
				<td>${config.runPreventIntensity}</td>
				<td>${config.startDelayIntensity}</td>
				<td>${config.runDelayIntensity}</td>
				<td>${config.autoRun}</td>
				<td>${config.autoRunTime}</td>
				<td>查看 修改 删除 启动</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
</div>
</div>


</body>
</html>