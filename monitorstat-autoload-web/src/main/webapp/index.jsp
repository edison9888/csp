<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.taobao.arkclient.csp.UserPermissionCheck"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GB18030" />
		<title>CSPϵͳ-�Զ�ѹ��</title>
		<script language="JavaScript"
			src="<%=request.getContextPath() %>/statics/swfobject.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath() %>/statics/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/jquery/jquery.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath() %>/statics/jquery/ui/ui.core.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/jquery/ui/ui.tabs.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/jquery/ui/jquery.ui.widget.js"></script>
		<script type="text/javascript" 
		   src="<%=request.getContextPath() %>/statics/jquery/validation/jquery.validate.js" ></script>	
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/style/css/style.css" />
		<link rel="shortcut icon" type="image/png"
			href="<%=request.getContextPath() %>/style/images/favicon.png" />
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/style/css/bootstrap-1.4.0.css" />
	
	 	<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/js/bootstrap-dropdown.js"></script>
	</head>
	<body>
		<div id="header" class="topbar">
			<div class="fill">
			    <div style="color:white;float:left;padding-top:9px;"></div>
				<div class="container fixed" >
					<h3>
						<a href="/autoload">Autoload</a>
					</h3>
					<ul class="menu">
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="#" class="dropdown-toggle">ѹ�����ü����</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/config.do?method=list">ѹ���ѯ</a></li>
								<li><a href="<%=request.getContextPath() %>/loadrun/config.do?method=gotoAdd">����ѹ��</a></li>
						    </ul>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="#" class="dropdown-toggle">ѹ�����</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/control.do?method=list">ѹ�����</a></li>
								<li><a href="<%=request.getContextPath() %>">ѹ��״̬</a></li>
						    </ul>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="#" class="dropdown-toggle">ѹ�����</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=showGuide">ѹ��ָ��</a></li>
						        <li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=showWarn">ѹ������</a></li>
								<li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=tairGuide">Tairѹ��ָ��</a></li>
						    </ul>
						</li>
					</ul>
					<div style="color:white;float:right;padding-top:10px;">��ӭ��:
					<% 
					  out.print(UserPermissionCheck.getLoginUserName(request)); 
					 %>
					�����Զ�ѹ��ϵͳ��</div>
				</div>
			</div>
		</div>
		<div class="container">
           <div id="bd" class="resource">   
            <div style="width: 990px; height: auto; margin: 0 auto">
               Slave ״̬ 
            </div>
			<div style="width: 990px; height: auto; margin: 0 auto">
				<table id="myTable" align="center">
					<tr align="center">
						<td>
							slave��ַ
						</td>
						<td>
							slave ID
						</td>
						<td>
							slave ���Job��
						</td>
						<td>
							slave ��ǰJob��
						</td>
					</tr>
					<tbody id="loadrunResultTable">
                         
					</tbody>
				</table>
			</div>
            <div style="width: 990px; height: auto; margin: 0 auto">
               �Զ�ѹ��״̬ 
            </div>
			<div style="width: 990px; height: auto; margin: 0 auto">

				<table id="myTable" align="center">
					<tr  align="center">
						<td>
							slave��ַ
						</td>
						<td colspan="3">
							ִ��JOB
						</td>
					</tr>
					<tbody id="JobTable">
                         
					</tbody>
				</table>

			</div>

			<div style="width: 990px; height: auto; margin: 0 auto">

				<table id="myTable" align="center">
					<tr>
						<td>
							�ֶ�ѹ������
						</td>
					</tr>
					<tbody id="manualTable">

					</tbody>
				</table>

			</div>
			</div>
		
<script type="text/javascript">
  function queryLoadValue() {
    $.getJSON("loadrun/show.do?method=showSlave", {},
    function(json) {
      $("#loadrunResultTable").empty();
      for (var i = 0; i < json.length; i++) {
        $("#loadrunResultTable").append("<tr align='center'><td>" + json[i]["s"] + "</td><td><a href='loadrun/control.do?method=showClassCache&id=" + json[i]["id"] + "' target='_blank'>" + json[i]["id"] + "</a></td><td>" + json[i]["c"] + "</td><td>" + json[i]["j"] + "</td></tr>");
      }

    });
    window.setTimeout("queryLoadValue()", 30000);
  }

  function queryJob() {
    $.getJSON("loadrun/show.do?method=showJob", {},
    function(json) {
      $("#JobTable").empty();
      for (s in json) {
        var a = json[s];
        var jobs = "";
        for (var i = 0; i < a.length; i++) {
          var m = a[i].split(":");
          var slaveId = m[0];
          var jobId = m[1];
          var jobDetail = m[2];
          jobs += jobId + ":" + jobDetail + "&nbsp;&nbsp;<a href='loadrun/control.do?method=stopJob&slaveId=" + slaveId + "&jobId=" + jobId + "'>ֹͣ����</a><br/>";
        }
        $("#JobTable").append("<tr align='center'><td>" + s + "</td><td colspan='3'>" + jobs + "</td></tr>");
      }

    });
    window.setTimeout("queryJob()", 30000);
  }

  function queryManual() {
    $.getJSON("loadrun/show.do?method=showManual", {},
    function(json) {
      $("#manualTable").empty();
      for (s in json) {
        $("#manualTable").append("<tr><td>" + json[s] + "</td></tr>");
      }

    });
    window.setTimeout("queryManual()", 30000);
  }

   queryJob();
   queryLoadValue();
   queryManual();
</script>
		<div style="width: 990px; height: 1px; margin: 0px auto; padding: 0px; background-color: #D5D5D5; overflow: hidden;"></div>
		<footer class="footer">
		<div style="float: left;">
			<p align="left">
				Powered by
				<strong>�Ա�����Ʒ������-�м��&�ȶ���ƽ̨</strong>
				<a href="mailto://youji.zj@taobao.com"> <img
						src="style/images/mail.png"></img> </a>
				<a target="_blank"
					href="http://amos.im.alisoft.com/msg.aw?v=2&uid=����&site=cntaobao&s=2&charset=gbk">
					<img border="0"
						src="http://amos.im.alisoft.com/online.aw?v=2&uid=����&site=cntaobao&s=2&charset=gbk" />
				</a>
				��ϵ����
				<br/>
				Copyright@
				<strong>2010-2012 Taobao.</strong> All Rights Reserved
			</p>
		</div>
		<div style="float: right; margin-top: 7px;">
			<p class="pull-right" align="right">
				<a href="http://cm.taobao.net:9999/monitorstat/index_day.jsp" target="_blank">CSP�ձ�</a>
				&nbsp;&nbsp;
				<a href="http://time.csp.taobao.net:9999/time/"
					target="_blank">CSPʵʱ���</a> &nbsp;&nbsp;
				<a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityLimit" target="_blank">CSP�����滮</a>
				&nbsp;&nbsp;
				<a href="http://depend.csp.taobao.net:9999/depend/app_info.do?method=gotoIndexPage" target="_blank">CSP����ϵͳ</a>
				&nbsp;&nbsp;
				<a href="http://sentinel.taobao.net:9999/sentinel/" target="_blank">Sentinel</a>
				&nbsp;&nbsp;
				<a href="http://110.75.18.198:9999/TinyMonitor/index.do?appName=detail&method=showIndex" target="_blank">SS����̨</a>
			</p>
		</div>
		</footer>
	</body>
</html>