package com.taobao.csp.depend.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.dao.CspAppHsfDependConsumeDao;
import com.taobao.csp.depend.dao.MonitorDayDao;
import com.taobao.csp.depend.po.AmlineFlash;
import com.taobao.csp.depend.po.HistoryGraphPo;
import com.taobao.csp.depend.po.hsf.AppConsumerInterfaceSummary;
import com.taobao.csp.depend.po.hsf.AppExceptionListPo;
import com.taobao.csp.depend.po.hsf.AppProviderSummary;
import com.taobao.csp.depend.po.hsf.AppSummary;
import com.taobao.csp.depend.po.hsf.HsfProvideMachine;
import com.taobao.csp.depend.po.hsf.InterfaceSummary;
import com.taobao.csp.depend.po.hsf.ProvideSiteRating;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * 这个类主要统计Center调用其他App的信息，从Consumer维度统计。
 * @author zhongting.zy
 *
 */
@Controller
@RequestMapping("/show/hsfconsume.do")
public class DependHsfConsumeDataAction extends BaseAction {
	
	@Resource(name = "cspAppHsfDependConsumeDao")
	private CspAppHsfDependConsumeDao cspAppHsfDependConsumeDao;
	
  
  @Resource(name = "monitorDayDao")
  MonitorDayDao monitorDayDao;
  
	/**
	 * 展示center 应用调用其他应用的概括信息
	 * @param opsName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showAppCenterConsumeHsfInfo")
	public ModelAndView showAppCenterConsumeHsfInfo(String opsName,String selectDate ){
		
		//校验日期
		Date now = MethodUtil.getDate(selectDate);
		selectDate = MethodUtil.getStringOfDate(now);
		
		List<AppProviderSummary> appProviderList = cspAppHsfDependConsumeDao.sumCenterAppHsfToAppConsumer(opsName, selectDate);
		
		if(appProviderList.size() ==0){
			
			ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/noMessage");
			view.addObject("opsName", opsName);
			view.addObject("selectDate", selectDate);
			return view;
		}
		
		
		
		List<AppConsumerInterfaceSummary>appProvderInterfaceList = cspAppHsfDependConsumeDao.sumCenterAppToAppProviderInterface(opsName, selectDate);
		
		Date predate = MethodUtil.getPreDate(selectDate);
		List<AppProviderSummary> preAppConsumerList = cspAppHsfDependConsumeDao.sumCenterAppHsfToAppConsumer(opsName, MethodUtil.getStringOfDate(predate));
		List<AppConsumerInterfaceSummary> preAppProvderInterfaceList = cspAppHsfDependConsumeDao.sumCenterAppToAppProviderInterface(opsName, MethodUtil.getStringOfDate(predate));
		
		long allCallNum = 0;
		long preAllCallNum = 0;
		Set<String> apps = new HashSet<String>();
		for(AppProviderSummary app:appProviderList){
			apps.add(app.getOpsName());
			allCallNum+=app.getCallAllNum();
		}
		
		Set<String> preApps = new HashSet<String>();
		for(AppProviderSummary app:preAppConsumerList){
			preApps.add(app.getOpsName());
			preAllCallNum+=app.getCallAllNum();
		}
		
		
		Set<String> interfaces = new HashSet<String>();
		for(AppConsumerInterfaceSummary app:appProvderInterfaceList){
			interfaces.add(app.getInterfaceName());
		}
		
		Set<String> preInterfaces = new HashSet<String>();
		for(AppConsumerInterfaceSummary app:preAppProvderInterfaceList){
			preInterfaces.add(app.getInterfaceName());
		}
		
		Map<String,AppSummary> appSummaryMap = comSummary(appProviderList,preAppConsumerList);
		Map<String,ProvideSiteRating> provideSiteRatingMap = comSiteRating(appProviderList,preAppConsumerList);
		Map<String,InterfaceSummary> interfaceMap  = comInterface(appProvderInterfaceList,preAppProvderInterfaceList);
		
		
		ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/appCenterHsfInfo");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		
		view.addObject("appSummaryMap", appSummaryMap);
		view.addObject("provideSiteRatingMap", provideSiteRatingMap);
		
		ArrayList<InterfaceSummary> tmp = new ArrayList<InterfaceSummary>();
		tmp.addAll(interfaceMap.values());
		Collections.sort(tmp);
		view.addObject("interfaceList",tmp);
		
		ArrayList<AppSummary> tmpapp = new ArrayList<AppSummary>();
		tmpapp.addAll(appSummaryMap.values());
		Collections.sort(tmpapp);
		view.addObject("appList",tmpapp);
		
		
		view.addObject("appNums", apps.size()+"");
		view.addObject("preAppNums", preApps.size()+"");
		
		view.addObject("interfaceNums", interfaces.size()+"");
		view.addObject("preInterfaceNums", preInterfaces.size()+"");
		
		
		view.addObject("allCallNum", allCallNum+"");
		view.addObject("preAllCallNum", preAllCallNum+"");
		return view;
	}	
	
	/**
	 * 各个接口的变化
	 * 修改bug: 按照应用名+app名称做主键，因为有服务名称重复的情况
	 * @return
	 */
	public static Map<String,InterfaceSummary> comInterface(List<AppConsumerInterfaceSummary> appConsumerList,List<AppConsumerInterfaceSummary> preAppConsumerList){
		
		Map<String,InterfaceSummary> map = new HashMap<String, InterfaceSummary>();
		
		for(AppConsumerInterfaceSummary app:appConsumerList){
			String mapKey = app.getInterfaceName() + "_" + app.getProviderAppName();
			InterfaceSummary sum = map.get(mapKey);
			if(sum == null){
				sum = new InterfaceSummary();
				sum.setName(app.getInterfaceName());
				sum.setKeyName(MethodUtil.simplifyHsfInterfaceName(app.getInterfaceName()));
				sum.setAppName(app.getProviderAppName());	//新增，存储提供服务的应用名称
				map.put(mapKey, sum);
			}
			sum.setCallAllNum(sum.getCallAllNum()+app.getCallAllNum());
		}
		
		Map<String,InterfaceSummary> preMap = new HashMap<String, InterfaceSummary>();
		
		for(AppConsumerInterfaceSummary app:preAppConsumerList){
			String mapKey = app.getInterfaceName() + "_" + app.getProviderAppName();
			InterfaceSummary sum = preMap.get(mapKey);
			if(sum == null){
				sum = new InterfaceSummary();
				sum.setName(app.getInterfaceName());
				sum.setKeyName(MethodUtil.simplifyHsfInterfaceName(app.getInterfaceName()));
				sum.setAppName(app.getProviderAppName());
				preMap.put(mapKey, sum);
			}
			sum.setPreCallAllNum(sum.getPreCallAllNum()+app.getCallAllNum());
		}
		
		
		for(Map.Entry<String,InterfaceSummary> entry:map.entrySet()){
			if(preMap.get(entry.getKey())==null){
				entry.getValue().setOption(ConstantParameters.CONTROST_ADD);
			}else{
				entry.getValue().setPreCallAllNum(preMap.get(entry.getKey()).getPreCallAllNum());
			}
		}
		
		//不关心
		for(Map.Entry<String,InterfaceSummary> entry:preMap.entrySet()){
			if(map.get(entry.getKey())==null){
				entry.getValue().setOption(ConstantParameters.CONTROST_SUB);
				map.put(entry.getKey(), entry.getValue());
			}else{
				entry.getValue().setPreCallAllNum(preMap.get(entry.getKey()).getPreCallAllNum());
			}
		}
		
		return map;
		
	}
	
	
	/**
	 * 计算各个机房的占有率
	 * @return
	 */
	private Map<String,ProvideSiteRating> comSiteRating(List<AppProviderSummary> appConsumerList,List<AppProviderSummary> preAppConsumerList){
		
		Map<String,ProvideSiteRating> map = new HashMap<String, ProvideSiteRating>();
		
		for(AppProviderSummary app:appConsumerList){
			ProvideSiteRating sum = map.get(app.getConsumeSiteName());
			if(sum == null){
				sum = new ProvideSiteRating();
				sum.setSiteName(app.getConsumeSiteName());
				map.put(app.getConsumeSiteName(), sum);
			}
			sum.setCallAllNum(sum.getCallAllNum()+app.getCallAllNum());
		}
		
		Map<String,ProvideSiteRating> preMap = new HashMap<String, ProvideSiteRating>();
		
		for(AppProviderSummary app:preAppConsumerList){
			ProvideSiteRating sum = preMap.get(app.getConsumeSiteName());
			if(sum == null){
				sum = new ProvideSiteRating();
				sum.setSiteName(app.getConsumeSiteName());
				preMap.put(app.getConsumeSiteName(), sum);
			}
			sum.setPreCallAllNum(sum.getPreCallAllNum()+app.getCallAllNum());
		}
		
		
		for(Map.Entry<String,ProvideSiteRating> entry:map.entrySet()){
			if(preMap.get(entry.getKey())==null){
				entry.getValue().setOption(ConstantParameters.CONTROST_ADD);
			}else{
				entry.getValue().setPreCallAllNum(preMap.get(entry.getKey()).getPreCallAllNum());
			}
		}
		
		//不关心
		for(Map.Entry<String,ProvideSiteRating> entry:preMap.entrySet()){
			if(map.get(entry.getKey())==null){
				entry.getValue().setOption(ConstantParameters.CONTROST_SUB);
				map.put(entry.getKey(), entry.getValue());
			}else{
				entry.getValue().setPreCallAllNum(preMap.get(entry.getKey()).getPreCallAllNum());
			}
		}
		
		return map;
		
	}
	
	/**
	 * 各个应用的总调用量和历史调用量
	 * @param appConsumerList
	 * @param preAppConsumerList
	 * @return
	 */
	private  Map<String,AppSummary>  comSummary(List<AppProviderSummary> appConsumerList,List<AppProviderSummary> preAppConsumerList){
		
		Map<String,AppSummary> map = new HashMap<String, AppSummary>();
		
		for(AppProviderSummary app:appConsumerList){
			AppSummary sum = map.get(app.getOpsName());
			if(sum == null){
				sum = new AppSummary();
				sum.setOpsName(app.getOpsName());
				map.put(app.getOpsName(), sum);
			}
			sum.setCallAllNum(sum.getCallAllNum()+app.getCallAllNum());
		}
		
		Map<String,AppSummary> preMap = new HashMap<String, AppSummary>();
		
		for(AppProviderSummary app:preAppConsumerList){
			AppSummary sum = preMap.get(app.getOpsName());
			if(sum == null){
				sum = new AppSummary();
				sum.setOpsName(app.getOpsName());
				preMap.put(app.getOpsName(), sum);
			}
			sum.setPreCallAllNum(sum.getPreCallAllNum()+app.getCallAllNum());
		}
		
		
		for(Map.Entry<String,AppSummary> entry:map.entrySet()){
			if(preMap.get(entry.getKey())==null){
				entry.getValue().setOption("add");
			}else{
				entry.getValue().setPreCallAllNum(preMap.get(entry.getKey()).getPreCallAllNum());
			}
		}
		
		return map;
	}
	
	/**
	 * 显示center应用中依赖的应用的provider列表
	 * @param providename
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showAppProvideAll")
	public ModelAndView showAppProvideAll(String opsName,String selectDate) {
		
		List<AppProviderSummary>appProvderInterfaceList = cspAppHsfDependConsumeDao.sumCenterAppHsfToAppConsumer(opsName, selectDate);
		Date predate = MethodUtil.getPreDate(selectDate);
		List<AppProviderSummary> preAppProvderInterfaceList = cspAppHsfDependConsumeDao.sumCenterAppHsfToAppConsumer(opsName, MethodUtil.getStringOfDate(predate));
		
		long allCallNum = 0;
		for(AppProviderSummary app:appProvderInterfaceList){
			allCallNum+=app.getCallAllNum();
		}
		
		Map<String,AppSummary> interfaceMap =  comSummary(appProvderInterfaceList,preAppProvderInterfaceList);
		
		ArrayList<AppSummary> tmp = new ArrayList<AppSummary>();
		tmp.addAll(interfaceMap.values());
		Collections.sort(tmp);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/appconsumerAll");
		
		view.addObject("appList",tmp);
		view.addObject("allCallNum",allCallNum+"");
		view.addObject("opsName",opsName);
		view.addObject("selectDate",selectDate);
		return view;
	} 	
	
	/**
	 * 展示center应用中的全部接口调用信息
	 * @param opsName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showAppAllCenterInterface")
	public ModelAndView showAppAllCenterInterface(String opsName,String selectDate) {
		
		List<AppConsumerInterfaceSummary>appProvderInterfaceList = cspAppHsfDependConsumeDao.sumCenterAppToAppProviderInterface(opsName, selectDate);
		Date predate = MethodUtil.getPreDate(selectDate);
		List<AppConsumerInterfaceSummary> preAppProvderInterfaceList = cspAppHsfDependConsumeDao.sumCenterAppToAppProviderInterface(opsName, MethodUtil.getStringOfDate(predate));
		
		long allCallNum = 0;
		for(AppConsumerInterfaceSummary app:appProvderInterfaceList){
			allCallNum+=app.getCallAllNum();
		}
		
		
		Map<String,InterfaceSummary> interfaceMap =  comInterface(appProvderInterfaceList,preAppProvderInterfaceList);
		
		ArrayList<InterfaceSummary> tmp = new ArrayList<InterfaceSummary>();
		tmp.addAll(interfaceMap.values());
		Collections.sort(tmp);
		
		
		ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/providerInterfaceAll");
		
		view.addObject("interfaceList",tmp);
		view.addObject("allCallNum",allCallNum+"");
		view.addObject("opsName",opsName);
		view.addObject("selectDate",selectDate);
		return view;
	} 	
	
	/**
	 * 统计针对在center，center应用具体调用了某个应用的所有接口信息。
	 * center 调用量，机房，历史信息对比等。
	 * @param providename
	 * @param consumeName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showAppProviderDetail")
	public ModelAndView showAppProviderDetail(String providename,String consumeName,String selectDate){
		ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/appconsumerdetail");
		
		 List<AppConsumerInterfaceSummary> appConsume = cspAppHsfDependConsumeDao.sumCenterAppToProviderAppInterface(providename,consumeName,selectDate);
		 Date predate = MethodUtil.getPreDate(selectDate);
		 List<AppConsumerInterfaceSummary> preAppConsume = cspAppHsfDependConsumeDao.sumCenterAppToProviderAppInterface(providename,consumeName,MethodUtil.getStringOfDate(predate));
		 
		 Set<String> preAppNameSet = new HashSet<String>();
		 for(AppConsumerInterfaceSummary app:preAppConsume){
			 preAppNameSet.add(app.getInterfaceName());
		 }
		 
		 Map<String, InterfaceSummary> interfaceMap = new HashMap<String, InterfaceSummary>();
		 
		 Set<String> siteSet = new HashSet<String>();
		 
		 long allSumCall = 0;
		 for(AppConsumerInterfaceSummary app:appConsume){
			 
			 allSumCall+=app.getCallAllNum();
			 
			 siteSet.add(app.getConsumeSiteName());
			 InterfaceSummary appsum =  interfaceMap.get(app.getInterfaceName());
			 
			 if(appsum==null){
				 appsum = new InterfaceSummary();
				 
				 if(!preAppNameSet.contains(app.getInterfaceName())){
					 appsum.setOption("add"); 
				 }
				 appsum.setKeyName(MethodUtil.simplifyHsfInterfaceName(app.getInterfaceName()));
				 appsum.setAppName(app.getProviderAppName());	//记录提供服务的应用名称
				 interfaceMap.put(app.getInterfaceName(), appsum);
			 }
			 appsum.setCallAllNum(appsum.getCallAllNum()+app.getCallAllNum());
			 
			 InterfaceSummary siteApp = appsum.getSiteMap().get(app.getConsumeSiteName());
			 if(siteApp == null){
				 siteApp = new InterfaceSummary();
				 appsum.getSiteMap().put(app.getConsumeSiteName(), siteApp);
			 }
			 siteApp.setCallAllNum(siteApp.getCallAllNum()+app.getCallAllNum());
		 }
		 
		 long preAllSumCall = 0;
		 for(AppConsumerInterfaceSummary app:preAppConsume){
			 
			 preAllSumCall+=app.getCallAllNum();
			 
			 InterfaceSummary appsum =  interfaceMap.get(app.getInterfaceName());
			 if(appsum==null){
				 //不关心减少的
//				 appsum = new AppSummary();
//				 appsum.setOption("sub");
//				 appMap.put(app.getOpsName(), appsum);
				 
			 }else{
				 appsum.setPreCallAllNum(appsum.getPreCallAllNum()+app.getCallAllNum());
				 InterfaceSummary siteApp = appsum.getSiteMap().get(app.getConsumeSiteName());
				 if(siteApp == null){
					 //不关心减少的
//					 siteApp = new AppSummary();
//					 appsum.getSiteMap().put(app.getProvideSiteName(), siteApp);
				 }else{
					 siteApp.setPreCallAllNum(siteApp.getPreCallAllNum()+app.getCallAllNum());
				 }
			
			 }
		 }
		 
		view.addObject("opsName",providename);
		view.addObject("consumeName",consumeName);
		view.addObject("selectDate",selectDate);
		
		view.addObject("interfaceMap",interfaceMap);
		view.addObject("siteSet",siteSet);
		
		view.addObject("preAllSumCall",preAllSumCall+"");
		view.addObject("allSumCall",allSumCall+"");
		return view;
	}	
	
	/**
	 * 展示某个center 应用的consume 对应center应用的机器调用分布
	 * @param opsName
	 * @param interfaceName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showAppConsumeProvideMachine")
	public ModelAndView showAppConsumeProvideMachine(String providename,String consumeName,String selectDate) {
		
		List<HsfProvideMachine>  list = cspAppHsfDependConsumeDao.sumCenterMachineToProvideApp(providename,consumeName,selectDate);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/providerMachineDetail");
		view.addObject("providename", providename);
		view.addObject("consumeName", consumeName);
		view.addObject("list", list);
		return view;
	}	
	
	/**
	 * 展示某个center接口在机器上面的调用量
	 * @param opsName
	 * @param interfaceName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showProvideMachine")
	public ModelAndView showProvideMachine(String opsName,String interfaceName,String selectDate) {
		
		List<HsfProvideMachine>  list = cspAppHsfDependConsumeDao.findHsfConsumeInterfaceMachine(opsName,interfaceName,selectDate);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/providerMachineDetail");
		view.addObject("opsName", opsName);
		view.addObject("interfaceName", MethodUtil.simplifyHsfInterfaceName(interfaceName));
		view.addObject("list", list);
		return view;
	}	
	
	/**
	 * 展示Center应用调用异常，统计总数
	 * @param opsName
	 * @param selectDate
	 * @return
	 */
	@RequestMapping(params = "method=showAppHsfException")
	public ModelAndView showAppHsfException(String opsName, String selectDate ){
		
		List<AppExceptionListPo>appProvderExceptionList = cspAppHsfDependConsumeDao.sumAppConsumerException(opsName, selectDate);
		
		ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/consumerExceptionAll");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("appProvderExceptionList", appProvderExceptionList);
		return view;
	}
	
	/**
	 * 展示Exception详细
	 * @param opsName
	 * @param selectDate
	 * @param customerName
	 * @return
	 */
	@RequestMapping(params = "method=showAppHsfExceptionDetail")
	public ModelAndView showAppHsfExceptionDetail(String opsName, String selectDate, String customerName){
		List<AppExceptionListPo>appConsumerExceptionList = cspAppHsfDependConsumeDao.sumAppConsumerExceptionDetail(opsName, selectDate, customerName);
		ModelAndView view = new ModelAndView("/depend/appinfo/hsf/consume/consumerExceptionDetail");
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("customerName", customerName);
		view.addObject("appConsumerExceptionList", appConsumerExceptionList);
		return view;
	}	
	
	@RequestMapping(params = "method=showConsumeAppHistoryGraph")
	public ModelAndView showConsumeAppHistoryGraph(String startDate, String endDate, String sourceAppName, String targetAppName) {
	  ModelAndView view = new ModelAndView("/depend/appinfo/hsf/report/apphistorygraph");

	  //时间不合法则默认显示昨天和昨天的NUMBER_OF_DAY_PRE天前
	  if(endDate == null) {
	    endDate = MethodUtil.getStringOfDate(MethodUtil.getYestoday());  
	  }
	  if(startDate == null) {
	    startDate = MethodUtil.getStringOfDate(MethodUtil.getDaysBefore(MethodUtil.getDate(endDate), ConstantParameters.NUMBER_OF_DAY_PRE));  
	  }
	  
	  view.addObject("startDate", startDate);
	  view.addObject("endDate", endDate);
	  view.addObject("sourceAppName", sourceAppName);
	  view.addObject("targetAppName", targetAppName);
	  view.addObject("type", "consume");
	  return view;
	}
	
	//显示target应用的走势
	@RequestMapping(params = "method=getAppConsumeHistoryGraphData")
	public void getAppConsumeHistoryGraphData(HttpServletResponse response, String startDate, String endDate, String sourceAppName, String targetAppName) {
	  List<AppProviderSummary> list = cspAppHsfDependConsumeDao.getAppHistoryCall(sourceAppName, targetAppName, startDate, endDate);
	  AmlineFlash pvamline = new AmlineFlash();
	  for(AppProviderSummary po: list) {
	    pvamline.addValue("callnum", po.getCollectDate().getTime(), po.getCallAllNum());
	  }
	  try {
	    writeJSONToResponse(response, pvamline.getAmline());
	  } catch (IOException e) {
	    logger.error("", e);
	  }
	}
	
	//显示总应用的走势
  @RequestMapping(params = "method=getAppConsumeHistoryGraphDataMain")
  public void getAppConsumeHistoryGraphDataMain(HttpServletResponse response, String startDate, String endDate, String sourceAppName) {
    AppInfoPo info = AppInfoAo.get().getAppInfoByAppName(sourceAppName);
    List<HistoryGraphPo> list = monitorDayDao.getAppHistoryCallConsumer(startDate, endDate, info.getAppId(), null);    
    AmlineFlash pvamline = new AmlineFlash();
    for(HistoryGraphPo po: list) {
      pvamline.addValue("callnum", po.getCollectDate().getTime(), po.getCallAllNum());
    }
    try {
      writeJSONToResponse(response, pvamline.getAmline());
    } catch (IOException e) {
      logger.error("", e);
    }
  }	
  
  @RequestMapping(params = "method=showInterfaceHistoryGraph")
  public ModelAndView showInterfaceHistoryGraph(String startDate, String endDate, String sourceAppName, String interfaceName) {
    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/report/interfacehistorygraph");

    //时间不合法则默认显示昨天和昨天的NUMBER_OF_DAY_PRE天前
    if(endDate == null) {
      endDate = MethodUtil.getStringOfDate(MethodUtil.getYestoday());  
    }
    if(startDate == null) {
      startDate = MethodUtil.getStringOfDate(MethodUtil.getDaysBefore(MethodUtil.getDate(endDate), ConstantParameters.NUMBER_OF_DAY_PRE));  
    }
    if(interfaceName != null) {//: 号必须转换，否则amchart加载数据有bug，猜测是json的
      interfaceName = interfaceName.replaceAll(":", "!");
    }
    view.addObject("startDate", startDate);
    view.addObject("endDate", endDate);
    view.addObject("sourceAppName", sourceAppName);
    view.addObject("interfaceName", interfaceName);
    view.addObject("type", "consume");
    return view;
  }
  
  //显示Interface应用的走势
  @RequestMapping(params = "method=getInterfaceHistoryGraphData")
  public void getInterfaceHistoryGraphData(HttpServletResponse response, String startDate, String endDate, String sourceAppName, String interfaceName) {
    if(interfaceName != null) { 
      interfaceName = interfaceName.replaceAll("!", ":");
    }
    AppInfoPo info = AppInfoAo.get().getAppInfoByAppName(sourceAppName);
    List<HistoryGraphPo> list = monitorDayDao.getAppHistoryCallConsumer(startDate, endDate, info.getAppId(), interfaceName);        
    AmlineFlash pvamline = new AmlineFlash();
    for(HistoryGraphPo po: list) {
      pvamline.addValue("callnum", po.getCollectDate().getTime(), po.getCallAllNum());
    }
    try {
      writeJSONToResponse(response, pvamline.getAmline());
    } catch (IOException e) {
      logger.error("", e);
    }
  }
}
