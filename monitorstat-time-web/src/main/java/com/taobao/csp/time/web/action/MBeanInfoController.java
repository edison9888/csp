/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;


/**
 *@author wb-lixing
 *2012-3-28 上午10:00:32
 */
@Controller
@RequestMapping("/app/detail/mbean/show.do")
public class MBeanInfoController extends BaseController {
	
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
	
	/**
	 * 进入首页Mbean
	 *@author xiaodu
	 * @param appId
	 * @return
	 *
	 */
	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(int appId) {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		
		List<String> threadList = commonService.childKeyList( appInfo.getAppName(),KeyConstants.MBEAN + Constants.S_SEPERATOR + KeyConstants.THREAD);
		
		List<String> dataSourceList = commonService.childKeyList( appInfo.getAppName(),KeyConstants.MBEAN + Constants.S_SEPERATOR + KeyConstants.DATASOURCE);

		List<String> threadPoolList = commonService.childKeyList( appInfo.getAppName(),KeyConstants.MBEAN + Constants.S_SEPERATOR + KeyConstants.THREADPOOL);

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		map.put("thread", threadList);

		map.put("threadPool", threadPoolList);

		ModelAndView view = new ModelAndView("/time/mbean/mbean_index");

		view.addObject("appInfo", appInfo);
		
		view.addObject("dataSource", dataSourceList);
		view.addObject("thread", threadList);
		view.addObject("threadPool", threadPoolList);
		
		return view;
	}
	
	@RequestMapping(params = "method=queryMbeanTop")
	public void queryMbeanTop(int appId,String key,String mianpro,HttpServletResponse response ){
		
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		
		List<TimeDataInfo> ipList = commonService.querykeyRecentlyDataForHostBySort(appInfo.getAppName(),key,mianpro);
		
		Collections.sort(ipList, new Comparator<TimeDataInfo>(){
			@Override
			public int compare(TimeDataInfo o1, TimeDataInfo o2) {
				
				if(DataUtil.transformInt(o1.getMainValue()) >DataUtil.transformInt(o2.getMainValue()) ){
					return 1;
				}else if(DataUtil.transformInt(o1.getMainValue()) <DataUtil.transformInt(o2.getMainValue()) ){
					return -1;
				}
				return 0;
			}});
		List<TimeDataInfo> tmp = new ArrayList<TimeDataInfo>();
		if(ipList.size() >10){
			tmp = ipList.subList(0, 10);
		}else{
			tmp = ipList;
		}
		
		try {
			writeJSONToResponseJSONArray(response, tmp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 展示mben 某个key 在每台机器的的信息
	 *@author xiaodu
	 * @param appId
	 * @param key
	 * @return
	 *TODO
	 */
	@RequestMapping(params = "method=showMbeanHost")
	public ModelAndView  showMbeanHost(int appId,String key,String mianpro){
		
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		
		
		List<SortEntry<TimeDataInfo>> sortEntryList= commonService.querykeyDataForHostBySort(appInfo.getAppName(),key,mianpro);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> timeList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			timeList.add(sdf.format(cal.getTime()));
		}
		
		
		ModelAndView view = new ModelAndView("/time/mbean/mbean_host");
		view.addObject("sortEntryList", sortEntryList);
		view.addObject("timeList", timeList);
		view.addObject("appInfo", appInfo);
		view.addObject("keyname", key);
		return view;
		
	}
	
	
	
	

}
