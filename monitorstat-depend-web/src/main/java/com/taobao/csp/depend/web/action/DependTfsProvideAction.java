package com.taobao.csp.depend.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.dao.CspTfsProviderDao;
import com.taobao.csp.depend.po.tfs.CspTfsProviderDetailPo;
import com.taobao.csp.depend.po.tfs.TfsProviderWebPo;
import com.taobao.csp.depend.util.MethodUtil;

@Controller
@RequestMapping("/show/tfsprovide.do")
public class DependTfsProvideAction {
  @Resource(name = "cspTfsProviderDao")
  private CspTfsProviderDao cspTfsProviderDao;
  
  /**
   * 展示center 应用的概括信息
   * @param opsName
   * @param selectDate
   * @return
   */
  @RequestMapping(params = "method=showTfsMain")
  public ModelAndView showTfsMain(String selectDate) {

    // 校验日期
    Date now = MethodUtil.getDate(selectDate);
    selectDate = MethodUtil.getStringOfDate(now);

    List<CspTfsProviderDetailPo> list = cspTfsProviderDao
        .getTfsListByDate(selectDate);

    if (list.size() == 0) {
      ModelAndView view = new ModelAndView(
          "/depend/appinfo/tfs/provide/noMessage");
      view.addObject("selectDate", selectDate);
      return view;
    }

    Date predate = MethodUtil.getPreDate(selectDate);
    List<CspTfsProviderDetailPo> preList = cspTfsProviderDao
        .getTfsListByDate(MethodUtil.getStringOfDate(predate));

    //记录read，write，unlink三个类型的请求数比例
    String[] typeArray = new String[]{"read","write","unlink"};
    Map<String, Long> typeMapNormal = new HashMap<String, Long>(); // 平均信息
    Map<String, Long> typeMapRush = new HashMap<String, Long>(); // 高峰请求信息

    Map<String, Long> preTypeMapNormal = new HashMap<String, Long>(); // 平均信息
    Map<String, Long> preTypeMapRush = new HashMap<String, Long>(); // 高峰请求信

    for (String type : typeArray) {
      typeMapNormal.put(type, new Long(0));
      typeMapRush.put(type, new Long(0));
      preTypeMapNormal.put(type, new Long(0));
      preTypeMapRush.put(type, new Long(0));
    }

    Map<String, TfsProviderWebPo> curMap = buildTfsData(list, typeMapNormal,
        typeMapRush);
    Map<String, TfsProviderWebPo> preMap = buildTfsData(preList,
        preTypeMapNormal, preTypeMapRush);

    // System.out.println(curMap);
    // System.out.println(preMap);

    // for (String appName : curMap.keySet()) {
    // System.out.println(appName);
    // TfsProviderWebPo po = curMap.get(appName);
    // System.out.println(po.rushMap);
    // System.out.println(po.norMap);
    // }

    ModelAndView view = new ModelAndView(
        "/depend/appinfo/tfs/provide/tfsprovideInfo");
    view.addObject("curMap", curMap);
    view.addObject("preMap", preMap);
    view.addObject("typeMapNormal", typeMapNormal);
    view.addObject("typeMapRush", typeMapRush);
    view.addObject("preTypeMapNormal", preTypeMapNormal);
    view.addObject("preTypeMapRush", preTypeMapRush);
    view.addObject("selectDate", selectDate);
    return view;
  }
  
  /**
   * 处理汇总数据
   * 
   * @param list
   * @param typeMap
   * @return appName,TfsProviderWebPo 的map
   */
  private Map<String, TfsProviderWebPo> buildTfsData(
      List<CspTfsProviderDetailPo> list, Map<String, Long> typeMapNormal,
      Map<String, Long> typeMapRush) {
    Map<String, TfsProviderWebPo> map = new HashMap<String, TfsProviderWebPo>();
    
    for (CspTfsProviderDetailPo po : list) { // 不做去重判断了，交给数据采集方处理
      String appName = po.getAppName();
      String operType  = po.getOperType();
      if (!map.containsKey(appName)) {
        TfsProviderWebPo webPo = new TfsProviderWebPo();
        webPo.setAppName(appName);
        map.put(appName, webPo);
      }

      Map<String, String> curValueMap = new HashMap<String, String>();
      curValueMap.put("oper_times", po.getOperTimes() + "");
      curValueMap.put("oper_size", po.getOperSize() + "");
      curValueMap.put("oper_rt", po.getOperRt() + "");
      curValueMap.put("oper_succ", po.getOperSucc() + "");
      curValueMap.put("cache_hit_ratio", po.getCacheHitRatio() + "");

      Map<String, String> rushMap = new HashMap<String, String>();
      rushMap.put("oper_times", po.getRushOperTimes() + "");
      rushMap.put("oper_size", po.getRushOperSize() + "");
      rushMap.put("oper_rt", po.getRushOperRt() + "");
      rushMap.put("oper_succ", po.getRushOperSucc() + "");
      rushMap.put("cache_hit_ratio", po.getRushcacheHitRatio() + "");

      TfsProviderWebPo curPo = map.get(appName);
      curPo.norMap.put(operType, curValueMap);
      curPo.rushMap.put(operType, rushMap);
      
      long operTimes = po.getOperTimes();
      long rushOperTimes = po.getRushOperTimes();

      if (typeMapNormal != null && typeMapRush != null) {
        typeMapNormal.put(operType, typeMapNormal.get(operType) + operTimes);
        typeMapRush.put(operType, typeMapRush.get(operType) + rushOperTimes);        
      }
    }
    return map;
  }

  public static void main(String[] args) {
    new DependTfsProvideAction().showTfsMain("2012-02-29");
  }
}
