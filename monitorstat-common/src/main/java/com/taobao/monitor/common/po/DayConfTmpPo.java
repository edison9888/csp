package com.taobao.monitor.common.po;

/**
 * �ձ���������
 * @author wuhaiqian.pt
 *
 */
public class DayConfTmpPo {

	private int tmpId;
	
	private String className;
	
	private String filePath;
	
	//�ָ�ÿ�е� �ָ���
	private String splitChar;
	
	//��־�ļ�������������Ӧ�û�����ȡ�����ģ���ֹ��������һ��
	private String aliasLogName;


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

	public int getTmpId() {
		return tmpId;
	}

	public void setTmpId(int tmpId) {
		this.tmpId = tmpId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	
	
}
