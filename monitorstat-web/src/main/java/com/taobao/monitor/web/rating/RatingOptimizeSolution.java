package com.taobao.monitor.web.rating;

import java.util.Date;

public class RatingOptimizeSolution {
	private int id;
	private int optimize_record_id;
	private String submitter;
	private String reason;
	private String solution;
	private Date gmtModified;
	private Date gmtCreate;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getOptimize_record_id() {
		return optimize_record_id;
	}

	public void setOptimize_record_id(int optimizeRecordId) {
		optimize_record_id = optimizeRecordId;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

}
