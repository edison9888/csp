<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","Tair压测指南"); %>
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<div id="bd" class="resource" style="width: 1050px;">
		<div id="manual_content">
			<p>
				<b  style="color:#987654">一、Tair压测的方式及优势
				</b> <br />
				 <div style="background-color:#FFFFFF">方式：用btrace动态修改字节码的方式，来进行压测
					<br/>优势：对应用方（开发，PE）来说，什么也不用做，重启之后也能完全恢复
				</div>
			</p>
              	<br />
              	
            <p>
				<b  style="color:#987654">二、压测的模块
				</b> <br />
				 <div style="background-color:#FFFFFF">
				 	1.线程池和队列
				 	<br/>
				 	&nbsp;&nbsp;引入线程池和队列，使得请求的重放是异步的，不会影响到业务系统
					<br/>
					<br/>
					2.压测重放模块
				 	<br/>
				 	&nbsp;&nbsp;使用TairClientFactory创建到指定压测tair server的连接，发送请求，达到压测目的。连接有缓存不会重复创建，压测完毕后从缓存移除，关闭连接
					<br/>
					<br/>
					3.日志记录模块
				 	<br/>
				 	&nbsp;&nbsp;每5s刷一次日志，记录以下内容：
				 	Get请求日志（请求次数、响应时间 ）;
					Put请求日志(请求次数、响应时间、value大小)
					<br/>
					<br/>
					
					4.参数控制模块
				 	<br/>
				 	&nbsp;&nbsp;参数通过diamond推送到client，再通过client发送到app，实现控制目的。
				 	<br/>
				 	&nbsp;&nbsp;可控制以下参数：
				 	<br/>
					&nbsp;&nbsp;1.拦截的方法get/put/other 
					<br/>
					&nbsp;&nbsp;2.请求重放次数，比如一个get请求，重复多少次，不精确到机器
					<br/>
					<br/>
					
				</div>
			</p>
                
			
		</div>
	</div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>