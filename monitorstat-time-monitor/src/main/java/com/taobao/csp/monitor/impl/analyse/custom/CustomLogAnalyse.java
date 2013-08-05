/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.custom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * prefix:
 *  time: 
 * timeformat: 
 * Pattern: 
 * keyIndex: 
 * keyScope: 
 * valueIndex:
 * valueNames: 
 * valueType:
 *  valueOperate:
 * 
 * 
 * 
 * 
 * @author xiaodu
 * 
 *         ÏÂÎç5:27:07
 */
public class CustomLogAnalyse extends AbstractDataAnalyse {

	private String prefix;
	private String time;
	private String timeformat;
	private String pattern;
	private int[] keyIndex;
	private String[] keyScope;
	private int[] valueIndex;
	private String[] valueNames;
	private String[] valueType;
	private String[] valueOperate;


	public CustomLogAnalyse(String appName, String ip, String feature) {
		super(appName, ip, feature);
		decodeFeature(feature);
		if(valueOperate==null||valueType == null||valueNames==null||
				valueIndex==null||keyScope==null||keyIndex==null||pattern==null||timeformat==null||time==null||prefix==null){
			
		}
	}
	
	
	private void decodeFeature(String feature){
		if(feature != null){
			String[] tmp = feature.split("$__$");
			for(String t:tmp){
				if(t.indexOf("prefix")>-1){
					prefix = t.substring("prefix".length()+1);
				}
				if(t.indexOf("time")>-1){
					time = t.substring("time".length()+1);
				}
				if(t.indexOf("timeformat")>-1){
					timeformat = t.substring("timeformat".length()+1);
				}
				if(t.indexOf("pattern")>-1){
					pattern = t.substring("pattern".length()+1);
				}
				if(t.indexOf("keyIndex")>-1){
					String index = t.substring("keyIndex".length()+1);
					String[] t_i = index.split(",");
					keyIndex = new int[t_i.length];
					for(int i=0;i<t_i.length;i++){
						keyIndex[i] = Integer.parseInt(t_i[i]);
					}
				}
				if(t.indexOf("keyScope")>-1){
					String ks = t.substring("keyScope".length()+1);
					keyScope = ks.split(",");
				}
				
				if(t.indexOf("valueIndex")>-1){
					String index = t.substring("valueIndex".length()+1);
					String[] t_i = index.split(",");
					valueIndex = new int[t_i.length];
					for(int i=0;i<t_i.length;i++){
						valueIndex[i] = Integer.parseInt(t_i[i]);
					}
				}
				
				if(t.indexOf("valueNames")>-1){
					String ks = t.substring("valueNames".length()+1);
					valueNames = ks.split(",");
				}
				if(t.indexOf("valueType")>-1){
					String ks = t.substring("valueType".length()+1);
					valueType = ks.split(",");
				}
				if(t.indexOf("valueOperate")>-1){
					String ks = t.substring("valueOperate".length()+1);
					valueOperate = ks.split(",");
				}
			}
		}
	}

	@Override
	public void analyseOneLine(String line) {
		
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(line);
		if(matcher.find()){
			
			for(int i=0;i<keyIndex.length;i++){
				
			}
			
//			matcher.group(group)
		}

	}

	@Override
	public void submit() {

	}

	@Override
	public void release() {

	}
	
	
	
	private class KeyEntry{
		private String keyName;
		
		private Object value;
		
		
	}
	
	private class valueEntry{
		
		
		
	}
	
	

}
