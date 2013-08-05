package com.taobao.monitor.web.backup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 把相同collectTime,App,Key累加做平均，这样相当于减少了：总数据/host总数
 * @author wuhaiqian.pt
 *
 */
public class AverageFilter extends Filter {
	
	private static final Logger logger = Logger.getLogger(AverageFilter.class);
	
	private List<Map<String, KeyValuePo>> filterList = new ArrayList<Map<String, KeyValuePo>>();	//过滤后放在这

	@Override
	public Map<String, KeyValuePo> doFilter(Map<String, KeyValuePo> map) {	
		//keyValuePo中把相同collectTime, app, key的但有不同site的m_data放到po中的一个siteValueMap
		
		if(this.accept) {
			
			//logger.info("开始使用AverageFilter过滤");
			for(Object key : map.keySet()) {
				
				KeyValuePo po = map.get(key);
				Double sum = Double.valueOf(0);
				
				for(Object key1 : po.getSiteValueMap().keySet()) {
					
					sum += po.getSiteValueMap().get(key1);
				}
				if(po.getSiteValueMap().size() != 0) {
					try{
						double backupMdata = Arith.div(sum, po.getSiteValueMap().size());
						po.setBackupSum(backupMdata);
					}catch (Exception e) {
						logger.error("", e);
					}
					
				}
			}
				
		}
		return super.doFilter(map);		
	}
}
