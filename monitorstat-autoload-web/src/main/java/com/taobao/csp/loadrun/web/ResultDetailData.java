package com.taobao.csp.loadrun.web;

import java.util.ArrayList;
import java.util.List;

/***
 * 压测详情数据节点
 * @author youji.zj
 * @version 2012-07-06
 *
 */
public class ResultDetailData {
	
	private int id;
	
	private String name;
	
	private double count;
	
	private double time;
	
	private double restTime;
	
	private List<ResultDetailData> children = new ArrayList<ResultDetailData>();

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getRestTime() {
		return restTime;
	}

	public void setRestTime(double restTime) {
		this.restTime = restTime;
	}

	public List<ResultDetailData> getChildren() {
		return children;
	}

	public void setChildren(List<ResultDetailData> children) {
		this.children = children;
	}
	
	public void addChild(ResultDetailData child) {
		children.add(child);
	}
}
