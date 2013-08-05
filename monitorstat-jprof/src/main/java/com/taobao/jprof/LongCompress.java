
package com.taobao.jprof;
/**
 * 
 * @author xiaodu
 * @version 2010-7-8 ����11:27:33
 */
public class LongCompress {
	
	/**
	 * ������� ����long ������ �ϲ��� һ��long��
	 * ��2byte Ϊtime Ȼ��ΪmethodId stackum 4byte+2byte+2byte ��ʽ����
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
	 * ȡ��ʱ��
	 * @param v
	 * @return
	 */
	public static long getTime(long v){		
		return (v&0xffffffffl);		
	}
	/**
	 * ȡ�÷���ID
	 * @param v
	 * @return
	 */
	public static long getMethodId(long v){
		return (v>>32)&0xffffl;
	}
	/**
	 * ȡ��stack num
	 * @param v
	 * @return
	 */
	public static long getStackNum(long v){
		return (v>>48)&0xffffl;
	}
	/**
	 * ����ʱ��
	 * @param v
	 * @param time
	 * @return
	 */
	public static long updateTime(long v,long time){
		return (v&0xffffffff00000000L)|time;	
	}
	
}
