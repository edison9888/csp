
package com.taobao.loadrun.control;

import org.junit.Before;
import org.junit.Test;

import com.taobao.csp.loadrun.core.control.ControlFactory;
import com.taobao.csp.loadrun.core.control.IControl;
import com.taobao.loadrun.BaseTest;


/**
 * 
 * @author xiaodu
 * @version 2011-7-1 ионГ10:56:45
 */
public class ApacheRestartControlTest extends BaseTest{
	
	private String targetIp = "10.232.15.173";
	
	IControl control = null;
	
	@Before
	public void init() throws Exception{
	
		control = ControlFactory.getApacheSplitControl(target, targetIp);	
	}
	
	
	@Test
	public void control() throws Exception{
		control.control();
	}
}
