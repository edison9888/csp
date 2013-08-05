package com.taobao.jprof;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 
 * @author xiaodu
 * @version 2010-6-22 上午09:22:30
 */
public class JprofConfig {

	private static final String CustomConfigFile = "/home/admin/jprof/jprof.properties";

	private boolean debugJprof = false;

	private String startJprofTime;

	private String endJprofTime;

	private String logFilePath;

	private String includePackageStartsWith;

	private String excludePackageStartsWith;

	private int eachJprofUseTime = -1;

	private int eachJprofIntervalTime = -1;

	private int delayTime = -1;

	private boolean needPrintAllStack;

	private boolean needNanoTime;

	private boolean ignoreGetSetMethod;

	public JprofConfig() {
		File file = new File(CustomConfigFile);
		if (file.exists()) {
			parseProperty(file);
		} else {
			// 使用默认配置
			parse("jprof");
		}
	}

	private void parseProperty(File path) {

		Properties resource = new Properties();
		try {
			resource.load(new FileReader(path));
			String startJprofTime = resource.getProperty("startJprofTime");
			String endJprofTime = resource.getProperty("endJprofTime");
			String logFilePath = resource.getProperty("logFilePath");
			String includePackageStartsWith = resource.getProperty("includePackageStartsWith");
			String debugJprof = resource.getProperty("debugJprof");
			String eachJprofUseTime = resource.getProperty("eachJprofUseTime");
			String eachJprofIntervalTime = resource.getProperty("eachJprofIntervalTime");
			String excludePackageStartsWith = resource.getProperty("excludePackageStartsWith");
			String delayTime = resource.getProperty("delayTime");
			String needPrintAllStack = resource.getProperty("needPrintAllStack");
			String needNanoTime = resource.getProperty("needNanoTime");
			String ignoreGetSetMethod = resource.getProperty("ignoreGetSetMethod");
			this.setExcludePackageStartsWith(excludePackageStartsWith);
			this.setEndJprofTime(endJprofTime);
			this.setIncludePackageStartsWith(includePackageStartsWith);
			this.setLogFilePath(logFilePath);
			this.setStartJprofTime(startJprofTime);
			this.setDebugJprof("true".equals(debugJprof));
			this.setNeedPrintAllStack("true".equals(needPrintAllStack));
			this.setNeedNanoTime("true".equals(needNanoTime));
			this.setIgnoreGetSetMethod("true".equals(ignoreGetSetMethod));
			if (delayTime != null) {
				this.setDelayTime(Integer.valueOf(delayTime.trim()));
			}
			if (eachJprofUseTime == null) {
				this.setEachJprofUseTime(1);
			} else {
				this.setEachJprofUseTime(Integer.valueOf(eachJprofUseTime.trim()));
			}
			if (eachJprofIntervalTime == null)
				this.setEachJprofIntervalTime(2);
			else
				this.setEachJprofIntervalTime(Integer.valueOf(eachJprofIntervalTime.trim()));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parse(String configName) {
		try {
			ResourceBundle resource = ResourceBundle.getBundle(configName);
			String startJprofTime = resource.getString("startJprofTime");
			String endJprofTime = resource.getString("endJprofTime");
			String logFilePath = resource.getString("logFilePath");
			String includePackageStartsWith = resource.getString("includePackageStartsWith");
			String debugJprof = resource.getString("debugJprof");
			String eachJprofUseTime = resource.getString("eachJprofUseTime");
			String eachJprofIntervalTime = resource.getString("eachJprofIntervalTime");
			String excludePackageStartsWith = resource.getString("excludePackageStartsWith");
			String delayTime = resource.getString("delayTime");
			String needPrintAllStack = resource.getString("needPrintAllStack");
			String needNanoTime = resource.getString("needNanoTime");
			String ignoreGetSetMethod = resource.getString("ignoreGetSetMethod");
			this.setExcludePackageStartsWith(excludePackageStartsWith);
			this.setEndJprofTime(endJprofTime);
			this.setIncludePackageStartsWith(includePackageStartsWith);
			this.setLogFilePath(logFilePath);
			this.setStartJprofTime(startJprofTime);
			this.setDebugJprof("true".equals(debugJprof));
			this.setNeedPrintAllStack("true".equals(needPrintAllStack));
			this.setNeedNanoTime("true".equals(needNanoTime));
			this.setIgnoreGetSetMethod("true".equals(ignoreGetSetMethod));
			if (delayTime != null) {
				this.setDelayTime(Integer.valueOf(delayTime.trim()));
			}
			if (eachJprofUseTime == null) {
				this.setEachJprofUseTime(1);
			} else {
				this.setEachJprofUseTime(Integer.valueOf(eachJprofUseTime.trim()));
			}
			if (eachJprofIntervalTime == null)
				this.setEachJprofIntervalTime(2);
			else
				this.setEachJprofIntervalTime(Integer.valueOf(eachJprofIntervalTime.trim()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getStartJprofTime() {
		return startJprofTime;
	}

	public void setStartJprofTime(String startJprofTime) {
		this.startJprofTime = startJprofTime;
	}

	public String getEndJprofTime() {
		return endJprofTime;
	}

	public void setEndJprofTime(String endJprofTime) {
		this.endJprofTime = endJprofTime;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public String getIncludePackageStartsWith() {
		return includePackageStartsWith;
	}

	public void setIncludePackageStartsWith(String includePackageStartsWith) {
		this.includePackageStartsWith = includePackageStartsWith;
	}

	public boolean isDebugJprof() {
		return debugJprof;
	}

	public void setDebugJprof(boolean debugJprof) {
		this.debugJprof = debugJprof;
	}

	public int getEachJprofUseTime() {
		return eachJprofUseTime;
	}

	public void setEachJprofUseTime(int eachJprofUseTime) {
		this.eachJprofUseTime = eachJprofUseTime;
	}

	public int getEachJprofIntervalTime() {
		return eachJprofIntervalTime;
	}

	public void setEachJprofIntervalTime(int eachJprofIntervalTime) {
		this.eachJprofIntervalTime = eachJprofIntervalTime;
	}

	public String getExcludePackageStartsWith() {
		return excludePackageStartsWith;
	}

	public void setExcludePackageStartsWith(String excludePackageStartsWith) {
		this.excludePackageStartsWith = excludePackageStartsWith;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public boolean isNeedPrintAllStack() {
		return needPrintAllStack;
	}

	public void setNeedPrintAllStack(boolean needPrintAllStack) {
		this.needPrintAllStack = needPrintAllStack;
	}

	public boolean isNeedNanoTime() {
		return needNanoTime;
	}

	public void setNeedNanoTime(boolean needNanoTime) {
		this.needNanoTime = needNanoTime;
	}

	public boolean isIgnoreGetSetMethod() {
		return ignoreGetSetMethod;
	}

	public void setIgnoreGetSetMethod(boolean ignoreGetSetMethod) {
		this.ignoreGetSetMethod = ignoreGetSetMethod;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("debugJprof").append(":").append(debugJprof).append("]");
		sb.append("[").append("startJprofTime").append(":").append(startJprofTime).append("]");
		sb.append("[").append("endJprofTime").append(":").append(endJprofTime).append("]");
		sb.append("[").append("logFilePath").append(":").append(logFilePath).append("]");
		sb.append("[").append("includePackageStartsWith").append(":").append(includePackageStartsWith).append("]");
		sb.append("[").append("eachJprofUseTime").append(":").append(eachJprofUseTime).append("]");
		sb.append("[").append("eachJprofIntervalTime").append(":").append(eachJprofIntervalTime).append("]");
		sb.append("[").append("delayTime").append(":").append(delayTime).append("]");
		sb.append("[").append("needPrintAllStack").append(":").append(needPrintAllStack).append("]");
		sb.append("[").append("needNanoTime").append(":").append(needNanoTime).append("]");
		sb.append("[").append("ignoreGetSetMethod").append(":").append(ignoreGetSetMethod).append("]");
		return sb.toString();
	}

}
