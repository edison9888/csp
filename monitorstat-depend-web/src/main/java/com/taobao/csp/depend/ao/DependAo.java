package com.taobao.csp.depend.ao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.depend.dao.CspCheckupDependDao;
import com.taobao.csp.depend.dao.CspDependentDao;
import com.taobao.csp.depend.po.AppDepApp;
import com.taobao.csp.depend.po.CheckupDependConfig;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.csp.depend.util.MethodUtil;

public class DependAo {
	private static final Logger logger =  Logger.getLogger(DependAo.class);
	
	private static DependAo ao = new DependAo();
	private DependAo(){}
	public static DependAo get() {
		return ao;
	}
	
	private CspDependentDao dao = new CspDependentDao();
	private CspCheckupDependDao cspCheckupDependDao = new CspCheckupDependDao();
	
	public List<AppDepApp> findMeDependAppList(final String opsName,Date collectTime){
		return dao.findAppDepend(opsName, collectTime);	
	}
	
	public List<AppDepApp> findDependMeAppList(final String opsName,Date collectTime){
		return dao.findDependApp(opsName, collectTime);	
	}
	
	public List<AppDepApp> getMeDependList(String opsName, String selectDate,String showType) {
		if(StringUtils.isBlank(opsName)){
			opsName = "detail";
		}
		if(StringUtils.isBlank(showType)){
			showType = "all";
		}
		
		Date date = MethodUtil.getDate(selectDate);
		Date predate = MethodUtil.getPreDate(selectDate);
		
		List<AppDepApp> currentDeplist = findMeDependAppList(opsName, date);
		List<AppDepApp> historyDeplist = findMeDependAppList(opsName, predate);
		HashSet<AppDepApp> allSet = new HashSet<AppDepApp>();
		allSet.addAll(currentDeplist);
		allSet.addAll(historyDeplist);
		for(AppDepApp app:allSet){
			if(historyDeplist.contains(app)&&currentDeplist.contains(app)){
				app.setExistStatus(ConstantParameters.CONTROST_SAME);
			}else{
				if(historyDeplist.contains(app)&&!currentDeplist.contains(app)){
					app.setExistStatus(ConstantParameters.CONTROST_SUB);
				}else	if(!historyDeplist.contains(app)&&currentDeplist.contains(app)){
					app.setExistStatus(ConstantParameters.CONTROST_ADD);
				}
			}
		}
		
		//检测依赖的状态
		checkMeDependState(allSet, opsName);
		
		List<AppDepApp> list = new ArrayList<AppDepApp>();
		list.addAll(allSet);
		
		if(!"all".equals(showType)){
			Iterator<AppDepApp> it = list.iterator();
			while(it.hasNext()){
				if(!showType.equals(it.next().getDependAppType())){
					it.remove();
				}
			}
		}	
		return list;
	}
	
	/**
	 * 为传入的的应用进行依赖检测
	 */
	private Set<AppDepApp> checkMeDependState( Set<AppDepApp> appDeplist, String opsName) {
		List<CheckupDependConfig> tmp = cspCheckupDependDao.getCheckupDependConfig(opsName);
		Map<String,CheckupDependConfig> map = new HashMap<String, CheckupDependConfig>();
		for(CheckupDependConfig f:tmp) {
			map.put(f.getTargetOpsName(), f);
		}
		for(AppDepApp app:appDeplist){
			CheckupDependConfig config = map.get(app.getDependOpsName());
			if(config != null){
				app.setConfig(config);
			}
		}
		return appDeplist;
	}
	
	public List<AppDepApp> showDependMeList(String opsName, String selectDate,String showType) {
		if(StringUtils.isBlank(opsName)){
			opsName = "detail";
		}
		if(StringUtils.isBlank(showType)){
			showType = "all";
		}
		
		Date date = MethodUtil.getDate(selectDate);
//		Date predate = MethodUtil.getPreDate(selectDate);
		List<AppDepApp> currentDeplist = findDependMeAppList(opsName, date);
		List<AppDepApp> historyDeplist = findDependMeAppList(opsName, date);
		HashSet<AppDepApp> allSet = new HashSet<AppDepApp>();
		allSet.addAll(currentDeplist);
		allSet.addAll(historyDeplist);
		for(AppDepApp app:allSet){
			if(historyDeplist.contains(app)&&currentDeplist.contains(app)){
				app.setExistStatus(ConstantParameters.CONTROST_SAME);
			}else{
				if(historyDeplist.contains(app)&&!currentDeplist.contains(app)){
					app.setExistStatus(ConstantParameters.CONTROST_SUB);
				}else	if(!historyDeplist.contains(app)&&currentDeplist.contains(app)){
					app.setExistStatus(ConstantParameters.CONTROST_ADD);
				}
			}
		}
		
		//检测依赖的状态
		checkDependMeState(allSet, opsName);
		List<AppDepApp> list = new ArrayList<AppDepApp>();
		list.addAll(allSet);
		
		if(!"all".equals(showType)){
			Iterator<AppDepApp> it = list.iterator();
			while(it.hasNext()){
				if(!showType.equals(it.next().getSelfAppType())){
					it.remove();
				}
			}
		}	
		return list;
	}	
	
	private Set<AppDepApp> checkDependMeState( Set<AppDepApp> appDeplist, String targetOpsName) {
	  List<CheckupDependConfig> tmp = cspCheckupDependDao.getCheckupDependConfigByTargetOpsName(targetOpsName);	
	  Map<String,CheckupDependConfig> map = new HashMap<String, CheckupDependConfig>();
	  for(CheckupDependConfig f:tmp) {
	    map.put(f.getOpsName(), f);
	  }
	  for(AppDepApp app:appDeplist){
	    CheckupDependConfig config = map.get(app.getOpsName());
	    if(config != null){
	      app.setConfig(config);
	    }
	  }
	  return appDeplist;
	}
	
	
	
	public void sortAppDepAppList(List<AppDepApp> list) {
//		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		//in case for "Comparison method violates its general contract" in JDK 7
		Collections.sort(list,new Comparator<AppDepApp>(){
			@Override
			public int compare(AppDepApp o1, AppDepApp o2) {
				if(o1.getExistStatus().equals(ConstantParameters.CONTROST_SAME)){
					return -1;
				}
				if(o1.getExistStatus().equals(ConstantParameters.CONTROST_SUB)){
					return 1;
				}
				return 0;
			}});
	}
}
