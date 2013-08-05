
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.core;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.Arith;

/**
 * 
 * 作为内存需要回收的一个钩子
 * 
 * @author xiaodu
 *
 * 下午4:58:39
 */
public class MemoryTrigger {
	
	private static final Logger logger = Logger.getLogger(MemoryTrigger.class);
	
	
	private ReferenceQueue<ItemObj> refQueue = new ReferenceQueue<ItemObj>();
	
	@SuppressWarnings("unused")
	private TriggerObject reference = new TriggerObject(new ItemObj(), refQueue);
	
	private MemoryHook hook;
	
	public MemoryTrigger(MemoryHook hook){
		this.hook = hook;
	}
	
	
	public void isTrigger(){

		TriggerObject trigger = (TriggerObject)refQueue.poll();
		if(trigger != null){
			
			
			long free = Runtime.getRuntime().freeMemory();
			long max = Runtime.getRuntime().maxMemory();
			long total = Runtime.getRuntime().totalMemory();
			
			logger.info("内存回收被激活 free:"+free+" max:"+max+" total:"+total);
			
			if(Arith.div(free, max, 2) <0.2){
				hook.freeMemory();
				logger.info("执行内存回收："+free+":"+max);
			}
			reference = new TriggerObject(new ItemObj(), refQueue);
		}
				
	}
	

	
	private class ItemObj {

			private Object[] values = new Object[10];
			
			@SuppressWarnings("unused")
			public Object[] getValues(){
				return values;
			}
		
	}
	
	
	
	private class TriggerObject extends SoftReference<ItemObj>{
		/**
		 * @param referent
		 * @param q
		 */
		public TriggerObject(ItemObj referent, ReferenceQueue<ItemObj> q) {
			super(referent, q);
		}
	}

}
