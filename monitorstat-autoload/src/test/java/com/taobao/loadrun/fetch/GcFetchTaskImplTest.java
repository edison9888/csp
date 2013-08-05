
package com.taobao.loadrun.fetch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taobao.csp.loadrun.core.fetch.GcFetchTaskImpl;
import com.taobao.csp.loadrun.core.result.FetchResult;
import com.taobao.loadrun.BaseTest;


/**
 * 
 * @author xiaodu
 * @version 2011-6-28 ÏÂÎç01:40:27
 */
public class GcFetchTaskImplTest  extends BaseTest{
	
	GcFetchTaskImpl impl = null;
	CountDownLatch latch = null;
	
	@Before
	public void init(){
		
		latch = new CountDownLatch(1);
		
		impl =  new GcFetchTaskImpl(target);
		
		impl.doTask(latch);
		
		try {
			latch.await(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		impl.stopFetch();
	}


	@Test
	public void testGetResult() {
		FetchResult r = impl.getResult();
		Assert.assertNotNull(r);
	}

}
