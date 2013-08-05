
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu
 * SS的日志解析器，采集路径:/home/admin/logs/sph.log
 * 上午9:10:47
 */
public class BlockCountAnalyse extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(BlockCountAnalyse.class);
	
	private Map<String,Integer> countmap = new HashMap<String, Integer>();
	
	/**
	 * @param appName
	 * @param ip
	 */
	public BlockCountAnalyse(String appName, String ip,String f) {
		super(appName, ip, f);
	}

	@Override
	public void analyseOneLine(String line) {
		String[] args = line.split(" ");
		if(args.length ==4){
			String time = args[0]+" "+args[1].substring(0,5);
			String request = args[2];
			if(request.indexOf("mainRequest") >=0){
				Integer c = countmap.get(time);
				if(c ==null){
					countmap.put(time, 1);
				}else{
					countmap.put(time, 1+c);
				}
			}
		}
		
	}

	@Override
	public void submit() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String d = sdf.format(new Date());
		
		for(Map.Entry<String,Integer> entry:countmap.entrySet()){
			
			String time =d+"-"+entry.getKey()+":00";
			
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), sdf1.parse(time).getTime(), new String[]{KeyConstants.PV_BLOCK},
						new KeyScope[]{KeyScope.ALL}, new String[]{PropConstants.PV_SS}, 
						new Object[]{entry.getValue()},new ValueOperate[]{ValueOperate.ADD});
			}catch (Exception e) {
				logger.error("发送失败", e);
			}
			
		}
		
		
		
	}

	@Override
	public void release() {
		countmap.clear();
	}
	
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String d = sdf.format(new Date());
		String time =d+"-01 08:1:00";
		try {
			System.out.println(sdf1.parse(time));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BlockCountAnalyse analyse = new BlockCountAnalyse("aaaa", "25.363.2.3","");
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("d:/tmp/sph.log")));
			String line;
			while ((line = br.readLine()) != null) {
				analyse.analyseOneLine(line);
			}
			
			analyse.submit();
			analyse.release();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}