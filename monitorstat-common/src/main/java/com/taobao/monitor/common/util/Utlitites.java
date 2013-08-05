package com.taobao.monitor.common.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utlitites {

	public static String formatHtmlStr(String str, int returnNum) {
		StringBuilder sb = new StringBuilder();
		if (str != null) {
			int len = str.length();
			for (int i = 0; i < len;) {
				if (i + returnNum > len) {
					sb.append(str.substring(i, len));
				} else {
					sb.append(str.substring(i, i + returnNum));
					sb.append(" ");
				}
				i += returnNum;
			}

		}
		return sb.toString();
	}

	/**
	 * 取得昨天的 时间
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getMonitorDate() {
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar parseLogCalendar = Calendar.getInstance();
		parseLogCalendar.add(Calendar.DATE, -1);
		parseLogCalendar.set(Calendar.HOUR_OF_DAY, 0);
		parseLogCalendar.set(Calendar.MINUTE, 0);
		parseLogCalendar.set(Calendar.MILLISECOND, 0);

		return parseLogFormatDate.format(parseLogCalendar.getTime());
	}

	/**
	 * 取得日期的 同比 日期 即同个星期
	 * 
	 * @param currentDate
	 *            yyyy-MM-dd
	 * @return yyyy-MM-dd
	 * @throws ParseException
	 */
	public static String getTongBiMonitorDate(String currentDate) throws ParseException {
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = parseLogFormatDate.parse(currentDate);
		Calendar parseLogCalendar = Calendar.getInstance();
		parseLogCalendar.setTime(date);
		parseLogCalendar.add(Calendar.DATE, -7);

		return parseLogFormatDate.format(parseLogCalendar.getTime());

	}

	/**
	 * 取得日期的 环比 日期 即昨天
	 * 
	 * @param currentDate
	 *            yyyy-MM-dd
	 * @return yyyy-MM-dd
	 * @throws ParseException
	 */
	public static String getHuanBiMonitorDate(String currentDate) throws ParseException {
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = parseLogFormatDate.parse(currentDate);
		Calendar parseLogCalendar = Calendar.getInstance();
		parseLogCalendar.setTime(date);
		parseLogCalendar.add(Calendar.DATE, -1);

		return parseLogFormatDate.format(parseLogCalendar.getTime());

	}

	/**
	 * 格式化 数字
	 * 
	 * @param number
	 * @return 0.00
	 */
	public static String formatDouble(double number) {
		DecimalFormat df1 = new DecimalFormat("0.00");

		return df1.format(number);

	}

	public static Double getDouble(String num) {
		try {
			return Double.parseDouble(num);
		} catch (Exception e) {
			return 0d;
		}
	}

	/**
	 * 输出百分比
	 * 
	 * @param src
	 * @param target
	 * @return
	 */
	public static String scale(String src, String target) {

		if (src == null || target == null) {
			return "-";
		}

		double srcD;
		double targetD;
		try {
			srcD = Double.parseDouble(src);
			targetD = Double.parseDouble(target);
		} catch (Exception e) {
			return "-";
		}
		
		return scale(srcD, targetD);
	}

	public static String scale(double srcD, double targetD) {

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

			if (scale < 0) {
				scale = Math.abs(scale);	//取绝对值。页面只通过↓来标记
				return "<font style=\"color: green;\">↓" + formatDouble(scale) + "%</font>";
			} else if (scale == 0) {
				return "0.00%";
			} else {
				return "<font style=\"color: red;\">↑" + formatDouble(scale) + "%</font>";
			}
		} catch (Exception e) {

		}
		return srcD + "/" + targetD + "异常";
	}

	/**
	 * 将long 数字 转成 亿万 单位
	 * 
	 * @param num
	 * @return
	 */
	public static String fromatLong(String num) {
		if (num == null)
			return " - ";

		try {
			int index = num.lastIndexOf(".");
			if (index > -1) {
				num = num.substring(0, num.lastIndexOf("."));
			}
			String[] types = new String[] { "", "万", "亿", "兆","京"};
			String number = Long.parseLong(num) + "";
			String v = "";
			for (int i = 0; i < types.length; i++) {
				int start = number.length() - 4 * (i + 1);
				int end = number.length() - 4 * i;
				if (end <= 0)
					break;
				String _n2 = number.substring(start < 0 ? 0 : start, end);
				Integer _n2_int = Integer.parseInt(_n2);
				if (_n2_int > 0) {
					v = _n2_int + types[i] + v;
				}

				if (start < 0)
					break;

			}
			if ("".equals(v)) {
				v = "0";
			}
			return v;
			// DecimalFormat df = new DecimalFormat();
			// return df.format(Long.parseLong(num));
		} catch (Exception e) {
			return " - ";
		}
	}

	public static long getLong(String num) {

		try {
			int index = num.lastIndexOf(".");
			if (index > -1) {
				num = num.substring(0, num.lastIndexOf("."));
			}
			return Long.parseLong(num);
		} catch (Exception e) {
			return -1l;
		}
	}

	public static String formatArray2Sqlin(String[] args) {

		if (args != null && args.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				sb.append("'" + args[i] + "'");
				if (i < args.length - 1)
					sb.append(",");
			}
			return sb.toString();
		}

		return "";
	}

	public static String formatDotTwo(String num) {
		try {
			Double f = Double.parseDouble(num);
			return formatDotTwo(f);
		} catch (Exception e) {
			return num;
		}
	}

	public static String formatDotTwo(Double num) {
		try {
			DecimalFormat df1 = new DecimalFormat("#.###");
			return df1.format(num);
		} catch (Exception e) {
			return "-";
		}
	}
	
	/**
	 * 重载函数，新增精度,参数不合法则直接取整
	 * @param num
	 * @param scale
	 * @return
	 */
	public static String formatDotTwo(Double num, int scale) {
		String strFormat = "#.#";
		switch(scale) {
		case 0:
			strFormat = "#";
			break;
		case 1:
			strFormat = "#.#";
			break;
		case 2:
			strFormat = "#.##";
			break;
		case 3:
			strFormat = "#.###";
			break;
		case 4:
			strFormat = "#.####";
			break;
		case 5:
			strFormat = "#.#####";
			break;
		default:
			strFormat = "#";
		}
		try {
			DecimalFormat df1 = new DecimalFormat(strFormat);
			return df1.format(num);
		} catch (Exception e) {
			return "-";
		}
	}	

	public static String formatNull(String g) {
		if (g == null) {
			return " - ";
		} else {
			return g;
		}
	}

	/**
	 * 获取文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		if (path != null) {
			File file = new File(path);
			return file.getName();
		}
		return null;
	}

	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		return sdf.format(new Date());

	}

  /**
   * 得到几天前的日期。如果format为null，则采用yyyy-MM-dd格式
   */  
  public static String getDateBefore(Date d, int day, String format) {
    if (format == null) {
      format = "yyyy-MM-dd";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
	  Calendar now = Calendar.getInstance();   
	  now.setTime(d);   
	  now.set(Calendar.DATE, now.get(Calendar.DATE) - day);   
	  return sdf.format(now.getTime());   
	}   
 
	  /**
   * 得到几天后的时间 。如果format为null，则采用yyyy-MM-dd格式 yyyy-MM-dd格式
   */  
  public static String getDateAfter(Date d, int day, String format) {
    if (format == null) {
      format = "yyyy-MM-dd";
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar now = Calendar.getInstance();   
    now.setTime(d);   
    now.set(Calendar.DATE, now.get(Calendar.DATE) + day);   
    return sdf.format(now.getTime());   
  }   
	/**
	 * 得到某一天开始
	 * @param date
	 * @return
	 */
  public static Date getBeginOfTheDay(Date date) {
    if(date == null)
      date = new Date();
    Calendar now = Calendar.getInstance();   
    now.setTime(date);   
    now.set(Calendar.HOUR_OF_DAY, 0);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);
    return now.getTime();
  }
  /**
   * 得到某一天结束
   * @param date
   * @return
   */
  public static Date getEndOfTheDay(Date date) {
    if(date == null)
      date = new Date();
    Calendar now = Calendar.getInstance();   
    now.setTime(date);   
    now.set(Calendar.HOUR_OF_DAY, 23);
    now.set(Calendar.MINUTE, 59);
    now.set(Calendar.SECOND, 59);
    return now.getTime();
  }
  
  public static String getStringOfDate(Date date, String format) {
    String strDate = "";
    try {
      if(format == null)
        format = "yyyy-MM-dd";
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      strDate = sdf.format(date);
    } catch (Exception e) {
    }
    return strDate;
  }
  
	public static URL getResource(String resource) {
		ClassLoader classLoader = null;
		URL url = null;

		try {
			classLoader = getTCL();
			if (classLoader != null) {
				url = classLoader.getResource(resource);
				if (url != null) {
					return url;
				}
			}

			classLoader = Utlitites.class.getClassLoader();
			if (classLoader != null) {

				url = classLoader.getResource(resource);
				if (url != null) {
					return url;
				}
			}
		} catch (Throwable t) {

		}
		return ClassLoader.getSystemResource(resource);
	}

	private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {

		// Are we running on a JDK 1.2 or later system?
		Method method = null;
		try {
			method = Thread.class.getMethod("getContextClassLoader", null);
		} catch (NoSuchMethodException e) {
			// We are running on JDK 1.1
			return null;
		}

		return (ClassLoader) method.invoke(Thread.currentThread(), null);
	}

  /**
   * 判断targetStr是否存在于数组array中
   * 
   * @param array
   * @param targetStr
   * @return
   */
  public static int isStringInArray(String[] array, String targetStr) {
    if (array != null && array.length != 0) {
      int i = 0;
      for (String str : array) {
        if (str.equals(targetStr))
          return i;
        i++;
      }
    }
    return -1;
  }
	public static void main(String[] args) {
//		System.out.println(Utlitites.formatDotTwo(2.3333,0));
//		System.out.println(Utlitites.formatDotTwo(2.3333,1));
//		System.out.println(Utlitites.formatDotTwo(2.3333,2));
//		System.out.println(Utlitites.formatDotTwo(2.3333,3));
//		System.out.println(Utlitites.formatDotTwo(2.3333,4));
//		System.out.println(Utlitites.formatDotTwo(2.3333,-1));
		System.out.println(Utlitites.getBeginOfTheDay(new Date()));
		System.out.println(Utlitites.getEndOfTheDay(new Date()));
	}
}
