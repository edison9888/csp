/**
 * 
 */
package com.taobao.jprof;



/**
 * ����ֻ�����ռ�����������ݣ�
 * ���ݶ��󲢲����Ϸ��͸���������ThreadLocal�Լ����У�
 * ������Endʱ���ٸ� ������
 * 
 * 
 * @author luqi
 * @author xiaodu �޸�����һ��long ������������Ϣ
 * 
 */
public class TestJProfProfiler {
	private final static int size = 65535;
	//2141
	public transient static ThreadProfilerData[] mThreadProfiler = new ThreadProfilerData[size];	
	public static void Start(int methodId) {	
			
		long id = Thread.currentThread().getId();//		31
		if(id>size){
			return ;
		}
		ThreadProfilerData thrData = mThreadProfiler[(int)id];//31
		if(thrData==null){
			thrData = new ThreadProfilerData();
			mThreadProfiler[(int)id] = thrData;
		}
		
		long[] mProfileData = new long[3];
		mProfileData[0] = methodId;
		mProfileData[1] = System.nanoTime();
		mProfileData[2] = thrData.mStackNum;
		//thrData.mStacks.push(mProfileData);
		thrData.mStackNum++;
	}
	
	public  static void End(int methodId) {			
			
//		long threadId = Thread.currentThread().getId();
//		if(threadId>size){
//			return ;
//		}
//		ThreadProfilerData thrData = mThreadProfiler[(int)threadId];
//		if(thrData==null){
//			return ;
//		}		
//		
//		if(thrData.mProfileData.size()>10000){
//			thrData.mStacks.pop();
//			return ;
//		}
//		
//		if (thrData.mStackNum <= 0) {
//			// �����mstack��== 0�����п�������Ϊ�첽ֹͣ���µģ����Ժ���			
//			return;
//		}
//		thrData.mStackNum--;
//
//		long[] profileData = thrData.mStacks.pop();
//				
//		long id = profileData[0];
//		if(methodId!=id){
//			thrData.mStacks.pop();
//			return;
//		}
//		
//		
//		long startTime = profileData[1];
//		long stackNum = profileData[2];
//		long endtime =  System.nanoTime();
//		long useTime = endtime-startTime;
//		
//		if (useTime > 500000)
//		{
//			long result = LongCompress.decode(useTime, methodId, stackNum);	
//			thrData.mProfileData.push(result);
//		}else{
//			long result = LongCompress.decode(useTime, methodId, stackNum);	
//			thrData.mProfileData.push(result);
//		}
	}
	
	
	
	public static void main(String[] args){
		
		long t = System.currentTimeMillis();
		for(int i=0;i<10000000;i++){
			TestJProfProfiler.Start(1);
			TestJProfProfiler.End(1);
		}
		System.out.println(System.currentTimeMillis()-t);
		
	}
	
}
