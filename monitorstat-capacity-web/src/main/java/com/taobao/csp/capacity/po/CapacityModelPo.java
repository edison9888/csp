package com.taobao.csp.capacity.po;

/**
 * 
 * 功能:容量预测模型PO类。
 * 
 * @author wb-tangjinge E-mail:wb-tangjinge@taobao.com
 * @version 1.0
 * @since 2013-1-22 下午4:40:04
 */

public class CapacityModelPo implements Comparable<CapacityModelPo> {

	private String id;

	private String resFrom;

	private String resTo;

	private double relRatio;

	public CapacityModelPo() {
		super();
	}

	public CapacityModelPo(String id, String resFrom, String resTo, double relRatio) {
		super();
		this.id = id;
		this.resFrom = resFrom;
		this.resTo = resTo;
		this.relRatio = relRatio;
	}

	@Override
	public int compareTo(CapacityModelPo o) {

		if (this.relRatio > o.getRelRatio()) {
			return -1;
		} else if (this.relRatio < o.getRelRatio()) {
			return 1;
		}
		return 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResFrom() {
		return resFrom;
	}

	public void setResFrom(String resFrom) {
		this.resFrom = resFrom;
	}

	public String getResTo() {
		return resTo;
	}

	public void setResTo(String resTo) {
		this.resTo = resTo;
	}

	public double getRelRatio() {
		return relRatio;
	}

	public void setRelRatio(double relRatio) {
		this.relRatio = relRatio;
	}

}
