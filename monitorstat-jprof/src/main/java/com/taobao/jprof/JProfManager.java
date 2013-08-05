package com.taobao.jprof;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiaodu
 * 
 */
public class JProfManager {
	/*
	 * 配置文件信息
	 */
	private JprofConfig jprofConfig = null;
	/*
	 * 是否可以进行Profiler,
	 */
	private AtomicBoolean mCanProfile = new AtomicBoolean(false);

	private AtomicBoolean pauseProfile = new AtomicBoolean(false);

	private AtomicBoolean sleepProfile = new AtomicBoolean(false);

	/*
	 * 测试状态 对 时间设置无效
	 */
	private AtomicBoolean debugJprof = new AtomicBoolean(false);
	/*
	 * 是否打印所有栈
	 */
	private AtomicBoolean needAllStack = new AtomicBoolean(false);

	private AtomicBoolean needNanoTime = new AtomicBoolean(false);

	private boolean ignoreGetSetMethod = true;
	/*
	 * JProfManager 实例
	 */
	private static JProfManager jProfManager = new JProfManager();

	private Object lock = new Object();

	private JProfManager() {
		jprofConfig = new JprofConfig();
		if (jprofConfig == null) {
			setCanProfile(false);// 无配置，不prof
			setPauseProfile(false);
		} else {
			debugJprof.set(jprofConfig.isDebugJprof());
			needNanoTime.set(jprofConfig.isNeedNanoTime());
			this.setIgnoreGetSetMethod(jprofConfig.isIgnoreGetSetMethod());
			setJprofFilter(jprofConfig);
			startupInnerControlThread(jprofConfig);
		}
	}

	public static JProfManager get() {
		return jProfManager;
	}

	public JprofConfig getJprofConfig() {
		return jprofConfig;
	}

	public AtomicBoolean getNeedAllStack() {
		return needAllStack;
	}

	public void setNeedAllStack(AtomicBoolean needAllStack) {
		this.needAllStack = needAllStack;
	}

	public boolean isIgnoreGetSetMethod() {
		return ignoreGetSetMethod;
	}

	public void setIgnoreGetSetMethod(boolean ignoreGetSetMethod) {
		this.ignoreGetSetMethod = ignoreGetSetMethod;
	}

	public boolean getNeedNanoTime() {
		return needNanoTime.get();
	}

	/**
	 * 设置需要注入的 package
	 * 
	 * @param jprofConfig
	 */
	private void setJprofFilter(JprofConfig jprofConfig) {
		String include = jprofConfig.getIncludePackageStartsWith();
		if (include != null) {
			String[] _includes = include.split(";");
			for (String pack : _includes) {
				JProfFilter.addIncludeClass(pack);
			}
		}
		String exclude = jprofConfig.getExcludePackageStartsWith();
		if (exclude != null) {
			String[] _excludes = exclude.split(";");
			for (String pack : _excludes) {
				JProfFilter.addExcludeClass(pack);
			}
		}

	}

	/**
	 * 启动时间控制线程,如果是debug 状态 将不启动线程并直接将jprof设置成开启状态
	 * 
	 * @param jprofConfig
	 */
	private void startupInnerControlThread(JprofConfig jprofConfig) {
		InnerControlThread thread = new InnerControlThread(jprofConfig.getStartJprofTime(),
				jprofConfig.getEndJprofTime());
		thread.setName("JProfManager-InnerControlThread");
		thread.setDaemon(true);
		thread.start();

		InnerSleep sleep = new InnerSleep(jprofConfig.getEachJprofIntervalTime(), jprofConfig.getEachJprofUseTime());
		sleep.setName("JProfManager-InnerSleep");
		sleep.setDaemon(true);
		sleep.start();
	}

	/**
	 * 设置是否jprof
	 * 
	 * @param canVal
	 *            true 做 ，false 不做
	 */
	public void setCanProfile(boolean canVal) {
		mCanProfile.set(canVal);
	}

	public boolean getSleepProfile() {
		return sleepProfile.get();
	}

	public boolean getPauseProfile() {
		return pauseProfile.get();
	}

	public void setPauseProfile(boolean pause) {
		this.pauseProfile.set(pause);
	}

	public boolean canProfile() {
		return mCanProfile.get();
	}

	private class InnerSleep extends Thread {
		private int pauseTime;
		private int runTime;
		private boolean run = true;
		private long lastTouchTime = 0;

		public InnerSleep(int p, int r) {
			this.pauseTime = p * 1000;
			this.runTime = r * 1000;
		}

		public void run() {

			if (!jprofConfig.isDebugJprof()) {
				while (true) {
					if (run) {
						synchronized (this) {
							try {
								sleepProfile.set(false);
								this.wait(runTime);
								run = false;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					} else {
						synchronized (this) {
							try {
								sleepProfile.set(true);
								JprofDump.get().flushJprofData();
								this.wait(pauseTime);
								run = true;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					checkRunStatus();
				}
			} else {
				while (true) {
					try {
						JprofDump.get().flushJprofData();// 通知可以刷出,暂停Profile并等待500ms
						Thread.sleep(5000);// 刷出数据,然后继续Profile,直到下次刷出
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private void checkRunStatus() {
			long currentTime = System.currentTimeMillis();
			// 一个小时读取一次
			if (currentTime - lastTouchTime > 3600000) {
				if (new File("/home/admin/jprof/stop").exists()) {
					pauseProfile.set(true);
				} else {
					pauseProfile.set(false);
				}
				lastTouchTime = currentTime;
			}
		}
	}

	/**
	 * 内部时间控制线程，用于监控在时间点内是否需要prof
	 * 
	 * @author xiaodu
	 * 
	 */
	private class InnerControlThread extends Thread {
		private InnerControTime startTime;
		private InnerControTime endTime;

		public InnerControlThread(String start, String end) {
			startTime = parse(start);
			endTime = parse(end);
		}

		public long waitTime(InnerControTime time) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, time.getHour());
			cal.set(Calendar.MINUTE, time.getMinute());
			cal.set(Calendar.SECOND, time.getSecond());
			long startupTime = cal.getTimeInMillis();
			long _waitTime = startupTime - System.currentTimeMillis();
			return _waitTime;
		}

		public long nextTime(InnerControTime time) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, time.getHour());
			cal.set(Calendar.MINUTE, time.getMinute());
			cal.set(Calendar.SECOND, time.getSecond());
			long startupTime = cal.getTimeInMillis();
			long _waitTime = startupTime - System.currentTimeMillis();
			return _waitTime;
		}

		private void await(long time) {
			synchronized (lock) {
				try {
					lock.wait(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {

			try {
				if (jprofConfig.getDelayTime() > 0) {
					Thread.sleep(jprofConfig.getDelayTime() * 1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!jprofConfig.isDebugJprof()) {
				if (startTime == null || endTime == null) {
					return;
				}
				while (true) {
					long time = waitTime(startTime);
					if (time > 0) {
						await(time);
					} else {
						time = waitTime(endTime);
						if (time > 0) {
							JProfManager.get().setCanProfile(true);
							await(time);
						} else {
							JProfManager.get().setCanProfile(false);
							time = nextTime(startTime);
							await(time);
						}

					}
				}
			} else {
				JProfManager.get().setCanProfile(true);
			}
		}

		public InnerControTime parse(String time) {
			if (time == null) {
				return null;
			} else {
				time = time.trim();
				String[] _time = time.split(":");
				if (_time.length == 3) {
					try {
						int hour = Integer.valueOf(_time[0]);
						int minute = Integer.valueOf(_time[1]);
						int second = Integer.valueOf(_time[2]);
						InnerControTime inner = new InnerControTime();
						inner.setHour(hour);
						inner.setMinute(minute);
						inner.setSecond(second);
						return inner;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}

		}

	}

	private class InnerControTime {
		private int hour;
		private int minute;
		private int second;

		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public int getMinute() {
			return minute;
		}

		public void setMinute(int minute) {
			this.minute = minute;
		}

		public int getSecond() {
			return second;
		}

		public void setSecond(int second) {
			this.second = second;
		}

	}
}
