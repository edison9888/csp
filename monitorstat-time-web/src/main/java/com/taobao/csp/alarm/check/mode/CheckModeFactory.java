
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.check.mode;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.check.AlarmKeyProcessor;



/**
 * @author xiaodu
 *
 * 下午2:39:41
 */
public class CheckModeFactory {
	
	private static final Logger logger = Logger.getLogger(CheckModeFactory.class);
	
	
	public static CheckMode createCheckMode(String modeConfig){
		
		CheckMode checkMode = null;
		try{
			String[] modes = modeConfig.split("\\@");
			
			for(String mode:modes){
				String[] modeInfo = mode.split("\\^");
				if(checkMode == null)
					checkMode = createCheckMode(modeInfo[0],modeInfo[1]);
				else
					checkMode.setNext(createCheckMode(modeInfo[0],modeInfo[1]));
			}
		}catch (Exception e) {
			logger.error("创建检查模型:"+modeConfig, e);
		}
		return checkMode;
	}
	
	private static CheckMode createCheckMode(String checkMode,String modeConfig){
		
		if("Threshold".equals(checkMode)){
			return new ThresholdCheckMode("阀值",modeConfig);
		}else if("History".equals(checkMode)){
			return new HistoryCheckMode("历史同期",modeConfig);
		}else if("baseline".equals(checkMode)){
			return new BaselineCheckMode("基线对比",modeConfig);
		}else if("RecentlyRate".equals(checkMode)){
			return new RecentlyRateCheckMode("最近对比",modeConfig);
		}
		return null;
	}

}
