
package com.taobao.csp.monitor.impl.analyse.top;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;

/**
 * 
 * 
 * top - 19:26:33 up 11:01,  3 users,  load average: 0.00, 0.01, 0.00
Tasks:  59 total,   1 running,  58 sleeping,   0 stopped,   0 zombie
Cpu(s):  1.4% us,  0.1% sy,  0.0% ni, 98.2% id,  0.3% wa,  0.0% hi,  0.0% si
Mem:   4194436k total,  4175400k used,    19036k free,    14752k buffers
Swap:  2096472k total,      128k used,  2096344k free,  3928928k cached
 * 
 * @author xiaodu
 * @version 2010-4-28 下午07:25:10
 */
public class TopLogAnalyse extends AbstractDataAnalyse{
	
	public TopLogAnalyse( String appName,String ip,String feature) {
		super( appName,ip, feature);
	}
	private static final Logger logger =  Logger.getLogger(TopLogAnalyse.class);
	
	private Queue<String> queue = new LinkedList<String>();
	
	private Map<String,Double> map = new HashMap<String, Double>();

	
	public void analyseOneLine(String line) {
		queue.add(line);
		
	}
	
	
	private static String loadKey = "System_LOAD_"+Constants.AVERAGE_USERTIMES_FLAG;
	
	private static String cpuKey = "System_CPU_"+Constants.AVERAGE_USERTIMES_FLAG;
	
	private static String swapKey = "System_SWAP_"+Constants.AVERAGE_USERTIMES_FLAG;
	private static Pattern swappattern = Pattern.compile("Swap:\\s+(\\d+)k\\s+total,\\s+(\\d+)k\\s+used");
	
	public void doAnalyse() {
		try {
			String top1 = null;
			boolean sec = false;
			while((top1=queue.poll())!=null){ //读取第二桢 的top 数据	
				if(top1.startsWith("top")){
					if(!sec){
						sec = true;
						continue;
					}else{
						sec = false;
					}
					Pattern pattern = Pattern.compile("\\s(\\d{2}:\\d{2}):\\d{2}\\s");			
					Matcher matcher = pattern.matcher(top1);			
					String time = null;
					if(matcher.find()){
						time = matcher.group(1);
					}
					if(time==null){
						return ;
					}
					Pattern pattern1 = Pattern.compile("load average:\\s?([\\d\\.]+),\\s?([\\d\\.]+),\\s?([\\d\\.]+)");
					Matcher matcher1 = pattern1.matcher(top1);
					String load = null;
					if(matcher1.find()){
						load = matcher1.group(1);
					}
					if(load!=null){
						if(map.get(loadKey)==null){
							map.put(loadKey, Double.parseDouble(load));
						}else{
							map.put(loadKey, Arith.div(Double.parseDouble(load)+map.get(loadKey), 2, 2));
						}
						
					}			
					queue.poll();			
					String cpu = queue.poll();//Cpu(s): 15.4% us,  2.6% sy,  0.0% ni, 82.1% id,  0.0% wa,  0.0% hi,  0.0% si
					if(cpu!=null){
						cpu = cpu.replaceAll(" ", "");
						Pattern pattern2 = Pattern.compile(":([\\d\\.]+)%us,([\\d\\.]+)%sy,");
						Matcher matcher2 = pattern2.matcher(cpu);
						if(matcher2.find()){
							String v1 = matcher2.group(1);
							String v2 = matcher2.group(2);				
							Double v = Double.parseDouble(v1)+Double.parseDouble(v2);
							if(map.get(cpuKey)==null){
								map.put(cpuKey, v);
							}else{
								map.put(cpuKey, Arith.div(v+map.get(cpuKey), 2, 2));
							}
						}
					}
					
					queue.poll();
					
					String swap = queue.poll();//Swap:  2096472k total,      176k used,  2096296k free,  1606096k cached
					if(swap != null){
						Matcher swapm = swappattern.matcher(swap);
						if(swapm.find()){
							String s1 = swapm.group(1);
							String s2 = swapm.group(2);	
							double v = Double.parseDouble(s2)/ Double.parseDouble(s1)*100;
							if(map.get(swapKey)==null){
								map.put(swapKey, v);
							}else{
								map.put(swapKey, Arith.div(v+map.get(swapKey), 2, 2));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		
	}
	

	@Override
	public void submit() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Double load = map.get(loadKey);
		Double cpu = map.get(cpuKey);
		Double swap = map.get(swapKey);
		if(load !=null&&cpu!=null&&swap!=null){
			try {
				CollectDataUtilMulti.collect(this.getAppName(), this.getIp(), cal.getTimeInMillis(), 
						new String[]{"TOPINFO"}, new KeyScope[]{KeyScope.HOST}, new String[]{"CPU","LOAD","SWAP"}, 
						new Object[]{cpu.floatValue(),load.floatValue(),swap.floatValue()},
						new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE});
			} catch (Exception e) {
				logger.error("TopLogAnalyse 分析出错",e);
			}
		}
	}

	@Override
	public void release() {
		queue.clear();
		map.clear();
		
	}
	

}