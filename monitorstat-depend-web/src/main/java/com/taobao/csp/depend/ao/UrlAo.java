package com.taobao.csp.depend.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.dao.UrlDao;
import com.taobao.csp.depend.po.url.RequestUrlSummary;
import com.taobao.csp.depend.po.url.UrlOriginSummary;
import com.taobao.csp.depend.po.url.UrlUv;
import com.taobao.monitor.common.util.page.Pagination;

public class UrlAo {
	private UrlAo(){}
	private static final Logger logger =  Logger.getLogger(UrlAo.class);
	private static final UrlAo urlAo = new UrlAo();

	public static UrlAo get() {
		return urlAo;
	}

	private UrlDao urlDao = new UrlDao(); //注入不起作用  

	public Pagination<UrlUv> queryUrlUv(String collect_time, String url_type, String url, 
			int pageNo, int pageSize) {
		List<UrlUv> list = urlDao.queryUrlUv(collect_time, url_type, url, pageNo, pageSize);
		int iTotal = Integer.MAX_VALUE; 
		final Pagination<UrlUv> page = new Pagination<UrlUv>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setList(list);
		page.setTotalCount(iTotal);
		return page;
	}

	//  public long getTotalForUrlUv(String collect_time, String url_type, String url) {
	//    return urlDao.getTotalCountBySql(collect_time, url_type, url);
	//  }

	private Map<String, Long> getCountByType(String startTime, String endTime, String type, String urlType) {
		if(type == null || "ipv".equals(type)) {
			type = "ipv";
		} else {
			type = "uv";
		}

		if(urlType == null || (!urlType.equals("url") && !urlType.equals("domain")))
			urlType = "url";

		return urlDao.queryUrl(startTime, endTime, type, urlType);
	}


	private Map<String, Long> getCountByTypeAndUrl(String startTime, String endTime, String type, String urlType, String url) {
		if(type == null || "ipv".equals(type)) {
			type = "ipv";
		} else {
			type = "uv";
		}

		if(urlType == null || URL_TYPE_URL.equals(urlType)) {
			urlType = URL_TYPE_URL;
		} else {
			urlType = URL_TYPE_DOMAIN;
		}	  

		return urlDao.queryByUrl(startTime, endTime, type, urlType, url);
	}  

	public Map<String, Long> getPV(String startTime, String endTime, String urlType) {
		return getCountByType(startTime, endTime, "ipv", urlType);
	}
	public Map<String, Long> getPVByUrl(String startTime, String endTime, String urlType, String url) {
		return getCountByTypeAndUrl(startTime, endTime, "ipv", urlType, url);
	}  

	public Map<String, Long> getUV(String startTime, String endTime, String urlType) {
		return getCountByType(startTime, endTime, "uv", urlType);
	}

	public Map<String, Long> getUVByUrl(String startTime, String endTime, String urlType, String url) {
		return getCountByTypeAndUrl(startTime, endTime, "uv", urlType, url);
	}  

	public static final String URL_TYPE_DOMAIN = "domain";
	public static final String URL_TYPE_URL = "url";

	public List<UrlOriginSummary> findOriginList(String appName, String collectDate) {
		return urlDao.findOriginList(appName, collectDate);  
	}

	public List<RequestUrlSummary> findRequestList(String appName, String collectDate) {
		return urlDao.findRequestList(appName, collectDate);  
	}  

	/**
	 * 按照应用名，时间，返回实际请求到的URL的调用次数及方法名。
	 * @return
	 */
	public Map<String,Long> getRequestUrlCall(String provideAppName, String collectDay){
		Map<String,Long> map = new HashMap<String,Long>();
		List<RequestUrlSummary> list = urlDao.findRequestList(provideAppName, collectDay);
		for(RequestUrlSummary po: list) {
			try {
				map.put(po.getRequestUrl(), po.getRequestNum());
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		return map;
	}
}
