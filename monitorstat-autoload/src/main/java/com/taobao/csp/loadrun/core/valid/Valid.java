
package com.taobao.csp.loadrun.core.valid;

import com.taobao.csp.loadrun.core.result.FetchResult;

/**
 * 
 * @author xiaodu
 * @version 2011-6-24 ����03:01:12
 */
public interface Valid {
	
	/**
	 * �ж�ֵ�Ƿ��Ѿ�������ֵ��
	 * @param result
	 * @return false ���� true ����
	 */
	public boolean valid(FetchResult result);

}
