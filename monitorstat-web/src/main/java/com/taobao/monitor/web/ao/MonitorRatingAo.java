
package com.taobao.monitor.web.ao;

import java.util.List;

import com.taobao.monitor.web.core.dao.impl.MonitorRatingDao;
import com.taobao.monitor.web.rating.RatingApp;
import com.taobao.monitor.web.rating.RatingIndicator;
import com.taobao.monitor.web.rating.RatingIndicatorValue;
import com.taobao.monitor.web.rating.RatingOptimizeRecord;
import com.taobao.monitor.web.rating.RatingOptimizeSolution;

/**
 *
 * @author xiaodu
 * @version 2010-4-19 上午11:36:00
 */
public class MonitorRatingAo {


	private static MonitorRatingAo  ao = new MonitorRatingAo();

	
	private MonitorRatingDao monitorRatingDao = new MonitorRatingDao();
	

	private MonitorRatingAo(){
				
		
	}


	public static  MonitorRatingAo get(){
		return ao;
	}


	
//	/**
//	 * 取得这个应用的所有评分信息
//	 * @param appId
//	 * @return
//	 */
//	public List<AppRating> findAppRating(int appId){
//		Calendar parseLogCalendar = Calendar.getInstance();
//		parseLogCalendar.add(Calendar.DATE, -1);
//		parseLogCalendar.set(Calendar.HOUR_OF_DAY, 0);
//		parseLogCalendar.set(Calendar.MINUTE, 0);
//		parseLogCalendar.set(Calendar.MILLISECOND, 0);
//		parseLogCalendar.set(Calendar.SECOND, 0);
//
//
//		return monitorRatingDao.findAppRating(appId,parseLogCalendar.getTime());
//	}


//	public String getAppRating(int appId){
//		Double sum = 0d;
//		List<AppRating> list = findAppRating(appId);
//
//		if(list.size()==0){
//			return null;
//		}
//
//		for(AppRating rating:list){
//			sum+=MarkGrade.gradeWithCv(rating.getRating(),rating.getRatingWeigth());
//		}
//		return Utlitites.formatDotTwo(sum);
//	}

    
    /**
	 * 
	 * @param indicator
	 */
	public void addRatingIndicator(RatingIndicator indicator){
		monitorRatingDao.addRatingIndicator(indicator);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<RatingApp> findAllRatingApp(){
		return monitorRatingDao.findAllRatingApp();
	}
	
	
	/**
	 * 
	 * @param indicator
	 */
	public void addRatingIndicatorValue(RatingIndicatorValue indicatorvalue){
		monitorRatingDao.addRatingIndicatorValue(indicatorvalue);
	}

	public List<RatingIndicatorValue> getRecentHealthIndexByAppId(int app_id){
		return monitorRatingDao.getRecentHealthIndexByAppId(app_id);
	}
	
	public List<RatingIndicatorValue> getHealthIndexByAppId(int app_id){
		return monitorRatingDao.getHealthIndexByAppId(app_id);
	}
	
	public void deleteRatingApp(int app_id) {
		monitorRatingDao.deleteRatingApp(app_id);
	}
	
	public void updateRatingApp(RatingIndicator indicator) {
		monitorRatingDao.updateRatingIndicator(indicator);
	}
	
	public List<RatingIndicator> getRatingIndicatorByAppId(int app_id) {
		return monitorRatingDao.getRatingIndicatorByAppId(app_id);
	}
	
	public void addRatingOptimizeRecord(RatingOptimizeRecord record){
		monitorRatingDao.addRatingOptimizeRecord(record);
	}
	
	public void updateRatingOptimizeRecord(RatingOptimizeRecord record){
		monitorRatingDao.updateRatingOptimizeRecord(record);
	}
	
	public List<RatingOptimizeRecord> getRatingOptimizeRecord(int app_id) {
		return monitorRatingDao.getRatingOptimizeRecord(app_id);
	}
	
	public RatingOptimizeRecord getRatingOptimizeRecordById(int id) {
		return monitorRatingDao.getRatingOptimizeRecordById(id);
	}

	public void deleteRatingOptimizeRecord(RatingOptimizeRecord record) {
		monitorRatingDao.deleteRatingOptimizeRecord(record);
	}
	
	public void addRatingOptimizeSolution(RatingOptimizeSolution solution) {
		monitorRatingDao.addRatingOptimizeSolution(solution);
	}
	
	public void updateRatingOptimizeSolution(RatingOptimizeSolution solution) {
		monitorRatingDao.updateRatingOptimizeSolution(solution);
	}
	
	public List<RatingOptimizeSolution> getRatingOptimizeSolution(int optimize_record_id) {
		return monitorRatingDao.getRatingOptimizeSolution(optimize_record_id);
	}

	public RatingOptimizeSolution getRatingOptimizeSolutionById(int id) {
		return monitorRatingDao.getRatingOptimizeSolutionById(id);
	}

	public void deleteRatingOptimizeSolution(RatingOptimizeSolution solution) {
		monitorRatingDao.deleteRatingOptimizeSolution(solution);
	}
	

	

	
	
}
