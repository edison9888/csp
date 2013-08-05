<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.ao.center.HostAo"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.codec.digest.DigestUtils"%>
<%@page import="org.dom4j.io.SAXReader"%>
<%@page import="org.dom4j.Document"%>
<%@page import="java.net.URL"%>
<%@page import="org.dom4j.Node"%>
<%@page import="org.dom4j.Element"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>应用主机列表</title>


<%
List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();
Map<String, List<AppInfoPo>> groupAppMap = new HashMap<String, List<AppInfoPo>>();
for (AppInfoPo po : appList) {
	String groupName = po.getGroupName();
	List<AppInfoPo> list = groupAppMap.get(groupName);
	if (list == null) {
		list = new ArrayList<AppInfoPo>();
		groupAppMap.put(groupName, list);
	}
	list.add(po);
}

SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
String md5 = DigestUtils.md5Hex("taobao_daily"+sdf.format(new Date()));
String url = "http://proxy.wf.taobao.org/DailyManage/tree-xml.ashx?sign="+md5;

Map<String, String> appMap = new HashMap<String, String>();
SAXReader reader = new SAXReader();

Document doc = reader.read(new URL(url));
List<Node> fristNodeList = doc.selectNodes("/taobao/node/node");
for(int i=0;i<fristNodeList.size()&&i<1;i++){
	Element fristNode = (Element)fristNodeList.get(i);//第一个节点  产品线					
	String name = fristNode.attributeValue("name");
	String fristid = fristNode.attributeValue("id");
	
	List<Element> secondNodeList = (List<Element>)fristNode.selectNodes("node");
	
	for(Element secondNode:secondNodeList){
		String secondname = secondNode.attributeValue("name");//第二节点 开发组
		String secondnid = secondNode.attributeValue("id");
		List<Element> thirdNodeList = (List<Element>)secondNode.selectNodes("node");
		
		for(Element thirdNode:thirdNodeList){
			String thirdname = thirdNode.attributeValue("name");//第三节点 产品线		
			String thirdid = thirdNode.attributeValue("id");
			List<Element> fourNodeList = (List<Element>)thirdNode.selectNodes("node");
			
			
			for(Element fourNode:fourNodeList){
				String fourname = fourNode.attributeValue("name");//第四节点 应用
				String fourid = fourNode.attributeValue("id");
				appMap.put(fourname, name+"-"+secondname+"-"+thirdname);
				
			}
		}
	}
}

Element fristNode = (Element)fristNodeList.get(1);//第一个节点  产品线	
if (fristNode.attributeValue("name").equals("公共服务")) {
	List<Element> secondNodeList = (List<Element>)fristNode.selectNodes("node");
	for(Element secondNode : secondNodeList){
		String secondname = secondNode.attributeValue("name");//第二节点 开发组
		if (secondNode.attributeValue("name").equals("核心系统") || secondNode.attributeValue("name").equals("DB")) {
			List<Element> thirdNodeList = (List<Element>)secondNode.selectNodes("node");
			for(Element thirdNode:thirdNodeList){
				String thirdname = thirdNode.attributeValue("name");//第三节点 产品线		
				List<Element> fourNodeList = (List<Element>)thirdNode.selectNodes("node");
				for(Element fourNode:fourNodeList){
					String fourname = fourNode.attributeValue("name");//第四节点 应用
					String fourid = fourNode.attributeValue("id");
					appMap.put(fourname, "公共服务"+"-"+secondname+"-"+thirdname);
				}
			}
		}
	}
}
%>
</head>
<body>

<table border="1"  align="center" >
	<tr align="center">
		<td  align="center" width="150">ops名</td>
		<td  align="center" width="150">应用名</td>
		<td  align="center" width="200">应用分组</td>
		<td  align="center" width="200">ops分组</td>
		<td  align="center" width="50">生效</td>
		
		<td  align="center" width="50">调整</td>
	</tr>
	<%
	for (Map.Entry<String, List<AppInfoPo>> entry : groupAppMap.entrySet()) {
		String groupName = entry.getKey();
		List<AppInfoPo> groupAppList = entry.getValue();
		for (AppInfoPo app : groupAppList) {
		%>
			<tr>
				<td align="center"><%=app.getOpsName() %></td>
				<td align="center"><%=app.getAppName() %></td>
				<td align="center"><%=app.getGroupName() %></td>
				<td align="center"><%=appMap.get(app.getOpsName()) %></td>
				<td align="center"><%=app.getAppStatus()==0?"是":"否" %></td>
				<td align="center"><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/center/app_info_update.jsp?id=<%=app.getAppId() %>">调整</a></td>
			</tr>
		<%
		}
	%>
	
	<%
	}
%>
</table>
</body>
</html>