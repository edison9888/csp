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
 * 把时间范围内的过滤掉
 * @author wuhaiqian.pt
 *
 */
public class LimitTimeFilter extends Filter {
	
	private static final Logger logger = Logger.getLogger(LimitTimeFilter.class);
	private Date limitStart = null;	//格式为HH:mm
	private Date limitEnd = null;
	
	private List<Map<String, KeyValuePo>> filterList = new ArrayList<Map<String, KeyValuePo>>();	//过滤后放在这
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	/**
	 * 参数填写被过滤的开始和结束时间 时间格式：HH:mm
	 * @param start
	 * @param end
	 */
	public LimitTimeFilter(String start, String end) {
		
		try {
			this.limitStart = sdf.parse(start);
			this.limitEnd = sdf.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.info("构造函数中时间格式转换出错");
		}
	}

	
	@Override
	public Map<String, KeyValuePo> doFilter(Map<String, KeyValuePo> map) {
		
		
		if(this.accept) {
			//logger.info("开始使用LimitTimeFilter过滤");
			
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
					logger.info("doFilter中时间格式转换出错");
				}
			}
		}
		return super.doFilter(map);
	}
	
}