/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.web.core.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.taobao.monitor.common.db.impl.DetailMonitorDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version 2010-5-24:ÏÂÎç09:04:39
 * 
 */
public class DetailMonitorDAOTest {

    DetailMonitorDAO detailMonitorDAO;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
//	detailMonitorDAO = new DetailMonitorDAO(
//		"jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=UTF-8", "xiangfei", "taobao1234");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link com.taobao.monitor.web.core.dao.impl.DetailMonitorDAO#queryStatic(java.lang.String, java.lang.String, int, java.util.Date, java.util.Date)}
     * .
     */
    @Test
    public void testQueryStatic() {

//	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//	String tableSuffix = simpleDateFormat.format(new Date());
//
//	Calendar cal = Calendar.getInstance();
//	cal.setTime(new Date());
//	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
//	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 1);
//
//	Date end = cal.getTime();
//
//	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
//
//	Date start = cal.getTime();
//
//	Map<Integer, Integer> statMap = detailMonitorDAO.queryStatic(tableSuffix, "1,2,3", 1, start, end);
//
//	Assert.assertNotNull(statMap);
//	Assert.assertEquals(3, statMap.size());
    }

}
