package com.taobao.csp.cost.web.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.cost.dao.CostDayDaoNew;
import com.taobao.csp.cost.po.CostAppTotal;
import com.taobao.csp.cost.po.CostAppType;
import com.taobao.csp.cost.service.AppCostService;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.CustomDateUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.db.impl.day.MonitorDayDao;
import com.taobao.monitor.common.po.AppInfoPo;


@Controller
public class IndexAction {

	private AppInfoAo appInfos=AppInfoAo.get();

	private static Logger logger = Logger.getLogger(CostAppAction.class);
	@Resource(name = "monitorDayDao")
	private MonitorDayDao monitorDayDao;
	@Resource(name = "costDayDaoNew")
	private CostDayDaoNew costDayDaoNew;
	@Resource(name = "appCostService")
	private AppCostService appCostService;
	
	@RequestMapping("/index.do")
	public ModelAndView index() {
		ModelAndView view = new ModelAndView("/cost/cost_helper");
		
		return view;
	} 

	static String[] dates=new String[]{"2012-06-02","2012-06-23",
			"2012-07-07","2012-07-21","2012-08-04","2012-08-18",
			"2012-09-01","2012-09-29",
			"2012-10-20","2012-11-03","2012-11-11"};
	
	static String[] taobaoApp=new String[]{"shopsystem","shopcenter",
			"tf_tm ","ump","login","uicfinal","tradeplatform","hesper","detail",
			"cart","tf_buy ","itemcenter"};
	
	static String[] tmallApp=new String[]{"tmallpromotion","malldetail","tmallsearch",
			"malldetailskip","tmallbuy","tmallcart","memberplatform",
			"logisticscenter","pointcenter","promotion","memberplatform"};
	
	static long[] keys=new long[]{992L,993L,14420L,42331L,54275L,
			42426L,42429L,34628L,42428L,42427L};

	
	@RequestMapping("/cost20121111.do")
	public ModelAndView cost20121111(String type,String isForce) {
		ModelAndView view = new ModelAndView("/cost/cost_20121111");
		
		String[] app=taobaoApp;
		
		if(StringUtils.isBlank(type) || type.equals("tm")){
			app=tmallApp;
		}
		Map<String,String> vs=new HashMap<String,String>();
		try{
			
			for(String ap:app){
				JSONArray ja=new JSONArray();
				
				for(String date:dates){
					Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
					Date ssdate=CustomDateUtil.getStartDay(sdate);
					Date edate=CustomDateUtil.getEndDay(sdate);
					
					List<CostAppTotal> rss=appCostService.getAppTotalCost(ap,date,ssdate, edate, isForce);
					
					if(rss.size()>0){
						ja.put(rss.get(0).getCostAll());
					}else{
						ja.put(0);
					}
					
				}
				vs.put(ap, ja.toString());
			}
		}catch(Exception e){
			//
			logger.warn("execute",e);
		}
		view.addObject("msg",vs);
		return view;
	} 

	/**
	 * 得到不同数量的机器列表
	 * 
	 * @return
	 */
	@RequestMapping("/hostdif.do")
	public ModelAndView hostdif() {
		ModelAndView view = new ModelAndView("/cost/cost_20121111");
		
		view = new ModelAndView("/ajax_result");
		StringBuilder sb=new StringBuilder();
		Map<String,String> tbs=new HashMap<String,String>();
		
		ttt(taobaoApp,tbs);
		ttt(tmallApp,tbs);
		
		for(Map.Entry<String, String> rsentry:tbs.entrySet()){
			sb.append(rsentry.getKey()).append(",").
				append(rsentry.getValue()).append("<br/>");
		}
		view.addObject("returnMsg",sb.toString());
		return view;
	} 
	
	private void ttt(String[] apps,Map<String,String> tbs){
		
		for(String tb:apps){
			AppInfoPo appInfo=appInfos.getAppInfoByAppName(tb);
			
			for(String date:dates){
				try {
					Map<Long, Long> rs=
								monitorDayDao.findMonitorCountMapAsValueByDate(appInfo.getAppId(), date, keys);
					long machineCount=0;
					for(Map.Entry<Long, Long> rsentry:rs.entrySet()){
						machineCount=machineCount+rsentry.getValue();
					}
					
					Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
					Date ssdate=CustomDateUtil.getStartDay(sdate);
					Date edate=CustomDateUtil.getEndDay(sdate);
					
					List<CostAppType> hostApps=
							costDayDaoNew.getTypeAppCostBy(tb, CostType.HOST.name(), 
									ssdate, edate);
					long machineCountNew=0;
					for(CostAppType cat:hostApps){
						machineCountNew=machineCountNew+cat.getCallNum();
					}
					tbs.put(appInfo.getAppName()+","+date,
							String.valueOf(machineCount)+","+machineCountNew);
				} catch (Exception e) {
					// TODO Ingore
				}
			}
		}
	}
	
}
