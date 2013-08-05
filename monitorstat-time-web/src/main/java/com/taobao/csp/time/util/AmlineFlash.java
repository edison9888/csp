
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.csp.time.web.po.Amline;

/**
 * @author xiaodu
 *
 * ÏÂÎç2:52:06
 */
public class AmlineFlash {
	
	private String format = "HH:mm";;	
	
	private Map<String,List<Amline>> titleMap = new HashMap<String, List<Amline>>();
	
	private List<String> titleList = new ArrayList<String>();
	
	private Set<String> timeSet = new HashSet<String>();
	
	private SimpleDateFormat sdf = new SimpleDateFormat(format);
	
	public AmlineFlash(){
	}
	public AmlineFlash(String format){
		this.format = format;
		sdf = new SimpleDateFormat(format);
	}
	
	
	public void addValue(String title,long time,double value){
		List<Amline> list = titleMap.get(title);
		if(list == null){
			list= new ArrayList<Amline>();
			titleMap.put(title, list);
			titleList.add(title);
		}
		String t = sdf.format(new Date(time));
		timeSet.add(t);
		
		Amline amline = new Amline();
		amline.setFtime(t);
		amline.setTime(time);
		amline.setValue(value);
		list.add(amline);
	}
	
	
	public String getAmline(){
		
		if(timeSet.size() ==0){
			return "<chart><series><value xid='0'>0</value></series><graphs><graph gid='0' title=''><value xid='0'>0</value></graph></graphs></chart>";
		}
		
		List<String> timeList = new ArrayList<String>();
		timeList.addAll(timeSet);
		Collections.sort(timeList);
		
		StringBuffer sb =  new StringBuffer("<chart>");			
		
		sb.append("<series>");
		for(int i=0;i<timeList.size();i++){			
			sb.append("<value xid='"+i+"'>"+timeList.get(i)+"</value>");
		}
		sb.append("</series>");
		sb.append("<graphs>");
		for(int i=0;i<titleList.size();i++){			
			List<Amline> dateList = titleMap.get(titleList.get(i));
			sb.append("<graph gid='"+i+"' title='"+titleList.get(i)+"'>");		
			
			Map<String,Double> valueMap = new HashMap<String, Double>();
			for(Amline a:dateList){
				valueMap.put(a.getFtime(), a.getValue());
			}
			
			Double prev = 0d;
			for(int k=0;k<timeList.size();k++){
				String ftime = timeList.get(k);
				Double value = valueMap.get(ftime);
				if(value == null){
					sb.append("<value xid='"+k+"'>"+prev+"</value>");	
				}else{
					prev = value;
					sb.append("<value xid='"+k+"'>"+value+"</value>");	
				}
			}	
			sb.append("</graph>");
		}
		sb.append("</graphs>");			
		sb.append("</chart>");
		return sb.toString();
		
	}
	
	

}
