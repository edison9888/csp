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

import com.taobao.csp.depend.dao.CspAppHsfDependProvideDao;
import com.taobao.csp.depend.dao.MonitorDayDao;
import com.taobao.csp.depend.po.AmlineFlash;
import com.taobao.csp.depend.po.HistoryGraphPo;
import com.taobao.csp.depend.po.hsf.AppConsumerSummary;
import com.taobao.csp.depend.po.hsf.AppExceptionListPo;
import com.taobao.csp.depend.po.hsf.AppProvderInterfaceSummary;
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
 * 这个action 主要是从 hsf center的维度来统计相关的信息
 * @author xiaodu
 *
 */
@Controller
@RequestMapping("/show/hsfprovide.do")
public class DependHsfProvideDataAction extends BaseAction{

  @Resource(name = "cspAppHsfDependProvideDao")
  private CspAppHsfDependProvideDao cspAppHsfDependProvideDao;
  
  @Resource(name = "monitorDayDao")
  MonitorDayDao monitorDayDao;

  /**
   * 展示center 应用的概括信息
   * @param opsName
   * @param selectDate
   * @return
   */
  @RequestMapping(params = "method=showAppCenterHsfInfo")
  public ModelAndView showAppCenterHsfInfo(String opsName,String selectDate ){

    //校验日期
    Date now = MethodUtil.getDate(selectDate);
    selectDate = MethodUtil.getStringOfDate(now);

    List<AppConsumerSummary> appConsumerList = cspAppHsfDependProvideDao.sumCenterAppHsfToAppConsumer(opsName, selectDate);

    if(appConsumerList.size() ==0){

      ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/noMessage");
      view.addObject("opsName", opsName);
      view.addObject("selectDate", selectDate);
      return view;
    }



    List<AppProvderInterfaceSummary>appProvderInterfaceList = cspAppHsfDependProvideDao.sumAppProviderInterfaceToCenterApp(opsName, selectDate);

    Date predate = MethodUtil.getPreDate(selectDate);

    List<AppConsumerSummary> preAppConsumerList = cspAppHsfDependProvideDao.sumCenterAppHsfToAppConsumer(opsName, MethodUtil.getStringOfDate(predate));
    List<AppProvderInterfaceSummary> preAppProvderInterfaceList = cspAppHsfDependProvideDao.sumAppProviderInterfaceToCenterApp(opsName, MethodUtil.getStringOfDate(predate));

    long allCallNum = 0;
    long preAllCallNum = 0;
    Set<String> apps = new HashSet<String>();
    for(AppConsumerSummary app:appConsumerList){
      apps.add(app.getOpsName());
      allCallNum+=app.getCallAllNum();
    }

    Set<String> preApps = new HashSet<String>();
    for(AppConsumerSummary app:preAppConsumerList){
      preApps.add(app.getOpsName());
      preAllCallNum+=app.getCallAllNum();
    }


    Set<String> interfaces = new HashSet<String>();
    for(AppProvderInterfaceSummary app:appProvderInterfaceList){
      interfaces.add(app.getInterfaceName());
    }

    Set<String> preInterfaces = new HashSet<String>();
    for(AppProvderInterfaceSummary app:preAppProvderInterfaceList){
      preInterfaces.add(app.getInterfaceName());
    }

    Map<String,AppSummary> appSummaryMap = comSummary(appConsumerList,preAppConsumerList);
    Map<String,ProvideSiteRating> provideSiteRatingMap = comSiteRating(appConsumerList,preAppConsumerList);
    Map<String,InterfaceSummary> interfaceMap  = comInterface(appProvderInterfaceList,preAppProvderInterfaceList);


    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/appCenterHsfInfo");
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
   * @return
   */
  private Map<String,InterfaceSummary> comInterface(List<AppProvderInterfaceSummary> appConsumerList,List<AppProvderInterfaceSummary> preAppConsumerList){

    Map<String,InterfaceSummary> map = new HashMap<String, InterfaceSummary>();

    for(AppProvderInterfaceSummary app:appConsumerList){
      InterfaceSummary sum = map.get(app.getInterfaceName());
      if(sum == null){
        sum = new InterfaceSummary();
        sum.setName(app.getInterfaceName());
        sum.setKeyName(MethodUtil.simplifyHsfInterfaceName(app.getInterfaceName()));
        map.put(app.getInterfaceName(), sum);
      }
      sum.setCallAllNum(sum.getCallAllNum()+app.getCallAllNum());
    }

    Map<String,InterfaceSummary> preMap = new HashMap<String, InterfaceSummary>();

    for(AppProvderInterfaceSummary app:preAppConsumerList){
      InterfaceSummary sum = preMap.get(app.getInterfaceName());
      if(sum == null){
        sum = new InterfaceSummary();
        sum.setName(app.getInterfaceName());
        sum.setKeyName(MethodUtil.simplifyHsfInterfaceName(app.getInterfaceName()));
        preMap.put(app.getInterfaceName(), sum);
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
  private Map<String,ProvideSiteRating> comSiteRating(List<AppConsumerSummary> appConsumerList,List<AppConsumerSummary> preAppConsumerList){

    Map<String,ProvideSiteRating> map = new HashMap<String, ProvideSiteRating>();

    for(AppConsumerSummary app:appConsumerList){
      ProvideSiteRating sum = map.get(app.getProvideSiteName());
      if(sum == null){
        sum = new ProvideSiteRating();
        sum.setSiteName(app.getProvideSiteName());
        map.put(app.getProvideSiteName(), sum);
      }
      sum.setCallAllNum(sum.getCallAllNum()+app.getCallAllNum());
    }

    Map<String,ProvideSiteRating> preMap = new HashMap<String, ProvideSiteRating>();

    for(AppConsumerSummary app:preAppConsumerList){
      ProvideSiteRating sum = preMap.get(app.getProvideSiteName());
      if(sum == null){
        sum = new ProvideSiteRating();
        sum.setSiteName(app.getProvideSiteName());
        preMap.put(app.getProvideSiteName(), sum);
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
    //		for(Map.Entry<String,ProvideSiteRating> entry:preMap.entrySet()){
    //			if(map.get(entry.getKey())==null){
    //				entry.getValue().setOption(ConstantParameters.CONTROST_SUB);
    //				map.put(entry.getKey(), entry.getValue());
    //			}else{
    //				entry.getValue().setPreCallAllNum(preMap.get(entry.getKey()).getPreCallAllNum());
    //			}
    //		}

    return map;

  }

  /**
   * 各个应用的总调用量和历史调用量
   * @param appConsumerList
   * @param preAppConsumerList
   * @return
   */
  private  Map<String,AppSummary>  comSummary(List<AppConsumerSummary> appConsumerList,List<AppConsumerSummary> preAppConsumerList){

    Map<String,AppSummary> map = new HashMap<String, AppSummary>();

    for(AppConsumerSummary app:appConsumerList){
      AppSummary sum = map.get(app.getOpsName());
      if(sum == null){
        sum = new AppSummary();
        sum.setOpsName(app.getOpsName());
        map.put(app.getOpsName(), sum);
      }
      sum.setCallAllNum(sum.getCallAllNum()+app.getCallAllNum());
    }

    Map<String,AppSummary> preMap = new HashMap<String, AppSummary>();

    for(AppConsumerSummary app:preAppConsumerList){
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

    //不关心
    for(Map.Entry<String,AppSummary> entry:preMap.entrySet()){
      if(map.get(entry.getKey())==null){
        entry.getValue().setOption("sub");
        map.put(entry.getKey(), entry.getValue());
      }else{
        entry.getValue().setPreCallAllNum(preMap.get(entry.getKey()).getPreCallAllNum());
      }
    }

    return map;
  }


  /**
   * 展示center 应用中的全部接口调用信息
   * @param opsName
   * @param selectDate
   * @return
   */
  @RequestMapping(params = "method=showAppAllCenterInterface")
  public ModelAndView showAppAllCenterInterface(String opsName,String selectDate) {

    List<AppProvderInterfaceSummary>appProvderInterfaceList = cspAppHsfDependProvideDao.sumAppProviderInterfaceToCenterApp(opsName, selectDate);
    Date predate = MethodUtil.getPreDate(selectDate);
    List<AppProvderInterfaceSummary> preAppProvderInterfaceList = cspAppHsfDependProvideDao.sumAppProviderInterfaceToCenterApp(opsName, MethodUtil.getStringOfDate(predate));

    long allCallNum = 0;
    for(AppProvderInterfaceSummary app:appProvderInterfaceList){
      allCallNum+=app.getCallAllNum();
    }


    Map<String,InterfaceSummary> interfaceMap =  comInterface(appProvderInterfaceList,preAppProvderInterfaceList);

    ArrayList<InterfaceSummary> tmp = new ArrayList<InterfaceSummary>();
    tmp.addAll(interfaceMap.values());
    Collections.sort(tmp);


    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/providerInterfaceAll");

    view.addObject("interfaceList",tmp);
    view.addObject("allCallNum",allCallNum+"");
    view.addObject("opsName",opsName);
    view.addObject("selectDate",selectDate);
    return view;
  } 

  /**
   * 展示center应用中的某个接口被 consume 应用调用的详细信息
   * @param opsName
   * @param interfaceName
   * @param selectDate
   * @return
   */
  @RequestMapping(params = "method=showAppHsfInterfaceDetail")
  public ModelAndView showAppHsfInterfaceDetail(String opsName,String interfaceName,String selectDate) {

    List<AppConsumerSummary>  list = cspAppHsfDependProvideDao.sumOneHsfInterfaceToAppConsumer(opsName,interfaceName,selectDate);
    Date predate = MethodUtil.getPreDate(selectDate);
    List<AppConsumerSummary>  prelist = cspAppHsfDependProvideDao.sumOneHsfInterfaceToAppConsumer(opsName,interfaceName, MethodUtil.getStringOfDate(predate));

    Set<String> preAppNameSet = new HashSet<String>();


    Map<String,ProvideSiteRating>siteMap= comSiteRating(list,prelist);

    for(AppConsumerSummary app:prelist){
      preAppNameSet.add(app.getOpsName());
    }

    Set<String> siteSet = new HashSet<String>();

    Map<String,AppSummary> appSumMap = new HashMap<String, AppSummary>();



    long allSumCall = 0;
    for(AppConsumerSummary app:list){

      allSumCall+=app.getCallAllNum();

      siteSet.add(app.getProvideSiteName());
      AppSummary appsum =  appSumMap.get(app.getOpsName());



      if(appsum==null){
        appsum = new AppSummary();

        if(!preAppNameSet.contains(app.getOpsName())){
          appsum.setOption("add"); 
        }
        appsum.setOpsName(app.getOpsName());
        appSumMap.put(app.getOpsName(), appsum);
      }
      appsum.setCallAllNum(appsum.getCallAllNum()+app.getCallAllNum());

      AppSummary siteApp = appsum.getSiteMap().get(app.getProvideSiteName());
      if(siteApp == null){
        siteApp = new AppSummary();
        appsum.getSiteMap().put(app.getProvideSiteName(), siteApp);
      }
      siteApp.setCallAllNum(siteApp.getCallAllNum()+app.getCallAllNum());
    }

    long preAllSumCall = 0;
    for(AppConsumerSummary app:prelist){

      preAllSumCall+=app.getCallAllNum();

      AppSummary appsum =  appSumMap.get(app.getOpsName());
      if(appsum==null){
        //不关心减少的
        //				 appsum = new AppSummary();
        //				 appsum.setOption("sub");
        //				 appMap.put(app.getOpsName(), appsum);

      }else{
        appsum.setPreCallAllNum(appsum.getPreCallAllNum()+app.getCallAllNum());
        AppSummary siteApp = appsum.getSiteMap().get(app.getProvideSiteName());
        if(siteApp == null){
          //不关心减少的
          //					 siteApp = new AppSummary();
          //					 appsum.getSiteMap().put(app.getProvideSiteName(), siteApp);
        }else{
          siteApp.setPreCallAllNum(siteApp.getPreCallAllNum()+app.getCallAllNum());
        }

      }
    }

    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/providerInterfaceDetail");
    view.addObject("appSumMap", appSumMap);
    view.addObject("opsName", opsName);
    view.addObject("interfaceName", MethodUtil.simplifyHsfInterfaceName(interfaceName));
    view.addObject("selectDate", selectDate);
    view.addObject("siteSet", siteSet);
    view.addObject("siteMap", siteMap);
    view.addObject("preAllSumCall", preAllSumCall+"");
    view.addObject("allSumCall", allSumCall+"");

    return view;
  }

  /**
   * 展示某个center 应用中某个接口的在每台机器上得调用量
   * @param opsName
   * @param interfaceName
   * @param selectDate
   * @return
   */
  @RequestMapping(params = "method=showProvideMachine")
  public ModelAndView showProvideMachine(String opsName,String interfaceName,String selectDate) {

    List<HsfProvideMachine>  list = cspAppHsfDependProvideDao.findHsfProvideInterfaceMachine(opsName,interfaceName,selectDate);

    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/providerMachineDetail");
    view.addObject("opsName", opsName);
    view.addObject("interfaceName", MethodUtil.simplifyHsfInterfaceName(interfaceName));
    view.addObject("list", list);
    return view;
  }

  /**
   * 显示center 应用中全部的 consume 应用调用信息
   * @param providename
   * @param selectDate
   * @return
   */
  @RequestMapping(params = "method=showAppConsumeAll")
  public ModelAndView showAppConsumeAll(String opsName,String selectDate) {

    List<AppConsumerSummary>appProvderInterfaceList = cspAppHsfDependProvideDao.sumCenterAppHsfToAppConsumer(opsName, selectDate);
    Date predate = MethodUtil.getPreDate(selectDate);
    List<AppConsumerSummary> preAppProvderInterfaceList = cspAppHsfDependProvideDao.sumCenterAppHsfToAppConsumer(opsName, MethodUtil.getStringOfDate(predate));

    long allCallNum = 0;
    for(AppConsumerSummary app:appProvderInterfaceList){
      allCallNum+=app.getCallAllNum();
    }


    Map<String,AppSummary> interfaceMap =  comSummary(appProvderInterfaceList,preAppProvderInterfaceList);

    ArrayList<AppSummary> tmp = new ArrayList<AppSummary>();
    tmp.addAll(interfaceMap.values());
    Collections.sort(tmp);


    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/appconsumerAll");

    view.addObject("appList",tmp);
    view.addObject("allCallNum",allCallNum+"");
    view.addObject("opsName",opsName);
    view.addObject("selectDate",selectDate);
    return view;
  } 
  /**
   * 统计针对在center 中的consume 应用调用center的具体调用了什么接口，以及调用的数据量信息
   * center 应用的机房被调用量等详细信息
   * @param providename
   * @param consumeName
   * @param selectDate
   * @return
   */
  @RequestMapping(params = "method=showAppConsumeDetail")
  public ModelAndView showAppConsumeDetail(String providename,String consumeName,String selectDate){
    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/appconsumerdetail");


    List<AppProvderInterfaceSummary> appConsume = cspAppHsfDependProvideDao.sumConsumeAppToCenterAppInterface(providename,consumeName,selectDate);
    Date predate = MethodUtil.getPreDate(selectDate);
    List<AppProvderInterfaceSummary> preAppConsume = cspAppHsfDependProvideDao.sumConsumeAppToCenterAppInterface(providename,consumeName,MethodUtil.getStringOfDate(predate));
    Set<String> preAppNameSet = new HashSet<String>();
    for(AppProvderInterfaceSummary app:preAppConsume){
      preAppNameSet.add(app.getInterfaceName());
    }

    Map<String, InterfaceSummary> interfaceMap = new HashMap<String, InterfaceSummary>();

    Set<String> siteSet = new HashSet<String>();

    long allSumCall = 0;
    for(AppProvderInterfaceSummary app:appConsume){

      allSumCall+=app.getCallAllNum();

      siteSet.add(app.getProvideSiteName());
      InterfaceSummary appsum =  interfaceMap.get(app.getInterfaceName());

      if(appsum==null){
        appsum = new InterfaceSummary();

        if(!preAppNameSet.contains(app.getInterfaceName())){
          appsum.setOption("add"); 
        }
        appsum.setKeyName(MethodUtil.simplifyHsfInterfaceName(app.getInterfaceName()));
        interfaceMap.put(app.getInterfaceName(), appsum);
      }
      appsum.setCallAllNum(appsum.getCallAllNum()+app.getCallAllNum());

      InterfaceSummary siteApp = appsum.getSiteMap().get(app.getProvideSiteName());
      if(siteApp == null){
        siteApp = new InterfaceSummary();
        appsum.getSiteMap().put(app.getProvideSiteName(), siteApp);
      }
      siteApp.setCallAllNum(siteApp.getCallAllNum()+app.getCallAllNum());
    }

    long preAllSumCall = 0;
    for(AppProvderInterfaceSummary app:preAppConsume){

      preAllSumCall+=app.getCallAllNum();

      InterfaceSummary appsum =  interfaceMap.get(app.getInterfaceName());
      if(appsum==null){
        //不关心减少的
        //				 appsum = new AppSummary();
        //				 appsum.setOption("sub");
        //				 appMap.put(app.getOpsName(), appsum);

      }else{
        appsum.setPreCallAllNum(appsum.getPreCallAllNum()+app.getCallAllNum());
        InterfaceSummary siteApp = appsum.getSiteMap().get(app.getProvideSiteName());
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

    List<HsfProvideMachine>  list = cspAppHsfDependProvideDao.sumConsumeAppToCenterMachine(providename,consumeName,selectDate);

    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/providerMachineDetail");
    view.addObject("providename", providename);
    view.addObject("consumeName", consumeName);
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

    List<AppExceptionListPo>appProvderExceptionList = cspAppHsfDependProvideDao.sumAppProviderException(opsName, selectDate);

    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/providerExceptionAll");
    view.addObject("opsName", opsName);
    view.addObject("selectDate", selectDate);
    view.addObject("appProvderExceptionList", appProvderExceptionList);
    return view;
  }

  /**
   * 展示Center调用详细
   * @param opsName
   * @param selectDate
   * @param customerName
   * @return
   */
  @RequestMapping(params = "method=showAppHsfExceptionDetail")
  public ModelAndView showAppHsfExceptionDetail(String opsName, String selectDate, String customerName){


    List<AppExceptionListPo>appProvderExceptionList = cspAppHsfDependProvideDao.sumAppProviderExceptionDetail(opsName, selectDate, customerName);

    ModelAndView view = new ModelAndView("/depend/appinfo/hsf/provide/providerExceptionDetail");
    view.addObject("opsName", opsName);
    view.addObject("selectDate", selectDate);
    view.addObject("customerName", customerName);
    view.addObject("appProvderExceptionList", appProvderExceptionList);
    return view;
  }

  @RequestMapping(params = "method=showProvideAppHistoryGraph")
  public ModelAndView showProvideAppHistoryGraph(String startDate, String endDate, String sourceAppName, String targetAppName) {
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
    view.addObject("type", "provide");
    return view;
  }

  //显示target应用的走势
  @RequestMapping(params = "method=getAppProvideHistoryGraphData")
  public void getAppProvideHistoryGraphData(HttpServletResponse response, String startDate, String endDate, String sourceAppName, String targetAppName) {
    List<AppProviderSummary> list = cspAppHsfDependProvideDao.getAppHistoryCall(sourceAppName, targetAppName, startDate, endDate);
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
  @RequestMapping(params = "method=getAppProvideHistoryGraphDataMain")
  public void getAppProvideHistoryGraphDataMain(HttpServletResponse response, String startDate, String endDate, String sourceAppName) {
    AppInfoPo info = AppInfoAo.get().getAppInfoByAppName(sourceAppName);
    List<HistoryGraphPo> list = monitorDayDao.getAppHistoryCallProvider(startDate, endDate, info.getAppId(), null);
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
    
    if(interfaceName != null) {
      interfaceName = interfaceName.replaceAll(":", "!");
    }
    
    view.addObject("startDate", startDate);
    view.addObject("endDate", endDate);
    view.addObject("sourceAppName", sourceAppName);
    view.addObject("interfaceName", interfaceName);
    view.addObject("type", "provide");
    return view;
  }

  //显示Interface的走势
  @RequestMapping(params = "method=getInterfaceHistoryGraphData")
  public void getInterfaceHistoryGraphData(HttpServletResponse response, String startDate, String endDate, String sourceAppName, String interfaceName) {
    if(interfaceName != null) {
      interfaceName = interfaceName.replaceAll("!", ":");
    }
    AppInfoPo info = AppInfoAo.get().getAppInfoByAppName(sourceAppName);
    List<HistoryGraphPo> list = monitorDayDao.getAppHistoryCallProvider(startDate, endDate, info.getAppId(), interfaceName);
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
