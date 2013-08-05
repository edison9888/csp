
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.po;

/**
 * @author xiaodu
 *
 * 下午4:47:56
 */
public class IndexHSfTable implements Comparable<IndexHSfTable> {
	
	private String ftime;
	
	private long pv;
	
	private long pvForSite;	//单个机房PV汇总
	
	private String pvRate="";
	
	private double rt;
	
	private String rtRate="";
	
	private long qps;
	
	
	private double cpu;
	
	private String cpuRate="";
	
	private double load;
	
	private String loadRate="";
	
	private double mem;
	
	private String memRate="";
	
	private int gc;
	
	private String gcRate="";

	private long tdodBlock;	
	private long ssBlock;
	private double blockRate;
	
	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public double getRt() {
		return rt;
	}

	public void setRt(double rt) {
		this.rt = rt;
	}

	public long getQps() {
		return qps;
	}

	public void setQps(long qps) {
		this.qps = qps;
	}


	public double getCpu() {
		return cpu;
	}

	public void setCpu(double cpu) {
		this.cpu = cpu;
	}

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}


	public double getMem() {
		return mem;
	}

	public void setMem(double mem) {
		this.mem = mem;
	}

	public int getGc() {
		return gc;
	}

	public void setGc(int gc) {
		this.gc = gc;
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(IndexHSfTable o) {
		
		
		return o.getFtime().compareTo(this.getFtime());
	}

	public String getPvRate() {
		return pvRate;
	}

	public void setPvRate(String pvRate) {
		this.pvRate = pvRate;
	}

	public String getRtRate() {
		return rtRate;
	}

	public void setRtRate(String rtRate) {
		this.rtRate = rtRate;
	}

	public String getCpuRate() {
		return cpuRate;
	}

	public void setCpuRate(String cpuRate) {
		this.cpuRate = cpuRate;
	}

	public String getLoadRate() {
		return loadRate;
	}

	public void setLoadRate(String loadRate) {
		this.loadRate = loadRate;
	}

	public String getMemRate() {
		return memRate;
	}

	public void setMemRate(String memRate) {
		this.memRate = memRate;
	}

	public String getGcRate() {
		return gcRate;
	}

	public void setGcRate(String gcRate) {
		this.gcRate = gcRate;
	}

	public long getPvForSite() {
		return pvForSite;
	}

	public void setPvForSite(long pvForSite) {
		this.pvForSite = pvForSite;
	}

	public long getTdodBlock() {
		return tdodBlock;
	}

	public void setTdodBlock(long tdodBlock) {
		this.tdodBlock = tdodBlock;
	}

	public long getSsBlock() {
		return ssBlock;
	}

	public void setSsBlock(long ssBlock) {
		this.ssBlock = ssBlock;
	}

	public double getBlockRate() {
		return blockRate;
	}

	public void setBlockRate(double blockRate) {
		this.blockRate = blockRate;
	}

}
