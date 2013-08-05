<%@page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.alarm.source.po.KeySourcePo"%>
<%@page import="com.taobao.monitor.alarm.source.change.KeySourceInfoAo"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>交易依赖key的设定</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/tablestyle.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript">
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#keyBodyId tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#keyBodyId tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
	})
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<%
	//参数获取
	String keyName = "";
	if(StringUtils.isNotBlank(request.getParameter("keyName"))){
		keyName = new String( request.getParameter("keyName").getBytes("ISO-8859-1"));
	}
	String strAppId = request.getParameter("appId");
	String pageNum = request.getParameter("pageNum");
	String strPageSize = request.getParameter("pageSize");
	//TODO 默认值发布的时候要改q
	int tpAppId = 330;
	String queryAppGroupName="交易中心";
	if( StringUtils.isNotBlank( strAppId ) ){
		tpAppId = Integer.valueOf(strAppId);
		AppInfoPo queryAppPo = AppInfoAo.get().findAppInfoById(tpAppId);
		queryAppGroupName=queryAppPo.getGroupName();
	}
	int pageNumber = 1;
	int totalNumber = 0; //页码显示
	int pageSize=40; //与数据库中一致
	if(StringUtils.isNotBlank(pageNum)){
		try{
			pageNumber = Integer.valueOf(pageNum);
			if(StringUtils.isNotBlank(strPageSize)){
				pageSize = Integer.valueOf(strPageSize);
			}
		} catch(Exception e){
		}
	}
	//查询所有分组
	List<String> listGroupName = AppInfoAo.get().findAllAppGroupName();
	//查找某个appId下的key
	List<KeyPo> keyList = null;
	//有keyName则查keyName
	if(StringUtils.isBlank(keyName)){
		keyName = "";
		totalNumber = KeySourceInfoAo.get().countAppKeyByAppId(tpAppId);
		keyList = KeySourceInfoAo.get().queryAppKeyByAppIdInPage(tpAppId, pageNumber, pageSize);
	} else {
		keyName = URLDecoder.decode(keyName.trim(), "GBK");
		totalNumber = KeySourceInfoAo.get().countAppKeyLikeName(keyName, tpAppId);
		keyList = KeySourceInfoAo.get().queryAppKeyLikeNameInPage(keyName, tpAppId, pageNumber, pageSize);
	}
	int keyCount = 0;
	if(keyList!= null && keyList.size()>0){
		keyCount = keyList.size();
	}
	//通过appId来获取
	List<KeySourcePo> listKeySourcePo = KeySourceInfoAo.get().findAllKeySourcePosByAppId(tpAppId);
	
	//key同列的select
	StringBuffer keyGroupSelect = new StringBuffer();
	StringBuffer queryGroupSelect = new StringBuffer();
	for(String groupName:listGroupName){
		keyGroupSelect.append("<option>"+groupName+"</option>");
		queryGroupSelect.append("<option>"+groupName+"</option>"+"\n");
	}
	queryGroupSelect.append("<option selected='selected'>"+queryAppGroupName+"</option>");
	keyGroupSelect.append("<option value='0' selected='selected'>选择应用组</option>");
	
	
	//查询groupName的app
	List<AppInfoPo> listSameGroupApp = AppInfoAo.get().findAppInfoByGroupName(queryAppGroupName);
	StringBuffer queryAppNameOption = new StringBuffer();
	for(AppInfoPo po:listSameGroupApp){
		if(tpAppId == po.getAppId()){
			queryAppNameOption.append("<option value='" + po.getAppId() + "' selected='selected'>"+po.getAppName()+"</option>");
		} else {
			queryAppNameOption.append("<option value='" + po.getAppId() + "'>"+po.getAppName()+"</option>");
		}
	}
	
%>
<div id="page">
	<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix topseparater">查询条件</div>
	<div id="dialog" class="ui-dialog-content ui-widget-content bottomseparater">
		<form action="./manage_relation_key.jsp" method="get" style="padding:4px;">
			key名称:<input id="keyinput" name="keyName" value="<%=keyName%>">&nbsp;&nbsp;
			应用组:
			<select id="parentGroupSelect" onchange="queryGroupNameChange()">
			<%=queryGroupSelect.toString()%>
			</select>
			应用名:
			<select name="appId" id="queryappid" >
			<%=queryAppNameOption.toString()%>
			</select>	
			<input id="queryappidbtn" value="查询" type="submit">
		</form>
	</div>
	<div class="ui-dialog ui-widget ui-widget-content ui-corner-all" style="width:100%;">
		<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">交易依赖key值设定</div>
		<div id="dialog" class="ui-dialog-content ui-widget-content">
			<table id="keyBodyId" border="1" class="ui-widget ui-widget-content">
				<tr>
				  <td align="center" width="67%">key名称</td>
				  <td align="center" width="14%">应用组</td>
				  <td align="center" >应用名称</td>
				  <td align="center" width="7%">操作</td>
				</tr>
				<%
					if(keyCount > 0){
						String strkeyName = "";
						for(KeyPo po:keyList){
							strkeyName = po.getKeyName();
							String keyGroupName = "";
							List<KeySourcePo> listKeySetPo = KeySourceInfoAo.get().queryKeySourceByKeyId(po.getKeyId());
							if(listKeySetPo!= null && listKeySetPo.size() > 0){
								for(KeySourcePo setPo:listKeySetPo){
									keyGroupName = setPo.getSourceGroupName();
				%>
				<tr>
					<td align="left" width="67%"><a href="#" title="<%=strkeyName%>">
					 <%
									if(strkeyName.length()>100){	
										out.print(strkeyName.substring(0, 100)+"...");
									} else {
										out.print(strkeyName);
									}
					 %>
					 </a> 
					 </td>
					 <td align="center" width="14%">
				 		<select class="opselect" id="keyGroupSelect<%=setPo.getKeyId()+setPo.getSourceAppId()%>" onchange="groupNameChange(<%=setPo.getKeyId()%>,<%=setPo.getSourceAppId()%>)">
					 	<%
								 	for(String groupName:listGroupName){
								 		if(StringUtils.isBlank(groupName)){
											continue;
										}
								 		if(keyGroupName.equals(groupName)){
											out.println("<option selected='selected'>"+groupName+"</option>");
								 		} else {
								 			out.println("<option>"+groupName+"</option>");
								 		}
									}
						%>
						</select>
					</td>
					<td align="center">
					 	<select class="opselect" id="tradeRelationGroupSelect<%=setPo.getKeyId()%>">
					 	<%
						 			List<AppInfoPo> listSameGroupAppInfo = AppInfoAo.get().findAppInfoByGroupName(keyGroupName);
						 			for(AppInfoPo appInfoPo:listSameGroupAppInfo){
						 				String appName = appInfoPo.getAppName();
						 				if(StringUtils.isBlank(appName)){
											continue;
										}
						 				if(appName.equals(setPo.getSourceAppName())){
						 					out.println("<option value='" + appInfoPo.getAppId() + "' selected='selected'>"+appInfoPo.getAppName()+"</option>");
						 				} else {
						 					out.println("<option value='" + appInfoPo.getAppId() + "'>"+appInfoPo.getAppName()+"</option>");
						 				}
						 			}
						%>
						</select>
					</td>
				 	<td align="center" width="7%">
				 		<div class="stoken">
				 			<a href="#" onClick="deleteRelationKey(<%=setPo.getKeyId()%>,<%=setPo.getSourceAppId()%>); return false;">删除</a>&nbsp;&nbsp;<a href="#" onClick="modifyRelationKey(<%=setPo.getKeyId()%>,<%=setPo.getSourceAppId()%>)" >修改</a>
				 		</div>
				 	</td>
				</tr>
				<%				
								}
							} else {
				%>
				<tr>
					<td align="left" width="67%"><a href="#" style="text-decoration:none; overflow:hidden;"title="<%=strkeyName%>">
					 <%
								if(strkeyName.length()>100){	
									out.print(strkeyName.substring(0, 100)+"...");
								} else {
									out.print(strkeyName);
								}
					 %>
					 </a> 
					 </td>
					 <td align="center" width="14%">
				 		<select class="opselect" id="keyGroupSelect<%=po.getKeyId() %>" onchange="groupNameChange(<%=po.getKeyId()%>,0); return false;">
					 	<%=keyGroupSelect.toString()%>
						</select>
					</td>
					<td align="center">
					 	<select class="opselect" id="tradeRelationGroupSelect<%=po.getKeyId()%>">
					 	<option value='0' selected='selected'>选择应用名称</option>
						</select>
					</td>
				 	<td align="center" width="7%">
				 		<div class="stoken">
				 			<a href="#" onClick="deleteRelationKey(<%=po.getKeyId()%>,0); return false;">删除</a>&nbsp;&nbsp;<a href="#" onClick="modifyRelationKey(<%=po.getKeyId()%>,0); return false;" >修改</a>
				 		</div>
				 	</td>
				</tr>
				<%
							}
						}
				%>
				<tr>
					<td colspan="4" align="center" >
						<div class="totalresult">
					<%
						StringBuilder sb = new StringBuilder();
						int pages = totalNumber/pageSize+1;
						if(pages <= 6){
							if(pages > 1){
								if(pageNumber == 1){
									for(int i=1; i<=pages; i++){
										if(i == pageNumber){
											sb.append("<span class='keycount'>"+i+"</span>");
										} else {
											sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
											if(keyName!=null){
												sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+i);
											} else {
												sb.append("appId="+tpAppId+"&pageNum="+i);
											}
											sb.append("'>"+i+"</a></span>");
										}
									}
									
									sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
									if(keyName!=null){
										sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum=2'>下一页</a><span>");
									} else {
										sb.append("appId="+tpAppId+"&pageNum=2'>下一页</a><span>");
									}
									
								} else if(pageNumber == pages){
									sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
									if(keyName!=null){
										sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
									} else {
										sb.append("appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
									}
									for(int i=1; i<=pages; i++){
										if(i == pageNumber){
											sb.append("<span class='keycount'>"+i+"</span>");
										} else {
											sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
											if(keyName!=null){
												sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+i);
											} else {
												sb.append("appId="+tpAppId+"&pageNum="+i);
											}
											sb.append("'>"+i+"</a></span>");
										}
									}
								} else {
									sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
									if(keyName!=null){
										sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
									} else {
										sb.append("appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
									}
									for(int i=1; i<=pages; i++){
										if(i == pageNumber){
											sb.append("<span class='keycount'>"+i+"</span>");
										} else {
											sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
											if(keyName!=null){
												sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+i);
											} else {
												sb.append("appId="+tpAppId+"&pageNum="+i);
											}
											sb.append("'>"+i+"</a></span>");
										}
									}
									sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
									if(keyName!=null){
										sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber+1)+"'>下一页</a><span>");
									} else {
										sb.append("appId="+tpAppId+"&pageNum=2'>下一页</a><span>");
									}
								}
							}
							
						} else {
								if( (pageNumber == pages) || (pageNumber==pages-1) || (pageNumber== pages-2) ){
									sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
									if(keyName!=null){
										sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
										sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
										sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum=1'>1</a></span><span class='linkpage'>……</span>");
									} else {
										sb.append("appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
										sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
										sb.append("appId="+tpAppId+"&pageNum=1'>1</a></span><span class='linkpage'>……</span>");
									}
									if(pageNumber < pages){
										for(int i=pages-3; i<pages; i++){
											if(i == pageNumber){
												sb.append("<span class='keycount'>"+i+"</span>");
											} else {
												sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
												if(keyName!=null){
													sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+i);
												} else {
													sb.append("appId="+tpAppId+"&pageNum="+i);
												}
												sb.append("'>"+i+"</a></span>");
											}
										}
										sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
										if(keyName!=null){
											sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+pages);
										} else {
											sb.append("appId="+tpAppId+"&pageNum="+pages);
										}
										sb.append("'>"+pages+"</a></span>");
										sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
										if(keyName!=null){
											sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber+1)+"'>下一页</a><span>");
										} else {
											sb.append("appId="+tpAppId+"&pageNum="+(pageNumber+1)+"'>下一页</a><span>");
										}
									} else {
										for(int i=pages-3; i<=pages; i++){
											if(i == pageNumber){
												sb.append("<span class='keycount'>"+i+"</span>");
											} else {
												sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
												if(keyName!=null){
													sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+i);
												} else {
													sb.append("appId="+tpAppId+"&pageNum="+i);
												}
												sb.append("'>"+i+"</a></span>");
											}
										}
									}
								} else {
									switch(pageNumber){
									case 1:
									case 2:
									case 3:
										if(pageNumber > 1){
											sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
											if(keyName!=null){
												sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
											} else {
												sb.append("appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
											}
										}
										for(int i=1; i<=4; i++){
											if(i == pageNumber){
												sb.append("<span class='keycount'>"+i+"</span>");
											} else {
												sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
												if(keyName!=null){
													sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+i);
												} else {
													sb.append("appId="+tpAppId+"&pageNum="+i);
												}
												sb.append("'>"+i+"</a></span>");
											}
										}
										sb.append("<span class='linkpage'>……</span><span class='linkpage'><a href='manage_relation_key.jsp?");
										if(keyName!=null){
											sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+pages);
										} else {
											sb.append("appId="+tpAppId+"&pageNum="+pages);
										}
										sb.append("'>"+pages+"</a></span>");
										sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
										if(keyName!=null){
											sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber+1)+"'>下一页</a><span>");
										} else {
											sb.append("appId="+tpAppId+"&pageNum="+(pageNumber+1)+"'>下一页</a><span>");
										}
										break;
									default:
										sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
										if(keyName!=null){
											sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
											sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
											sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum=1'>1</a></span><span class='linkpage'>……</span>");
										} else {
											sb.append("appId="+tpAppId+"&pageNum="+(pageNumber-1)+"'>上一页</a><span>");
											sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
											sb.append("appId="+tpAppId+"&pageNum=1'>1</a></span><span class='linkpage'>……</span>");
										}
										for(int i=pageNumber-1; i<=pageNumber+1; i++){
											if(i == pageNumber){
												sb.append("<span class='keycount'>"+i+"</span>");
											} else {
												sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
												if(keyName!=null){
													sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+i);
												} else {
													sb.append("appId="+tpAppId+"&pageNum="+i);
												}
												sb.append("'>"+i+"</a></span>");
											}
										}
										sb.append("<span class='linkpage'>……</span><span class='linkpage'><a href='manage_relation_key.jsp?");
										if(keyName!=null){
											sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+pages);
										} else {
											sb.append("appId="+tpAppId+"&pageNum="+pages);
										}
										sb.append("'>"+pages+"</a></span>");
										sb.append("<span class='linkpage'><a href='manage_relation_key.jsp?");
										if(keyName!=null){
											sb.append("keyName="+keyName+"&appId="+tpAppId+"&pageNum="+(pageNumber+1)+"'>下一页</a><span>");
										} else {
											sb.append("appId="+tpAppId+"&pageNum="+(pageNumber+1)+"'>下一页</a><span>");
										}
										break;			
									}
								}
							}
							sb.append("<span class='currentpage'>共<strong>"+pages+"</strong>页<span>");
							out.write(sb.toString());
					%>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center" >
						<div class="totalresult">共计<span class="keycount"><%=totalNumber %></span>条记录</div>
					</td>
				</tr>
				<%
					} else {
						
				%>
				<tr>
					<td colspan="4" align="center" >
						<div class="noresult">没有查到任何信息</div>
					</td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
	</div>
</div>
<jsp:include page="../buttom.jsp"></jsp:include>
<script type="text/javascript">

	function deleteRelationKey(relKeyId,srcAppId){
		var trs = '#tradeRelationGroupSelect' + relKeyId + srcAppId;
		var val_srcAppId = $(trs).find("option:selected").val();
		if(srcAppId != val_srcAppId && val_srcAppId != undefined){
			srcAppId = val_srcAppId;
		}
		if(srcAppId == 0){
			alert('请选择应用组和应用名称');
		} else {
			if(window.confirm('确认删除?')){
				$.post("./update_relation_key.jsp",
						{op:"delete", keyId:relKeyId, sourceAppId:srcAppId },
						function (data, textStatus){
							var res = data.toString();
							alert( res.replace(/^\s*/, "") );
							resetSelectionText(relKeyId,srcAppId);
						},
						"text"
						);
			}
		}

	}
	
	function resetSelectionText(keyId,relAppId){
		$('#keyGroupSelect'+keyId+relAppId).html("<%=keyGroupSelect.toString()%>"); //选择应用组 的index
		$('#tradeRelationGroupSelect'+keyId+relAppId).html("<option value='0' selected='selected'>选择应用名称</option>");
	}
	
	function modifyRelationKey(relKeyId,relSoruceAppId){
		var trs = '#tradeRelationGroupSelect' + relKeyId;
		var srcAppId = $(trs).find("option:selected").val();
		if(srcAppId == 0){
			alert('请选择应用组和应用名称');
		} else {
			if(window.confirm('确认修改?')){
				$.post("./update_relation_key.jsp",
						{op:"modify", keyId:relKeyId, appId:<%=tpAppId%>, sourceAppId:srcAppId },
						function (data, textStatus){
							var res = data.toString();
							alert(res.replace(/^\s*/, ""));
						},
						"text"
						);
			}
		}
	}
	
	function groupNameChange(keyId, appId){
		if(appId==0){
			var ks = '#keyGroupSelect'+keyId;
			var trs = '#tradeRelationGroupSelect'+keyId;
			var gn = $(ks).find("option:selected").text();
			$.get('./get_group_app.jsp', {Action:'get',groupName:encodeURI(gn)}, function(data, textStatus){ $(trs).html(data); });
		} else {
			var ks = '#keyGroupSelect'+keyId+appId;
			var trs = '#tradeRelationGroupSelect'+keyId+appId;
			var gn = $(ks).find("option:selected").text();
			$.get('./get_group_app.jsp', {Action:'get',groupName:encodeURI(gn)}, function(data, textStatus){ $(trs).html(data); });
		}
	}
	
	function queryGroupNameChange(){
		var gn = $('#parentGroupSelect').find("option:selected").text();
		$.get('./get_group_app.jsp', {Action:'get',groupName:encodeURI(gn)}, function(data, textStatus){ $('#queryappid').html(data); });
	}
</script>
</body>
</html>
