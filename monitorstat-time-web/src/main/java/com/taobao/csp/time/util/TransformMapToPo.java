package com.taobao.csp.time.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;

public class TransformMapToPo {
	private static Logger logger = Logger.getLogger(TransformMapToPo.class);
	public static Object transformDataEntry(Map<String,DataEntry> map,Object obj){
		if(map==null&&obj==null)return obj;
		Class classType = obj.getClass();
		Field[] fields = classType.getDeclaredFields();
		boolean flag = false;
		for(Field field : fields){
			String fieldName = field.getName();
			if(fieldName == "time"){
				flag = true;
				continue;
			}
			String firstLetter = fieldName.substring(0,1).toUpperCase();
			String setMethodName = "set"+firstLetter+fieldName.substring(1);
			try {
				Method setMethod = classType.getMethod(setMethodName,new Class[]{field.getType()});
				DataEntry dataEntry = map.get(TransferMap.getSingle().get(fieldName));
				if(dataEntry == null)continue;
				Object value = dataEntry.getValue();
				setMethod.invoke(obj, value);
			} catch (Exception e) {
				logger.info(e);
			}
		}
		if(flag){
			for(Field field : fields){
				String fieldName = field.getName();
				if(fieldName.equals("time"))continue;
				try {
					DataEntry dataEntry = map.get(TransferMap.getSingle().get(fieldName));
					if(dataEntry == null)continue;
					Object value = dataEntry.getTime();
					String setMethodName = "setTime";
					Field timeField = classType.getDeclaredField("time");
					Method setMethod = classType.getMethod(setMethodName,new Class[]{timeField.getType()});
					setMethod.invoke(obj, value);
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					setMethodName = "setFtime";
					field = classType.getDeclaredField("ftime");
					setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});
					setMethod.invoke(obj,format.format(new Date((Long)value)));
					break;
				} catch (Exception e) {
					logger.info(e);
				}
			}
		}
		return obj;
	}
	/**
	 *×Ô¶¯Ó³Éä*
	 */
	public static Object transform(String key,Map<String,Object> map,Object obj){
		if(obj==null||map==null||key==null){
			return obj;
		}
		try {
			obj = transform(map,obj);
			Class classType = obj.getClass();
			String setMethodName = "setTime";
			Long value = Long.parseLong(key);
			Field field = classType.getDeclaredField("time");
			Method setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			setMethod.invoke(obj, value);
			setMethodName = "setFtime";
			field = classType.getDeclaredField("ftime");
			setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});
			setMethod.invoke(obj,format.format(new Date(value)));
		} catch (Exception e) {
			logger.info(e);
		}
		return obj;
	}
	
	public static Object transform(Map<String,Object> map,Object obj){
		if(map == null||obj==null)return obj;
		try {
			Class classType = obj.getClass();
			Field[] fields = classType.getDeclaredFields();
			for(Field field : fields){
				String fieldName = field.getName();
				String firstLetter = fieldName.substring(0,1).toUpperCase();
				String setMethodName = "set"+firstLetter+fieldName.substring(1);
				try {
					Method setMethod = classType.getMethod(setMethodName,new Class[]{field.getType()});
					setMethod.setAccessible(true);
					Object value = map.get(TransferMap.getSingle().get(fieldName));
					if(value == null)continue;
					setMethod.invoke(obj, value);
				} catch (Exception e) {
					logger.info(e);
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return obj;
	}
}
