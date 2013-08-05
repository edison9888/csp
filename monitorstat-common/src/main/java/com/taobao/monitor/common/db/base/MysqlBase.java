//package com.taobao.monitor.common.db.base;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Types;
//import java.util.Date;
//
//import org.apache.commons.dbcp.BasicDataSource;
//import org.apache.log4j.Logger;
//
//
//
///**
// * 
// * @author xiaodu
// * @version 2010-1-7 下午04:33:47
// */
//public class MysqlBase {
//	
//	private static  Logger log = Logger.getLogger(MysqlBase.class);
//	private BasicDataSource dataSource;
//	private String dbUrl ;
//	
//	
//	
//	protected MysqlBase(String url,String name,String psw){
//		dataSource = new BasicDataSource();
//		dataSource.setDriverClassName("org.gjt.mm.mysql.Driver");
//		dataSource.setUrl(url);
//		dataSource.setUsername(name);
//		dataSource.setPassword(psw);
//		dataSource.setDefaultAutoCommit(false);
//		dataSource.setMaxActive(1);		
//		this.dbUrl = url;	
//		log.info("连接mysql"+url+"");
//		
//	}
//	
//	protected MysqlBase(String url,String name,String psw,int maxActive){
//		dataSource = new BasicDataSource();
//		dataSource.setDriverClassName("org.gjt.mm.mysql.Driver");
//		dataSource.setUrl(url);
//		dataSource.setUsername(name);
//		dataSource.setPassword(psw);
//		dataSource.setDefaultAutoCommit(false);
//		dataSource.setMaxActive(maxActive);		
//		this.dbUrl = url;		
//		log.info("连接mysql"+url+"");
//		
//	}
//	
//	
//	protected Connection getConnection() throws SQLException{
//		return dataSource.getConnection();
//		
//	}
//	
//	protected void execute(String sql) throws SQLException{		
//		execute(sql,null);
//	}
//	
//	
//	protected void query(String sql,SqlCallBack callBack) throws Exception{
//		query(sql,null,callBack);
//	}
//	
//	protected float getFloatValue(String sql,Object[] params) throws SQLException{
//		log.debug(sql);
//		Connection conn = getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try{
//			ps = conn.prepareStatement(sql);			
//			setParmas(ps,params);				
//			rs = ps.executeQuery();
//			if(rs.next()){
//				return rs.getFloat(1);
//			}			
//			
//		}finally{			
//			close(conn,ps,rs);
//		}
//		
//		return 0;
//	}
//	
//	protected long getLongValue(String sql) throws SQLException{
//		log.debug(sql);
//		Connection conn = getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try{
//			ps = conn.prepareStatement(sql);			
//						
//			rs = ps.executeQuery();
//			if(rs.next()){
//				return rs.getLong(1);
//			}			
//			
//		}finally{			
//			close(conn,ps,rs);
//		}
//		
//		return 0;
//	}
//	protected long getLongValue(String sql,Object[] params) throws SQLException{
//		log.debug(sql);
//		Connection conn = getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try{
//			ps = conn.prepareStatement(sql);			
//			setParmas(ps,params);				
//			rs = ps.executeQuery();
//			if(rs.next()){
//				return rs.getLong(1);
//			}			
//			
//		}finally{			
//			close(conn,ps,rs);
//		}
//		
//		return 0;
//	}
//	
//	protected int getIntValue(String sql,Object[] params) throws SQLException{
//		log.debug(sql);
//		Connection conn = getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try{
//			ps = conn.prepareStatement(sql);			
//			setParmas(ps,params);			
//			rs = ps.executeQuery();
//			if(rs.next()){
//				return rs.getInt(1);
//			}			
//			
//		}finally{			
//			close(conn,ps,rs);
//		}
//		
//		return 0;
//	}
//	
//	protected Date getDateValue(String sql) throws SQLException{
//		log.debug(sql);
//		Connection conn = getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try{
//			ps = conn.prepareStatement(sql);			
//						
//			rs = ps.executeQuery();
//			if(rs.next()){				
//				return new Date(rs.getTimestamp(1).getTime());
//			}
//		}finally{			
//			close(conn,ps,rs);
//		}
//		
//		return null;
//	}
//	
//	protected Date getDateValue(String sql,Object[] params) throws SQLException{
//		log.debug(sql);
//		Connection conn = getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try{
//			ps = conn.prepareStatement(sql);			
//			setParmas(ps,params);						
//			rs = ps.executeQuery();
//			if(rs.next()){				
//				return new Date(rs.getTimestamp(1).getTime());
//			}
//		}finally{			
//			close(conn,ps,rs);
//		}
//		return null;
//	}
//	
//	protected void query(String sql,Object[] params,SqlCallBack callBack) throws Exception{
//		log.debug(sql);
//		Connection conn = getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		
//		try{
//			ps = conn.prepareStatement(sql);			
//			setParmas(ps,params);
//			
//			rs = ps.executeQuery();
//			
//			while(rs.next()){
//				callBack.readerRows(rs);
//			}
//		}finally{			
//			close(conn,ps,rs);
//		}
//	}
//	private void close(Connection conn,PreparedStatement ps,ResultSet rs) throws SQLException{
//		if(rs!=null){
//			try{
//				rs.close();
//			}catch(Exception e){
//				log.warn("关闭 ResultSet 出错", e);
//			}
//		}
//		if(ps!=null){
//			try{
//				ps.close();
//			}catch(Exception e){
//				log.warn("关闭preparestatement 出错", e);
//			}
//		}
//		if(conn!=null){
//			try{
//				conn.commit();
//			}catch(Exception e){
//				log.warn("commit connection 出错", e);
//			}
//			try{
//				conn.close();
//			}catch(Exception e){
//				log.warn("close connection 出错", e);
//			}
//		}
//		
//		
//	}
//	
//	protected void execute(String sql,Object[] parms) throws SQLException{		
//		log.debug(sql);		
//		Connection conn = getConnection();
//		PreparedStatement ps = null;
//		try{
//			ps = conn.prepareStatement(sql);
//			
//			setParmas(ps,parms);
//			
//			ps.execute();
//		}finally{
//			close(conn,ps,null);
//		}
//		
//	}		
//	private void setParmas(PreparedStatement ps,Object[] parms ) throws SQLException{		
//		if(parms!=null){
//			int index = 1;
//			for(Object obj:parms){
//				if(obj instanceof String){
//					ps.setString(index, (String)obj);
//				}else if(obj instanceof Long){
//					ps.setLong(index, (Long)obj);
//				}else if(obj instanceof Integer){
//					ps.setInt(index, (Integer)obj);
//				}else if(obj instanceof Double){
//					ps.setDouble(index, (Double)obj);
//				}else if(obj instanceof Float){
//					ps.setDouble(index, (Float)obj);
//				}else if(obj instanceof Date){
//					Date d = (Date)obj;
//					java.sql.Timestamp sqlDate = new java.sql.Timestamp(d.getTime());
//					ps.setTimestamp(index, sqlDate);
//				}else{
//					ps.setNull(index, Types.NULL);
//				}
//				index++;
//			}			
//		}
//		
//	}
//	
//	protected void finalize() throws Throwable {
//		super.finalize();		
//		if(dataSource!=null){
//			dataSource.close();
//		}
//	}
//	protected interface SqlCallBack{		
//		public void readerRows(ResultSet rs)throws Exception;
//		
//	}
//	
//	
//	public void close(){
//		try {
//			dataSource.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	
//}