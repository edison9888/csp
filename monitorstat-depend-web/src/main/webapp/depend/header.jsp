<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@page import="com.taobao.csp.depend.util.StartUpParamWraper"%>
<%@page import="com.taobao.csp.depend.util.StartUpParam"%>
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet" />
<link
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css"
	rel="stylesheet" />
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<style>
<!--
body {
	padding-bottom: 40px;
	padding-top: 60px;
}
-->
</style>
<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse"> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span> </a>
			<a class="brand" href="<%=request.getContextPath()%>/">����ϵͳ</a>
			<div class="nav-collapse">
				<ul class="nav">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">������Ϣ<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li id="consumeTddl">
								<a href="javascript:void(0)"
									onclick="gotoTddlPage('<%=request.getContextPath()%>','consumeTddl')">TDDL������Ϣ</a>
							</li>
							<li id="consumeTddl">
								<a href="javascript:void(0)"
									onclick="gotoTairSummaryPage('<%=request.getContextPath()%>','tairsummary')">Tair���ֲܷ�<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></a>
							</li>
							<li id="provideTfs">
								<a href="javascript:void(0)"
									onclick="gotoTfsPage('<%=request.getContextPath()%>','provideTfs')">TFS��Ϣͳ��</a>
							</li>							
							<li id="provideTfs">
								<a href="javascript:void(0)"
									onclick="gotoPVUVPage('<%=request.getContextPath()%>','provideTfs')">UV��PV��Ϣͳ��<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></a>
							</li>							
						</ul>
					</li>
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">�ҵ���ϸ��Ϣ<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li id="provoidHSF">
								<a href="javascript:void(0)"
									onclick="gotoAppIndexPage('<%=request.getContextPath()%>','appmain')">Ӧ����ҳ<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></a>
							</li>						
							<li id="provoidHSF">
								<a href="javascript:void(0)"
									onclick="gotoAppCenterHsfInfo('<%=request.getContextPath()%>','provide')">���ṩ��HSF��Ϣ</a>
							</li>
							<li id="consumeHSF">
								<a href="javascript:void(0)"
									onclick="gotoAppCenterHsfInfo('<%=request.getContextPath()%>','consume')">�����ѵ�HSF��Ϣ</a>
							</li>
							<li id="consumeTair">
								<a href="javascript:void(0)"
									onclick="gotoAppCenterTairInfo('<%=request.getContextPath()%>','consume')">�����ѵ�Tair��Ϣ</a>
							</li>
							<li id="consumeNotify">
								<a href="javascript:void(0)"
									onclick="gotoAppCenterNotifyInfo('<%=request.getContextPath()%>','consume')">�����ѵ�Notify��Ϣ</a>
							</li>
							<li id="originURL">
								<a href="javascript:void(0)"
									onclick="gotoURLPage('<%=request.getContextPath()%>','origin')">URL��Դͳ��</a>
							</li>
						</ul>
					</li>
					<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#">������ͼ<b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
							<li>
								<a href="javascript:void(0)" onclick="gotoDependMapPage('<%=request.getContextPath()%>')">������ͼ
								<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></a>
							</li>	
							<li>
								<a href="javascript:void(0)" onclick="gotoDependPathPage('<%=request.getContextPath()%>')">Ӧ�õ���·�����
								<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></a>
							</li>													
						</ul>					
					</li>						
					<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#">����ϵͳ���ϼ�����<b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
							<li>
								<a href="<%=request.getContextPath()%>/depend/contactme/contactme.jsp" target="_blank">ϵͳ˵��
								<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></a>
							</li>
							<li>
								<a href="<%=StartUpParamWraper.getEosUrl()%>" target="blank">ǿ������ϵͳ(EOS)</a>
							</li>							
						</ul>					
					</li>					
				</ul>
				<ul class="nav pull-right">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" style="color:rgb(0, 136, 204)">CSP����ϵͳ
							<b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
							  <li >
						<a href="http://cm.taobao.net:9999/monitorstat/index_day.jsp" class="brand2">�ձ�ͳ��</a>
					</li>
					<li>
						<a href="http://time.csp.taobao.net:9999/time" class="brand2">ʵʱ���</a>
					</li>
					<li  class="active">
						<a href="javascript:void(0)" class="brand2">ϵͳ����</a>
					</li>
					<li>
						<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit" class="brand2">�����滮</a>
					</li>
						</ul>
					</li>
				</ul>
				<p class="navbar-text pull-right">
					<a href="<%=request.getContextPath()%>/update.do?method=display">${domainName}</a>
				</p>
			</div>
		</div>
		<!-- /topbar-inner -->
	</div>
	<!-- /topbar -->
</div>
<script type="text/javascript" src="http://js.tongji.linezing.com/3207337/tongji.js"></script><noscript><a href="http://www.linezing.com"><img src="http://img.tongji.linezing.com/3207337/tongji.gif"/></a></noscript>

