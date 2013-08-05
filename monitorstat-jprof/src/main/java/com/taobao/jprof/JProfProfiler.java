/**
 * 
 */
package com.taobao.jprof;

/**
 * ����ֻ�����ռ�����������ݣ� ���ݶ��󲢲����Ϸ��͸���������ThreadLocal�Լ����У� ������Endʱ���ٸ� ������
 * 
 * 
 * @author luqi
 * @author xiaodu �޸�����һ��long ������������Ϣ
 * 
 */
public class JProfProfiler {
	private final static int size = 65535;

	public transient static ThreadProfilerData[] mThreadProfiler = new ThreadProfilerData[size];

	public static void Start(int methodId) {
		if (!JProfManager.get().canProfile() || JProfManager.get().getPauseProfile() || JProfManager.get().getSleepProfile()) {
			return;
		}
		long id = Thread.currentThread().getId();
		if (id > size) {
			return;
		}
		try {
			ThreadProfilerData thrData = mThreadProfiler[(int) id];
			if (thrData == null) {
				thrData = new ThreadProfilerData();
				mThreadProfiler[(int) id] = thrData;
			}

			long[] mProfileData = new long[3];
			mProfileData[0] = methodId;
			if (JProfManager.get().getNeedNanoTime()) {
				mProfileData[1] = System.nanoTime();
			} else {
				mProfileData[1] = System.currentTimeMillis();
			}
			mProfileData[2] = thrData.mStackNum;
			thrData.mStacks.push(mProfileData);
			thrData.mStackNum++;
		} catch (Exception e) {
		}
	}

	public static void End(int methodId) {
		if (!JProfManager.get().canProfile() || JProfManager.get().getPauseProfile() || JProfManager.get().getSleepProfile()) {
			return;
		}
		long threadId = Thread.currentThread().getId();
		if (threadId > size) {
			return;
		}
		ThreadProfilerData thrData = mThreadProfiler[(int) threadId];
		if (thrData == null) {
			return;
		}

		try {
			if (thrData.mProfileData.size() > 20000) {
				thrData.mStackNum--;
				thrData.mStacks.pop();
				return;
			}

			if (thrData.mStackNum <= 0) {
				// �����mstack��== 0�����п�������Ϊ�첽ֹͣ���µģ����Ժ���
				return;
			}
			thrData.mStackNum--;

			long[] profileData = thrData.mStacks.pop();

			long id = profileData[0];
			if (methodId != id) {
				thrData.mStacks.pop();// why?
				return;
			}

			long startTime = profileData[1];
			long stackNum = profileData[2];
			long endtime = 0;

			if (JProfManager.get().getNeedNanoTime()) {
				endtime = System.nanoTime();
				long useTime = endtime - startTime;
				if (useTime > 500000) {
					long result = LongCompress.decode(useTime, methodId, stackNum);
					thrData.mProfileData.push(result);
				} else {
					if (JProfManager.get().getNeedAllStack().get()) {
						long result = LongCompress.decode(useTime, methodId, stackNum);
						thrData.mProfileData.push(result);
					}
				}
			} else {
				endtime = System.currentTimeMillis();
				long useTime = endtime - startTime;
				if (useTime > 2) {
					long result = LongCompress.decode(useTime, methodId, stackNum);
					thrData.mProfileData.push(result);
				} else {
					if (JProfManager.get().getNeedAllStack().get()) {
						long result = LongCompress.decode(useTime, methodId, stackNum);
						thrData.mProfileData.push(result);
					}
				}
			}
		} catch (Exception e) {
		}
	}
}
