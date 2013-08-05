
package com.taobao.monitor.alarm.n.key.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author xiaodu
 * @version 2011-2-27 ÏÂÎç08:40:45
 */
public class JudgeFactory {
	
	
	private  Map<Integer, Judge> map = new ConcurrentHashMap<Integer, Judge>();
	
	
	private  Judge getJudge(Integer type){
		if(map.get(type)==null){
			synchronized (map) {
				if(map.get(type)==null){
					Judge j= judge(type);
					map.put(type, j);
					
				}
			}
		}
		return map.get(type);
	}
	
	public  Judge createJudge(int type){
		
		return getJudge(type);
	}
	
	
	private  Judge judge(int type){
		
		switch (type) {
		case 1:return new ThresholdJudge();
		case 2:return new WaveJudge();	
		case 3:return new AutoJudge();
		case 4:return new ConstantJudge();
		default:return null;
		}
		
	}

}
