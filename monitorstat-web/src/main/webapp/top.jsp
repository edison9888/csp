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
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/index_time.jsp">ʵʱ���</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/index_day.jsp">�ձ�</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/week_report.jsp" >�ܱ�</a></td></tr>
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
          <tr><td><a class="menu" href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit">��������</a></td></tr>
          <tr><td><a class="menu" href="http://capacity.taobao.net:9999/capacity/show.do?method=showMachine">����������Ԥ��</a></td></tr>
          <tr><td><a class="menu" href="http://110.75.26.90:9999/autoload/loadrun/show.do?method=list">����ѹ�����̨ </a></td></tr>
          <tr><td><a class="menu" href="http://capacity.taobao.net:9999/capacity/capacity/CapacityDenpend.jsp">�����ֲ�ͼ</a></td></tr>             
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
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/health/index.jsp">���ܽ���ָ��</a></td></tr> 
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/health/index.jsp">�����Ż���¼</a></td></tr> 
                       
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
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/distrib/distrib_provider.jsp">HSF�����ֲ� </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/tair/tair_show.jsp">Tair�����ֲ� </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/dependent/index.jsp">Ӧ������ </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/dependent/appinfo/appJar.jsp">jar������ </a></td></tr>         
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
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_record_detail.jsp">�澯��ʷ��ѯ </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_record.jsp">�澯��ʷ���� </a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_collect.jsp">�澯�û�ͳ��</a></td></tr>          
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
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/manage_key.jsp">�澯key����</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/user/manage_user.jsp">�澯�û�����</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_record_exception1.jsp">�쳣��Ϣ�鿴</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/alarm/alarm_filter.jsp">�澯����</a></td></tr> 
          <tr><td><a class="menu" href="<%=request.getContextPath()%>/reportConfig.do">��������</a></td></tr>            
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
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/center/manage_center.jsp">����������ҳ</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/script/script_manager.jsp">�ű�����</a></td></tr>
          <tr><td><a class="menu" href="<%=request.getContextPath () %>/jprof/manage_jprof_host.jsp">Jprofģ�� </a></td></tr>          
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
			<a href="<%=request.getContextPath() %>/index.jsp" class="menu">�����ҳ</a>
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
			<a href="<%=request.getContextPath () %>/index_time.jsp" class="menu">���&����</a>
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
			<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit" class="menu">�����滮</a>
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
			<a href="<%=request.getContextPath() %>/health/index.jsp" class="menu">���ܸ�����ϵ</a>
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
			<a href="<%=request.getContextPath () %>/dependent/index.jsp" class="menu">Ӧ������</a>
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
			<a href="<%=request.getContextPath () %>/alarm/alarm_record_detail.jsp" class="menu">�澯��ʷ</a>
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
			<a href="<%=request.getContextPath () %>/alarm/manage_key.jsp" class="menu">��ع���</a>
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
			<a href="./center/manage_center.jsp" class="menu">��������</a>
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
