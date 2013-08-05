
package com.taobao.monitor.common.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 * @author xiaodu
 * @version 2010-5-31 ионГ10:57:31
 */
public class TableNameConverUtilTest {

	@Test
	public void testFormatDayOfYear() {
		try {
			String str = "2010-05-31 00:25";
			String tablename = TableNameConverUtil.formatDayTableName(str);
			Assert.assertEquals("ms_monitor_data_0151", tablename);
		} catch (Exception e) {
			fail("Not yet implemented");
		}		
	}
	
	
	

}
