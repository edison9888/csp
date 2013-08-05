<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%
	String groupName = "";
	if(StringUtils.isNotBlank(request.getParameter("groupName"))){
		groupName = URLDecoder.decode(request.getParameter("groupName"),"utf-8");
		List<AppInfoPo> listApp = AppInfoAo.get().findAppInfoByGroupName(groupName);
		for(AppInfoPo po:listApp){
			out.println("<option value='"+po.getAppId()+"'>"+po.getAppName()+"</option>");
		}
	}
%>