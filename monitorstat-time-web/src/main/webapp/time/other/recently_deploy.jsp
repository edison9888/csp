<%@page import="com.taobao.csp.other.changefree.ChangeFree"%>
<%@page import="com.taobao.csp.other.artoo.Artoo"%>
<%@page import="com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container-fluid">
		<marquee id="affiche" align="left" behavior="scroll"  direction="left" width="100%"  loop="-1" scrollamount="10" scrolldelay="300" onMouseOut="this.start()" onMouseOver="this.stop()">
			<c:forEach items="${ artooList}" var="artoo">
			<img height="16px" width="16px"  src="<%=request.getContextPath() %>/statics/img/Info.png" ><a>${artoo.creator }ÔÚ[${artoo.deployTime }]${artoo.planType }Ó¦ÓÃ:${artoo.appName }×´Ì¬:${artoo.state }</a>
			</c:forEach>
		</marquee>
	</div>
