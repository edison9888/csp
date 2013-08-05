package com.taobao.csp.dao.hbase.andor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import com.taobao.ustore.client.andor.executor.andor_executor.AndOrClient;
import com.taobao.ustore.common.config.ConfigContext;
import com.taobao.ustore.common.inner.IRowSet;
import com.taobao.ustore.common.inner.ResultCursor;
import com.taobao.ustore.optimizer.context.OptimizerContext;
import com.taobao.ustore.optimizer.impl.context.OptimizerContextImpl;

/**
 * @description 初始版本，可能有并发问题
 * @author <a href="junyu@taobao.com">junyu</a>
 * @date 2012-9-3下午05:18:30
 */
public class HBaseAndorUtil {
	private static Logger logger = Logger.getLogger(HBaseAndorUtil.class);
	private static String TABLENAME = "CSP_RAS_TABLE";
	private static String FAMILY = "CSP_REALTIME_DATA";

	
	private static DiamondManager diamondManager = new DefaultDiamondManager(
			"CSP_GROUP", "com.taobao.csp.hbase.config", new ManagerListener() {
				
				@Override
				public Executor getExecutor() {
					return null;
				}

				@Override
				public void receiveConfigInfo(String arg0) {
					 //do nothing
				}
			});
	
	private AndOrClient ds;
	private OptimizerContext oc = null;
	private AtomicBoolean inited = new AtomicBoolean(false);

	private final String appName;
	private final String rule;
	private final String schema;
	private final String topology;

	private final Map<String/* real column_name */, String/* temp column_name */> specMap = new HashMap<String, String>();

	private static  HBaseAndorUtil cspHbaseUtil=null;

	static{
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
		
		TABLENAME = config.get("csp_table_name","CSP_RAS_TABLE");
		FAMILY= config.get("csp_table_family","CSP_REALTIME_DATA");
		cspHbaseUtil=new HBaseAndorUtil("CSP_HBASE_APP");
	}
	public HBaseAndorUtil(String appName) {
		this(appName, null, null, null);
	}

	public static HBaseAndorUtil getCspHbaseUtil(){
		return cspHbaseUtil;
	}
	
	public HBaseAndorUtil(String rule, String schema, String topology) {
		this(null, rule, schema, topology);
	}

	public HBaseAndorUtil(String appName, String rule, String schema,
			String topology) {
		this.appName = appName;
		this.rule = rule;
		this.schema = schema;
		this.topology = topology;
		initAndOr();
		initSpecQualifierMapping();
	}

	private void initAndOr() {
		if (inited.compareAndSet(false, true)) {
			ds = new AndOrClient();
			ConfigContext configContext = new ConfigContext();
			configContext.setAppName(appName);
			configContext.setAppRuleFile(rule);
			configContext.setSchemaFile(schema);
			configContext.setMachineTopologyFile(topology);
			ds.setConfigContext(configContext);
			try {
				ds.init();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}

			oc = new OptimizerContextImpl();
			oc.setConfigContext(configContext);
			try {
				oc.init();
			} catch (Exception e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
		}
	}

	public void initSpecQualifierMapping() {
		specMap.put("C-time", "Ctime");
		specMap.put("E-times", "Etimes");
		specMap.put("P-size", "Psize");
		specMap.put("R-size", "Rsize");
		specMap.put("C-200", "C200");
		specMap.put("C-302", "C302");
		specMap.put("c-other", "cother");
	}

	private final String insertPrefix = "insert into " + TABLENAME + " (";
	private final String insertValues = ") values(";
	private final String insertPostfix = ")";
	private final String rowKey = "rowkey";

	public void addRow(String rowkey, String qualifier, Object o) {
		StringBuilder sb = new StringBuilder();
		sb.append(insertPrefix);
		sb.append(rowKey);
		sb.append(",");
		sb.append(FAMILY);
		sb.append("$");
		sb.append(getQualifier(qualifier));
		sb.append(insertValues);
		sb.append("?");
		sb.append(",");
		sb.append("?");
		sb.append(insertPostfix);

		List<Object> args = new ArrayList<Object>();
		args.add(rowkey);
		args.add(o);
		ds.execute().execute(null, sb.toString(), args);
	}

	private String getQualifier(String qualifier) {
		String q = specMap.get(qualifier);
		return q == null ? qualifier : q;
	}

	public void addRow(String rowkey, String[] qualifier, Object[] o) {
		StringBuilder sb = new StringBuilder();
		sb.append(insertPrefix);
		sb.append(rowKey);
		sb.append(",");
		for (int i = 0; i < qualifier.length; i++) {
			sb.append(FAMILY);
			sb.append("$");
			sb.append(getQualifier(qualifier[i]));
			if (i < (qualifier.length - 1)) {
				sb.append(",");
			}
		}

		sb.append(insertValues);
		for (int i = 0; i < qualifier.length + 1; i++) {
			sb.append("?");
			if (i < (qualifier.length)) {
				sb.append(",");
			}
		}
		sb.append(insertPostfix);

		List<Object> args = new ArrayList<Object>();
		args.add(rowkey);
		for (Object x : o) {
			args.add(x);
		}
		ds.execute().execute(null, sb.toString(), args);
	}

	private final String selectPrefix = "select ";
	private final String selectFrom = " from " + TABLENAME;
	private final String selectSingleWhere = " where rowkey=?";

	@SuppressWarnings("unchecked")
	public <U> U queryCellValue(String rowkey, String qualifier, Class<U> c) {
		StringBuilder sb = new StringBuilder();
		sb.append(selectPrefix);
		sb.append(FAMILY);
		sb.append("$");
		sb.append(getQualifier(qualifier));
		sb.append(selectFrom);
		sb.append(selectSingleWhere);
		ResultCursor rc = ds.execute().execute(null, sb.toString(),
				Arrays.asList(rowkey));

		IRowSet pair = null;
		U value = null;
		try {
			while ((pair = rc.next()) != null) {
				Object val = rc.getIngoreTableName(pair, (FAMILY + "$"
						+ getQualifier(qualifier)).toUpperCase());
				value = (U) val;
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return value;
	}

	public HashMap<String, String> queryCellValue(String rowkey,
			String[] qualifierArray) {
		StringBuilder sb = new StringBuilder();
		sb.append(selectPrefix);
		for (int i = 0; i < qualifierArray.length; i++) {
			sb.append(FAMILY);
			sb.append("$");
			sb.append(getQualifier(qualifierArray[i]));
			if (i < (qualifierArray.length - 1)) {
				sb.append(",");
			}
		}
		sb.append(selectFrom);
		sb.append(selectSingleWhere);
		ResultCursor rc = ds.execute().execute(null, sb.toString(),
				Arrays.asList(rowkey));

		IRowSet pair = null;
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			while ((pair = rc.next()) != null) {
				for (int i = 0; i < qualifierArray.length; i++) {
					Object val = rc.getIngoreTableName(pair, (FAMILY + "$"
							+ getQualifier(qualifierArray[i])).toUpperCase());
					result.put(qualifierArray[i], String.valueOf(val));
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return result;
	}

	private final String selectRangeWhere = " where rowkey>=? and rowkey<=?";

	@SuppressWarnings("unchecked")
	public <U> List<U> queryValueList(String startId, String endId,
			final String qualifier, final Class<U> c) {
		StringBuilder sb = new StringBuilder();
		sb.append(selectPrefix);
		sb.append(FAMILY);
		sb.append("$");
		sb.append(getQualifier(qualifier));
		sb.append(selectFrom);
		sb.append(selectRangeWhere);
		ResultCursor rc = ds.execute().execute(null, sb.toString(),
				Arrays.asList(startId, endId));

		IRowSet pair = null;
		List<U> value = new ArrayList<U>();
		try {
			while ((pair = rc.next()) != null) {
				Object val = rc.getIngoreTableName(pair, (FAMILY + "$"
						+ getQualifier(qualifier)).toUpperCase());
				value.add((U) val);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public <U> Map<String, U> queryValueMap(String startId, String endId,
			final String qualifier, final Class<U> c) {
		StringBuilder sb = new StringBuilder();
		sb.append(selectPrefix);
		sb.append(rowKey);
		sb.append(",");
		sb.append(FAMILY);
		sb.append("$");
		sb.append(getQualifier(qualifier));
		sb.append(selectFrom);
		sb.append(selectRangeWhere);
		ResultCursor rc = ds.execute().execute(null, sb.toString(),
				Arrays.asList(startId, endId));

		IRowSet pair = null;
		Map<String, U> value = new HashMap<String, U>();
		try {
			while ((pair = rc.next()) != null) {
				Object val = rc.getIngoreTableName(pair, (FAMILY + "$"
						+ getQualifier(qualifier)).toUpperCase());
				Object rowKey = rc.getIngoreTableName(pair, "ROWKEY");
				value.put(String.valueOf(rowKey), (U) val);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return value;
	}

	public Map<String, Map<String, String>> queryValueMap(String startId,
			String endId, final String[] qualifiers) {
		StringBuilder sb = new StringBuilder();
		sb.append(selectPrefix);
		sb.append(rowKey);
		sb.append(",");
		for (int i = 0; i < qualifiers.length; i++) {
			sb.append(FAMILY);
			sb.append("$");
			sb.append(getQualifier(qualifiers[i]));
			if (i < (qualifiers.length - 1)) {
				sb.append(",");
			}
		}
		sb.append(selectFrom);
		sb.append(selectRangeWhere);
		ResultCursor rc = ds.execute().execute(null, sb.toString(),
				Arrays.asList(startId, endId));

		IRowSet pair = null;
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		try {
			while ((pair = rc.next()) != null) {
				HashMap<String, String> row = new HashMap<String, String>();
				for (int i = 0; i < qualifiers.length; i++) {
					Object val = rc.getIngoreTableName(pair, (FAMILY + "$"
							+ getQualifier(qualifiers[i])).toUpperCase());
					row.put(qualifiers[i], String.valueOf(val));
				}
				Object rowKey = rc.getIngoreTableName(pair, "ROWKEY");
				result.put(String.valueOf(rowKey), row);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return result;
	}
}
