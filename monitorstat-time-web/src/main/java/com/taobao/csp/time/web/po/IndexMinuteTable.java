package com.taobao.csp.time.web.po;

/**
 * 按分钟间隔统计数据 
 */
public class IndexMinuteTable {
	private String ftime;
	
	//集群总PV(单位时间内)
	private long pv;	//or hsf call
	private long pvPre;
	private String pvRate;
	
	private long pv200;
	private long pv200Pre;
	private String pv200Rate;
	
	//集群总响应时间
	private long rt;
	private long rtPre;
	private String rtRate;
	
//	//集群的QPS
//	private long qps;
//	private long qpsPre;
//	private String qpsRate;
	
	//单机的QPS
	private long hostQps;
	private long hostQpsPre;
	private String hostQpsRate;
	
	private float cpu;
	private float cpuPre;
	private String cpuRate;

	private double load;	
	private double loadPre;
	private String loadRate;
	
	private int gc;
	private int gcPre;
	private String gcRate;
	
	private float memory;
	private float memoryPre;
	private String memoryRate;
	
	public long getPv() {
		return pv;
	}



	public void setPv(long pv) {
		this.pv = pv;
	}



	public String getPvRate() {
		return pvRate;
	}



	public void setPvRate(String pvRate) {
		this.pvRate = pvRate;
	}



	public long getRt() {
		return rt;
	}



	public void setRt(long rt) {
		this.rt = rt;
	}



	public String getRtRate() {
		return rtRate;
	}



	public void setRtRate(String rtRate) {
		this.rtRate = rtRate;
	}



	public float getCpu() {
		return cpu;
	}



	public void setCpu(float cpu) {
		this.cpu = cpu;
	}



	public String getCpuRate() {
		return cpuRate;
	}



	public void setCpuRate(String cpuRate) {
		this.cpuRate = cpuRate;
	}



	public int getGc() {
		return gc;
	}



	public void setGc(int gc) {
		this.gc = gc;
	}



	public String getGcRate() {
		return gcRate;
	}



	public void setGcRate(String gcRate) {
		this.gcRate = gcRate;
	}



	public double getLoad() {
		return load;
	}



	public void setLoad(double load) {
		this.load = load;
	}



	public String getLoadRate() {
		return loadRate;
	}



	public void setLoadRate(String loadRate) {
		this.loadRate = loadRate;
	}

	public long getPvPre() {
		return pvPre;
	}



	public void setPvPre(long pvPre) {
		this.pvPre = pvPre;
	}



	public long getRtPre() {
		return rtPre;
	}





	public float getCpuPre() {
		return cpuPre;
	}



	public void setCpuPre(float cpuPre) {
		this.cpuPre = cpuPre;
	}



	public int getGcPre() {
		return gcPre;
	}



	public void setGcPre(int gcPre) {
		this.gcPre = gcPre;
	}



	public double getLoadPre() {
		return loadPre;
	}



	public void setLoadPre(double loadPre) {
		this.loadPre = loadPre;
	}



	public void setLoadPre(long loadPre) {
		this.loadPre = loadPre;
	}



	public float getMemory() {
		return memory;
	}



	public void setMemory(float memory) {
		this.memory = memory;
	}



	public float getMemoryPre() {
		return memoryPre;
	}



	public void setMemoryPre(float memoryPre) {
		this.memoryPre = memoryPre;
	}

	public long getPv200() {
		return pv200;
	}



	public void setPv200(long pv200) {
		this.pv200 = pv200;
	}



	public long getPv200Pre() {
		return pv200Pre;
	}



	public void setPv200Pre(long pv200Pre) {
		this.pv200Pre = pv200Pre;
	}


	public String getPv200Rate() {
		return pv200Rate;
	}

	public void setPv200Rate(String pv200Rate) {
		this.pv200Rate = pv200Rate;
	}

	public String getMemoryRate() {
		return memoryRate;
	}

	public void setMemoryRate(String memoryRate) {
		this.memoryRate = memoryRate;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}


//	public String getQpsRate() {
//		return qpsRate;
//	}
//
//	public void setQpsRate(String qpsRate) {
//		this.qpsRate = qpsRate;
//	}
//	public long getQps() {
//		return qps;
//	}
//
//
//
//	public void setQps(long qps) {
//		this.qps = qps;
//	}
//
//
//
//	public long getQpsPre() {
//		return qpsPre;
//	}
//
//
//
//	public void setQpsPre(long qpsPre) {
//		this.qpsPre = qpsPre;
//	}

	public long getHostQps() {
		return hostQps;
	}
	
	public void setHostQps(long hostQps) {
		this.hostQps = hostQps;
	}



	public long getHostQpsPre() {
		return hostQpsPre;
	}



	public void setHostQpsPre(long hostQpsPre) {
		this.hostQpsPre = hostQpsPre;
	}



	public String getHostQpsRate() {
		return hostQpsRate;
	}



	public void setHostQpsRate(String hostQpsRate) {
		this.hostQpsRate = hostQpsRate;
	}



	public void setRtPre(long rtPre) {
		this.rtPre = rtPre;
	}
}
