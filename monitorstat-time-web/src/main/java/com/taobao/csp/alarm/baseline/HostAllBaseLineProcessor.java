
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.baseline;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.dataserver.util.Util;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 * 不区分机房。全部机器抽样
 * 下午2:57:07
 */
public class HostAllBaseLineProcessor extends BaseLineProcessor{
	/**
	 * @param appName
	 * @param keyName
	 * @param propertyName
	 * @param date
	 * @param cmSize机房的个数
	 */
	public HostAllBaseLineProcessor(String appName, String keyName, String propertyName, Date date, int cmSize) {
		super(appName, keyName, propertyName,KeyScope.HOST.toString(), date);
		if(cmSize > 0) {
			this.hostsize *= cmSize;//每个机房默认检查4个
		} else {
			this.hostsize = 8;
		}
	}

	private int hostsize = 4;	//因为不按机房，所以主机个数为8个
	/**
	 * 将机器分机房
	 * @return
	 */
	private  Set<String> selectHost(){

		Map<String,HostPo> ipMap = CspCacheTBHostInfos.get().getHostInfoMapByOpsName(this.getAppName());

		Set<String> set = new HashSet<String>();

		for(Map.Entry<String,HostPo> entry:ipMap.entrySet()){
			set.add(entry.getValue().getHostIp());
		}
		return set;
	}

	@Override
	public void process(Date date) {
		Set<String> ips = selectHost();
		int i=1;
		for(String ip:ips){
			if(i>hostsize){
				return ;
			}
			Map<Date,String> dateMap = QueryHistoryUtil.querySingleHost(this.getAppName(), this.getKeyName(), ip, this.getPropertyName(), date);
			if(dateMap.size() > 500){
				for(Map.Entry<Date,String> entry:dateMap.entrySet()){
					try {
						putData(formatDate(entry.getKey()), DataUtil.transformDouble(entry.getValue()));
					} catch (Exception e) {
					}
				}
				i++;
			}
		}
	}


	/* (non-Javadoc)
	 * @see com.taobao.csp.alarm.baseline.BaseLineProcessor#hbaseRowID()
	 */
	@Override
	protected String hbaseRowID() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getProcessDate());
		int week = cal.get(Calendar.DAY_OF_WEEK);
		return Util.combinBaseLineRowID(this.getAppName(), getKeyName(), getPropertyName())+Constants.S_SEPERATOR+week;
	}

}
