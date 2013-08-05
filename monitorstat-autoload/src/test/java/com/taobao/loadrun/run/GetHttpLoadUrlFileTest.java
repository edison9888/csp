
package com.taobao.loadrun.run;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.taobao.csp.loadrun.core.run.GetHttpLoadUrlFile_n;
import com.taobao.loadrun.BaseTest;


/**
 * 
 * @author xiaodu
 * @version 2011-6-29 ÏÂÎç05:07:32
 */
public class GetHttpLoadUrlFileTest extends BaseTest{
	
	
	@Test
	public void getpath() throws Exception{
		
		
		
		GetHttpLoadUrlFile_n file = new GetHttpLoadUrlFile_n(this.target);
		String f = file.getFilePath();
		System.out.println(f);
		
		File log = new File(f);
		
		Assert.assertTrue(log.length() >10000);
		
	}
	

}
