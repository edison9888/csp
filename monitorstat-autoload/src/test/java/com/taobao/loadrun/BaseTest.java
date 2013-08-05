
package com.taobao.loadrun;

import java.util.HashSet;
import java.util.Set;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;

/**
 * 
 * @author xiaodu
 * @version 2011-6-28 ÏÂÎç01:20:13
 */
public class BaseTest {
	
	public LoadrunTarget target = new LoadrunTarget();
	
	{
		target.setAppId(1);
		target.setAppName("item");
		target.setTargetIp("10.232.21.198");
		target.setTargetUserName("youji.zj");
		target.setTargetPassword("zhangjun11");
		target.setLimitFeature("CPU:40;Load:1;");
		target.setLoadrunType(AutoLoadType.apache);
		target.setConfigFeature("2_4");
		
		
		Set<String> classSet = new HashSet<String>();
		classSet.add("com.taobao.csp.loadrun.core.fetch.ApacheFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.TomcatFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.CpuFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.GcFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.JvmFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.LoadFetchTaskImpl");
		target.setFetchClasses(classSet);
		
		
		
		
		
	}
	
}
