
package com.taobao.csp.loadrun.core.run;

import java.lang.reflect.Constructor;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;

/**
 * 
 * @author xiaodu
 * @version 2011-6-23 обнГ04:16:58
 */
public class LoadrunFactory {
	
	
	public static BaseLoadrunTask createLoadrun(LoadrunTarget target) throws Exception{
		
		AutoLoadType type = target.getLoadrunType();
		
		if(type != null){
			Class<? extends BaseLoadrunTask> task = type.getLoadClass();
			Constructor<? extends BaseLoadrunTask> c = task.getDeclaredConstructor(LoadrunTarget.class);
			BaseLoadrunTask obj = c.newInstance(target);
			return obj;
		}
		return null;
	}

}
