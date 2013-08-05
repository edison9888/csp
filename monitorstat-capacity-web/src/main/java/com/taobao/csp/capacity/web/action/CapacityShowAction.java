package com.taobao.csp.capacity.web.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.capacity.bo.DependencyCapacityBo;
import com.taobao.csp.capacity.constant.Constants;
import com.taobao.csp.capacity.dao.CapacityCostDao;
import com.taobao.csp.capacity.dao.CapacityDao;
import com.taobao.csp.capacity.dao.CapacityPvDao;
import com.taobao.csp.capacity.dao.CapacityRankingDao;
import com.taobao.csp.capacity.dao.CspDayDao;
import com.taobao.csp.capacity.dao.CspDependencyDao;
import com.taobao.csp.capacity.dao.CspLoadRunDao;
import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.day.BaseDayTrendModel;
import com.taobao.csp.capacity.model.equation.QuadraticEquation;
import com.taobao.csp.capacity.model.qps.QpsModel_N;
import com.taobao.csp.capacity.po.CapacityAppPo;
import com.taobao.csp.capacity.po.CapacityCostConfigPo;
import com.taobao.csp.capacity.po.CapacityCostDepPo;
import com.taobao.csp.capacity.po.CapacityCostInfoPo;
import com.taobao.csp.capacity.po.CapacityCostRatioPo;
import com.taobao.csp.capacity.po.CapacityRankingPo;
import com.taobao.csp.capacity.po.DependencyCapacityPo;
import com.taobao.csp.capacity.po.PvcountPo;
import com.taobao.csp.capacity.util.ChartUtil;
import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.monitor.common.db.impl.capacity.CapacityCapDao;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2011-9-19 下午01:46:31
 */
@Controller
@RequestMapping("/show.do")
public class CapacityShowAction {
	
	private static final Logger logger =  Logger.getLogger(CapacityShowAction.class);
	

	@Resource(name = "capacityDao")
	private CapacityDao capacityDao;
	
	@Resource(name = "capacityRankingDao")
	private CapacityRankingDao capacityRankingDao;

	@Resource(name = "appInfoDao")
	private AppInfoDao appInfoDao;

	@Resource(name = "cspLoadRunDao")
	private CspLoadRunDao cspLoadRunDao;

	@Resource(name = "cspDayDao")
	private CspDayDao cspDayDao;
	
	@Resource(name = "capacityPvDao")
	private CapacityPvDao capacityPvDao;
	
	@Resource(name = "dependencyCapacityBo")
	private DependencyCapacityBo dependencyCapacityBo;
	
	
	@Resource(name = "capacityCostDao")
	private CapacityCostDao capacityCostDao;
	
	@Resource(name = "dependencyDao")
	private CspDependencyDao dependencyDao;
	
	@Resource(name = "capacityCapDao")
	private CapacityCapDao capacityCapDao;

	@Resource(name = "constants")
	private Constants constants;

	/***  核心应用 ***/
	private Set<Integer> limitShowApp = new HashSet<Integer>();
	
	/*** 核心应用下面的图 ***/
	private Set<Integer> chartShowApp = new HashSet<Integer>();
	
	
	/***  无线应用 ***/
	private Set<Integer> wirelessShowApp = new HashSet<Integer>();
	

	{
		limitShowApp.add(1);
		limitShowApp.add(2);
		limitShowApp.add(3);
		// limitShowApp.add(8);
		limitShowApp.add(375);
		limitShowApp.add(376);
		limitShowApp.add(377);
		limitShowApp.add(378);
		
		// limitShowApp.add(322);
		limitShowApp.add(383);
		limitShowApp.add(384);
		limitShowApp.add(385);
		
		limitShowApp.add(930);  // trademanager
		limitShowApp.add(330);
		limitShowApp.add(4);
		limitShowApp.add(16);
		limitShowApp.add(21);
		limitShowApp.add(341);
		
		limitShowApp.add(870);  // ju
//		limitShowApp.add(25); // snsju
		limitShowApp.add(11);  // mytaobao
		
		limitShowApp.add(33);  // uiclogin
		limitShowApp.add(432);  // Detail-ump
		limitShowApp.add(729);  // Tmall-ump
		limitShowApp.add(456);  // Default-ump
		limitShowApp.add(431);  // Cart-ump
		limitShowApp.add(688);  // tmallcart
		limitShowApp.add(593);  // cartap
		limitShowApp.add(48);  // sell-upload
		limitShowApp.add(239);  // sell_top
		limitShowApp.add(47);  // sell
		limitShowApp.add(12);  // mercury
		limitShowApp.add(368);  // tcc
		limitShowApp.add(381);  // delivery 
		limitShowApp.add(725);  // juitemcenter 

		// tmall
		limitShowApp.add(464);  // tmallsell
		limitShowApp.add(369);  // malldetail
		limitShowApp.add(590);  // malldetailskip
		limitShowApp.add(705);  // tmallsearch
		limitShowApp.add(379);  // tmallbuy
		limitShowApp.add(460);  // tmallpromotion
		limitShowApp.add(287);  // marketingcenter
		limitShowApp.add(585);  // inventoryplatform
		limitShowApp.add(81);  // promotioncenter
		limitShowApp.add(460);  // tmallpromotion
		limitShowApp.add(286);  // promotion
		limitShowApp.add(390);  // pointcenter
		limitShowApp.add(324);  // wlb 
		limitShowApp.add(327);  // wlbexternal 
		limitShowApp.add(39);  // mc 
		
		chartShowApp.add(1);
		chartShowApp.add(2);
		chartShowApp.add(3);
		chartShowApp.add(375);
		chartShowApp.add(376);
		chartShowApp.add(377);
		chartShowApp.add(378);
		chartShowApp.add(383);
		chartShowApp.add(384);
		chartShowApp.add(385);
		chartShowApp.add(930);
		chartShowApp.add(330);
		chartShowApp.add(4);
		chartShowApp.add(16);
		chartShowApp.add(21);
		chartShowApp.add(341);
		chartShowApp.add(870);
		chartShowApp.add(11);  // mytaobao
		
		wirelessShowApp.add(623); // wsearch
		wirelessShowApp.add(394); // gene
		wirelessShowApp.add(674); // wdetail
		wirelessShowApp.add(123); // wdc
		wirelessShowApp.add(393); // wim
		wirelessShowApp.add(395); // wass
		wirelessShowApp.add(675); // wmac
		wirelessShowApp.add(122); // wtm
		wirelessShowApp.add(628); // softwarestore
		wirelessShowApp.add(118); // galaxy
	}

	/**
	 * 展示核心应用的容量
	 * 
	 * @param rankingName
	 * @param date
	 * @return
	 */
	@RequestMapping(params = "method=showCapacityLimit")
	public ModelAndView showCapacityRankingLimit() {
		String rankingName = "容量排名";
		
		Calendar cal1 = constants.getRankingTime();

		List<CapacityRankingPo> limitList = new ArrayList<CapacityRankingPo>();
		List<CapacityRankingPo> chartList = new ArrayList<CapacityRankingPo>();

		List<CapacityRankingPo> list = capacityRankingDao.findCapacityLatestRankingPos(rankingName);

		for (CapacityRankingPo po : list) {
			if (limitShowApp.contains(po.getAppId())) {
				limitList.add(po);
			}
			if (chartShowApp.contains(po.getAppId())) {
				chartList.add(po);
			}
		}

		ModelAndView view = new ModelAndView("/capacity/index_capacity");
		view.addObject("limitList", limitList);
		view.addObject("flashString", ChartUtil.drawCapacityRankChart(chartList));
		view.addObject("forestFlashString", ChartUtil.drawCapacityForestRankChart(chartList));
		view.addObject("year", cal1.get(Calendar.YEAR));
		return view;
	}
	
	/**
	 * 展示tair应用的容量
	 * 
	 * @param rankingName
	 * @param date
	 * @return
	 */
	@RequestMapping(params = "method=showCapacityTair")
	public ModelAndView showCapacityRankingTair() {
		String rankingName = "容量排名";
		
		Calendar cal1 = constants.getRankingTime();

		List<CapacityRankingPo> limitList = new ArrayList<CapacityRankingPo>();

		List<CapacityRankingPo> list = capacityRankingDao.findCapacityLatestRankingPos(rankingName);

		for (CapacityRankingPo po : list) {
			if (po.getAppType().equals("tair")) {
				limitList.add(po);
			}
		}

		ModelAndView view = new ModelAndView("/capacity/index_capacity_tair");
		view.addObject("limitList", limitList);
		view.addObject("flashString", ChartUtil.drawCapacityRankChart(limitList));
		view.addObject("forestFlashString", ChartUtil.drawCapacityForestRankChart(limitList));
		view.addObject("year", cal1.get(Calendar.YEAR));
		return view;
	}
	
	/**
	 * 展示无线应用的容量
	 * 
	 * @param rankingName
	 * @param date
	 * @return
	 */
	@RequestMapping(params = "method=showCapacityWireless")
	public ModelAndView showCapacityWireless() {
		String rankingName = "容量排名";
		
		Calendar cal1 = constants.getRankingTime();

		List<CapacityRankingPo> limitList = new ArrayList<CapacityRankingPo>();
		List<CapacityRankingPo> chartList = new ArrayList<CapacityRankingPo>();

		List<CapacityRankingPo> list = capacityRankingDao.findCapacityLatestRankingPos(rankingName);

		for (CapacityRankingPo po : list) {
			if (wirelessShowApp.contains(po.getAppId())) {
				limitList.add(po);
			}
			if (wirelessShowApp.contains(po.getAppId())) {
				chartList.add(po);
			}
		}

		ModelAndView view = new ModelAndView("/capacity/index_capacity");
		view.addObject("limitList", limitList);
		view.addObject("flashString", ChartUtil.drawCapacityRankChart(chartList));
		view.addObject("forestFlashString", ChartUtil.drawCapacityForestRankChart(chartList));
		view.addObject("year", cal1.get(Calendar.YEAR));
		return view;
	}

	/***
	 * 展示所有应用的容量
	 * @return
	 */
	@RequestMapping(params = "method=showCapacityMore")
	public ModelAndView showCapacityRankingMore() {

		String rankingName = "容量排名";
		Calendar cal1 = constants.getRankingTime();

		List<CapacityRankingPo> list = capacityRankingDao.findCapacityLatestRankingPos(rankingName);
		
		List<CapacityRankingPo> limitList = new ArrayList<CapacityRankingPo>();
		for (CapacityRankingPo po : list) {
			if ((!po.getAppType().equals("tair")) && (!limitShowApp.contains(po.getAppId())) && (!wirelessShowApp.contains(po.getAppId()))) {
				limitList.add(po);
			}
		}

		ModelAndView view = new ModelAndView("/capacity/index_capacity_more");

		view.addObject("list", limitList);
		view.addObject("year", cal1.get(Calendar.YEAR));
		return view;
	}
	
	/***
	 * 容量报表
	 * @param appIds
	 * @return
	 */
	@RequestMapping(params = "method=report")
	public ModelAndView showReport(String appIds) {

		String rankingName = "容量排名";
		Calendar cal1 = constants.getRankingTime();

		List<CapacityRankingPo> list = capacityRankingDao.findCapacityRankingPoByAppIds(rankingName, appIds);

		ModelAndView view = new ModelAndView("/capacity/index_capacity_report");

		view.addObject("list", list);
		view.addObject("year", cal1.get(Calendar.YEAR));
		return view;
	}
	
	
	@RequestMapping(params = "method=showCapacityApp")
	public ModelAndView showCapacityApp() {

		List<CapacityAppPo> capacityAppPos = capacityDao.findCapacityAppList();
		List<AppInfoPo> appInfoPos = new ArrayList<AppInfoPo>();
		
		for (CapacityAppPo capacitypo : capacityAppPos) {
			int appId = capacitypo.getAppId();
			AppInfoPo appPo = appInfoDao.findAppInfoById(appId);
			appInfoPos.add(appPo);
		}
		
		
		ModelAndView view = new ModelAndView("/capacity/index_capacity_app");
		view.addObject("capacityAppPos", capacityAppPos);
		view.addObject("appList", appInfoPos);
		return view;
	} 
	

	/***
	 * 暂时被去掉，后续要使用的话在放开
	 * @param appId
	 * @param raise
	 * @return
	 */
	@RequestMapping(params = "method=showCapacityDepend")
	public ModelAndView showCapacityDepend(int appId, String raise) {
		ModelAndView view = new ModelAndView("/capacity/app_capacity_depends");
		
		int raisePerecentage = 0;
		if (!StringUtils.isBlank(raise)) {
			try {
				raisePerecentage = Integer.parseInt(raise.trim());
			} catch (Exception e) {
				raisePerecentage = 0;
			}
			
		}
		
		List<DependencyCapacityPo> list = dependencyCapacityBo.findAllDependencyCapacityPos(appId, raisePerecentage);
		String flashString = ChartUtil.drawDependCapacityChart(list);
		AppInfoPo appInfoPo = appInfoDao.findAppInfoById(appId);
		
		view.addObject("list",list);
		view.addObject("flashString", flashString);
		view.addObject("appId", appId);
		view.addObject("appName", appInfoPo.getAppName());
		view.addObject("percentage", String.valueOf(raisePerecentage));
		
		return view;
	}
	
	@RequestMapping(params = "method=showCapacityDetail")
	public ModelAndView showCapacityDetail(int appId) {
		ModelAndView view = new ModelAndView("/capacity/app_capacity_details");
		
		String rankingName = "容量排名";
	
		List<CapacityRankingPo> list = capacityRankingDao.findCapacityHistoryPos(rankingName, appId);

		view.addObject("list", list);
		return view;
	}
	
	@RequestMapping(params = "method=forecastDayPv")
	public void showForecastDayPv(int appId, int year,HttpServletResponse response){
		
		/////////日预测
		Map<String, Map<String, Long>> dataMap = new LinkedHashMap<String, Map<String, Long>>();
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("MMdd");
		//获取预测需要的pv 数据
		List<PvcountPo> dayPvList = capacityPvDao.findFeatureNeedData(appId, year);
		//预测模型
		QuadraticEquation quadraticEquation = new QuadraticEquation();
		BaseDayTrendModel dayModel = new BaseDayTrendModel(appId, year, dayPvList, quadraticEquation);
		//获取预测值列表
		List<Coordinate> dayFeatrueList = dayModel.getFutureDay();
		Map<String, Long> featureMap = new HashMap<String, Long>();
		if (quadraticEquation.isAvaliable()) {
			for (Coordinate c : dayFeatrueList) {
				featureMap.put((c.getX()+"").substring(4,8), (long) Arith.div(c.getY(), 10000));
			}
		}
		
		//当实际值列表
		List<PvcountPo> curYearList = capacityPvDao.findAllByAppIdAndYear(appId, year);
		Map<String, Long> curentryMap = new HashMap<String, Long>();
		for (PvcountPo c : curYearList) {
			curentryMap.put(sdf1.format(c.getCollectTime()), (long) Arith.div(c.getPvCount(), 10000));
		}
		//去年实际值
		List<PvcountPo> pYearList = capacityPvDao.findAllByAppIdAndYear(appId, year - 1);
		Map<String, Long> pentryMap = new HashMap<String, Long>();
		for (PvcountPo c : pYearList) {
			pentryMap.put(sdf1.format(c.getCollectTime()), (long) Arith.div(c.getPvCount(), 10000));
		}
		
		//前年实际值
		List<PvcountPo> ppYearList = capacityPvDao.findAllByAppIdAndYear(appId, year - 2);
		Map<String, Long> ppentryMap = new HashMap<String, Long>();
		for (PvcountPo c : ppYearList) {
			ppentryMap.put(sdf1.format(c.getCollectTime()), (long) Arith.div(c.getPvCount(), 10000));
		}
		
		if (featureMap.size() > 0) {
			dataMap.put(year + "预测值", featureMap);
		}
		if (curentryMap.size() > 0) {
			dataMap.put(year + "实际值", curentryMap);
		}
		if (pentryMap.size() > 0) {
			dataMap.put((year - 1) + "实际值", pentryMap);
		}
		if (ppentryMap.size() > 0) {
			dataMap.put((year - 2) + "实际值", ppentryMap);
		}
		
		try{
			 String xml = createCommonCharXm2(dataMap);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(xml);
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
		
	}
	
	@RequestMapping(params = "method=forecastQps")
	public void showForecastQps(int appId, int year,HttpServletResponse response){
		
		AppInfoPo appPo = appInfoDao.findAppInfoById(appId);
		
		//获取预测需要的pv 数据
		List<PvcountPo> dayPvList = capacityPvDao.findFeatureNeedData(appId, year);

		List<HostPo> machineNum = OpsFreeHostCache.get().getHostListNoCache(appPo.getOpsName());
		
		//获取应用的QPS key ID
		int qpsKeyId = 16931;
		if("center".equals(appPo.getAppType())){
			qpsKeyId = 27734;
		}

		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -180);

		Date startDate = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Map<String, Double> mappv = new HashMap<String, Double>();
		
		
		List<PvcountPo> currentPvList = capacityPvDao.findAllByAppIdAndYear(appId, year);

		for (PvcountPo po : currentPvList) {
			mappv.put(sdf.format(po.getCollectTime()), po.getPvCount());
		}
		//获取历史QPS
		List<KeyValuePo> listQps = cspDayDao.findMonitorCountByDate(appPo.getAppDayId(), qpsKeyId, startDate, endDate);
		Map<String, Double> mapQps = new HashMap<String, Double>();
		for (KeyValuePo po : listQps) {
			mapQps.put(sdf.format(po.getCollectTime()), Double.parseDouble(po.getValueStr()));
		}
		
		//获取历史机器数量
		Map<String, Double> mapMachine = new HashMap<String, Double>();
		for (Map.Entry<String, Integer> entry : constants.getHostSiteKey().entrySet()) {
			List<KeyValuePo> listhost = cspDayDao.findMonitorCountByDate(appPo.getAppDayId(), entry.getValue(),
					startDate, endDate);
			for (KeyValuePo po : listhost) {
				String key = sdf.format(po.getCollectTime());
				Double v = mapMachine.get(key);
				if (v == null) {
					mapMachine.put(key, Double.parseDouble(po.getValueStr()));
				} else {
					mapMachine.put(key, Double.parseDouble(po.getValueStr()) + v);
				}

			}
		}
		//创建qps 模型
		QpsModel_N qpsmodel = new QpsModel_N(mappv, mapQps, mapMachine);

		int curMachineNum = machineNum.size();

		Map<String, Double> featureQPSMap = new HashMap<String, Double>();
		
		//预测模型
		BaseDayTrendModel dayModel = new BaseDayTrendModel(appId, year, dayPvList, new QuadraticEquation());
		//获取预测值列表
		List<Coordinate> dayFeatrueList = dayModel.getFutureDay();	
		//预测未来的QPS
		for (Coordinate c : dayFeatrueList) {
			double featureQps = qpsmodel.getY(Double.valueOf(c.getY()).intValue());// 预测的流量在高峰期可能达到的qps
			featureQPSMap.put(c.getX() + "", featureQps);
		}

		Map<String, Double> entryQpsMap = new HashMap<String, Double>();
		  List<KeyValuePo> poList = cspDayDao.findMonitorCountByDate(appPo.getAppDayId(),qpsKeyId,new Date(year-1900,0,1),new Date());
		  for(KeyValuePo po:poList){
			  String key = sdf.format(po.getCollectTime());
			  Double machines =  mapMachine.get(key);
				if(machines == null){
			  		machines =(double)curMachineNum;
			  	}
			  entryQpsMap.put(key,Double.parseDouble(po.getValueStr())*machines);
		  }
		
		

		AutoLoadType type = cspLoadRunDao.findLoadRunTypeByAppId(appPo.getAppId());

		List<LoadrunResult> loadList = cspLoadRunDao.findLoadrunResult(appPo.getAppId(), type.getQpsKey(), new Date(
				year - 1900, 0, 1), new Date());
		Map<String, Double> yaceQpsMap = new HashMap<String, Double>();
		for (LoadrunResult entry : loadList) {
			String time = sdf.format(entry.getCollectTime());;
			Double machines = mapMachine.get(time);
			if (machines == null) {
				machines = (double) curMachineNum;
			}
			yaceQpsMap.put(time, entry.getValue() * machines);
		}

		Map<String, Map<String, Double>> qpsDataMap = new HashMap<String, Map<String, Double>>();
		qpsDataMap.put(year + "压测QPS", yaceQpsMap);
		qpsDataMap.put(year + "实际QPS", entryQpsMap);
		qpsDataMap.put(year + "预测QPS", featureQPSMap);

		
		
		try{
			 String xml3 = createCommonCharXmlExt(qpsDataMap);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(xml3);
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
	}
	
	


	@RequestMapping(params = "method=showMachine")
	public ModelAndView showMachineRanking() {

		String rankingName = "使用率排名";
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DAY_OF_MONTH, -2);

		List<CapacityRankingPo> list = capacityRankingDao.findCapacityLatestRankingPos(rankingName);

		ModelAndView view = new ModelAndView("/capacity/index_machine");

		view.addObject("list", list);
		view.addObject("year", cal1.get(Calendar.YEAR));
		return view;
	}
	
	/**
	 * 用于构建一天内 以HH:mm 为单位的X轴坐标走视图
	 * @param map key HH:mm结构时间，value 值
	 * @return amline 的xml 格式数据
	 * @throws ParseException
	 */
	public static String createCommonCharXm2(Map<String,Map<String, Long>> map) throws ParseException{
		
		if(map.size()<1)return "";
		StringBuffer sb =  new StringBuffer("<chart>");			
		Set<String> timeSet = new HashSet<String>();
		for(Map.Entry<String,Map<String, Long>> entry:map.entrySet()){
			Map<String, Long> tmp = entry.getValue();
			timeSet.addAll(tmp.keySet());			
		}
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(timeSet);
		Collections.sort(dateList,new Comparator<String>(){
			
			public int compare(String o1, String o2) {
				
				int time1 = Integer.parseInt(o1.replaceAll(":", ""));
				int time2 = Integer.parseInt(o2.replaceAll(":", ""));
				
				if(time1>time2){
					return 1;
				}else if(time1<time2){
					return -1;
				}
				
				return 0;
			}});
		
		sb.append("<series>");
		for(int i=0;i<dateList.size();i++){			
			sb.append("<value xid='"+i+"'>"+dateList.get(i)+"</value>");
		}
		sb.append("</series>");
		sb.append("<graphs>");
		int index=1;
		for(Map.Entry<String,Map<String, Long>> entry:map.entrySet()){			
			sb.append("<graph gid='"+index+"' title='"+entry.getKey()+"'>");				
			Map<String, Long> poMap = entry.getValue();			
			int allSize = poMap.size();	
			int renderSize = 0;
			double tmp = 0;
			for(int i=0;i<dateList.size();i++){
				String key = dateList.get(i);				
				Long label = poMap.get(key);
				if(label!=null&&label!=0){
					renderSize++;
					sb.append("<value xid='"+i+"'>"+label+"</value>");			
					tmp =label;					
				}else{
					sb.append("<value xid='"+i+"'>"+tmp+"</value>");
				}
				if(renderSize>=allSize){
					break;
				}
				
			}	
			sb.append("</graph>");
			index++;
		}
		sb.append("</graphs>");			
		sb.append("</chart>");
		return sb.toString();
		
	}
	
	
	/**
	 * 用于构建一天内 以HH:mm 为单位的X轴坐标走视图
	 * @param map key HH:mm结构时间，value 值
	 * @return amline 的xml 格式数据
	 * @throws ParseException
	 */
	public static String createCommonCharXmlExt(Map<String,Map<String, Double>> map) throws ParseException{
		
		if(map.size()<1)return "";
		StringBuffer sb =  new StringBuffer("<chart>");			
		Set<String> timeSet = new HashSet<String>();
		for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){
			Map<String, Double> tmp = entry.getValue();
			timeSet.addAll(tmp.keySet());			
		}
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(timeSet);
		Collections.sort(dateList,new Comparator<String>(){
			
			public int compare(String o1, String o2) {
				
				int time1 = Integer.parseInt(o1.replaceAll(":", ""));
				int time2 = Integer.parseInt(o2.replaceAll(":", ""));
				
				if(time1>time2){
					return 1;
				}else if(time1<time2){
					return -1;
				}
				
				return 0;
			}});
		
		sb.append("<series>");
		for(int i=0;i<dateList.size();i++){			
			sb.append("<value xid='"+i+"'>"+dateList.get(i)+"</value>");
		}
		sb.append("</series>");
		sb.append("<graphs>");
		int index=1;
		for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){			
			sb.append("<graph gid='"+index+"' title='"+entry.getKey()+"'>");				
			Map<String, Double> poMap = entry.getValue();			
			int allSize = poMap.size();	
			int renderSize = 0;
			Double tmp = new Double(0);
			for(int i=0;i<dateList.size();i++){
				String key = dateList.get(i);				
				Double label = poMap.get(key);
				if(label!=null){
					renderSize++;
					if(renderSize<allSize-1){//最后一个不要
						sb.append("<value xid='"+i+"'>"+Math.round(label)+"</value>");	
					}				
					tmp =label;					
				}else{
					sb.append("<value xid='"+i+"'>"+Math.round(tmp)+"</value>");
				}
				if(renderSize>=allSize){
					break;
				}
				
			}	
			sb.append("</graph>");
			index++;
		}
		sb.append("</graphs>");			
		sb.append("</chart>");
		return sb.toString();
		
	}
	
	
	@RequestMapping(params = "method=showCapacityImage")
	public void showCapacityImage(HttpServletResponse response) {
		String rankingName = "容量排名";
		
		Calendar cal1 = constants.getRankingTime();

		List<CapacityRankingPo> limitList = new ArrayList<CapacityRankingPo>();

		List<CapacityRankingPo> list = capacityRankingDao.findCapacityLatestRankingPos(rankingName);

		for (CapacityRankingPo po : list) {
			if (limitShowApp.contains(po.getAppId())) {
				limitList.add(po);
			}
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<chart>");
		sb.append("<series>");
		for (int i = 0; i < limitList.size(); i++) {
			CapacityRankingPo po = limitList.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getAppName()
					+ "</value>");
		}
		sb.append("</series>");

		sb.append("<graphs>");

		sb.append("<graph gid='pqs' title='高峰期QPS'>");
		for (int i = 0; i < limitList.size(); i++) {
			CapacityRankingPo po = limitList.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + po.getCQps() / po.getCLoadrunQps() + "</value>");
		}
		sb.append("</graph>");

		sb.append("<graph gid='pqs' title='剩余压测QPS'>");
		for (int i = 0; i < limitList.size(); i++) {
			CapacityRankingPo po = limitList.get(i);
			sb.append("<value xid='" + (i + 1) + "'>" + (po.getCLoadrunQps() - po.getCQps()) / po.getCLoadrunQps()
					+ "</value>");
		}
		sb.append("</graph>");
		sb.append("</graphs>");
		sb.append("</chart>");

		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(sb.toString());
			response.flushBuffer();	
		}catch (Exception e) {
		}
	}
	
	@RequestMapping(params = "method=showCapacityCostListNormal")
	public ModelAndView showCapacityCostListNormal() {
		ModelAndView view = new ModelAndView("/capacity/cost/capacity_cost_list");
		List<CapacityCostInfoPo> costList = capacityCostDao.findCapacityCostListNormal();
		view.addObject("costList", costList);
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostListTair")
	public ModelAndView showCapacityCostListTair() {
		ModelAndView view = new ModelAndView("/capacity/cost/capacity_cost_list");
		List<CapacityCostInfoPo> costList = capacityCostDao.findCapacityCostListTair();
		view.addObject("costList", costList);
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostListDB")
	public ModelAndView showCapacityCostListDB() {
		ModelAndView view = new ModelAndView("/capacity/cost/capacity_cost_list");
		List<CapacityCostInfoPo> costList = capacityCostDao.findCapacityCostListDB();
		view.addObject("costList", costList);
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostDep")
	public ModelAndView showCapacityCostDep(String appName, String pv) {
		ModelAndView view = new ModelAndView("/capacity/cost/capacity_cost_depend");
		List<CapacityCostDepPo> costDepList = capacityCostDao.findCapacityCostDepList(appName);
		
		long totalDepPv = 0;
		long totalDepCost = 0;
		double totolPerDepCost = 0;
		for (CapacityCostDepPo po :  costDepList) {
			 double perDepCost = (double)po.getDepCost() / Double.valueOf(pv).doubleValue();
			 po.setPerDepCost(perDepCost);
			 totalDepPv += po.getDepPv();
			 totalDepCost += po.getDepCost();
			 totolPerDepCost += perDepCost;
		}
		
		DecimalFormat dfDouble = new DecimalFormat("###,###.##");
		view.addObject("costDepList", costDepList);
		view.addObject("appName", appName);
		view.addObject("totalDepPv", Utlitites.fromatLong(String.valueOf(totalDepPv)));
		view.addObject("totalDepCost", Utlitites.fromatLong(String.valueOf(totalDepCost)));
		view.addObject("totalPerDepCost", dfDouble.format(totolPerDepCost));
		view.addObject("pv", Utlitites.fromatLong(String.valueOf(pv)));
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostSelf")
	public ModelAndView showCapacityCostDep(String appName, String machineNum, 
			String pv, String perCost, String selfCost) {
		ModelAndView view = new ModelAndView("/capacity/cost/capacity_cost_depend_self");
		List<CapacityCostDepPo> costDepList = capacityCostDao.findCapacityCostDepSelfList(appName);
		
		view.addObject("costDepList", costDepList);
		view.addObject("appName", appName);
		view.addObject("machineNum", machineNum);
		view.addObject("pv", Utlitites.fromatLong(pv));
		view.addObject("perCost", perCost);
		view.addObject("selfCost", Utlitites.fromatLong(selfCost));
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostConfig")
	public ModelAndView showCapacityCostConfig() {
		ModelAndView view = new ModelAndView("/capacity/cost/capacity_cost_config");
		List<CapacityCostConfigPo> list = new ArrayList<CapacityCostConfigPo>();
		Map<String, List<CapacityCostConfigPo>> costMap = dependencyDao.getCapacityCostConfigPos();
		
		for (Map.Entry<String, List<CapacityCostConfigPo>> entry : costMap.entrySet()) {
			String groupName = entry.getKey();
			List<CapacityCostConfigPo> pos =  entry.getValue();
			int machineNum = CspSyncOpsHostInfos.findOnlineHostByOpsName(groupName).size();
			double perMachine = (double) machineNum / pos.size();
			
			for (CapacityCostConfigPo po : pos) {
				po.setGroupMachineNum(machineNum);
				po.setPerMchine(perMachine);
				list.add(po);
			}
		}
		
		view.addObject("costList", list);
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostDiamond")
	public ModelAndView showCapacityCostDiamond() {
		ModelAndView view = new ModelAndView("/capacity/cost/capacity_cost_config");
		List<CapacityCostConfigPo> list = new ArrayList<CapacityCostConfigPo>();
		Map<String, List<CapacityCostConfigPo>> costMap = dependencyDao.getCapacityCostDiamondPos();
		
		for (Map.Entry<String, List<CapacityCostConfigPo>> entry : costMap.entrySet()) {
			String groupName = entry.getKey();
			List<CapacityCostConfigPo> pos =  entry.getValue();
			int machineNum = CspSyncOpsHostInfos.findOnlineHostByOpsName(groupName).size();
			double perMachine = (double) machineNum / pos.size();
			
			for (CapacityCostConfigPo po : pos) {
				po.setGroupMachineNum(machineNum);
				po.setPerMchine(perMachine);
				list.add(po);
			}
		}
		
		view.addObject("costList", list);
		
		return view;
	} 
	
	@RequestMapping(params = "method=showCapacityCostRatio")
	public ModelAndView showCapacityCostRatio() {
		ModelAndView view = new ModelAndView("/capacity/cost/capacity_cost_ratio");
		List<CapacityCostRatioPo> list;
		
		list = capacityCostDao.findCapacityCostRatios();
		
		view.addObject("list", list);
		return view;
	} 
	
	@RequestMapping(params = "method=showCostHelp")
	public ModelAndView showCostHelp() {
		ModelAndView view = new ModelAndView("/capacity/cost/cost_helper");
		
		return view;
	} 
	
}
