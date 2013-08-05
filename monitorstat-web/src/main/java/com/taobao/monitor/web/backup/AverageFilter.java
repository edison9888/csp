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
 * ����ͬcollectTime,App,Key�ۼ���ƽ���������൱�ڼ����ˣ�������/host����
 * @author wuhaiqian.pt
 *
 */
public class AverageFilter extends Filter {
	
	private static final Logger logger = Logger.getLogger(AverageFilter.class);
	
	private List<Map<String, KeyValuePo>> filterList = new ArrayList<Map<String, KeyValuePo>>();	//���˺������

	@Override
	public Map<String, KeyValuePo> doFilter(Map<String, KeyValuePo> map) {	
		//keyValuePo�а���ͬcollectTime, app, key�ĵ��в�ͬsite��m_data�ŵ�po�е�һ��siteValueMap
		
		if(this.accept) {
			
			//logger.info("��ʼʹ��AverageFilter����");
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
