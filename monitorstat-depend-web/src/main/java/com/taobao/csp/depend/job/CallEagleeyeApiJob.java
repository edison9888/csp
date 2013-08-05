package com.taobao.csp.depend.job;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.csp.common.ZKClient;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.csp.depend.util.StartUpParamWraper;
import com.taobao.monitor.common.ao.center.EagleeyeDataAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestPart;
import com.taobao.monitor.common.po.EagleeyeApiChildKeyPo;
import com.taobao.monitor.common.po.EagleeyeUrlListPo;
import com.taobao.monitor.common.util.Constants;


public class CallEagleeyeApiJob {
	
	private static final Logger logger = Logger.getLogger("cronTaskLog");
	
	private static List<String> appList = new ArrayList<String>();
	private static final String[] defaultAppNameArray = new String[] { "detail", "hesper", "login", "cart",
			"shopsystem", "tradeplatform", "tf_buy", "tf_tm", "uicfinal",
			"itemcenter", "ump", "shopcenter" };
	
	Map <String, List<String>> keyNameMap = new HashMap<String, List<String>>();
	private static Set<String> typeSet = new HashSet<String>();
	private static Set<String> filter = new HashSet<String>();
	ExecutorService executor = Executors.newFixedThreadPool(5);
	static {
		typeSet.add(Constants.API_CHILD_APP);
		typeSet.add(Constants.API_FATHER_APP);
		typeSet.add(Constants.API_CHILD_KEY);
		typeSet.add(Constants.API_FATHER_KEY);
		filter.add("/check_on_start.htm");
		filter.add("from://");
	}
	private static final String ZK_DEPEND_APP_ROOT = "/csp/depend/eagleeyeapp";
	
	public static List<String> getApplist() {
		List<String> list = ZKClient.get().list(ZK_DEPEND_APP_ROOT);
		if(list == null || list.size() == 0) {
			appList =  Arrays.asList(defaultAppNameArray);	
		} else {
			appList = list;			
		}
		logger.info("监控的APP发生变化->" + appList.toString());
		return new ArrayList<String>(appList);
	}

	public synchronized void registerRoot() {
		ZKClient.get().mkdirPersistent(ZK_DEPEND_APP_ROOT);
		for(String appName:defaultAppNameArray) {
			ZKClient.get().mkdirPersistent(ZK_DEPEND_APP_ROOT + "/" + appName);
		}
	}
	
	public void callEagleeyeApi() {
		registerRoot();	//注册节点
		
		logger.info("开始下载Eagleeye的API数据");
		//		executor.execute(new DumpAppAndKeyData(Constants.API_CHILD_KEY));
		//		executor.execute(new DumpAppAndKeyData(Constants.API_CHILD_APP));
		//		executor.execute(new DumpAppAndKeyData(Constants.API_FATHER_APP));
		executor.execute(new DumpAppAndKeyData(Constants.API_CHILD_KEY));
		executor.execute(new DumpAppAndKeyData(Constants.API_FATHER_KEY));
		executor.execute(new DumpKeyListData(Constants.API_URL_KEY_LIST));
		executor.execute(new DumpKeyListData(Constants.API_HSF_KEY_LIST));
	}

	class DumpAppAndKeyData implements Runnable {
		private String apiTpye;
		public DumpAppAndKeyData(String apiType) {
			this.apiTpye = apiType;
		}
		@Override
		public void run() {
			if(apiTpye == null || !typeSet.contains(apiTpye)) {
				logger.error("apiTpye不正确，apiTpye=" + apiTpye);
				return;
			}
			logger.info("apiTpye=" + apiTpye + ";开始");
			for(String appName : getApplist()) {
				if(apiTpye.equals(Constants.API_CHILD_APP) || apiTpye.equals(Constants.API_FATHER_APP)) {
					//暂无
				} else if(apiTpye.equals(Constants.API_CHILD_KEY) || apiTpye.equals(Constants.API_FATHER_KEY)) {
					AppInfoPo info = AppInfoCache.getAppInfoByAppName(appName);
					String url = "";
					if(info == null)
						continue;
					
					if(info.getAppType().equalsIgnoreCase("pv") && apiTpye.equals(Constants.API_CHILD_KEY)) {
						url = StartUpParamWraper.getEagleeyeUrlByType(Constants.API_URL_KEY_LIST);	
					} else if(info.getAppType().equalsIgnoreCase("center")){
						url = StartUpParamWraper.getEagleeyeUrlByType(Constants.API_HSF_KEY_LIST);
					} else {
						continue;
					}

					String keyListUrl = String.format("%s?appName=%s", url, appName);

					String json = callApi(keyListUrl, false);
					if(StringUtil.isBlank(json))
						continue;
					
					EagleeyeUrlListPo keyListPo = JSONObject.parseObject(json,EagleeyeUrlListPo.class);
					List<String> keyList = keyListPo.getKeylist();
					if(keyList.size() > 0) {
						for(String sourceKey : keyList) {
							if(isFilter(sourceKey)) {
								logger.info("过滤掉网址->" + sourceKey);
								continue;
							}
							try {
								CspEagleeyeApiRequestPart part = new CspEagleeyeApiRequestPart();
								part.setApiType(apiTpye);
								part.setAppName(appName);
								part.setSourcekey(sourceKey);
								
								// sourceKey change here. @ -> _
								setValueKeyPart(part);	
								
								EagleeyeDataAo.get().addCspEagleeyeApiRequestPart(part);								
							} catch (Exception e) {
								logger.error("sourceKey=" + sourceKey, e);
							}
						}
					}
				}
			}
			logger.info("apiTpye=" + apiTpye + ";结束");
		}
	}

	private void setValueKeyPart(CspEagleeyeApiRequestPart part) throws ParseException {
		String url = StartUpParamWraper.getEagleeyeUrlByType(part.getApiType());
		String requestUrl = String.format("%s?appName=%s&keyName=%s", url, part.getAppName(), changeParamCode(part.getSourcekey()));
		//String url = "http://eagleeye.taobao.net:9999/api/QueryDownstreams";
		//String requestUrl = String.format("%s?appName=%s&keyName=%s", url, part.getAppName(), part.getSourcekey());

		String jsonTmp = callApi(requestUrl, true);
		if(StringUtil.isBlank(jsonTmp))
			return;
		
		EagleeyeApiChildKeyPo partApiPo = JSONObject
				.parseObject(jsonTmp, EagleeyeApiChildKeyPo.class);
		if(partApiPo.getTopo() == null) {
			logger.error("partApiPo.getTopo()=" + partApiPo.getTopo());
			return;
		} 
		String jsonTopo = JSONObject.toJSONString(partApiPo.getTopo());
		part.setVersion("1.0");
		part.setCollectTime(MethodUtil.getDateByFormat(partApiPo.getTime(), ConstantParameters.FORMAT_STRING_FULL));
		part.setResponseContent(jsonTopo);
		part.setSourcekey(partApiPo.getKeyName());
	}

	class DumpKeyListData implements Runnable {
		private String apiTpye;
		public DumpKeyListData(String apiType) {
			this.apiTpye = apiType;
		}
		@Override
		public void run() {
			if(!Constants.API_URL_KEY_LIST.equals(apiTpye) && !Constants.API_HSF_KEY_LIST.equals(apiTpye)) {
				logger.error("apiTpye!=url_key_list,hsf_key_list, apiTpye=" + apiTpye);
				return;
			}
			logger.info("apiTpye=" + apiTpye + ";开始");
			for(String appName : getApplist()) {
				String requestUrl = StartUpParamWraper.getEagleeyeUrlByType(apiTpye) + "?appName=" + appName;
				//String requestUrl = "http://eagleeye.taobao.net:9999/api/QueryTraceNames" + "?appName=" + appName;;
				String json = callApi(requestUrl, true);
				if(StringUtil.isBlank(json))
					return;
				EagleeyeUrlListPo keyListPo = JSONObject.parseObject(json,EagleeyeUrlListPo.class);
				try {
					CspEagleeyeApiRequestPart part = new CspEagleeyeApiRequestPart();
					part.setApiType(apiTpye);
					part.setAppName(appName);
					part.setSourcekey(appName);
					part.setVersion("1.0");
					part.setCollectTime(MethodUtil.getDateByFormat(
							keyListPo.getTime(),
							ConstantParameters.FORMAT_STRING_FULL));
					part.setResponseContent(json);
					EagleeyeDataAo.get().addCspEagleeyeApiRequestPart(part);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
	}

	public String callApi(String url, Boolean isChangeKey) {
		if(StringUtil.isBlank(url)) {
			logger.error("传入URL参数为空");
			return "";
		}
//		try {
//			url = URLEncoder.encode(url, "UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			logger.error("url=" + url, e1);
//			return "";
//		}
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		String json = "";
		try {
			int code = client.executeMethod(method);
			logger.debug("url=" + url + ";返回码->" + code);
			json = method.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error("定时程序调用API异常,url=" + url, e);
			json = "";
		} finally {
			method.releaseConnection();
		}
		if(isChangeKey != null && isChangeKey == true)
			return json.replaceAll("@", "_");
		return json;
	}
	
	public String changeParamCode(String param) {
		try {
			return URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("param=" + param, e);
			return "";
		}
	}

	public boolean isFilter(String url) {
		for(String filterUrl : filter) {
			if(url.indexOf(filterUrl) >=0 )
				return true;
		}
		return false;
	} 

	public static void main(String[] args) {
		//		String url = "http://time.csp.taobao.net:9999/time/api.do?method=notifyDataByDay&appName=trade_notify&selectDate=2012-07-24&key1=deliveried_F&key2=&key3=";
		//		System.out.println(new CallEagleeyeApiJob().callApi(url));
		//new CallEagleeyeApiJob().callEagleeyeApi();
		
		try {
			System.out.println( URLEncoder.encode("com.taobao.item.service.ItemQueryService@queryItemForDetail~lA", "UTF-8"));
			System.out.println( URLEncoder.encode("com.taobao.item.service.ItemQueryService@queryItemForDetail~lA", "gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
