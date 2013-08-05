
package com.taobao.monitor.web.report;

import com.taobao.monitor.web.util.RequestByUrl;


/**
 * 
 * @author xiaodu
 * @version 2010-4-9 上午10:45:45
 */
public class DayReport {
	
//	private static  Logger log = Logger.getLogger(DayReport.class);
//	
//	public static String outTables(String searchDate) throws Exception{
//		StringWriter out = new StringWriter();
//		 out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
//	      out.write("<html>\r\n");
//	      out.write("<head>\r\n");
//	      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">\r\n");
//	      out.write("<META HTTP-EQUIV=\"pragma\" CONTENT=\"cache\">\r\n");
//	      out.write("<META HTTP-EQUIV=\"Cache-Control\" CONTENT=\"cache, must-revalidate\">\r\n");
//	      out.write("<title>监控</title>\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("<style type=\"text/css\">\r\n");
//	      out.write("body {\r\n");
//	      out.write("\tfont-size: 62.5%;\r\n");
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
//	      out.write("/statics/images/4_17.gif);\r\n");
//	      out.write("}\r\n");
//	      out.write("img {\r\n");
//	      out.write("cursor:pointer;\r\n");
//	      out.write("}\r\n");
//	      out.write("</style>\r\n");
//	      out.write("\r\n");
//	      out.write("</head>\r\n");
//	      out.write("<body>\r\n");
//	      out.write("\r\n");
//	      out.write("<div>\r\n");
//
//
//	List<AppInfoPo> appList = MonitorAo.get().findAllApp();
//
//
//
//
//	      out.write("\r\n");
//	      out.write("\r\n");
//	//yyyy-MM-dd
//	if(searchDate==null){
//		searchDate = Utlitites.getMonitorDate();
//	}
//	String tongbiDate = Utlitites.getTongBiMonitorDate(searchDate);
//	String huanbiDate = Utlitites.getHuanBiMonitorDate(searchDate);
//	Map<String, MonitorVo> map = MonitorAo.get().findMonitorCountMapByDate(searchDate);
//	Map<String, MonitorVo> tongqiMap = MonitorAo.get().findMonitorCountMapByDate(tongbiDate);
//	Map<String, MonitorVo> huanqiMap = MonitorAo.get().findMonitorCountMapByDate(huanbiDate);
//	Map<Integer,Map<Integer,KeyValueBaseLinePo>> baseMap = MonitorAo.get().findMonitorBaseLine(null,searchDate);
//
//	      out.write('\r');
//	      out.write('\n');
//
//	List<String> appNameList = new ArrayList<String>();
//	appNameList.add("list");
//	appNameList.add("shopsystem");
//	appNameList.add("item");
//	appNameList.add("buy");
//	appNameList.add("trademgr");
//	appNameList.add("ic");
//	appNameList.add("tbuic");
//	appNameList.add("shopcenter");
//	appNameList.add("tc");	
//		
//	      out.write("\r\n");
//	      out.write("<div id=\"everyReportCountSummary\" style=\"text-align:left\">\r\n");
//	      out.write("\t      <table border=\"1\" width=\"100%\" class=\"ui-widget ui-widget-content\">\r\n");
//	      out.write("        \t<tr class=\"ui-widget-header \">        \t\t\r\n");
//	      out.write("                <th align=\"center\" >List(PV)</th>\r\n");
//	      out.write("                <th align=\"center\">ShopSystem(PV)</th>\r\n");
//	      out.write("                <th align=\"center\">Detail(PV)</th>\r\n");
//	      out.write("                <th align=\"center\">Buy(PV)</th>\r\n");
//	      out.write("                <th align=\"center\">Tm(PV)</th>\r\n");
//	      out.write("                <th align=\"center\">加入购物车次数</th>\r\n");
//	      out.write("                <th align=\"center\">立即购买次数</th>\r\n");
//	      out.write("                <th align=\"center\">立即购买Exception</th>\r\n");
//	      out.write("                <th align=\"center\">创建订单次数</th>\r\n");
//	      out.write("                <th align=\"center\">订单付款</th>\r\n");
//	      out.write("                <th align=\"center\">支付总额</th>\r\n");
//	      out.write("            </tr>\r\n");
//	      out.write("            <tr>            \t\r\n");
//	      out.write("                <td align=\"left\" class=\"alt\">");
//	      out.write(map.get("list")==null?" - ":Utlitites.fromatLong(map.get("list").getPv()) );
//	      out.write("</td>\r\n");
//	      out.write("                <td align=\"left\" class=\"alt\">");
//	      out.write(map.get("shopsystem")==null?" - ":Utlitites.fromatLong(map.get("shopsystem").getPv()) );
//	      out.write("</td>\r\n");
//	      out.write("                <td align=\"left\" class=\"alt\">");
//	      out.write(map.get("item")==null?" - ":Utlitites.fromatLong(map.get("item").getPv()) );
//	      out.write("</td>\r\n");
//	      out.write("                <td align=\"left\" class=\"alt\">");
//	      out.write(map.get("buy")==null?" - ":Utlitites.fromatLong(map.get("buy").getPv()) );
//	      out.write("</td>\r\n");
//	      out.write("                <td align=\"left\" class=\"alt\">");
//	      out.write(map.get("trademgr")==null?" - ":Utlitites.fromatLong(map.get("trademgr").getPv()) );
//	      out.write("</td>\r\n");
//	      out.write("                <td align=\"left\"  class=\"alt\">");
//	      out.write(map.get("buy")==null?" - ":Utlitites.fromatLong(map.get("buy").getShoppingCart()) );
//	      out.write("</td>\r\n");
//	      out.write("                  <td align=\"left\"  class=\"alt\">");
//	      out.write(map.get("buy")==null?" - ":Utlitites.fromatLong(map.get("buy").getSubmitBuy()) );
//	      out.write("</td>\r\n");
//	      out.write("                  <td align=\"left\" class=\"alt\">");
//	      out.write(map.get("buy")==null?" - ":Utlitites.fromatLong(map.get("buy").getSubmitBuyExc()) );
//	      out.write("</td>\r\n");
//	      out.write("                  <td align=\"left\"  class=\"alt\">");
//	      out.write(map.get("buy")==null?" - ":Utlitites.fromatLong(map.get("buy").getCreateOrderCount()) );
//	      out.write("</td>\r\n");
//	      out.write("                  <td align=\"left\" class=\"alt\">");
//	      out.write(map.get("buy")==null?" - ":Utlitites.fromatLong(map.get("buy").getPayOrderCount()) );
//	      out.write("</td>\r\n");
//	      out.write("                  <td align=\"left\"  class=\"alt\">");
//	      out.write(map.get("buy")==null?" - ":Utlitites.fromatLong(map.get("buy").getAmountAll()));
//	      out.write("</td>\r\n");
//	      out.write("            </tr>                    \r\n");
//	      out.write("         </table>\r\n");
//	      out.write("    </div>\r\n");
//	      out.write("\r\n");
//
//				
//		for(AppInfoPo appPo:appList){
//			String appName = appPo.getAppName();
//			
//			if(!appNameList.contains(appName)){
//				continue;
//			}
//			
//			MonitorVo vo = map.get(appName);
//			MonitorVo tongqiVo = tongqiMap.get(appName);
//			MonitorVo huanbiVo = huanqiMap.get(appName);
//			
//			Map<Integer,KeyValueBaseLinePo> appBaseMap = baseMap.get(appPo.getAppId());
//			
//			if(appBaseMap==null){
//				appBaseMap = new HashMap<Integer,KeyValueBaseLinePo>();
//			}
//			
//			if(vo==null){
//				vo = new MonitorVo();
//			}
//			if(tongqiVo==null){
//				tongqiVo = new MonitorVo();
//			}
//			if(huanbiVo==null){
//				huanbiVo = new MonitorVo();
//			}
//
//
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("<div class=\"ui-dialog ui-widget ui-widget-content ui-corner-all \" style=\"width: 100%;\">\r\n");
//	      out.write("<div class=\"ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix\"><font color=\"#FF0000\" > = ");
//	      out.write(appName );
//	      out.write(" = [");
//	      out.write(vo.getMachines()==null?" - ":vo.getMachines());
//	      out.write("]</font>&nbsp;&nbsp; <a href=\"http://cm.taobao.net:9999/monitorstat/app_detail.jsp?searchDate=");
//	      out.write(searchDate);
//	      out.write("&selectAppName=");
//	      out.write(appName );
//	      out.write("\">详细</a></div>\r\n");
//	      out.write("<div id=\"dialog\" class=\"ui-dialog-content ui-widget-content\">\r\n");
//	      out.write("<table width=\"100%\">\r\n");
//	      out.write("\t<tr class=\"headcon \">\r\n");
//	      out.write("\t\t<td colspan=\"2\" align=\"left\"><font color=\"#000000\" size=\"2\">性能数据</font></td>\r\n");
//	      out.write("\t</tr>\r\n");
//	      out.write("\t<tr>\r\n");
//	      out.write("\t\t<td>\r\n");
//	      out.write("\t\t<table width=\"400\" border=\"1\" class=\"ui-widget ui-widget-content\">\r\n");
//	      out.write("\t\t\t<tr class=\"ui-widget-header \">\r\n");
//	      out.write("\t\t\t\t<td width=\"100\" align=\"center\">系统数据</td>\r\n");
//	      out.write("\t\t\t\t<td width=\"90\" align=\"center\">当前</td>\r\n");
//	      out.write("\t\t\t\t<td width=\"70\" align=\"center\">同比</td>\r\n");
//	      out.write("\t\t\t\t<td width=\"70\" align=\"center\">环比</td>\r\n");
//	      out.write("\t\t\t\t<td width=\"70\" align=\"center\">基线</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>CPU</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(vo.getCpu()==null?" - ":vo.getCpu() );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getCpu(),tongqiVo.getCpu()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getCpu(),huanbiVo.getCpu()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getCpu(),getBaseValue(vo.getCpuKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>IOWAIT</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(vo.getIowait()==null?" - ":vo.getIowait() );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getIowait(),tongqiVo.getIowait()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getIowait(),huanbiVo.getIowait()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getIowait(),getBaseValue(vo.getIowaitKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>Load</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(vo.getLoad()==null?" - ":vo.getLoad() );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getLoad(),tongqiVo.getLoad()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getLoad(),huanbiVo.getLoad()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getLoad(),getBaseValue(vo.getLoadKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>Men</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(vo.getMen()==null?" - ":vo.getMen() );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getMen(),tongqiVo.getMen()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getMen(),huanbiVo.getMen()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getMen(),getBaseValue(vo.getMenKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>Swap</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(vo.getSwap()==null?" - ":vo.getSwap() );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getSwap(),tongqiVo.getSwap()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getSwap(),huanbiVo.getSwap()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getSwap(),getBaseValue(vo.getSwapKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>GC次数</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.formatNull(vo.getGcpo().getGcCount()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getGcpo().getGcCount(),tongqiVo.getGcpo().getGcCount()));
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getGcpo().getGcCount(),huanbiVo.getGcpo().getGcCount()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getGcpo().getGcCount(),getBaseValue(vo.getGcpo().getAveMachinekeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>GC时间</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.formatNull(vo.getGcpo().getGcAverage()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getGcpo().getGcAverage(),tongqiVo.getGcpo().getGcAverage()));
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getGcpo().getGcAverage(),huanbiVo.getGcpo().getGcAverage()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getGcpo().getGcAverage(),getBaseValue(vo.getGcpo().getAveuserTimeKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>FullGc次数</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.formatNull(vo.getFullGcpo().getGcCount()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getFullGcpo().getGcCount(),tongqiVo.getFullGcpo().getGcCount()));
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getFullGcpo().getGcCount(),huanbiVo.getFullGcpo().getGcCount()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getFullGcpo().getGcCount(),getBaseValue(vo.getFullGcpo().getAveMachinekeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>FullGC时间</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.formatNull(vo.getFullGcpo().getGcAverage()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getFullGcpo().getGcAverage(),tongqiVo.getFullGcpo().getGcAverage()));
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getFullGcpo().getGcAverage(),huanbiVo.getFullGcpo().getGcAverage()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getFullGcpo().getGcAverage(),getBaseValue(vo.getFullGcpo().getAveuserTimeKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t</table>\r\n");
//	      out.write("\t\t</td>\r\n");
//	      out.write("\t\t<td valign=\"top\">\r\n");
//	      out.write("\t\t<table width=\"400\" border=\"1\" class=\"ui-widget ui-widget-content\">\r\n");
//	      out.write("\t\t\t<tr class=\"ui-widget-header \">\r\n");
//	      out.write("\t\t\t\t<td width=\"100\" align=\"center\">访问信息</td>\r\n");
//	      out.write("\t\t\t\t<td width=\"90\" align=\"center\">当前</td>\r\n");
//	      out.write("\t\t\t\t<td width=\"70\" align=\"center\">同比</td>\r\n");
//	      out.write("\t\t\t\t<td width=\"70\" align=\"center\">环比</td>\r\n");
//	      out.write("\t\t\t\t<td width=\"70\" align=\"center\">基线</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t");
//	if("ic".equals(appName)||"tbuic".equals(appName)||"shopcenter".equals(appName)||"tc".equals(appName)){ 
//					int keyId = 0;
//					int rt = 0;
//					if("ic".equals(appName)){//2342
//						keyId = 2328;
//						rt = 2342;
//					}
//					if("shopcenter".equals(appName)){//2621
//						keyId = 2753;
//						rt = 2621;
//						
//					}
//					if("tbuic".equals(appName)){//3192
//						keyId = 3242;
//						rt = 3192;
//					}
//					if("tc".equals(appName)){//1525
//						keyId = 1526;
//						rt = 1525;
//					}
//					
//					String machines = vo.getMachines();
//					int mNumber = 0;
//					if(machines!=null){
//						String[] ms =  machines.split("/");					
//						for(String m:ms){
//							String[] h = m.split(":");
//							if(h.length==2){
//								mNumber+=Integer.parseInt(h[1]);
//							}
//						}
//					}
//					if(mNumber==0){
//						mNumber = 1;
//					}
//					
//					String qps = MonitorAo.get().parseCenterAppQps(appPo.getAppId(),keyId,searchDate);
//					if(qps!=null){
//						qps = Arith.div(Double.parseDouble(qps),60*mNumber,2)+"";
//					}
//					String rt_v = MonitorAo.get().parseCenterAppQps(appPo.getAppId(),rt,searchDate);
//					KeyValuePo pv = MonitorAo.get().findKeyValueFromCountByDate(keyId+"",appPo.getAppName(),searchDate);
//					if(pv==null){
//						pv = new KeyValuePo();
//					}
//					String tongbiqps = MonitorAo.get().parseCenterAppQps(appPo.getAppId(),keyId,tongbiDate);
//					if(tongbiqps!=null){
//						tongbiqps = Arith.div(Double.parseDouble(tongbiqps),60*mNumber,2)+"";
//					}
//					String tongbirt_v = MonitorAo.get().parseCenterAppQps(appPo.getAppId(),rt,tongbiDate);
//					KeyValuePo tongbipv = MonitorAo.get().findKeyValueFromCountByDate(keyId+"",appPo.getAppName(),tongbiDate);
//					if(tongbipv==null){
//						tongbipv = new KeyValuePo();
//					}
//					
//					String huanbiqps = MonitorAo.get().parseCenterAppQps(appPo.getAppId(),keyId,huanbiDate);
//					if(huanbiqps!=null){
//						huanbiqps = Arith.div(Double.parseDouble(huanbiqps),60*mNumber,2)+"";
//					}
//					String huanbirt_v = MonitorAo.get().parseCenterAppQps(appPo.getAppId(),rt,huanbiDate);
//					KeyValuePo huanbipv = MonitorAo.get().findKeyValueFromCountByDate(keyId+"",appPo.getAppName(),huanbiDate);
//					if(huanbipv==null){
//						huanbipv = new KeyValuePo();
//					}
//					
//				
//	      out.write("\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>PV</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.fromatLong(pv.getValueStr()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(pv.getValueStr(),tongbipv.getValueStr()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(pv.getValueStr(),huanbipv.getValueStr()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>-</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>Qps</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.formatNull(qps) );
//	      out.write("(高峰期 平均到"+mNumber+"机器)</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(qps,tongbiqps) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(qps,huanbiqps) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>-</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>Rt</td>\r\n");
//	      out.write("\t\t\t\t<td>"+Utlitites.formatNull(rt_v)+"</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(rt_v,tongbirt_v) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(rt_v,huanbirt_v) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>-</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t");
//	}else{ 
//	      out.write("\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>PV</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.fromatLong(vo.getPv()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getPv(),tongqiVo.getPv()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getPv(),huanbiVo.getPv()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getPv(),getBaseValue(vo.getPvKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>Qps</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.formatNull(vo.getQpsNum()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getQpsNum(),tongqiVo.getQpsNum()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getQpsNum(),huanbiVo.getQpsNum()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getQpsNum(),getBaseValue(vo.getQpsKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t<tr>\r\n");
//	      out.write("\t\t\t\t<td>Rt</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.formatNull(vo.getRtNum()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getRtNum(),tongqiVo.getRtNum()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getRtNum(),huanbiVo.getRtNum()) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t\t<td>");
//	      out.write(Utlitites.scale(vo.getRtNum(),getBaseValue(vo.getRtKeyId(),appBaseMap)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t\t</tr>\r\n");
//	      out.write("\t\t\t");
//	} 
//	      out.write("\r\n");
//	      out.write("\t\t</table>\r\n");
//	      out.write("\r\n");
//	      out.write("\t\t</td>\r\n");
//	      out.write("\t</tr>\r\n");
//	      out.write("</table>\r\n");
//
//	Map<String, String> alarmMap = vo.getAlarmMap();
//	Map<String, String> tongbiAlarmMap = tongqiVo.getAlarmMap();
//	Map<String, String> huanbiAlarmMap = huanbiVo.getAlarmMap();
//
//	Set<String> alarmNameSet = new HashSet<String>();
//	alarmNameSet.addAll(alarmMap.keySet());
//	alarmNameSet.addAll(tongbiAlarmMap.keySet());
//	alarmNameSet.addAll(huanbiAlarmMap.keySet());
//
//
//
//
//	      out.write('\r');
//	      out.write('\n');
//	if(alarmNameSet.size()>0){ 
//	      out.write("\r\n");
//	      out.write("<table width=\"100%\" border=\"1\" class=\"ui-widget ui-widget-content\">\r\n");
//	      out.write("\t<tr class=\"headcon \">\r\n");
//	      out.write("\t\t<td align=\"left\" colspan=\"4\"><font color=\"#000000\" size=\"2\">告警信息</font></td>\r\n");
//	      out.write("\t</tr>\r\n");
//	      out.write("\t<tr class=\"ui-widget-header \">\r\n");
//	      out.write("\t\t<td width=\"250\" align=\"center\">告警点</td>\r\n");
//	      out.write("\t\t<td align=\"center\">次数</td>\r\n");
//	      out.write("\t\t<td align=\"center\">同比</td>\r\n");
//	      out.write("\t\t<td align=\"center\">环比</td>\r\n");
//	      out.write("\t</tr>\r\n");
//	      out.write("\t");
//	for(String alarmName:alarmNameSet){ 
//	      out.write("\r\n");
//	      out.write("\t<tr>\r\n");
//	      out.write("\t\t<td align=\"center\">");
//	      out.write(alarmName );
//	      out.write("</td>\r\n");
//	      out.write("\t\t<td align=\"center\">");
//	      out.write(alarmMap.get(alarmName)==null?" - ":alarmMap.get(alarmName) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t<td align=\"center\">");
//	      out.write(Utlitites.scale(alarmMap.get(alarmName),tongbiAlarmMap.get(alarmName)) );
//	      out.write("</td>\r\n");
//	      out.write("\t\t<td align=\"center\">");
//	      out.write(Utlitites.scale(alarmMap.get(alarmName),huanbiAlarmMap.get(alarmName)) );
//	      out.write("</td>\r\n");
//	      out.write("\t</tr>\r\n");
//	      out.write("\t");
//	} 
//	      out.write("\r\n");
//	      out.write("</table>\r\n");
//	} 
//	      out.write("\r\n");
//	      out.write("</div>\r\n");
//	      out.write("</div>\r\n");
//	} 
//	      out.write("\r\n");
//	      out.write("\r\n");
//	      out.write("</div>\r\n");
//	      out.write("</body>\r\n");
//	      out.write("</html>");
//		return out.toString();
//	      
//	}
//	
//	private static String getBaseValue(Integer keyId,Map<Integer,KeyValueBaseLinePo> appBaseMap){
//		KeyValueBaseLinePo po = appBaseMap.get(keyId);
//		if(po==null){
//			return null;
//		}else{
//			return po.getBaseLineValue()+"";
//		}
//	}
	
	public static  String getDayReportByJsp(String searchDate){		
		String url = "http://127.0.0.1:9999/monitorstat/report/report_day.jsp?searchDate="+searchDate;		
		return RequestByUrl.getMessageByJsp(url);
	}
	
}
