//
//package com.taobao.csp.capacity.model.qps;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import com.taobao.csp.capacity.ao.CapacityAo;
//import com.taobao.csp.capacity.model.Coordinate;
//import com.taobao.csp.capacity.model.equation.Equation;
//import com.taobao.csp.capacity.po.CapacityAppPo;
//import com.taobao.csp.capacity.po.PvcountPo;
//import com.taobao.monitor.common.po.AppInfoPo;
//import com.taobao.monitor.common.util.Arith;
//import com.taobao.monitor.web.ao.MonitorDayAo;
//import com.taobao.monitor.web.cache.AppCache;
//import com.taobao.monitor.common.po.KeyValuePo;
//
///**
// * qps 构建模型采用将 最近的60天的pv数据与高峰期的qps平均数据 通过线性拟合获取他的 公式
// * 
// * @author xiaodu
// * @version 2011-4-19 上午09:06:44
// */
//public class QpsModel implements Equation{
//	
//	/**
//	 * 采用的方程式
//	 */
//	private Equation equation;
//	/**
//	 * 采集数据的开始时间
//	 */
//	private Date endDataCollectTime;
//	
//	private Date startDataCollectTime;
//	
//	private int appId;
//	
//	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//	
//	public Equation getEquation() {
//		return equation;
//	}
//
//	public int getAppId() {
//		return appId;
//	}
//
//	public QpsModel(int appId,Date date,Equation equation){
//		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		cal.add(Calendar.DAY_OF_MONTH, -180);
//		
//		this.equation = equation;
//		this.endDataCollectTime = date;
//		this.startDataCollectTime = cal.getTime();
//		this.appId = appId;
//		Map<String,Double> mapPv = collectPv();
//		Map<String,Double> mapQps = collectQps();
//		Map<String,Double> mapMachine = collectMachineNumber();
//		List<Coordinate> listC = new ArrayList<Coordinate>();
//		
//		for(Map.Entry<String,Double> entry:mapPv.entrySet()){
//			String key = entry.getKey();
//			Double pv = entry.getValue();
//			Double qps = mapQps.get(key);
//			Double machine = mapMachine.get(key);
//			if(pv!=null&&qps!=null&&machine!=null ){
//				Coordinate c = new Coordinate();
//				c.setX(pv.longValue());
//				c.setY(Arith.mul(qps, machine));
//				listC.add(c);
//			}
//			
//		}
//		
//		
//		for(Coordinate c:listC){
//			System.out.println(c.getX()+":"+c.getY());
//		}
//		
//		compute(listC);
//		
//		
//	}
//	
//	
//	private Map<String,Double> collectPv(){
//		
//		List<PvcountPo> listPv = CapacityAo.get().findAllByAppId(this.startDataCollectTime,this.endDataCollectTime,  this.appId);
//		Map<String,Double> mappv = new HashMap<String, Double>();
//		
//		for(PvcountPo po:listPv){
//			mappv.put(sdf.format(po.getCollectTime()), po.getPvCount());
//		}
//		return mappv;
//	}
//	
//	private Map<String,Double> collectQps(){
//		
//		CapacityAppPo cApp = CapacityAo.get().getCapacityApp(this.appId);
//		int keyId = 16931;
//		if("hsf".equals(cApp.getAppType())){
//			keyId = 27734;
//		}
//		
//		
//		AppInfoPo appinfo = AppCache.get().getKey(this.appId);
//		
//		
//		List<KeyValuePo> listQps = MonitorDayAo.get().findMonitorCountByDate(appinfo.getAppDayId(), keyId, this.startDataCollectTime,this.endDataCollectTime);
//		
//		Map<String,Double> mapQps = new HashMap<String, Double>();
//		for(KeyValuePo po:listQps){
//			mapQps.put(sdf.format(po.getCollectTime()), Double.parseDouble(po.getValueStr()));
//		}
//		
//		return mapQps;
//	}
//	
//	
//	private Map<String ,Double> collectMachineNumber(){
//		AppInfoPo appinfo = AppCache.get().getKey(this.appId);
//		return MonitorDayAo.get().findAppMachineNumber(appinfo.getAppDayId(), this.startDataCollectTime,this.endDataCollectTime);
//	}
//	
//
//	
//	@Override
//	public double getY(long x) {
//		return equation.getY(x);
//	}
//
//
//	@Override
//	public void compute(List<Coordinate> datas) {
//		equation.compute(datas);
//	}
//	
//	
//
//}
