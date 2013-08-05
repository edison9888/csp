//
//package com.taobao.csp.capacity.model.day;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.taobao.csp.capacity.NongliCalendar;
//import com.taobao.csp.capacity.ao.CapacityAo;
//import com.taobao.csp.capacity.model.Coordinate;
//import com.taobao.csp.capacity.model.TrendModel;
//import com.taobao.csp.capacity.model.equation.Equation;
//import com.taobao.csp.capacity.po.PvcountPo;
//import com.taobao.monitor.common.util.Arith;
//
///**
// * 
// * @author xiaodu
// * @version 2011-4-6 上午11:12:22
// */
//public class NewDayTrendModel  implements DayTrendModel,TrendModel{
//	
//	private List<PvcountPo> originalDayList;//原始数据数据，在日模型总中原始数据 为一个365天的list数据 
//	private int appId;
//	
//	public int getAppId() {
//		return appId;
//	}
//	public List<PvcountPo> getOriginalDayList() {
//		return originalDayList;
//	}
//	
//	public NewDayTrendModel(int appId,Equation defaultEquation){
//		this.appId = appId;
//		this.defaultEquation = defaultEquation;
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DAY_OF_YEAR, -7);
//		Date end = cal.getTime();
//		cal.add(Calendar.DAY_OF_YEAR, -7-365-365);
//		Date start = cal.getTime();
//		this.originalDayList = CapacityAo.get().findAllByAppId(start, end, appId);
//		
//		pretreatmentData();
//		
//		
//		
//		this.movingDatas = movingAverages(this.datas);
//		
//		for(PvcountPo po:this.getOriginalDayList()){
//			originalDayMap.put(format.format(po.getCollectTime()), po);
//			
//			nongli.setTime(po.getCollectTime());
//			System.out.println(nongli.getyyyyMMdd()+"农历");
//			originalNongliDayMap.put(nongli.getyyyyMMdd(), po);
//			
//			
//		}
//		
//		
//		
//		
//	}
//	
//	private Map<String,PvcountPo> originalDayMap = new HashMap<String, PvcountPo>();//阳历 
//	private Map<String,PvcountPo> originalNongliDayMap = new HashMap<String, PvcountPo>();//阴历
//	
//	private List<Coordinate> movingDatas;
//	
//	private List<Coordinate> datas; // 原始数据
//	
//	
//	private Equation defaultEquation = null; 
//	
//	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//	
//	private SimpleDateFormat monthformat = new SimpleDateFormat("yyyyMMdd");
//	
//	private Calendar cal = Calendar.getInstance();
//	
//	private NongliCalendar nongli = NongliCalendar.getInstance();
//	
//	
//	public List<Coordinate> getMovingDatas() {
//		return movingDatas;
//	}
//	
//	public List<Coordinate> getDatas() {
//		return datas;
//	}
//	
//	public Equation getDefaultEquation() {
//		return defaultEquation;
//	}
//	public void setDefaultEquation(Equation defaultEquation) {
//		this.defaultEquation = defaultEquation;
//	}
//	
//	public List<Coordinate> movingAverages(List<Coordinate> datas) {
//		
//		List<Coordinate> list = new ArrayList<Coordinate>();
//		
//		int x = 365;
//		for (int i = datas.size() - 1; i > 0 && i >= datas.size() - 365; i--) { // 一共得到365个移动平均数
//			Coordinate g = datas.get(i);
//			List<Coordinate> sub = datas.subList(i - 364, i + 1); // 一共12个数字移动平均
//			Double a = averages(sub);
//			Coordinate r = new Coordinate();
//			// r[0] = Double.parseDouble((datas.get(i)[0] + 11) + "");
//			// //取出原来数据的x值加上11=得到模拟出来的曲线的x轴
//			r.setX(x--) ; // 这是移动平均后关于波动系数的月份号，同时也是模拟出来的曲线的x轴序列，由于是以365作为周期的，所以当x
//			r.setY(a) ; // 移动平均后的值,也就是模拟曲线用的y轴
//			list.add(r);
//		}
//		return list;
//	}
//	
//	
//	private Double averages(List<Coordinate> datas) {
//		Double s = 0.0;
//		for (Coordinate l : datas) {
//			s =Arith.add(l.getY(), s) ;
//		}
//		return Arith.div(s, datas.size());
//
//	}
//	
//	
//	
//	/**
//	 * 预处理数据，将数据中的一些问题噪点数据去除
//	 */
//	private List<Coordinate> pretreatmentData(){
//		
//		Collections.sort(this.getOriginalDayList());
//		List<Coordinate> cList = new ArrayList<Coordinate>();
//		
//		int i=0;
//		
//		for(PvcountPo po:this.getOriginalDayList()){
//			Coordinate c = new Coordinate();
//			c.setX(i++);
//			c.setY(po.getPvCount());
//			c.setTmpValue(po.getCollectTime().getTime());
//			cList.add(c);
//		}
//		return cList;
//	}
//	@Override
//	public List<Coordinate> getFurureMonth() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public List<Coordinate> getFutureDay() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public Double degreeOfFitting() {
//		return null;
//	}
//	@Override
//	public double getWaveRatio(int x) {
//		
//		
//		return 0;
//	}
//	@Override
//	public Double getY(int x) {
//		return defaultEquation.getY(x);
//	}
//	@Override
//	public void model(List<Coordinate> datas) {
//		defaultEquation.compute(this.movingDatas);
//	}
//	
//	
//
//
//
//
//
//
//}
