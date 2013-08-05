<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>

<%@page import="com.taobao.monitor.web.util.PvRecode"%>
<div id="left_main_panel" style="position:absolute; left:0px; top:50px; width:150px; height:40px;; overflow:visible;font-size: 62.5%;z-index:99100;">
	<div id="left_panel" style="display:none;">				
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width:150px;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">日报模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/index_day.jsp" >进入日报主界面</a>
				</span>
				</div>
				</div>
				<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">实时模块</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/index_time.jsp" >进入实时主界面</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/new_index_time.jsp" >进入实时主界面(IE)
				<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">新功能</span></a>
				</span>
			</div>
		</div>
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">周报模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/week_report.jsp" >进入周报主界面</a>
				</span>
				</div>
				</div>
				<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">重要数据库信息模块</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/index_day.jsp#dbLink">查看重要数据库信息</a>
				</span>
			</div>
		</div>
		
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">Jprof模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/jprof/manage_jprof_host.jsp" >进入Jprof管理界面</a>
				</span>
			</div>
		</div>
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">评分模块<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">新功能</span></div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<span>
					<a href="<%=request.getContextPath () %>/health/manage_rating_app.jsp" >进入评分管理界面</a>
					</span>
				</div>
			</div>		
		
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">监控管理模块</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/alarm/manage_key.jsp" title="可以设置哪些key需要告警">进入告警key管理界面</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/alarm/manage_user.jsp" title="修改和添加告警接收人">进入告警用户管理界面</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/alarm/alarm_record.jsp" title="查看历史告警记录">进入告警历史查看界面</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/alarm/alarm_record_exception.jsp" >进入异常信息查看界面</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/alarm/alarm_filter.jsp" target="_blank">进入应用是否告警控制</a>
				</span>
			</div>
		</div>
	
	</div>
	<div id="imageClick" style="position:absolute; left:0px; top:150px; overflow:visible;"><img onclick="_leftImageclick()" src="<%=request.getContextPath() %>/statics/images/frameShow.gif"/></div>
		
</div>

<script type="text/javascript">


function _leftImageclick(){
	var display = $('#left_panel').css('display');
	if(display&&display=='none'){
		 $('#left_panel').css('display','block');
		 $("#imageClick").css("left","150px")
	}else{
		 $('#left_panel').css('display','none');
		 $("#imageClick").css("left","0px");
	}
}


$(window).scroll( function() {

	  var bodyTop = 0;  
	  if (typeof window.pageYOffset != 'undefined') {  
	   	bodyTop = window.pageYOffset;  
	   } else if (typeof document.compatMode != 'undefined' && document.compatMode != 'BackCompat') {  
	  bodyTop = document.documentElement.scrollTop;  
	  }  
	  else if (typeof document.body != 'undefined') {  
	  bodyTop = document.body.scrollTop;  
	  }  
	  
	    $("#left_main_panel").css("top", 50 + bodyTop)   // 设置层的CSS样式中的top属性, 注意要是小写，要符合“标准”  
	
} ); 


</script>

<%PvRecode.get().pv(request); %>
