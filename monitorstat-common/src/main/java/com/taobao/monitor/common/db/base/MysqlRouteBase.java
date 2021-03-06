package com.taobao.monitor.common.db.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;



/**
 * 
 * @author xiaodu
 * @version 2010-1-7 下午04:33:47
 */
public class MysqlRouteBase {
	
	private static  Logger log = Logger.getLogger(MysqlRouteBase.class);
	
	private DbRoute defaultDbRoute = null;
	
	public MysqlRouteBase(){
	}
	public MysqlRouteBase(DbRoute defaultDbRoute){
		this.defaultDbRoute = defaultDbRoute;
	}
	private DbRoute getDefaultDbRoute(){
		if(defaultDbRoute == null){
			log.debug("get default  defaultDbRoute ");
			return DbRouteManage.get().getMainDBRoute();
		}else{
			log.debug("get  defaultDbRoute :"+defaultDbRoute.getDbRouteId());
		}
		return defaultDbRoute;
	}
	
	
	protected Connection getConnection(DbRoute dbRoute) throws SQLException{
		if(dbRoute==null){
			throw new SQLException("不存在 dbRoute");
		}
		log.debug("get dbRoute :"+dbRoute.getDbRouteId());
		return dbRoute.getDataSource().getConnection();
		
	}
	
	protected void execute(String sql,DbRoute dbRoute) throws SQLException{		
		execute(sql,null,dbRoute);
	}
	protected void execute(String sql) throws SQLException{		
		execute(sql,null,getDefaultDbRoute());
	}
	
	protected void query(String sql,SqlCallBack callBack,DbRoute dbRoute) throws Exception{
		query(sql,null,callBack,dbRoute);
	}
	
	protected void query(String sql,SqlCallBack callBack) throws Exception{
		query(sql,null,callBack,getDefaultDbRoute());
	}
	
	protected float getFloatValue(String sql,Object[] params,DbRoute dbRoute) throws SQLException{
		log.debug(sql);
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);			
			setParmas(ps,params);				
			rs = ps.executeQuery();
			if(rs.next()){
				return rs.getFloat(1);
			}			
			
		}finally{			
			close(conn,ps,rs);
		}
		
		return 0;
	}
	
	protected float getFloatValue(String sql,Object[] params) throws SQLException{
				
		return getFloatValue(sql,params,getDefaultDbRoute());
	}
	
	
	protected long getLongValue(String sql,DbRoute dbRoute) throws SQLException{
		log.debug(sql);
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);			
						
			rs = ps.executeQuery();
			if(rs.next()){
				return rs.getLong(1);
			}			
			
		}finally{			
			close(conn,ps,rs);
		}
		
		return 0;
	}
	
	protected long getLongValue(String sql) throws SQLException{
		
		return getLongValue(sql,getDefaultDbRoute());
	}
	
	protected long getLongValue(String sql,Object[] params,DbRoute dbRoute) throws SQLException{
		log.debug(sql);
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);			
			setParmas(ps,params);				
			rs = ps.executeQuery();
			if(rs.next()){
				return rs.getLong(1);
			}			
			
		}finally{			
			close(conn,ps,rs);
		}
		
		return 0;
	}
	
	protected long getLongValue(String sql,Object[] params) throws SQLException{
				
		return getLongValue(sql,params,getDefaultDbRoute());
	}
	
	protected int getIntValue(String sql,Object[] params,DbRoute dbRoute) throws SQLException{
		log.debug(sql);
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);			
			setParmas(ps,params);			
			rs = ps.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}			
			
		}finally{			
			close(conn,ps,rs);
		}
		
		return -1;
	}
	
	protected int getIntValue(String sql,Object[] params) throws SQLException{
		
		return getIntValue(sql,params,getDefaultDbRoute());
	}
	
	protected Date getDateValue(String sql,DbRoute dbRoute) throws SQLException{
		log.debug(sql);
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);			
						
			rs = ps.executeQuery();
			if(rs.next()){				
				return new Date(rs.getTimestamp(1).getTime());
			}
		}finally{			
			close(conn,ps,rs);
		}
		
		return null;
	}
	protected Date getDateValue(String sql) throws SQLException{
		return getDateValue(sql,getDefaultDbRoute());
	}
	protected Date getDateValue(String sql,Object[] params,DbRoute dbRoute) throws SQLException{
		log.debug(sql);
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			setParmas(ps,params);						
			rs = ps.executeQuery();
			if(rs.next()){	
				if(rs.getTimestamp(1) == null){
					return null;
				}
				return new Date(rs.getTimestamp(1).getTime());
			}
		}finally{			
			close(conn,ps,rs);
		}
		return null;
	}
	
	protected Date getDateValue(String sql,Object[] params) throws SQLException{
		return getDateValue(sql,params,getDefaultDbRoute());
	}
	
	
	
	protected String getString(String sql,Object[] params,DbRoute dbRoute) throws SQLException{
		log.debug(sql);
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			setParmas(ps,params);						
			rs = ps.executeQuery();
			if(rs.next()){				
				return rs.getString(1);
			}
		}finally{			
			close(conn,ps,rs);
		}
		return null;
	}
	
	
	protected String getString(String sql,Object[] params) throws SQLException{
		return getString(sql,params,getDefaultDbRoute());
	}
	
	
	
	protected void query(String sql,Object[] params,SqlCallBack callBack,DbRoute dbRoute) throws Exception{
		log.debug(sql);
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			ps = conn.prepareStatement(sql);			
			setParmas(ps,params);
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				callBack.readerRows(rs);
			}
		}finally{			
			close(conn,ps,rs);
		}
	}
	
	protected void query(String sql,Object[] params,SqlCallBack callBack) throws Exception{
		query(sql,params,callBack,getDefaultDbRoute());
	}
	
	private void close(Connection conn,PreparedStatement ps,ResultSet rs) throws SQLException{
		if(rs!=null){
			try{
				rs.close();
			}catch(Exception e){
				log.warn("关闭 ResultSet 出错", e);
			}
		}
		if(ps!=null){
			try{
				ps.close();
			}catch(Exception e){
				log.warn("关闭preparestatement 出错", e);
			}
		}
		if(conn!=null){
			try{
				conn.commit();
			}catch(Exception e){
				log.warn("commit connection 出错", e);
			}
			try{
				conn.close();
			}catch(Exception e){
				log.warn("close connection 出错", e);
			}
		}
		
		
	}
	
	protected void execute(String sql,Object[] parms,DbRoute dbRoute) throws SQLException{		
		log.debug(sql);		
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement(sql);
			
			setParmas(ps,parms);
			
			ps.execute();
		}finally{
			close(conn,ps,null);
		}
		
	}
	
	protected void executeBatch(String sql,List<Object[]> parms,DbRoute dbRoute)throws SQLException{
		if(parms==null||parms.size()==0){
			return ;
		}
		Connection conn = getConnection(dbRoute);
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement(sql);
			
			for(Object[] objs:parms){
				setParmas(ps,objs);
				ps.addBatch();
			}
			
			
			
			ps.executeBatch();
		}finally{
			close(conn,ps,null);
		}
	}
	
	/**
	 * add by 中亭批量插入
	 * @param sql
	 * @param parms
	 * @throws SQLException
	 */
	protected void executeBatch(String sql,List<Object[]> parms)throws SQLException{
		if(parms==null||parms.size()==0){
			return ;
		}
		Connection conn = getConnection(this.getDefaultDbRoute());
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement(sql);
			
			for(Object[] objs:parms){
				setParmas(ps,objs);
				ps.addBatch();
			}
			ps.executeBatch();
		}finally{
			close(conn,ps,null);
		}
	}	
	
	protected void execute(String sql,Object[] parms) throws SQLException{		
		execute(sql,parms,getDefaultDbRoute());		
	}
	
	
	private void setParmas(PreparedStatement ps,Object[] parms ) throws SQLException{		
		if(parms!=null){
			int index = 1;
			for(Object obj:parms){
				if(obj instanceof String){
					ps.setString(index, (String)obj);
				}else if(obj instanceof Long){
					ps.setLong(index, (Long)obj);
				}else if(obj instanceof Integer){
					ps.setInt(index, (Integer)obj);
				}else if(obj instanceof Double){
					ps.setDouble(index, (Double)obj);
				}else if(obj instanceof Float){
					ps.setDouble(index, (Float)obj);
				}else if(obj instanceof Date){
					Date d = (Date)obj;
					java.sql.Timestamp sqlDate = new java.sql.Timestamp(d.getTime());
					ps.setTimestamp(index, sqlDate);
				}else{
					ps.setNull(index, Types.NULL);
				}
				index++;
			}			
		}
		
	}
	
	protected interface SqlCallBack{		
		public void readerRows(ResultSet rs)throws Exception;
		
	}
}