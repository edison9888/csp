<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/bootstrap/bootstrap-dropdown.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery.fixedtableheader-1.0.2.js"></script>
<title>Capacity</title>
</head>
<body style="padding-top:40px" class="span20">

<div class="topbar-wrapper" style="z-index: 5;">
    <div class="topbar" data-dropdown="dropdown" >
      <div class="topbar-inner">
        <div class="container">
          <h3><a href="./index.do">CSP�ɱ���ҳ</a></h3>
          <ul class="nav">
         
				<li class="dropdown">
					  <a href="./index.do" class="dropdown-toggle">����</a>
					  <ul class="dropdown-menu">
						 <li><a href="./appCost.do?method=showTop">��˾ÿ��ƽ���ɱ�����</a></li>
						 <li><a href="./appCost.do?method=showPreTop">ÿǧ�ε��óɱ�top10</a></li>
						 <li><a href="./appCost.do?method=showApp">Ӧ��ÿ�ܳɱ���ϸ</a></li>
					 </ul>
				</li>
				 
                  <li><a href="./cost20121111.do?type=tb">˫11taobao����Ӧ�óɱ��仯</a></li>
                  <li><a href="./cost20121111.do?type=tm">˫11tmall����Ӧ�óɱ��仯</a></li>
				  <li><a href="<%=request.getContextPath() %>/cost_form.jsp" target="_blank">�ɱ����㹫ʽ</a></li>
             
          </ul>
         
          <ul class="nav secondary-nav">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle">CSP����ϵͳ</a>
              <ul class="dropdown-menu">
                <li><a href="http://time.csp.taobao.net:9999/time/index_table.jsp">ʵʱ���</a></li>
                <li><a href="http://110.75.2.75:9999/depend/show.do?method=showMeDependTable">����ϵͳ</a></li>
                <li class="divider"></li>
                <li><a href="#">����</a></li>
              </ul>
            </li>
          </ul>

        </div>
      </div><!-- /topbar-inner -->
    </div><!-- /topbar -->
  </div><!-- /topbar-wrapper -->


</body>
</html>
