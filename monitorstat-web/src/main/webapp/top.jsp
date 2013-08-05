<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>

<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="com.taobao.monitor.web.util.SessionUtil"%>
<link href="<%=request.getContextPath() %>/statics/css/style.css" rel="stylesheet" type="text/css">
<%

LoginUserPo po = SessionUtil.getUserSession(request);

%>
<script type="text/javascript"> 
 
function navOver(navNumber) {
	var navDiv = document.getElementById("js0" + navNumber);
	navDiv.style.display = "block";
	}
	function navOut(navNumber) {
	var navDiv = document.getElementById("js0" + navNumber);
	navDiv.style.display = "none";
	}
</script>
<table width="1000"  border="0" align="center" cellpadding="0" cellspacing="0">

 <tr>
    <td>
        <div style="position:absolute; width:1px; height:1px;">   
    <div style="position: relative; z-index:100; left:120px; top:75px; display:none; width:220px;" id="js01" onmouseover="navOver(1);" onmouseout="navOut(1);" >
      <table cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tl.png" width="25" height="21"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_tt.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tr.png" width="31" height="21"></td>
        </tr>
        <tr>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_ll.png);"></td>
          <td class="drop" bgcolor="#DAF0FD" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bg.png);">
          <table border="0">           
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/index_time.jsp">实时监控</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/index_day.jsp">日报</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/week_report.jsp" >周报</a></td></tr>
          </table> </td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_rr.png);"></td>
        </tr>  
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_bl.png" width="25" height="30"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bb.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_br.png" width="31" height="30"></td>
        </tr>  
       </table>
    </div>
     <div style="position: relative; z-index:100; left:250px; top:75px; display:none; width:220px;" id="js02" onmouseover="navOver(2);" onmouseout="navOut(2);" >
      <table cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tl.png" width="25" height="21"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_tt.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tr.png" width="31" height="21"></td>
        </tr>
        <tr>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_ll.png);"></td>
          <td class="drop" bgcolor="#DAF0FD" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bg.png);">
          <table border="0">           
          <tr><td><a class="menu" href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit">容量排行</a></td></tr>
          <tr><td><a class="menu" href="http://capacity.taobao.net:9999/capacity/show.do?method=showMachine">服务器数据预测</a></td></tr>
          <tr><td><a class="menu" href="http://110.75.26.90:9999/autoload/loadrun/show.do?method=list">线上压测控制台 </a></td></tr>
          <tr><td><a class="menu" href="http://capacity.taobao.net:9999/capacity/capacity/CapacityDenpend.jsp">容量分布图</a></td></tr>             
          </table>
          </td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_rr.png);"></td>
        </tr>  
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_bl.png" width="25" height="30"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bb.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_br.png" width="31" height="30"></td>
        </tr>  
       </table>
    </div>
     <div style="position: relative; z-index:100; left:370px; top:75px;  display:none; width:220px;" id="js03" onmouseover="navOver(3);" onmouseout="navOut(3);" >
      <table cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tl.png" width="25" height="21"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_tt.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tr.png" width="31" height="21"></td>
        </tr>
        <tr>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_ll.png);"></td>
          <td class="drop" bgcolor="#DAF0FD" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bg.png);">
          <table border="0">           
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/health/index.jsp">性能健康指数</a></td></tr> 
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/health/index.jsp">性能优化记录</a></td></tr> 
                       
          </table>
          </td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_rr.png);"></td>
        </tr>  
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_bl.png" width="25" height="30"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bb.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_br.png" width="31" height="30"></td>
        </tr>  
       </table>
    </div>
     <div style="position: relative; z-index:100; left:520px; top:75px;  display:none; width:220px;"id="js04" onmouseover="navOver(4);" onmouseout="navOut(4);" >
      <table cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tl.png" width="25" height="21"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_tt.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tr.png" width="31" height="21"></td>
        </tr>
        <tr>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_ll.png);"></td>
          <td class="drop" bgcolor="#DAF0FD" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bg.png);">
          <table border="0">           
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/distrib/distrib_provider.jsp">HSF流量分布 </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/tair/tair_show.jsp">Tair流量分布 </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/dependent/index.jsp">应用依赖 </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/dependent/appinfo/appJar.jsp">jar包依赖 </a></td></tr>         
          </table>
          </td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_rr.png);"></td>
        </tr>  
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_bl.png" width="25" height="30"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bb.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_br.png" width="31" height="30"></td>
        </tr>  
       </table>
    </div>
     <div style="position: relative; z-index:100; left:640px; top:75px; display:none; width:220px;"id="js05" onmouseover="navOver(5);" onmouseout="navOut(5);" >
      <table cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tl.png" width="25" height="21"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_tt.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tr.png" width="31" height="21"></td>
        </tr>
        <tr>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_ll.png);"></td>
          <td class="drop" bgcolor="#DAF0FD" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bg.png);">
          <table border="0">           
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_record_detail.jsp">告警历史查询 </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_record.jsp">告警历史汇总 </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_collect.jsp">告警用户统计</a></td></tr>          
          </table>
          </td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_rr.png);"></td>
        </tr>  
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_bl.png" width="25" height="30"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bb.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_br.png" width="31" height="30"></td>
        </tr>  
       </table>
    </div>
     <div style="position: relative; z-index:100; left:760px; top:75px;  display:none; width:220px;"id="js06" onmouseover="navOver(6);" onmouseout="navOut(6);" >
      <table cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tl.png" width="25" height="21"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_tt.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tr.png" width="31" height="21"></td>
        </tr>
        <tr>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_ll.png);"></td>
          <td class="drop" bgcolor="#DAF0FD" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bg.png);">
          <table border="0">           
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/manage_key.jsp">告警key管理</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/user/manage_user.jsp">告警用户管理</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_record_exception1.jsp">异常信息查看</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_filter.jsp">告警控制</a></td></tr> 
          <tr><td><a class="menu" href="<%=request.getContextPath()%>/reportConfig.do">报表配置</a></td></tr>            
          </table>
          </td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_rr.png);"></td>
        </tr>  
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_bl.png" width="25" height="30"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bb.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_br.png" width="31" height="30"></td>
        </tr>  
       </table>
    </div>
     <div style="position: relative; z-index:100; left:880px; top:75px;  display:none; width:220px;"id="js07" onmouseover="navOver(7);" onmouseout="navOut(7);" >
      <table cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tl.png" width="25" height="21"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_tt.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_tr.png" width="31" height="21"></td>
        </tr>
        <tr>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_ll.png);"></td>
          <td class="drop" bgcolor="#DAF0FD" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bg.png);">
          <table border="0">           
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/center/manage_center.jsp">控制中心主页</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/script/script_manager.jsp">脚本管理</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/jprof/manage_jprof_host.jsp">Jprof模块 </a></td></tr>          
          </table>
          </td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_rr.png);"></td>
        </tr>  
        <tr>
          <td width="25"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_bl.png" width="25" height="30"></td>
          <td class="drop" style="background-image:url(<%=request.getContextPath () %>/statics/images/d_bb.png);"></td>
          <td width="31"><img class="drop" alt="" src="<%=request.getContextPath () %>/statics/images/d_br.png" width="31" height="30"></td>
        </tr>  
       </table>
    </div>
</div>
 </td>

		<td>
		<img width="999"    src="<%=request.getContextPath () %>/statics/images/my-account-pg22.gif">
		</td>

  </tr>

 <tr>
       <td colspan="2">
      <!-- menu -->
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
    <td onmouseover="this.style.cursor='pointer'" onclick="window.location.href='<%=request.getContextPath() %>/index.jsp'">
	<table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#FFFFFF">
		<td width="15" valign="top" rowspan="3">
			<img width="15" height="45" src="<%=request.getContextPath () %>/statics/images/btn_lh.gif" alt="">
		</td>
		<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_th.gif);">
			<img width="1" height="19" src="<%=request.getContextPath () %>/statics/images/spacer.gif" alt="">
		</td>
		<td width="14" valign="top" rowspan="3">
			<img width="14" height="45" src="<%=request.getContextPath () %>/statics/images/btn_rh.gif" alt="">
		</td>
		<tr>
			<td align="center" height="18">
			<table cellspacing="0" cellpadding="0" border="0"><tr>
			<td>
			<a href="<%=request.getContextPath() %>/index.jsp" class="menu">监控首页</a>
			</td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="8"></td>
		</tr></table>
	</td>
	<td onmouseout="navOut(1);" onmouseover="navOver(1);">
	<table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#DAF0FD">
		<tr>
			<td rowspan="3" width="15" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_l.gif" width="15" height="45"></td>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_t.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="19"></td>
			<td rowspan="3" width="14" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_r.gif" width="14" height="45"></td>
		</tr>
		<tr>
			<td align="center" height="18" >
			<table cellspacing="0" cellpadding="0" border="0"><tr>
			<td>
			<a href="<%=request.getContextPath () %>/index_time.jsp" class="menu">监控&报告</a>
			</td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_b.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="8"></td>
		</tr></table>
	</td>
	<td onmouseout="navOut(2);" onmouseover="navOver(2);">
	<table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#DAF0FD">
		<tr>
			<td rowspan="3" width="15" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_l.gif" width="15" height="45"></td>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_t.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="19"></td>
			<td rowspan="3" width="14" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_r.gif" width="14" height="45"></td>
		</tr>
		<tr>
			<td align="center" height="18">
			<table cellspacing="0" cellpadding="0" border="0"><tr>
			<td>
			<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit" class="menu">容量规划</a>
			</td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_b.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="8"></td>
		</tr></table>
	</td>
		<td onmouseout="navOut(3);" onmouseover="navOver(3);">
	<table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#DAF0FD">
		<tr>
			<td rowspan="3" width="15" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_l.gif" width="15" height="45"></td>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_t.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="19"></td>
			<td rowspan="3" width="14" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_r.gif" width="14" height="45"></td>
		</tr>
		<tr>
			<td align="center" height="18">
			<table cellspacing="0" cellpadding="0" border="0"><tr>
			<td>
			<a href="<%=request.getContextPath() %>/health/index.jsp" class="menu">性能跟踪体系</a>
			</td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_b.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="8"></td>
		</tr></table>
	</td>
		<td onmouseout="navOut(4);" onmouseover="navOver(4);">
	<table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#DAF0FD">
		<tr>
			<td rowspan="3" width="15" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_l.gif" width="15" height="45"></td>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_t.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="19"></td>
			<td rowspan="3" width="14" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_r.gif" width="14" height="45"></td>
		</tr>
		<tr>
			<td align="center" height="18">
			<table cellspacing="0" cellpadding="0" border="0"><tr>
			<td>
			<a href="<%=request.getContextPath () %>/dependent/index.jsp" class="menu">应用依赖</a>
			</td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_b.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="8"></td>
		</tr></table>
	</td>
		<td onmouseout="navOut(5);" onmouseover="navOver(5);">
	<table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#DAF0FD">
		<tr>
			<td rowspan="3" width="15" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_l.gif" width="15" height="45"></td>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_t.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="19"></td>
			<td rowspan="3" width="14" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_r.gif" width="14" height="45"></td>
		</tr>
		<tr>
			<td align="center" height="18">
			<table cellspacing="0" cellpadding="0" border="0"><tr>
			<td>
			<a href="<%=request.getContextPath () %>/alarm/alarm_record_detail.jsp" class="menu">告警历史</a>
			</td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_b.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="8"></td>
		</tr></table>
	</td>

	<td onmouseout="navOut(6);" onmouseover="navOver(6);">
	<table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#DAF0FD">
		<tr>
			<td rowspan="3" width="15" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_l.gif" width="15" height="45"></td>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_t.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="19"></td>
			<td rowspan="3" width="14" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_r.gif" width="14" height="45"></td>
		</tr>
		<tr>
			<td align="center" height="18">
			<table cellspacing="0" cellpadding="0" border="0"><tr>
			<td>
			<a href="<%=request.getContextPath () %>/alarm/manage_key.jsp" class="menu">监控管理</a>
			</td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_b.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="8"></td>
		</tr></table>
	</td>
		<td onmouseout="navOut(7);" onmouseover="navOver(7);">
	<table cellspacing="0" cellpadding="0" border="0" width="100%" bgcolor="#DAF0FD">
		<tr>
			<td rowspan="3" width="15" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_l.gif" width="15" height="45"></td>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_t.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="19"></td>
			<td rowspan="3" width="14" valign="top"><img alt="" src="<%=request.getContextPath () %>/statics/images/btn_r.gif" width="14" height="45"></td>
		</tr>
		<tr>
			<td align="center" height="18">
			<table cellspacing="0" cellpadding="0" border="0"><tr>
			<td>
			<a href="./center/manage_center.jsp" class="menu">控制中心</a>
			</td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td style="background-image:url(<%=request.getContextPath () %>/statics/images/btn_b.gif);"><img alt="" src="<%=request.getContextPath () %>/statics/images/spacer.gif" width="1" height="8"></td>
		</tr></table>
	</td>
	
        </tr>
      </table>     
      <!-- end of menu -->   
    </td>
    <td></td>    
  </tr>
    <tr height="30"></tr>
</table>
