package com.taobao.csp.dao.hbase.andor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.taobao.csp.dao.hbase.andor.HBaseAndorUtil;

/**
 * @description
 * @author <a href="junyu@taobao.com">junyu</a>
 * @date 2012-9-4ÏÂÎç04:14:47
 */
public class TestHBaseAndorUtil {
	static HBaseAndorUtil util;
	private static String rowKey1;
	private static String rowKey2;
	private static String rowKey3;
	private static String rowKey4;
	private static String rowKey5;
	private static String rowKey6;
	private static String rowKey7;
	private static String rowKey8;
	private static String rowKey9;

	private static String qualifierCtime = "C-time";
	private static String qualifierEtimes = "E-times";
	private static String qualifierPsize = "P-size";
	private static String qualifierError = "error";
	private static String qualifierSucRate = "sucRate";
	private static String qualifierTimeout = "timeout";
	
	private static String qualifierRsize="R-size";
	private static String qualifierC200="C-200";
	private static String qualifierC302="C-302";
	private static String qualifiercother="c-other";
	
	private static String qualifierTIMEDWAITING="TIMEDWAITING";
	private static String qualifierRUNNABLE="RUNNABLE";
	
	@BeforeClass
	public static void startUp() {
//		util = new HBaseAndorUtil("V0#classpath:andor_hbase/hbaseRule.xml",
//				"andor_hbase/hbaseSchema.xml", "andor_hbase/hbaseTopology.xml");
		util = new HBaseAndorUtil("CSP_HBASE_APP");
		rowKey1 = "andor_hbase_test1`1346921077311";
		rowKey2 = "andor_hbase_test2`1346921374091";
		rowKey3 = "andor_hbase_test2`1346921374011";
		rowKey4 = "andor_hbase_test2`1346921374131";
		rowKey5 = "andor_hbase_test2`1346921374491";
		
		rowKey6 = "andor_hbase_test3`1346921374091";
		rowKey7 = "andor_hbase_test3`1346921374011";
		rowKey8 = "andor_hbase_test3`1346921374131";
		rowKey9 = "andor_hbase_test3`1346921374491";
	}

	@Test
	public void testAddRowSingleQualifier() {
		util.addRow(rowKey1, qualifierCtime, "100");
		util.addRow(rowKey1, qualifierEtimes, "200");
		util.addRow(rowKey1, qualifierPsize, "300");
		util.addRow(rowKey1, qualifierError, "400");
		util.addRow(rowKey1, qualifierSucRate, "500");
		util.addRow(rowKey1, qualifierTimeout, "600");
	}

	@Test
	public void testAddRowMutiQualifier() {
		String[] qualifiers = new String[6];
		qualifiers[0] = qualifierCtime;
		qualifiers[1] = qualifierEtimes;
		qualifiers[2] = qualifierPsize;
		qualifiers[3] = qualifierError;
		qualifiers[4] = qualifierSucRate;
		qualifiers[5] = qualifierTimeout;

		List<Object> objects = new ArrayList<Object>();
		objects.add("1000");
		objects.add("1100");
		objects.add("1200");
		objects.add("1300");
		objects.add("1400");
		objects.add("1500");
		util.addRow(rowKey2, qualifiers, objects.toArray());

		List<Object> objects2 = new ArrayList<Object>();
		objects2.add("2000");
		objects2.add("2100");
		objects2.add("2200");
		objects2.add("2300");
		objects2.add("2400");
		objects2.add("2500");
		util.addRow(rowKey3, qualifiers, objects2.toArray());

		List<Object> objects3 = new ArrayList<Object>();
		objects3.add("3000");
		objects3.add("3100");
		objects3.add("3200");
		objects3.add("3300");
		objects3.add("3400");
		objects3.add("3500");
		util.addRow(rowKey4, qualifiers, objects3.toArray());

		List<Object> objects4 = new ArrayList<Object>();
		objects4.add("4000");
		objects4.add("4100");
		objects4.add("4200");
		objects4.add("4300");
		objects4.add("4400");
		objects4.add("4500");
		util.addRow(rowKey5, qualifiers, objects4.toArray());
	}

	@Test
	public void testAddRowMutiQualifier2() {
		String[] qualifiers = new String[6];
		qualifiers[0] = qualifierRsize;
		qualifiers[1] = qualifierC200;
		qualifiers[2] = qualifierC302;
		qualifiers[3] = qualifiercother;
		qualifiers[4] = qualifierTIMEDWAITING;
		qualifiers[5] = qualifierRUNNABLE;

		List<Object> objects = new ArrayList<Object>();
		objects.add("100000");
		objects.add("110000");
		objects.add("120000");
		objects.add("130000");
		objects.add("140000");
		objects.add("150000");
		util.addRow(rowKey6, qualifiers, objects.toArray());

		List<Object> objects2 = new ArrayList<Object>();
		objects2.add("200000");
		objects2.add("210000");
		objects2.add("220000");
		objects2.add("230000");
		objects2.add("240000");
		objects2.add("250000");
		util.addRow(rowKey7, qualifiers, objects2.toArray());

		List<Object> objects3 = new ArrayList<Object>();
		objects3.add("300000");
		objects3.add("310000");
		objects3.add("320000");
		objects3.add("330000");
		objects3.add("340000");
		objects3.add("350000");
		util.addRow(rowKey8, qualifiers, objects3.toArray());

		List<Object> objects4 = new ArrayList<Object>();
		objects4.add("400000");
		objects4.add("410000");
		objects4.add("420000");
		objects4.add("430000");
		objects4.add("440000");
		objects4.add("450000");
		util.addRow(rowKey9, qualifiers, objects4.toArray());
	}
	
	@Test
	public void testQueryCellValueSingleQualifier() {
		String s = util.queryCellValue(rowKey1, qualifierEtimes, String.class);
		Assert.assertEquals("200", s);
	}
	
	@Test
	public void testQueryCellValueSingleQualifier2() {
		String s = util.queryCellValue(rowKey9, qualifierC200, String.class);
		Assert.assertEquals("410000", s);
	}

	@Test
	public void testQueryCellValueMutiQualifier() {
		String[] qualifiers = new String[2];
		qualifiers[0] = qualifierCtime;
		qualifiers[1] = qualifierEtimes;
		Map<String, String> re = util.queryCellValue(rowKey2, qualifiers);
		Assert.assertEquals("1000", re.get(qualifierCtime));
		Assert.assertEquals("1100", re.get(qualifierEtimes));
	}
	
	@Test
	public void testQueryCellValueMutiQualifie2() {
		String[] qualifiers = new String[2];
		qualifiers[0] = qualifierTIMEDWAITING;
		qualifiers[1] = qualifiercother;
		Map<String, String> re = util.queryCellValue(rowKey8, qualifiers);
		Assert.assertEquals("340000", re.get(qualifierTIMEDWAITING));
		Assert.assertEquals("330000", re.get(qualifiercother));
	}

	@Test
	public void testQueryValueList() {
		List<String> re = util.queryValueList(rowKey3, rowKey5,
				qualifierSucRate, String.class);
		Assert.assertEquals("2400", re.get(0));
		Assert.assertEquals("1400", re.get(1));
		Assert.assertEquals("3400", re.get(2));
	
	}
	
	@Test
	public void testQueryValueList2() {
		List<String> re = util.queryValueList(rowKey7, rowKey9,
				qualifierTIMEDWAITING, String.class);
		Assert.assertEquals("240000", re.get(0));
		Assert.assertEquals("140000", re.get(1));
		Assert.assertEquals("340000", re.get(2));
	}

	@Test
	public void testQueryValueMap() {
		Map<String, String> re = util.queryValueMap(rowKey3, rowKey5,
				qualifierCtime, String.class);

		Assert.assertEquals("2000", re.get(rowKey3));
		Assert.assertEquals("3000", re.get(rowKey4));
		Assert.assertEquals("1000", re.get(rowKey2));
	}
	
	
	@Test
	public void testQueryValueMap2() {
		Map<String, String> re = util.queryValueMap(rowKey7, rowKey9,
				qualifierRsize, String.class);

		Assert.assertEquals("200000", re.get(rowKey7));
		Assert.assertEquals("300000", re.get(rowKey8));
		Assert.assertEquals("100000", re.get(rowKey6));
	}

	@Test
	public void testQueryValueMapMutiQualifier() {
		String[] qualifiers = new String[2];
		qualifiers[0]=qualifierCtime;
		qualifiers[1]=qualifierEtimes;
		Map<String, Map<String, String>> re = util.queryValueMap(rowKey3,
				rowKey5, qualifiers);
		Map<String, String> reRowkey1 = re.get(rowKey3);
		Map<String, String> reRowkey2 = re.get(rowKey4);
		Map<String, String> reRowkey3 = re.get(rowKey2);

		Assert.assertEquals("2000", reRowkey1.get(qualifierCtime));
		Assert.assertEquals("2100", reRowkey1.get(qualifierEtimes));

		Assert.assertEquals("3000", reRowkey2.get(qualifierCtime));
		Assert.assertEquals("3100", reRowkey2.get(qualifierEtimes));
		
		Assert.assertEquals("1000", reRowkey3.get(qualifierCtime));
		Assert.assertEquals("1100", reRowkey3.get(qualifierEtimes));
	}
	
	@Test
	public void testQueryValueMapMutiQualifier2() {
		String[] qualifiers = new String[2];
		qualifiers[0]=qualifierRsize;
		qualifiers[1]=qualifierC200;
		Map<String, Map<String, String>> re = util.queryValueMap(rowKey7,
				rowKey9, qualifiers);
		Map<String, String> reRowkey1 = re.get(rowKey7);
		Map<String, String> reRowkey2 = re.get(rowKey8);
		Map<String, String> reRowkey3 = re.get(rowKey6);

		Assert.assertEquals("200000", reRowkey1.get(qualifierRsize));
		Assert.assertEquals("210000", reRowkey1.get(qualifierC200));

		Assert.assertEquals("300000", reRowkey2.get(qualifierRsize));
		Assert.assertEquals("310000", reRowkey2.get(qualifierC200));
		
		Assert.assertEquals("100000", reRowkey3.get(qualifierRsize));
		Assert.assertEquals("110000", reRowkey3.get(qualifierC200));
	}
}
