<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.po.tfs.TfsProviderWebPo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.po.hsf.InterfaceSummary"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.po.hsf.ProvideSiteRating"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/statics/css/main.css" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<title>����ģ��</title>
<%
String selectDate = (String)request.getAttribute("selectDate");
Map<String, TfsProviderWebPo> curMap = (Map<String, TfsProviderWebPo>)request.getAttribute("curMap"); 
Map<String, TfsProviderWebPo> preMap = (Map<String, TfsProviderWebPo>)request.getAttribute("preMap"); 
Map<String, Long> typeMapNormal = (Map<String, Long>)request.getAttribute("typeMapNormal"); 
Map<String, Long> typeMapRush = (Map<String, Long>)request.getAttribute("typeMapRush"); 


if(curMap == null || preMap == null) {
  curMap = new HashMap();
  preMap = new HashMap();
}

if(typeMapNormal == null || typeMapRush == null) {
  typeMapNormal = new HashMap();
  typeMapRush = new HashMap();
}

//��֯��ͼ������
StringBuilder sbNormal = new StringBuilder();
sbNormal.append("<pie>");
for(String normalType : typeMapNormal.keySet()){
	sbNormal.append("<slice title='"+normalType+"'>" + typeMapNormal.get(normalType)+"</slice>");			
}
sbNormal.append("</pie>");

StringBuilder sbRush = new StringBuilder();
sbRush.append("<pie>");
for(String normalType : typeMapRush.keySet()){
	sbRush.append("<slice title='"+normalType+"'>" + typeMapRush.get(normalType)+"</slice>");			
}
sbRush.append("</pie>");


%>
</head>
<body>
<%@ include file="../../../header.jsp"%>
<form id="mainForm"  action="<%=request.getContextPath() %>/show/tfsprovide.do" method="get">
  <input type="hidden" value="showTfsMain" name="method">
  <div class="mainwrap">
     <div id="page_nav"></div>
		<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>
    <table>
      <tr>
        <td valign="top"><table>
            <tr>
              
              <td width="100%" valign="top">
			  		<table  width="100%" >
					  <tr>
						<td valign="top">
						  <table width="100%"  class="table table-striped table-bordered table-condensed">
							<tr>
							  <td width="500" align="center"><span style="font-weight:bold">TFS�߷��ڶ�д���</span><div id="chartdiv1" align="center"></div></td>
							  <td width="500" align="center"><span style="font-weight:bold">TFSƽ����д���</span><div id="chartdiv2" align="center"></div></td>
							</tr>
                      	</table>
					</td>
                  </tr>
                  <tr>
                    <td><table  width="100%"  class="table table-striped table-bordered table-condensed">
                        <tr>
                          <td colspan="27" align="center">TFS������Ϣ &nbsp;&nbsp;</td>
                        </tr>
                        <tr>
                          <td align="center" rowspan="3">Ӧ��</td>
                          <td align="center" colspan="13">�߷���ֵ</td>
                          <td align="center" colspan="13">ƽ����ֵ</td>
                        </tr>
                        <tr align="center">
                          <td colspan="5" >read</td>
                          <td colspan="4">write</td>
                          <td colspan="4">unlink</td>
                          <td colspan="5">read</td>
                          <td colspan="4">write</td>
                          <td colspan="4">unlink</td>
                        </tr>
                        <tr align="center">
                          <td>�������</td>
                          <td>�����С(�ֽ�)</td>
                          <td>��Ӧʱ��(ms)</td>
                          <td>�ɹ�����</td>
                          <td>������</td>
                          <td>�������</td>
                          <td>�����С(�ֽ�)</td>
                          <td>��Ӧʱ��(ms)</td>
                          <td>�ɹ�����</td>
                          <td>�������</td>
                          <td>�����С(�ֽ�)</td>
                          <td>��Ӧʱ��(ms)</td>
                          <td>�ɹ�����</td>
                          <td>�������</td>
                          <td>�����С(�ֽ�)</td>
                          <td>��Ӧʱ��(ms)</td>
                          <td>�ɹ�����</td>
                          <td>������</td>
                          <td>�������</td>
                          <td>�����С(�ֽ�)</td>
                          <td>��Ӧʱ��(ms)</td>
                          <td>�ɹ�����</td>
                          <td>�������</td>
                          <td>�����С(�ֽ�)</td>
                          <td>��Ӧʱ��(ms)</td>
                          <td>�ɹ�����</td>
                        </tr>
                  <%
                  	for(String appName: curMap.keySet()) {
                  	  TfsProviderWebPo curPo = curMap.get(appName);
                  	  TfsProviderWebPo prePo = preMap.get(appName);
                  	  if(prePo == null)
                  	    prePo = new TfsProviderWebPo();
                  	  out.print("<tr>");
                  	  out.print("<td>" + appName + "</td>");
                  	  //�߷����
                  	  Map<String, String> rushReadMap = curPo.rushMap.get("read");
                  	  Map<String, String> rushPreReadMap = prePo.rushMap.get("read");
					  if(rushReadMap == null)
					    rushReadMap = new HashMap<String,String>();
					  if(rushPreReadMap == null)
					    rushPreReadMap = new HashMap<String,String>();
					  
                  	  out.print("<td>" + Utlitites.fromatLong(rushReadMap.get("oper_times")) + "&nbsp;" + Utlitites.scale(rushReadMap.get("oper_times"), rushPreReadMap.get("oper_times")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushReadMap.get("oper_size")) + "&nbsp;" + Utlitites.scale(rushReadMap.get("oper_size"), rushPreReadMap.get("oper_size")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushReadMap.get("oper_rt")) + "&nbsp;" + Utlitites.scale(rushReadMap.get("oper_rt"), rushPreReadMap.get("oper_rt")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushReadMap.get("oper_succ")) + "&nbsp;" + Utlitites.scale(rushReadMap.get("oper_succ"), rushPreReadMap.get("oper_succ")) + "</td>");
                  	  out.print("<td>" + rushReadMap.get("cache_hit_ratio") + "%&nbsp;" + Utlitites.scale(rushReadMap.get("cache_hit_ratio"), rushPreReadMap.get("cache_hit_ratio")) + "</td>");
                  	  
                  	  Map<String, String> rushWriteMap = curPo.rushMap.get("write");
                  	  Map<String, String> rushPreWriteMap = prePo.rushMap.get("write");
          			  if(rushWriteMap == null)
          			    rushWriteMap = new HashMap<String,String>();
          			  if(rushPreWriteMap == null)
          			    rushPreWriteMap = new HashMap<String,String>();
                  	  
                  	  out.print("<td>" + Utlitites.fromatLong(rushWriteMap.get("oper_times")) + "&nbsp;" + Utlitites.scale(rushWriteMap.get("oper_times"), rushPreWriteMap.get("oper_times")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushWriteMap.get("oper_size")) + "&nbsp;" + Utlitites.scale(rushWriteMap.get("oper_size"), rushPreWriteMap.get("oper_size")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushWriteMap.get("oper_rt")) + "&nbsp;" + Utlitites.scale(rushWriteMap.get("oper_rt"), rushPreWriteMap.get("oper_rt")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushWriteMap.get("oper_succ")) + "&nbsp;" + Utlitites.scale(rushWriteMap.get("oper_succ"), rushPreWriteMap.get("oper_succ")) + "</td>");
                  	  
                  	  Map<String, String> rushUnlinkMap = curPo.rushMap.get("unlink");
                  	  Map<String, String> rushPreUnlinkMap = prePo.rushMap.get("unlink");
              		  if(rushUnlinkMap == null)
              		    rushUnlinkMap = new HashMap<String,String>();
              		  if(rushPreUnlinkMap == null)
              		    rushPreUnlinkMap = new HashMap<String,String>();                  	  
                  	  
                  	  out.print("<td>" + Utlitites.fromatLong(rushUnlinkMap.get("oper_times")) + "&nbsp;" + Utlitites.scale(rushUnlinkMap.get("oper_times"), rushPreUnlinkMap.get("oper_times")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushUnlinkMap.get("oper_size")) + "&nbsp;" + Utlitites.scale(rushUnlinkMap.get("oper_size"), rushPreUnlinkMap.get("oper_size")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushUnlinkMap.get("oper_rt")) + "&nbsp;" + Utlitites.scale(rushUnlinkMap.get("oper_rt"), rushPreUnlinkMap.get("oper_rt")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(rushUnlinkMap.get("oper_succ")) + "&nbsp;" + Utlitites.scale(rushUnlinkMap.get("oper_succ"), rushPreUnlinkMap.get("oper_succ")) + "</td>");
                  	  
                  	  
                  	  //ƽ�����
                  	  Map<String, String> readMap = curPo.norMap.get("read");
                  	  Map<String, String> preReadMap = prePo.norMap.get("read");
                	  if(readMap == null)
                	    readMap = new HashMap<String,String>();
                	  if(preReadMap == null)
                	    preReadMap = new HashMap<String,String>();            
                		  
                  	  out.print("<td>" + Utlitites.fromatLong(readMap.get("oper_times")) + "&nbsp;" + Utlitites.scale(readMap.get("oper_times"), preReadMap.get("oper_times")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(readMap.get("oper_size")) + "&nbsp;" + Utlitites.scale(readMap.get("oper_size"), preReadMap.get("oper_size")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(readMap.get("oper_rt")) + "&nbsp;" + Utlitites.scale(readMap.get("oper_rt"), preReadMap.get("oper_rt")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(readMap.get("oper_succ")) + "&nbsp;" + Utlitites.scale(readMap.get("oper_succ"), preReadMap.get("oper_succ")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(readMap.get("cache_hit_ratio")) + "%&nbsp;" + Utlitites.scale(readMap.get("cache_hit_ratio"), preReadMap.get("cache_hit_ratio")) + "</td>");
                  	  
                  	  Map<String, String> writeMap = curPo.norMap.get("write");
                  	  Map<String, String> preWriteMap = prePo.norMap.get("write");
                	  if(writeMap == null)
                	    writeMap = new HashMap<String,String>();
                	  if(preWriteMap == null)
                	    preWriteMap = new HashMap<String,String>();            
                		  
                  	  out.print("<td>" + Utlitites.fromatLong(writeMap.get("oper_times")) + "&nbsp;" + Utlitites.scale(writeMap.get("oper_times"), preWriteMap.get("oper_times")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(writeMap.get("oper_size")) + "&nbsp;" + Utlitites.scale(writeMap.get("oper_size"), preWriteMap.get("oper_size")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(writeMap.get("oper_rt")) + "&nbsp;" + Utlitites.scale(writeMap.get("oper_rt"), preWriteMap.get("oper_rt")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(writeMap.get("oper_succ")) + "&nbsp;" + Utlitites.scale(writeMap.get("oper_succ"), preWriteMap.get("oper_succ")) + "</td>");
                  	  
                  	  Map<String, String> unlinkMap = curPo.norMap.get("unlink");
                  	  Map<String, String> preUnlinkMap = prePo.norMap.get("unlink");
                	  if(unlinkMap == null)
                	    unlinkMap = new HashMap<String,String>();
                	  if(preUnlinkMap == null)
                	    preUnlinkMap = new HashMap<String,String>();            
                		  
                  	  out.print("<td>" + Utlitites.fromatLong(unlinkMap.get("oper_times")) + "&nbsp;" + Utlitites.scale(unlinkMap.get("oper_times"), preUnlinkMap.get("oper_times")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(unlinkMap.get("oper_size")) + "&nbsp;" + Utlitites.scale(unlinkMap.get("oper_size"), preUnlinkMap.get("oper_size")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(unlinkMap.get("oper_rt")) + "&nbsp;" + Utlitites.scale(unlinkMap.get("oper_rt"), preUnlinkMap.get("oper_rt")) + "</td>");
                  	  out.print("<td>" + Utlitites.fromatLong(unlinkMap.get("oper_succ")) + "&nbsp;" + Utlitites.scale(unlinkMap.get("oper_succ"), preUnlinkMap.get("oper_succ")) + "</td>");
                  	  
                  	  out.print("</tr>");
                  	}
                  %>                        
                      </table></td>
                  </tr>
                  <tr></tr>
                </table></td>
            </tr>
          </table></td>
      </tr>
    </table>
  </div>
</form>
<script type="text/javascript">
//��ʼ��search bar
$(document).ready(function(){
	//$('#mm').accordion('select', "�ҵ���ϸ��Ϣ");
	//changeColor('provoidHSF');
	var so1 = new SWFObject("<%=request.getContextPath() %>/statics/ampie/ampie.swf", "ampie", "500", "380", "8", "#FFFFFF");
	so1.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
	so1.addVariable("chart_id", "amline");   
	so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/ampie/ampie_settings.xml");
	so1.addVariable("chart_data", encodeURIComponent("<%=sbNormal.toString()%>"));
	so1.write("chartdiv1");	
	
	var so2 = new SWFObject("<%=request.getContextPath() %>/statics/ampie/ampie.swf", "ampie", "500", "380", "8", "#FFFFFF");
	so2.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
	so2.addVariable("chart_id", "amline");   
	so2.addVariable("settings_file", "<%=request.getContextPath() %>/statics/ampie/ampie_settings.xml");
	so2.addVariable("chart_data", encodeURIComponent("<%=sbRush.toString()%>"));
	so2.write("chartdiv2");		
}); 
</script>
</body>
</html>
