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
 * ����ÿ�������Ӿ�ͳ��
 * @author wuhaiqian.pt
 *
 */
public class JumpFilter extends Filter {
	
	private static final Logger logger = Logger.getLogger(JumpFilter.class);
	private int loop;//ѭ����־�������������loop=3,��ô����ÿ3����ȡһ�μ�¼
	private List<Map<String, KeyValuePo>> filterList = new ArrayList<Map<String, KeyValuePo>>();	//���˺������
	
	/**
	 * ������д��Ҫ�����Ӳɼ�һ��
	 * @param loop
	 */
	public JumpFilter(int loop) {
		
		this.loop = loop;
	}

	
	@Override
	public Map<String, KeyValuePo> doFilter(Map<String, KeyValuePo> map) {
		
		
		if(this.accept) {
			//logger.info("��ʼʹ��JumpFilter����");
			Map<String, KeyValuePo> m = new HashMap<String, KeyValuePo>();
			
			for(String key : map.keySet()) {
				//key��HH:mm
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
