package com.taobao.csp.depend.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.ao.UrlAo;
import com.taobao.csp.depend.dao.UrlDao;
import com.taobao.csp.depend.po.AmlineFlash;
import com.taobao.csp.depend.po.GraphData;
import com.taobao.csp.depend.po.url.RequestUrlSummary;
import com.taobao.csp.depend.po.url.UrlOriginSummary;
import com.taobao.csp.depend.po.url.UrlUv;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.util.page.Pagination;

/**
 * 从URL来源维度统计
 * @author zhongting.zy
 *
 */
@Controller
@RequestMapping("/show/urlorigin.do")
public class DependUrlOriginAction extends BaseAction implements ConstantParameters {

  private static final Logger logger =  Logger.getLogger(DependUrlOriginAction.class);

  @Resource(name = "urlDao")
  private UrlDao urlDao;	

  public static String TYPE_ORIGIN = "origin";
  public static String TYPE_REQUEST = "request";

  private static final int PAGE_SIZE = 50;
  
  /**
   * URL首页进入的Action
   */
  @RequestMapping(params = "method=showUrlOriginMain")
  public ModelAndView showUrlOriginMain(String opsName,String selectDate ) {
    //校验日期
    Date now = MethodUtil.getDate(selectDate);
    selectDate = MethodUtil.getStringOfDate(now);
    String preDate = MethodUtil.getStringOfDate(MethodUtil.getPreDate(selectDate)); 

    List<UrlOriginSummary> originList = urlDao.findOriginList(opsName, selectDate);
    List<RequestUrlSummary> requestList = urlDao.findRequestList(opsName, selectDate);

    List<UrlOriginSummary> originPreList = urlDao.findOriginList(opsName, preDate);
    List<RequestUrlSummary> requestPreList = urlDao.findRequestList(opsName, preDate);

    Map<String, UrlOriginSummary> originMap = new HashMap<String, UrlOriginSummary>();
    Map<String, RequestUrlSummary> requestMap = new HashMap<String, RequestUrlSummary>();

    long originTotal = insertUrlListToMap(originList, originMap, TYPE_ORIGIN);
    long requestTotal = insertUrlListToMap(requestList, requestMap, TYPE_REQUEST);

    //对比历史的数据
    long originPreTotal = compareUrlDataWithPre(originPreList, originMap, TYPE_ORIGIN);
    long requestPreTotal = compareUrlDataWithPre(requestPreList, requestMap, TYPE_REQUEST);

    List<UrlOriginSummary> urlOriginList = MethodUtil.getSortedList(originMap.values());
    List<RequestUrlSummary> urlRequestList = MethodUtil.getSortedList(requestMap.values());

    ModelAndView view = new ModelAndView("/depend/appinfo/url/origin/urlmainpage");
    view.addObject("opsName", opsName);
    view.addObject("selectDate", selectDate);
    view.addObject("urlOriginList", urlOriginList);
    view.addObject("urlRequestList", urlRequestList);
    view.addObject("originTotal", originTotal + "");
    view.addObject("requestTotal", requestTotal + "");
    view.addObject("originPreTotal", originPreTotal + "");
    view.addObject("requestPreTotal", requestPreTotal + "");
    return view;
  }

  /**
   * 显示全部请求的URL信息
   * @return
   */
  @RequestMapping(params = "method=showOriginUrlAll")
  public ModelAndView showOriginUrlAll(String opsName,String selectDate ) {
    //校验日期
    Date now = MethodUtil.getDate(selectDate);
    selectDate = MethodUtil.getStringOfDate(now);
    String preDate = MethodUtil.getStringOfDate(MethodUtil.getPreDate(selectDate)); 

    List<UrlOriginSummary> originList = urlDao.findOriginList(opsName, selectDate);
    List<UrlOriginSummary> originPreList = urlDao.findOriginList(opsName, preDate);
    Map<String, UrlOriginSummary> originMap = new HashMap<String, UrlOriginSummary>();
    long originTotal = insertUrlListToMap(originList, originMap, TYPE_ORIGIN);
    //对比历史的数据
    long originPreTotal = compareUrlDataWithPre(originPreList, originMap, TYPE_ORIGIN);
    List<UrlOriginSummary> urlOriginList = MethodUtil.getSortedList(originMap.values());

    ModelAndView view = new ModelAndView("/depend/appinfo/url/origin/originurlall");
    view.addObject("opsName", opsName);
    view.addObject("selectDate", selectDate);
    view.addObject("urlOriginList", urlOriginList);
    view.addObject("originTotal", originTotal + "");
    view.addObject("originPreTotal", originPreTotal + "");
    return view;	
  }

  /**
   * @return
   */
  @RequestMapping(params = "method=showRequestUrlAll")
  public ModelAndView showRequestUrlAll(String opsName,String selectDate) {
    //校验日期
    Date now = MethodUtil.getDate(selectDate);
    selectDate = MethodUtil.getStringOfDate(now);
    String preDate = MethodUtil.getStringOfDate(MethodUtil.getPreDate(selectDate)); 

    List<RequestUrlSummary> requestList = urlDao.findRequestList(opsName, selectDate);
    List<RequestUrlSummary> requestPreList = urlDao.findRequestList(opsName, preDate);
    Map<String, RequestUrlSummary> requestMap = new HashMap<String, RequestUrlSummary>();
    long requestTotal = insertUrlListToMap(requestList, requestMap, TYPE_REQUEST);

    //对比历史的数据
    long requestPreTotal = compareUrlDataWithPre(requestPreList, requestMap, TYPE_REQUEST);
    List<RequestUrlSummary> urlRequestList = MethodUtil.getSortedList(requestMap.values());

    ModelAndView view = new ModelAndView("/depend/appinfo/url/origin/requesturlall");
    view.addObject("opsName", opsName);
    view.addObject("selectDate", selectDate);
    view.addObject("urlRequestList", urlRequestList);
    view.addObject("requestTotal", requestTotal + "");
    view.addObject("requestPreTotal", requestPreTotal + "");
    return view;
  }	


  @SuppressWarnings("unchecked")  
  private long insertUrlListToMap(List datalist, Map datamap, String type) { 
    long total = 0;
    if(type.equals(TYPE_ORIGIN)) {
      List<UrlOriginSummary> originList = (List<UrlOriginSummary>)datalist;
      Map<String, UrlOriginSummary> originMap = (Map<String, UrlOriginSummary>)datamap;
      for(UrlOriginSummary origin: originList) {
        if(origin.getOriginUrl() != null) { 
          total += origin.getOriginUrlNum();
          if(originMap.containsKey(origin.getOriginUrl())) {
            origin.setOriginUrlNum(origin.getOriginUrlNum() + originMap.get(origin.getOriginUrl()).getOriginUrlNum());
          } 
          originMap.put(origin.getOriginUrl(), origin);         
        }
      }
    } else if(type.equals(TYPE_REQUEST)) {
      List<RequestUrlSummary> requestList = (List<RequestUrlSummary>)datalist;
      Map<String, RequestUrlSummary> requestMap = (Map<String, RequestUrlSummary>)datamap;
      for(RequestUrlSummary request: requestList) {
        if(request.getRequestUrl() != null) {
          total += request.getRequestNum();
          if(requestMap.containsKey(request.getRequestUrl())) {
            request.setRequestNum(request.getRequestNum() + requestMap.get(request.getRequestUrl()).getRequestNum());
          } 
          requestMap.put(request.getRequestUrl(), request);
        }
      }
    } else {
      logger.error("参数错误！");
      assert false;
    }
    return total;
  }

  @SuppressWarnings("unchecked")  
  private long compareUrlDataWithPre(List datalist, Map datamap, String type) { 
    long total = 0;
    if(type.equals(TYPE_ORIGIN)) {
      List<UrlOriginSummary> originPreList = (List<UrlOriginSummary>)datalist;
      Map<String, UrlOriginSummary> originMap = (Map<String, UrlOriginSummary>)datamap;
      for(UrlOriginSummary originPre: originPreList) {
        if(originPre.getOriginUrl() != null) {
          total += originPre.getOriginUrlNum();
          if(originMap.containsKey(originPre.getOriginUrl())) {
            UrlOriginSummary origin = originMap.get(originPre.getOriginUrl());
            origin.setPreOriginUrlNum(origin.getPreOriginUrlNum() + originPre.getOriginUrlNum());
          }
        }
      }
    } else if(type.equals(TYPE_REQUEST)) {
      List<RequestUrlSummary> requestPreList = (List<RequestUrlSummary>)datalist;
      Map<String, RequestUrlSummary> requestMap = (Map<String, RequestUrlSummary>)datamap;
      for(RequestUrlSummary requestPre: requestPreList) {
        if(requestPre.getRequestUrl() != null) {
          total += requestPre.getRequestNum();
          if(requestMap.containsKey(requestPre.getRequestUrl())) {
            RequestUrlSummary request = requestMap.get(requestPre.getRequestUrl());
            request.setPreRequestUrlNum(request.getPreRequestUrlNum() + requestPre.getRequestNum());
          }
        }
      }
    } else {
      logger.error("参数错误！");
      assert false;
    }
    return total;
  } 
  
  @RequestMapping(params = "method=showUrlHistoryGraph")
  public ModelAndView showUrlHistoryGraph(String type, String startDate, String endDate, String opsName, String url) {
    ModelAndView view = new ModelAndView("/depend/appinfo/url/report/urlhistorygraph");

    //时间不合法则默认显示昨天和昨天的NUMBER_OF_DAY_PRE天前
    if(endDate == null) {
      endDate = MethodUtil.getStringOfDate(MethodUtil.getYestoday());  
    }
    if(startDate == null) {
      startDate = MethodUtil.getStringOfDate(MethodUtil.getDaysBefore(MethodUtil.getDate(endDate), ConstantParameters.NUMBER_OF_DAY_PRE));  
    }
    if(type == null) {
      type = "origin";
    }
    if(url != null) { //参数转换，amchart在url含有":"的时候，加载有问题
      url = url.replace(":", "!");
    }
    view.addObject("startDate", startDate);
    view.addObject("endDate", endDate);
    view.addObject("opsName", opsName);
    view.addObject("url", url);
    view.addObject("type", type);
    return view;
  }
  
  @RequestMapping(params = "method=getUrlOriginHistoryGraphData")
  public void getUrlOriginHistoryGraphData(HttpServletResponse response, String startDate, String endDate, String opsName, String url) {
    
    if(url != null) { //参数转换，amchart在url含有":"的时候，加载有问题
      url = url.replace("!", ":");
    }
    
    List<GraphData> list = urlDao.findOriginListHistory(startDate, endDate, opsName, url);
    AmlineFlash pvamline = new AmlineFlash();
    for(GraphData po: list) {
      pvamline.addValue("callnum", po.getCollectDate().getTime(), po.getCallAllNum());
    }
    try {
      writeJSONToResponse(response, pvamline.getAmline());
    } catch (IOException e) {
      logger.error("", e);
    }
  }
  
  @RequestMapping(params = "method=getUrlOriginTotalHistoryGraphData")
  public void getUrlOriginTotalHistoryGraphData(HttpServletResponse response, String startDate, String endDate, String opsName) {
    
    List<GraphData> list = urlDao.findOriginListHistory(startDate, endDate, opsName, null);
    AmlineFlash pvamline = new AmlineFlash();
    for(GraphData po: list) {
      pvamline.addValue("callnum", po.getCollectDate().getTime(), po.getCallAllNum());
    }
    try {
      writeJSONToResponse(response, pvamline.getAmline());
    } catch (IOException e) {
      logger.error("", e);
    }
  }
  
  @RequestMapping(params = "method=getUrlRequestHistoryData")
  public void getUrlRequestHistoryData(HttpServletResponse response, String startDate, String endDate, String opsName, String url) {
    
    if(url != null) { //参数转换，amchart在url含有":"的时候，加载有问题
      url = url.replace("!", ":");
    }
    
    List<GraphData> list = urlDao.findRequestListHistory(startDate, endDate, opsName, url);
    AmlineFlash pvamline = new AmlineFlash();
    for(GraphData po: list) {
      pvamline.addValue("callnum", po.getCollectDate().getTime(), po.getCallAllNum());
    }
    try {
      writeJSONToResponse(response, pvamline.getAmline());
    } catch (IOException e) {
      logger.error("", e);
    }
  } 
  
  @RequestMapping(params = "method=getUrlRequestTotalHistoryData")
  public void getUrlRequestTotalHistoryData(HttpServletResponse response, String startDate, String endDate, String opsName) {
    List<GraphData> list = urlDao.findRequestListHistory(startDate, endDate, opsName, null);
    AmlineFlash pvamline = new AmlineFlash();
    for(GraphData po: list) {
      pvamline.addValue("callnum", po.getCollectDate().getTime(), po.getCallAllNum());
    }
    try {
      writeJSONToResponse(response, pvamline.getAmline());
    } catch (IOException e) {
      logger.error("", e);
    }
  } 
  
  //search domain url
  @RequestMapping(params = "method=queryDomainAndUrl")
  public ModelAndView queryDomainAndUrl(String queryValue, String type, String startDate, 
      String pageNo, String pageSize) {
    
    if(queryValue == null)
      queryValue = "";
    else 
      queryValue = queryValue.trim();
    
    if(type == null)
      type = "url"; // url or domain
    else 
      type = type.trim();
    if(startDate == null)
      startDate = MethodUtil.getStringOfDate(MethodUtil.getDaysBefore(new Date(), 3));
    
    int iPageNo = 1;
    int iPageSize = PAGE_SIZE;
//    String pageNoParam = request.getParameter("pageNo");
//    String pageSizeParam = request.getParameter("pageSize"); 
    
    
    if (pageNo != null) {
      try {
        iPageNo = Integer.parseInt(pageNo);
      } catch (NumberFormatException e) {
        logger.error("", e);
      }
    }
    if (pageSize != null) {
      if (pageSize.trim().equals(""))
        iPageSize = PAGE_SIZE;
      try {
        iPageSize = Integer.parseInt(pageSize);
      } catch (NumberFormatException e) {
        iPageSize = PAGE_SIZE;
        logger.error("", e);;
      }
    }
//    int iTotal = 0;
//    if(total == null || total.trim().equals("") || total.trim().equals("0")) {
//      iTotal = (int) UrlAo.get().getTotalForUrlUv(startDate, type, queryValue); //可能缺失精度
//    } else {
//      iTotal = Integer.parseInt(total);
//    }
    
    Pagination<UrlUv> page = UrlAo.get().queryUrlUv(startDate, type, queryValue, iPageNo, iPageSize);
    
    ModelAndView view = new ModelAndView("depend/appinfo/url/domian_url");
    view.addObject("pagination", page);
    view.addObject("queryValue", queryValue);
    view.addObject("type", type);
    view.addObject("startDate", startDate);
    view.addObject("pageNo", iPageNo + "");
    view.addObject("pageSize", pageSize + "");
    return view;
  }

  
  
}