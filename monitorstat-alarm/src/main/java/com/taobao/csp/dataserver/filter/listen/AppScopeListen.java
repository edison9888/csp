package com.taobao.csp.dataserver.filter.listen;

import java.util.List;

public interface AppScopeListen {
	
	public void appScopeChange(List<String> configs);
	
	public void appScopeAdd(String config);
	
	public void appScopeDelete(String config);

}
