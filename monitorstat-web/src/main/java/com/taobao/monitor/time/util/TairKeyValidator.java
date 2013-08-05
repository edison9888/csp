package com.taobao.monitor.time.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class TairKeyValidator {
	public static String TAIR_KEY_PREFIX = "Tair";
	///////////tradeplatform
	/**
	 * tair的key值 kdbtc_dtair,kdbtc2_dtair,kdbtc3_dtair,	ldbtc4_dtair
	 * 匹配格式group_kdbtc3`1002 
	 * 匹配格式group_ldbtc4
	 */
	private static String tpSnapShotTairInfix = "dbtc";
	private static String tpSnapShotTairDesc = "交易快照Tair告警"; 

	private static String tpUicTairInfix = "group3";
	private static String tpUicTairDesc = "访问UIC的tair告警"; 
	
	private static String tpTradeTairInfix = "group_1";
	private static String tpTradeTairDesc = "创建订单去重校验的tair";
	
	private static String tpDetailTairInfix = "comm";
	private static String tpDetailTairDesc = "detail页面的成交记录的tair";
	
	private static String tpMseckilTairInfix = "mseckill";
	private static String tpMseckilTairDesc = "统计超卖情况的tair";
	
	private static String tpSeckilTairRegex = ".+[^m]seckill";
	private static String tpSeckilTairDesc = "秒杀 买家批量主订单确认收货的时间限制  保存sid用于验证码出现，订单count数，防止买卖家炒作信用的count计数，小额订单的限制，卖家auctionTitle的查询限制的tair";
	
	private static String tpItemTairInfix = "group1";
	private static String tpItemTairDesc = "交易公用tair、平均退款时间、去O开关的tair。";
	
	///misccenter
	/**
	 * tair的key值 kdbtc_dtair,kdbtc2_dtair,kdbtc3_dtair,	ldbtc4_dtair
	 * 匹配格式group_kdbtc3`1002 
	 * 匹配格式group_ldbtc4
	 */
	private static String mcGroup1TairInfix = "group_1";
	private static String mcGroup1TairDesc = "交易公用tair、平均退款时间、去O开关。";
	
	private static String mcMseckilTairInfix = "mseckill";
	private static String mcMseckilTairDesc = "秒杀业务、超卖、C2B求购市场的tair";
	
	private static String mcMcommTairInfix = "mcomm";
	private static String mcMcommTairDesc = "直充宝贝优选业务、购物车活动缓存、拍卖、交易合约的tair";
	
	private static String mcMarketTairInfix = "market";
	private static String mcMarketTairDesc = "卡易售文件信息缓存的tair";
	
	private static String mcItemTairInfix = "group1";
	private static String mcItemTairDesc = "交易公用tair，用于平均退款时间、去O开关。";
	
	///cart
	/**
	 * tair的key值 
	 * 匹配格式group_kdbtc3`1002 
	 * 匹配格式group_ldbtc4
	 */
	private static String cartGroup3TairInfix = "group3";
	private static String cartGroup3TairDesc = "Uic tair"; 
	
	private static String cartCommTairInfix = "comm";
	private static String cartCommTairDesc = "mini购物车数字和列表的tair";
	
	private static String cartItemTairInfix = "group1";
	private static String cartItemTairDesc = "查询商品的tair"; 
	
	///tradeface
	/**
	 * tair的key值 
	 * 匹配格式group_kdbtc3`1002 
	 * 匹配格式group_ldbtc4
	 */
	private static String tfMseckilTairInfix = "mseckil";
	private static String tfMseckilTairDesc = "秒杀用，商品，黑名单和只允许部分用户参加的白名单、消保315活动的tair"; 
	
	private static String tfMcommTairInfix = "mcomm";
	private static String tfMcommTairDesc = "统计当前三分钟内销量最高的商品排行榜,本地团用、物流的运费模板、Umidtair、存储用户虚拟交易信息、拍卖临时活动、已买到列表htm历史库订单默认列表的、电子凭证买家卖家每天重发短信记录 tair、修改价格参数过长缓存、HTM卖家访问频率控制共用、新注册用户发红包、关闭交易和已卖出列表限制、交易安全";
	
	private static String tfSnscommTairInfix = "snscomm";
	private static String tfSnscommTairDesc = "聚划算tair";
	
	private static String tfSeckillTairInfix = "seckill";
	private static String tfSeckillTairDesc = "秒杀集群、已买到页头count、关闭交易和已卖出列表限制的tair";
	
	private static String tfMarketTairInfix = "market";
	private static String tfMarketTairDesc = "卡易售游戏的xml文件内容、区服等业务的tair"; 
	
	private static String tfItemTairInfix = "group1";
	private static String tfItemTairDesc = "商品缓存的tair";
	
	private static String tfUicTairInfix = "group3";
	private static String tfUicTairDesc = "Uic tair"; 
	
	private static String tfSessionTairInfix = "session";
	private static String tfSessionTairDesc = "Uic Session Tair"; 
	
	//tradelogs
	/**
	 * tair的key值 
	 * 匹配格式group_kdbtc3`1002 
	 * 匹配格式group_ldbtc4
	 */
	private static String tlSeckilTairInfix = "seckil";
	private static String tlSeckilTairDesc = "订单count[卖家的提醒计数、买家的提醒计数、商品的累计售出数]的tair"; 
	
	private static String tlMcommTairInfix = "mcomm";
	private static String tlMcommTairDesc = "商品的最近成交记录的tair";
	
	private static String tlTradeTairInfix = "group_1";
	private static String tlTradeTairDesc = "交易缓存化的tair"; 
	
	/**
	 * 判断是否是tair的key值
	 * @param keyName
	 * @return keyDesc
	 */
	public static boolean isTairKey(String keyName){
		return (StringUtils.isNotBlank(keyName) && keyName.startsWith(TAIR_KEY_PREFIX));
	}
	
	/**
	 * 通过appName和key值获取key值的作用
	 * @param keyName
	 * @return keyDesc
	 */
	public static String getTairKeyDescByAppNameAndKeyName(String appName, String keyName){
		if("tradeplatform".equals(appName)){
			return getTradeplatformTairKeyDesc(keyName);
		}
		if("tf_buy".equals(appName) || "tf_tm".equals(appName)){
			return getTradefaceTairKeyDesc(keyName);
		}
		if("cart".equals(appName)){
			return getCartTairKeyDesc(keyName);
		}
		if("misccenter".equals(appName)){
			return getMisccenterTairKeyDesc(keyName);
		}
		if("tradelogs".equals(appName)){
			return getMisccenterTairKeyDesc(keyName);
		}
		return ("未配置应用:"+appName);
	}
	
	/**
	 * 通过key值获取key值的作用
	 * @param keyName
	 * @return keyDesc
	 */
	public static String getTradeplatformTairKeyDesc(String keyName){
		if(keyName.contains(tpSnapShotTairInfix)){
			return tpSnapShotTairDesc;
		}
		if(keyName.contains(tpUicTairInfix)){
			return tpUicTairDesc;
		}
		if(keyName.contains(tpTradeTairInfix)){
			return tpTradeTairDesc;
		}
		if(keyName.contains(tpDetailTairInfix)){
			return tpDetailTairDesc;
		}
		if(keyName.contains(tpMseckilTairInfix)){
			return tpMseckilTairDesc;
		}
		if( Pattern.matches(tpSeckilTairRegex, keyName) ){
			return tpSeckilTairDesc;
		}
		if(keyName.contains(tpItemTairInfix)){
			return tpItemTairDesc;
		}
		return (keyName+"未配置业务含义");
	}
	
	public static String getMisccenterTairKeyDesc(String keyName){
		if(keyName.contains(mcGroup1TairInfix)){
			return mcGroup1TairDesc;
		}
		if(keyName.contains(mcMseckilTairInfix)){
			return mcMseckilTairDesc;
		}
		if(keyName.contains(mcMcommTairInfix)){
			return mcMcommTairDesc;
		}
		if(keyName.contains(mcMcommTairInfix)){
			return mcMcommTairDesc;
		}
		if(keyName.contains(mcMarketTairInfix)){
			return mcMarketTairDesc;
		}
		if(keyName.contains(mcItemTairInfix)){
			return mcItemTairDesc;
		}
		return (keyName+"未配置业务含义");
	}
	
	public static String getCartTairKeyDesc(String keyName){
		if(keyName.contains(cartGroup3TairInfix)){
			return cartGroup3TairDesc;
		}
		if(keyName.contains(cartCommTairInfix)){
			return cartCommTairDesc;
		}
		if(keyName.contains(cartItemTairInfix)){
			return cartItemTairDesc;
		}
		return (keyName+"未配置业务含义");
	}
	
	public static String getTradefaceTairKeyDesc(String keyName){
		if(keyName.contains(tfMseckilTairInfix)){
			return tfMseckilTairDesc;
		}
		if(keyName.contains(tfMcommTairInfix)){
			return tfMcommTairDesc;
		}
		if(keyName.contains(tfSnscommTairInfix)){
			return tfSnscommTairDesc;
		}
		if(keyName.contains(tfSeckillTairInfix)){
			return tfSeckillTairDesc;
		}
		if(keyName.contains(tfMarketTairInfix)){
			return tfMarketTairDesc;
		}
		if(keyName.contains(tfItemTairInfix)){
			return tfItemTairDesc;
		}
		if(keyName.contains(tfUicTairInfix)){
			return tfUicTairDesc;
		}
		if(keyName.contains(tfSessionTairInfix)){
			return tfSessionTairDesc;
		}
		return (keyName+"未配置业务含义");
	}
	
	public static String getTradelogsTairKeyDesc(String keyName){
		if(keyName.contains(tlSeckilTairInfix)){
			return tlSeckilTairDesc;
		}
		if(keyName.contains(tlMcommTairInfix)){
			return tlMcommTairDesc;
		}
		if(keyName.contains(tlTradeTairInfix)){
			return tlTradeTairDesc;
		}
		return (keyName+"未配置业务含义");
	}
}
