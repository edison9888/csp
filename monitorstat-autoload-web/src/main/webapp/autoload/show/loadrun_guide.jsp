<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","压测指南"); %>
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<div id="bd" class="resource" style="width: 1050px;">
		<div id="manual_content">
			<h3>压测配置指南:</h3>
	         <br />
			<p>
				<b  style="color:#987654">一、apache或者nginx方式压测，需要打出jboss或者tomcat的log</b> <br /> <div style="background-color:#FFFFFF">jboss修改配置文件:
				<code>conf/tomcat-server.xml</code>
				<br /> tomcat修改配置文件:
				<code>/opt/taobao/tomcat/conf/server.xml;/home/admin/appname/.default/conf/server.xml</code>
				<br /> 加入一段<code> &lt;Valve
				className="org.apache.catalina.valves.AccessLogValve"
				prefix="localhost_access_log." suffix=".log" pattern="%t %s %m %b
				%D" directory="/home/admin/logs/tomcataccess" resolveHosts="false"
				/&gt;</code>。</div>
			</p>
              	<br />
                
			<p>
				<b  style="color:#987654">二、apache压测方式jk配置文件路径需要填写正确的mod_jk配置文件的完整路径（注意：需要填写到具体的文件），压测采取的方式会直接修改这个配置文件。</b>
				<br />
			</p>
             <br />
			<p>
				<b  style="color:#987654"> 三、apacheProxy压测方式proxy配置文件路径只需要填写到目录级别（例如：<code>/home/admin/cai/conf/extra/</code>），注意最后面的"/"不能少，
					<br />压测采取的方式会生成一个mod_proxy的配置文件到这个目录，所以
					要求httpd.conf这个主配置文件中Include的方式为类似 Include "<code>/home/admin/cai/conf/extra/*.conf</code>"。
				</b>
			</p>
<br />
			<p>
				<b  style="color:#987654">
					 四、nginxProxy压测模式proxy配置文件路径需要填写到对应location文件的完整路径（注意：需要填写到具体的文件），压测采取的方式会直接修改这个配置文件。 
				</b>
			</p>
<br />
			<p>
				<b  style="color:#987654">五、压测模式请选择SSH。SCRIPT方式是之前专门为B2B准备的。 </b>
			</p>
<br />
			<p>
				<b  style="color:#987654">
					六、压测开始执行url和压测结束执行url用于压测期间限流开关的打开和关闭。可在压测开始执行url中关闭限流开关，压测结束执行url中重新打开限流开关。
				</b>
			</p>
<br />
			<p>
				<b  style="color:#987654"> 七、压测阀值的设置，请默认配上cpu和load阀值，其它阀值可以根据实际情况进行添加，比如rt。 </b>
			</p>
		</div>
	</div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>