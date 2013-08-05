
package com.taobao.loadrun.run;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taobao.csp.loadrun.core.run.HttploadLoadrunTask;
import com.taobao.loadrun.BaseTest;


/**
 * 
 * @author xiaodu
 * @version 2011-6-29 ÏÂÎç01:48:43
 */
public class HttpLoadLoadrunTaskTest extends BaseTest{
	
	
	private HttploadLoadrunTask task = null;
	
	
	@Before
	public void init(){
		
		
		target.setLoadFeature("3,5,7,9,10,14,17,20");
		
		try {
			task = new HttploadLoadrunTask(this.target);
			
			task.runFetchs();
			
			task.runAutoControl();
			
			task.waitForStop();
			
			task.stopTask();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void getResult(){
		
		Assert.assertTrue(task.getLoadResult().size() >0);
		
	}
	
	

}
