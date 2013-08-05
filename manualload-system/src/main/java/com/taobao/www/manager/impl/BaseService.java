package com.taobao.www.manager.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;

import com.taobao.www.arkclient.csp.ManualCurUser;
import com.taobao.www.common.ReadOrWriteFile;
import com.taobao.www.entity.AppMachine;
import com.taobao.www.entity.PressureResult;

@SuppressWarnings("serial")
public class BaseService extends ReadOrWriteFile {

	private static final Logger logger = Logger.getLogger(BaseService.class);

	protected String manualCurUser = ManualCurUser.getLoginUserName(requests);

	private static final String onlineMacUrl = "http://a.alibaba-inc.com/page/api/free/product/dumptree.htm?notree=1&appname=";

	// 获取跳板机的路径
	protected String jumpMac = ResourceBundle.getBundle("common").getString("JUMP_MACHINE");

	// 获取文件存放的路径
	protected String path = ResourceBundle.getBundle("common").getString("EXECUTEFILE_PATH");

	private static HttpClient client = null;

	protected static Map<String, String> userMap = new ConcurrentHashMap<String, String>();

	public BaseService() {

	}

	// 设置ClientConnectionManager
	static {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

		ClientConnectionManager cm = new ThreadSafeClientConnManager(schemeRegistry);
		client = new DefaultHttpClient(cm);
		client.getParams().setParameter("http.connection.timeout", Integer.valueOf(3000));
	}

	public String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 功能：登录跳板机。
	 * 
	 * @param username
	 * 
	 * @param password
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-09-10
	 * 
	 */
	protected Connection loginJumpMachine(String username, String password) {
		boolean isAuthenticated = false;
		try {
			Connection conn = new Connection(jumpMac);
			conn.connect();
			isAuthenticated = conn.authenticateWithPassword(username, password);
			if (isAuthenticated) {
				return conn;
			}
		} catch (IOException e) {
			logger.info(" login manchin is failed ：" + jumpMac);
		}
		return null;
	}

	/**
	 * 功能：根据应用名来获取该应用下面的所有机器信息。
	 * 
	 * @param appId
	 * 
	 * @param appName
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-09-10
	 * 
	 */
	public static List<AppMachine> getAllMachineDataByAppName(int appId, String appName) {
		try {
			appName = URLEncoder.encode(appName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("encode exception", e);
		}
		logger.info("get machine name start");
		String urlString = onlineMacUrl + appName + "&_username=droid/csp";
		logger.info(" url address is :" + urlString);
		String resultInfo = getUrlResultNew(urlString);
		if (resultInfo.indexOf("product_not_find") > 0) {
			return null;
		}
		List<AppMachine> frontList = new ArrayList<AppMachine>();
		JSONArray info = JSONArray.fromObject(resultInfo);
		if (info.size() == 0) {
			return null;
		}
		JSONObject object = info.getJSONObject(0);
		for (Object obj : object.values())
			if (obj instanceof JSONArray) {
				JSONArray json = (JSONArray) obj;
				for (int ch = 0; ch < json.size(); ch++) {
					Object tmp = json.get(ch);
					if (tmp instanceof JSONObject) {
						JSONObject child = (JSONObject) tmp;
						JSONArray array = child.getJSONArray("child");
						AppMachine mac = null;
						for (int i = 0; i < array.size(); i++) {
							JSONObject hostObj = array.getJSONObject(i);
							String dns_ip = hostObj.getString("dns_ip");
							String nodename = hostObj.optString("nodename");
							String state = hostObj.optString("state");
							if (state.equals("working_online")) {
								mac = new AppMachine();
								mac.setAppId(appId);
								mac.setAppName(appName);
								mac.setCreateTime(new Date());
								mac.setMacName(nodename);
								mac.setMacIp(dns_ip);
								mac.setMacState(state);
								frontList.add(mac);
							}
						}
					}
				}
			}
		return frontList;
	}

	/**
	 * 功能：返回指定url地址的内容。
	 * 
	 * @param requestUrl
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-09-10
	 * 
	 */
	protected static String getUrlResultNew(String requestUrl) {
		try {
			HttpGet httpGet = new HttpGet(requestUrl);
			HttpResponse response = client.execute(httpGet);
			String result = org.apache.http.util.EntityUtils.toString(response.getEntity());
			return result;
		} catch (Exception e) {
			logger.error("http 请求url 出错:" + requestUrl, e);
		}
		return null;
	}

	/**
	 * 功能：根据URL地址来获取对应的json字符串信息。
	 * 
	 * @param urlStr
	 * 
	 * @return resultStr
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-08-09
	 */
	public static String getUrlContent(String urlStr) {
		URL url;
		String resultStr = null;
		HttpURLConnection connection = null;
		InputStream is = null;
		InputStreamReader isRead = null;
		BufferedReader br = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			// 获取响应代码
			int code = connection.getResponseCode();
			if (200 == code) {
				is = connection.getInputStream();
				isRead = new InputStreamReader(is);
				br = new BufferedReader(isRead);
				StringBuffer sb = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				resultStr = sb.toString();
				logger.info(" get json information is suceese !  ");
			}
		} catch (Exception e) {
			logger.info(" get json information is faile ! url address :" + urlStr, e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isRead != null) {
					isRead.close();
				}
				if (is != null) {
					is.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return resultStr;
	}

	/**
	 * 功能：将压测结果封装成压测结果对象。
	 * 
	 * @param urlStr
	 *            url地址
	 * 
	 * @param ip
	 * 
	 * @param appId
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-08-09
	 */
	@SuppressWarnings("null")
	public static PressureResult getLogContentByUrl(String urlStr, String ip, int appId) {
		URL url;
		HttpURLConnection connection = null;
		InputStream is = null;
		InputStreamReader isRead = null;
		BufferedReader br = null;
		PressureResult pr = new PressureResult();
		String[] str1 = new String[] {};
		String[] str2 = new String[] {};
		String[] str3 = new String[] {};
		String[] str4 = new String[] {};
		String[] str5 = new String[] {};
		StringBuffer http_state = new StringBuffer();
		StringBuffer resultInfo = new StringBuffer();
		int bytesConnection = 0;
		int badCount = 0;
		pr.setCreateTime(new Date());
		boolean result = false;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			// 获取响应代码
			int code = connection.getResponseCode();
			if (200 == code) {
				is = connection.getInputStream();
				isRead = new InputStreamReader(is);
				br = new BufferedReader(isRead);
				String tempString = null;
				while ((tempString = br.readLine()) != null) {
					resultInfo.append(tempString);
					if (tempString.contains("max parallel")) {
						result = true;
						str1 = tempString.split(",");
						for (int i = 0; i < str1.length && str1.length > 0; i++) {
							String str = str1[i];
							if (str.contains("fetches")) {
								pr.setRequestTotle((int) Math.round(Double.parseDouble(str.replace("fetches", "")
										.trim())));
							} else if (str.contains("max parallel")) {
								pr.setProcessCount((int) Math.round(Double.parseDouble(str.replace("max parallel", "")
										.trim())));
							} else if (str.contains("bytes")) {
								pr.setBytesTotle((int) Math.round(Double.parseDouble(str.replace("bytes", "").trim())));
							}
						}

					} else if (tempString.contains("mean bytes/connection")) {
						result = true;
						// 连接平均传输的数据量
						bytesConnection = (int) Math.round(Double.parseDouble(tempString.replace(
								"mean bytes/connection", "").trim()));
						pr.setBytesConnection(bytesConnection);
					} else if (tempString.contains("fetches/sec")) {
						result = true;
						str2 = tempString.split(",");

						for (int i = 0; i < str2.length && str2.length > 0; i++) {
							String str = str2[i];
							if (str.contains("fetches/sec")) {
								pr.setFetchesSec((int) Math.round(Double.parseDouble(str.replace("fetches/sec", "")
										.trim())));
							} else if (str.contains("bytes/sec")) {
								pr.setBytesSec((int) Math.round(Double.parseDouble(str.replace("bytes/sec", "").trim())));
							}
						}

					} else if (tempString.contains("msecs/connect:")) {
						result = true;
						str3 = tempString.replace("msecs/connect:", "").trim().split(",");

						for (int i = 0; i < str3.length && str3.length > 0; i++) {
							String str = str3[i];
							if (str.contains("mean")) {
								pr.setConnectAvg((int) Math.round(Double.parseDouble(str.replace("mean", "").trim())));
							} else if (str.contains("max")) {
								pr.setConnectMax((int) Math.round(Double.parseDouble(str.replace("max", "").trim())));
							} else if (str.contains("min")) {
								pr.setConnectMin((int) Math.round(Double.parseDouble(str.replace("min", "").trim())));
							}
						}

					} else if (tempString.contains("msecs/first-response:")) {
						result = true;
						str4 = tempString.replace("msecs/first-response:", "").trim().split(",");
						for (int i = 0; i < str4.length && str4.length > 0; i++) {
							String str = str4[i];
							if (str.contains("mean")) {
								pr.setResponseAvg((int) Math.round(Double.parseDouble(str.replace("mean", "").trim())));
							} else if (str.contains("max")) {
								pr.setResponseMax((int) Math.round(Double.parseDouble(str.replace("max", "").trim())));
							} else if (str.contains("min")) {
								pr.setResponseMin((int) Math.round(Double.parseDouble(str.replace("min", "").trim())));
							}
						}
					} else if (tempString.contains("bad byte counts")) {
						result = true;
						String badcount = tempString.replace("bad byte counts", "").trim();
						if (badcount != null || !badcount.equals("")) {
							badCount = (int) Math.round(Double.parseDouble(badcount));
						} else {
							pr.setBadCount(badCount);
						}
					} else if (tempString.contains("  code ")) {
						result = true;
						str5 = tempString.replace("  code ", "").split(" -- ");
						http_state.append(str5[0].toString() + ":" + str5[1].toString() + ";");
					}
				}
				logger.info("appId:==" + appId + ", hostIp:==" + ip + ", resultInfo:==" + resultInfo.toString());
			}
		} catch (Exception e) {
			logger.info(" get pressure result information is faile ! url address :" + urlStr, e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isRead != null) {
					isRead.close();
				}
				if (is != null) {
					is.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result) {
			pr.setHttpState_stateCount(http_state.toString());
			return pr;
		} else {
			return null;
		}
	}

	/**
	 * 功能：获取load信息。
	 * 
	 * @param urlString
	 *            url地址
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-08-09
	 */
	protected double getLoadAvgInformation(String urlString) {
		double result = 0.0;
		String data = getUrlContent(urlString);
		if (data == null || "".equals(data)) {
			return result;
		} else {
			Pattern pattern = Pattern.compile("load average:\\s?([\\d\\.]+),\\s?([\\d\\.]+),\\s?([\\d\\.]+)");
			Matcher match = pattern.matcher(data);
			if (match.find()) {
				result = Double.parseDouble(match.group(1));
			}
			return result;
		}
	}

}
