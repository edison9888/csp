package com.taobao.csp.time.web.po;

import java.util.ArrayList;
import java.util.List;

/**
 * һ��ҵ����������нڵ���Ϣ
 * @author zhongting.zy
 *
 */
public class TopoPo {
	
	public TopoPo() {
	}
	
	public TopoPo(String url, List<KeyNode> keyList) {
		this.url = url;
		this.keyList = keyList;
		try {
      this.id = "" + Math.abs(url.hashCode());
		} catch (Exception e) {
		}
	}
	
	private String url;	//identityId
	private String id;	//url ��ϣ����ֵ
	
	private List<KeyNode> keyList = new ArrayList<KeyNode>();
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<KeyNode> getKeyList() {
		return keyList;
	}
	public void setKeyList(List<KeyNode> keyList) {
		this.keyList = keyList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
