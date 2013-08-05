package com.taobao.csp.cost.po;

import com.taobao.monitor.common.po.HostPo;

/**
 * ��̨���������ɱ���
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-11
 */
public class HostCostDetail{

	public HostCostDetail(){
		
	}
	private String hostName;		//������
	private String hostType;		//��������
	private String opsName;			//ops
	
	private HostPo detailHostInfo;	//��ϸ������Ϣ
	private int parentSplitSize;	//ʵ����ֳɶ��ٸ������
	//������ɱ�
	private double hostPrice;
	private String hardInfo;
	
	
	public double getHostPrice() {
		return hostPrice;
	}
	public void setHostPrice(double hostPrice) {
		this.hostPrice = hostPrice;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostType() {
		return hostType;
	}
	public void setHostType(String hostType) {
		this.hostType = hostType;
	}
	public HostPo getDetailHostInfo() {
		return detailHostInfo;
	}
	public void setDetailHostInfo(HostPo detailHostInfo) {
		this.detailHostInfo = detailHostInfo;
	}

	public String getOpsName() {
		return opsName;
	}
	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}
	public int getParentSplitSize() {
		return parentSplitSize;
	}
	public void setParentSplitSize(int parentSplitSize) {
		this.parentSplitSize = parentSplitSize;
	}
	public String getHardInfo() {
		return hardInfo;
	}
	public void setHardInfo(String hardInfo) {
		this.hardInfo = hardInfo;
	}
	
}
