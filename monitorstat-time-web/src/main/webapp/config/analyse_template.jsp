<%@ page language="java" import="java.util.*" pageEncoding="GBK" isELIgnored="false"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>

<html>
	<head>
		<title>ͳһ�ɼ�ģ�����ý���</title>
		<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-source.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/raphael-min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_graffle.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_graph.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_algorithms.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>
</head>
<body>
<div class="form-horizontal" >
<%
	Boolean flag = (Boolean)request.getAttribute("flag");
	if(flag != null) {
		out.println("<h2 align=\"center\">��̨�����Ľ��</h2>");
		if(flag == false) {
%>
	<div class="control-group">
		<label class="control-label" for="errorMsg">�쳣��Ϣ</label>
		<div class="controls">
			<input type="text" id="errorMsg" class="input-xxlarge"
				value="${errorMsg}">
		</div>
	</div>
<%
		} else {
			%>
	<div class="control-group">
		<label class="control-label" for="jsonString">���ɵ�JSON</label>
		<div class="controls">
			<textarea rows="6" cols="1" id="jsonString" class="input-xxlarge">${jsonString}</textarea>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="result">������־�ó�����Ϣ</label>
		<div class="controls">
			<div class="input-xxlarge">
			${result}
			</div>
		</div>
	</div>
		<%			
		}
	}
%>
</div>

<h2 align="center">�ɼ�ģ������</h2>
<form class="form-horizontal" action="<%=request.getContextPath()%>/analyseconfig.do" method="get">
<input type="hidden" name="method" value="testRegularString">
  <div class="control-group">
    <label class="control-label" for="line">��־ʾ��</label>
    <div class="controls">
      <textarea id="line" name="line" rows="3" class="input-xxlarge">
      ${line}
      </textarea>
 		<div>
      	��ʽ����:<br/>
      	<p><span style="font-size:18px;">��־��ʽ:<br />
      	[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException 2 16<br />
      	</span></p>
      	<p><span style="font-size:18px;">[2012-07-12 16:12:52]���п���</span></p>
      </div>      
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="logConfigWeb">
    	������ʽ����
    </label>
    <div class="controls">
      <input type="text" id="logConfigWeb" name = "logConfigWeb" class="input-xxlarge" value="${webConfig.logConfigWeb}"><br>
      <div>
      	��ʽ����:<br/>{datetime}&nbsp;{key_1}&nbsp;{key_2}:{key_3}:{key_4}&nbsp;{E-times}&nbsp;{C-time}<br/>
      	{datetime}Ϊ�������ԡ�<br/>
      	key�����Ʊ�����key_��ͷ<br/>
      	���Ե����Ʊ���ΪE-times��C-time������������֧�ֵ����ԡ�<br/>
      	<strong style="color: red;">
			���õ�˳��Ҫ����־��ͬ��keyֵ�м�����пհ׷��ţ�����һ���ո���档      	
      	</strong>
      </div>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="timeFormat">ʱ���ʽ</label>
    <div class="controls">
      <input type="text" id="timeFormat" name = "timeFormat" placeholder="long" class="input-xxlarge" value="${webConfig.timeFormat}">
      <div>
      	֧�ֵ�ֵ:long������java����format��ֵ����yyyy-MM-dd��
      </div>        
    </div>
  </div>  
  <div class="control-group">
    <label class="control-label" for="keyConfig">keyֵ����</label>
    <div class="controls">
      <input type="text" id="keyConfig" name = "keyConfig" class="input-xxlarge" value="${webConfig.keyConfig}">
      <div>
      	��ʽ����:<br/>key_1,ALL;key_2,APP;key_3,HOST;key_4,NO;<br/>
      	key�������������ʽ������key��������ͬ<br/>
      	������Ϊkey��Ӧ�Ľ�������,Ŀǰֻ֧��ALL,APP,HOST,NO����<br/>
      </div>        
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="keyNumber">key�ĸ���</label>
    <div class="controls">
      <input type="text" id="keyNumber" name = "keyNumber" class="input-xxlarge" value="${webConfig.keyNumber}">
    </div>
  </div>   
  <div class="control-group">
    <label class="control-label" for="valueConfig">valueֵ����</label>
    <div class="controls">
      <input type="text" id="valueConfig" name = "valueConfig" class="input-xxlarge" value="${webConfig.valueConfig}">
      <div>
      	��ʽ����:<br/>E-times,ADD;C-time,REPLACE;<br/>
      	value�������������ʽ������value��������ͬ<br/>
      	������Ϊvalue֧�ֵĲ���,Ŀǰֻ֧��ADD,REPLACE����<br/>
      </div>       
    </div>
  </div>  
  <div class="control-group">
    <label class="control-label" for="valueNumber">value�ĸ���</label>
    <div class="controls">
      <input type="text" id="valueNumber" name = "valueNumber" class="input-xxlarge" value="${webConfig.valueNumber}">
    </div>
  </div>    
  <div class="control-group">
    <label class="control-label" for="keyOrder">����˳��</label>
    <div class="controls">
      <input type="text" id="keyOrder" name = "keyOrder" class="input-xxlarge" value="${webConfig.keyOrder}">
      <div>
      	��ʽ����:<br/>key_1,key_2,key_3;key_3,key_2,key_1<br/>
      	���շ�������ʱ��ƴװ��key��˳�򡣰�,��;�ָ�<br/>
      </div>        
    </div>
    <font color="red">Ҫע�⣬�������Ե�ֵ�������ͬ������key��ֵ�����ͳһ</font>
  </div>
  <div class="control-group">
    <div class="controls">
      <button type="submit" class="btn">�ύ����</button>
    </div>
  </div>
</form>
</body>
</html>
