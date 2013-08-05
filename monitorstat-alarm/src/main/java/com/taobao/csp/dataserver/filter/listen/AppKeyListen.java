package com.taobao.csp.dataserver.filter.listen;

import java.util.List;

/**
 * 对主机级别的控制
 * @author zhongting.zy
 *
 */
public interface AppKeyListen {
	
	public void appKeyChange(List<String> configs);
	
	public void appKeyAdd(String config);
	
	public void appKeyDelete(String config);

}
