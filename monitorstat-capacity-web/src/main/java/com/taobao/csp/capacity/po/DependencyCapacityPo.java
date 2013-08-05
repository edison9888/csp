package com.taobao.csp.capacity.po;

import com.taobao.monitor.common.util.Utlitites;

/***
 * 依赖容量相关信息
 * 
 * @author youji.zj
 *
 */
public class DependencyCapacityPo extends BasePo implements Cloneable {
	
	private String depApp;
	
	private long totalPv;
	
	private long depPv;
	private String formatDepPv; // for display;
	
	private double percent;
	private String formatPercent; // for display
	
	private double level;
	private String formatLevel; // for display
	
	public String getDepApp() {
		return depApp;
	}
	
	public void setDepApp(String depApp) {
		this.depApp = depApp;
	}
	
	public long getTotalPv() {
		return totalPv;
	}
	
	public void setTotalPv(long totalPv) {
		this.totalPv = totalPv;
	}
	
	public long getDepPv() {
		return depPv;
	}
	
	public void setDepPv(long depPv) {
		this.depPv = depPv;
		this.formatDepPv =  Utlitites.fromatLong(String.valueOf(depPv));
	}
	
	public String getFormatDepPv() {
		return formatDepPv;
	}
	
	public void setFormatDepPv(String formatDepPv) {
		this.formatDepPv = formatDepPv;
	}
	
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
		this.formatPercent = DF_DOUBLE.format(percent) + "%";
	}
	
	public String getFormatPercent() {
		return formatPercent;
	}
	
	public void setFormatPercent(String formatPercent) {
		this.formatPercent = formatPercent;
	}
	
	public double getLevel() {
		return level;
	}
	
	public void setLevel(double level) {
		this.level = level;
		this.formatLevel = DF_DOUBLE.format(level) + "%";
	}
	
	public String getFormatLevel() {
		return formatLevel;
	}
	
	public void setFormatLevel(String formatLevel) {
		this.formatLevel = formatLevel;
	}
	
	 public DependencyCapacityPo clone() {
	        try {
				return (DependencyCapacityPo)super.clone();
			} catch (CloneNotSupportedException e) {
				throw new IllegalStateException("DependencyCapacityPo clone error");
			}
	 }
	
}
