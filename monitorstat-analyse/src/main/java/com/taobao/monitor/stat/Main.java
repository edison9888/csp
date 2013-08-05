
package com.taobao.monitor.stat;


import com.taobao.monitor.stat.schedule.ScheduleControl;

/**
 * 
 * @author xiaodu
 * @version 2010-4-6 ÉÏÎç09:20:59
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		if(args.length>0){
			String type = args[0];
			if("g".equals(type)){
				ScheduleControl.startSchedule(true);
			}
		}
		else
			ScheduleControl.startSchedule(false);
		
	}

}
