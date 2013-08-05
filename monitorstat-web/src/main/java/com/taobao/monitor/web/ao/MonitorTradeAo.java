
package com.taobao.monitor.web.ao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.core.dao.impl.HaBoDao;
import com.taobao.monitor.web.core.dao.impl.TcReportDao;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.web.vo.TradeVo;

/**
 * 
 * @author xiaodu
 * @version 2010-10-25 上午10:50:18
 */
public class MonitorTradeAo {
	
	private static final Logger logger =  Logger.getLogger(MonitorTradeAo.class);
	
	
	private HaBoDao haBoDao = new HaBoDao();
	
	private TcReportDao tcReport = new TcReportDao();
	
	private static MonitorTradeAo ao = new MonitorTradeAo();
	
	
	private MonitorTradeAo(){
		
	}
	
	public static MonitorTradeAo get(){
		return ao;
	}
	
	
	/**
	 * 取得全网交易总额
	 * @param startDate  yyyy-MM-dd
	 * @return
	 */
	public long sumAllamountByHabo(String startDate){
		return haBoDao.sumAllamount(startDate);
	}
	
	public long sumAllAmountByTc(String startDate){
		return tcReport.sumAllamount(startDate);
	}
	
	
	/******************交易直接查询哈勃的库**************************/


	public Map<String,TradeVo> findTradeByTime(List<TradeVo>  tradeVoList){
		try {

			SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			List<String> collectTimeList = new ArrayList<String>();
			Calendar cal = Calendar.getInstance();
			for(TradeVo vo:tradeVoList){
				cal.setTime(vo.getDate());
				cal.add(Calendar.DAY_OF_MONTH, -7);
				collectTimeList.add(parseLogFormatDate.format(cal.getTime()));
			}


			return haBoDao.findTradeByTime(collectTimeList);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;


	}


	/**
	 * 查询交易记录最近的几条数据
	 *
	 * @param limit
	 * @return
	 */
	public List<TradeVo>  findTradeByLimit(int limit){
		try {
			List<TradeVo> timeVomap = haBoDao.findTradeLimitByOrder(limit);
			return timeVomap;
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<TradeVo>();
	}

	/**
	 * 根据数据获取 交易记录
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<KeyValuePo>> findTradeByCollectTime(int type,Date startTime,Date endTime) {
		try {
			return haBoDao.findTradeByCollectTime(type, startTime, endTime);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	
	public List<TradeVo[]> findTcCreateAndPay(Date start,Date end,int biz_type){
		Map<String,TradeVo> createMap = haBoDao.findTcCreateSumNew(start, end,biz_type);
		Map<String,TradeVo> transactionMap =haBoDao.findTcTransactionSum(start, end,biz_type);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		
		List<TradeVo[]> list = new ArrayList<TradeVo[]>();
		
		for(int i=0;i<10;i++){
			String time = sdf.format(cal.getTime());
			cal.add(Calendar.MINUTE, -1);
			
			if(createMap.get(time)!=null&&transactionMap.get(time)!=null){
				TradeVo[] v = new TradeVo[2];
				v[0] = createMap.get(time);
				v[1] = transactionMap.get(time);
				list.add(v);
			}
		}				
		return list;
	}
	
	
	public List<TradeVo[]> findTcCreateAndPay_new(Date start,Date end,int biz_type,int limit){
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(end);
		cal.add(Calendar.HOUR_OF_DAY, -2);
		Map<String,TradeVo> createMap = haBoDao.findTcCreateSumNew(cal.getTime(),end,biz_type);
		Map<String,TradeVo> transactionMap =haBoDao.findTcTransactionSumLimit(start,end, biz_type,limit);
		List<TradeVo> listTs = new ArrayList<TradeVo>();
		listTs.addAll(transactionMap.values());
		Collections.sort(listTs);
		List<TradeVo[]> tsAndCreateList = new ArrayList<TradeVo[]>();
		
		for(int i=0;i<listTs.size()-1&&i<limit;i++){
			
			TradeVo[] tsandCreate = new TradeVo[2];
			TradeVo vo = listTs.get(i);
			TradeVo create = new TradeVo();
			create.setCollectTime(vo.getCollectTime());
			create.setDate(vo.getDate());
			tsandCreate[1]=vo;
			tsandCreate[0]=create;
			cal.setTime(vo.getDate());
			TradeVo voNext = listTs.get(i+1);
			do{
				String time = sdf.format(vo.getDate());
				TradeVo tmp = createMap.get(time);
				if(tmp != null){
					create.setTraderAmount(tmp.getTraderAmount()+create.getTraderAmount());
					create.setTraderCount(tmp.getTraderCount()+create.getTraderCount());
				}
				cal.add(Calendar.MINUTE, -1);
			}while(voNext.getDate().before(cal.getTime()));
			
			tsAndCreateList.add(tsandCreate);
		}
		return tsAndCreateList;
	}
	
	
	
	
	public Map<String,TradeVo> findTcCreateSumNew(Date start,Date end,int biz_type){
		return haBoDao.findTcCreateSumNew(start, end,biz_type);
	}
	
	public Map<String,TradeVo> findTcTransactionSum(Date start,Date end,int biz_type){
		return haBoDao.findTcTransactionSum(start, end,biz_type);
	}
}
