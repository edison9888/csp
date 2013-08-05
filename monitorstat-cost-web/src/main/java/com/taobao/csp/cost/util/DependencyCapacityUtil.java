package com.taobao.csp.cost.util;

import java.util.HashMap;
import java.util.Map;

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
	

}
