
package com.taobao.monitor.web.report;

import com.taobao.monitor.web.util.RequestByUrl;

/**
 * 
 * @author xiaodu
 * @version 2010-9-7 ����10:49:17
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
//	      out.write("<title>Ӧ�ü��ϵͳ-��ѯӦ��key</title>\r\n");
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
//	      out.write("\tfont-family: \"����\";\r\n");
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
//	      out.write("˵��:�����Զ�ѹ�����http_load������ʵ��,ѹ��ʱ��Ϊ60�룬����5���û����ϵ���30��Ϊֹ��<br>\r\n");
//	      out.write("��־����ֱ����Ŀ�������apache��־�л�ȡ10000����http statusΪ200��ѹ��˿�Ϊ7001ҳ���С��ѹ����<br>\r\n");
//	      out.write("GC��Ϣ������gc.log<br>\r\n");
//	      out.write("ƽ��ÿ���������� ��ÿ��PSYoungGen����/qps <br>\r\n");
//	      out.write("ѹ��ʱ��Ϊ:����7�� <br>\r\n");
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
//	      out.write("]</font><a href=\"http://cm.taobao.net:9999/monitorstat/load/app_loadrun_detail.jsp?appId="+appId+"&hostip="+host.getHostIp()+"\" target=\"_blank\">��ϸ</a>" +
//	      		"<a href=\"http://cm.taobao.net:9999/monitorstat/load/upload/apache_"+host.getHostIp()+"_"+collectTime+"\" target=\"_blank\"/>ѹ���ļ�����</a></td>\r\n");
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
//	      out.write("</td><td width=\"100\" align=\"center\">�ڴ�:</td><td>");
//	      out.write(memory);
//	      out.write("</td>\r\n");
//	      out.write("\t</tr>\t\r\n");
//	      out.write("</table>\r\n");
//	      out.write("<table width=\"100%\" border=\"1\" class=\"ui-widget ui-widget-content\"> \r\n");
//	      out.write("  <tr class=\"ui-widget-header \">\r\n");
//	      out.write("    <td width=\"100\" align=\"center\"></td>\r\n");
//	      out.write("    <td width=\"200\" align=\"center\">5�û�</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">10�û�</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">15�û�</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">20�û�</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">25�û�</td>\r\n");
//	      out.write("   <td width=\"200\" align=\"center\">30�û�</td>\r\n");
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
//	      out.write("    <td align=\"center\">ƽ����Ӧ(ms)</td>\r\n");
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
//	      out.write("    <td align=\"center\">ƽ��ҳ��(kb)</td>\r\n");
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
//	      out.write("    <td align=\"center\">GC��Ϣ</td>\r\n");
//	      out.write("     ");
//
//		  for(LoadRunResult load:loadList){
//		 
//	      out.write("\r\n");
//	      out.write("    <td>\r\n");
//	      out.write("    ");
//	   JvmMessage jvm = AnalyseLoadLog.analyseAverageGc(load.getGcDesc()); 
//	      out.write("\r\n");
//	      out.write("    \tƽ��ÿ��:");
//	      out.write(jvm.getGcNum() +"");
//	      out.write("��<br/>\r\n");
//	      out.write("    \tƽ��ÿ��:");
//	      out.write(jvm.getGcAverageTime() +"");
//	      out.write("ms\r\n");
//	      out.write("    </td>  \r\n");
//	      out.write("     ");
//	} 
//	      out.write("\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("  <tr>\r\n");
//	      out.write("    <td align=\"center\">FGC��Ϣ</td>\r\n");
//	      out.write("     ");
//
//		  for(LoadRunResult load:loadList){
//		 
//	      out.write("\r\n");
//	      out.write("    <td>");
//	   JvmMessage jvm = AnalyseLoadLog.analyseAverageGc(load.getGcDesc()); 
//	      out.write("\r\n");
//	      out.write("    \t�ܼ�:");
//	      out.write(jvm.getFgcNum() +"");
//	      out.write("��<br/>\r\n");
//	      out.write("    \tƽ��ÿ��:");
//	      out.write(jvm.getFgcAverageTime() +"");
//	      out.write("ms</td>  \r\n");
//	      out.write("     ");
//	} 
//	      out.write("\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("  <tr>\r\n");
//	      out.write("    <td align=\"center\">ƽ��ÿ�����������ڴ�</td>\r\n");
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
//	      out.write("   \t ƽ��ÿ����������:");
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
