
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * 
 * 这个是君山 Cachechecker失效varnish的应用
 * hosts代表失效的机器数，itemsize代表失效的商品数，time代表发送消耗的时间是ms

 * @author xiaodu
 *
 * 下午2:00:08
 */
public class CachecheckerLogAnalyse extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(CachecheckerLogAnalyse.class);

	/**
	 * @param appName
	 * @param ip
	 * @param feature
	 */
	public CachecheckerLogAnalyse(String appName, String ip, String feature) {
		super(appName, ip, feature);
	}
	
	public CachecheckerLogAnalyse(String appName, String ip) {
		super(appName, ip, "");
	}
	
	private Map<String,int[]> map = new HashMap<String,int[]>();

	@Override
	public void analyseOneLine(String line) {
		//2012-06-11 14:38:12,455 WARN  monitor - hosts:267,itemsize:243,time:814
		String[] tmp = StringUtils.split(line, " ");
		if(tmp.length !=6){
			return ;
		}
		
		String msg = tmp[5];
		String time = line.substring(0, 16);
		String[] m = StringUtils.split(msg,",");
		int[] t = map.get(time);
		if(t==null){
			t = new int[4];
			map.put(time, t);
		}
		for(String s:m){
			String[] kv = s.split(":");
			if(kv.length == 2){
				if("hosts".equals(kv[0])){
					t[0]+=Integer.parseInt(kv[1]);
				}else if("itemsize".equals(kv[0])){
					t[1]+=Integer.parseInt(kv[1]);
				}else if("time".equals(kv[0])){
					t[2]+=Integer.parseInt(kv[1]);
					t[3]+=1;
				}
			}
		}

	}

	@Override
	public void submit() {
		SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(Map.Entry<String,int[]> entry:map.entrySet()){
			String time = entry.getKey();
			int h = entry.getValue()[0];
			int i = entry.getValue()[1];
			int c = entry.getValue()[2];
			int s = entry.getValue()[3];
			
			int v = (s==0?0:c/s);
			
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), sdf1.parse(time).getTime(), new String[]{"cachechecker-varnish"},
						new KeyScope[]{KeyScope.HOST}, new String[]{"hosts","itemsize","costtime"}, 
						new Object[]{h,i,v},new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD,ValueOperate.AVERAGE});
			}catch (Exception e) {
				logger.error("发送失败", e);
			}
			
		}
	}

	@Override
	public void release() {
		map.clear();
	}

	public static void main(String[] args){
		
		CachecheckerLogAnalyse analyse = new CachecheckerLogAnalyse("cachechecker","10.232.15.53");
		analyse.analyseOneLine("2012-06-13 18:58:53,11 WARN  monitor - hosts:267,itemsize:243,time:814");
		analyse.analyseOneLine("2012-06-13 18:57:53,11 WARN  monitor - hosts:267,itemsize:243,time:814");
		analyse.analyseOneLine("2012-06-13 18:56:53,11 WARN  monitor - hosts:267,itemsize:243,time:814");
		analyse.analyseOneLine("2012-06-13 18:55:53,11 WARN  monitor - hosts:267,itemsize:243,time:814");
		analyse.submit();
	}
	
	
}
