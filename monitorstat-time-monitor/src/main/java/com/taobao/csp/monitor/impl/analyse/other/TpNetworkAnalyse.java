
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * 
 * tp的/home/admin/monitor/network/logs/monitor_network.log 检查网络情况
 * 
 * mixnotify211195.cm3 : xmt/rcv/%loss = 10/10/0%, min/avg/max = 0.35/0.39/0.45
 * 
 * @author xiaodu
 *
 * 下午12:10:49
 */

public class TpNetworkAnalyse extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(TpNetworkAnalyse.class);
	
	public TpNetworkAnalyse(String appName, String ip) {
		super(appName, ip);
	}
	
	/**
	 * @param appName
	 * @param ip
	 * @param feature
	 */
	public TpNetworkAnalyse(String appName, String ip, String feature) {
		super(appName, ip, feature);
	}

	private Pattern pattern1 = Pattern.compile(" xmt/rcv/%loss\\s*=\\s*\\d+/\\d+/(\\d+)%");
	private Pattern pattern2 = Pattern.compile("min/avg/max\\s*=\\s*[\\d\\.]+/([\\d\\.]+)/[\\d\\.]+");

	private Map<String,TpNetWork> networkMap = new HashMap<String, TpNetWork>();
	
	@Override
	public void analyseOneLine(String line) {
		String[] tmp = StringUtils.split(line, ":");
		
		if(tmp.length !=2){
			return ;
		}
		
		String hostname = tmp[0];
		String dataString =  tmp[1];
		String[] data =  StringUtils.split(dataString, ",");
		
		if(data.length != 2){
			return ;
		}
		
		TpNetWork net = networkMap.get(hostname);
		if(net== null){
			net = new TpNetWork();
			networkMap.put(hostname, net);
		}
		if(data.length == 2){
			String lossStr = data[0];
			try{
				Matcher m1 = pattern1.matcher(lossStr);
				if(m1.find()){
					String loss = m1.group(1);
					net.loss = Float.parseFloat(loss);
				}
			}catch (Exception e) {
			}
			String avgStr = data[1];
			try{
				Matcher m2 = pattern2.matcher(avgStr);
				if(m2.find()){
					String avg = m2.group(1);
					net.avg = Float.parseFloat(avg);
				}
			}catch (Exception e) {
			}
		}
		
	}

	@Override
	public void submit() {
		
		SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String d = sdf1.format(new Date());
		
		for(Map.Entry<String,TpNetWork> entry:networkMap.entrySet()){
			
			String time =d;
			
			TpNetWork tp = entry.getValue();
			
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), sdf1.parse(time).getTime(), new String[]{"ping",entry.getKey()},
						new KeyScope[]{KeyScope.NO,KeyScope.HOST}, new String[]{"loss","avg"}, 
						new Object[]{tp.loss,tp.avg},new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE});
			}catch (Exception e) {
				logger.error("发送失败", e);
			}
		}
	}

	@Override
	public void release() {
		networkMap.clear();
	}
	
	
	private class TpNetWork{
		private float loss;
		
		private float avg;
	}
	
	
	public static void main(String[] ags){
		
		TpNetworkAnalyse tp = new TpNetworkAnalyse("","");
		
		tp.analyseOneLine("tradenotify049157.cm4 : xmt/rcv/%loss = 10/10/1%, min/avg/max = 0.19/0.22/0.29");
		tp.analyseOneLine("======================= NotifyCm4 2012-07-28 12:11 =======================");
		
		tp.submit();
		
	}

}
