package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspTimeAppDependInfo;
import com.taobao.monitor.common.po.CspTimeExtraAppKey;
import com.taobao.monitor.common.po.CspTimeKeyDependInfo;
import com.taobao.monitor.common.util.Constants;

@Controller
@RequestMapping("/config/dependconfig.do")
public class DependConfigAction extends BaseController {
	CspDependInfoAo depAo = CspDependInfoAo.get();
	private static final Logger logger = Logger.getLogger(DependConfigAction.class);

	/*
	 * 查询依赖关系
	 */
	@RequestMapping(params = "method=searchAppConfigList")
	public ModelAndView searchAppConfigList(String sourceAppName, String appName, String appId) throws IOException {
		AppInfoPo appInfo = null;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}

		if(sourceAppName == null || "".equals(sourceAppName.trim())) {
			ModelAndView view = new ModelAndView("/time/depend/config/configappnew");
			view.addObject("sourceAppName", sourceAppName);
			view.addObject("appName", appName);
			view.addObject("appInfo", appInfo);
			view.addObject("meDepAppList", new ArrayList<CspTimeAppDependInfo>());
			return view;
		} else if(appName == null) {
			//如果子APP没有，则默认查询sourceAppName的子App
			appName = sourceAppName;  
		}

		Map<String, CspTimeAppDependInfo> map = CspDependInfoAo.get().getMeDependMap_New(sourceAppName, appName);
		List<CspTimeAppDependInfo> meDepAppList = new ArrayList<CspTimeAppDependInfo>(map.values());

		List<CspTimeAppDependInfo> sourceAppList = CspDependInfoAo.get().getCspTimeAppDependInfoBySourceName(sourceAppName);

		Collections.sort(meDepAppList);

		ModelAndView view = new ModelAndView("/time/depend/config/configappnew");
		view.addObject("sourceAppName", sourceAppName);
		view.addObject("appName", appName);
		view.addObject("meDepAppList", meDepAppList);
		view.addObject("appInfo", appInfo);
		view.addObject("sourceAppList", sourceAppList);

		view.addObject("EXTRA_CONFIG", Constants.EXTRA_CONFIG + "");
		view.addObject("AUTO_NOTCONFIG", Constants.AUTO_NOTCONFIG + "");
		view.addObject("NOTAUTO_CONFIG", Constants.NOTAUTO_CONFIG + "");
		view.addObject("AUTO_CONFIG", Constants.AUTO_CONFIG + "");
		return view;
	}

	/**
	 * 添加额外的应用
	 */
	@RequestMapping(params = "method=gotoAddAppPage")
	public ModelAndView gotoAddAppPage(String sourceAppName, String appName, String appId) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}
		ModelAndView view = new ModelAndView("/time/depend/config/addAppPage"); //添加Key的界面
		view.addObject("appInfo", appInfo);
		view.addObject("sourceAppName",sourceAppName);
		view.addObject("appName",appName);
		return view;
	}  

	/**
	 * 保存单独添加的sourcekey
	 */
	@RequestMapping(params = "method=saveExtraApp")
	public String saveExtraApp(String sourceAppName, String appId, String appName, String extraAppName, String extraAppDesc, String extraAppType) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}

		CspDependInfoAo.get().addExtraApp(extraAppName, extraAppDesc,extraAppType);
		//return "redirect:/config/dependconfig.do?method=showSourceKeyList&sourceAppName=" + sourceAppName + "&appId=" + appId;
		return "redirect:/config/dependconfig.do?method=searchAppConfigList&sourceAppName=" + sourceAppName + "&appName=" + appName + "&appId=" + appId;
	}    

	/**
	 * 跳转到添加子key的页面
	 */
	@RequestMapping(params = "method=gotoAddSubKeyPage")
	public ModelAndView gotoAddSubKeyPage(String sourceAppName, String appId, String sourceKeyName, String appName, String keyName) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}
		ModelAndView view = new ModelAndView("/time/depend/config/addSubKeyPage"); //添加子Key的界面
		view.addObject("appInfo", appInfo);
		view.addObject("sourceAppName",sourceAppName);
		view.addObject("sourceKeyName", sourceKeyName);
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		return view;
	}  

	/**
	 * 保存单独添加的sourcekey
	 */
	@RequestMapping(params = "method=saveExtraKey")
	public String saveExtraKey(String sourceAppName, String sourceKeyName, String appId, String appName, String keyName, String extraAppName, String extraKeyName) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}
		CspDependInfoAo.get().addExtraAppKey(extraAppName, extraKeyName);
		return "redirect:/config/dependconfig.do?method=gotoSubkeyConfig&sourceAppName=" + sourceAppName + "&sourceKeyName=" + sourceKeyName +"&appName=" + appName + "&keyName" + keyName + "&appId=" + appId;
	}  

	/*
	 * 保存依赖关系
	 */
	@RequestMapping(params = "method=saveAppConfig")
	public String saveAppConfig(String sourceAppName, String appName, String[] combineId, String appId) throws IOException {
		if(sourceAppName == null || "".equals(sourceAppName.trim()) || appName == null) {
			//不做处理
		} else {
			Set<String> saveExistIdSet = new HashSet<String>();   //已经存在的需要保存的app
			Set<String> saveAutoIdSet = new HashSet<String>();    //自动生成的需要保存的app
			Set<String> extraAppNameSet = new HashSet<String>();  //手动添加的App

			if(combineId == null)
				combineId = new String[0];

			//处理字符串，得到要保存的Id
			for(String idStr: combineId) {
				String[] array = idStr.split("_#");
				String id = array[0];
				String type = array[1];
				if(type.equals(Constants.AUTO_CONFIG + "") || type.equals(Constants.NOTAUTO_CONFIG + "")) {
					saveExistIdSet.add(id);
				} else if(type.equals(Constants.AUTO_NOTCONFIG + "")) {
					saveAutoIdSet.add(id);
				} else if(type.equals(Constants.EXTRA_CONFIG + "")) {
					extraAppNameSet.add(array[2]);
				}  
			}      
			CspDependInfoAo.get().saveAppConfig(sourceAppName, appName, saveExistIdSet, saveAutoIdSet, extraAppNameSet);
		}
		return "redirect:/config/dependconfig.do?method=searchAppConfigList&sourceAppName=" + sourceAppName + "&appName=" + appName + "&appId=" + appId;
	}

	/**
	 * 显示SourceKey列表
	 */
	@RequestMapping(params = "method=showSourceKeyList")
	public ModelAndView showSourceKeyList(String sourceAppName, String appId) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}

		if(sourceAppName == null || "".equals(sourceAppName.trim())) {
			ModelAndView view = new ModelAndView("/time/depend/config/configkeymain");  //key配置主界面
			view.addObject("sourceAppName", sourceAppName);
			view.addObject("appInfo", appInfo);
			view.addObject("sourceKeyList", new ArrayList<CspTimeKeyDependInfo>());
			return view;
		} 

		List<CspTimeKeyDependInfo> sourceKeyList = CspDependInfoAo.get().showSourceKeyList(sourceAppName);
		ModelAndView view = new ModelAndView("/time/depend/config/configkeymain");  //key配置主界面
		view.addObject("sourceAppName", sourceAppName);
		view.addObject("appInfo", appInfo);
		view.addObject("sourceKeyList", sourceKeyList);
		return view;
	}

	/**
	 * 添加SourceKey
	 */
	@RequestMapping(params = "method=gotoAddSourceKeyPage")
	public ModelAndView gotoAddSourceKeyPage(String sourceAppName, String appId) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}
		List<String> keyList = CspDependInfoAo.get().getKeyList(appInfo.getAppId(), sourceAppName);
		ModelAndView view = new ModelAndView("/time/depend/config/addKeyPage"); //添加Key的界面
		view.addObject("appInfo", appInfo);
		view.addObject("sourceAppName",sourceAppName);
		view.addObject("keyList",keyList);
		return view;
	}

	/**
	 * 删除sourceKey
	 */
	@RequestMapping(params = "method=deleteSourceKey")
	public String deleteSourceKey(String sourceAppName, String appId, String[] combineId) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}
		if(combineId == null)
			combineId = new String[0];

		Set<String[]> set = new HashSet<String[]>();
		//处理字符串，得到要保存的Id
		for(String idStr: combineId) {
			String[] array = idStr.split("_#");
			set.add(array);
		}
		//删除
		CspDependInfoAo.get().deleteSourceKeys(tmpSourceAppName, set);
		return "redirect:/config/dependconfig.do?method=showSourceKeyList&sourceAppName=" + sourceAppName + "&appId=" + appId;
	}  

	/**
	 * 保存单独添加的sourcekey
	 */
	@RequestMapping(params = "method=saveSingleSourceKey")
	public String saveSingleSourceKey(String sourceAppName, String appId, String sourceKeyName, String dependAppName, String dependKeyName) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}

		CspDependInfoAo.get().addSourceKey(sourceAppName, sourceKeyName);
		//return "redirect:/config/dependconfig.do?method=showSourceKeyList&sourceAppName=" + sourceAppName + "&appId=" + appId;
		return "redirect:/config/dependconfig.do?method=gotoSubkeyConfig&sourceAppName=" + sourceAppName + "&appName=" + sourceAppName + "&appId=" + appId + "&sourceKeyName=" + sourceKeyName;
	}    

	/**
	 * goto子key的配置页面
	 */
	@RequestMapping(params = "method=gotoSubkeyConfig")
	public ModelAndView gotoSubkeyConfig(String sourceAppName, String appId, String sourceKeyName, String appName, String keyName) {
		AppInfoPo appInfo;
		String tmpSourceAppName = sourceAppName;
		try {
			appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
			tmpSourceAppName = appInfo.getAppName();
			if(tmpSourceAppName != null)
				sourceAppName = tmpSourceAppName;
		} catch (Exception e) {
			logger.error("查询应用异常",e);
			appInfo = new AppInfoPo();
		}
		if(keyName == null) //默认显示sourcekey下面的第一级
			keyName = sourceKeyName;

		List<CspTimeKeyDependInfo> meDepKeyList = CspDependInfoAo.get().getCspTimeKeyDependInfoSub(sourceAppName, sourceKeyName, appName, keyName);
		Collections.sort(meDepKeyList);
		List<CspTimeKeyDependInfo> sourceKeyList = CspDependInfoAo.get().getSingleSourceKeyList(sourceAppName, sourceKeyName);
		ModelAndView view = new ModelAndView("/time/depend/config/configkeysub");
		view.addObject("sourceAppName", sourceAppName);
		view.addObject("sourceKeyName", sourceKeyName);
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		view.addObject("meDepKeyList", meDepKeyList);
		view.addObject("appInfo", appInfo);
		view.addObject("sourceKeyList", sourceKeyList);
		view.addObject("EXTRA_CONFIG", Constants.EXTRA_CONFIG + "");
		view.addObject("AUTO_NOTCONFIG", Constants.AUTO_NOTCONFIG + "");
		view.addObject("NOTAUTO_CONFIG", Constants.NOTAUTO_CONFIG + "");
		view.addObject("AUTO_CONFIG", Constants.AUTO_CONFIG + "");
		return view;
	} 

	/*
	 * 保存依赖关系
	 */
	@RequestMapping(params = "method=saveSubKey")
	public String saveSubKey(String sourceAppName, String sourceKeyName, String[] combineId, String appName, String keyName, String appId) throws IOException {
		if(sourceAppName == null || "".equals(sourceAppName.trim()) ||sourceKeyName == null || "".equals(sourceKeyName.trim()) || keyName == null) {
			//不做处理
		} else {
			Set<String> saveExistIdSet = new HashSet<String>();  //已经存在的需要保存的key
			Set<String> saveAutoIdSet = new HashSet<String>();   //自动生成的需要保存的key
			Map<String,String> saveAutoMap = new HashMap<String, String>(); //autokey id,appname
			Set<CspTimeExtraAppKey> extraAppKeySet = new HashSet<CspTimeExtraAppKey>();

			if(combineId == null)
				combineId = new String[0];

			//字符串格式:${item.id}_${item.dependStatus}_${item.appName}_${item.dependAppName}_${item.dependKeyName}
			for(String idStr: combineId) {
				String[] array = idStr.split("_#");
				String id = array[0];
				String type = array[1];

				if(type.equals(Constants.AUTO_CONFIG + "") || type.equals(Constants.NOTAUTO_CONFIG + "")) {
					saveExistIdSet.add(id);
				} else if(type.equals(Constants.AUTO_NOTCONFIG + "")) {
					String keyAppName = array[2];   //此处是为了避免appname在插入前重复查询而设置的
					saveAutoIdSet.add(id);
					saveAutoMap.put(id, keyAppName);  
				} else if(type.equals(Constants.EXTRA_CONFIG + "")) { //手动添加的情况
					CspTimeExtraAppKey extraKey = new CspTimeExtraAppKey();
					extraKey.setExtraAppName(array[3]);
					extraKey.setKeyName(array[4]);
					extraAppKeySet.add(extraKey);
				} 
			}      
			//入库操作
			CspDependInfoAo.get().saveSubKey(sourceAppName, sourceKeyName, appName, keyName, saveExistIdSet, saveAutoMap, extraAppKeySet);
		}    
		return "redirect:/config/dependconfig.do?method=gotoSubkeyConfig&sourceAppName=" + sourceAppName + "&sourceKeyName=" + sourceKeyName + "&appName=" + appName + "&keyName=" + keyName + "&appId=" + appId;
	}  
}
