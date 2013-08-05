package com.taobao.monitor.alarm.source.change;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class ChangeFreeAo {
	private static final Logger logger = Logger.getLogger(ChangeFreeAo.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static ChangeFreeAo ao = new ChangeFreeAo();
	public static ChangeFreeAo get(){
		return ao;
	}
	
	
	/**
	 * 初始化changeFree数据
	 * @param startTime
	 * @return
	 */
	public  Map<String, List<ChangeFreePo>>  getChangeFreeDataMapByStartTime(Date startTime){
		Map<String, List<ChangeFreePo>> m_changeFreeMap  = new HashMap<String, List<ChangeFreePo>>();
		logger.warn("getChangeFreeDataMapByStartTime,startTime=" + startTime);
		for(String systemName:ChangeConstants.systemFromChangeFreeList){
			m_changeFreeMap.put(systemName, getChangeFreeInfo(startTime,systemName));
		}
		return m_changeFreeMap;
		
	}
	
	public List<ChangeFreePo>  getChangeFreeInfo(Date startTime,String systemName){
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(ChangeConstants.changeFreeUrl);
		postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		postMethod.addRequestHeader("Accept", "text/plain");
		//使用系统提供的默认的恢复策略
		postMethod.setParameter("starttime", sdf.format(startTime));
		postMethod.setParameter("product_line", systemName);
		try {
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String response= postMethod.getResponseBodyAsString();//读取内容
				logger.warn("getChangeFreeInfo,startTime=" + startTime + ",systemName=" + systemName + ",response=" + response);
				JSONArray jsonArray = JSONArray.fromObject(response);
				List<ChangeFreePo> list  = new ArrayList<ChangeFreePo> ();
				for (int i = 0; i < jsonArray.size(); i++) {
					ChangeFreePo changeFreePo = getChangeFreePoFromJson(jsonArray.getJSONObject(i));
					changeFreePo.setSystemName(systemName);
					list.add(changeFreePo);
				}
				return list;
			}else{
				logger.warn("http status is not OK,url=" + ChangeConstants.changeFreeUrl);
			}
		} catch (HttpException e) {
			logger.warn("url error, check url="+ChangeConstants.changeFreeUrl, e);
		} catch (IOException e) {
			logger.warn("acess changfree url="+ChangeConstants.changeFreeUrl+" error:", e);
		} catch (Exception e) {
			logger.warn("parse json error", e);
		} finally {
			postMethod.releaseConnection();
		}
		return null;
		
	}
	
	private ChangeFreePo getChangeFreePoFromJson(JSONObject json) throws JSONException{
		ChangeFreePo changeFreePo = new ChangeFreePo();
		changeFreePo.setChangeType(json.getString("change_type"));
		changeFreePo.setEndTime(json.getString("change_end_time"));
		changeFreePo.setId(json.getString("id") );
		changeFreePo.setMobilePhone(json.getString("mobile_phone"));
		changeFreePo.setPhone(json.getString("phone"));
		changeFreePo.setStartTime(json.getString("change_start_time"));
		changeFreePo.setTitle(json.getString("title"));
		changeFreePo.setUserName(json.getString("username") );
		return changeFreePo;
	}
	
}
