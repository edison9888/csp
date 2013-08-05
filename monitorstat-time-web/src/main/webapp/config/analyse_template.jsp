<%@ page language="java" import="java.util.*" pageEncoding="GBK" isELIgnored="false"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>

<html>
	<head>
		<title>统一采集模板配置界面</title>
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
		out.println("<h2 align=\"center\">后台解析的结果</h2>");
		if(flag == false) {
%>
	<div class="control-group">
		<label class="control-label" for="errorMsg">异常信息</label>
		<div class="controls">
			<input type="text" id="errorMsg" class="input-xxlarge"
				value="${errorMsg}">
		</div>
	</div>
<%
		} else {
			%>
	<div class="control-group">
		<label class="control-label" for="jsonString">生成的JSON</label>
		<div class="controls">
			<textarea rows="6" cols="1" id="jsonString" class="input-xxlarge">${jsonString}</textarea>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="result">解析日志得出的信息</label>
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

<h2 align="center">采集模板配置</h2>
<form class="form-horizontal" action="<%=request.getContextPath()%>/analyseconfig.do" method="get">
<input type="hidden" name="method" value="testRegularString">
  <div class="control-group">
    <label class="control-label" for="line">日志示范</label>
    <div class="controls">
      <textarea id="line" name="line" rows="3" class="input-xxlarge">
      ${line}
      </textarea>
 		<div>
      	格式举例:<br/>
      	<p><span style="font-size:18px;">日志格式:<br />
      	[2012-07-12 16:12:52] 1342080772593 Ocean_Service_Exception com.alibaba.ptqa.ocean.test HttpToHttpTest:1:10.20.162.50 InvokeMethodException 2 16<br />
      	</span></p>
      	<p><span style="font-size:18px;">[2012-07-12 16:12:52]可有可无</span></p>
      </div>      
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="logConfigWeb">
    	解析格式配置
    </label>
    <div class="controls">
      <input type="text" id="logConfigWeb" name = "logConfigWeb" class="input-xxlarge" value="${webConfig.logConfigWeb}"><br>
      <div>
      	格式举例:<br/>{datetime}&nbsp;{key_1}&nbsp;{key_2}:{key_3}:{key_4}&nbsp;{E-times}&nbsp;{C-time}<br/>
      	{datetime}为必须属性。<br/>
      	key的名称必须以key_开头<br/>
      	属性的名称必须为E-times或C-time或其他缓存中支持的属性。<br/>
      	<strong style="color: red;">
			配置的顺序要和日志相同，key值中间如果有空白符号，请用一个空格代替。      	
      	</strong>
      </div>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="timeFormat">时间格式</label>
    <div class="controls">
      <input type="text" id="timeFormat" name = "timeFormat" placeholder="long" class="input-xxlarge" value="${webConfig.timeFormat}">
      <div>
      	支持的值:long或其他java常见format的值，如yyyy-MM-dd等
      </div>        
    </div>
  </div>  
  <div class="control-group">
    <label class="control-label" for="keyConfig">key值配置</label>
    <div class="controls">
      <input type="text" id="keyConfig" name = "keyConfig" class="input-xxlarge" value="${webConfig.keyConfig}">
      <div>
      	格式举例:<br/>key_1,ALL;key_2,APP;key_3,HOST;key_4,NO;<br/>
      	key的名称与解析格式配置中key的配置相同<br/>
      	括号内为key对应的解析级别,目前只支持ALL,APP,HOST,NO四种<br/>
      </div>        
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="keyNumber">key的个数</label>
    <div class="controls">
      <input type="text" id="keyNumber" name = "keyNumber" class="input-xxlarge" value="${webConfig.keyNumber}">
    </div>
  </div>   
  <div class="control-group">
    <label class="control-label" for="valueConfig">value值配置</label>
    <div class="controls">
      <input type="text" id="valueConfig" name = "valueConfig" class="input-xxlarge" value="${webConfig.valueConfig}">
      <div>
      	格式举例:<br/>E-times,ADD;C-time,REPLACE;<br/>
      	value的名称与解析格式配置中value的名称相同<br/>
      	括号内为value支持的操作,目前只支持ADD,REPLACE两种<br/>
      </div>       
    </div>
  </div>  
  <div class="control-group">
    <label class="control-label" for="valueNumber">value的个数</label>
    <div class="controls">
      <input type="text" id="valueNumber" name = "valueNumber" class="input-xxlarge" value="${webConfig.valueNumber}">
    </div>
  </div>    
  <div class="control-group">
    <label class="control-label" for="keyOrder">发送顺序</label>
    <div class="controls">
      <input type="text" id="keyOrder" name = "keyOrder" class="input-xxlarge" value="${webConfig.keyOrder}">
      <div>
      	格式举例:<br/>key_1,key_2,key_3;key_3,key_2,key_1<br/>
      	最终发送数据时，拼装的key的顺序。按,和;分割<br/>
      </div>        
    </div>
    <font color="red">要注意，所有属性的值请务必相同。所以key的值请务必统一</font>
  </div>
  <div class="control-group">
    <div class="controls">
      <button type="submit" class="btn">提交测试</button>
    </div>
  </div>
</form>
</body>
</html>
