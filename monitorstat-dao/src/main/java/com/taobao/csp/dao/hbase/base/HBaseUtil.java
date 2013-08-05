package com.taobao.csp.dao.hbase.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import com.nearinfinity.hbase.dsl.ForEach;
import com.nearinfinity.hbase.dsl.HBase;
import com.nearinfinity.hbase.dsl.QueryOps;
import com.nearinfinity.hbase.dsl.Row;
import com.nearinfinity.hbase.dsl.SaveRow;
import com.nearinfinity.hbase.dsl.SaveRow.SaveFamilyCol;
import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
/**
 * 提供访问Hbase的一些操作的封装
 * @author zhongting.zy
 */
public class HBaseUtil implements ManagerListener {
	

	private HBase<QueryOps<String>, String> hBase = null;
	
	//现在就一张表一个FAMILY
//	private final static String TABLENAME = "CSP_APP_HSF_PROVIDE_DETAIL";
//	private final static String FAMILY = "CM1";
	private static String TABLENAME = "CSP_RAS_TABLE";
	private static String FAMILY = "CSP_REALTIME_DATA";
	
	private static final int MAX_ROW_NUM = 10000;
	
	
	private static HBaseUtil hBaseUtil = new HBaseUtil();
	private HBaseUtil(){}
	
	private DiamondManager diamondManager = new DefaultDiamondManager("CSP_GROUP", "com.taobao.csp.hbase.config", this);
	
	private HBase<QueryOps<String>, String> getHBase(){
		
		if(hBase ==null){
			synchronized (HBaseUtil.class) {
				if(hBase == null){
					 String xml = diamondManager.getAvailableConfigureInfomation(10000);
					 File file = new File("hbase-default.xml");
					 try {
						BufferedWriter wirter = new BufferedWriter(new FileWriter(file));
						wirter.write(xml);
						wirter.close();
					} catch (IOException e) {
					}
					Configuration config = new Configuration();
					try {
						config.addResource(file.toURI().toURL());
					} catch (MalformedURLException e) {
					}
					hBase = new HBase<QueryOps<String>, String>(String.class, config);
					TABLENAME = config.get("csp_table_name","CSP_RAS_TABLE");
					FAMILY= config.get("csp_table_family","CSP_REALTIME_DATA");
				}
			}
		}
		return hBase;
	}
	
	
	private static Logger logger = Logger.getLogger(HBaseUtil.class);
	

//	
//	private static HBase<QueryOps<String>, String> getHBase() {
//		return hBase;
//	}
	
	private static SaveRow<QueryOps<String>, String> getSaveRow() {
		return hBaseUtil.getHBase().save(TABLENAME);
	}
	
	/**
	 * 单行单column添加
	 * @param rowkey
	 * @param qualifier
	 * @param o
	 * @throws IOException
	 */
	public static void addRow(String rowkey, String qualifier, Object o) throws IOException {
		getSaveRow().row(rowkey).family(FAMILY).col(qualifier, o);
	}
	
	/**
	 * 当行多column添加
	 * @param rowkey
	 * @param qualifier
	 * @param o
	 * @throws IOException
	 */
	public static void addRow(String rowkey, String[] qualifier, Object[] o) throws IOException {
		if(qualifier == null || o == null || (qualifier.length < o.length)) {
			logger.error("addRow 参数错误，null or qualifier.length < o.length.");
			throw new RuntimeException("addRow 参数错误，null or qualifier.length < o.length.");
		}
		
		SaveFamilyCol<QueryOps<String>, String> sfc = getSaveRow().row(rowkey).family(FAMILY);
		for(int i=0; i<qualifier.length; i++) {
			sfc.col(qualifier[i], o[i]);
		}
	}	
	
	/**
	 * 根据rowkey返回某列的某个类型的值
	 * @param rowkey
	 * @param qualifier
	 * @param c
	 * @return
	 */
	public static <U> U queryCellValue(String rowkey, String qualifier, Class<U> c) {
		long t = System.currentTimeMillis();
		try{
			Row<String> row = hBaseUtil.getHBase().fetch(TABLENAME).row(rowkey);  
			if(row == null)
				return null;
			return (U) row.value(FAMILY, qualifier, c);
		}finally{
			logger.debug("queryCellValue Object:"+(System.currentTimeMillis()-t));
		}
	}
	
	/**
	 * 多值查询
	 * @param rowkey
	 * @param qualifierArray
	 * @return
	 */
	public static HashMap<String, String> queryCellValue(String rowkey, String[] qualifierArray) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		long t = System.currentTimeMillis();
		
		try {
			Row<String> row = hBaseUtil.getHBase().fetch(TABLENAME).row(rowkey);
			if (row == null)
				return map;
			for (String qualifier : qualifierArray) {
				map.put(qualifier, row.value(FAMILY, qualifier, String.class));
			}
		} catch (Exception e) {
			logger.error("查询异常：", e);
		}
		
		logger.debug("queryCellValue HashMap:"+(System.currentTimeMillis()-t));
		return map; 
	}
	
	/**
	 * 根据起始rowkey查询结果
	 * @param startRowKey
	 * @param endRowKey
	 * @return
	 */
	public static <U> List<U> queryValueList(final String startId,final String endId, final String qualifier, final Class<U> c) {
		
		long t = System.currentTimeMillis();
		
		final List<U> list = new ArrayList<U>();
		hBaseUtil.getHBase().scan(TABLENAME, startId, endId).
			foreach(new ForEach<Row<String>>() {
				@Override
				public void process(Row<String> row) {
					
					
					if(list.size()>MAX_ROW_NUM){
						logger.error("<U> List<U> queryValueList ("+startId+","+endId+")  query row >"+MAX_ROW_NUM);
						return;
					}
					
					list.add((U) row.value(FAMILY, qualifier, c));
				}
			});
		
		logger.debug("queryValueList List:"+(System.currentTimeMillis()-t));
		return list;
	}
	
	
	/**
	 * 根据起始rowkey查询结果
	 * @param startRowKey
	 * @param endRowKey
	 * @return Map<ROWID,U>
	 */
	public static <U> Map<String,U> queryValueMap(final String startId, final String endId, final String qualifier, final Class<U> c) {
		
		long t = System.currentTimeMillis();
		
		final Map<String,U> map = new HashMap<String, U>();
		hBaseUtil.getHBase().scan(TABLENAME, startId, endId).select().family(FAMILY).
			foreach(new ForEach<Row<String>>() {
				@Override
				public void process(Row<String> row) {
					
					if(map.size()>MAX_ROW_NUM){
						
						logger.error("<U> Map<String,U> queryValueMap ("+startId+","+endId+")  query row >"+MAX_ROW_NUM);
						
						return;
					}
					
					map.put(row.getId(), (U) row.value(FAMILY, qualifier, c));
				}
			});
		
		logger.debug("queryValueMap Map:"+(System.currentTimeMillis()-t));
		
		return map;
	}
	
	
	
	/**
	 * 根据起始rowkey查询结果
	 * @param startRowKey
	 * @param endRowKey
	 * @return Map<ROWID,U>
	 */
	public static  Map<String,Map<String,String>> queryValueMap(final String startId,final String endId, final String[] qualifiers) {
		
		long t = System.currentTimeMillis();
		
		final Map<String,Map<String,String>> map = new HashMap<String, Map<String,String>>();
		hBaseUtil.getHBase().scan(TABLENAME, startId, endId).select().family(FAMILY).
			foreach(new ForEach<Row<String>>() {
				@Override
				public void process(Row<String> row) {
					
					if(map.size()>MAX_ROW_NUM){
						logger.error("Map<String,Map<String,String>> queryValueMap ("+startId+","+endId+")  query row >"+MAX_ROW_NUM);
						return;
					}
					
					String id = row.getId();
					Map<String,String> pMap = map.get(id);
					if(pMap == null){
						pMap = new HashMap<String, String>();
						map.put(id, pMap);
					}
					for(String qualifier:qualifiers){
						pMap.put(qualifier, row.value(FAMILY, qualifier, String.class));
					}
					
				
					
					
				}
			});
		
		logger.debug("queryValueMap Map<String,Map<String,String>>:"+(System.currentTimeMillis()-t));
		
		return map;
	}
	
	
	
//	public static void deleteData() {
//		getHBase().delete(TABLENAME);
//	}
//	
//	//获取所有行
//	public static <U> List<U> getAllData(final String qualifier, final Class<U> c) {
//		final List<U> list = new ArrayList<U>();
//
//		getHBase().scan(TABLENAME).foreach(new ForEach<Row<String>>() {
//			@Override
//			public void process(Row<String> row) {
//				list.add((U) row.values(FAMILY, qualifier, c));
//			}
//		});
//		return list;
//	}
	
	public static void main(String[] args) {
		
		try {
			HBaseUtil.addRow("ttt1234567", "rrrr", "1");
			HBaseUtil.addRow("ttt1234567", "rrrr", "2");
			HBaseUtil.addRow("ttt1234567", "rrrr", "3");
			HBaseUtil.addRow("ttt1234567", "rrrr", "4");
			
			
			System.out.println(HBaseUtil.queryCellValue("ttt1234567",  "rrrr", String.class));;
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
		System.out.println(HBaseUtil.queryValueMap("detail`PV`1329926400000", "detail`PV`1330012799000", new String[]{"E-times","C-time","P-size","C-200", "C-302", "c-other"}));;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Executor getExecutor() {
		return null;
	}

	@Override
	public void receiveConfigInfo(String arg0) {
		 HBase<QueryOps<String>, String> tmp = hBase;
		 hBase = null;
		 tmp.flush();
		 tmp = null;
	}
	
  /*
   * DBA提供的MD5加密函数，使用UTF-8加密
   */
  public static byte[] getMD5FromBytes(byte[] data) {
    if (data != null && data.length > 0) {
      try {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return new BigInteger(1, md5.digest(data)).toString(16).getBytes(
            "UTF-8");
      } catch (NoSuchAlgorithmException e) {
        logger.error("初始化MD5编码类失败!" + e);
      } catch (UnsupportedEncodingException e) {
        logger.error("UTF-8编码不存在!" + e);
      }
    }
    return null;
  }

  /*
   * 对给定的String进行Md5加密，并返回加密后生成的等长的字符串
   */
  public static String getMD5String(String targetString) {
    if (targetString == null)
      return null;
    try {
      final String charsetName = "UTF-8";
      byte[] md5Data = getMD5FromBytes(targetString.getBytes(charsetName));
      if (md5Data == null)
        return null;

      return new String(md5Data, charsetName);
    } catch (UnsupportedEncodingException e) {
      logger.error("UTF-8编码不存在!" + e);
    }
    return null;
  }
}
