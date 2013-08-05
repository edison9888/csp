<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/bootstrap/bootstrap-dropdown.js"></script>
<title>Sentinel</title>
</head>

<body style="padding-top:40px" class="span20">
<div class="topbar-wrapper" style="z-index: 5;">
    <div class="topbar" data-dropdown="dropdown" >
      <div class="topbar-inner">
        <div class="container">
          <h3><a href="#">Sentinel</a></h3>
          <ul class="nav">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle">权限授予</a>
              <ul class="dropdown-menu">
                 <li><a href="<%=request.getContextPath() %>/show.do?method=showWhiteListInterface">接口白名单</a></li>
                 <li><a href="<%=request.getContextPath() %>/show.do?method=showBlackListInterface">接口黑名单</a></li>
                 <li class="divider"></li>
                 <li><a href="<%=request.getContextPath() %>/show.do?method=showBlackListApp">应用黑名单</a></li>
                 <li class="divider"></li>
                 <li><a href="<%=request.getContextPath() %>/show.do?method=showWhiteListCustomer">自定义白名单</a></li>
                 <li><a href="<%=request.getContextPath() %>/show.do?method=showBlackListCustomer">自定义黑名单</a></li>
                 <li class="divider"></li>
                 <li><a href="<%=request.getContextPath() %>/invokeShow.do?method=searchInterfaceInvoke">接口调用管理</a></li>
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle">流量限制</a>
              <ul class="dropdown-menu">
                 <li><a href="./show.do?method=showFlowControlApp&strategy=thread">应用限流</a></li>
                 <li><a href="./show.do?method=showFlowControlInterface&strategy=thread">接口限流</a></li>
                 <li class="divider"></li>
                 <li><a href="./show.do?method=showFlowControlDependency">依赖限流</a></li>
                 <li class="divider"></li>
                 <li><a href="./show.do?method=showFlowControlApp&strategy=qps">Qps应用限流</a></li>
                 <li><a href="./show.do?method=showFlowControlInterface&strategy=qps">Qps接口限流</a></li>
                 <li class="divider"></li>
                 <li><a href="./show.do?method=showFlowControlParam">特殊参数限流</a></li>
                 <li class="divider"></li>
                 <li><a href="./show.do?method=showQpsPeriod">qps区间设置</a></li>
              </ul>
            </li>
            
            <li class="dropdown">
              <a href="#" class="dropdown-toggle">系统操作</a>
              <ul class="dropdown-menu">
                 <li><a href="./manage.do?method=goToPushConfig">推送配置</a></li>
                 <li><a href="./show.do?method=pushRecord">近期推送记录</a></li>
                 <li class="divider"></li>
                 <li><a href="#">演习</a></li>
              </ul>
            </li>
           
            <li class="dropdown">
              <a href="#" class="dropdown-toggle">当前运行状况</a>
              <ul class="dropdown-menu">
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsWhiteListInterface">接口白名单</a></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsBlackListInterface">接口黑名单</a></li>
                 <li class="divider"></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsBlackListApp">应用黑名单</a></li>
                 <li class="divider"></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsWhiteListCustomer">自定义白名单</a></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsBlackListCustomer">自定义黑名单</a></li>
                 <li class="divider"></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsFlowControlApp&strategy=thread">应用限流</a></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsFlowControlInterface&strategy=thread">接口限流</a></li>
                 <li class="divider"></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsFlowControlDependency">依赖限流</a></li>
                 <li class="divider"></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsFlowControlApp&strategy=qps">Qps应用限流</a></li>
                 <li><a href="<%=request.getContextPath() %>/info.do?method=ipsFlowControlInterface&strategy=qps">Qps接口限流</a></li>
              </ul>
            </li>
          </ul>
          <form class="pull-left" action="">
            <input type="text" placeholder="Search" />
          </form>
          <ul class="nav secondary-nav">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle">系统设置</a>
              <ul class="dropdown-menu">
                 <li><a href="./show.do?method=permissionList">权限管理</a></li>
                 <li class="divider"></li>
                 <li><a href="./show.do?method=dataUrlList">数据url维护</a></li>
              </ul>
            </li>
          </ul>
        </div>
      </div><!-- /topbar-inner -->
    </div><!-- /topbar -->
  </div><!-- /topbar-wrapper -->


</body>
</html>
