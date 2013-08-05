package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AnalyseClass;

/**
 * AnalyseClass的DAO
 * @author wuhaiqian.pt
 *
 */
public class AnalyseClassDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(AnalyseClassDao.class);

	public void addAnalyseClassData(int classId, String className, int classType) {
		try {

			String sql = "insert into MS_MONITOR_ANALYSE_CLASS "
					+ "(class_id, classname, class_tpye) values(?,?,?)";
			this.execute(sql, new Object[] {classId,className,classType});
		} catch (Exception e) {
			logger.error("addAnalyseClassData 出错,", e);
		}
	}
	
	/**
	 * 添加addAnalyseClassData
	 * @param analyseClass
	 */
	public void addAnalyseClassData(AnalyseClass analyseClass) {
		try {
			
			String sql = "insert into MS_MONITOR_ANALYSE_CLASS "
				+ "(class_id, classname, class_tpye) values(?,?,?)";
			this.execute(sql, new Object[] {analyseClass.getClassId(), analyseClass.getClassName(), analyseClass.getClassType()});
		} catch (Exception e) {
			logger.error("addAnalyseClassData 出错,", e);
		}
	}
	
	/**
	 * 删除AnalyseClass
	 * @param appId
	 */
	public void deleteanalyseClass(int classId) {
		String sql = "delete from MS_MONITOR_ANALYSE_CLASS where class_id=?";
		try {
			this.execute(sql, new Object[] { classId });
		} catch (SQLException e) {
			logger.error("deleteanalyseClass: ", e);
		}
	}

	/**
	 * 删除AnalyseClass
	 * @param analyseClass
	 */
	public void deleteTimeConfData(AnalyseClass analyseClass) {
		String sql = "delete from MS_MONITOR_ANALYSE_CLASS where class_id=?";
		try {
			this.execute(sql, new Object[] { analyseClass.getClassId() });
		} catch (SQLException e) {
			logger.error("deleteTimeConfData: ", e);
		}
	}
	
	/**
	 * 获取全部AnalyseClass
	 * 
	 * @return
	 */
	public List<AnalyseClass> findAllAnalyseClass() {
		String sql = "select * from MS_MONITOR_ANALYSE_CLASS";

		final List<AnalyseClass> analyseClassList = new ArrayList<AnalyseClass>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AnalyseClass po = new AnalyseClass();
					po.setClassId(rs.getInt("class_id"));
					po.setClassName(rs.getString("classname"));
					po.setClassType(rs.getInt("class_tpye"));
					analyseClassList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAnalyseClass:", e);
		}
		return analyseClassList;
	}
	
	/**
	 * 根据AnalyseClass更新
	 * @param analyseClass
	 * @return
	 */
	public boolean updateAnalyseClass(AnalyseClass analyseClass){
		String sql = "update MS_MONITOR_ANALYSE_CLASS set classname=?,class_tpye=? where class_id=? ";
		try {
			this.execute(sql, new Object[]{analyseClass.getClassName(), analyseClass.getClassType(), analyseClass.getClassId()});
		} catch (SQLException e) {
			logger.error("updateAnalyseClass: ", e);
			return false;
		}
		return true;
	}
	
	
	
	
	
}
