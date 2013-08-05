
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.query;

import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.dao.hbase.base.HBaseUtil;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.util.Util;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 *
 * 下午3:53:19
 */
public class QueryBaseLineUtil {
	
	public static Map<String,Double> queryHostBaseLine(String appName, String keyName, String propertyName,String ip) throws Exception{
		
		Map<String,Double> map = new HashMap<String, Double>();
		
		HostPo po = CspCacheTBHostInfos.get().getHostInfoByIp(ip);
		
		if(po == null){
			throw new Exception(ip+" IP 不存在");
		}
		
		String f = HBaseUtil.queryCellValue(Util.combinBaseLineRowID(appName, keyName, propertyName, po.getHostSite()), Constants.BASELINE_HBASE_COL, String.class);
		
		if(f == null){
			throw new Exception(" 基线不存在不存在");
		}
		
		String[] values = f.split(";");
		for(String value:values){
			String[] v = value.split(",");
			if(v.length ==2){
				map.put(v[0], Double.parseDouble(v[1]));
			}
		}
		return map;
	}
	
	
	public static Map<String,Double> queryBaseLine(String appName, String keyName, String propertyName) throws Exception{
		
		Map<String,Double> map = new HashMap<String, Double>();
		String f = HBaseUtil.queryCellValue(Util.combinBaseLineRowID(appName, keyName, propertyName), Constants.BASELINE_HBASE_COL, String.class);
		
		if(f == null){
			throw new Exception(" 基线不存在不存在");
		}
		
		String[] values = f.split(";");
		for(String value:values){
			String[] v = value.split(",");
			if(v.length ==2){
				map.put(v[0], Double.parseDouble(v[1]));
			}
		}
		return map;
		
	}

}
