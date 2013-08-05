package com.taobao.csp.config.ao;

import com.taobao.csp.config.dao.KeyDao;
import com.taobao.monitor.common.po.KeyPo;

/*只用来补充原KeyAo中没有的方法*/
public class KeyAo {
	private static KeyAo keyAo = new KeyAo();

	private KeyAo() {
	};

	private KeyDao keyDao = new KeyDao();

	public static KeyAo get() {
		return keyAo;
	}
	
	public KeyPo getKeyPo(int id ){
		return keyDao.getKeyPo(id);
	}
}