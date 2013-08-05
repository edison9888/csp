package com.taobao.monitor.common.po;

/**
 * ʵʱ��������
 * @author wuhaiqian.pt
 *
 */
public class TimeConfPo {

	private int confId;
	
	private int appId;
	
	private String className;
	
	private String filePath;
	
	private int analyseFrequency;
	
	//�ָ�ÿ�е� �ָ���
	private String splitChar;

	//��־�ļ�������������Ӧ�û�����ȡ�����ģ���ֹ��������һ��
	private String aliasLogName;
	
	private String analyseFuture;// ����һЩ�����˵�������ݽ�����className �У�
	
	//����Ƕ��ļ���ȡ��ʹ�õ����ֶ�
	private String tailType;//��Ҫ�������ļ�tail ��ȡ�� ��������tail -n  ����tail -c
	
	
	private int analyseType;// 1��ʾ��ȡ�ļ�   filePath���Ǵ����־  2 ��������className 3
	
	private int obtainType;
	

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}



	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSplitChar() {
		return splitChar;
	}

	public void setSplitChar(String splitChar) {
		this.splitChar = splitChar;
	}

	public String getAliasLogName() {
		return aliasLogName;
	}

	public void setAliasLogName(String aliasLogName) {
		this.aliasLogName = aliasLogName;
	}
	
	public int getAnalyseFrequency() {
		return analyseFrequency;
	}

	public void setAnalyseFrequency(int analyseFrequency) {
		this.analyseFrequency = analyseFrequency;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAnalyseFuture() {
		return analyseFuture;
	}

	public void setAnalyseFuture(String analyseFuture) {
		this.analyseFuture = analyseFuture;
	}

	public String getTailType() {
		return tailType;
	}

	public void setTailType(String tailType) {
		this.tailType = tailType;
	}

	public int getAnalyseType() {
		return analyseType;
	}

	public void setAnalyseType(int analyseType) {
		this.analyseType = analyseType;
	}

	public int getConfId() {
		return confId;
	}

	public void setConfId(int confId) {
		this.confId = confId;
	}

	public int getObtainType() {
		return obtainType;
	}

	public void setObtainType(int obtainType) {
		this.obtainType = obtainType;
	}

	@Override
	public String toString() {
		return "TimeConfPo [confId=" + confId + ", appId=" + appId
				+ ", className=" + className + ", filePath=" + filePath
				+ ", analyseFrequency=" + analyseFrequency + ", splitChar="
				+ splitChar + ", aliasLogName=" + aliasLogName
				+ ", analyseFuture=" + analyseFuture + ", tailType=" + tailType
				+ ", analyseType=" + analyseType + ", obtainType=" + obtainType
				+ "]";
	}

	
	
}
