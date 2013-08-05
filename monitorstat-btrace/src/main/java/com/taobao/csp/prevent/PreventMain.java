package com.taobao.csp.prevent;

import java.lang.instrument.Instrumentation;
import java.util.List;

public class PreventMain {
	
	
	public static void premain(String args, Instrumentation inst) {
		System.out.println("Æô¶¯ PreventMain");
		TaskDetail taskDetail = PreventHelper.getPreventTaskDetail();
		if(taskDetail != null){
			
			List<ClassDetail> list = SimpleIo.queryPreventClass(taskDetail.getRemoteAddr(), taskDetail.getRemotePort(),taskDetail.getTaskId());
			if(list.size() >0){
				inst.addTransformer(new PreventTransformer(list,taskDetail.getInjectType()), true);
			}
		}
	}

}
