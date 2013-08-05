
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.po;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaodu
 *
 * œ¬ŒÁ4:47:56
 */
public class IndexDependTable implements Comparable<IndexDependTable> {
	
	private String ftime;
	List<TimeDataInfo> sourceList = new ArrayList<TimeDataInfo>();
	List<TimeDataInfo> dependMeAppList = new ArrayList<TimeDataInfo>();
	List<TimeDataInfo> meDependAppList = new ArrayList<TimeDataInfo>();
	//Tairœ‡πÿ
	
	@Override
	public int compareTo(IndexDependTable o) {
		// TODO Auto-generated method stub
		return 0;
	}


	public String getFtime() {
		return ftime;
	}


	public void setFtime(String ftime) {
		this.ftime = ftime;
	}


	public List<TimeDataInfo> getSourceList() {
		return sourceList;
	}


	public void setSourceList(List<TimeDataInfo> sourceList) {
		this.sourceList = sourceList;
	}


	public List<TimeDataInfo> getDependMeAppList() {
		return dependMeAppList;
	}


	public void setDependMeAppList(List<TimeDataInfo> dependMeAppList) {
		this.dependMeAppList = dependMeAppList;
	}


	public List<TimeDataInfo> getMeDependAppList() {
		return meDependAppList;
	}

	public void setMeDependAppList(List<TimeDataInfo> meDependAppList) {
		this.meDependAppList = meDependAppList;
	}

}
