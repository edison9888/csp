package com.taobao.csp.monitor.impl.analyse.apache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
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
 * modify by zhongting.zy ，日志格式发生变化，对应日志：
 * 日志格式：/home/admin/cai/logs/tmd.log
 * 上午9:10:47
 */
public class TdodLogAnalyse extends AbstractDataAnalyse {
	public TdodLogAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
		// TODO Auto-generated constructor stub
	}
	Logger logger = Logger.getLogger(TdodLogAnalyse.class);
	private Map<String,Integer> countmap = new HashMap<String, Integer>();

	public void analyseOneLine(String line) {
		/**
		 * 2012/10/22 00:14:17 [info] 10665#0: [Shopsystem] workmode: checkcode, cookie: , address: 203.208.60.186, url: www.zkj.cc/do/job.php, PassMode: 0, phase: blacklist
		 */
		String[] args = line.split(",");
		if(args.length ==6){
			String PassMode = args[4].trim();
			if(PassMode.endsWith("0")) { //TMD server端开启拦截.
				String time = args[0].trim().substring(0,16) + ":00";
				String workMode = args[0].substring(args[0].lastIndexOf(':') + 1).trim();
				if(workMode.equals("punish")|| workMode.equals("checkcode") || workMode.equals("hourglass")) {
					Integer c = countmap.get(time);
					if(c ==null){
						countmap.put(time, 1);
					}else{
						countmap.put(time, 1+c);
					}					
				}
			} else {
				//TMD 观察者模式或其他
				return;
			}
		}
	}

	@Override
	public void submit() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//		String d = sdf.format(new Date());
		SimpleDateFormat sdf1= new SimpleDateFormat("yyyy/MM/dd HH:mm");
		for(Map.Entry<String,Integer> entry:countmap.entrySet()){
			String time =entry.getKey();
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), sdf1.parse(time).getTime(), new String[]{KeyConstants.PV_BLOCK},
						new KeyScope[]{KeyScope.ALL}, new String[]{PropConstants.TDOD}, 
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
		TdodLogAnalyse analyse = new TdodLogAnalyse("aaaa", "25.363.2.3","");
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("d:/tmp/tmd.log")));
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