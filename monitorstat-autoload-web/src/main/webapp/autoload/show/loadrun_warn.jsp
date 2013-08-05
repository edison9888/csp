<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","压测提醒"); %>
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<div id="bd" class="resource" style="width: 1050px;">
		<div id="manual_content">
			<h3 style="color:#FF0000">线上压测有风险，操作请谨慎！注意几点：</h3>
	        <br />
	        
			<p>
				<b  style="color:#987654">1.新应用压测，要找人一起仔细review下配置，一开始要做手动压测，不能做自动压测。
				</b> <br /> 
			</p>
			
			<p>
				<b  style="color:#987654">2.被压测的新机器必须先开通个人账号，确保压测的个人账号设置没问题。
				</b> <br /> 
			</p>
			
			<p>
				<b  style="color:#987654">3.被压测的机器上如果有web, 请先添加下tsar里的qps和rt指标。
				</b> <br /> 
			</p>
			
			<p>
				<b  style="color:#987654">4.手动压测的时候一定要多观察系统负载的变化，tsar –l –I 1 不停看着。
				</b> <br /> 
			</p>
			
			<p>
				<b  style="color:#987654">5.压测的时间不要过长，譬如超过2个小时，分配的流量不要过多，譬如不要超过20%。
				</b> <br /> 
			</p>
			
			<p>
				<b  style="color:#987654">6.多次手工压测稳定之后（一周之后）才能转成自动压测。
				</b> <br /> 
			</p>
			
			<p>
				<b  style="color:#987654">7.建议纯web的，和登陆cookie影响不大的，可以先用httpdload做压测，要先停止对外服务，相对压力也固定些。
				</b> <br /> 
			</p>
			
			<p>
				<b  style="color:#987654">8.<a href="http://x.wf.taobao.org/PE/%E9%87%8D%E8%A6%81%E9%A1%B9%E7%9B%AE/%E5%AE%B9%E9%87%8F%E7%AE%A1%E7%90%86/CSP%E5%8E%8B%E6%B5%8B%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E" target="_blank">
				更多注意事项</a>
				</b> <br /> 
			</p>
			
			
              
		</div>
	</div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>