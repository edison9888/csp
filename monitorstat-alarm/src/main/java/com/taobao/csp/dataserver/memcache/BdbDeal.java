package com.taobao.csp.dataserver.memcache;

/**
 * 数据的处理逻辑
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-11-16
 */
public abstract class BdbDeal{

	public static BdbDeal DEAL_NOTHING=new BdbDeal() {
		
		@Override
		public void doValue(byte[] vdata) {
		}
	};
	
	public abstract void doValue(byte[] vdata);

}
