package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.AmlineFlash;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

@Controller
@RequestMapping("/app/detail/hsf/provider/show.do")
public class CenterAppHSFController extends BaseController {
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;

	// @Resource(name = "hsfService")
	// private HSFService hsfService;

	
	
	/**HSF-provider ��������*/
	@RequestMapping(params = "method=queryAreaRate")
	public void queryAreaRate(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<TimeDataInfo> list = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_AREA_RATE, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}
	
	
	/**
	 * @author wb-lixing 2012-3-8 ����03:11:36
	 */

	@RequestMapping(params = "method=gotohsfSourceMethodHost")
	public ModelAndView gotohsfSourceMethodHost(int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		ModelAndView view = new ModelAndView("time/hsf/hsf_source_method_host");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfSourceMethodHost")
	public void queryhsfSourceMethodHost(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	/**
	 * @author wb-lixing 2012-3-8 ����02:55:36
	 */
	@RequestMapping(params = "method=gotohsfSourceHost")
	public ModelAndView gotohsfSourceHost(int appId, String className) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/hsf/hsf_source_host");

		view.addObject("appInfo", appInfo);
		view.addObject("className", className);
		return view;
	}

	/**
	 * @author wb-lixing 2012-3-8 ����02:55:36
	 */
	@RequestMapping(params = "method=queryhsfSourceHost")
	public void queryhsfSourceHost(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=showHostRestInfo")
	public void showHostRestInfo(HttpServletResponse response, Integer appId, String ip) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		List<TimeDataInfo> list = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.C_TIME, ip, new Date());

		
		AmlineFlash restamline = new AmlineFlash();
		
		Map<String,Double> b = BaseLineCache.get().getBaseLine(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.C_TIME,ip);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf1.format(new Date());
		if(b != null){
			for(Map.Entry<String,Double> h: b.entrySet() ){
				try {
					restamline.addValue("����", sdf.parse(time+" "+h.getKey()).getTime(), h.getValue());
				} catch (ParseException e) {
				}
			}
		}

		for (TimeDataInfo info : list) {
			restamline.addValue("request-time", info.getTime(), info.getMainValue());
		}
		try {
			writeJSONToResponse(response, restamline.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author wb-lixing 2012-3-7 ����05:13:09 ��ȡ ���� hsf provider һ���������Ϣ��ͨ������
	 * @param response
	 * @param appId
	 */
	@RequestMapping(params = "method=showHostPvInfo")
	public void showHistoryHsfProviderPvHost(HttpServletResponse response, int appId, String ip) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		List<TimeDataInfo> list = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES, ip, new Date());
		
		AmlineFlash pvamline = new AmlineFlash();
		
		
		Map<String,Double> b = BaseLineCache.get().getBaseLine(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES,ip);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf1.format(new Date());
		if(b != null){
			for(Map.Entry<String,Double> h: b.entrySet() ){
				try {
					pvamline.addValue("����", sdf.parse(time+" "+h.getKey()).getTime(), h.getValue());
				} catch (ParseException e) {
				}
			}
		}
	

		for (TimeDataInfo info : list) {
			pvamline.addValue("hsf-pv", info.getTime(), info.getMainValue());
		}
		try {
			writeJSONToResponse(response, pvamline.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * @author wb-lixing 2012-3-7 ����05:13:27
	 */
	@RequestMapping(params = "method=queryHostDetail")
	public void queryHostDetail(HttpServletResponse response, int appId, String ip) {
		Map<String, List<TimeDataInfo>> map = new HashMap<String, List<TimeDataInfo>>();
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<TimeDataInfo> referList = commonService
				.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_REFER, PropConstants.E_TIMES, ip);
		List<TimeDataInfo> sourceList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_PROVIDER,
				PropConstants.E_TIMES, ip);

		ratePv(referList);
		ratePv(sourceList);

		if (sourceList.size() > 10) {
			sourceList = sourceList.subList(0, 10);
		}
		if (referList.size() > 10) {
			referList = referList.subList(0, 10);
		}

		map.put("source", sourceList);
		map.put("refer", referList);
		try {
			writeJSONToResponseJSONObject(response, map);
		} catch (IOException e) {
		}

	}

	/**
	 * @author wb-lixing 2012-3-7 ����05:13:27
	 */
	/**
	 * * �������� - ҳ��
	 * http://localhost:8100/monitorstat-time-web/app/detail/apache/show
	 * .do?method=gotoIPDetail&appId=1&ip=172.23.172.121
	 */
	@RequestMapping(params = "method=gotoHostDetail")
	public ModelAndView gotoHostDetail(HttpServletResponse response, int appId, String ip) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		ModelAndView view = new ModelAndView("/time/hsf/ip_detail");
		view.addObject("appInfo", appInfo);
		view.addObject("ip", ip);
		return view;
	}

	@RequestMapping(params = "method=gotohsfReferClassHost")
	public ModelAndView gotohsfReferClassHost(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/hsf/hsf_refer_class_host");

		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfReferClassHost")
	public void queryhsfReferClassHost(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);

		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfReferMethodHost")
	public ModelAndView gotohsfReferMethodHost(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/hsf/hsf_refer_method_host");

		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfReferMethodHost")
	public void queryhsfReferMethodHost(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfReferClass")
	public ModelAndView Class(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		ModelAndView view = new ModelAndView("time/hsf/hsf_refer_class");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfReferClass")
	public void queryhsfReferClass(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);

		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfReferMethod")
	public ModelAndView gotohsfReferMethod(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		ModelAndView view = new ModelAndView("time/hsf/hsf_refer_method");
		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfReferMethod")
	public void queryhsfReferMethod(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfReferHost")
	public ModelAndView gotohsfReferHost(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/hsf/hsf_refer_host");

		view.addObject("appInfo", appInfo);
		return view;
	}

	@RequestMapping(params = "method=queryhsfReferHost")
	public void queryhsfReferHost(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	// findHsfInfosByHost
	@RequestMapping(params = "method=gotohsfRefer")
	public ModelAndView gotohsfRefer(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_REFER, PropConstants.E_TIMES);

		if (list == null || list.size() == 0) {
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName() + " ������ HSF-�����ҵ� ��Ϣ");
			message.addObject("callbackurl", "/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex");
			message.addObject("appInfo", appInfo);
			return message;
		}

		ModelAndView view = new ModelAndView("time/hsf/hsf_refer");

		view.addObject("appInfo", appInfo);
		return view;

	}

	/**
	 */
	@RequestMapping(params = "method=queryhsfRefer")
	public void queryhsfRefer(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_REFER, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(Integer appId) {
		// log.debug("------------------appId: " + appId);
		if (appId == null) {
			appId = 4;
			// log.debug("------------------use default appId: " + appId);
		}
		ModelAndView view = new ModelAndView("time/hsf/center_app_hsf");
		AppInfoPo po = AppInfoCache.getAppInfoById(appId);
		view.addObject("appInfo", po);
		return view;
	}

	

	private static final Logger log = Logger.getLogger(CenterAppHSFController.class);

	/**
	 * ��ȡhsf provider һ���������Ϣ��ͨ������
	 * 
	 * @param response
	 * @param appId
	 */
	@RequestMapping(params = "method=showHistoryHsProviderPv")
	public void showHistoryHsProviderPv(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<TimeDataInfo> list = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES, new Date());
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		
		List<TimeDataInfo> prelist = commonService.queryKeyDataHistory(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES, cal.getTime());
		

		AmlineFlash restamline = new AmlineFlash();
		for (TimeDataInfo info : list) {
			restamline.addValue("hsf-pv", info.getTime(), info.getMainValue());
		}
		
		for (TimeDataInfo info : prelist) {
			restamline.addValue("hsf-pv-"+TimeUtil.formatTime(cal.getTime(), "yyyy-MM-dd"), info.getTime(), info.getMainValue());
		}
		
		
		try {
			writeJSONToResponse(response, restamline.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(params = "method=gotohsfProviderIndex")
	/**
	 *����Ӧ��hsf pprovider��Ϣ��ҳ��
	 * @param appId
	 * @return
	 */
	public ModelAndView gotoHsfProviderIndex(int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		ModelAndView view = new ModelAndView("/time/hsf/hsf_provider_index");
		List<TimeDataInfo> list = commonService.querySingleKeyData(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);

		if (list == null || list.size() == 0) {
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName() + " ������HSF Provider ��Ϣ");
			message.addObject("callbackurl", "/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex");
			message.addObject("appInfo", appInfo);
			return message;
		}

		List<HostPo> ipList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appInfo.getAppName());

		
		view.addObject("ipList", ipList);
		view.addObject("appInfo", appInfo);
		return view;
	}

	/**
	 * ��ȡhsfӦ��ÿ������ṩ�ķ����� ͨ��json����Map<String,List<HsfInfo>>
	 * 
	 * @param response
	 * @param appId
	 */
	@RequestMapping(params = "method=showhsfProviderHost")
	public void showHsfProviderHost(HttpServletResponse response, int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		List<TimeDataInfo> list = commonService.querykeyRecentlyDataForHostBySort(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);

		for (TimeDataInfo hsfInfo : list) {
			HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(hsfInfo.getIp());
			hsfInfo.setSite(host.getHostSite().toUpperCase());
		}
		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}
	}

	/**
	 * ͨ��
	 * 
	 * @param appId
	 */
	@RequestMapping(params = "method=findHsfReferAppAndProviderInterface")
	public void findHsfReferAppAndProviderInterface(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		Map<String, List<TimeDataInfo>> result = new HashMap<String, List<TimeDataInfo>>();
		List<TimeDataInfo> providerList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_PROVIDER,
				PropConstants.E_TIMES);
		ratePv(providerList);
		if (providerList.size() > 10) {
			providerList = providerList.subList(0, 10);
		}
		result.put("providerList", providerList);

		List<TimeDataInfo> referList = commonService.querykeyRecentlyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_REFER, PropConstants.E_TIMES);
		ratePv(referList);
		if (referList.size() > 10) {
			referList = referList.subList(0, 10);
		}

		result.put("referList", referList);

		try {
			writeJSONToResponseJSONObject(response, result);
		} catch (IOException e) {
		}

	}

	@RequestMapping(params = "method=gotohsfProvider")
	public ModelAndView gotoHsfProviderInfo(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);

		if (list == null || list.size() == 0) {
			ModelAndView message = new ModelAndView("/time/common/message");
			message.addObject("message", appInfo.getAppName() + " ������HSF Provider ��Ϣ");
			message.addObject("callbackurl", "/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex");
			message.addObject("appInfo", appInfo);
			return message;
		}

		ModelAndView view = new ModelAndView("/time/hsf/hsf_provider");

		view.addObject("appInfo", appInfo);
		return view;

	}

	@RequestMapping(params = "method=queryHsfProvider")
	public void queryHsfProviderInfos(HttpServletResponse response, int appId) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);

		try {
			writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
		}
	}

	@RequestMapping(params = "method=gotohsfProviderChlid")
	public ModelAndView gotoHsfProviderChildInfo(int appId) {

		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);

		ModelAndView view = new ModelAndView("/time/hsf/hsf_provider_child");

		view.addObject("appInfo", appInfo);
		return view;

	}

	@RequestMapping(params = "method=queryHsfProviderChild")
	public void queryHsfProviderChild(HttpServletResponse response, int appId, String key) {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<SortEntry<TimeDataInfo>> l = commonService.querykeyDataForChildBySort(appInfo.getAppName(), key, PropConstants.E_TIMES);
		try {
			writeJSONToResponseJSONArray(response, l);
		} catch (IOException e) {
		}
	}

	private void ratePv(List<TimeDataInfo> poList) {

		int pv = 0;
		for (TimeDataInfo po : poList) {
			pv += po.getMainValue();
		}

		if (pv != 0) {
			for (TimeDataInfo po : poList) {
				po.setMainRate(DataUtil.rate(DataUtil.transformLong(po.getMainValue()), pv) + "");
			}
		}

	}

	/**
	 * 
	 * ��ѯĳ���ӿ�����ЩӦ����ʹ�� ��queryhsfRefer���صĽ����һ���ģ���Ȼ��ѯ��ʽ��ͬ
	 * 
	 * @param appName
	 *            ��Ӧ��Ӧ��
	 * @param key
	 *            ���������֣�HSF-provider`���� ; HSF-provider1`����`������ �ӿ�����
	 */
	@RequestMapping(params = "method=gotoClassOrMethodUser")
	public ModelAndView gotoClassOrMethodUser(int appId,String key) {
		
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		
		ModelAndView view = new ModelAndView("/time/hsf/hsf_app_use_class");
		view.addObject("appInfo", appInfo);
		view.addObject("keyName", key);
		
		// ȥ��key֮ǰ��HSF-provider
		String keySuffix = key.replace(KeyConstants.HSF_PROVIDER + Constants.S_SEPERATOR, "");
		try {
			String likeName = KeyConstants.HSF_REFER + Constants.S_SEPERATOR + "%" + Constants.S_SEPERATOR + keySuffix;
			List<CspKeyInfo> keyInfoList = KeyAo.get().findKeyLikeName(likeName);
			List<String> keyNameList = new ArrayList<String>();
			for (CspKeyInfo keyInfo : keyInfoList) {
				String keyName = keyInfo.getKeyName();
				keyNameList.add(keyName);
			}
			List<SortEntry<TimeDataInfo>> list = commonService.queryMutilKeyDataBySort(appInfo.getAppName(), keyNameList, PropConstants.E_TIMES);
			
			Iterator<SortEntry<TimeDataInfo>> it = list.iterator();
			while(it.hasNext()){
				SortEntry<TimeDataInfo> s = it.next();
				if(s.getObjectMap().size()==0){
					it.remove();
				}
			}
			
			view.addObject("sortEntryList", list);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			List<String> timeList = new ArrayList<String>();
			Calendar cal = Calendar.getInstance();
			for (int i = 1; i <= 10; i++) {
				cal.add(Calendar.MINUTE, -1);
				timeList.add(sdf.format(cal.getTime()));
			}
			view.addObject("timeList", timeList);
			
		} catch (Exception e) {
		}
		return view;
	}

	

	

}
