package com.taobao.csp.cost.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

/***
 * The basic util class
 * @author youji.zj
 *
 */
public class LocalUtil {
	
	public static Date getYesToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}
	
	/***
	 * ��ʱд��6��14�գ�db���������ֶ������Ϊ14�գ��ȶ����ٸ�
	 * @return
	 */
	public static Date getCapacityCostDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2012);
		calendar.set(Calendar.MONTH, 5);  // 5��ʾ6��
		calendar.set(Calendar.DAY_OF_MONTH, 14);
		// calendar.add(Calendar.DAY_OF_MONTH, -2);
		return calendar.getTime();
	}
	
	public static String transferUnit(long num) {
		String value = String.valueOf(num);
		
		if (num >= 10000) {
			BigDecimal divident = new BigDecimal(value);
			BigDecimal divisor = new BigDecimal("10000");
			BigDecimal answer = divident.divide(divisor, 2, RoundingMode.HALF_UP);
			value = String.valueOf(answer.doubleValue()) + "��";
		}
		
		return value;
	}

}
