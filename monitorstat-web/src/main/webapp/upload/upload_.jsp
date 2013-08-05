<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.File"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>上传</title>
</head>
<body>
<%

String realpath = request.getRealPath("/");
String filePath = realpath+"/statics/upload/";


boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//Create a factory for disk-based file items
DiskFileItemFactory factory = new DiskFileItemFactory();
// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload(factory);

// Set overall request size constraint
upload.setSizeMax(10240);
// Parse the request
List<FileItem> /* FileItem */ items = upload.parseRequest(request);
Iterator<FileItem> iter = items.iterator();
while (iter.hasNext()) {
    FileItem item = iter.next();
    if (item.isFormField()) {
    	String name = item.getFieldName();
    	String value = item.getString();
    } else {
    	String fieldName = item.getFieldName();
        String fileName = item.getName();
        String contentType = item.getContentType();
        boolean isInMemory = item.isInMemory();
        long sizeInBytes = item.getSize();
    	File file = new File(filePath+fileName);
    	item.write(file);
    }
}

%> 
</body>

</html>