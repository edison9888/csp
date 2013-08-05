package com.taobao.csp.capacity.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.capacity.po.DepCapacityPo;
import com.taobao.csp.capacity.web.action.assist.DepDisplayObject;
import com.taobao.monitor.common.util.Arith;

/***
 * util class of capacity system
 * the last day of 2011
 * @author youji.zj
 *
 */
public class DependencyCapacityUtil {
	
	/***
	 * analyze dependency qps of rooms. The format in dependency database is like CM4:649$0.0$3.92#CM3:937$0.0$4.14#
	 * @param roomQpsStirng
	 * @return
	 */
	public static Map<String, Double> generateRoomQps(String roomQpsStirng){
		Map<String, Double> roomQpsMap = new HashMap<String, Double>();
		String [] roomQpsArray = roomQpsStirng.split("#");
		for (String roomQps : roomQpsArray) {
			String [] qps = roomQps.split(":");
			if (qps.length == 2) {
				String [] values = qps[1].split("\\$");
				if (values.length > 2) {
					roomQpsMap.put(qps[0], Double.valueOf(values[1]));
				}
			}
		}
		return roomQpsMap;
	}
	
	public static List<DepDisplayObject> generateAllDisplayDep(List<DepCapacityPo> depCapacityList, double qps) {
		List<DepDisplayObject> list = new ArrayList<DepDisplayObject>();
		
		// transform one by one
		for (DepCapacityPo depCapacity : depCapacityList) {
			DepDisplayObject depDisplayObject = new DepDisplayObject();
			depDisplayObject.setAppName(depCapacity.getConsumerApp());
			depDisplayObject.setAverageQps(depCapacity.getDepQps());
			double ratio = Arith.div(depCapacity.getDepQps(), qps, 4);
			double ratioPercent = Arith.mul(ratio, 100);
			depDisplayObject.setRatio(ratioPercent);
			
			Map<String, Double> roomQpsMap = depCapacity.getRoomQpsMap();
			for (Map.Entry<String, Double> roomQps : roomQpsMap.entrySet()) {
				String key = roomQps.getKey();
				double value = roomQps.getValue().doubleValue();
				depDisplayObject.getRoomQps().put(key, value);
			}
			list.add(depDisplayObject);
		}
		computeInfluence(list);
		return list;
	}
	
	public static Map<String, List<DepDisplayObject>> generateGroupDisplayDep(Map<String, List<DepCapacityPo>> depCapacityMap, double qps) {
		Map<String, List<DepDisplayObject>> map = new HashMap<String, List<DepDisplayObject>>();
		for (Map.Entry<String, List<DepCapacityPo>> depCapacityEntry: depCapacityMap.entrySet()) {
			String key = depCapacityEntry.getKey();
			List<DepCapacityPo> value = depCapacityEntry.getValue();
			List<DepDisplayObject> depObjectList;
			if (!map.keySet().contains(key)) {
				depObjectList = new ArrayList<DepDisplayObject>();
				map.put(key, depObjectList);
			} else {
				depObjectList = map.get(key);
			}
			
			// transform one by one
			for (DepCapacityPo depCapacity : value) {
				DepDisplayObject depDisplayObject = new DepDisplayObject();
				depDisplayObject.setAppName(depCapacity.getConsumerApp());
				depDisplayObject.setAverageQps(depCapacity.getDepQps());
				double ratio = Arith.div(depCapacity.getDepQps(), qps, 4);
				double ratioPercent = Arith.mul(ratio, 100);
				depDisplayObject.setRatio(ratioPercent);
				
				Map<String, Double> roomQpsMap = depCapacity.getRoomQpsMap();
				for (Map.Entry<String, Double> roomQps : roomQpsMap.entrySet()) {
					String roomKey = roomQps.getKey();
					double roomValue = roomQps.getValue().doubleValue();
					depDisplayObject.getRoomQps().put(roomKey, roomValue);
				}
				depObjectList.add(depDisplayObject);
			}
			
		}
		for (Map.Entry<String, List<DepDisplayObject>> depEntryList: map.entrySet()) {
			computeInfluence(depEntryList.getValue());
		}
		return map;
	}
	
	public static String generateAllPie(List<DepDisplayObject> allDepDisplay) {		
		Map<String, Integer> data = new LinkedHashMap<String, Integer>();
		double other = 0d;
		for (int i = 0; i < allDepDisplay.size(); i++) {
			DepDisplayObject po = allDepDisplay.get(i);
			if (i < 5) {
				String name = po.getAppName();
				int influence = (int)Math.round(po.getAverageQps());
				data.put(name, influence);
			} else {
				other += po.getAverageQps();
			}
			
		}
		if (other != 0) {
			data.put("others", (int)Math.round(other));
		}
		
		// Ã»ÓÐÒÀÀµ
		if (data.size() == 0) {
			data.put("none", 1);
		}
		return AmLineFlash.createPieXml(data);	
	}
	
	public static Map<String, String> generateGroupPie(Map<String, List<DepDisplayObject>> groupDepDisplay) {
		Map<String, String> pie = new ConcurrentHashMap<String, String>();
		for (Map.Entry<String, List<DepDisplayObject>> entry : groupDepDisplay.entrySet()) {
			List<DepDisplayObject> list = entry.getValue();
			if (list.size() > 0) {
				String group = generateAllPie(list);
				pie.put(entry.getKey(), group);
			}
		}
		return pie;
	}
	
	private static void computeInfluence(List<DepDisplayObject> depList) {
		double totalDepQps = 0d;
		
		// first sum
		for (DepDisplayObject depObject : depList) {
			totalDepQps = totalDepQps + depObject.getAverageQps();
		}
		
		// then compute influence each
		for (DepDisplayObject depObject : depList) {
			double influence = Arith.div(depObject.getAverageQps(), totalDepQps, 4);
			double influencePercent = Arith.mul(influence, 100);
			depObject.setInfluence(influencePercent);
		}
	}

}
