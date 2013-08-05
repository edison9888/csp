package com.taobao.csp.day.dao;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.taobao.csp.day.base.DataType;
import com.taobao.csp.day.config.DatabaseInfo;
import com.taobao.csp.day.po.RecordPo;
import com.taobao.csp.day.tddl.TDDL;
import com.taobao.csp.day.tddl.TddlLogKey;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/***
 * log分析到的位置dao
 * 
 * @author youji.zj
 *
 */
public class RecordDao extends MysqlRouteBase  {
	
	public static Logger logger = Logger.getLogger(RecordDao.class);
	
	public RecordDao () {
		super(DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN_DAY));
	}
	
	public RecordPo checkExist(RecordPo po) {
		final List<RecordPo> list = new ArrayList<RecordPo>();
		
		String sql = "select * from ms_monitor_log_record where app_name=? and ip_addr=? and data_type=? and log_path=?";
		try {
			this.query(sql, new Object[]{ po.getAppName(), po.getIp(), po.getDataType().toString(), 
					po.getLogPath() }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					RecordPo fPo = new RecordPo();
					fPo.setId(rs.getString("id"));
					fPo.setAppName(rs.getString("app_name"));
					fPo.setIp(rs.getString("ip_addr"));
					fPo.setDataType(DataType.valueOf(rs.getString("data_type")));
					fPo.setLogPath(rs.getString("log_path"));
					fPo.setPosition(rs.getLong("pos"));
					fPo.setUpdateTime(rs.getTimestamp("update_time"));
					
					list.add(fPo);
				}}, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (Exception e) {
			logger.error(e);
		}
		
		return list.isEmpty() ? null : list.get(0);
	}
	
	public void addRecord(RecordPo record) {
		try {
			String sql = "insert into ms_monitor_log_record (id,app_name,ip_addr,data_type,log_path,pos,update_time) values(?,?,?,?,?,?,?)";
			this.execute(sql, new Object [] {
					UUID.randomUUID().toString(), record.getAppName(), record.getIp(), record.getDataType().toString(),
					record.getLogPath(), record.getPosition(), record.getUpdateTime()
			}, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (SQLException e) {
			logger.error(e);
		}
	}
	
	public void updateRecord(RecordPo record) {
		try {
			String sql = "update ms_monitor_log_record set pos=?,update_time=? where id=?";
			this.execute(sql, new Object [] {
					record.getPosition(), record.getUpdateTime(), record.getId()
			}, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (SQLException e) {
			logger.error(e);
		}
	}
	
	public void addSerialIp(String ip, DataType dataType) {
		try {
			String sql = "insert into ms_monitor_serial_ip (ip,data_type,serial_id) select ?,?,? from dual where not exists " +
					" (select * from ms_monitor_serial_ip where ip = ? and data_type=?)";
			this.execute(sql, new Object [] {
					ip, dataType.toString(), null, ip, dataType.toString()
			}, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	
	public String findSerialId(String ip, DataType dataType) {
		final StringBuffer sb = new StringBuffer();
		
		String sql = "select * from ms_monitor_serial_ip where ip=? and data_type=?";
		try {
			this.query(sql, new Object[]{ ip, dataType }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String serialId = rs.getString("serial_id");
					if (StringUtils.isNotEmpty(serialId)) {
						sb.append(serialId);
					}
				}}, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (Exception e) {
			logger.error(e);
		}
		
		return StringUtils.isEmpty(sb.toString()) ? null : sb.toString();
	}
	
	public void addSerialData(byte[] data) {

			Connection conn = null;
			PreparedStatement ps = null;
			try{
				String sql = "insert into ms_monitor_serial_data (id,serial_data) values (?,?)";
				conn = getConnection(DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
				ps = conn.prepareStatement(sql);
				
				ps.setString(1, UUID.randomUUID().toString());
				ps.setBytes(2, data);
				
				ps.execute();
			} catch (Exception e) {
				logger.error(e);
			}finally{
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						logger.error(e);
					}
				}
				if (conn != null) {
					try {
						conn.commit();
						conn.close();
					} catch (SQLException e) {
						logger.error(e);
					}
				}
			}
	}
	
	public void updateSerialData(String id, byte[] data) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			String sql = "update ms_monitor_serial_data set serial_data=? where id=?";
			conn = getConnection(DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
			ps = conn.prepareStatement(sql);
			
			ps.setBytes(1, data);
			ps.setString(2, id);
			
			ps.execute();
		} catch (Exception e) {
			logger.error(e);
		}finally{
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
			if (conn != null) {
				try {
					conn.commit();
					conn.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}
	
	public Blob findSerialData(String id) {
		final List<Blob> list = new ArrayList<Blob>();
		
		String sql = "select * from ms_monitor_serial_data where id=?";
		try {
			this.query(sql, new Object[]{ id}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Blob serialBlob = rs.getBlob("serial_data");
					
					list.add(serialBlob);
				}}, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (Exception e) {
			logger.error(e);
		}
		
		return list.size() == 0 ? null : list.get(0);
	}
	
	/***
	 * 测试代码,后续抽取到单元测试中去
	 * @param args
	 */
	public static void main(String [] args) throws Exception {
		RecordDao dao = new RecordDao();
		
		// 测试查找ip所分配到的序列化data的id
		dao.addSerialIp("127.0.0.1", DataType.TDDL);
		System.out.println(dao.findSerialId("127.0.0.1", DataType.TDDL));
		
		Kryo kryo = new Kryo();
		
		// 测试插入序列化
//		ByteArrayOutputStream   bos   =   new   ByteArrayOutputStream(); 
//		Output output = new Output(bos);
//		Map<String, Map<TddlLogKey, TDDL>> cacheDay = new ConcurrentHashMap<String, Map<TddlLogKey, TDDL>>();
//		Map<TddlLogKey, TDDL> valueMap = new ConcurrentHashMap<TddlLogKey, TDDL>();
//		for (int i =0; i <= 1; i++) {
//			TddlLogKey key = new TddlLogKey(String.valueOf(i), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaadddddddddddddddddddddddddddddddddddddddddd" +
//					"dsadaadsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaaaaaaaaaaaaa", "aaa", "aaa", "aaa", "aaa", "aaa");
//			TDDL tddl = new TDDL();
//			tddl.setExeCount(100);
//			tddl.setMaxRespDate("dsadaaaaaaaaaaaaaaaa");
//			tddl.setMaxRespDate("dsadaaaaaaaaaaaaaaaaaaaa");
//			tddl.setMaxResp(10);
//			valueMap.put(key, tddl);
//		}
//		cacheDay.put("111", valueMap);
//		kryo.writeObject(output, cacheDay);
//		output.close();
//		dao.addSerialData(bos.toByteArray());
		
		// 测试查找序列化
		Blob serialBlob = dao.findSerialData("0434646f-34d5-4fed-833a-6c46db434704");
		InputStream inputStream = serialBlob.getBinaryStream();
		Input input = new Input(inputStream);
							
		@SuppressWarnings("unchecked")
		Map<String, Map<TddlLogKey, TDDL>> someObject1 = kryo.readObject(input,ConcurrentHashMap.class);
		input.close();
		for (Map.Entry<String, Map<TddlLogKey, TDDL>> entry : someObject1.entrySet()) {
			String time = entry.getKey();
			System.out.println(time);
			Map<TddlLogKey, TDDL> val = entry.getValue();
			
			for (Map.Entry<TddlLogKey, TDDL> entry2 : val.entrySet()) {
				TddlLogKey key1 = entry2.getKey();
				TDDL tddls = entry2.getValue();
				System.out.println(key1.getAppName());
				System.out.println(tddls.getMaxRespDate());
			}
		}
		
		// 测试更新序列化
//		dao.updateSerialData("0434646f-34d5-4fed-833a-6c46db434704", bos.toByteArray());
	}
	
}
