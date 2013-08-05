/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.event;

import org.apache.log4j.Logger;

import com.taobao.csp.dao.hbase.base.HBaseUtil;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.memcache.HbaseQueue;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.memcache.entry.PropertyEntry;
import com.taobao.monitor.MonitorLog;

/**
 * @author xiaodu
 * 
 *         上午11:12:21
 */
public class HostHbaseValueEvent implements DataEvent {

	private static final Logger logger = Logger
			.getLogger(HostHbaseValueEvent.class);

	public void onEvent(String appName,String propname,String fullName,long collectime,
			Object value) {
		try {
			if (value != null && collectime > 0) {
				String rowkey = HBaseUtil.getMD5String(fullName)+ Constants.S_SEPERATOR + collectime;

				long time = System.currentTimeMillis();

				// HBaseUtil.addRow(rowkey, propname, value.toString());
				HbaseQueue.get().addData(rowkey, propname, value.toString());
				MonitorLog.addStat(Constants.DATA_PERSISTENCE_LOG, new String[]{"Hbase_HOST","PV"}, new Long[]{1l,System.currentTimeMillis()-time});
				MonitorLog.addStat(Constants.DATA_PERSISTENCE_LOG, new String[] { "Hbase_HOST",appName }, new Long[] { 1l,System.currentTimeMillis() - time });

				logger.debug(rowkey + " " + propname + " " + value.toString());
			}
		} catch (Exception e) {
			logger.error("数据持久化到Hbase中异常", e);
		}
	}
	
	
}
