package com.taobao.csp.depend.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;

public class MethodUtil implements ConstantParameters {

	private static Logger logger = Logger.getLogger(MethodUtil.class);
	public static final String formatString = "";
	/**
	 * @param selectDate	һ��ʱ��
	 * @return				����NUMBER_OF_DAY_PRE��ǰ��ʱ��
	 */
	public static Date getPreDate(String selectDate) {
		Date date = new Date();
		Date preDate = new Date();
		if(selectDate != null){
			try {
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING);

				date = sdf.parse(selectDate);
				calendar.setTime(date); 
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - NUMBER_OF_DAY_PRE); 
				preDate = calendar.getTime();
			} catch (ParseException e) {
			}
		}	
		return preDate;
	}

	public static Date getDate(String selectDate) {

		//�������ʱ��Ƿ������ʱ����Ϊǰһ��
		if(selectDate == null || "".equals(selectDate) || "null".equals(selectDate)) {
			return getYestoday();
		}

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING);
			date = sdf.parse(selectDate);
		} catch (ParseException e) {
			date = getYestoday();
		}
		return date;
	}	
	
	public static Date getDateThrowExceptions(String selectDate) throws ParseException {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING);
		date = sdf.parse(selectDate);
		return date;
	}		
	
	public static Date getDateByFormat(String selectDate, String formatString) throws ParseException {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		date = sdf.parse(selectDate);
		return date; 
	}
	
	/**
	 * ��ȡ��������ڶ���
	 * @return
	 */
	public static Date getYestoday() {
		Date date = new Date();
		try {
			long dif = new Date().getTime() - 86400 * 1000;
			date.setTime(dif);
			return date;
		} catch (Exception e) {
			date = new Date();
		}
		return date;
	}
	/**
	 * ���չ涨�ĸ�ʽ��Dateת��ΪString
	 * @param date
	 * @return
	 */
	public static String getStringOfDate(Date date) {
		String strDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING);
			strDate = sdf.format(date);
		} catch (Exception e) {
		}
		return strDate;
	}

	public static Date getDaysBefore(Date now, int days) {
		Calendar date = Calendar.getInstance();
		date.setTime(now);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - days);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING);
			Date preDateDays = sdf.parse(sdf.format(date.getTime()));
			return preDateDays;
		} catch (Exception e) {
		}
		return now;
	}

	/**
	 * ʹ����С��Χ�ı仯�ıȽ� ��
	 * @param srcD
	 * @param targetD
	 * @return
	 */
	public static String compare(long srcD, long targetD) {

		if (srcD < 0 || targetD < 0) {
			return "-";
		}

		try {

			if (srcD == 0 && targetD != 0) {
				return "<font style=\"color: green;\"> - </font>";
			}
			if (targetD == 0 && srcD != 0) {
				return "<font style=\"color: red;\"> - </font>";
			}
			if (srcD == 0 && targetD == 0) {
				return " - ";
			}

			long offset = srcD - targetD;

			if (offset < 0) {
				return "<font style=\"color: green;\">�� " + offset+"</font>";
			} else if (offset == 0) {
				return "";
			} else {
				return "<font style=\"color: red;\">�� " +offset+ "</font>";
			}
		} catch (Exception e) {

		}
		return srcD + "/" + targetD + "�쳣";
	}

	/**
	 * ��ȡǰһ�������
	 * @return
	 * @throws ParseException
	 */
	public static String getOneDayPre() {
		try {
			long dif = new Date().getTime() - 86400 * 1000;
			Date date = new Date();
			date.setTime(dif);
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING);
			return sdf.format(date);
		} catch (Exception e) {
		}
		return "";
	}	

	/**
	 * ����URl�������������
	 */
	public static String getStringByUrl(String requestUrl, String encoding) throws Exception {
		if(encoding == null || encoding.trim().equals("")) {
			encoding = "utf-8";
		}
		BufferedInputStream input = null;
		URL url = new URL(requestUrl);
		URLConnection urlCon = url.openConnection();
		urlCon.setDoInput(true);
		urlCon.setConnectTimeout(1000000);
		urlCon.connect();
		input = new BufferedInputStream(urlCon.getInputStream());
		BufferedReader readerf = new BufferedReader(new InputStreamReader(input, encoding));
		String str = null;
		StringBuffer sb = new StringBuffer();
		while ((str = readerf.readLine()) != null) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * ����Json�ַ���������T���͵�����
	 * @param <T>
	 */
	public static <T> T[] getObjectArrayByJsonStr(Class<T> rootClass, String json) {
		JSONArray jsonArray = JSONArray.fromObject(json);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setArrayMode( JsonConfig.MODE_OBJECT_ARRAY );
		jsonConfig.setRootClass(rootClass);
		return (T[]) JSONSerializer.toJava( jsonArray, jsonConfig );    
	}

	/**
	 * ����Json�ַ��������ɶ���
	 * @param <T>
	 */
	public static <T> T getObjectByJsonStr(Class<T> rootClass, String json) {
		JSONObject jsonObj = JSONObject.fromObject(json);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(rootClass);
		return (T)JSONSerializer.toJava( jsonObj, jsonConfig );    
	}


	//IN_HSF-ProviderDetail_com.taobao.tc.service.TcTradeService:1.0.0_modifyWapDetailTradeFee
	public static String simplifyHsfInterfaceName(String name){
		if(name == null)
			return "";
		return name.substring(22, name.length());
	}

	/**
	 * ����ͼƬ����  picture description<seperate>/standard/example/example.png<seperate><br/>
	 * @return
	 */
	public static String getDefaultStepRecordsString() {
		return "ͼƬ����" + ConstantParameters.DEPEND_MIDDLE_SEPERATOR + "/standard/example/example.png" + 
				ConstantParameters.DEPEND_MIDDLE_SEPERATOR + "֧�ֶ�������" + ConstantParameters.DEPEND_MIDDLE_SEPERATOR + "/standard/example/example.png"
				+ ConstantParameters.DEPEND_MIDDLE_SEPERATOR  + ConstantParameters.DEPEND_CHECK_MESSAGE_SEPERATOR;
	}

	/**
	 * fileName��ȫ·��
	 * @param fileName
	 * @throws IOException 
	 */
	public static void writeContentToFile(String fileName, String content){
		try {
			File source = new File(fileName);
			if (!source.exists()) {
				File parentDir = new File(source.getParent());
				parentDir.mkdirs();
				source.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(source, true);
			fos.write(content.getBytes("utf-8"));
			fos.flush();
			fos.close();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static <T extends Comparable<? super T>> List<T> getSortedList(Collection<? extends T> c) {
		List<T> list;
		try {
			list = new ArrayList<T>(c);
			Collections.sort(list);
		} catch (Exception e) {
			logger.error("getSortedList�����쳣", e);
			list = new ArrayList<T>();
		}
		return list;
	}

	public static Date getBeginOfDay(String dateString) {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING);
			Date date = sdf.parse(dateString);
			calendar.setTime(date); 
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			return calendar.getTime();
		} catch (Exception e) {
		}
		return new Date();
	}

	public static Date getEndOfDay(String dateString) {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STRING);
			Date date = sdf.parse(dateString);
			calendar.setTime(date); 
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			return calendar.getTime();
		} catch (Exception e) {
		}
		return new Date();
	}

	public static void main(String[] args) {
		//System.out.println(MethodUtil.getOneDayPre());
		//System.out.println("sss");
	}
}
