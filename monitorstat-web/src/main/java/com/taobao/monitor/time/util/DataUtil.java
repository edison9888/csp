/**
 * monitorstat-time-web
 */
package com.taobao.monitor.time.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author xiaodu
 * 
 *         下午12:27:08
 */
public class DataUtil {

	public static int transformInt(Object value) {
		
		if(value == null){
			return 0;
		}

		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof Long) {
			return ((Long) value).intValue();
		}
		if (value instanceof Double) {
			return ((Double) value).intValue();
		}
		if (value instanceof Float) {
			return ((Float) value).intValue();
		}
		return (int) Double.parseDouble(value.toString());
	}

	public static long transformLong(Object value) {

		if(value == null){
			return 0;
		}
		
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof Long) {
			return ((Long) value);
		}
		if (value instanceof Double) {
			return ((Double) value).longValue();
		}
		if (value instanceof Float) {
			return ((Float) value).intValue();
		}
		return (long) Double.parseDouble(value.toString());
	}

	public static float transformFloat(Object value) {
		
		if(value == null){
			return 0;
		}

		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof Long) {
			return ((Long) value).intValue();
		}
		if (value instanceof Double) {
			
			
			Double d = (Double)value;
			if(Double.isInfinite(d)||Double.isNaN(d)){
				return 0;
			}
			
			BigDecimal bd = new BigDecimal(d);
			bd = bd.setScale(2, RoundingMode.HALF_EVEN);
			return bd.floatValue();
		}
		if (value instanceof Float) {
			
			Float f = (Float)value;
			if(Float.isInfinite(f)||Float.isNaN(f)){
				return 0;
			}
			
			BigDecimal bd = new BigDecimal(f);
			bd = bd.setScale(2, RoundingMode.HALF_EVEN);
			return bd.floatValue();
		}
		
		BigDecimal bd = new BigDecimal( Double.parseDouble(value.toString()));
		bd = bd.setScale(2, RoundingMode.HALF_EVEN);

		return bd.floatValue();
	}

	public static double transformDouble(Object value) {
		
		
		if(value == null){
			return 0;
		}

		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof Long) {
			return ((Long) value).longValue();
		}
		if (value instanceof Double) {
			
			Double d = (Double)value;
			if(Double.isInfinite(d)||Double.isNaN(d)){
				return 0;
			}
			
			BigDecimal bd = new BigDecimal(d);
			bd = bd.setScale(2, RoundingMode.HALF_EVEN);
			return bd.doubleValue();
		}
		if (value instanceof Float) {
			Float f = (Float)value;
			if(Float.isInfinite(f)||Float.isNaN(f)){
				return 0;
			}
			
			BigDecimal bd = new BigDecimal(f);
			bd = bd.setScale(2, RoundingMode.HALF_EVEN);
			return bd.doubleValue();
		}
		
		BigDecimal bd = new BigDecimal(Double.parseDouble(value.toString()));
		bd = bd.setScale(2, RoundingMode.HALF_EVEN);
		return bd.doubleValue();
	}

	public static String transformString(Object value) {

		if (value == null) {
			return "";
		}
		return value.toString();
	}

	public static double rate(long dest, long source) {
		if (source == 0) {
			return 100d;
		}
		BigDecimal b1 = new BigDecimal(dest);
		BigDecimal b2 = new BigDecimal(source);
		BigDecimal b3 = new BigDecimal(100);
		return b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).multiply(b3)
				.doubleValue();

	}

	public static String scale(double srcD, double targetD) {

		DecimalFormat df1 = new DecimalFormat("0.##");

		if (srcD < 0 || targetD < 0) {
			return "-";
		}

		try {

			if (srcD == 0 && targetD != 0) {
				return "<font style=\"color: green;\">-</font>";
			}
			if (targetD == 0 && srcD != 0) {
				return "<font style=\"color: red;\">-</font>";
			}
			if (srcD == 0 && targetD == 0) {
				return "-";
			}

			double offset = srcD - targetD;

			double scale = (offset / targetD) * 100;

			if (scale > 999) {// 如果百分比太大就
				scale = 999;
			}

			if (scale < 0) {
				scale = Math.abs(scale); // 取绝对值。页面只通过↓来标记
				return "<font style=\"color: green;\">↓" + df1.format(scale)
						+ "%</font>";
			} else if (scale == 0) {
				return "0.00%";
			} else {
				return "<font style=\"color: red;\">↑" + df1.format(scale)
						+ "%</font>";
			}
		} catch (Exception e) {

		}
		return srcD + "/" + targetD + "异常";
	}

	public static double round(double value, int scale, int roundingMode) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale, roundingMode);
		double d = bd.doubleValue();
		bd = null;
		return d;
	}
}
