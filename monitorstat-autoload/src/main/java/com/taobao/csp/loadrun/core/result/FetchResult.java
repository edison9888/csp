
package com.taobao.csp.loadrun.core.result;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.csp.loadrun.core.constant.ResultKey;

/**
 * 
 * @author xiaodu
 * @version 2011-6-28 ����03:37:34
 */
public interface FetchResult {
	
	public void put(ResultKey key,Double value,Date time);
	/**
	 * ͳ�����20���ڵ�����
	 * @return
	 */
	public Map<ResultKey,Double> getResult();
	
	/**
	 * ��ԭʼ���ݷ���
	 * @return
	 */
	public Map<ResultKey, List<ResultCell>> getOriginalResult();
	
	
	public void clear();

}
