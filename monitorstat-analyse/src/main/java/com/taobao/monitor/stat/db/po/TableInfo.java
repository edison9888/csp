package com.taobao.monitor.stat.db.po;
/**   
 * @author xiaoxie   
 * @create time��2010-4-13 ����03:36:43   
 * @description  
 */
public class TableInfo {
	/** ���� */
	private String tableName;
	/** �ܼ�¼�� */
	private long recordNum;
	/** ÿ������ */
	private long delta;
	
	/** ÿ��������¼�� */
	private long insDiff;
	/** ÿ����¼�¼�� */
	private long updDiff;
	/** ÿ��ɾ�� */
	private long delDiff;
	/** ��¼������recordNum�����������Բ�ͬ�ı���ͳ�Ʊ�*/
	private long numRows;
	
	private String owner;
	
	private String dbName;
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public long getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(long recordNum) {
		this.recordNum = recordNum;
	}
	public long getDelta() {
		return delta;
	}
	public void setDelta(long delta) {
		this.delta = delta;
	}
	public String getAppName() {
		if ("collect_info".equals(this.getTableName())) {
			return "dwdb";
		} else if ("feed_receive".equals(this.getTableName())) {
			return "dwdb";
		}else if ("auction_auctions".equals(this.getTableName())) {
			return "tbdb1";
		}else if ("tc_biz_order".equals(this.getTableName())) {
			return "comm";
		}else if ("cart".equals(this.getTableName())) {
			return "misc";
		}else if ("tc_pay_order".equals(this.getTableName())) {
			return "comm";
		}else if ("tc_logistics_order".equals(this.getTableName())) {
			return "comm";
		}else if ("collect_item".equals(this.getTableName())) {
			return "dwdb";
		}else if ("bmw_users".equals(this.getTableName())) {
			return "bmw";
		}else if ("bmw_shops".equals(this.getTableName())) {
			return "shop";
		} 
		return null;
	}
	public long getInsDiff() {
		return insDiff;
	}
	public void setInsDiff(long insDiff) {
		this.insDiff = insDiff;
	}
	public long getUpdDiff() {
		return updDiff;
	}
	public void setUpdDiff(long updDiff) {
		this.updDiff = updDiff;
	}
	public long getDelDiff() {
		return delDiff;
	}
	public void setDelDiff(long delDiff) {
		this.delDiff = delDiff;
	}
	public long getNumRows() {
		return numRows;
	}
	public void setNumRows(long numRows) {
		this.numRows = numRows;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
