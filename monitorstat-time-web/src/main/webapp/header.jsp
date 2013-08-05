<%@page import="com.taobao.monitor.common.po.CspUserInfoPo"%>
<%@page import="com.taobao.csp.time.web.session.SessionUtil"%>
<%@page import="com.taobao.csp.other.changefree.ChangeFreeInfo"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.taobao.csp.time.custom.ao.NaviAo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo" %>
<%@page import="com.taobao.csp.time.web.po.UserCustomNaviMainPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.time.cache.AppLeftMenuCache"%>
<%@page import="com.taobao.csp.time.web.po.LeftMenuPo"%>
<%@page import="com.taobao.csp.time.custom.arkclient.ArkDomain"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-float.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css"
	media="screen" />
<style>
#to-right,#to-left {
	width: 60px;
	color: #FFF;
	font-size: 14px;
	font-weight: bold;
	text-align: center;
	cursor: pointer;
}
</style>
<%
		AppInfoPo appInfo = (AppInfoPo)request.getAttribute("appInfo");
		if(appInfo==null){appInfo = AppInfoAo.get().findAppInfoById(1);request.setAttribute("appInfo", appInfo);}
		LeftMenuPo poheader = AppLeftMenuCache.get().query(appInfo.getAppName());
		request.setAttribute("leftmenu", poheader);
	%>
<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse"> <span class="i-bar"></span> <span
				class="i-bar"></span> <span class="i-bar"></span>
			</a> <a class="brand" href="#">实时监控系统 </a>
			<div class="nav-collapse">
				<ul class="nav nav-pills">
					<li><a 
						href="javascript:location.href='<%=request.getContextPath()%>/app/detail/perf/show.do?method=showIndex&appId='+getSelectAppId()">性能数据
							<b class="caret"></b>
					</a></li>
					<c:if test="${leftmenu.pv == true }">
					<li id="provoidHSF"><a
						href="javascript:location.href='<%=request.getContextPath()%>/app/detail/apache/show.do?method=showIndex&appId='+getSelectAppId()">WEB流量信息<b class="caret"></b></a>
					</li>
					</c:if>
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#">应用运行数据 <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<c:if test="${leftmenu.tairConsumer == true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/tair/show.do?method=showIndex&appId='+getSelectAppId()">Tair调用信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.tairProvider == true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/tairProvider/show.do?method=gotoHost&appId='+getSelectAppId()">Tair提供信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.search == true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/search/show.do?method=showIndex&appId='+getSelectAppId()">Search信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.exception== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId='+getSelectAppId()">异常信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.mbean== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/mbean/show.do?method=showIndex&appId='+getSelectAppId()">MBean信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.uicfinal== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/uicfinal/consumer/show.do?method=showIndex&appId='+getSelectAppId()">uicfinal信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.tbsession== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/tbsession/show.do?method=showIndex&appId='+getSelectAppId()">tbsession信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.tddl== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/tddl/consumer/show.do?method=showIndex&appId='+getSelectAppId()">TDDL调用信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.notifyConsumer == true }">
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/notify/consumer/show.do?method=showIndex&appId='+getSelectAppId()">notify消耗</a></li>
							</c:if>
							<c:if test="${leftmenu.notifyProvider == true }">
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/notify/provider/show.do?method=showIndex&appId='+getSelectAppId()">notify提供</a></li>
							</c:if>
							<%
								if(poheader.isB2bOcean()) {
									%>
<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRoot&keyName=Ocean_Service_Application&appId='+getSelectAppId()">ocean api调用</a></li>
<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRoot&keyName=Ocean_Service_Application_Reverse&appId='+getSelectAppId()">ocean app调用</a></li>
<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRoot&keyName=Ocean_Service_Exception&appId='+getSelectAppId()">Ocean异常</a></li>
<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRootHistory&keyName=Ocean_Service_Application&appId='+getSelectAppId()">汇总统计（临时）</a></li>
									<%
								}
							%>
							<%
								if(poheader.isChongzhi()) {
									%>
								<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/detail/customer/show.do?method=showIndex&appId='+getSelectAppId()">查看充值</a></li>
									<%
								}
							%>							
							<li class="divider"></li>
							<%
								List<UserCustomNaviMainPo> navis2 = new ArrayList<UserCustomNaviMainPo>();
								AppInfoPo appInfo2 = (AppInfoPo) request.getAttribute("appInfo");
								NaviAo.get().findNavisByAppName(appInfo2.getAppName(), navis2);
								request.setAttribute("navis2", navis2);
							%>
							<c:forEach var="navi2" items="${navis2 }">
								<li><a
									href="javascript:location.href='<%=request.getContextPath()%>/app/detail/custom/show.do?method=showIndex&naviId=${navi2.naviId}&appId='+getSelectAppId()">
										${navi2.naviName }</a></li>
							</c:forEach>
						</ul></li>
					<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">HSF调用信息 <b class="caret"></b></a>
						<ul class="dropdown-menu">
						<c:if test="${leftmenu.hsf == true }">
							<li id="meDependAll"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex&appId='+getSelectAppId()">HSF调用信息</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.hsfProvider == true }">
							<li id="meDependAll"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProvider&appId='+getSelectAppId()">HSF-Provider</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.hsfConsumer == true }">
							<li id="meDependHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/hsf/consumer/show.do?method=gotohsfConsumer&appId='+getSelectAppId()">HSF-Consumer</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.hsfRefer == true }">
							<li id="meDependTair"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfRefer&appId='+getSelectAppId()">HSF依赖我的</a>
							</li>
							</c:if>
						</ul></li>
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#">应用管理信息 <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/config.do?method=editAppNavi&appId='+getSelectAppId()">主导航配置</a></li>						
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/custom/show.do?method=indexNaviApp&appId='+getSelectAppId()">应用导航配置</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app_info.do?method=edit&appId='+getSelectAppId()">应用信息</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/conf/host/show.do?method=showIndex&appId='+getSelectAppId()">应用机器列表管理</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/conf/url/show.do?method=showIndex&appId='+getSelectAppId()">应用URL管理</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/config/dependconfig.do?method=searchAppConfigList&appId='+getSelectAppId()">应用依赖配置</a></li>
							<li class="divider"></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/alarm/show.do?method=IpShield&appId='+getSelectAppId()">ip屏蔽操作</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/alarm/show.do?method=showIndex&appId='+getSelectAppId()">告警记录查询</a></li>
							<li class="divider"></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/conf/key/show.do?method=showIndex&appId='+getSelectAppId()">应用Key管理</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/conf/key/show.do?method=alarmKeyList&appId='+getSelectAppId()">应用告警Key</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/config/dependconfig.do?method=showSourceKeyList&appId='+getSelectAppId()">Key依赖配置</a></li>
						</ul></li>
						
						<%
						 CspUserInfoPo userInfo = SessionUtil.getCspUserInfo(request);
						if(userInfo != null){
						%>
							<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#">我的配置 <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li ><a href="javascript:location.href='<%=request.getContextPath()%>/app/detail/custom/show.do?method=indexNavi&appId='+getSelectAppId()">自定义配置</a></li>
  							<li ><a href="javascript:location.href='<%=request.getContextPath()%>/user.do?method=display'" >修改告警接收</a></li>
						</ul></li>
						<%} %>
						
					<li class="dropdown">
					<a class="dropdown-toggle"	data-toggle="dropdown" href="#">2012大促必备<b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li ><a href="<%=request.getContextPath()%>/index.do?method=showIndex2012">天猫大盘</a></li>
						<li ><a href="<%=request.getContextPath()%>/tmall/index_table.jsp">天猫表格</a></li>
						<li ><a href="<%=request.getContextPath()%>/index.jsp">集市大盘</a></li>
						<li ><a href="<%=request.getContextPath()%>/index_table.jsp">集市表格</a></li>
						<li ><a href="<%=request.getContextPath()%>/index_table_jinrong.jsp">金融表格</a></li>
						<li ><a href="<%=request.getContextPath()%>/app/depend/query/show.do?method=gotoHotInterfacePage">应用URL和接口</a></li>
						<li ><a href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showAppException">应用异常总量</a></li>
						<li ><a href="<%=request.getContextPath()%>/app/report.do?method=reportAppRTDistribution">响应区间统计</a></li>
						<li ><a href="<%=request.getContextPath()%>/time/capacity/realtimeCapacity.jsp" target= "_blank">实时容量信息</a></li>
					</ul>
					</li>
					<li class="dropdown" ><a class="dropdown-toggle"
						data-toggle="dropdown" href="#" style="font:40px;color:yellow">CSP其他系统 <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a
								href="http://cm.taobao.net:9999/monitorstat/index_day.jsp">日报统计</a>
							</li>
							<li><a
								href="http://depend.csp.taobao.net:9999/depend/show/hsfconsume.do?method=showAppCenterConsumeHsfInfo&opsName=detail">系统依赖</a>
							</li>
							<li><a
								href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit">容量规划</a>
							</li>
							<li><a
								href="http://sentinel.taobao.net:9999/sentinel/">sentinel</a>
							</li>	
							<li><a
								href="http://capacity.taobao.net:9999/cost/index.do">成本中心</a>
							</li>							
						</ul></li>
				</ul>
			</div>
			<p class="navbar-text pull-right">
			<%
			
			if(userInfo == null){
			%>
				<a href="<%=request.getContextPath()%>/user.do?method=login">登录</a>
			<%
			}else{
			%>
				<a  href="<%=request.getContextPath()%>/user.do?method=display" title="点击修改我的信息"><%=userInfo.getMail() %></a>
			<%} %>
			</p>
		</div>
	</div>
</div>

<script type="text/javascript" src="http://js.tongji.linezing.com/3181580/tongji.js"></script><noscript><a href="http://www.linezing.com"><img src="http://img.tongji.linezing.com/3181580/tongji.gif"/></a></noscript>
