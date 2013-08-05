
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
 * @version 2010-5-31 ÉÏÎç10:57:31
 */
public class TimeConvertUtilTest {

	@Test
	public void testFormatDayOfYear() {
		try {
			String str = "2010-05-31 00:25";
			Date date = TimeConvertUtil.parseStrToDayByFormat(str, "yyyy-MM-dd HH:mm");		
			String d = TimeConvertUtil.formatDayOfYear(date);
			Assert.assertEquals("0151", d);
		} catch (Exception e) {
			fail("Not yet implemented");
		}		
	}
	
	
	

}
