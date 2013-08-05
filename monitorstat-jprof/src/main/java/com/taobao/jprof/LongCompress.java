
package com.taobao.jprof;
/**
 * 
 * @author xiaodu
 * @version 2010-7-8 上午11:27:33
 */
public class LongCompress {
	
	/**
	 * 将传入的 三个long 型数据 合并到 一个long中
	 * 低2byte 为time 然后为methodId stackum 4byte+2byte+2byte 方式保存
	 * @param time
	 * @param methodId
	 * @param stackum
	 * @return
	 */
	public static long decode(long time,long methodId,long stackum){		
		long tmp = (time&0xffffffffL)|(((methodId<<32)&0xffff00000000L))|(((stackum<<48)&0xffff000000000000l));
		return tmp;
	}		
	
	/**
	 * 取得时间
	 * @param v
	 * @return
	 */
	public static long getTime(long v){		
		return (v&0xffffffffl);		
	}
	/**
	 * 取得方法ID
	 * @param v
	 * @return
	 */
	public static long getMethodId(long v){
		return (v>>32)&0xffffl;
	}
	/**
	 * 取得stack num
	 * @param v
	 * @return
	 */
	public static long getStackNum(long v){
		return (v>>48)&0xffffl;
	}
	/**
	 * 更新时间
	 * @param v
	 * @param time
	 * @return
	 */
	public static long updateTime(long v,long time){
		return (v&0xffffffff00000000L)|time;	
	}
	
}
