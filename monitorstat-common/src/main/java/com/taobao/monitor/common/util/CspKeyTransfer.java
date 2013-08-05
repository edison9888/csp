package com.taobao.monitor.common.util;

import com.alibaba.common.lang.StringUtil;

/**
 * 
 * @author zhongting.zy
 *
 */
public class CspKeyTransfer {
	public static String changeDependHsfProviderKeyToEagleeyeKey(String cspHsfKey) throws Exception {
		if(StringUtil.isBlank(cspHsfKey))
			throw new Exception("hsfKey is blank");
		//IN_HSF-ProviderDetail_com.taobao.item.service.ItemBidService:1.0.0_updateItemQuantity~liLlA
		String tmp1 = cspHsfKey.substring(22,cspHsfKey.indexOf(':'));
		String tmp2 = cspHsfKey.substring(cspHsfKey.lastIndexOf('_'));
		return tmp1 + tmp2;
	}
	
	public static String changeSentinueHsfKeyToEagleeyeKey(String sentinueKey) throws Exception {
		if(StringUtil.isBlank(sentinueKey))
			throw new Exception("sentinueKey is blank");
		//method:L0//com.taobao.item.service.ItemQueryService:queryItemById(long,com.taobao.item.domain.query.QueryItemOptionsDO,com.taobao.item.domain.AppInfoDO)
		//method:BID//com.taobao.item.service.ItemBidService:updateItemQuantityWithAntiOverSold(long,int,java.lang.Long,long,com.taobao.item.domain.AppInfoDO)
		String key = sentinueKey.substring(sentinueKey.indexOf("//") + 2,sentinueKey.indexOf("(")).replace(':', '_');
		String tmp2 = sentinueKey.substring(sentinueKey.lastIndexOf('(') + 1, sentinueKey.indexOf(')'));
		
		return key + getLastString(tmp2);
	}
	
	private static String getLastString(String tmp2) {
		if(StringUtil.isBlank(tmp2))
			return "";
		String[] array = tmp2.split(",");
		StringBuilder sb = new StringBuilder("~");
		for(String param : array) {
			int length = param.lastIndexOf('.');
			if(length >= 0)
				param = param.substring(length + 1);
			sb.append(param.charAt(0));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(changeDependHsfProviderKeyToEagleeyeKey("IN_HSF-ProviderDetail_com.taobao.item.service.AdminService:1.0.0_cronUpdateQuantityAndDownShelfBidItem~lIA"));
		System.out.println(changeSentinueHsfKeyToEagleeyeKey("method:L0//com.taobao.item.service.ItemQueryService:queryItemById(long,com.taobao.item.domain.query.QueryItemOptionsDO,com.taobao.item.domain.AppInfoDO)"));
	}
}
