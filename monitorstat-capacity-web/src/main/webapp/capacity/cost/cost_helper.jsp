<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityRankingPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>�ɱ����㹫ʽ</title>

</head>
<body style="padding-top:45px" class="span20">
<%@ include file="../../top.jsp" %>

<br>

<strong><font size="3">һ��������ɱ�ֵ�Ĺ�ϵ</font></strong>
<br/>
<br/>
һ̨���� = 10��9�η����ɱ���������1���ɱ� = 10�ĸ�9�η�̨����<br/>
���detail��itemcenter�������ɱ�Ϊ2��,˵��itemcenter����̨�����Ǳ�detail������<br/><br/><br/>

<strong><font size="3">�������ε��óɱ�</font></strong>
<br/>
<br/>
1��ֱ�ӳɱ����������Ļ�����<br/>
2�������ɱ����������Ӧ�õĻ�����
<br/><br/><br/>

<strong><font size="3">��������ģʽ</font></strong>
<br/>
<br/>
1��ֱ��������detail->itemcenter,itemcenterΪdetailֱ��������<br/>
2�����������detail->itemcenter-tair1,tair1Ϊdetail�ļ��������
&nbsp;��������в�����hsf��������,����detail->itemcenter-uic,uic����ͳ�Ƶ�detail�ļ��������
<br/><br/><br/>

Ŀǰ�ɱ����ĵ����ݸ�����������ȫ�����������У���л��ҵ�֧�֣���

</body>
</html>