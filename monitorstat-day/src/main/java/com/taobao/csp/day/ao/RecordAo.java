package com.taobao.csp.day.ao;

import com.taobao.csp.day.dao.RecordDao;
import com.taobao.csp.day.po.RecordPo;

public class RecordAo {
	
	private RecordDao dao;
	
	private static RecordAo ao = new RecordAo();
	
	private RecordAo() {
		this.dao = new RecordDao();
	}
	
	public static RecordAo get() {
		return ao;
	}
	
	/***
	 * 更新recordPo记录
	 * @param po
	 */
	public void addOrUpdateRecord(RecordPo po) {
		RecordPo existPo = dao.checkExist(po);
		if (existPo == null) {
			dao.addRecord(po);
		} else {
			po.setId(existPo.getId());
			dao.updateRecord(po);
		}
	}
	
	/***
	 * 用po的基本信息去查询已经存在的记录 
	 * @param po
	 * @return
	 */
	public RecordPo existRecordPo(RecordPo po) {
		return dao.checkExist(po);
	}
}
