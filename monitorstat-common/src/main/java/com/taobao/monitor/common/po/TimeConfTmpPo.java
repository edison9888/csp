package com.taobao.monitor.common.po;

/**
 * ʵʱ��������
 * @author wuhaiqian.pt
 *
 */
public class TimeConfTmpPo {

	
	private int tmpId;
	
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
	

	private String analyseDesc;
	
	private int obtainType;
	
	

	public String getAnalyseDesc() {
		return analyseDesc;
	}

	public void setAnalyseDesc(String analyseDesc) {
		this.analyseDesc = analyseDesc;
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

	public int getTmpId() {
		return tmpId;
	}

	public void setTmpId(int tmpId) {
		this.tmpId = tmpId;
	}

	public int getObtainType() {
		return obtainType;
	}

	public void setObtainType(int obtainType) {
		this.obtainType = obtainType;
	}

	
	
}
