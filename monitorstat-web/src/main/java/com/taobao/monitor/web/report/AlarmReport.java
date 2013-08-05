
package com.taobao.monitor.web.report;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.web.ao.MonitorTimeAo;
import com.taobao.monitor.web.util.RequestByUrl;
import com.taobao.monitor.web.vo.AlarmRecordPo;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * 
 * @author xiaodu
 * @version 2010-7-29 下午04:57:03
 */
public class AlarmReport {
	
//	
//	public static String out(String appNames,String currentDay) throws ParseException{
//		
//		StringWriter out = new StringWriter();
//		
//		 out.write("\r\n");
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
//	      out.write("<html>\r\n");
//	      out.write("<head>\r\n");
//	      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\">\r\n");
//	      out.write("<META HTTP-EQUIV=\"pragma\" CONTENT=\"no-cache\">\r\n");
//	      out.write("<META HTTP-EQUIV=\"Cache-Control\" CONTENT=\"no-cache, must-revalidate\">\r\n");
//	      out.write("<title>应用监控系统-告警历史记录</title>\r\n");
//	      out.write("<link type=\"text/css\" href=\"http://cm.taobao.net:9999/monitorstat/statics/css/themes/base/ui.all.css\" rel=\"stylesheet\" />\r\n");
//	      out.write("<style type=\"text/css\">\r\n");
//	      out.write("div {\r\n");
//	      out.write("\tfont-size: 12px;\r\n");
//	      out.write("}\r\n");
//	      out.write("\r\n");
//	      out.write("table {\r\n");
//	      out.write("\tmargin: 1em 1em 1em 1em;\r\n");
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
//	      out.write("img {\r\n");
//	      out.write("cursor:pointer;\r\n");
//	      out.write("}\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("</style>\r\n");
//	      out.write("</head>\r\n");
//	      out.write("<body>\r\n");
//
//
//	String[] appNameArray = appNames.split(",");
//	List<AppInfoPo> appIds = new ArrayList<AppInfoPo>();
//
//	List<AppInfoPo> listApp = MonitorTimeAo.get().findAllApp();
//
//
//	for(AppInfoPo appInfoPo:listApp){
//		String appName = appInfoPo.getAppName();
//		String appId = appInfoPo.getAppId()+"";
//		for(String app:appNameArray){
//			if(app.equals(appName)||app.equals(appId)){
//				appIds.add(appInfoPo);
//				break;
//			}
//		}
//	}
//
//
//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	if(currentDay==null){
//		currentDay = sdf.format(new Date());
//	}
//
//
//	      out.write("\r\n");
//	      out.write("\r\n");
//
//	for(AppInfoPo appid:appIds) {
//		long num = MonitorTimeAo.get().countAlarmKeyNum(appid.getAppName());
//
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("<div class=\"ui-dialog ui-widget ui-widget-content ui-corner-all \" style=\"width: 100%;\">\r\n");
//	      out.write("<div class=\"ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix\"><font color=\"#FF0000\" > ");
//	      out.write(appid.getAppName() );
//	      out.write("(设置告警key数量:");
//	      out.write(num +"");
//	      out.write(")</font></div>\r\n");
//	      out.write("<div id=\"dialog\" class=\"ui-dialog-content ui-widget-content\">\r\n");
//	      out.write("\r\n");
//	      out.write("<table width=\"100%\" border=\"1\" width=\"100%\" class=\"ui-widget ui-widget-content\" style=\"font-size: 12px;\">   \r\n");
//	      out.write("  ");
//	if(num>0){ 
//	      out.write(" \r\n");
//	      out.write("  <tr class=\"ui-widget-header \">\r\n");
//	      out.write("    <td width=\"200\" align=\"center\">出现告警key名称</td>\r\n");
//	      out.write("    <td width=\"400\" align=\"center\">告警分布</td>\r\n");
//	      out.write("    <td width=\"200\" align=\"center\">次数汇总</td>    \r\n");
//	      out.write("    <td width=\"200\" align=\"center\">查看历史告警</td>\r\n");
//	      out.write("  </tr>\r\n");
//	      out.write("  ");
//
//	  List<AlarmRecordPo> list = MonitorTimeAo.get().getAppAlarmCountByDate(appid.getAppId(),sdf.parse(currentDay));
//	  if(list.size()>0){
//		  for(AlarmRecordPo po:list){
//			  int sum = 0;
//		  
//	      out.write("\r\n");
//	      out.write("\t\t<tr>\r\n");
//	      out.write("\t    \t<td align=\"center\">");
//	      out.write(po.getAlarmKeyName() );
//	      out.write("</td>\r\n");
//	      out.write("\t    \t<td align=\"center\">\r\n");
//	      out.write("\t    \t\t");
//
//		    		Map<String,Integer> siteMap = po.getSiteMap();
//		    		for(Map.Entry<String,Integer> siteEntry:siteMap.entrySet()){
//		    			sum+=siteEntry.getValue();
//		    			
//	      out.write("\r\n");
//	      out.write("\t    \t\t\t");
//	      out.write(siteEntry.getKey() );
//	      out.write(":<font color=\"red\">");
//	      out.write(siteEntry.getValue().toString() );
//	      out.write("</font>次<br/>\r\n");
//	      out.write("\t    \t\t\t");
//
//		    		}
//		    		
//	      out.write("\r\n");
//	      out.write("\t\t\t</td>\r\n");
//	      out.write("\t\t\t<td align=\"center\">\r\n");
//	      out.write("\t    \t\t");
//	      out.write(sum +"");
//	      out.write("\r\n");
//	      out.write("\t\t\t</td>\r\n");
//	      out.write("\t   \t \t<td align=\"center\"><a href=\"http://cm.taobao.net:9999/monitorstat/alarm/alarm_record_flash.jsp?appId=");
//	      out.write(appid.getAppId()+"");
//	      out.write("&keyId=");
//	      out.write(po.getAlarmkeyId()+"");
//	      out.write("\" target=\"_blank\">查看历史告警</a>&nbsp;&nbsp;<a href=\"http://cm.taobao.net:9999/monitorstat/alarm/alarm_record_flash_pie.jsp?appId=");
//	      out.write(appid.getAppId()+"");
//	      out.write("\" target=\"_blank\">查看告警分布</a></td>\r\n");
//	      out.write("\t  \t</tr>\t\r\n");
//	      out.write("\t  ");
//
//		  }
//	   }else{
//		
//	      out.write("\r\n");
//	      out.write("   <tr>\r\n");
//	      out.write("    <td colspan=\"4\" align=\"center\">相安无事。<a href=\"http://cm.taobao.net:9999/monitorstat/alarm/manage_key.jsp?appId=");
//	      out.write(appid.getAppId()+"");
//	      out.write("\" target=\"_blank\" style=\"color:green\">点击这里添加更多的告警点</a></td>\r\n");
//	      out.write("   </tr> \r\n");
//	      out.write("\t\r\n");
//	      out.write("  ");
//		  
//	  }}else{
//	  
//	      out.write("\r\n");
//	      out.write("     <tr>\r\n");
//	      out.write("    <td colspan=\"4\" align=\"center\">应用还没有设置告警,<a href=\"http://cm.taobao.net:9999/monitorstat/alarm/manage_key.jsp?appId=");
//	      out.write(appid.getAppId()+"");
//	      out.write("\" target=\"_blank\" style=\"color:green\">点击这里添加更多的告警点</a></td>\r\n");
//	      out.write("   </tr> \r\n");
//	      out.write("  ");
//
//	  }
//	  
//	      out.write("  \r\n");
//	      out.write("</table>\r\n");
//	      out.write("</div>\r\n");
//	      out.write("</div>\r\n");
//	} 
//	      out.write("\r\n");
//	      out.write("</body>\r\n");
//	      out.write("</html>");
//		return out.toString();
//	}
	
	
	public static  String getAlarmReportByJsp(String appNames,String currentDay){		
		String url = "http://127.0.0.1:9999//monitorstat/report/report_alarm.jsp?appNames="+appNames+"&currentDay="+currentDay;		
		return RequestByUrl.getMessageByJsp(url);
	}

}
