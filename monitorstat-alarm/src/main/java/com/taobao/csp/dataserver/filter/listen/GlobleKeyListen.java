package com.taobao.csp.dataserver.filter.listen;

import java.util.List;

public interface GlobleKeyListen {
	public void gloableKeyChange(List<String> keys);
	
	public void gloableKeyAdd(String Key);
	
	public void gloableKeyDelete(String Key);
}
