<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>

<%@page import="com.taobao.monitor.web.util.PvRecode"%>
<div id="left_main_panel" style="position:absolute; left:0px; top:50px; width:150px; height:40px;; overflow:visible;font-size: 62.5%;z-index:99100;">
	<div id="left_panel" style="display:none;">				
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width:150px;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�ձ�ģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/index_day.jsp" >�����ձ�������</a>
				</span>
				</div>
				</div>
				<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">ʵʱģ��</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/index_time.jsp" >����ʵʱ������</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/new_index_time.jsp" >����ʵʱ������(IE)
				<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">�¹���</span></a>
				</span>
			</div>
		</div>
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�ܱ�ģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/week_report.jsp" >�����ܱ�������</a>
				</span>
				</div>
				</div>
				<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��Ҫ���ݿ���Ϣģ��</div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/index_day.jsp#dbLink">�鿴��Ҫ���ݿ���Ϣ</a>
				</span>
			</div>
		</div>
		
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">Jprofģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/jprof/manage_jprof_host.jsp" >����Jprof�������</a>
				</span>
			</div>
		</div>
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">����ģ��<span style="top: 0pt; font-size: 10px; color: rgb(255, 0, 0);">�¹���</span></div>
				<div id="dialog" class="ui-dialog-content ui-widget-content">
					<span>
					<a href="<%=request.getContextPath () %>/health/manage_rating_app.jsp" >�������ֹ������</a>
					</span>
				</div>
			</div>		
		
		<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 150px;">
			<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">��ع���ģ��</div>
			<div id="dialog" class="ui-dialog-content ui-widget-content">
				<span>
				<a href="<%=request.getContextPath () %>/alarm/manage_key.jsp" title="����������Щkey��Ҫ�澯">����澯key�������</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/alarm/manage_user.jsp" title="�޸ĺ���Ӹ澯������">����澯�û��������</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/alarm/alarm_record.jsp" title="�鿴��ʷ�澯��¼">����澯��ʷ�鿴����</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/alarm/alarm_record_exception.jsp" >�����쳣��Ϣ�鿴����</a>
				</span>
				<br/>
				<span>
				<a href="<%=request.getContextPath () %>/alarm/alarm_filter.jsp" target="_blank">����Ӧ���Ƿ�澯����</a>
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
	  
	    $("#left_main_panel").css("top", 50 + bodyTop)   // ���ò��CSS��ʽ�е�top����, ע��Ҫ��Сд��Ҫ���ϡ���׼��  
	
} ); 


</script>

<%PvRecode.get().pv(request); %>
