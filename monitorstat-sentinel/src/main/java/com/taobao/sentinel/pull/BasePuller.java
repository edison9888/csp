package com.taobao.sentinel.pull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class BasePuller {
	
	public static Logger logger = Logger.getLogger(BasePuller.class);
	
	private String ip;
	
	private String appName;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public BasePuller() {}
	
	public BasePuller(String appName, String ip) {
		this.appName = appName;
		this.ip = ip;
	}
	
	protected String getClientInfo(String urlAddr) {
		StringBuffer sb = new StringBuffer(StringUtils.EMPTY);

		InputStream in = null;
		InputStreamReader iReader = null;
		BufferedReader bReader = null;
		try {
			URL url = new URL(urlAddr);
			in = url.openStream();
			iReader = new InputStreamReader(in, "utf-8");
			bReader = new BufferedReader(iReader);

			String line = null;
			while ((line = bReader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			logger.error("getClientInfo error", e);
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
					logger.error("getClientInfo error", e);
				}
			}
		}

		return sb.toString();
	}

	protected List<String> transferJsonToList(String jsonString) {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isEmpty(jsonString.trim())) {
			return list;
		}

		JSONArray array = JSONArray.fromObject(jsonString);
		for (int i = 0; i < array.size(); i++) {
			list.add(array.getString(i));
		}

		return list;
	}

	protected Map<String, String> transferJsonToMap(String jsonString) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isEmpty(jsonString.trim())) {
			return map;
		}

		JSONObject object = JSONObject.fromObject(jsonString);
		Set keysSet = object.keySet();
		for (Object key : keysSet) {
			map.put(key.toString(), object.get(key).toString());
		}

		return map;
	}
}
