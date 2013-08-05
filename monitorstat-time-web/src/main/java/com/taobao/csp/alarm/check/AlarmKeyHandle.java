
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.check;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.service.UserService;
import com.taobao.monitor.common.po.CspKeyMode;

/**
 * @author xiaodu
 *
 * 下午9:15:45
 */
public class AlarmKeyHandle implements Runnable{


	private static final Logger logger = Logger.getLogger(AlarmKeyHandle.class);

	private Map<String,AlarmKeyProcessor> modeHandleMap = new ConcurrentHashMap<String, AlarmKeyProcessor>();

	private Thread thread = null;

	private boolean start = false;

	public synchronized void startup(){
		if(!start){
			thread = new Thread(this);
			thread.setName("Thread - AlarmKeyHandle");
			thread.start();
		}
	}


	public void clean(){
		modeHandleMap.clear();
	}

	public void addMode(CspKeyMode mode){
		String m = mode.getAppName()+"_"+mode.getKeyName()+"_"+mode.getPropertyName();

		AlarmKeyProcessor handle = new AlarmKeyProcessor(mode);
		handle.addModeConfig(mode);
		modeHandleMap.put(m, handle);
		logger.info("AlarmKeyHandle register "+mode.getAppName()+":"+mode.getKeyName()+":"+mode.getPropertyName()+":"+mode.getKeyScope()+" :"+mode.getHostModeConfig()+mode.getAppModeConfig());


	}

	public void updateMode(CspKeyMode mode){
		String m = mode.getAppName()+"_"+mode.getKeyName()+"_"+mode.getPropertyName();
		AlarmKeyProcessor handle = modeHandleMap.get(m);
		if(handle != null){
			handle.addModeConfig(mode);
			logger.info("AlarmKeyHandle update  "+mode.getAppName()+":"+mode.getKeyName()+":"+mode.getPropertyName()+":"+mode.getKeyScope()+" :"+mode.getModeConfig());
		}
	}

	public void removeMode(CspKeyMode mode){
		String m = mode.getAppName()+"_"+mode.getKeyName()+"_"+mode.getPropertyName();
		modeHandleMap.remove(m);

		logger.info("AlarmKeyHandle remove  "+mode.getAppName()+":"+mode.getKeyName()+":"+mode.getPropertyName()+":"+mode.getKeyScope()+" :"+mode.getModeConfig());
	}


	@Override
	public void run() {

		int i = 0;
		while(true){
			try {
//				Thread.sleep(60000);
				TimeUnit.MINUTES.sleep(1); 
			} catch(Exception e) {
				logger.error(e.toString());
			}

			i++;
			if(i==10) {
				i=0;
				UserService.get().init();	//每10分钟重新读取一次报警信息
			}
			logger.info("alarm check 。。。  modeHandle："+modeHandleMap.size());

			Iterator<Map.Entry<String,AlarmKeyProcessor>> it =  modeHandleMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,AlarmKeyProcessor> e = it.next();
				AlarmKeyProcessor handle = e.getValue();

				handle.doAppAlarmHandle();
				handle.doHostAlarmHandle();
			}
		}
	}


}
