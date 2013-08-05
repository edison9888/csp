<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.File"%>
<%@page import="com.taobao.monitor.web.jprof.AutoJprofManage"%>
<%@page import="com.taobao.monitor.web.core.po.JprofHost"%>
<%@page import="com.taobao.monitor.web.ao.MonitorJprofAo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>上传</title>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<%
if(!UserPermissionCheck.check(request,"jprof","")){
	out.print("你没有权限操作!");
	return;
}


String id = null;
boolean isMultipart = ServletFileUpload.isMultipartContent(request);
DiskFileItemFactory factory = new DiskFileItemFactory();
ServletFileUpload upload = new ServletFileUpload(factory);
List<FileItem> items = upload.parseRequest(request);
Iterator<FileItem> iter = items.iterator();
while (iter.hasNext()) {
    FileItem item = iter.next();
    if (item.isFormField()) {
    	String name = item.getFieldName();
    	String value = item.getString();    	
    	if("id".equals(name)){
    		id = value;
    	}    	
    }
}

JprofHost h = MonitorJprofAo.get().getJprofHosts(Integer.parseInt(id));

String filePath = AutoJprofManage.get().getLocalTmpPath(h,AutoJprofManage.get().getUploadCollectDay());



iter = items.iterator();
while (iter.hasNext()) {
    FileItem item = iter.next();
    if (!item.isFormField()) {
    	String fieldName = item.getFieldName();
        String fileName = item.getName();
        String contentType = item.getContentType();
        boolean isInMemory = item.isInMemory();
        long sizeInBytes = item.getSize();
    	File file = new File(filePath);
    	item.write(file);
    }
}



AutoJprofManage.get().doParseJprofile(h);

response.sendRedirect(request.getContextPath ()+"/jprof/manage_jprof_class_method.jsp?appName="+h.getAppName()+"&collectDay="+AutoJprofManage.get().getUploadCollectDay());


%> 
</body>

</html>