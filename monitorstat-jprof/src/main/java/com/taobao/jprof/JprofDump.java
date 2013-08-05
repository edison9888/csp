package com.taobao.jprof;

/**
 * 
 * @author xiaodu
 * @version 2010-6-22 上午11:51:24
 */
public class JprofDump {

	private static JprofDump writer = new JprofDump();

	public static JprofDump get() {
		return writer;
	}

	private String logFilePath = "jprof.txt";

	private boolean dumpJprofData = false;

	private InnerDumpThread dump = null;

	private Object lock = new Object();

	private JprofDump() {
		dump = new InnerDumpThread();
		dump.setName("JprofDump-InnerDumpThread");
		dump.start();
	}

	public void setLogFilePath(String path) {
		this.logFilePath = path;
	}

	public String getLogFilePath() {

		JprofConfig config = JProfManager.get().getJprofConfig();
		if (config != null && config.getLogFilePath() != null) {
			return config.getLogFilePath();
		}
		return logFilePath;
	}

	/**
	 * 将缓存中的数据全部写到日志中
	 * 
	 * 
	 */
	public void flushJprofData() {

		if (!dumpJprofData) {
			synchronized (lock) {
				if (!dumpJprofData) {
					JProfManager.get().setPauseProfile(true);
					dumpJprofData = true;
					try {
						lock.wait(500); // 此作用是等PauseProfile完全起效
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					lock.notifyAll();
				}
			}
		}
	}

	/**
	 * dump 线程
	 * 
	 * @author xiaodu
	 * 
	 */
	private class InnerDumpThread extends Thread {
		private DailyRollingFileWriter fileWriter = new DailyRollingFileWriter(JprofDump.this.getLogFilePath());

		public void run() {
			if (fileWriter == null) {
				return;
			}
			boolean nano = JProfManager.get().getNeedNanoTime();
			fileWriter.append("##nano:" + nano + "\n");

			while (true) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				try {
					ThreadProfilerData[] mThreadProfiler = JProfProfiler.mThreadProfiler;

					for (int index = 0; index < mThreadProfiler.length; index++) {
						ThreadProfilerData profilerData = mThreadProfiler[index];
						if (profilerData == null) {
							continue;
						}
						JprofStack<Long> stack = profilerData.mProfileData;
						long data = -1;
						while (stack.size() > 0) {
							StringBuilder sb = new StringBuilder();
							data = stack.pop();
							long useTime = LongCompress.getTime(data);
							long methodid = LongCompress.getMethodId(data);
							long stacknum = LongCompress.getStackNum(data);

							JprofMethodInfo method = JProfMethodCache.getJprofMethodInfo((int) methodid);
							for (int i = 0; i < stacknum; i++) {
								sb.append("-");
							}
							sb.append(method.getMClassName());
							sb.append(":");
							sb.append(method.getMMethodName());
							sb.append(":");
							sb.append((method.getMLineNum()));
							sb.append('\t');
							sb.append(useTime);
							sb.append('\t');
							sb.append(index);
							sb.append('\t');
							sb.append(stacknum);
							sb.append('\n');
							fileWriter.append(sb.toString());
						}

						fileWriter.flushAppend();
						profilerData.clear();
					}
				} catch (Exception e) {
				}

				if (dumpJprofData) {
					dumpJprofData = false;
					JProfManager.get().setPauseProfile(false);
				}
			}

		}

	}

}
