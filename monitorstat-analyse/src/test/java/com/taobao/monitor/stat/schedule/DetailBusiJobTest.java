package com.taobao.monitor.stat.schedule;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionException;

public class DetailBusiJobTest {
    
      DetailBusiJob  job  = null;
    
    @Before
    public void setUp() throws Exception {
	
	job = new DetailBusiJob();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testExecute() throws JobExecutionException {
	
	//job.execute(null);
	
    }

}
