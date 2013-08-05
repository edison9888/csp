<%@ page import="com.taobao.csp.alarm.tddl.TddlChecker" %>
<%@ page import="com.taobao.csp.alarm.tddl.TddlRuleInfo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    String targetIp = request.getParameter("targetIp");
    String appId = request.getParameter("appId");
    String dbName = request.getParameter("dbName");
    String appName = request.getParameter("appName");
    String dbGroupQuantity = request.getParameter("dbGroupQuantity");
    String dbGroupKeyFormat = request.getParameter("dbGroupKeyFormat");

    TddlRuleInfo ruleInfo = new TddlRuleInfo();
    ruleInfo.setTargetIp(targetIp);
    ruleInfo.setAppId(appId);
    ruleInfo.setDbName(dbName);
    ruleInfo.setAppName(appName);
    ruleInfo.setDbGroupKeyFormat(dbGroupKeyFormat);
    ruleInfo.setDbGroupQuantity(dbGroupQuantity);
    if(ruleInfo.checkParms()) {
        TddlChecker.addTddlRuleInfo(ruleInfo);
    }
    List<TddlRuleInfo> tddlRuleInfos = TddlChecker.getTddlRuleInfoList();
%>
<form action="./addTddlRule.jsp" method="post">
    <table>
        <tr>
            <td>targetIp:</td><td><input type="text" name="targetIp"></td>
            <td>appId:</td><td><input type="text" name="appId"></td>
            <td>dbName:</td><td><input type ="text" name="dbName"></td>
        </tr>
        <tr>
            <td>appName:</td><td><input type="text" name="appName"></td>
            <td>dbGroupQuantity:</td><td><input type="text" name="dbGroupQuantity"></td>
            <td>dbGroupKeyFormat:</td><td><input type="text" name="dbGroupKeyFormat"></td>
        </tr>
    </table>
    <input type="submit"  value="提交">
</form>
务必填写完整信息才能更新，提交将覆盖当前配置的信息。
</br></br></br>
<%
    if(tddlRuleInfos != null) {
%>
<table border="1">
    <thead>
    <tr>
        <td>当前配置的机器信息</td>
    </tr>
    <tr>
        <th>应用名称</th>
        <th>应用Id</th>
        <th>数据库名</th>
        <th>机器IP</th>
        <th>dbGroup数量</th>
        <th>dbGroupKey格式</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        for(TddlRuleInfo info : tddlRuleInfos) {
    %>
    <tr>
        <td><%=info.getAppName()%></td>
        <td><%=info.getAppId()%></td>
        <td><%=info.getDbName()%></td>
        <td><%=info.getTargetIp()%></td>
        <td><%=info.getDbGroupQuantity()%></td>
        <td><%=info.getDbGroupKeyFormat()%></td>
        <td><a href="./deleteTddlRule.jsp?appName=<%=info.getAppName()%>&dbName=<%=info.getDbName()%>" onclick="{if(confirm('确定要删除这条配置吗?')){return true;}return false;}">删除</a></td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
<%
}else {
%>
无配置信息
<%
    }
%>
</body>
</html>