
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.other.artoo.Artoo;
import com.taobao.csp.other.artoo.ArtooInfo;
import com.taobao.csp.other.changefree.ChangeFree;
import com.taobao.csp.other.changefree.ChangeFreeInfo;

/**
 * @author xiaodu
 *
 * ÉÏÎç11:34:47
 */
@Controller
@RequestMapping("/other/show.do")
public class OtherMessageController {
	
	@RequestMapping(params = "method=recentlyChangeFree")
	public ModelAndView recentlyChangeFree(){
		List<ChangeFree> cfList = ChangeFreeInfo.get().getRecentlyChangeFree();
		ModelAndView view = new ModelAndView("/time/other/recently_cf");
		view.addObject("cfList", cfList);
		return view;
	}
	
	
	@RequestMapping(params = "method=todayArtoo")
	public ModelAndView todayArtoo(String appName){
		List<Artoo> list = ArtooInfo.get().getRecentlyArtoo(appName);
		
		Iterator<Artoo> it = list.iterator();
		while(it.hasNext()){
			Artoo artoo = it.next();
			if(StringUtils.isNotBlank(appName)&&!appName.equals(artoo.getAppName())){
				it.remove();
			}
		}
		
		Map<String,Artoo> map = new HashMap<String, Artoo>();
		
		for(Artoo artoo:list){
			Artoo a = map.get(artoo.getAppName());
			if(a == null){
				map.put(artoo.getAppName(), artoo);
			}else{
				if(a.getDeployTime().compareToIgnoreCase(artoo.getDeployTime())<0){
					map.put(artoo.getAppName(), artoo);
				}
			}
		}
		
		list.clear();
		list.addAll(map.values());
		
		
		ModelAndView view = new ModelAndView("/time/other/recently_deploy");
		view.addObject("artooList", list);
		return view;
	}
	
	
	
	

}
