package com.taobao.csp.prevent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class PreventHelper {
	
	private static String TASK_ID_FILE = "/tmp/depID";
	/**
	 *  获取阻止任务ID
	 * @return
	 */
	public static TaskDetail getPreventTaskDetail(){
		File file = new File(TASK_ID_FILE);
		if(file.exists()&&file.isFile()){
			try {
				StringBuilder sb = new StringBuilder();
				String line = null;
				BufferedReader reader = new BufferedReader(new FileReader(file));
				while((line=reader.readLine())!=null){
					sb.append(line);
				}
				if(sb.length()>0){
					String[] tmp = sb.toString().split(",");
					if(tmp.length == 3){
						TaskDetail detail = new TaskDetail();
						detail.setRemoteAddr(tmp[0]);
						detail.setRemotePort(tmp[1]);
						detail.setTaskId(tmp[2]);
						detail.setInjectType(tmp[3]);
						return detail;
					}
				}
				return null;
			} catch (Exception e) {
			}
		}
		return null;
	}
}
