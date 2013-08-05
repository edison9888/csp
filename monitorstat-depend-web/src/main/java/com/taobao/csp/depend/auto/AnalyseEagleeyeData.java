//
///**
// * monitorstat-depend-web
// */
//package com.taobao.csp.depend.auto;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.taobao.monitor.common.ao.center.AppInfoAo;
//import com.taobao.monitor.common.ao.center.EagleeyeDataAo;
//import com.taobao.monitor.common.ao.center.KeyAo;
//import com.taobao.monitor.common.po.CspCallsRelationship;
//import com.taobao.monitor.common.po.EagleeyeRate;
//import com.taobao.monitor.common.util.Arith;
//
///**
// * @author xiaodu
// *
// * 下午10:13:27
// */
//public class AnalyseEagleeyeData {
//	
//	private void parseHsfInfo(String[] tmpTopo,int traceCount,Map<String,Map<String,ReferCount>> referMap){
//		String preHsf = null;
//		String preSource = null;
//		
//		Map<String,Integer> hsfcount = new HashMap<String, Integer>();
//		
//		for(String trace:tmpTopo){
//			String[] tmp = StringUtils.split(trace, ",");
//			if(tmp.length!=3){
//				continue;
//			}
//			String s =  tmp[1];
//			String t =  tmp[2];
//			
//			Integer c = hsfcount.get(t);
//			if(c == null){
//				hsfcount.put(t, traceCount);
//			}else{
//				hsfcount.put(t, c+traceCount);
//			}
//			
//			if(!s.startsWith("com")){
//				continue;
//			}
//			
//			if(preHsf != null&&s.equals(preHsf)){//这个还属于上次的
//			}else{
//				preSource = s;
//			}
//			
//			preHsf = t;
//			
//			Map<String,ReferCount> refer = referMap.get(preSource);
//			if(refer == null){
//				refer = new HashMap<String, ReferCount>();
//				referMap.put(preSource, refer);
//			}
//			
//			
//			ReferCount count = refer.get(s);
//			if(count == null){
//				count = new ReferCount();
//				refer.put(s, count);
//				
//				count.sourceURL = s;
//				count.selfName = s;
//			}
//			
//			Integer child = count.childMap.get(t);
//			if(child == null){
//				count.childMap.put(t, traceCount);
//			}else{
//				count.childMap.put(t, traceCount+child);
//			}
//		}
//		
//		
//		for(Map.Entry<String,Integer> entry:hsfcount.entrySet()){
//			String hsfkey = entry.getKey();
//			Integer c = entry.getValue();
//			Map<String,ReferCount> refer = referMap.get(hsfkey);
//			if(refer != null){
//				ReferCount rc = refer.get("browser|");
//				if(rc == null){
//					rc = new ReferCount();
//					rc.childMap.put(hsfkey+"|",c);
//					refer.put("browser|", rc);
//				}else{
//					rc.childMap.put(hsfkey+"|", rc.childMap.get(hsfkey+"|")+c);
//				}
//				
//			}
//		}
//	}
//	
//	
//	
//	public void analyseData(Date date){
//		
//		
//		Map<String,String> urlappmap = AppInfoAo.get().findAllAppUrlRelationMap();
//		
//		Map<String,String> hsfMap = KeyAo.get().findAppNameByHsfKeyName();
//		
//		
//		Calendar  cal = Calendar.getInstance();
//		cal.setTime(date);
//		
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		Date start = cal.getTime();
//		cal.set(Calendar.HOUR_OF_DAY, 23);
//		cal.set(Calendar.MINUTE, 59);
//		cal.set(Calendar.SECOND,59);
//		Date end = cal.getTime();
//		
//		
//		List<EagleeyeTraceStatDO> list = EagleeyeDataAo.get().findEagleeyeTraceStatsSum(start, end);
//		Map<String,Map<String,ReferCount>> referMap = analyse(list);
//		
//		for(Map.Entry<String,Map<String,ReferCount>> entry:referMap.entrySet()){
//			String tracename = entry.getKey();
//			
//			if(entry.getValue().size()==0){
//				continue;
//			}
//			
//			String traceApp = urlappmap.get(tracename);
//			if(tracename.charAt(tracename.length()-1)=='|'){
//				tracename = tracename.substring(0, tracename.length()-1);
//			}
//			if(tracename.startsWith("http://")){//这个是URl
//				traceApp  = urlappmap.get(tracename);
//    		}else if(tracename.startsWith("com")){
//    			String tmpSourcename="HSF-provider`"+tracename.replaceAll("\\|", "`");
//    			traceApp = hsfMap.get(tmpSourcename);
//    		}
//			
//			
//			EagleeyeDataAo.get().deleteCallsRelationship(tracename);
//			
//			Map<String,ReferCount> map = entry.getValue();
//			
//			ReferCount rc = map.get("browser|");
//			Integer count = 0;
//			if(rc != null){
//				count = rc.childMap.get(tracename+"|");
//			}else{
//				System.out.println("");
//			}
//			
//			for(Map.Entry<String,ReferCount> t:map.entrySet()){
//				String sourcename = t.getKey();
//				if(sourcename.charAt(sourcename.length()-1)=='|'){
//					sourcename = sourcename.substring(0, sourcename.length()-1);
//				}
//				
//				
//				String tmpSourceApp = null;
//				
//				if(sourcename.startsWith("http://")){//这个是URl
//					tmpSourceApp  = urlappmap.get(sourcename);
//	    		}else if(sourcename.startsWith("com")){
//	    			String tmpSourcename="HSF-provider`"+sourcename.replaceAll("\\|", "`");
//	    			tmpSourceApp = hsfMap.get(tmpSourcename);
//	    		}
//				
//				for(Map.Entry<String,Integer> d:t.getValue().childMap.entrySet()){
//					String targetname = d.getKey();
//					
//					String targetApp = null;
//					if(targetname.startsWith("http://")){//这个是URl
//						targetApp = urlappmap.get(targetname);
//		    		}else if(targetname.startsWith("com")){
//		    			String tmpTargetname="HSF-provider`"+targetname.replaceAll("\\|", "`");
//		    			targetApp = hsfMap.get(tmpTargetname);
//		    		}
//					
//					if(targetname.charAt(targetname.length()-1)=='|'){
//						targetname = targetname.substring(0, targetname.length()-1);
//					}
//					
//					int targetNum = d.getValue();
//					EagleeyeRate er = new EagleeyeRate();
//					er.setCollectTime(cal.getTime());
//					er.setSourceNum(count);
//					er.setOrigin(sourcename);
//					er.setSourceUrl(tracename);
//					er.setTarget(targetname);
//					er.setTargetNum(targetNum);
//					EagleeyeDataAo.get().addEagleeyeRate(er);
//					
//					
//					if(count != null){
//						CspCallsRelationship cr = new CspCallsRelationship();
//						cr.setSourceApp(traceApp);
//						cr.setSourceUrl(tracename);
//						cr.setTargetApp(targetApp);
//						cr.setTarget(targetname);
//						cr.setOriginApp(tmpSourceApp);
//						cr.setOrigin(sourcename);
//						cr.setRate((float)Arith.div(targetNum, count, 4));
//						EagleeyeDataAo.get().addCallsRelationship(cr);
//					}
//				}
//			}
//		}
//	}
//	
//	
//	
//	
//	
//	public static void main(String[] args){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		AnalyseEagleeyeData d = new AnalyseEagleeyeData();
//		
//		
//		try {
//			d.analyseData(sdf.parse("2012-06-29"));
//			System.out.println("0k");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//	}
//	
//	
//	private class ReferCount{
//		public String sourceURL;
//		
//		public String selfName;
//		
//		private Map<String,Integer> childMap = new HashMap<String, Integer>();
//		
//	}
//	
//
//
//}
