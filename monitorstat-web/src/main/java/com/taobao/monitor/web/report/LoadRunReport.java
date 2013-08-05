
package com.taobao.monitor.web.report;

import com.taobao.monitor.web.util.RequestByUrl;

/**
 * 
 * @author xiaodu
 * @version 2010-9-7 上午10:49:17
 */
public class LoadRunReport {
	
//	public static String out(){
//		
//		
//		StringWriter out = new StringWriter();
//		out.write("\r\n");
//	      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("<html>\r\n");
//	      out.write("<head>\r\n");
//	      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\">\r\n");
//	      out.write("<META HTTP-EQUIV=\"pragma\" CONTENT=\"no-cache\">\r\n");
//	      out.write("<META HTTP-EQUIV=\"Cache-Control\" CONTENT=\"no-cache, must-revalidate\">\r\n");
//	      out.write("<title>应用监控系统-查询应用key</title>\r\n");
//	      out.write("\r\n");
//	      out.write("<style type=\"text/css\">\r\n");
//	      out.write("div {\r\n");
//	      out.write("\tfont-size: 12px;\r\n");
//	      out.write("}\r\n");
//	      out.write("\r\n");
//	      out.write("table {\r\n");
//	      out.write("\tmargin: 1em 0;\r\n");
//	      out.write("\tborder-collapse: collapse;\r\n");
//	      out.write("\twidth: 100%;\r\n");
//	      out.write("\tfont-size: 12px;\r\n");
//	      out.write("\tmargin: 0px 0;\r\n");
//	      out.write("}\r\n");
//	      out.write("\r\n");
//	      out.write("table td {\r\n");
//	      out.write("font-size: 12px;\r\n");
//	      out.write("}\r\n");
//	      out.write("\r\n");
//	      out.write(".ui-button {\r\n");
//	      out.write("\toutline: 0;\r\n");
//	      out.write("\tmargin: 0;\r\n");
//	      out.write("\tpadding: .4em 1em .5em;\r\n");
//	      out.write("\ttext-decoration: none; ! important;\r\n");
//	      out.write("\tcursor: pointer;\r\n");
//	      out.write("\tposition: relative;\r\n");
//	      out.write("\ttext-align: center;\r\n");
//	      out.write("}\r\n");
//	      out.write("\r\n");
//	      out.write(".ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {\r\n");
//	      out.write("\tpadding: .3em;\r\n");
//	      out.write("}\r\n");
//	      out.write("\r\n");
//	      out.write(".headcon {\r\n");
//	      out.write("\tfont-family: \"宋体\";\r\n");
//	      out.write("\tfont-size: 12px;\r\n");
//	      out.write("\tfont-weight: bold;\r\n");
//	      out.write("\ttext-indent: 3px;\r\n");
//	      out.write("\tborder-color: #316cb2;\r\n");
//	      out.write("\t/*text-transform: uppercase;*/\r\n");
//	      out.write("\tbackground: url(");
//	      out.write("http://cm.taobao.net:9999/monitorstat");
//	      out.write("/statics/images/4_17.gif);\r\n");
//	      out.write("}\r\n");
//	      out.write("img {\r\n");
//	      out.write("cursor:pointer;\r\n");
//	      out.write("}\r\n");
//	      out.write(".report_on{background:#bce774;}\r\n");
//	      out.write("\r\n");
//	      out.write("</style>\r\n");
//	      out.write("</head>\r\n");
//	     
//	      out.write("<body>\r\n");
//	      out.write("说明:线上自动压测采用http_load工具来实现,压测时间为60秒，利用5个用户线上递增30个为止，<br>\r\n");
//	      out.write("日志量将直接重目标机器的apache日志中获取10000条且http status为200。压测端口为7001页面大小无压缩。<br>\r\n");
//	      out.write("GC信息来自于gc.log<br>\r\n");
//	      out.write("平均每次请求消耗 ：每秒PSYoungGen回收/qps <br>\r\n");
//	      out.write("压测时间为:早上7点 <br>\r\n");
//
//	List<LoadRunHost> listLoadHost = MonitorLoadRunAo.get().findAllLoadRunHost();
//
//	for(LoadRunHost host:listLoadHost){
//
//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	String appId= host.getAppId()+"";
//	String collectTime = sdf.format(new Date());
//
//	List<LoadRunResult> loadList = MonitorLoadRunAo.get().findAppLoadRunResult(Integer.parseInt(appId),collectTime);
//
//	List<AppInfoPo> appList = MonitorTimeAo.get().findAllApp();
//
//	Map<Integer,String> mapApp = new HashMap<Integer,String>();
//	for(AppInfoPo po:appList){
//		mapApp.put(po.getAppId(),po.getAppName());
//	}
//
//
//	Collections.sort(loadList,new Comparator<LoadRunResult>(){
//		public  int compare(LoadRunResult o1, LoadRunResult o2){
//			
//			String s1 = o1.getShell();
//			String s2 = o2.getShell();
//			
//			String[] tmp1 = s1.split(" ");
//			String[] tmp2 = s2.split(" ");
//			int parallel1 = Integer.parseInt(tmp1[2]);
//			int parallel2 = Integer.parseInt(tmp2[2]);;
//			
//			if(parallel1>parallel2){
//				return 1;
//			}else if(parallel2>parallel1){
//				return -1;
//			}
//			return 0;
//		}
//	});
//
//
//	 out.write("\r\n");
//	      
//	      out.write("<table width=\"100%\" border=\"1\">\r\n");
//	      out.write("\t <tr>\r\n");
//	      out.write("\t \t<td colspan=\"4\" align=\"center\"><font size=\"5\">");
//	      out.write(mapApp.get(Integer.parseInt(appId)) );
//	      out.write('[');
//	      out.write(host.getHostIp());
//	      out.write("]</font><a href=\"http://cm.taobao.net:9999/monitorstat/load/app_loadrun_detail.jsp?appId="+appId+"&hostip="+host.getHostIp()+"\" target=\"_blank\">详细</a>" +
//	      		"<a href=\"http://cm.taobao.net:9999/monitorstat/load/upload/apache_"+host.getHostIp()+"_"+collectTime+"\" target=\"_blank\"/>压测文件下载</a></td>\r\n");
//	      out.write("  \t</tr>\r\n");
//	      out.write("  \t ");
//
//	  	 String cpu = "";
//	  	 String memory = "";
//		  for(LoadRunResult load:loadList){
//			  cpu = AnalyseLoadLog.analyseHostCpuInfo(load.getHostDesc());
//			  memory = AnalyseLoadLog.analyseHostMemoryInfo(load.getHostDesc());
//		  } 
//		 
//	      out.write("\t\r\n");
//	      out.write("\t<tr>\r\n");
//	      out.write("\t\t<td width=\"100\" align=\"center\">Cpu:</td><td width=\"400\">");
//	      out.write(cpu);
//	      out.write("</td><td width=\"100\" align=\"center\">内存:</td><td>");
//	      out.write(memory);
//	      out.write("</td>\r\n");
//	      out.write("\t</tr>\t\r\n");
//	      out.write("</table>\r\n");
//	      out.write("<table width=\"100%\" border=\"1\" class=\"ui-widget ui-widget-content\"> \r\n");
//	      out.write("  <tr class=\"ui-widget-header \">\r\n");
//	      out.write("    <td width=\"100\" align=\"center\"></td>\r\n");
//	      out.write("    <td width=\"200\" align=\"center\">5用户</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">10用户</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">15用户</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">20用户</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">25用户</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">30用户</td>\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write(" \r\n");
//	      out.write("  <tr>\r\n");
//	      out.write("    <td align=\"center\">QPS(p/s)</td>\r\n");
//	      out.write("     ");
//
//		  for(LoadRunResult load:loadList){
//		 
//	      out.write("\r\n");
//	      out.write("    <td>");
//	      out.write(AnalyseLoadLog.analyseAverageQps(load.getLoadRunDesc()) +"");
//	      out.write("</td>  \r\n");
//	      out.write("     ");
//	} 
//	      out.write("\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write(" \r\n");
//	      out.write("  <tr>\r\n");
//	      out.write("    <td align=\"center\">CPU(%)</td>\r\n");
//	      out.write("    ");
//
//		  for(LoadRunResult load:loadList){
//		
//	      out.write("\r\n");
//	      out.write("    <td>");
//	      out.write(AnalyseLoadLog.analyseAverageCpu(load.getCpuDesc()) +"");
//	      out.write("</td>\r\n");
//	      out.write("    ");
//	} 
//	      out.write("   \r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("  <tr>\r\n");
//	      out.write("    <td align=\"center\">平均响应(ms)</td>\r\n");
//	      out.write("     ");
//
//		  for(LoadRunResult load:loadList){
//		 
//	      out.write("\r\n");
//	      out.write("    <td>");
//	      out.write(AnalyseLoadLog.analyseAverageResT(load.getLoadRunDesc()) +"");
//	      out.write("</td>  \r\n");
//	      out.write("     ");
//	} 
//	      out.write("\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("  <tr>\r\n");
//	      out.write("    <td align=\"center\">平均页面(kb)</td>\r\n");
//	      out.write("     ");
//
//		  for(LoadRunResult load:loadList){
//		 
//	      out.write("\r\n");
//	      out.write("    <td>");
//	      out.write(AnalyseLoadLog.analyseAveragePageSize(load.getLoadRunDesc()) +"");
//	      out.write("</td>  \r\n");
//	      out.write("     ");
//	} 
//	      out.write("\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("   <tr>\r\n");
//	      out.write("    <td align=\"center\">GC信息</td>\r\n");
//	      out.write("     ");
//
//		  for(LoadRunResult load:loadList){
//		 
//	      out.write("\r\n");
//	      out.write("    <td>\r\n");
//	      out.write("    ");
//	   JvmMessage jvm = AnalyseLoadLog.analyseAverageGc(load.getGcDesc()); 
//	      out.write("\r\n");
//	      out.write("    \t平均每秒:");
//	      out.write(jvm.getGcNum() +"");
//	      out.write("次<br/>\r\n");
//	      out.write("    \t平均每次:");
//	      out.write(jvm.getGcAverageTime() +"");
//	      out.write("ms\r\n");
//	      out.write("    </td>  \r\n");
//	      out.write("     ");
//	} 
//	      out.write("\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("  <tr>\r\n");
//	      out.write("    <td align=\"center\">FGC信息</td>\r\n");
//	      out.write("     ");
//
//		  for(LoadRunResult load:loadList){
//		 
//	      out.write("\r\n");
//	      out.write("    <td>");
//	   JvmMessage jvm = AnalyseLoadLog.analyseAverageGc(load.getGcDesc()); 
//	      out.write("\r\n");
//	      out.write("    \t总计:");
//	      out.write(jvm.getFgcNum() +"");
//	      out.write("次<br/>\r\n");
//	      out.write("    \t平均每次:");
//	      out.write(jvm.getFgcAverageTime() +"");
//	      out.write("ms</td>  \r\n");
//	      out.write("     ");
//	} 
//	      out.write("\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("  <tr>\r\n");
//	      out.write("    <td align=\"center\">平均每次请求消耗内存</td>\r\n");
//	      out.write("     ");
//
//		  for(LoadRunResult load:loadList){
//		 
//	      out.write("\r\n");
//	      out.write("    <td>");
//
//	    double qps= AnalyseLoadLog.analyseAverageQps(load.getLoadRunDesc());
//	    JvmMessage jvm =  AnalyseLoadLog.analyseAverageGc(load.getGcDesc()) ;    
//	    
//	      out.write("\r\n");
//	      out.write("   \t 平均每次请求消耗:");
//	      out.write(Arith.div(jvm.getGcUseMemory(),qps,2)+"");
//	      out.write("k\r\n");
//	      out.write("    </td>  \r\n");
//	      out.write("     ");
//	} 
//	      out.write("\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("</table>\r\n");
//	} 
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("</body>\r\n");
//	      out.write("</html>");
//		
//		
//		return out.toString();
//	}
	
	
	
	public static  String getLoadREportByJsp(String appIds){		
		String url = "http://127.0.0.1:9999//monitorstat/report/report_loadrun_detail.jsp?appIds="+appIds;		
		return RequestByUrl.getMessageByJsp(url);
	}
	

}
