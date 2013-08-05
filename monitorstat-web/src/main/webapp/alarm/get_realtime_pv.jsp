<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<%@page import="com.taobao.monitor.time.ao.AppHSFQueryAo"%>
<%@page import="com.taobao.monitor.time.po.AppPvPO"%>
<%@page import="com.taobao.util.CollectionUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%
	String appName = (String)request.getParameter("appName");
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	List<String> urlList = new ArrayList<String>();
	if( "login".equals(appName) ){
		urlList.add(AppHSFQueryAo.LOGIN_URL);
	} else if("detail".equals(appName)){
		urlList.add(AppHSFQueryAo.DETAIL_URL);
	} else if("cart".equals(appName)){
		urlList.add(AppHSFQueryAo.CART_URL);
	} else if("tf_buy".equals(appName)){
		urlList.add(AppHSFQueryAo.TF_BUY_URL1);
		urlList.add(AppHSFQueryAo.TF_BUY_URL2);
		urlList.add(AppHSFQueryAo.TF_BUY_URL3);
	} else if("tmallbuy".equals(appName)){
		urlList.add(AppHSFQueryAo.TMALL_BUY_URL1);
		urlList.add(AppHSFQueryAo.TMALL_BUY_URL2);
	} else if("malldetail".equals(appName)){
		urlList.add(AppHSFQueryAo.MALL_DETAIL_URL);
	}
	List<AppPvPO> pvPoList = AppHSFQueryAo.get().queryAppPv(appName, urlList);
	%>		
<table width="100%" class="gradient-style">
	<thead>
		<tr>
			<th>时间</th>
			<th>PV量</th>
			<th>应用名称</th>
			<th>URL名称</th>
			<th>查看调用详情</th>
		</tr>
	</thead>
	<tbody>
	<%
	if(CollectionUtil.isNotEmpty(pvPoList)){
		for(AppPvPO po : pvPoList){
		%>
		<tr>
			<td><%=sdf.format(po.getPvTime())%></td>
			<td><%=po.getPvCount()%></td>
			<td><%=po.getAppName()%></td>
			<td><%=po.getUrlName()%></td>
			<td><a href="http://time.csp.taobao.net:9999/time/app/detail/history.do?method=showHistory&appName=<%=appName%>&keyName=PV" target="_blank">查看</a></td>
		</tr>
	<%
		}
	}
%>
	</tbody>
</table>