<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","ѹ��ָ��"); %>
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<div id="bd" class="resource" style="width: 1050px;">
		<div id="manual_content">
			<h3>ѹ������ָ��:</h3>
	         <br />
			<p>
				<b  style="color:#987654">һ��apache����nginx��ʽѹ�⣬��Ҫ���jboss����tomcat��log</b> <br /> <div style="background-color:#FFFFFF">jboss�޸������ļ�:
				<code>conf/tomcat-server.xml</code>
				<br /> tomcat�޸������ļ�:
				<code>/opt/taobao/tomcat/conf/server.xml;/home/admin/appname/.default/conf/server.xml</code>
				<br /> ����һ��<code> &lt;Valve
				className="org.apache.catalina.valves.AccessLogValve"
				prefix="localhost_access_log." suffix=".log" pattern="%t %s %m %b
				%D" directory="/home/admin/logs/tomcataccess" resolveHosts="false"
				/&gt;</code>��</div>
			</p>
              	<br />
                
			<p>
				<b  style="color:#987654">����apacheѹ�ⷽʽjk�����ļ�·����Ҫ��д��ȷ��mod_jk�����ļ�������·����ע�⣺��Ҫ��д��������ļ�����ѹ���ȡ�ķ�ʽ��ֱ���޸���������ļ���</b>
				<br />
			</p>
             <br />
			<p>
				<b  style="color:#987654"> ����apacheProxyѹ�ⷽʽproxy�����ļ�·��ֻ��Ҫ��д��Ŀ¼�������磺<code>/home/admin/cai/conf/extra/</code>����ע��������"/"�����٣�
					<br />ѹ���ȡ�ķ�ʽ������һ��mod_proxy�������ļ������Ŀ¼������
					Ҫ��httpd.conf����������ļ���Include�ķ�ʽΪ���� Include "<code>/home/admin/cai/conf/extra/*.conf</code>"��
				</b>
			</p>
<br />
			<p>
				<b  style="color:#987654">
					 �ġ�nginxProxyѹ��ģʽproxy�����ļ�·����Ҫ��д����Ӧlocation�ļ�������·����ע�⣺��Ҫ��д��������ļ�����ѹ���ȡ�ķ�ʽ��ֱ���޸���������ļ��� 
				</b>
			</p>
<br />
			<p>
				<b  style="color:#987654">�塢ѹ��ģʽ��ѡ��SSH��SCRIPT��ʽ��֮ǰר��ΪB2B׼���ġ� </b>
			</p>
<br />
			<p>
				<b  style="color:#987654">
					����ѹ�⿪ʼִ��url��ѹ�����ִ��url����ѹ���ڼ��������صĴ򿪺͹رա�����ѹ�⿪ʼִ��url�йر��������أ�ѹ�����ִ��url�����´��������ء�
				</b>
			</p>
<br />
			<p>
				<b  style="color:#987654"> �ߡ�ѹ�ֵⷧ�����ã���Ĭ������cpu��load��ֵ��������ֵ���Ը���ʵ�����������ӣ�����rt�� </b>
			</p>
		</div>
	</div>
</div>
<jsp:include page="../footer.jsp"></jsp:include>