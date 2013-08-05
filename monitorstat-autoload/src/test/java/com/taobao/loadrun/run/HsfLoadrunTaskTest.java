
package com.taobao.loadrun.run;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taobao.csp.loadrun.core.run.HsfLoadrunTask;
import com.taobao.loadrun.BaseTest;


/**
 * 
 * @author xiaodu
 * @version 2011-6-29 ÏÂÎç01:48:43
 */
public class HsfLoadrunTaskTest extends BaseTest{
	
	
	private HsfLoadrunTask task = null;
	
	
	@Before
	public void init(){
		
		try {
			task = new HsfLoadrunTask(this.target);
			
			task.runFetchs();
			
			task.runAutoControl();
			
			task.waitForStop();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void getResult(){
		
		Assert.assertEquals(task.getLoadResult().size(),0);
		
	}
	
	

}
