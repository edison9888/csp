<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.management.MBeanOperationInfo"%>
<%@page import="javax.management.MBeanInfo"%>
<%@page import="javax.management.MBeanServerFactory"%>
<%@page import="javax.management.MBeanServer"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.management.ManagementFactory"%>
<%@page import="com.taobao.common.smonitor.MBeanServerService"%>
<%@page import="javax.management.ObjectName"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>


<%




ObjectName threadObjName = new ObjectName("Catalina:*"); 

List<MBeanServer> list = MBeanServerFactory.findMBeanServer(null);

for(MBeanServer m:list){
	
	out.println("MBeanServer:"+m.getDefaultDomain()+"<br/>");
}



ObjectName o = ObjectName.getInstance("Catalina:type=DataSource,context=/time,host=localhost,class=javax.sql.DataSource,name=\"csp_monitor\"");
if(o!= null){ 
	Object maxThreads = list.get(0).getAttribute(o, "maxActive");
	
	out.println("ObjectName maxActive:"+maxThreads);
	
}


List<String> names = new ArrayList<String>();

for(MBeanServer server:list){
	Set<ObjectName> ons  = MBeanServerFactory.newMBeanServer().queryNames(threadObjName, null);
	
	
	
	
	if(ons != null){
		for(ObjectName mo:ons){
			names.add(mo.getKeyPropertyListString());
			String p = mo.getKeyProperty("type");
			
			if(p!= null&&p.equals("DataSource")){ 
				Object maxThreads = server.getAttribute(mo, "maxActive");
				
				out.println("DataSource maxThreads:"+maxThreads);
				
			}
			
		}
	}
}
Collections.sort(names);
for(String name:names){
	out.println(""+name+"<br/>");
}

Set<ObjectName> objectNames = MBeanServerService.getObjectName("Catalina:type=ThreadPool,*");
out.println(objectNames);
if(objectNames != null){
	for(ObjectName name:objectNames){
		out.println(name.getDomain()+":"+name.getKeyPropertyListString());
	}
}

%>

</body>
</html>