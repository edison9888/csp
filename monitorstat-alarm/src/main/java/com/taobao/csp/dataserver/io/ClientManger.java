
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import com.taobao.csp.dataserver.io.exception.NoServerException;

/**
 * @author xiaodu
 *
 * ����12:00:31
 */
public interface ClientManger {
	
	public DataClient selectSingleClient(String keyName)throws NoServerException;
	
	public DataClient selectMutilClient()throws NoServerException;
	
	public void removeDataClient(DataClient client);

}
