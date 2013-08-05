<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<title>常用链接</title>
</head>
<body>
<div>
开始同步数据：http://**:8080/depend/manualtrigger.do?method=startDumpOnlineData<br>
显示检测结果：http://**:8080/depend/checkupdepend.do?method=showCheckResult&opsName=itemcenter&targetOpsName=diamond<br>
强弱依赖报表地址：http://10.232.135.198:8080/depend/show/reportaction.do?method=dependAutoCheckReport&selectDate=2012-05-10<br>
手动应用关系检查同步:http://depend.csp.taobao.net:9999/depend/manualtrigger.do?method=bulidAppDepRelation<br>
手动端口检测：http://**:8080/depend/checkupdepend.do?method=checkupDepend&opsName=<br>
手动调用callHostSync，强制同步PE的机器数据：http://**:8080/depend/manualtrigger.do?method=callHostSync<br>
手动调用拉去Eagleeye数据到Relationship中：http://**:8080/depend/manualtrigger.do?method=dumpEagleEyeDataToCsp<br>
手动调用分析Relationship的数据入库到CSP：http://**:8080/depend/manualtrigger.do?method=callEagleeyeAnalysis<br>
把Eagleeye的小时表数据导入到日表：http://depend.csp.taobao.net:9999/depend/manualtrigger.do?method=bulidAppDepRelation<br>
</div>
</body>
</html>