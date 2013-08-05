
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;

/**
 * @author xiaodu
 *
 * ����1:32:25
 */
public class AnalyseInfo {
	
	private String analyseClass;
	
	private String analyseType;//�������� javascript,dynamicjava,java
	
	private String analyseDetail;//��¼�������ݣ�javascript����̬java�����ʹ�Ŵ��룬

	public String getAnalyseType() {
		return analyseType;
	}

	public void setAnalyseType(String analyseType) {
		this.analyseType = analyseType;
	}

	public String getAnalyseDetail() {
		return analyseDetail;
	}

	public void setAnalyseDetail(String analyseDetail) {
		this.analyseDetail = analyseDetail;
	}

	@Override
	public int hashCode() {
		String h2 = this.getAnalyseDetail()+"_"+this.getAnalyseType();
		return h2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AnalyseInfo){
			AnalyseInfo info = (AnalyseInfo)obj;
			String h1 = info.getAnalyseDetail()+"_"+info.getAnalyseType()+"_"+info.getAnalyseClass();
			String h2 = this.getAnalyseDetail()+"_"+this.getAnalyseType()+"_"+this.getAnalyseClass();
			return h1.equals(h2);
		}
		return super.equals(obj);
	}

	public String getAnalyseClass() {
		return analyseClass;
	}

	public void setAnalyseClass(String analyseClass) {
		this.analyseClass = analyseClass;
	}
	

}
