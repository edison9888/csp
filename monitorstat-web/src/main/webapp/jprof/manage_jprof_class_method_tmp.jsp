<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.core.po.JprofHost"%>
<%@page import="com.taobao.monitor.web.core.po.JprofClassMethod"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<style>
.report_on{background:#bce774;}
</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/dynatree/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/dynatree/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/dynatree/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/dynatree/jquery.dynatree.js"></script>
<link href='<%=request.getContextPath() %>/statics/dynatree/skin/ui.dynatree.css' rel='stylesheet' type='text/css'>

<style type="text/css">
      #tree {
      vertical-align: top;
        width: 250px;
      }
      iframe {
        border: 1px dotted gray;
      }
  </style>    
  
  
  

</head>
<%
String appName = request.getParameter("appName");
String collectDay = request.getParameter("collectDay");

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

if(collectDay==null){
	collectDay = sdf.format(new Date());
}

%>
<body class="example"> 
<form action="./manage_jprof_class_method.jsp" method="get">
  <table>
  	<tr>
  		<td>
  			应用名:<input type="text" name="appName" value="<%=appName==null?"":appName %>">时间<input type="text" name="collectDay" value="<%=collectDay %>">
  		</td>
  	</tr>
  	<tr>
  		<td>
  			<input type="submit" value="查看jprof信息">
  		</td>
  	</tr>
  </table>
 </form>
  <%
  

  
  
  if(appName != null&&collectDay != null){
  
  List<JprofClassMethod> listClass = MonitorJprofAo.get().findJprofClassMethod(appName,collectDay);
  
  Map<String,List<JprofClassMethod>> mapClass = new HashMap<String,List<JprofClassMethod>>();
  
  for(JprofClassMethod method:listClass){
	  String className = method.getClassName();
	  
	  List<JprofClassMethod> list =  mapClass.get(className);
	  if(list == null){
		  list = new ArrayList<JprofClassMethod>();
		  mapClass.put(className,list);
	  }	  
	  list.add(method);	  
  }
  
  
  
  
  %>


  

  

 <script type='text/javascript'>

	var dataArray = [];
	<%
	for(int i = 0;i<listClass.size();i++){
		JprofClassMethod c = listClass.get(i);
	%>
	dataArray[<%=i%>]={className:"<%=c.getClassName()%>",methodName:"<%=c.getMethodName()%>"};
	<%}%>
 
    $(function(){
    $("#tree").dynatree({
      rootVisible: false,
      persist: true,
      onPostInit: function(isReloading, isError) {
         this.reactivate();
      },
      onActivate: function(dtnode) {
        if( dtnode.data.url )
          $("[name=contentFrame]").attr("src", dtnode.data.url);
      }
    });



    var rootNode = $("#tree").dynatree("getRoot");
	for(var i=0;i<dataArray.length;i++){
		var data = dataArray[i];
		var classname = data.className;
		var methodName = data.methodName;
		var tmp = classname.split("/");
		var tmpNode = rootNode;
		for(var j=0;j<tmp.length&&j<3;j++){
			var node = $("#tree").dynatree("getTree").getNodeByKey(tmp[j]);
			if(node == null){
				tmpNode = tmpNode.addChild({title: tmp[j], key: tmp[j],isFolder: true, expand: true});
			}else{
				tmpNode = node;
			}
			
		}
		tmpNode.addChild({title: methodName, key: methodName});
	}

    

	

    
  });   


     
   </script>
  
   
  <table>
  <colgroup>
    <col width="300px" valign="top">
    <col width="90%">
  </colgroup>
  <tr>
    <td valign="top">
        <div id="tree"> </div>
    </td>
    <td>
      <iframe src="sample-iframe-1.html" name="contentFrame" width="100%" height="500" 
              scrolling="yes" marginheight="0" marginwidth="0" frameborder="0">        
      </iframe>
    </td>
  </tr>
  </table>  
  
  
  <%} %>
</body>


</html>