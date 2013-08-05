package com.taobao.monitor.web.backup;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.KeyValuePo;

/**
 * ��ʱ�䷶Χ�ڵĹ��˵�
 * @author wuhaiqian.pt
 *
 */
public class LimitTimeFilter extends Filter {
	
	private static final Logger logger = Logger.getLogger(LimitTimeFilter.class);
	private Date limitStart = null;	//��ʽΪHH:mm
	private Date limitEnd = null;
	
	private List<Map<String, KeyValuePo>> filterList = new ArrayList<Map<String, KeyValuePo>>();	//���˺������
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	/**
	 * ������д�����˵Ŀ�ʼ�ͽ���ʱ�� ʱ���ʽ��HH:mm
	 * @param start
	 * @param end
	 */
	public LimitTimeFilter(String start, String end) {
		
		try {
			this.limitStart = sdf.parse(start);
			this.limitEnd = sdf.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.info("���캯����ʱ���ʽת������");
		}
	}

	
	@Override
	public Map<String, KeyValuePo> doFilter(Map<String, KeyValuePo> map) {
		
		
		if(this.accept) {
			//logger.info("��ʼʹ��LimitTimeFilter����");
			
			Iterator<String> keys = map.keySet().iterator();

			while(keys.hasNext()) {
				
				try {
					String key = keys.next();
					Date time;
					time = sdf.parse(key);
					if(time.compareTo(this.limitStart) >= 0 && time.compareTo(this.limitEnd) <= 0) {
						
						keys.remove();
					}
				} catch (ParseException e) {
					logger.info("doFilter��ʱ���ʽת������");
				}
			}
		}
		return super.doFilter(map);
	}
	
}