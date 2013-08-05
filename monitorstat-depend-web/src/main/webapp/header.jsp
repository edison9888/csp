<%@ page  contentType="text/html; charset=GBK"%>
<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="i-bar"></span>
            <span class="i-bar"></span>
            <span class="i-bar"></span>
          </a>
			<a class="brand" href="#">依赖系统&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
			<div class="nav-collapse">
				<ul class="nav">
					<li >
						<a href="http://cm.taobao.net:9999/monitorstat/index_day.jsp">日报统计</a>
					</li>
					<li >
						<a href="http://time.csp.taobao.net:9999/time/">实时监控</a>
					</li>
					<li class="active">
						<a href="http://depend.csp.taobao.net:9999/depend/show/hsfconsume.do?method=showAppCenterConsumeHsfInfo&opsName=detail">系统依赖</a>
					</li>
					<li>
						<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit">容量规划</a>
					</li>
					<li>
						<a href="http://sentinel.taobao.net:9999/sentinel/">sentinel</a>
					</li>					
				</ul>
			</div>
			
			<p class="navbar-text pull-right"><a href="<%=request.getContextPath() %>/update.do?method=display">${domainName}</a></p>
		</div>
	</div>
</div>
