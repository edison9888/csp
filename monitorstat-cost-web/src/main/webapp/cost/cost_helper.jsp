<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/statics/css/style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<title>�ɱ�����</title>

</head>
<body style="padding-top:45px" class="span20">
<%@ include file="../../top.jsp" %>

<style type="text/css">
#demo{font-size:13px;color:#333;width:1000px;padding-top:20px;}
#demo img{float:left; margin:0px 10px 10px 0;}
#main{clear:both;}
#appMain{text-align:center;}
</style>

<div id="demo">
	<img src="<%=request.getContextPath() %>/statics/images/cost_down.JPG" style="width:200px;hight:80px;"/>
	��վ��ģ������ϵͳ�ĳɱ����Ϊ��˾��֧������Ҫ���֣���˳ɱ����ƾ��Ե÷ǳ���Ҫ��֮ǰ��ũ���ڰ���ζ���������˶��������ָ���ɱ��˷ѵ�����
	��<a href="http://www.aliway.com/read.php?fid=38&tid=157787">�������ϵġ�ˮ����һ���˷ѳ���һ��</a> ��
	��<a href="http://www.aliway.com/read.php?fid=38&tid=155433">ҵ��һȥ���������豸����600��</a> ��
	��<a href="http://www.aliway.com/read.php?fid=38&tid=153865">�ݳ޵�����II��1���˷�2143��</a> ������������Ӧ�õ�loadһֱ�ǱȽϵ͵�״̬��
	���������������Ƕ���ͳ��Ӧ�õĳɱ���<br/><br/>
	�����뿴��<br/>
	<a target="_blank" href="./appCost.do?method=showTop" style="font-size:12px;color:red;">
	��˾ÿ��ƽ���ɱ�����</a>������֯����ĳɱ�����<br/>
	<a target="_blank" href="./appCost.do?method=showApp" style="font-size:12px;color:red;">
	Ӧ��ÿ�ܳɱ���ϸ</a>���� ����Ӧ�ò��棬��ϸ����������Ӳ����hsf������db��tair�� <br/>
	<a target="_blank" href="./appCost.do?method=showPreTop" style="font-size:12px;color:red;">
	ÿǧ�ε��óɱ�top10</a> �������ֵ�Ƚϸߵ�Ӧ�ã�˵�������ʲ���
	<br/>
	
	<br>
</div>

<hr style="clear:both;"/>

Ŀǰҵ��ϵͳ�͸���C�Լ�һЩ������ʩ��������ͼ��������ϵ:
<div id="appMain">

	<img src="<%=request.getContextPath() %>/statics/images/app_arc.JPG" style="margin:auto;top:auto;width:550px;hight:400px;"/>
</div>

������ǵó�������ĳɱ����㷽ʽ��<br/>
1.��������ϵͳֻ��Ӳ���ɱ��Լ���ά�ɱ�(tair/db��)<br/>
2.ҵ��ϵͳ�Լ�����C������Ӳ���ɱ�����ά�ɱ���Ҫ���������ɱ�<br/>


<img src="<%=request.getContextPath() %>/statics/images/cost_all.JPG" style="width:450px;hight:60px;"/><br/>
���У�<br/>
<img src="<%=request.getContextPath() %>/statics/images/cost_hard.jpg" style="width:390px;hight:55px;"/><br/>
<img src="<%=request.getContextPath() %>/statics/images/cost_ops.jpg" style="width:200px;hight:25px;"/><br/><br/>

<hr/>
<div id="main">
	Ŀǰ�ɱ����ĵ����ݸ�����������ȫ�����������У���л��ҵ�֧�֣���
</div>

</body>
</html>