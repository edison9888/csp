
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.taobao.csp.dataserver.io.DataConnect;

/**
 * @author xiaodu
 *
 * ÏÂÎç5:13:48
 */
public class MonitorDeadRequest {
	
	private Timer timeschedule = new Timer();
	
	private List<DataConnect> list = Collections.synchronizedList(new ArrayList<DataConnect>());
	
	public MonitorDeadRequest(){
		
		timeschedule.schedule(new TimerTask() {
			@Override
			public void run() {
				Iterator<DataConnect> it = list.iterator();
				while(it.hasNext()){
					it.next().scanDeadRequest();
				}
			}
		}, 60000, 600000);
		
	}
	
	
	public void addConnect(DataConnect conn){
		list.add(conn);
	}
	
	public void removeConnect(DataConnect conn){
		list.remove(conn);
	}
	

}
