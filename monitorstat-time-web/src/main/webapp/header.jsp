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
			</a> <a class="brand" href="#">ʵʱ���ϵͳ </a>
			<div class="nav-collapse">
				<ul class="nav nav-pills">
					<li><a 
						href="javascript:location.href='<%=request.getContextPath()%>/app/detail/perf/show.do?method=showIndex&appId='+getSelectAppId()">��������
							<b class="caret"></b>
					</a></li>
					<c:if test="${leftmenu.pv == true }">
					<li id="provoidHSF"><a
						href="javascript:location.href='<%=request.getContextPath()%>/app/detail/apache/show.do?method=showIndex&appId='+getSelectAppId()">WEB������Ϣ<b class="caret"></b></a>
					</li>
					</c:if>
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#">Ӧ���������� <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<c:if test="${leftmenu.tairConsumer == true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/tair/show.do?method=showIndex&appId='+getSelectAppId()">Tair������Ϣ</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.tairProvider == true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/tairProvider/show.do?method=gotoHost&appId='+getSelectAppId()">Tair�ṩ��Ϣ</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.search == true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/search/show.do?method=showIndex&appId='+getSelectAppId()">Search��Ϣ</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.exception== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId='+getSelectAppId()">�쳣��Ϣ</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.mbean== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/mbean/show.do?method=showIndex&appId='+getSelectAppId()">MBean��Ϣ</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.uicfinal== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/uicfinal/consumer/show.do?method=showIndex&appId='+getSelectAppId()">uicfinal��Ϣ</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.tbsession== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/tbsession/show.do?method=showIndex&appId='+getSelectAppId()">tbsession��Ϣ</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.tddl== true }">
							<li id="provoidHSF"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/tddl/consumer/show.do?method=showIndex&appId='+getSelectAppId()">TDDL������Ϣ</a>
							</li>
							</c:if>
							<c:if test="${leftmenu.notifyConsumer == true }">
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/notify/consumer/show.do?method=showIndex&appId='+getSelectAppId()">notify����</a></li>
							</c:if>
							<c:if test="${leftmenu.notifyProvider == true }">
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/notify/provider/show.do?method=showIndex&appId='+getSelectAppId()">notify�ṩ</a></li>
							</c:if>
							<%
								if(poheader.isB2bOcean()) {
									%>
<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRoot&keyName=Ocean_Service_Application&appId='+getSelectAppId()">ocean api����</a></li>
<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRoot&keyName=Ocean_Service_Application_Reverse&appId='+getSelectAppId()">ocean app����</a></li>
<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRoot&keyName=Ocean_Service_Exception&appId='+getSelectAppId()">Ocean�쳣</a></li>
<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRootHistory&keyName=Ocean_Service_Application&appId='+getSelectAppId()">����ͳ�ƣ���ʱ��</a></li>
									<%
								}
							%>
							<%
								if(poheader.isChongzhi()) {
									%>
								<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/detail/customer/show.do?method=showIndex&appId='+getSelectAppId()">�鿴��ֵ</a></li>
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
					<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">HSF������Ϣ <b class="caret"></b></a>
						<ul class="dropdown-menu">
						<c:if test="${leftmenu.hsf == true }">
							<li id="meDependAll"><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex&appId='+getSelectAppId()">HSF������Ϣ</a>
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
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfRefer&appId='+getSelectAppId()">HSF�����ҵ�</a>
							</li>
							</c:if>
						</ul></li>
					<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#">Ӧ�ù�����Ϣ <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a href="javascript:location.href='<%=request.getContextPath()%>/app/config.do?method=editAppNavi&appId='+getSelectAppId()">����������</a></li>						
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/custom/show.do?method=indexNaviApp&appId='+getSelectAppId()">Ӧ�õ�������</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app_info.do?method=edit&appId='+getSelectAppId()">Ӧ����Ϣ</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/conf/host/show.do?method=showIndex&appId='+getSelectAppId()">Ӧ�û����б����</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/conf/url/show.do?method=showIndex&appId='+getSelectAppId()">Ӧ��URL����</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/config/dependconfig.do?method=searchAppConfigList&appId='+getSelectAppId()">Ӧ����������</a></li>
							<li class="divider"></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/detail/alarm/show.do?method=IpShield&appId='+getSelectAppId()">ip���β���</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/alarm/show.do?method=showIndex&appId='+getSelectAppId()">�澯��¼��ѯ</a></li>
							<li class="divider"></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/conf/key/show.do?method=showIndex&appId='+getSelectAppId()">Ӧ��Key����</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/app/conf/key/show.do?method=alarmKeyList&appId='+getSelectAppId()">Ӧ�ø澯Key</a></li>
							<li><a
								href="javascript:location.href='<%=request.getContextPath()%>/config/dependconfig.do?method=showSourceKeyList&appId='+getSelectAppId()">Key��������</a></li>
						</ul></li>
						
						<%
						 CspUserInfoPo userInfo = SessionUtil.getCspUserInfo(request);
						if(userInfo != null){
						%>
							<li class="dropdown"><a class="dropdown-toggle"
						data-toggle="dropdown" href="#">�ҵ����� <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li ><a href="javascript:location.href='<%=request.getContextPath()%>/app/detail/custom/show.do?method=indexNavi&appId='+getSelectAppId()">�Զ�������</a></li>
  							<li ><a href="javascript:location.href='<%=request.getContextPath()%>/user.do?method=display'" >�޸ĸ澯����</a></li>
						</ul></li>
						<%} %>
						
					<li class="dropdown">
					<a class="dropdown-toggle"	data-toggle="dropdown" href="#">2012��ٱر�<b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li ><a href="<%=request.getContextPath()%>/index.do?method=showIndex2012">��è����</a></li>
						<li ><a href="<%=request.getContextPath()%>/tmall/index_table.jsp">��è���</a></li>
						<li ><a href="<%=request.getContextPath()%>/index.jsp">���д���</a></li>
						<li ><a href="<%=request.getContextPath()%>/index_table.jsp">���б��</a></li>
						<li ><a href="<%=request.getContextPath()%>/index_table_jinrong.jsp">���ڱ��</a></li>
						<li ><a href="<%=request.getContextPath()%>/app/depend/query/show.do?method=gotoHotInterfacePage">Ӧ��URL�ͽӿ�</a></li>
						<li ><a href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showAppException">Ӧ���쳣����</a></li>
						<li ><a href="<%=request.getContextPath()%>/app/report.do?method=reportAppRTDistribution">��Ӧ����ͳ��</a></li>
						<li ><a href="<%=request.getContextPath()%>/time/capacity/realtimeCapacity.jsp" target= "_blank">ʵʱ������Ϣ</a></li>
					</ul>
					</li>
					<li class="dropdown" ><a class="dropdown-toggle"
						data-toggle="dropdown" href="#" style="font:40px;color:yellow">CSP����ϵͳ <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a
								href="http://cm.taobao.net:9999/monitorstat/index_day.jsp">�ձ�ͳ��</a>
							</li>
							<li><a
								href="http://depend.csp.taobao.net:9999/depend/show/hsfconsume.do?method=showAppCenterConsumeHsfInfo&opsName=detail">ϵͳ����</a>
							</li>
							<li><a
								href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit">�����滮</a>
							</li>
							<li><a
								href="http://sentinel.taobao.net:9999/sentinel/">sentinel</a>
							</li>	
							<li><a
								href="http://capacity.taobao.net:9999/cost/index.do">�ɱ�����</a>
							</li>							
						</ul></li>
				</ul>
			</div>
			<p class="navbar-text pull-right">
			<%
			
			if(userInfo == null){
			%>
				<a href="<%=request.getContextPath()%>/user.do?method=login">��¼</a>
			<%
			}else{
			%>
				<a  href="<%=request.getContextPath()%>/user.do?method=display" title="����޸��ҵ���Ϣ"><%=userInfo.getMail() %></a>
			<%} %>
			</p>
		</div>
	</div>
</div>

<script type="text/javascript" src="http://js.tongji.linezing.com/3181580/tongji.js"></script><noscript><a href="http://www.linezing.com"><img src="http://img.tongji.linezing.com/3181580/tongji.gif"/></a></noscript>
