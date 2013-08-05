package com.taobao.monitor.stat.db.po;

/**
 * xiaoxie 2010-4-28
 */
public class ShareReportPo {
	private long createOrderCount;
	private long payOrderCount;

	public long getCreateOrderCount() {
		return createOrderCount;
	}

	public void setCreateOrderCount(long createOrderCount) {
		this.createOrderCount = createOrderCount;
	}

	public long getPayOrderCount() {
		return payOrderCount;
	}

	public void setPayOrderCount(long payOrderCount) {
		this.payOrderCount = payOrderCount;
	}
}
