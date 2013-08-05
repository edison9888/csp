package com.taobao.csp.day.apache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.Log;

public class ApacheSpecialLogAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(ApacheSpecialLogAnalyser.class);
	
	private static List<String> URL_FILTER =new ArrayList<String>();
	
	private Map<ApacheSpecialLogKey, ApacheSepcial> cache = new HashMap<ApacheSpecialLogKey, ApacheSepcial>();
	
	static {
		// cart
		URL_FILTER.add("http://cart.taobao.com/top_cart_quantity.htm");
		URL_FILTER.add("http://cart.taobao.com/my_cart.htm");
		URL_FILTER.add("http://cart.taobao.com/update_cart_quantity.htm");
		URL_FILTER.add("http://cart.taobao.com/check_cart_login.htm");
		URL_FILTER.add("http://cart.taobao.com/trail_mini_cart.htm");
		URL_FILTER.add("http://cart.taobao.com/add_cart_item.htm");
		
		// detail
		URL_FILTER.add("http://item.taobao.com/item.htm");
		URL_FILTER.add("http://item.taobao.com/auction/noitem.htm");
		URL_FILTER.add("http://sd.tbcdn.cn/dcAsyn.htm");
		
		// tf_buy
		URL_FILTER.add("http://buy.taobao.com/auction/buy.htm");
		URL_FILTER.add("http://buy.taobao.com/auction/order/confirm_order.htm");
		URL_FILTER.add("http://buy.taobao.com/auction/order/unity_order_confirm.htm");
		URL_FILTER.add("http://buy.taobao.com/auction/buy_now.jhtml");
		URL_FILTER.add("http://buy.taobao.com/auction/order/confirm_order_error.htm");
		
		// tf_tm
		URL_FILTER.add("http://trade.taobao.com/trade/itemlist/list_bought_items.htm");
		URL_FILTER.add("http://trade.taobao.com/trade/pay.htm");
		URL_FILTER.add("http://trade.taobao.com/trade/itemlist/list_sold_items.htm");
		URL_FILTER.add("http://trade.taobao.com/trade/pay_success.htm");
		URL_FILTER.add("http://trade.taobao.com/trade/detail/tradeSnap.htm");
		URL_FILTER.add("http://trade.taobao.com/trade/cancel_order_buyer.htm");
		URL_FILTER.add("http://trade.taobao.com/trade/detail/trade_item_detail.htm");
		URL_FILTER.add("http://trade.taobao.com/trade/itemlist/listBoughtItems.htm");
		URL_FILTER.add("http://trade.taobao.com/trade/json/user_info.htm");
	}
	
	public ApacheSpecialLogAnalyser(String appName, HostInfo hostInfo, char lineSplit) {
		super(appName, hostInfo, lineSplit);
	}
	
	@Override
	public List<Log> analyse(String segment) {
		logger.debug("analyse segment");
		segment = this.getResidue() + segment;
		int lastFlag = segment.lastIndexOf(String.valueOf(this.getLineSplit()));
		String newResidue = new String(segment.substring(lastFlag + 1, segment.length()).toCharArray());
		this.setResidue(newResidue);
		segment = segment.substring(0, lastFlag + 1);

		cache.clear();
		String [] lines = segment.split(String.valueOf(this.getLineSplit()));
		for (String line : lines) {
			analyseOneLine(line);
		}
		
		List<Log> list = generateLogPos();
		cache.clear();
		
		return list;
	}
	
	public void analyseOneLine(String line) {
		logger.debug("analyse line");

		if (!passFilterUrl(line)) {
			return;
		}
		
		String useTimeStr = "";
		String requestUrl = "";
		String httpCode = " ";
		try {
			String collectTime = parseLogLineCollectTime(line);
			if (!passFilterCollectTime(collectTime)) {
				return;
			}
			
			String[] tokens = line.split("\"");
			useTimeStr = tokens[0].split(" ")[1];
			requestUrl = cleanUrl(tokens[1].split(" ")[1]);
			if (requestUrl == null || requestUrl.length() < 20 || (!URL_FILTER.contains(requestUrl.trim()))) {
				return;
			}
			
			httpCode = tokens[2].trim().split(" ")[0];
			long useTime = 0L;
			if (!useTimeStr.equals("-")) {
				if (useTimeStr.indexOf(".") > -1) {
					useTime = (long)(Float.parseFloat(useTimeStr) * 1000 * 1000);
				} else {
					useTime = Long.parseLong(useTimeStr);
				}
			}

			ApacheSpecialLogKey key = new ApacheSpecialLogKey();
			key.setAppName(this.getAppName());
			key.setRequstUrl(requestUrl);
			key.setHttpCode(httpCode);
			key.setCollectTime(collectTime);
			
			ApacheSepcial value;
			if (cache.containsKey(key)) {
				value = cache.get(key);
			} else {
				value = new ApacheSepcial();
				cache.put(key, value);
			}
			
			value.setRequstNum(value.getRequstNum() + 1);
			value.setRt(value.getRt() + useTime);
		} catch (Exception e) {
			logger.error("analyse error", e);
		}
	}
	
	@Override
	public boolean isCurrentLog(String segment) {
		boolean isCurrentLog = true;
		
		return isCurrentLog;
	}
	
	private List<Log> generateLogPos() {
		List<Log> list = new ArrayList<Log>();
		for (Map.Entry<ApacheSpecialLogKey, ApacheSepcial> entry : cache.entrySet()) {
			ApacheSpecialLogKey key = entry.getKey();
			ApacheSepcial value = entry.getValue();
			
			ApacheSpecialLog po = new ApacheSpecialLog();
			po.setAppName(key.getAppName());
			po.setRequestUrl(key.getRequstUrl());
			po.setHttpCode(key.getHttpCode());
			po.setCollectTime(key.getCollectTime());
			po.setRequestNum(value.getRequstNum());
			po.setRt(value.getRt());
			
			list.add(po);
		}
		
		return list;
	}
	
	private String parseLogLineCollectTime(String line) {
		try {
			Pattern timePattern = Pattern.compile("(\\[\\d{2}\\/\\w{3}\\/\\d{4}:\\d{2}:\\d{2}:\\d{2})");
			SimpleDateFormat sdfInfo = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss",Locale.ENGLISH);
			SimpleDateFormat sdfSecend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Matcher matcher;

			matcher = timePattern.matcher(line);
			if (matcher.find()) {
				return sdfSecend.format(sdfInfo.parse(matcher.group(1)));
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.error("parseLogLineCollectTime error:" +  line, e);
			return null;
		}
	}
	
	private String cleanUrl(String str) {
		if (str.indexOf("?") > -1) {
			return str.substring(0, str.indexOf("?"));
		}
		return str;
	}
	
	private boolean passFilterCollectTime(String collectTime) {
		boolean pass = false;
		if (collectTime != null && collectTime.compareTo("2012-11-10 23:30:00") >=0 
				&& collectTime.compareTo("2012-11-10 23:59:59") <= 0) {
			pass = true;
		}
		
		return pass;
	}
	
	private boolean passFilterUrl(String line) {
//		if (line.indexOf("status.taobao") > -1 || line.indexOf("nginx.taobao") > -1) {
//			return;
//		}
		
		for (String url : URL_FILTER) {
			if (line.indexOf(url) > -1) {
				return true;
			}
		}
		
		return false;
	}
}
