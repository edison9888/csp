<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css" rel="stylesheet"/>
<title>常用链接</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/alarmconfig.do"
	method="get" class="form-horizontal" >
	<input type="hidden" value="searchAlarmConfig" name="method">
	 	<h1 align="center">我提供的HSF接口预警提醒</h1>
  <div class="control-group" style="display: none;">
    <label class="control-label" for="appName">应用名：</label>
    <div class="controls">
      <input type="text" name="appName" id="appName" 
      value="${alarmConfigPo.appName}"  placeholder="查询应用的报警配置">
      <button type="submit" class="btn">查询</button>
    </div>
  </div>		 	
</form>
<form class="form-horizontal" action="<%=request.getContextPath()%>/alarmconfig.do" 
method="post" id="alarm_form" name="alarm_form">
  	<input type="hidden" name="method" value="saveAlarmConfig">
	<input type="hidden" name="id" id="id" value="${alarmConfigPo.id}">
	<input type="hidden" name="alarmMode" id="alarmMode" value="${alarmConfigPo.alarmMode}">
  <div class="control-group">
    <label class="control-label" for="appName">应用名：</label>
    <div class="controls">
      <input type="text" name="appName" id="appName" 
      value="${alarmConfigPo.appName}" placeholder="应用名" class="input-xxlarge">
    </div>
  </div>	
  <div class="control-group">
    <label class="control-label" for="emailString">邮箱：</label>
    <div class="controls">
      <input type="text" name="emailString" id="emailString" 
      value="${alarmConfigPo.emailString}" placeholder="英文逗号分隔" class="input-xxlarge">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="wangwangString">报警旺旺：</label>
    <div class="controls">
      <input type="text" name="wangwangString" id="wangwangString" 
      value="${alarmConfigPo.wangwangString}" placeholder="英文逗号分隔" class="input-xxlarge">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="daysPre">对比时间间隔</label>
    <div class="controls">
      <input type="text" name="daysPre" id="daysPre" 
      value="${alarmConfigPo.daysPre}" placeholder="数字，默认为1（比较前一天数据）" class="input-xxlarge">
    </div>
  </div>    
 <div class="control-group">
    <div class="controls">
    	<!-- saveHsfAlarm 方法在map_index.jsp中定义 -->
      <button type="button" class="btn" onclick="saveHsfAlarm()">保存</button>
    </div>
  </div>
</form>
</body>
</html>