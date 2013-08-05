<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="快速找到解决问题的人或资料的网站">
<meta name="author" content="zhongting.zy">
<title>聚热点  聚人气</title>
<!-- Le styles -->
<link href="<%=request.getContextPath()%>/assets/css/bootstrap.css"
	rel="stylesheet">
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
</style>
<style type="text/css" <!--
.normal {  font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 12px; font-weight: normal; color: #000000}
.medium {  font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 15px; font-weight: bold; color: #000000; text-decoration: none}
--></style>
<link
	href="<%=request.getContextPath()%>/assets/css/bootstrap-responsive.css"
	rel="stylesheet">

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

<!-- Le fav and touch icons -->
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/assets/ico/favicon.ico">
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="<%=request.getContextPath()%>/assets/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="<%=request.getContextPath()%>/assets/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="<%=request.getContextPath()%>/assets/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed"
	href="<%=request.getContextPath()%>/assets/ico/apple-touch-icon-57-precomposed.png">
</head>
<body>
	<div class="container">
		<div><h1 style="color:#191970" align="center">聚热点  聚人气</h1></div>
		<br />
		<div class="inner">
			<h2>背景</h2>
			<br />
			<H3 style="color: #4169E1;">闲逛无购买用户比例大</H3>
			<p style="color: #444444;">用户的访问购买数据告诉我们，有很大一部分用户有“闲逛”淘宝的习惯，但购买较少。他们的购买行为，受从众心理影响较大。</p>
			<H3 style="color: #4169E1;">在线用户彼此独立无相互联系</H3>
			<p style="color: #444444;">用户只跟卖家和宝贝有交流而用户之间无交流，无相互影响，而实际上用户是在淘宝这个无形的商场里活动着</p>
			<H3 style="color: #4169E1;">大多数人不知道淘宝的热点页面</H3>
			<p style="color: #444444;">热点可以使一个关键词，一个产品，一个类目，或是活动页面</p>
			<H3 style="color: #4169E1;">热点区价值</H3>
			<p style="color: #444444;">在用户浏览中淘宝网站的过程中，就会产生热点区，这些热点区集中了更多的用户，体现了更强烈的用户需求</p>
			<img alt="用户上网习惯" src="<%=request.getContextPath()%>/dist/pie.jpg">
			<hr>
		</div>
		<div class="inner">
			<h2>关于热点的数据</h2>
			<br />
			<H3 style="color: #4169E1;">普通商品的人气聚集情况</H3>
			<p style="color: #444444;">一天中所有被访问过的商品数位 43320000，而这些商品中访问次数超过1000的有18000，说明人气是具有一定的集中的特性</p>
			<H3 style="color: #4169E1;">促销商品的人气聚集情况</H3>
			<p style="color: #444444;">在促销的商品聚集了大量的人气，形成热点。</p>
			<table border=1>
			<tr>
			<td bgcolor=silver class='medium'>商品id</td><td bgcolor=silver class='medium'>商品title</td><td bgcolor=silver class='medium'>访问次数</td></tr>
			<tr>
			<td class='normal' valign='top'>18737480869</td>
			<td class='normal' valign='top'>【限购 0利润】2012KUNBU新款女包包时尚百搭单肩手提斜跨正品</td>
			<td class='normal' valign='top'>107870940</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>10591245779</td>
			<td class='normal' valign='top'>买一送1 秒杀ZEFER品牌男包 牛皮单肩包 男士包包 正品韩版休闲包</td>
			<td class='normal' valign='top'>42209160</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>8857032280</td>
			<td class='normal' valign='top'>金冠信誉！ZEFER爆款男士单肩包 休闲斜挎包 商务包包 韩版男包</td>
			<td class='normal' valign='top'>38452105</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>14309738151</td>
			<td class='normal' valign='top'>正品左旋肉碱 强效型减肥药胶囊男女健康纯中药产品瘦身瘦腰瘦腿</td>
			<td class='normal' valign='top'>38450496</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>8563878801</td>
			<td class='normal' valign='top'>秒杀!送卡包 斐格 男士单肩包背包 斜挎包 商务牛皮包 包包 男包</td>
			<td class='normal' valign='top'>34981100</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>10592918255</td>
			<td class='normal' valign='top'>[聚团购]ZEFER品牌男包 牛皮单肩包 男士公文包 韩版休闲斜挎包包</td>
			<td class='normal' valign='top'>26598000</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>13018400006</td>
			<td class='normal' valign='top'>包邮不满意包退！免安装日本高级充气娃娃少妇范冰冰真人半实体拍</td>
			<td class='normal' valign='top'>25549200</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>7768492676</td>
			<td class='normal' valign='top'>买一送六 耳机 mp3 mp4 mp5 电脑通用 耳塞式 低音控 性价比极高</td>
			<td class='normal' valign='top'>22157922</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>14085840086</td>
			<td class='normal' valign='top'>淘金币 【限量疯抢】法国Dunvel2012新款女包正品女包牛皮女包特</td>
			<td class='normal' valign='top'>21535295</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>10732989963</td>
			<td class='normal' valign='top'>淘金币 【爆款疯抢最后一天】萨贝尼牛皮时尚手提包 女式单肩包</td>
			<td class='normal' valign='top'>20999128</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>13915504612</td>
			<td class='normal' valign='top'>playboy家纺 纯棉公主床品四件套 全棉韩国婚庆田园床上用品包邮</td>
			<td class='normal' valign='top'>18204346</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>4686476126</td>
			<td class='normal' valign='top'>秒杀包邮！2012秋季高品质大码女装新款韩版职业西装配印花连衣裙</td>
			<td class='normal' valign='top'>15442524</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>10737309968</td>
			<td class='normal' valign='top'>左旋360咖啡减肥药正品减肥咖啡胶囊瘦咔燃脂黑咖啡左旋肉碱产品</td>
			<td class='normal' valign='top'>12701920</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>13672997982</td>
			<td class='normal' valign='top'>【七夕好礼】买一送一！圣大高夫男包单肩包 休闲包斜挎包商务包</td>
			<td class='normal' valign='top'>12611961</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>13716406990</td>
			<td class='normal' valign='top'>正品左旋肉碱 健康减肥胶囊安全瘦身减淝产品 非药品 男女通用</td>
			<td class='normal' valign='top'>12065264</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>15289609531</td>
			<td class='normal' valign='top'>2012新款真皮女包袋 专柜正品牛皮包包 单肩手提女包 包邮</td>
			<td class='normal' valign='top'>12005147</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>5847598677</td>
			<td class='normal' valign='top'>左旋肉碱超级强效减肥中药非胶囊正品p瘦身减淝产品销售排行榜57</td>
			<td class='normal' valign='top'>11711132</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>10236558646</td>
			<td class='normal' valign='top'>【疯抢2小时】欧美品牌女包正品真皮韩版复古牛皮女士包包</td>
			<td class='normal' valign='top'>11472009</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>5756846738</td>
			<td class='normal' valign='top'>如熙2012新款夏季韩版破洞夏女牛仔连体背带裤短裤修身显瘦 大码</td>
			<td class='normal' valign='top'>10967693</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>12743178168</td>
			<td class='normal' valign='top'>新一代足膜 脚膜 脚贴 去死皮 去角质 去老茧 美白滋润去异味 1袋</td>
			<td class='normal' valign='top'>10933166</td>
			</tr>
			<tr>
			<td class='normal' valign='top'>14494818886</td>
			<td class='normal' valign='top'>4件包邮(圆通)纯棉莱卡男士内裤 男式平角内裤U凸性感CK0l 正品</td>
			<td class='normal' valign='top'>10684447</td>
			</tr>
			</table>
			<hr>
			<div class="inner">
			<h2>热点</h2>
			<br />
			<H3 style="color: #444444;">热点指当前用户最关注的地方，也相当于最有人气的地方。</H3>
			<H3 style="color: #4169E1;">最热门搜索</H3><p style="color: #444444;">用户搜索最多的关键字</p>
			<H3 style="color: #4169E1;">最热门宝贝</H3><p style="color: #444444;">用户浏览最多的宝贝</p>
			<H3 style="color: #4169E1;">最热门促销商品</H3><p style="color: #444444;">促销活动中被用户浏览最多的宝贝</p>
			<H3 style="color: #4169E1;">最热门类目</H3><p style="color: #444444;">用户浏览最多的类目</p>
			<H3 style="color: #4169E1;">最热门活动页面</H3><p style="color: #444444;">一定时间段用户聚集最多的活动页面</p>
		<hr>
		<div class="inner">
			<h2>目的</h2>
			<br />
			<H3 style="color: #4169E1;">促进用户间交流</H3><p style="color: #444444;">热点的实时展现让用户由参与和互动感</p>
			<H3 style="color: #4169E1;">增加购买转换率</H3><p style="color: #444444;">通过层层的热点推进，让“闲逛”用户转化为购买用户</p>
			<H3 style="color: #4169E1;">帮组用户获得及时可靠信息</H3><p style="color: #444444;">挖掘出用户之间的实时行为热点，让用户之间针对同一热点进行讨论，获取更多有用信息，提高购买满意度。</p>
			<H3 style="color: #4169E1;">及时获取用户的有用信息</H3><p style="color: #444444;">为淘宝评价系统提供更多信息</p>
		</div>	
		<hr>
		<div class="inner">
			<h2>说吧</h2>
			<br />
			<H3 style="color: #444444;">让说吧演变为实时的共同话题的讨论</H3>
		</div>	
		<hr>
	</div>
	
	<!--/.fluid-container-->

	<!-- Le javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="<%=request.getContextPath()%>/assets/js/jquery.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-transition.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-alert.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-modal.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-dropdown.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-scrollspy.js"></script>
	<script src="<%=request.getContextPath()%>/assets/js/bootstrap-tab.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-tooltip.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-popover.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-button.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-collapse.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-carousel.js"></script>
	<script
		src="<%=request.getContextPath()%>/assets/js/bootstrap-typeahead.js"></script>
</body>
</html>
