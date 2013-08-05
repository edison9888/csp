
package com.taobao.monitor.common.vo;
/**
 * 
 * @author xiaodu
 * @version 2010-4-13 上午10:32:34
 */
public class SqlTop10Po implements Comparable<SqlTop10Po>{
	
private Integer keyId;
	
	public Integer getKeyId() {
		return keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}
	
	
	/** 单次耗时 */
	private String valueData;
	
	private long valueDataNum;
	
	
	/** sql 文本*/
	private String sqlFullText;
	
	private String htmlSqlText;
	
	private int returnLen = 100;
	
	public String getHtmlSqlText() {
		StringBuilder sb = new StringBuilder();
		if(this.sqlFullText!=null){
			int len = this.sqlFullText.length();
			for(int i=0;i<len;){				
				if(i+100>len){
					sb.append(this.sqlFullText.substring(i,len));
				}else{
					sb.append(this.sqlFullText.substring(i,i+100));
					sb.append("<br/>");
				}
				i+=100;
			}
			
		}		
		return sb.toString();
	}

	public void setHtmlSqlText(String htmlSqlText) {
		this.htmlSqlText = htmlSqlText;
	}
	
	
	
	/** SQL执行的可的应用名称*/
	private String dbName;


	public long getValueDataNum() {
		return valueDataNum;
	}

	public void setValueDataNum(long valueDataNum) {
		this.valueDataNum = valueDataNum;
	}

	public String getSqlFullText() {
		return sqlFullText;
	}

	public void setSqlFullText(String sqlFullText) {
		this.sqlFullText = sqlFullText;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getValueData() {
		return valueData;
	}

	public void setValueData(String valueData) {
		this.valueData = valueData;
	}
	public int compareTo(SqlTop10Po o) {
		
		if(o.valueDataNum==this.valueDataNum){
			return 0;
		}else if(o.valueDataNum>this.valueDataNum){
			return 1;
		}else if(o.valueDataNum<this.valueDataNum){
			return -1;
		}
		return 0;
	}


}
