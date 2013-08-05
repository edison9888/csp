<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.regex.Matcher"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil.CallBack"%>
<%@page import="java.util.regex.Pattern"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.taobao.monitor.other.tbsession.AnalyseTbSessionLog"%>
<%@page import="com.taobao.monitor.other.tbsession.TbSeesionLog"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>


<%

Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH,-1);

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


String ips = request.getParameter("ips");

ips="172.23.2.101,172.23.4.51,172.23.4.51";

if(ips==null){
	out.print("目标机器的IP");
	return ;
}

for(String ip:ips.split(",")){


AnalyseTbSessionLog log = new AnalyseTbSessionLog("/home/admin/logs/session.log."+sdf.format(cal.getTime()),ip);
log.doAnalyse();
Map<String,TbSeesionLog> map = log.getResult();
for(Map.Entry<String,TbSeesionLog> entry:map.entrySet()){
	TbSeesionLog l = entry.getValue();
}
%>
<%
final TbSeesionLog all = log.allSeesionLogMsg;


HostPo hostPo = CspCacheTBHostInfos.get().getHostInfoByIp(ip);


%>

<table style='font-size:14px;border-collapse:collapse;width:100%' border='1' cellspacing='0' bordercolor='#4f81bd'>
  <tr>
    <td colspan="3" align="center"><%=hostPo.getOpsName() %></td>
  </tr>
  <tr>
    <td colspan="3">读取记录数：<%=all.valueCount %></td>
  </tr>
  <tr>
    <td>平均请求cookie总长度</td>
    <td>最小长度</td>
    <td>最大长度</td>
  </tr>
  <tr>
    <td><%=Arith.div(all.valueSum,all.valueCount,2) %></td>
    <td><%=all.minValueLen %></td>
    <td><%=all.maxValueLen %></td>
  </tr>
</table>
<table style='font-size:14px;border-collapse:collapse;width:100%' border='1' cellspacing='0' bordercolor='#4f81bd'>
  <tr>
    <td colspan="5">key列表</td>
  </tr>
  <tr>
    <td>keyname</td>
    <td>出现比例</td>
    <td>平均占用比例</td>
    <td>平均长度</td>
    <td>最小长度</td>
    <td>最大长度</td>
  </tr>
  <%
  List<TbSeesionLog> list = new ArrayList<TbSeesionLog>();
  list.addAll(map.values());
  
  
  Collections.sort(list,new Comparator<TbSeesionLog>(){
	  public int compare(TbSeesionLog o1, TbSeesionLog o2){
		  if(Arith.div(o1.valueCount,all.valueCount) >Arith.div(o2.valueCount,all.valueCount)){
			  return -1;
		  }else if(Arith.div(o1.valueCount,all.valueCount) == Arith.div(o2.valueCount,all.valueCount)){
			  return 0;
		  }else{
			  return 1;
		  }
	  }
  });
  
  
  
  for(TbSeesionLog t:list){
	  double v= Arith.mul(Arith.div(t.valueCount,all.valueCount,4),100);
	  if(v <1){
		  continue;
	  }
	  
  %>
  <tr>
  	<td><%=t.keyName %></td>
  	<td><%=v%>%</td>
    <td><%=Arith.mul(Arith.div(t.perSum,t.valueCount,4),100) %>%</td>
    <td><%=Arith.div(t.valueSum,t.valueCount) %></td>
    <td><%=t.minValueLen %></td>
   <td><%=t.maxValueLen %></td>
  </tr>
  <%} %>
</table>
<%} %>
</body>
</html>