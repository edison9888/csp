
package com.taobao.monitor.web.report;

import com.taobao.monitor.web.util.RequestByUrl;

/**
 * 
 * @author xiaodu
 * @version 2010-9-6 下午02:39:22
 */
public class JprofReport {
	
	
//	public static String out(String appName,String collectDay){
//		
//		StringWriter out = new StringWriter();
//		
//		 out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
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
//	      out.write("<html>\r\n");
//	      out.write("<head>\r\n");
//	      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">\r\n");
//	      out.write("<META HTTP-EQUIV=\"pragma\" CONTENT=\"no-cache\">\r\n");
//	      out.write("<META HTTP-EQUIV=\"Cache-Control\" CONTENT=\"no-cache, must-revalidate\">\r\n");
//	      out.write("<title>监控</title>\r\n");
//	      out.write("<style>\r\n");
//	      out.write(".report_on{background:#bce774;}\r\n");
//	      out.write("</style>\r\n");
//	      out.write("<link type=\"text/css\" href=\"");
//	      out.write("http://cm.taobao.net:9999/" );
//	      out.write("/statics/css/themes/base/ui.all.css\" rel=\"stylesheet\" />\r\n");
//	      out.write("<script type=\"text/javascript\"\tsrc=\"");
//	      out.write("http://cm.taobao.net:9999/" );
//	      out.write("/statics/js/jquery.min.js\"></script>\r\n");
//	      out.write("<script type=\"text/javascript\"\tsrc=\"");
//	      out.write("http://cm.taobao.net:9999/" );
//	      out.write("/statics/js/ui/ui.core.js\"></script>\r\n");
//	      out.write("<script type=\"text/javascript\" src=\"");
//	      out.write("http://cm.taobao.net:9999/" );
//	      out.write("/statics/js/ui/ui.draggable.js\"></script>\r\n");
//	      out.write("<script type=\"text/javascript\" src=\"");
//	      out.write("http://cm.taobao.net:9999/" );
//	      out.write("/statics/js/ui/ui.resizable.js\"></script>\r\n");
//	      out.write("<script type=\"text/javascript\" src=\"");
//	      out.write("http://cm.taobao.net:9999/" );
//	      out.write("/statics/js/ui/ui.dialog.js\"></script>\r\n");
//	      out.write("<script type=\"text/javascript\" src=\"");
//	      out.write("http://cm.taobao.net:9999/" );
//	      out.write("/statics/js/ui/ui.accordion.js\"></script>\r\n");
//	      out.write("<script type=\"text/javascript\" src=\"");
//	      out.write("http://cm.taobao.net:9999/" );
//	      out.write("/statics/js/jquery.bgiframe.js\"></script>\r\n");
//	      out.write("<style type=\"text/css\">\r\n");
//	      out.write("div {\r\n");
//	      out.write("\tfont-size: 12px;\r\n");
//	      out.write("}\r\n");
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
//	      out.write("http://cm.taobao.net:9999/");
//	      out.write("/statics/images/4_17.gif);\r\n");
//	      out.write("}\r\n");
//	      out.write("img {\r\n");
//	      out.write("cursor:pointer;\r\n");
//	      out.write("border:0;\r\n");
//	      out.write("}\r\n");
//	      out.write(".body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}\r\n");
//	      out.write("</style>  \r\n");
//	      out.write("  \r\n");
//	      out.write("  \r\n");
//	      out.write("  \r\n");
//	      out.write("\r\n");
//	      out.write("</head>\r\n");
//
//
//	String orderType = "1";
//
//	
//	String classNamelike="com/";
//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//	if(collectDay==null){
//		collectDay = sdf.format(new Date());
//	}
//
//
//	      out.write("\r\n");
//	      out.write("<body class=\"example\"> \r\n");
//	      out.write("  ");
//
//	  
//	  if(appName != null&&collectDay != null){
//	  
//	  List<JprofClassMethod> listClass = MonitorJprofAo.get().findJprofClassMethod(appName,collectDay);
//	  
//	  if("1".equals(orderType)){	  
//		  Collections.sort(listClass,new Comparator<JprofClassMethod>(){
//			  public int compare(JprofClassMethod o1, JprofClassMethod o2){
//				  long e1 = o1.getExcuteNum();
//				  double t1 = o1.getUseTime();
//				  
//				  long e2 = o2.getExcuteNum();
//				  double t2 = o2.getUseTime();
//				  
//				  if(t1/e1 >t2/e2){
//					  return -1;
//				  }else if(t1/e1 == t2/e2){
//					  return 0;
//				  }			  
//				  return 1;
//			  }
//		  });	  
//	  }
//	  
//	  
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("<div class=\"ui-dialog ui-widget ui-widget-content ui-corner-all \" style=\"width: 100%;\">\r\n");
//	      out.write("<div class=\"ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix\"><font color=\"#FF0000\" >"+appName+" 执行时间前30名  <a href=\"http://cm.taobao.net:9999/monitorstat/jprof/manage_jprof_class_method.jsp?appName=");
//	      out.write(appName );
//	      out.write("&collectDay=");
//	      out.write(collectDay );
//	      out.write("\" target=\"_blank\">查看详细</a></font>&nbsp;&nbsp; </div>\r\n");
//	      out.write("<div id=\"dialog\" class=\"ui-dialog-content ui-widget-content\">\r\n");
//	      out.write("<table width=\"100%\" border=\"1\" class=\"ui-widget ui-widget-content\">\t\r\n");
//	      out.write("\t<tr class=\"headcon \">\r\n");
//	      out.write("\t\t<td >类信息</td>\t\r\n");
//	      out.write("\t\t<td >平均时间</td>\t\t\r\n");
//	      out.write("\t\t<td >总次数</td>\r\n");
//	      out.write("\t\t<td >总时间</td>\r\n");
//	      out.write("\t\t\r\n");
//	      out.write("\t</tr>\r\n");
//	      out.write("\t");
//
//		
//		for(int i=0;i<listClass.size()&&i<30;i++){
//			JprofClassMethod m = listClass.get(i);
//			
//	      out.write("\r\n");
//	      out.write("\t\t<tr >\r\n");
//	      out.write("\t\t\t<td >");
//	      out.write(m.getClassName()+":"+m.getMethodName()+":"+m.getLineNum() );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t<td >");
//	      out.write(Arith.div(m.getUseTime(),m.getExcuteNum(),2)+"");
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t<td >");
//	      out.write(m.getExcuteNum() +"");
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t<td >");
//	      out.write(m.getUseTime() +"");
//	      out.write("</td>\t\t\t\r\n");
//	      out.write("\t\t</tr>\r\n");
//	      out.write("\t\t");
//		
//			} 
//		
//	      out.write("\r\n");
//	      out.write("</table>\r\n");
//	      out.write("</div>\r\n");
//	      out.write("</div>  \r\n");
//	      out.write("  ");
//	} 
//	      out.write("\r\n");
//	      out.write("  \r\n");
//	      out.write("</body>\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("</html>");
//	      return out.toString();
//	}
	
	
	public static  String getJprofReportByJsp(String appNames,String collectDay){		
		String url = "http://127.0.0.1:9999/monitorstat/report/report_jprof_class_method.jsp?appNames="+appNames+"&collectDay="+collectDay;		
		return RequestByUrl.getMessageByJsp(url);
	}

}
