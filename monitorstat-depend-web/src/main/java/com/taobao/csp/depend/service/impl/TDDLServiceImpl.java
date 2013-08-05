/**
 * 
 */
package com.taobao.csp.depend.service.impl;



import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.tddl.MainResultPo;
import com.taobao.csp.depend.service.TDDLService;
import com.taobao.csp.depend.util.MethodUtil;

/**
 *@author wb-lixing
 *2012-4-17 ÏÂÎç04:57:44 
 */
public class TDDLServiceImpl implements TDDLService{
  private static Logger logger = Logger.getLogger(TDDLServiceImpl.class);
	/* (non-Javadoc)
	 * @see com.taobao.csp.depend.service.TDDLService#queryWithAppName(java.lang.String, java.lang.String)
	 */
//	@Override
//	public List<ConsumeTDDLDetail> queryByName(String name, String selectDate, int pageNo, int pageSize) {
//		return null;
//	} 
//
//	@Override
//	public List<ConsumeTDDLDetail> queryByName(String name, int hour, int pageNo, int pageSize) {
//		return null;
//	} 
//	/**
//	 *@author wb-lixing
//	 *2012-4-18 ÉÏÎç09:55:15
//	 *@param maxTime 
//	 */
//	private float getTimeValue(String timeStr) {
//		String[] timeStrArr = timeStr.split("#");
//		String timeStrArr1 = timeStrArr[0];
//		float time = Float.parseFloat(timeStrArr1);
//		return time;
//		
//	}
//
//
//	//@Resource(name="cspTddlConsumeDao") 
//	private CspTddlConsumeDao dao;

  public MainResultPo getIndexInfoByTableName(String tableName) {  //software_version
    if(tableName == null)
      return null;
    String requestUrl = "http://dba.tools.taobao.com:9999/services/dict/getTablesByName.jsn?tableName=" + tableName;
    String recordString;
    try {
      recordString = MethodUtil.getStringByUrl(requestUrl, "GBK");
      MainResultPo po = MethodUtil.getObjectByJsonStr(MainResultPo.class, recordString);
      return po;
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }
  
  public static void main(String[] args) {
    MainResultPo po = new TDDLServiceImpl().getIndexInfoByTableName("software_version");
    System.out.println(po);
  }
}
