/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu 针对cart /home/admin/cart/logs/dbMonitor.log 日志格式 2012-02-28
 *         19:21:18,896 WARN dbMonitor - mysql5_queryCartItemsByUserId,1 前面是分库名
 *         请求方法 平均响应时间 下午1:27:31
 */
public class CartDbmonitorLogAnalyse extends AbstractDataAnalyse {

	private Map<String, Map<String, int[]>> timemap = new HashMap<String, Map<String, int[]>>();

	/**
	 * @param appName
	 * @param ip
	 */
	public CartDbmonitorLogAnalyse(String appName, String ip,String f) {
		super(appName, ip, f);
	}

	@Override
	public void analyseOneLine(String line) {

		try {
			String dateString = line.substring(0, 16);
			String[] tmp = StringUtils.split(line," ");
			String[] info = tmp[5].split(",");

			String timeout = info[1];
			String dbName = info[0].split("_")[0];

			Map<String, int[]> map = timemap.get(dateString);
			if (map == null) {
				map = new HashMap<String, int[]>();
				timemap.put(dateString, map);
			}

			int[] c = map.get(dbName);
			if (c == null) {
				c = new int[2];
				map.put(dbName, c);
			}
			c[0] += Integer.parseInt(timeout);
			c[1] += 1;
		} catch (Exception e) {
		}
	}

	@Override
	public void submit() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		for (Map.Entry<String, Map<String, int[]>> entry : timemap.entrySet()) {

			try {
				Date time = sdf.parse(entry.getKey());
				Map<String, int[]> map = entry.getValue();
				for (Map.Entry<String, int[]> g : map.entrySet()) {
					int[] t = g.getValue();
					CollectDataUtilMulti.collect(this.getAppName(), this.getIp(), time.getTime(), new String[] { "cartdb操作相关", g.getKey() }, new KeyScope[] {
							KeyScope.NO, KeyScope.HOST }, new String[] { PropConstants.C_TIME }, new Object[] { t[0] / t[1] },
							new ValueOperate[] { ValueOperate.AVERAGE });
				}
			} catch (Exception e) {
			}

		}

	}

	@Override
	public void release() {
		// TODO Auto-generated method stub

	}
	
	
	
	public static void main(String[] args) {
		CartDbmonitorLogAnalyse job = new CartDbmonitorLogAnalyse("itemcenter","172.24.12.99","");
		
		job.analyseOneLine("2012-05-17 13:42:37,653 WARN  dbMonitor - mysql9_queryCartItemsByUserId,2");
		
	}

}
