
package com.taobao.monitor.common.db.impl.other;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.po.TradeVo;
import com.taobao.monitor.common.util.Utlitites;



/**
 * +-------------+------------+------+-----+---------------------+-------+
| Field       | Type       | Null | Key | Default             | Extra |
+-------------+------------+------+-----+---------------------+-------+
| tradecount  | int(11)    | NO   |     | 0                   |       | 
| tradeamount | double     | NO   |     | 0                   |       | 
| tradetime   | datetime   | NO   | PRI | 0000-00-00 00:00:00 |       | 
| biz_type    | tinyint(4) | NO   | PRI | 0                   |       | 
| countday    | int(11)    | YES  |     | NULL                |       | 
| amountday   | int(11)    | YES  |     | NULL                |       | 
| gmt_create  | timestamp  | NO   |     | CURRENT_TIMESTAMP   |       | 
+-------------+------------+------+-----+---------------------+-------+
7 rows in set (0.00 sec)

tradecount    与上一时间段内交易比数
tradecount    与上一时间段内交易金额
tradetime     交易时间
countday      当天比数累计值
amountday     当天金额累计值
gmt_create    数据创建时间


+----------     +
| biz_type   业务类型   |
+-------------- +
|        0  c2c | 
|        1  b2c | 
|        2  wap | 
|        9  3c  | 
|      100 全网 | 
+---------------+

 * @author xiaodu
 * @version 2010-4-29 下午07:39:52
 */
public class HaBoDao  extends MysqlRouteBase {
	public HaBoDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_HaBo"));
	}
	
	
	/**
	 * 取得全网交易总额
	 * @param startDate  yyyy-MM-dd
	 * @return
	 */
	public long sumAllamount(String startDate){
		
		String sql ="select amount from tc_alipay_sum where check_day = ?";
		
		try {
			return this.getLongValue(sql,new Object[]{startDate});
		} catch (Exception e) {
		}
		
		return 0;
	}

	
	
	/**
	 * 根据数据获取 交易记录
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<KeyValuePo>> findTradeByCollectTime(int type,Date startTime,Date endTime) throws Exception{
		
		String sql = "select tradecount,tradeamount,tradetime,biz_type from tc_create_sum_new where biz_type=? and tradetime between ? and ?";
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		
		final Map<String, List<KeyValuePo>> poMap = new HashMap<String, List<KeyValuePo>>();
		
		this.query(sql,new Object[]{type,startTime,endTime}, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {
				
				String tradeamount = rs.getString("tradeamount");
				String tradecount = rs.getString("tradecount");
				
				Timestamp collectTime = rs.getTimestamp("tradetime");
				Date date = new Date(collectTime.getTime());
				
				{
				List<KeyValuePo> poList = poMap.get("订单数["+parseLogFormatDate.format(date)+"]");
				
				if(poList==null){
					poList = new ArrayList<KeyValuePo>();
					poMap.put("订单数["+parseLogFormatDate.format(date)+"]", poList);
				}
				
				KeyValuePo po = new KeyValuePo();
				po.setCollectTime(date);
				po.setValueStr(tradecount);
				poList.add(po);
				}
				{
				List<KeyValuePo> poList = poMap.get("交易额["+parseLogFormatDate.format(date)+"]");
				
				if(poList==null){
					poList = new ArrayList<KeyValuePo>();
					poMap.put("交易额["+parseLogFormatDate.format(date)+"]", poList);
				}
				
				KeyValuePo po = new KeyValuePo();
				po.setCollectTime(date);
				po.setValueStr(tradeamount);
				poList.add(po);
				}
				
			}});
		
		
		return poMap;
	}
	
	/**
	 * 根据时间获取数据   
	 * @param collectTimeList  存放 yyyy-MM-dd HH:mm:ss 时间格式的
	 * @return
	 * @throws Exception
	 */
	public Map<String,TradeVo> findTradeByTime(List<String> collectTimeList) throws Exception{
		
		String sql = "select tradecount,tradeamount,tradetime,biz_type,countday,amountday from tc_create_sum_new where tradetime " +
				"in("+Utlitites.formatArray2Sqlin(collectTimeList.toArray(new String[]{}))+")";
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		
		final Map<String,TradeVo> voMap = new HashMap<String, TradeVo>();
		
		this.query(sql, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {

				
				Timestamp collectTime = rs.getTimestamp("tradetime");
				String time = parseLogFormatDate.format(new Date(collectTime.getTime()));
				
				TradeVo inner = voMap.get(time);
				if(inner==null){
					inner =  new TradeVo();
					voMap.put(time, inner);
				}
				inner.setCollectTime(time);
				inner.setDate(new Date(collectTime.getTime()));
				
				String biz_type = rs.getString("biz_type");
				String tradeamount = rs.getString("tradeamount");
				String tradecount = rs.getString("tradecount");
				String amount = rs.getString("amountday");
				String count = rs.getString("countday");
				int biz_type_num = Integer.parseInt(biz_type);
				
				if(biz_type_num==100){
					inner.setTraderAmount100(tradeamount);
					inner.setTraderCount100(tradecount);
					inner.setAmountday100(amount);
					inner.setCountday100(count);
				}
				if(biz_type_num==0){
					inner.setTraderAmount0(tradeamount);
					inner.setTraderCount0(tradecount);
					inner.setAmountday0(amount);
					inner.setCountday0(count);
				}
				if(biz_type_num==1){
					inner.setTraderAmount1(tradeamount);
					inner.setTraderCount1(tradecount);
					inner.setAmountday1(amount);
					inner.setCountday1(count);
				}
				if(biz_type_num==2){
					inner.setTraderAmount2(tradeamount);
					inner.setTraderCount2(tradecount);
					inner.setAmountday2(amount);
					inner.setCountday2(count);
				}
				if(biz_type_num==2){
					inner.setTraderAmount9(tradeamount);
					inner.setTraderCount9(tradecount);
					inner.setAmountday9(amount);
					inner.setCountday9(count);
				}
				
			}});
		
		return voMap;
	}
	/**
	 * 
	 * 查询返回一个list 类型的 TradeVo 按照时间降序
	 *    TRADE_AMOUNT_
	 *    TRADE_COUNT_
	 * @param limit 
	 * @return
	 * @throws Exception
	 */
	public List<TradeVo>  findTradeLimitByOrder(int limit) throws Exception{
		
		String sql = "select tradecount,tradeamount,tradetime,biz_type,countday,amountday from tc_create_sum_new order by tradetime desc limit "+(limit);
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		
		final Map<String,TradeVo> voMap = new HashMap<String,TradeVo>();
		
		this.query(sql, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {
				
				Timestamp collectTime = rs.getTimestamp("tradetime");
				String time = parseLogFormatDate.format(new Date(collectTime.getTime()));
				
				TradeVo inner = voMap.get(time);
				if(inner==null){
					inner =  new TradeVo();
					voMap.put(time, inner);
				}
				inner.setCollectTime(time);
				inner.setDate(new Date(collectTime.getTime()));
				
				String biz_type = rs.getString("biz_type");
				String tradeamount = rs.getString("tradeamount");
				String tradecount = rs.getString("tradecount");
				String amount = rs.getString("amountday");
				String count = rs.getString("countday");
				int biz_type_num = Integer.parseInt(biz_type);
				
				if(biz_type_num==100){
					inner.setTraderAmount100(tradeamount);
					inner.setTraderCount100(tradecount);
					inner.setAmountday100(amount);
					inner.setCountday100(count);
				}
				if(biz_type_num==0){
					inner.setTraderAmount0(tradeamount);
					inner.setTraderCount0(tradecount);
					inner.setAmountday0(amount);
					inner.setCountday0(count);
				}
				if(biz_type_num==1){
					inner.setTraderAmount1(tradeamount);
					inner.setTraderCount1(tradecount);
					inner.setAmountday1(amount);
					inner.setCountday1(count);
				}
				if(biz_type_num==2){
					inner.setTraderAmount2(tradeamount);
					inner.setTraderCount2(tradecount);
					inner.setAmountday2(amount);
					inner.setCountday2(count);
				}
				if(biz_type_num==2){
					inner.setTraderAmount9(tradeamount);
					inner.setTraderCount9(tradecount);
					inner.setAmountday9(amount);
					inner.setCountday9(count);
				}
			}});
		
		List<TradeVo> list = new ArrayList<TradeVo>();
		list.addAll(voMap.values());		
		Collections.sort(list);				
		return list;
	}
	
	
	
	
	
	
	/**
	 * 
	 * 查询返回一个list 类型的 TradeVo 按照时间降序
	 *    TRADE_AMOUNT_
	 *    TRADE_COUNT_
	 * @param limit 
	 * @return
	 * @throws Exception
	 */
	public Map<String,TradeVo>  findTcCreateSumNew(Date start,Date end,int biz_type) {
		
		String sql = "select tradecount,tradeamount,tradetime,biz_type,countday,amountday " +
				" from tc_create_sum_new where tradetime >= ? and tradetime <= ? and biz_type =?";
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		
		final Map<String,TradeVo> voMap = new HashMap<String,TradeVo>();
		
		try {
			this.query(sql,new Object[]{start,end,biz_type}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					
					Timestamp collectTime = rs.getTimestamp("tradetime");
					String time = parseLogFormatDate.format(new Date(collectTime.getTime()));
					
					TradeVo inner = voMap.get(time);
					if(inner==null){
						inner =  new TradeVo();
						voMap.put(time, inner);
					}
					inner.setCollectTime(time);
					inner.setDate(new Date(collectTime.getTime()));
					
					int biz_type_num = rs.getInt("biz_type");
					double tradeamount = rs.getDouble("tradeamount");
					double tradecount = rs.getDouble("tradecount");
					inner.setTraderAmount(tradeamount);
					inner.setTraderCount(tradecount);
				}});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return voMap;
	}
	
	public Map<String,TradeVo>  findTcTransactionSum(Date start,Date end,int biz_type) {
		
		String sql = "select tradecount,tradeamount,tradetime,biz_type " +
				" from tc_transaction_sum where tradetime >= ? and tradetime <= ? and biz_type =?";
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		
		final Map<String,TradeVo> voMap = new HashMap<String,TradeVo>();
		
		try {
			this.query(sql,new Object[]{start,end,biz_type}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					
					Timestamp collectTime = rs.getTimestamp("tradetime");
					String time = parseLogFormatDate.format(new Date(collectTime.getTime()));
					
					TradeVo inner = voMap.get(time);
					if(inner==null){
						inner =  new TradeVo();
						voMap.put(time, inner);
					}
					inner.setCollectTime(time);
					inner.setDate(new Date(collectTime.getTime()));
					
					int biz_type_num = rs.getInt("biz_type");
					double tradeamount = rs.getDouble("tradeamount");
					double tradecount = rs.getDouble("tradecount");
					inner.setTraderAmount(tradeamount);
					inner.setTraderCount(tradecount);
				}});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return voMap;
	}
	
	
	
	public Map<String,TradeVo>  findTcTransactionSumLimit(Date start,Date end,int biz_type,int limit) {
		
		String sql = "select tradecount,tradeamount,tradetime,biz_type " +
				" from tc_transaction_sum where tradetime >= ? and tradetime <= ? and  biz_type =? ";
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		final Map<String,TradeVo> voMap = new HashMap<String,TradeVo>();
		try {
			this.query(sql,new Object[]{start,end,biz_type}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					
					Timestamp collectTime = rs.getTimestamp("tradetime");
					String time = parseLogFormatDate.format(new Date(collectTime.getTime()));
					
					TradeVo inner = voMap.get(time);
					if(inner==null){
						inner =  new TradeVo();
						voMap.put(time, inner);
					}
					inner.setCollectTime(time);
					inner.setDate(new Date(collectTime.getTime()));
					
					int biz_type_num = rs.getInt("biz_type");
					double tradeamount = rs.getDouble("tradeamount");
					double tradecount = rs.getDouble("tradecount");
					inner.setTraderAmount(tradeamount);
					inner.setTraderCount(tradecount);
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return voMap;
	}
	
	

}
