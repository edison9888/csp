package com.taobao.csp.depend.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.csp.depend.dao.CspCheckupDependDao;
import com.taobao.csp.depend.po.CheckupDependConfig;

public class CspCheckUpDependAo {
	private static CspCheckUpDependAo ao = new CspCheckUpDependAo();
	private CspCheckUpDependAo() {
	}
	private CspCheckupDependDao cspCheckupDependDao = new CspCheckupDependDao();

	public static final int LAST_CHECK_LINE = 1;
	
	public static final int APP_CONFIG = 0;
	public static final int APP_COMBINATION = 1; 
	
	public static CspCheckUpDependAo getInstance() {
		return ao;
	}

	public Map<String, String> getCspCheckAppMap() {
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}

	public CheckupDependConfig getCheckupDependConfig(String opsName, String targetOpsName, int codeversion) {
		return cspCheckupDependDao.getCheckupDependConfig(opsName, targetOpsName, codeversion);
	}

	public void deleteCheckupDependConfig(CheckupDependConfig config) {
		cspCheckupDependDao.deleteCheckupDependConfig(config);
	}

	public void addCheckupDependConfig(CheckupDependConfig config) {
		cspCheckupDependDao.addCheckupDependConfig(config);
	}

	public Map<String, List<Integer>> getAllCodeVersion() {
		return cspCheckupDependDao.getAllCodeVersion();
	} 

	public static void main(String[] args) {
	}
}
