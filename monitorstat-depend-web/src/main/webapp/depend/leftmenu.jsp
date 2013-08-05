<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@page import="com.taobao.csp.depend.util.StartUpParamWraper"%>
<%@page import="com.taobao.csp.depend.util.StartUpParam"%>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<%
	String strDisPlay = "";
	if(StartUpParam.PRJTYPE_ONLINE.equals(StartUpParamWraper.getPrjType())) {
	  strDisPlay = "display:none";
	}
%>
	<div style="width: 200px; float: left; background: #fafafa;">
		<div id="mm" class="easyui-accordion" border="false">
			<%
			if(StartUpParam.PRJTYPE_ONLINE.equals(StartUpParamWraper.getPrjType())) { //日常不显示
			%>
			<div title="我的详细信息">
			<dl style="border-color: #C4D5DF;  border-style: solid;   border-width: 1px 1px 1px;    line-height: 28px;    overflow: hidden;    width: 148px;">
				<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px;" id="provoidHSF">
				<a href="javascript:void(0)"	onclick="gotoAppCenterHsfInfo('<%=request.getContextPath() %>','provide')">我提供的HSF信息</a></dt>
				<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px; " id="consumeHSF">
				<a href="javascript:void(0)"	onclick="gotoAppCenterHsfInfo('<%=request.getContextPath() %>','consume')">我消费的HSF信息</a></dt>
				<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px;" id="consumeTair">
				<a href="javascript:void(0)"	onclick="gotoAppCenterTairInfo('<%=request.getContextPath() %>','consume')">我消费的Tair信息</a></dt>			
				<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px;" id="consumeTddl">
				<a href="javascript:void(0)"	onclick="gotoTddlPage('<%=request.getContextPath() %>','consumeTddl')">我TDDL调用信息</a></dt>
				<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px;" id="originURL">
				<a href="javascript:void(0)"	onclick="gotoURLPage('<%=request.getContextPath() %>','origin')">URL来源统计</a></dt>
				<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px; display: none;" id="responseURL">
				<a href="javascript:void(0)"	onclick="gotoURLPage('<%=request.getContextPath() %>','request')">URL去向统计</a></dt>				
				<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px; " id="provideTfs">
				<a href="javascript:void(0)" onclick="gotoTfsPage('<%=request.getContextPath()%>','provideTfs')">TFS信息统计</a></dt>				
			</dl>				
			</div>
			<%			
			}
			%>			
			<div title="我依赖的应用">
			<dl style="border-color: #C4D5DF;  border-style: solid;   border-width: 1px 1px 1px;    line-height: 28px;    overflow: hidden;    width: 148px;">
				<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px;" id="meDependAll">
					<a href="javascript:void(0)"	onclick="gotoMeDepend('<%=request.getContextPath() %>','all')">我的全部依赖</a>
				</dt>
				<dd id="meDependHSF"><a href="javascript:void(0)"
						onclick="gotoMeDepend('<%=request.getContextPath() %>','hsf')">HSF依赖</a></dd>
				<dd id="meDependTair"><a href="javascript:void(0)"
						onclick="gotoMeDepend('<%=request.getContextPath() %>','tair')">Tair依赖</a></dd>
				<dd id="meDependSearch"><a href="javascript:void(0)"
						onclick="gotoMeDepend('<%=request.getContextPath() %>','搜索')">搜索依赖</a></dd>
			</dl>				
			</div>
			<div title="依赖我的应用" onclick="dependMeLinkSearch('all')">
				<dl style="border-color: #C4D5DF;  border-style: solid;   border-width: 1px 1px 1px;    line-height: 28px;    overflow: hidden;    width: 148px;">
					<dt style="cursor: pointer;    padding-left: 0;    position: relative;    text-indent: 20px;" id="dependMeAll">
					<a href="javascript:void(0)"	onclick="gotoDependMe('<%=request.getContextPath() %>','all')">我的全部依赖</a></dt>
					<dd id="dependMeHSF"><a href="javascript:void(0)"
							onclick="gotoDependMe('<%=request.getContextPath() %>','hsf')">HSF依赖</a></dd>
					<dd id="dependMeTair"><a href="javascript:void(0)"
							onclick="gotoDependMe('<%=request.getContextPath() %>','tair')">Tair依赖</a></dd>
					<dd id="dependMeSearch"><a href="javascript:void(0)"
							onclick="gotoDependMe('<%=request.getContextPath() %>','搜索')">搜索依赖</a></dd>
				</dl>		
			</div>
			<%
			if(StartUpParam.PRJTYPE_DAILY.equals(StartUpParamWraper.getPrjType())) { //DIV的CSS不起作用
			%>
			 <div title="依赖检查配置">
				<ul class="pitem" id="dependConfig">
					<li><a href="javascript:void(0)" onclick="gotoConfigList('<%=request.getContextPath() %>')">进入配置检测页面</a></li>
				</ul>
			</div> 
			<%			
			}
			%>
		</div>
	</div>
