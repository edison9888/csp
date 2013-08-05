
package com.taobao.loadrun.fetch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taobao.csp.loadrun.core.fetch.ApacheFetchTaskImpl;
import com.taobao.csp.loadrun.core.result.FetchResult;
import com.taobao.loadrun.BaseTest;

/**
 * 
 * @author xiaodu
 * @version 2011-6-28 ÉÏÎç11:36:50
 */
public class ApacheFetchTaskImplTest extends BaseTest{
	
	
	ApacheFetchTaskImpl impl = null;
	CountDownLatch latch = null;
	
	@Before
	public void init(){
		
		latch = new CountDownLatch(1);
		
		impl =  new ApacheFetchTaskImpl(target,"tail -f /home/admin/cai/logs/cronolog/2011/07/2011-07-26-taobao-access_log");
		
		impl.doTask(latch);
		
		try {
			latch.await(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		impl.stopFetch();
	}


	@Test
	public void testGetResult() {
		FetchResult r = impl.getResult();
		r.getResult();
		
		Assert.assertNotNull(r);
	}

}
