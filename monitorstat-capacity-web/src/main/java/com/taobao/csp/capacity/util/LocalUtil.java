package com.taobao.csp.capacity.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.taobao.arkclient.ArkDomain;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ProductLine;
import com.taobao.monitor.common.util.TBProductCache;

/***
 * The basic util class
 * @author youji.zj
 *
 */
public class LocalUtil {
	
	public static String getCurrentUser(HttpServletRequest request) {
		
		return ArkDomain.getArkUserEmail(request);
		
	}
	
	public static Date getYesToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}
	
	/***
	 * 暂时写死6月14日，db的数据是手动导入的为14日，稳定了再改
	 * @return
	 */
	public static Date getCapacityCostDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2012);
		calendar.set(Calendar.MONTH, 7);  // 5表示6月
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// calendar.add(Calendar.DAY_OF_MONTH, -2);
		return calendar.getTime();
	}
	
	public static String transferUnit(long num) {
		String value = String.valueOf(num);
		
		if (num >= 10000) {
			BigDecimal divident = new BigDecimal(value);
			BigDecimal divisor = new BigDecimal("10000");
			BigDecimal answer = divident.divide(divisor, 2, RoundingMode.HALF_UP);
			value = String.valueOf(answer.doubleValue()) + "万";
		}
		
		return value;
	}
	
	public static String getPe(int appId) {
		String pe = "";
		int realId = AppGroupUtil.getSourceAppId(appId);
		AppInfoPo appPo = AppInfoAo.get().findAppInfoById(realId);
		ProductLine productLine = TBProductCache.getProductLineByAppName(appPo.getOpsName());
		
		if (productLine.getPe() != null) {
			pe = productLine.getPe();
		}
		
		return pe;
	}

}
