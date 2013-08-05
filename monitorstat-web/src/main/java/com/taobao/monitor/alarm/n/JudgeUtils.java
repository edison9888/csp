package com.taobao.monitor.alarm.n;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JudgeUtils {
	public static List<Date> sortDateDesc(Map<Date, String> dataMap) {

		List<Date> dataList = new ArrayList<Date>();
		for (Date date : dataMap.keySet()) {
			dataList.add(date);
		}
		Collections.sort(dataList, new Comparator<Date>() {

			public int compare(Date o1, Date o2) {

				long thisTime = o1.getTime();
				long anotherTime = o2.getTime();
				return (thisTime < anotherTime ? 1 : (thisTime == anotherTime ? 0 : -1));
			}
		});
		if (dataList.size() > 0) {
			return dataList;
		} else {
			return null;
		}
	}
	
	/**
	 * 将时间解析成 整型 HHmm
	 * @param date
	 * @return 整型 HHmm
	 */
	public static int parseDateToNumber(Date date){
		if(date==null)return 0;		
		String str = sdf.format(date);		
		return  Integer.parseInt(str);
	}
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HHmm");

}
