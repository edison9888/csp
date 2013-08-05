package com.taobao.csp.depend.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.taobao.csp.depend.po.EagleeyeTreeGridData;
import com.taobao.csp.depend.service.DependTreeService;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.EagleeyeDataAo;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.util.Constants;

@Controller
@RequestMapping("/rate/callrate.do")
public class RateAction extends BaseAction {
	public static final Logger logger = Logger.getLogger(RateAction.class);

	//    //返回多条拓扑数据
	//    @RequestMapping(params = "method=getTopoByOriginKey")
	//    public void getTopoByOriginKey(String sourceKey, HttpServletResponse response) {
	////      sourceKey = DependRelationAo.get().changeDependKeyToEagleeyeKey(sourceKey);
	//      List<TreeGridData> rootList = DependRelationAo.get().getMultiRelationBySourceKey(sourceKey);
	//      try {
	//        this.writeJSONToResponseJSONArray(response, rootList);
	//      } catch (IOException e) {
	//        logger.error("", e);
	//      }      
	//    }

	@RequestMapping(params = "method=gotoTopoPage")
	public ModelAndView gotoTopoPage(String sourceKey, String appName,String type, String collectTime) {
		//需要计算的规则,比如初期测试几个重要的URL
		Calendar cal = Calendar.getInstance();
		cal.setTime(MethodUtil.getDate(collectTime));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date collect_timeStart = cal.getTime();

		if(collectTime == null)
			collectTime = MethodUtil.getStringOfDate(MethodUtil.getDate(collectTime));
	
		if(type == null)
			type = Constants.API_CHILD_KEY;
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date collect_timeEnd = cal.getTime();
		
//		AppInfoPo app = AppInfoCache.getAppInfoByAppName(appName);
		Set<String> urlSet = EagleeyeDataAo.get().getDistinctSourceKey(type, appName, collect_timeStart, collect_timeEnd);	
		List<String> urlList = new ArrayList<String>(urlSet);
		  Collections.sort(urlList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		  });
		ModelAndView view = new ModelAndView("/depend/appinfo/rate/appratetable_new");
		view.addObject("sourceKey", sourceKey);
		view.addObject("appName", appName);
		view.addObject("type", type);
		view.addObject("collectTime", collectTime);
		view.addObject("urlList", urlList);
		return view;
	}
	
	@RequestMapping(params = "method=getSourceKeyList")
	public void getSourceKeyList(String appName,String type, String collectTime,HttpServletResponse response) {
		//需要计算的规则,比如初期测试几个重要的URL
		Calendar cal = Calendar.getInstance();
		cal.setTime(MethodUtil.getDate(collectTime));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date collect_timeStart = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date collect_timeEnd = cal.getTime();
		
//		AppInfoPo app = AppInfoCache.getAppInfoByAppName(appName);
		Set<String> urlSet = EagleeyeDataAo.get().getDistinctSourceKey(type, appName, collect_timeStart, collect_timeEnd);	
		List<String> urlList = new ArrayList<String>(urlSet);
		  Collections.sort(urlList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		  });
		  try {
			this.writeJSONToResponseJSONArray(response, urlList);
		} catch (IOException e) {
			logger.info("", e);
		}
	}

	/**
	 * @param sourceKey 
	 * @param appName 
	 * @param type 查上游 or 查下游
	 * @param response
	 */
	@RequestMapping(params = "method=getEagleeyeTopoByType")
	public void getEagleeyeTopoByType(String sourceKey, String appName,String type, String collectTime,HttpServletResponse response) {
		if(type == null)
			type = Constants.API_CHILD_KEY;
		//	List<CspEagleeyeApiRequestPart> part = EagleeyeDataAo.get().searchEagleeyeApiRequestPart(sourceKey,type, new Date(), new Date());
		CspEagleeyeApiRequestDay day = EagleeyeDataAo.get().searchCspEagleeyeApiRequestDay(sourceKey, type, collectTime, appName);
		EagleeyeTreeGridData root = DependTreeService.get().getDependTreeAll(day);
		EagleeyeTreeGridData[] array = new EagleeyeTreeGridData[]{root};
		try {
			String str = JSONArray.toJSONString(array);
			this.writeJSONToResponse(response, str);
		} catch (IOException e) {
			logger.error("", e);
		}
	}



	public static void main(String[] args) {

		String sourceKey = "http://buy.taobao.com/auction/combo/confirm_order.htm";
		String appName = "tf_buy";

		new RateAction().getEagleeyeTopoByType(sourceKey, appName, Constants.API_CHILD_KEY, "", null);
	}
}
