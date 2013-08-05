//package com.taobao.monitor.common.db.impl;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.taobao.monitor.common.db.po.DetailStatisticDO;
//
//public class DetailBusiDataDAOTest {
//
//    
//    DetailBusiDataDAO detailBusiDataDAO = null;
//    /**
//     * @throws java.lang.Exception
//     */
//    @Before
//    public void setUp() throws Exception {
//	detailBusiDataDAO = new DetailBusiDataDAO();
//    }
//
//    /**
//     * @throws java.lang.Exception
//     */
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    /**
//     * Test method for {@link com.taobao.monitor.stat.db.impl.DetailBusiDataDAO#insertDailyStatisticData(com.taobao.monitor.stat.db.po.DetailStatisticDO)}.
//     */
//    @Test
//    public void testInsertDailyStatisticData() {
//	
//	Date date = new Date(); 
//	Calendar cal = Calendar.getInstance();
//	cal.setTime(date);
//	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) +1, 0, 0, 0);
//	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
//
//	Date end = cal.getTime();
//	
//	DetailStatisticDO statisticDO = new DetailStatisticDO();
//	statisticDO.setAppId(1);
//	statisticDO.setGroupId(1);
//	statisticDO.setKeyId(1);
//	statisticDO.setVaue(3);
//	statisticDO.setGroupSum(5);
//	statisticDO.setStatisticDate(end);
//	statisticDO.setCreateDate(new Date());
//	detailBusiDataDAO.insertDailyStatisticData(statisticDO);
//    }
//
//    /**
//     * Test method for {@link com.taobao.monitor.stat.db.impl.DetailBusiDataDAO#queryStatisticData(int, int, java.util.Date, java.util.Date)}.
//     */
//    @Test
//    public void testQueryStatisticData() {
////	Date date = new Date(); 
////	Calendar cal = Calendar.getInstance();
////	cal.setTime(date);
////	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) +1, 0, 0, 0);
////	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 1);
////
////	Date end = cal.getTime();
////	
////	cal.setTime(date);
////	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) , 0, 0, 0);
////	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) );
////	Date start = cal.getTime();
////	List<DetailStatisticDO>  list = detailBusiDataDAO.queryStatisticData(1, 1, start, end);
////	Assert.assertEquals(1, list.size());
//    }
//
//}
