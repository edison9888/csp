package com.taobao.monitor.web.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.core.dao.impl.MonitorAlarmDao;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 这是每隔三分钟就统计
 * @author wuhaiqian.pt
 *
 */
public class JumpFilter extends Filter {
	
	private static final Logger logger = Logger.getLogger(JumpFilter.class);
	private int loop;//循环标志，例如这里如果loop=3,那么就是每3分钟取一次记录
	private List<Map<String, KeyValuePo>> filterList = new ArrayList<Map<String, KeyValuePo>>();	//过滤后放在这
	
	/**
	 * 参数填写需要几分钟采集一次
	 * @param loop
	 */
	public JumpFilter(int loop) {
		
		this.loop = loop;
	}

	
	@Override
	public Map<String, KeyValuePo> doFilter(Map<String, KeyValuePo> map) {
		
		
		if(this.accept) {
			//logger.info("开始使用JumpFilter过滤");
			Map<String, KeyValuePo> m = new HashMap<String, KeyValuePo>();
			
			for(String key : map.keySet()) {
				//key是HH:mm
				int temp = Integer.parseInt(key.substring(3, 5));
				if(temp % 3 == 0) {
					
					m.put(key, map.get(key));
				}
			}
			
			
			return super.doFilter(m);
		} else {
			return super.doFilter(map);
		}
	}
	
	public int getLoop() {
		return loop;
	}

	public void setLoop(int loop) {
		this.loop = loop;
	}
}
