
package com.taobao.monitor.web.baseline;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.baseline.core.AverageCompute;
import com.taobao.monitor.baseline.core.BaseLineValue;
import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.ao.MonitorBaseLineAo;
import com.taobao.monitor.web.ao.MonitorTimeAo;

/**
 * 
 * @author xiaodu
 * @version 2010-8-27 下午12:32:57
 */
public class BaseLineManage {
	
	private static  Logger log = Logger.getLogger(BaseLineManage.class);
	
	private static BaseLineManage bb = new BaseLineManage();
	
	private static int TIME_SIZE = 42;
	
	public static BaseLineManage get(){
		return bb;
	}
	
	public Map<Integer,BaseLineValue> change(List<BaseLineValue> t){
		
		Map<Integer,BaseLineValue> map = new HashMap<Integer, BaseLineValue>();
		
		for(BaseLineValue bv:t){
			map.put(bv.getTime(), bv);
		}
		return map;
	}
	
	/**
	 * 预测值的下降
	 * 相对于当前值，向前走30分钟，如果当前值在向前30分钟中，都只在一个下降的趋势，且所有值都在基线的之下	 
	 * 基线值必须在 200以上
	 * @param appId
	 * @param keyid
	 */
	public double calculateValueDown(int appId,int keyid){
		
		Calendar cal = Calendar.getInstance();		
//		cal.set(Calendar.HOUR_OF_DAY, 8);
//		cal.set(Calendar.MINUTE, 45);
		
		Date current = cal.getTime();
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> currentTimeList = new ArrayList<String>();
		
		for(int i=2;i<TIME_SIZE;i++){
			cal.add(Calendar.MINUTE, -1);
			currentTimeList.add(sdf.format(cal.getTime()));
		}
		Map<String, KeyValueBaseLinePo> baseLineMap = MonitorBaseLineAo.get().findKeyBaseValueByDate(appId, keyid);
		
		if(baseLineMap==null||baseLineMap.size()==0){
			return -1;
		}
		Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId ,keyid, current);
		
				
		if(currentMap==null||currentMap.size()==0){
			return -1;
		}
		
		int size = 0;
		for(int i=0;i<currentTimeList.size()-1;i++){
			String time1 = currentTimeList.get(i);
			String time2 = currentTimeList.get(i+1);
			KeyValuePo p1 = currentMap.get(time1);
			KeyValuePo p2 = currentMap.get(time2);
			if(p1==null||p2==null){
				continue;
			}
			double v1 = Double.parseDouble(p1.getValueStr());
			double v2 = Double.parseDouble(p2.getValueStr());
			if(v1 < v2){
				size++;
			}
		}
		
		//递减的数据必须是超过90%
		if(Arith.div(size, currentTimeList.size()) <0.9){
			return -1;
		}
		
		
		
		m3(currentMap);		
		int down = 0;		
		int all = 0;		
		
		List<Double> plist = new ArrayList<Double>();
		for(String time:currentTimeList){			
			KeyValueBaseLinePo b = baseLineMap.get(time);
			KeyValuePo v = currentMap.get(time);			
			if(b!=null&&v!=null){
				double g = Double.parseDouble(v.getValueStr());
				plist.add(Arith.sub(g, b.getBaseLineValue()));
				all++;
				if(g<b.getBaseLineValue()&&b.getBaseLineValue()-g>5){	
					down++;
				}
			}
		}
		
		if(all>10){
			if(down == all){
				//最后5个点的数据必须是在基线的45%以下,如果不满足条件,就退出
				for(int i=currentTimeList.size()-1,offset=0;i>-1&&offset<5;i--){
					String time = currentTimeList.get(i);
					KeyValueBaseLinePo b = baseLineMap.get(time);
					KeyValuePo v = currentMap.get(time);			
					if(b!=null&&v!=null){
						double g = Double.parseDouble(v.getValueStr());
						double scole = Arith.div(g,b.getBaseLineValue());
						if(scole > 0.55){
							return -1;
						}
						offset++;
					}					
				}								
				//在抽样时间内的，实际值与基线值的差值，利用这个差值来计算角度
				double allSub = 0;
				for(int i=0;i<plist.size()-1;i++){
					double v = Arith.sub(plist.get(i), plist.get(i+1));
					allSub = Arith.add(allSub, v);
				}
				if(allSub >=0){
					return -1;
				}
				//将这个增长值 与在抽样时间内的 基线的平均值的一个比值
				double allBase = 0;
				for(String time:currentTimeList){
					KeyValueBaseLinePo b = baseLineMap.get(time);
					allBase = Arith.add(allBase,b.getBaseLineValue());
				}
				allBase = Arith.div(allBase, currentTimeList.size());
				double upScole = Arith.div(Math.abs(allSub), allBase,4);
				if(upScole>0){
					return upScole;
				}
			}
		}		
		return -1;
	}
	
	
	
	/**
	 * 预测key的增长，
	 * 相对于当前值，向前走30分钟，如果当前值在向前30分钟中，都只在一个上升的趋势，且所有值都在基线的之上
	 * 如果这个当前值 保持在实际值5以下忽略。
	 * 一些接口的响应时间都在是1到2ms 范围内，
	 * @param appId
	 * @param keyid
	 * @return
	 */
	public double calculateValueUpper(int appId,int keyid){
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 2);
		
		Date current = cal.getTime();
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		List<String> currentTimeList = new ArrayList<String>();
		for(int i=2;i<TIME_SIZE;i++){
			cal.add(Calendar.MINUTE, -1);
			currentTimeList.add(sdf.format(cal.getTime()));
		}
				
		
		Map<String, KeyValueBaseLinePo> baseLineMap = MonitorBaseLineAo.get().findKeyBaseValueByDate(appId, keyid);
		
		if(baseLineMap==null||baseLineMap.size()==0){
			return -1;
		}		
		
		Map<String, KeyValuePo> currentMap = MonitorTimeAo.get().findKeyValueByDate(appId,keyid, current);
		
		if(currentMap==null||currentMap.size()==0){
			return -1;
		}
		
		int size = 0;
		for(int i=0;i<currentTimeList.size()-1;i++){
			String time1 = currentTimeList.get(i);
			String time2 = currentTimeList.get(i+1);
			KeyValuePo p1 = currentMap.get(time1);
			KeyValuePo p2 = currentMap.get(time2);
			if(p1==null||p2==null){
				continue;
			}
			double v1 = Double.parseDouble(p1.getValueStr());
			double v2 = Double.parseDouble(p2.getValueStr());
			if(v1 > v2){
				size++;
			}
		}
		
		//递增的数据必须是超过90%
		if(Arith.div(size, currentTimeList.size()) <0.9){
			return -1;
		}
		
		
		
		m3(currentMap);		
		
		int upper = 0;		
		int all = 0;		
		
		List<Double> plist = new ArrayList<Double>();//当前值 减去 基线
		for(String time:currentTimeList){			
			KeyValueBaseLinePo b = baseLineMap.get(time);
			KeyValuePo v = currentMap.get(time);			
			if(b!=null&&v!=null){
				double g = Double.parseDouble(v.getValueStr());
				all++;
				plist.add(Arith.sub(g, b.getBaseLineValue()));
				
				if(g>5&&g>b.getBaseLineValue()&&g-b.getBaseLineValue()>5){	
					upper++;
				}
			}
		}
		
		if(all>10){
			if(all == upper){
				
				//最后5个点的数据必须是在基线的45%以上,如果不满足条件,就退出
				for(int i=currentTimeList.size()-1,offset=0;i>-1&&offset<5;i--){
					String time = currentTimeList.get(i);
					KeyValueBaseLinePo b = baseLineMap.get(time);
					KeyValuePo v = currentMap.get(time);			
					if(b!=null&&v!=null){
						double g = Double.parseDouble(v.getValueStr());
						double scole = Arith.div(g,b.getBaseLineValue());
						if(scole < 1.45){
							return -1;
						}
						offset++;
					}					
				}								
				//在抽样时间内的，实际值与基线值的差值，利用这个差值来计算角度
				double allSub = 0;
				for(int i=0;i<plist.size()-1;i++){
					double v = Arith.sub(plist.get(i), plist.get(i+1));
					allSub = Arith.add(allSub, v);
				}
				if(allSub <0){
					return -1;
				}
				//将这个增长值 与在抽样时间内的 基线的平均值的一个比值
				double allBase = 0;
				for(String time:currentTimeList){
					KeyValueBaseLinePo b = baseLineMap.get(time);
					allBase = Arith.add(allBase,b.getBaseLineValue());
				}
				allBase = Arith.div(allBase, currentTimeList.size());
				double upScole = Arith.div(allSub, allBase,4);
				if(upScole>0){
					return upScole;
				}
			}
		}		
		return -1;
	}
		
	public void m1(Map<String, KeyValuePo> currentMap){
	
		List<KeyValuePo> currentList = new ArrayList<KeyValuePo>();
		currentList.addAll(currentMap.values());
		Collections.sort(currentList);
		
		double[] values = new double[currentList.size()];
		for(int i=currentList.size()-1,j=0;i>-1;i--,j++){
			values[j] = Double.parseDouble(currentList.get(i).getValueStr());
		}
		double[] new_values =AverageCompute.expma(values);
		
		for(int i=currentList.size()-1,j=0;i>-1;i--,j++){
			currentList.get(i).setValueStr(new_values[j]+"");
		}
	}
	/**
	 * 当前值 与前面2个值的平均
	 * @param currentMap
	 */
	public void m2(Map<String, KeyValuePo> currentMap){
		
		List<KeyValuePo> currentList = new ArrayList<KeyValuePo>();
		currentList.addAll(currentMap.values());
		Collections.sort(currentList);
		double[] values = new double[currentList.size()];
		for(int i=0;i<currentList.size()-2;i++){
			String value1 = currentList.get(i).getValueStr();
			String value2 = currentList.get(i+1).getValueStr();
			String value3 = currentList.get(i+2).getValueStr();
			values[i] = Arith.div(Double.parseDouble(value1)+Double.parseDouble(value2)+Double.parseDouble(value3), 3);
		}
		
		for(int i=0;i<values.length-2;i++){
			currentList.get(i).setValueStr(values[i]+"");
		}
		
	}
	
	
	
	/**
	 * 当前值 与 它的前两个点和后两个点的比较
	 *  B2 B1 B0 A0  F0 F1 F2
	 *   
	 *   
	 * 
	 * @param currentMap
	 */
	public void m3(Map<String, KeyValuePo> currentMap){
		int offset = 3;
		
		List<KeyValuePo> currentList = new ArrayList<KeyValuePo>();
		currentList.addAll(currentMap.values());
		Collections.sort(currentList,new Comparator<KeyValuePo>(){
			public int compare(KeyValuePo o1, KeyValuePo o2) {
				if(o1.getCollectTime().before(o2.getCollectTime())){
					return -1;
				}else if(o1.getCollectTime().equals(o2.getCollectTime())){
					return 0;
				}else if(o1.getCollectTime().after(o2.getCollectTime())){
					return 1;
				}
				
				
				return 0;
			}			
		});
		
		for(int i=0;i<currentList.size();i++){
			String v = currentList.get(i).getValueStr();
			
			double bValue = 0;
			int bSize = 0;
			for(int b=i-offset;b<i&&b>-1;b++){
				bValue = Arith.add(bValue, Double.parseDouble(currentList.get(b).getValueStr()));
				bSize++;
			}
			double fValue = 0;
			int fSize = 0;
			for(int f=i+offset;i<f&&f<currentList.size();f--){
				fValue = Arith.add(fValue, Double.parseDouble(currentList.get(f).getValueStr()));
				fSize++;
			}
			
			if(bSize==offset&&fSize==offset){
				bValue = Arith.div(bValue, bSize, 4);
				fValue = Arith.div(fValue, fSize, 4);
				double s = Double.parseDouble(v);
				double average = Arith.div(Arith.add(bValue, fValue), 2);
				if(s<Arith.mul(average, 0.5)||s>Arith.mul(average, 1.5)){
					//如果当前点的值是 前后三个点平均 且大于1.5倍 ，说明这个是一个异常点
					currentList.get(i).setValueStr(average+"");
				}
			}
			
			
		}
		
	}
 	
	public List<KeyValueBaseLinePo> createBaseLine(int appId,int keyid,Date time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		
		DecimalFormat formate = new DecimalFormat("0000");
		
		String day = sdf1.format(time);
		List<BaseLineValue> bvlist = baseLineByExpma(appId,keyid,time);
		if(bvlist == null){
			return null;
		}
		
		List<KeyValueBaseLinePo> list = new ArrayList<KeyValueBaseLinePo>();
		
		for(BaseLineValue v:bvlist){
			try {				
				String t = day+" "+formate.format(v.getTime());				
				KeyValueBaseLinePo keyBasePo = new KeyValueBaseLinePo();
				keyBasePo.setAppId(appId);
				keyBasePo.setKeyId(keyid);
				keyBasePo.setBaseLineValue(v.getValue());
				keyBasePo.setCollectTime(sdf.parse(t));
				//MonitorBaseLineAo.get().addMonitorDateBaseLine(keyBasePo);
				list.add(keyBasePo);
			} catch (Exception e) {
				log.error("createBaseLine(int appId,int keyid,Date time),异常",e);
			}
		}
		return list;
	}
	
	public void createBaseLine(){
		log.info("createBaseLine启动：" + new Date().toString());
		MonitorBaseLineAo.get().deleteAllBaseLine();//删除基线表所有记录
		
		Calendar cal = Calendar.getInstance();
		
		List<AppInfoPo> allApps = AppInfoAo.get().findAllEffectiveAppInfo();
		
		for(AppInfoPo po:allApps){
			Date start = new Date();
			log.info("appName=" + po.getAppName() + ",appId=" + po.getAppId() + ",Start Time:" + start.toString());
			
			int appId = po.getAppId();
			// 只获取报警的key修改成获取需要插入到baseline表的key
			List<KeyPo> keyList = KeyAo.get().findAllMonitorBaseLineKey(appId);			
			//List<AlarmDataPo> keyList =  MonitorAlarmAo.get().findAllAlarmKeyByAimAndLikeName(appId,null);
			log.info("appName=" + po.getAppName() + ",keyList.size=" + keyList.size());
			for(KeyPo key : keyList){
				List<KeyValueBaseLinePo> list = createBaseLine(appId, key.getKeyId(), cal.getTime());
				if(list != null && list.size() > 0) {
					log.info("keyId=" + key.getKeyId() + ",list.size()=" + list.size());
					MonitorBaseLineAo.get().addMonitorDateBaseLineByList(list);
				}
			}
			
			Date end = new Date();
			log.info("Over Time:" + end.toString() + ";运行时间：" + (end.getTime() - start.getTime()));
		}
		log.info("createBaseLine结束：" + new Date().toString());
	}
	
	
	/**
	 * 六个星期基线  expma 算法
	 * @param appId
	 * @param keyId
	 * @param collectTime
	 * @return
	 */
	public List<BaseLineValue> baseLineByExpma(int appId,int keyId,Date collectTime){
		try{
			Calendar currentCalendar = Calendar.getInstance();
			currentCalendar.setTime(collectTime);
			
			Map<String, List<KeyValuePo>> keyListMap = new HashMap<String, List<KeyValuePo>>();
			
			for(int i=0;i<6;i++){
				currentCalendar.add(Calendar.DAY_OF_MONTH, -7);
				Date previousDate = currentCalendar.getTime();					
				Map<String, KeyValuePo> keyMap1 =MonitorTimeAo.get().findKeyValueByDate(appId,keyId, previousDate);
				
				for(Map.Entry<String, KeyValuePo> entry:keyMap1.entrySet()){
					List<KeyValuePo> list = keyListMap.get(entry.getKey());
					if(list == null){
						list = new ArrayList<KeyValuePo>();
						keyListMap.put(entry.getKey(), list);
					}
					list.add(entry.getValue());				
				}			
			}
			
			List<BaseLineValue> b = BaseLine.expma(keyListMap);
			return b;
		}catch (Exception e) {
			log.error("baseLineByExpma异常：", e);
		}
		return null;
	}
	
	
	
	
	
	
	public String test(int appId,int keyId,Date collectTime){
		List<BaseLineValue> t0= test00(appId,keyId,collectTime);
		List<BaseLineValue> t1 = test01(appId,keyId,collectTime);
//		List<BaseLineValue> t1 = baseLineByExpma(appId,keyId,collectTime);
//		List<BaseLineValue> t2 = test2(appId,keyId,collectTime);
//		List<BaseLineValue> t3 = test3(appId,keyId,collectTime);
		
		Set<Integer> setTime = new HashSet<Integer>();
		for(BaseLineValue bv:t0){
			setTime.add(bv.getTime());
		}
		for(BaseLineValue bv:t1){
			setTime.add(bv.getTime());
		}
//		for(BaseLineValue bv:t2){
//			setTime.add(bv.getTime());
//		}
		
		
		List<Integer> dateList = new ArrayList<Integer>();
		dateList.addAll(setTime);
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("<chart>");
		Collections.sort(dateList);
		sb.append("<series>");
		
		for(int i=0;i<dateList.size();i++){	
			sb.append("<value xid='"+i+"'>"+dateList.get(i)+"</value>");
		}
		sb.append("</series>");
		sb.append("<graphs>");
		{
			sb.append("<graph gid='0' title='t0'>");
			Map<Integer,BaseLineValue> mapt = change(t0);
			for(int i=0;i<dateList.size();i++){	
				
				BaseLineValue bvl = mapt.get(dateList.get(i));
				if(bvl!=null){
					sb.append("<value xid='"+i+"'>"+bvl.getValue()+"</value>");
				}else{
					sb.append("<value xid='"+i+"'>"+0+"</value>");
				}
			}
			
			sb.append("</graph>");
		}
		{
			sb.append("<graph gid='1' title='t1'>");
			Map<Integer,BaseLineValue> mapt1 = change(t1);
			for(int i=0;i<dateList.size();i++){	
				
				BaseLineValue bvl = mapt1.get(dateList.get(i));
				if(bvl!=null){
					sb.append("<value xid='"+i+"'>"+bvl.getValue()+"</value>");
				}else{
					sb.append("<value xid='"+i+"'>"+0+"</value>");
				}
			}
			
			sb.append("</graph>");
		}
//		{
//			sb.append("<graph gid='2' title='average'>");
//			Map<Integer,BaseLineValue> mapt2 = change(t2);
//			for(int i=0;i<dateList.size();i++){	
//				
//				BaseLineValue bvl = mapt2.get(dateList.get(i));
//				if(bvl!=null){
//					sb.append("<value xid='"+i+"'>"+bvl.getValue()+"</value>");
//				}else{
//					sb.append("<value xid='"+i+"'>"+0+"</value>");
//				}
//			}
//			
//			sb.append("</graph>");
//		}
//		{
//			sb.append("<graph gid='3' title='average_new'>");
//			Map<Integer,BaseLineValue> mapt3 = change(t3);
//			for(int i=0;i<dateList.size();i++){	
//				
//				BaseLineValue bvl = mapt3.get(dateList.get(i));
//				if(bvl!=null){
//					sb.append("<value xid='"+i+"'>"+bvl.getValue()+"</value>");
//				}else{
//					sb.append("<value xid='"+i+"'>"+0+"</value>");
//				}
//			}
//			
//			sb.append("</graph>");
//		}
		sb.append("</graphs>");			
		sb.append("</chart>");
		
		return sb.toString();
	}
	
	
	
	
	
	
	
	public List<BaseLineValue> test00(int appId,int keyId,Date collectTime){
		
		List<BaseLineValue> list = new ArrayList<BaseLineValue>();
		
		Map<String, KeyValuePo> pvMap =MonitorTimeAo.get().findKeyValueByDate(appId,keyId, collectTime);
		m3(pvMap);
		for(Map.Entry<String, KeyValuePo> entry:pvMap.entrySet()){
			
			int time = Integer.parseInt(entry.getKey().replaceAll(":", ""));
			KeyValuePo po = entry.getValue();
			BaseLineValue b = new BaseLineValue();
			b.setTime(time);
			b.setValue(Double.parseDouble(po.getValueStr()));
			list.add(b);
		}		
		return list;
	}
	public List<BaseLineValue> test01(int appId,int keyId,Date collectTime){
		
		List<BaseLineValue> list = new ArrayList<BaseLineValue>();
		
		Map<String, KeyValueBaseLinePo> pvMap =MonitorBaseLineAo.get().findKeyBaseValueByDate(appId, keyId);
		for(Map.Entry<String, KeyValueBaseLinePo> entry:pvMap.entrySet()){
			
			int time = Integer.parseInt(entry.getKey().replaceAll(":", ""));
			KeyValueBaseLinePo po = entry.getValue();
			BaseLineValue b = new BaseLineValue();
			b.setTime(time);
			b.setValue(po.getBaseLineValue());
			list.add(b);
		}		
		return list;
	}
	
	
	
	
	
	
//	/**
//	 * 六个星期基线 算术平均
//	 * @param appId
//	 * @param keyId
//	 * @param collectTime
//	 * @return
//	 */
//	public List<BaseLineValue> test2(int appId,int keyId,Date collectTime){
//		Calendar currentCalendar = Calendar.getInstance();
//		currentCalendar.setTime(collectTime);
//		
//		Map<String, List<KeyValuePo>> keyListMap = new HashMap<String, List<KeyValuePo>>();
//		
//		for(int i=0;i<6;i++){
//			currentCalendar.add(Calendar.DAY_OF_MONTH, -7);
//			Date previousDate = currentCalendar.getTime();					
//			Map<String, KeyValuePo> keyMap1 =MonitorTimeAo.get().findKeyIdByDate1(keyId,appId, previousDate);
//			
//			for(Map.Entry<String, KeyValuePo> entry:keyMap1.entrySet()){
//				List<KeyValuePo> list = keyListMap.get(entry.getKey());
//				if(list == null){
//					list = new ArrayList<KeyValuePo>();
//					keyListMap.put(entry.getKey(), list);
//				}
//				list.add(entry.getValue());				
//			}			
//		}
//		
//		List<BaseLineValue> b = BaseLine.average(keyListMap);
//		return b;
//	}
	
	
//	public List<BaseLineValue> test3(int appId,int keyId,Date collectTime){
//		Calendar currentCalendar = Calendar.getInstance();
//		currentCalendar.setTime(collectTime);
//		
//		Map<String, List<KeyValuePo>> keyListMap = new HashMap<String, List<KeyValuePo>>();
//		
//		for(int i=0;i<6;i++){
//			currentCalendar.add(Calendar.DAY_OF_MONTH, -7);
//			Date previousDate = currentCalendar.getTime();					
//			Map<String, KeyValuePo> keyMap1 =MonitorTimeAo.get().findKeyIdByDate1(keyId,appId, previousDate);
//			
//			for(Map.Entry<String, KeyValuePo> entry:keyMap1.entrySet()){
//				List<KeyValuePo> list = keyListMap.get(entry.getKey());
//				if(list == null){
//					list = new ArrayList<KeyValuePo>();
//					keyListMap.put(entry.getKey(), list);
//				}
//				list.add(entry.getValue());				
//			}			
//		}
//		
//		List<BaseLineValue> b = BaseLine.compute1(keyListMap);
//		return b;
//	}
	
	
	
	public static void main(String[] args){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		Calendar currentCalendar = Calendar.getInstance();
//		currentCalendar.set(Calendar.DAY_OF_MONTH, 20);
//		for(int i=0;i<6;i++){
//			currentCalendar.add(Calendar.DAY_OF_MONTH, -7);
//			Date previousDate = currentCalendar.getTime();					
//		}
//		
//		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH,8);
		String s = BaseLineManage.get().test(26,175,cal.getTime());
//		BaseLineManage.get().createBaseLine(2, 3113, cal.getTime());
		
		double b1 = BaseLineManage.get().calculateValueDown(26, 175);
		double b2 = BaseLineManage.get().calculateValueUpper(26, 175);
	}
	
}
