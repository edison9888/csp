<%@ page language="java" contentType="text/html; charset=gb2312"
	pageEncoding="gb2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.po.CloudTagPo"%>
<%@page import="com.taobao.po.HotQueryPo"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=request.getContextPath()%>/assets/css/bootstrap.css" rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/assets/css/bootstrap-responsive.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/assets/css/docs.css" rel="stylesheet">	
<style type="text/css">
body {
	background: #F2F2F2;
	color: #555;
	font-family: Verdana, "BitStream vera Sans", Helvetica, Sans-serif;
	font-size: 14px
}

h2,h1,h3 {
	font-weight: bolder;
	letter-spacing: -0.05em;
	font-family: Arial
}

h2 {
	font-size: 200%
}

h1 {
	font-size: 170%
}

h3 {
	font-size: 1.2em
}

img {
	border: 0
}

small {
	font-size: 10px
}

a:hover img.sided {
	border-color: #A6A6A6
}

a {
	outline: none;
	color: #2970A6;
	text-decoration: none;
}

a:hover {
	text-decoration: underline
}
#main {
	background:
		url("http://www.ludou.org/blog/style/img/corner_gradient.png")
		no-repeat scroll right top #FFFFFF;
	border: 1px solid #DDDDDD;
	padding: 20px 30px 15px
}

.post {
	padding-bottom: 5px !important;
	padding-bottom: 15px
}
</style>
<body>
<div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="">
                <a href="<%=request.getContextPath()%>/showdata.do?method=showHotItemsList">全网热点</a>
              </li>
              <li class="">
                <a href="#hotsearch">最热门搜索</a>
              </li>
              <li class="">
                <a href="#hotitem">最热门宝贝</a>
              </li>
              <li class="">
                <a href="#umdacution">最热门促销商品</a>
              </li>              
              <li class=""><!-- active -->
                <a href="#hotlist">最热门类目</a>
              </li>              
              <li class="">
                <a href="#hotpage">最热门活动页面</a>
              </li>
              <li class="">
                <a href="aboutus.jsp" target="_blank">关于我们</a>
              </li>              
            </ul>
          </div>
        </div>
      </div>
    </div>
	<div align="center">
		<div id="content" align="center" style="width: 900px; float: none;height: auto;">
			<section id="hotsearch">
			<div id="main">
				<div class="post" id="post-111">
					<h1>最热搜索</h1>
					<div class="content">
						<%
							String frameUrl = "index.jsp";
							List<CloudTagPo> cloudList = (List<CloudTagPo>) request
									.getAttribute("cloudList");
							if (cloudList == null) {
								out.println("没有数据");
							} else {
								HotQueryPo maxPo = (HotQueryPo) request.getAttribute("maxPo");
								if (maxPo != null) {
									frameUrl = request.getContextPath()
											+ "/showdata.do?method=showHotItemTableList&query="
											+ maxPo.getItemName() + "&count="
											+ maxPo.getQueryCount();
								}
								for (CloudTagPo po : cloudList) {
						%>
						<a
							href='<%=request.getContextPath()%>/showdata.do?method=showHotItemTableList&query=<%=po.getShowValue()%>&count=<%=po.getTitle()%>'
							class='<%=po.getLinkCss()%>' title='<%=po.getTitle()%>'
							style='<%=po.getStyle()%>' target="view_frame"><%=po.getShowValue()%></a>&nbsp;&nbsp;
						<%
							}
							}
						%>
					</div>
					<br/>
				</div>
				<iframe src="<%=frameUrl%>" width="100%" style="min-height: 500px;"
					frameborder="no" border="0" marginwidth="0" marginheight="0"
					scrolling="yes" allowtransparency="yes" name="view_frame"></iframe>
			</div>
			</section>
			<section id="hotitem">
			<div id="main">
				<div class="post" id="post-111">
					<h1>最热宝贝</h1>
					<div class="content">
						<iframe
							src="<%=request.getContextPath()%>/showdata.do?method=showAuctionsHotTableList"
							width="100%" style="min-height: 600px;" frameborder="no"
							border="0" marginwidth="0" marginheight="0" scrolling="yes"
							allowtransparency="yes" name="hotitem_frame"></iframe>
					</div>
				</div>
			</div>
			</section>
			<section id="umdacution">
			<div id="main">
				<div class="post" id="post-111">
					<h1>最热门促销商品</h1>
					<div class="content">
						<iframe
							src="<%=request.getContextPath()%>/showdata.do?method=showUmdAuctionTableList"
							width="100%" style="min-height: 600px;" frameborder="no"
							border="0" marginwidth="0" marginheight="0" scrolling="yes"
							allowtransparency="yes" name="hoturl_frame"></iframe>
					</div>
				</div>
			</div>
			</section>	
			<section id="hotlist">
			<div id="main">
				<div class="post" id="post-111">
					<h1>最热类目</h1>
					<div class="content">
						<%
							List<CloudTagPo> catHotCloudList = (List<CloudTagPo>) request
									.getAttribute("catHotCloudList");
							if (catHotCloudList == null) {
								out.println("没有数据");
							} else {
								for (CloudTagPo po : catHotCloudList) {
						%>
						<a
							href='param.html'
							class='<%=po.getLinkCss()%>' title='<%=po.getTitle()%>'
							style='<%=po.getStyle()%>' target="_blank;"><%=po.getShowValue()%></a>&nbsp;&nbsp;
						<%
							}
							}
						%>
					</div>
				</div>
			</div>
			</section>							
			<section id="hotpage">
			<div id="main">
				<div class="post" id="post-111">
					<h1>最热活动页面</h1>
					<div class="content">
						<iframe
							src="<%=request.getContextPath()%>/showdata.do?method=showUrlHotTableList"
							width="100%" style="min-height: 800px;" frameborder="no"
							border="0" marginwidth="0" marginheight="0" scrolling="yes"
							allowtransparency="yes" name="hoturl_frame"></iframe>
					</div>
				</div>
			</div>
			</section>			
		</div>
	</div>
</body>
</html>
