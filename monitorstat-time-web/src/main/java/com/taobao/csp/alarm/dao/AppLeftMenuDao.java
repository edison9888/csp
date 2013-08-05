package com.taobao.csp.alarm.dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.time.web.po.LeftMenuPo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class AppLeftMenuDao extends MysqlRouteBase {
	private static Logger logger = Logger.getLogger(AppLeftMenuDao.class);
	public boolean update(LeftMenuPo po){
		boolean flag = true;
		String sql = "delete from csp_time_app_leftmenu_info where app_name = ? ";
		try {
			this.execute(sql,new Object[]{po.getAppName()});
		} catch (SQLException e) {
			logger.info(e);
		}
		insert(po);
		return flag;
	}
	public boolean insert(LeftMenuPo po) {
		boolean flag = true;
		String str= "";
		Class clazz = po.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Integer i=1;i<fields.length;i++){
			Field field = fields[i];
			field.setAccessible(true);
			boolean value = false;
			try {
				value = field.getBoolean(po);
			} catch (Exception e) {
			}
			if(value==false)str+=0;
			else str+=1;
		}
		String sql = "insert into csp_time_app_leftmenu_info(app_name,flag) values(?,?)";
		try {
			this.execute(
					sql,
					new Object[] { po.getAppName(),
							str});
		} catch (SQLException e) {
			logger.info(e);
			return false;
		}
		return flag;
	}

	public List<LeftMenuPo> find(final String appName) {
		final List<LeftMenuPo> list = new ArrayList<LeftMenuPo>();
		String sql = "select * from csp_time_app_leftmenu_info where app_name = ?";
		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					LeftMenuPo po = new LeftMenuPo();
					Class clazz = po.getClass();
					Field[] fields = clazz.getDeclaredFields();
					po.setAppName(appName);
					String flag = rs.getString("flag");
					char[] flags = flag.toCharArray();
					for(Integer i=0;i<flags.length;i++){
						char ch = flags[i];
						fields[i+1].setAccessible(true);
						if(ch == '1'){
							fields[i+1].setBoolean(po, true);
						}else{
							fields[i+1].setBoolean(po, false);
						}
					}
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.info(e);
		}
		return list;
	}

}
