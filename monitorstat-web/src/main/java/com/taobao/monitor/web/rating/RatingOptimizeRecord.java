package com.taobao.monitor.web.rating;

import java.util.Date;

public class RatingOptimizeRecord {
	private int id;
	private int appId;
	private String subject;
	private String submitter;
	private String owner;
	private String description;
	private String status;
	private String priority;
	private String comment;
	private double rating_value;
	private Date collect_day;
	private Date gmtModified;
	private Date gmtCreate;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setRating_value(double rating_value) {
		this.rating_value = rating_value;
	}

	public double getRating_value() {
		return rating_value;
	}

	public void setCollect_day(Date collect_day) {
		this.collect_day = collect_day;
	}

	public Date getCollect_day() {
		return collect_day;
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
