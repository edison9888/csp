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
<title>��������</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/alarmconfig.do"
	method="get" class="form-horizontal" >
	<input type="hidden" value="searchAlarmConfig" name="method">
	 	<h1 align="center">���ṩ��HSF�ӿ�Ԥ������</h1>
  <div class="control-group" style="display: none;">
    <label class="control-label" for="appName">Ӧ������</label>
    <div class="controls">
      <input type="text" name="appName" id="appName" 
      value="${alarmConfigPo.appName}"  placeholder="��ѯӦ�õı�������">
      <button type="submit" class="btn">��ѯ</button>
    </div>
  </div>		 	
</form>
<form class="form-horizontal" action="<%=request.getContextPath()%>/alarmconfig.do" 
method="post" id="alarm_form" name="alarm_form">
  	<input type="hidden" name="method" value="saveAlarmConfig">
	<input type="hidden" name="id" id="id" value="${alarmConfigPo.id}">
	<input type="hidden" name="alarmMode" id="alarmMode" value="${alarmConfigPo.alarmMode}">
  <div class="control-group">
    <label class="control-label" for="appName">Ӧ������</label>
    <div class="controls">
      <input type="text" name="appName" id="appName" 
      value="${alarmConfigPo.appName}" placeholder="Ӧ����" class="input-xxlarge">
    </div>
  </div>	
  <div class="control-group">
    <label class="control-label" for="emailString">���䣺</label>
    <div class="controls">
      <input type="text" name="emailString" id="emailString" 
      value="${alarmConfigPo.emailString}" placeholder="Ӣ�Ķ��ŷָ�" class="input-xxlarge">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="wangwangString">����������</label>
    <div class="controls">
      <input type="text" name="wangwangString" id="wangwangString" 
      value="${alarmConfigPo.wangwangString}" placeholder="Ӣ�Ķ��ŷָ�" class="input-xxlarge">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="daysPre">�Ա�ʱ����</label>
    <div class="controls">
      <input type="text" name="daysPre" id="daysPre" 
      value="${alarmConfigPo.daysPre}" placeholder="���֣�Ĭ��Ϊ1���Ƚ�ǰһ�����ݣ�" class="input-xxlarge">
    </div>
  </div>    
 <div class="control-group">
    <div class="controls">
    	<!-- saveHsfAlarm ������map_index.jsp�ж��� -->
      <button type="button" class="btn" onclick="saveHsfAlarm()">����</button>
    </div>
  </div>
</form>
</body>
</html>