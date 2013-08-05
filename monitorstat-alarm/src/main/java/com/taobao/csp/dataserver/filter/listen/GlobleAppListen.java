package com.taobao.csp.dataserver.filter.listen;

import java.util.List;

public interface GlobleAppListen {
	
	public void gloableAppChange(List<String> apps);
	
	public void gloableAppAdd(String app);
	
	public void gloableAppDelete(String app);

}
