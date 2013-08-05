
package com.taobao.jprof;


/**
 * 
 * @author xiaodu
 * @version 2010-6-22 обнГ02:03:47
 */
public class ThreadProfilerData {
	
	
	public JprofStack<Long> mProfileData = new JprofStack<Long>();
	public int mStackNum = 0;
	public JprofStack<long[]> mStacks = new JprofStack<long[]>();
		
	public void clear(){
		mProfileData.clear();
		mStackNum = 0;
		mStacks.clear();
	}
	
}
