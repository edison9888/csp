<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/bootstrap/bootstrap.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery.fixedtableheader-1.0.2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap/bootstrap-dropdown.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap/bootstrap-modal.js"></script>
<title>Capacity</title>
</head>
<body style="padding-top:40px" class="span20">
<div id="header" class="topbar">
    <div class="fill">
				<div style="color:white;float:left;padding-top:9px;"></div>
				<div class="container fixed" >
					<h3>
						<a href="<%=request.getContextPath() %>/show.do?method=showCapacityLimit">�����滮</a>
					</h3>
					<ul class="menu">
						<li class="dropdown" id="caplevel">
          					<a href="#caplevel" class="dropdown-toggle" data-toggle="dropdown">����ˮλ</a>
             				<ul class="dropdown-menu">
                				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityLimit">����Ӧ��</a></li>
                				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityTair">TairӦ��</a></li>
                				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityMore">����Ӧ��</a></li>
             				</ul>
             			</li>
             
             			<li class="dropdown" id="capop">
          					<a href="#capop" class="dropdown-toggle" data-toggle="dropdown">��������</a>
             				<ul class="dropdown-menu">
                				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityApp">����Ӧ��ά��</a></li>
                				<li><a href="<%=request.getContextPath() %>/capacity/manage.do?method=manageCapacityCap">���������ֶ�ά��</a></li>
                				<li><a href="<%=request.getContextPath() %>/capacity/manage.do?method=reflushRanking">�������¼���</a></li>
             				</ul>
            			 </li>
            
           			 	<li class="dropdown" id="costcenter">
              				<a href="#costcenter" class="dropdown-toggle" data-toggle="dropdown">�ɱ�����</a>
              				<ul class="dropdown-menu">
                 				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityCostListNormal">����Ӧ��</a></li>
                 				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityCostListTair">Tair</a></li>
                 				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityCostListDB">DB</a></li>
                 				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityCostConfig">ConfigServer</a></li>
                 				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityCostDiamond">Diamond</a></li>
                 				<li class="divider"></li>
                 				<li><a href="<%=request.getContextPath() %>/show.do?method=showCapacityCostRatio">�ɱ����ӵ���</a></li>
                 				<li class="divider"></li>
                 				<li><a href="<%=request.getContextPath() %>/show.do?method=showCostHelp">�ɱ����İ���</a></li>
              				</ul>
            			</li>
            			
            			<li class="dropdown" id="costcenter">
              				<a href="#forecastmodel" class="dropdown-toggle" data-toggle="dropdown">����Ԥ��ģ��</a>
              				<ul class="dropdown-menu">
                 				<li><a href="<%=request.getContextPath() %>/model.do?method=showModelList">ģ���б�</a></li>
                 				<li><a href="<%=request.getContextPath() %>/model.do?method=capacityModelCompute">ģ�ͼ���</a></li>
              				</ul>
            			</li>
					
            			<li class="dropdown" id="otherSystem">
              				<a href="#" class="dropdown-toggle" data-toggle="dropdown">CSP����ϵͳ</a>
              				<ul class="dropdown-menu">
              					<li><a href="http://cm.taobao.net:9999/monitorstat/index_day.jsp">�ձ�ͳ��</a></li>
                				<li><a href="http://time.csp.taobao.net:9999/time/index_table.jsp">ʵʱ���</a></li>
                				<li><a href="http://depend.csp.taobao.net:9999/depend/app_info.do?method=gotoIndexPage">����ϵͳ</a></li>
								<li><a href="http://sentinel.taobao.net:9999/sentinel/">sentinel</a></li>
              				</ul>
            			</li>
					</ul>
					
				</div>
			</div>
		</div>
		
					
         


</body>
</html>
