
package com.taobao.jprof;

import java.util.Stack;

/**
 * 
 * @author xiaodu
 * @version 2010-6-24 ÉÏÎç10:45:40
 */
public class TestJprof {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			ThreadProfilerData[] mThreadProfiler = new ThreadProfilerData[2];
			
			
			
			mThreadProfiler[0 ]= new ThreadProfilerData();
			
			mThreadProfiler[0].mProfileData.push(1111111l);
			mThreadProfiler[0].mProfileData.push(22222222l);
			mThreadProfiler[0].mProfileData.push(33333333l);
			mThreadProfiler[0].mProfileData.push(44444444l);
			long size = 0;
			for(int index=0;index<mThreadProfiler.length;index++){
				ThreadProfilerData profilerData = mThreadProfiler[index];
				if(profilerData==null){
					continue;
				}
				JprofStack<Long> stack = profilerData.mProfileData;
				long data = -1;						
				StringBuilder sb = new StringBuilder();						
				while(stack.size()>0){	//methodid starttime endtime threadid stacknum
					 data=stack.pop();
				}
				
				profilerData.clear();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
