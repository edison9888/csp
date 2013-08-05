/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version 2010-5-18:下午08:09:21
 * 
 */
public class DetailStatisticHelper {

    private static Pattern pattern = Pattern.compile("shop[0-9]*");
	private static Pattern pattern4List =  Pattern.compile("list[0-9]*\\.t");
    /**
     * 页面大小定义 30k ,40k,....
     */
    public final static long PAGE_SIZE_30K = 30000;
    public final static long PAGE_SIZE_40K = 40000;
    public final static long PAGE_SIZE_50K = 50000;
    public final static long PAGE_SIZE_60K = 60000;
    public final static long PAGE_SIZE_70K = 70000;
    public final static long PAGE_SIZE_80K = 80000;
    public final static long PAGE_SIZE_90K = 90000;
    public final static long PAGE_SIZE_100K = 100000;

    //1. page size key
    public final static String PAGE_SIZE_0_30K = "detail_page_sieze_0_30";
    public final static String PAGE_SIZE_30_40K = "detail_page_sieze_30_40";
    public final static String PAGE_SIZE_40_50K = "detail_page_sieze_40_50";
    public final static String PAGE_SIZE_50_60K = "detail_page_sieze_50_60";
    public final static String PAGE_SIZE_60_70K = "detail_page_sieze_60_70";
    public final static String PAGE_SIZE_70_80K = "detail_page_sieze_70_80";
    public final static String PAGE_SIZE_80_90K = "detail_page_sieze_80_90";
    public final static String PAGE_SIZE_90_100K = "detail_page_sieze_90_100";
    public final static String PAGE_SIZE_100_upper = "detail_page_sieze_100_upper";

    /**
     * 页面响应时间 50ms,60ms ...
     */
    public final static long RESPONSE_TIME_50 = 50;
    public final static long RESPONSE_TIME_60 = 60;
    public final static long RESPONSE_TIME_70 = 70;
    public final static long RESPONSE_TIME_80 = 80;
    public final static long RESPONSE_TIME_90 = 90;
    public final static long RESPONSE_TIME_100 = 100;
    public final static long RESPONSE_TIME_110 = 110;

    //2. resp key
    public final static String RESPONSE_TIME_0_50 = "detail_resp_time_0_50";
    public final static String RESPONSE_TIME_50_60 = "detail_resp_time_50_60";
    public final static String RESPONSE_TIME_60_70 = "detail_resp_time_60_70";
    public final static String RESPONSE_TIME_70_80 = "detail_resp_time_70_80";
    public final static String RESPONSE_TIME_80_90 = "detail_resp_time_80_90";
    public final static String RESPONSE_TIME_90_100 = "detail_resp_time_90_100";
    public final static String RESPONSE_TIME_100_110 = "detail_resp_time_100_110";
    public final static String RESPONSE_TIME_110_upper = "detail_resp_time_110_upper";

    
    //3.  refer key
    public final static String REFER_SEARCH = "detail_refer_search.taobao.com";

    public final static String REFER_SEARCH_8 = "detail_refer_search8.taobao.com";
    public final static String REFER_LIST = "detail_refer_list.taobao.com";
    public final static String REFER_ITEM = "detail_refer_item.taobao.com";
    public final static String REFER_STORE = "detail_refer_store.taobao.com";
    public final static String REFER_TRADE = "detail_refer_trade.taobao.com";

    public final static String REFER_FAVORITE = "detail_refer_favorite.taobao.com";
    public final static String REFER_SELL = "detail_refer_sell.taobao.com";
    public final static String REFER_BUY = "detail_refer_buy.taobao.com";
    public final static String REFER_LIST_MALL = "detail_refer_list.mall.taobao.com";
    public final static String REFER_OTHER = "detail_refer_other";
    
    
    //4. ic cache key 
    public final static String IC_CACHE_SUCC = "detail_ic_cache_success_AVERAGEUSERTIMES";
    public final static String IC_CACHE_FAILURE = "detail_ic_cache_failure_AVERAGEUSERTIMES";
    
    //4. uic cache key 
    public final static String UIC_CACHE_SUCC = "detail_uic_cache_success_AVERAGEUSERTIMES";
    public final static String UIC_CACHE_DATA_SUCC = "detail_uic_data_cache_success_AVERAGEUSERTIMES";
    

    // detail中数据数据的分类
    public final static int PAGE_GROUP = 1;
    public final static int RESP_GROUP = 2;
    public final static int REFER_GROUP = 3;
    private static final String PREFIX_PAGE = "detail_page";
    private static final String PREFIX_RESP = "detail_resp";
    private static final String PREFIX_REFE = "detail_refer";
    private static final String PREFIX_IC = "detail_ic";
    
    private static final String IC_CACHE = "detail_ic";
    private static Map<Integer, String> groupId2prefix = new HashMap<Integer, String>();

    private static Map<String, Integer> prefix2groupId = new HashMap<String, Integer>();

    static {

	groupId2prefix.put(1, PREFIX_PAGE);
	groupId2prefix.put(2, PREFIX_RESP);
	groupId2prefix.put(3, PREFIX_REFE);

	prefix2groupId.put(PREFIX_PAGE, 1);
	prefix2groupId.put(PREFIX_RESP, 2);
	prefix2groupId.put(PREFIX_REFE, 3);

    }

    /**
     * 根据对应的prefix得到groupId
     * @param prefix
     * @return
     */
    public static int getGroupIdByPrefix(String prefix) {

	return prefix2groupId.get(prefix);
    }

    /**
     * 根据groupId得到对应的prefix
     * @param groupId
     * @return
     */
    public static String getPrefixByGroupId(Integer groupId) {

	return groupId2prefix.get(groupId);
    }

    // public final static String REFER_SEARCH_8 = "search8.taobao.com";

    public static String genPageSizeKey(long pageSize) {
	String key = PAGE_SIZE_0_30K;

	if (pageSize >= PAGE_SIZE_30K && pageSize < PAGE_SIZE_40K) {
	    key = PAGE_SIZE_30_40K;
	}

	if (pageSize >= PAGE_SIZE_40K && pageSize < PAGE_SIZE_50K) {
	    key = PAGE_SIZE_40_50K;
	}
	if (pageSize >= PAGE_SIZE_50K && pageSize < PAGE_SIZE_60K) {
	    key = PAGE_SIZE_50_60K;
	}
	if (pageSize >= PAGE_SIZE_60K && pageSize < PAGE_SIZE_70K) {
	    key = PAGE_SIZE_60_70K;
	}
	if (pageSize >= PAGE_SIZE_70K && pageSize < PAGE_SIZE_80K) {
	    key = PAGE_SIZE_70_80K;
	}
	if (pageSize >= PAGE_SIZE_80K && pageSize < PAGE_SIZE_90K) {
	    key = PAGE_SIZE_80_90K;
	}
	if (pageSize >= PAGE_SIZE_90K && pageSize < PAGE_SIZE_100K) {
	    key = PAGE_SIZE_90_100K;
	}
	if (pageSize >= PAGE_SIZE_100K) {
	    key = PAGE_SIZE_100_upper;
	}

	return key;
    }

    public static String genResponseTimeKey(long responseTime) {
	String key = RESPONSE_TIME_0_50;

	if (responseTime >= RESPONSE_TIME_50 && responseTime < RESPONSE_TIME_60) {
	    key = RESPONSE_TIME_50_60;
	}

	if (responseTime >= RESPONSE_TIME_60 && responseTime < RESPONSE_TIME_70) {
	    key = RESPONSE_TIME_60_70;
	} else if (responseTime >= RESPONSE_TIME_70 && responseTime < RESPONSE_TIME_80) {
	    key = RESPONSE_TIME_70_80;
	} else if (responseTime >= RESPONSE_TIME_80 && responseTime < RESPONSE_TIME_90) {
	    key = RESPONSE_TIME_80_90;
	} else if (responseTime >= RESPONSE_TIME_90 && responseTime < RESPONSE_TIME_100) {
	    key = RESPONSE_TIME_90_100;
	} else if (responseTime >= RESPONSE_TIME_100 && responseTime < RESPONSE_TIME_110) {
	    key = RESPONSE_TIME_100_110;
	} else if (responseTime >= RESPONSE_TIME_110) {
	    key = RESPONSE_TIME_110_upper;
	}

	return key;
    }

    public static String genReferKey(String refer) {

	String key = REFER_OTHER;

	// 没有refer信息
	if (refer == null || refer.length() < 5) {
	    key = REFER_OTHER;
	}
	// 1. search.taobao.com

	if (refer.indexOf("search.") > -1) {
	    key = REFER_SEARCH;
	} else
	// 2. search8.taobao.com
	if (refer.indexOf("search8.") > -1) {
	    key = REFER_SEARCH_8;
	} else
	// 3. list.taobao.com
	if (refer.indexOf("list.t") > -1 || pattern4List.matcher(refer).find() ) {
	    key = REFER_LIST;
	} else
	// 4. item.taobao.com
	if (refer.indexOf("item.ta") > -1) {
	    key = REFER_ITEM;
	} else
	// 5. store.taobao.com, shop2232.taobao.com
	if (refer.indexOf("store.t") > -1 || pattern.matcher(refer).find()) {
	    key = REFER_STORE;
	} else
	// 6. trade.taobao.com
	if (refer.indexOf("trade.t") > -1) {
	    key = REFER_TRADE;
	} else
	// 7. favorite.taobao.com
	if (refer.indexOf("favorite.t") > -1) {
	    key = REFER_FAVORITE;
	} else
	// 8. sell.taobao.com
	if (refer.indexOf("sell.t") > -1) {
	    key = REFER_SELL;
	} else
	// 9. buy.taobao.com
	if (refer.indexOf("buy.t") > -1) {
	    key = REFER_BUY;
	} else
	// 10. list.mall.taobao.com
	if (refer.indexOf("list.m") > -1) {
	    key = REFER_LIST_MALL;
	} else
	// 11 商城店铺
	if (refer.indexOf("mall.t") > -1) {
	    key = REFER_STORE;
	}

	return key;

    }


}
