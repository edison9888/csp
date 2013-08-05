
package com.taobao.csp.alarm.po;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 
 * @author xiaodu
 * @version 2011-2-27 ÏÂÎç09:29:32
 */
public class UserDefine {
	
	private int userId;
	
	private String phone;
	
	private String wangwang;
	
	private String email;
	
	private Set<Integer> acceptAppIdSet = null;
	
	private Map<Integer,Set<Integer>> extraAcceptAppKey = null;
	
	private List<ReportRange> wangwangRangeList = null;
	
	private List<ReportRange> phoneRangeList = null;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWangwang() {
		return wangwang;
	}




	public void setWangwang(String wangwang) {
		this.wangwang = wangwang;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public Set<Integer> getAcceptAppIdSet() {
		return acceptAppIdSet;
	}




	public void setAcceptAppIdSet(Set<Integer> acceptAppIdSet) {
		this.acceptAppIdSet = acceptAppIdSet;
	}




	public Map<Integer, Set<Integer>> getExtraAcceptAppKey() {
		return extraAcceptAppKey;
	}




	public void setExtraAcceptAppKey(Map<Integer, Set<Integer>> extraAcceptAppKey) {
		this.extraAcceptAppKey = extraAcceptAppKey;
	}

	public List<ReportRange> getWangwangRangeList() {
		return wangwangRangeList;
	}

	public void setWangwangRangeList(List<ReportRange> wangwangRangeList) {
		this.wangwangRangeList = wangwangRangeList;
	}

	public List<ReportRange> getPhoneRangeList() {
		return phoneRangeList;
	}

	public void setPhoneRangeList(List<ReportRange> phoneRangeList) {
		this.phoneRangeList = phoneRangeList;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	





}
