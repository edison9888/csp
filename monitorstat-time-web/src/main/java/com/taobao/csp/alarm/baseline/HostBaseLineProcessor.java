
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.baseline;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
 *
 * 下午2:57:07
 */
public class HostBaseLineProcessor extends BaseLineProcessor{
	
	
	private String cm;
	public String getCM(){
		return cm;
	}
	/**
	 * 
	 * @param cm 那个机房 
	 * @param appName
	 * @param keyName
	 * @param propertyName
	 * @param date
	 */
	public HostBaseLineProcessor(String cm,String appName, String keyName, String propertyName, Date date) {
		super(appName, keyName, propertyName,KeyScope.HOST.toString(), date);
		this.cm = cm.toUpperCase();
	}

	private int hostsize = 4;
	
	
	
	/**
	 * 将机器分机房
	 * @return
	 */
	private  Map<String,Set<String>> selectHost(){
		
		Map<String,HostPo> ipMap = CspCacheTBHostInfos.get().getHostInfoMapByOpsName(this.getAppName());
		
		Map<String,Set<String>> map = new HashMap<String, Set<String>>();
		
		for(Map.Entry<String,HostPo> entry:ipMap.entrySet()){
			String cm = entry.getValue().getHostSite();
			Set<String> set = map.get(cm.toUpperCase());
			if(set == null){
				set = new HashSet<String>();
				map.put(cm.toUpperCase(), set);
			}
			set.add(entry.getValue().getHostIp());
		}
		return map;
	}
	
	
	@Override
	public void process(Date date) {
		
		Map<String,Set<String>> mapHost = selectHost();
		Set<String> ips = mapHost.get(cm);
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
		return Util.combinBaseLineRowID(this.getAppName(), getKeyName(), getPropertyName(),this.cm)+Constants.S_SEPERATOR+week;
	}

}
