
package com.taobao.csp.capacity.model.day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.csp.capacity.NongliCalendar;
import com.taobao.csp.capacity.filter.AverFilter;
import com.taobao.csp.capacity.filter.IfilterBreak;
import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.TrendModel;
import com.taobao.csp.capacity.model.equation.Equation;
import com.taobao.csp.capacity.po.PvcountPo;
import com.taobao.monitor.common.util.Arith;

/**
 * 
 * @author xiaodu
 * @version 2011-4-14 ����11:18:50
 */
public class BaseDayTrendModel extends AbstractDayTrendModel implements TrendModel{
	
	private Map<String,PvcountPo> originalDayMap = new HashMap<String, PvcountPo>();//���� 
	private Map<String,PvcountPo> originalNongliDayMap = new HashMap<String, PvcountPo>();//����
	
	private List<Coordinate> movingDatas;
	
	private List<Coordinate> datas; // ԭʼ����
	
	
	private Equation defaultEquation = null; 
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	
	private SimpleDateFormat monthformat = new SimpleDateFormat("yyyyMMdd");
	
	private Calendar cal = Calendar.getInstance();
	
	private NongliCalendar nongli = NongliCalendar.getInstance();
	
	
	public List<Coordinate> getMovingDatas() {
		return movingDatas;
	}
	
	public List<Coordinate> getDatas() {
		return datas;
	}
	
	public Equation getDefaultEquation() {
		return defaultEquation;
	}
	public void setDefaultEquation(Equation defaultEquation) {
		this.defaultEquation = defaultEquation;
	}
	
	
//	public BaseDayTrendModel(int appId,int year,Equation defaultEquation){
//		super(appId,year);
//		this.defaultEquation = defaultEquation;
//		this.datas = pretreatmentData();
//		this.movingDatas = movingAverages(this.datas);
//		
//		for(PvcountPo po:this.getOriginalDayList()){
//			originalDayMap.put(format.format(po.getCollectTime()), po);
//			
//			nongli.setTime(po.getCollectTime());
//			System.out.println(nongli.getyyyyMMdd()+"ũ��");
//			originalNongliDayMap.put(nongli.getyyyyMMdd(), po);
//			
//			
//		}
//		
//		model(this.movingDatas);
//	}
	
	/**
	 * ���캯��������Ϊ365���ԭʼ����
	 * @param days
	 */
	public BaseDayTrendModel(int appId,int year,List<PvcountPo> days,Equation defaultEquation){
		super(appId,year,days);
		this.defaultEquation = defaultEquation;
		this.datas = pretreatmentData();
		this.movingDatas = movingAverages(this.datas);
		for(PvcountPo po:this.getOriginalDayList()){
			originalDayMap.put(format.format(po.getCollectTime()), po);
			System.out.println(nongli.getyyyyMMdd()+"ũ��");
			originalNongliDayMap.put(nongli.getyyyyMMdd(), po);
		}
		model(this.movingDatas);
	}
	
	@Override
	public List<Coordinate> movingAverages(List<Coordinate> datas) {
		
		List<Coordinate> list = new ArrayList<Coordinate>();
		
		// ����С������Ͳ�Ԥ���ˣ���׼
		if(datas.size() < 730) {
			return list;
		}
		for (int i = datas.size() - 1; i > 0 && i >= datas.size() - 365; i--) { // һ���õ�365���ƶ�ƽ����
			Coordinate g = datas.get(i);
			List<Coordinate> sub = datas.subList(i - 364, i + 1); // һ��12�������ƶ�ƽ��
			Double a = averages(sub);
			Coordinate r = new Coordinate();
			// r[0] = Double.parseDouble((datas.get(i)[0] + 11) + "");
			// //ȡ��ԭ�����ݵ�xֵ����11=�õ�ģ����������ߵ�x��
			r.setX(g.getX() - 365) ; // �����ƶ�ƽ������ڲ���ϵ�����·ݺţ�ͬʱҲ��ģ����������ߵ�x�����У���������365��Ϊ���ڵģ����Ե�x
			r.setY(a) ; // �ƶ�ƽ�����ֵ,Ҳ����ģ�������õ�y��
			list.add(r);
		}
		return list;
	}
	
	
	private Double averages(List<Coordinate> datas) {
		Double s = 0.0;
		for (Coordinate l : datas) {
			s =Arith.add(l.getY(), s) ;
		}
		return Arith.div(s, datas.size());

	}
	
	
	
	/**
	 * Ԥ�������ݣ��������е�һЩ�����������ȥ��
	 */
	private List<Coordinate> pretreatmentData(){
		
		
		List<Coordinate> cList = new ArrayList<Coordinate>();
		
		Calendar cal = Calendar.getInstance();
		
		IfilterBreak filterBreak = new AverFilter();
		List<PvcountPo> list = filterBreak.filter(this.getOriginalDayList());
		Collections.sort(list);
		int minYear = 9999;
		
		for(PvcountPo po:list){
			cal.setTime(po.getCollectTime());
			int year = cal.get(Calendar.YEAR);
			if(minYear >year){
				minYear = year;
			}
		}
		
		for(PvcountPo po:list){
			cal.setTime(po.getCollectTime());
			int year = cal.get(Calendar.YEAR);
			int offsetYear = year - minYear;
			int offsetDay = 0;
			if((year%400 == 0) || ((year % 4 == 0) && (year % 100 != 0))){//����
				int m = cal.get(Calendar.MONTH)+1;
				int o = cal.get(Calendar.DAY_OF_MONTH)+1;
				if(m == 2 && o == 29){
					continue;
					
				}
				if(m >2){
					offsetDay = -1;
				}
				
			}
			int d = cal.get(Calendar.DAY_OF_YEAR);
			int x = offsetYear*365+d+offsetDay;
			offsetDay = 0;
			
			
			Coordinate c = new Coordinate();
			c.setX(x);
			c.setY(po.getPvCount());
			c.setTmpValue(po.getCollectTime().getTime());
			
			cList.add(c);
			
		}
		
		
		return cList;
	}
	
	
	
	
	@Override
	public Double degreeOfFitting() {
		Double MSE = 0.0; // ƽ��Ԥ�����ƽ���ͣ�mean squared error��:���ǡ�(y-y^)2�������ٳ��Ը���
		Double chancha = 0.0; // �в�
		Double sumY = 0.0; // ������ʵy��ֵ��ƽ����
		Double sumYPredict = 0.0; // ����Ԥ��y��ֵ��ƽ����

		List<Coordinate> list = movingAverages(getDatas());

		for (int i = 0; i < list.size(); i++) {

			// Double test = Arith.sub(y[i], cal(x[i],args));
			chancha = Arith.add(chancha, Arith.pow(Arith.sub(list.get(i).getY(), getY(Long.valueOf(list.get(i).getX()).intValue())), 2)); // ��(y-y^)2
			sumY = Arith.add(sumY, Arith.pow(list.get(i).getY(), 2)); // ������ʵy��ֵ��ƽ����
			sumYPredict = Arith.add(sumYPredict, getY(Long.valueOf(list.get(i).getX()).intValue() + 12));
		}

		MSE = Arith.div(chancha, list.size()); // ��(y-y^)2�������ٳ��Ը���
		// Theil�����ϵ�� U
		Double u = Arith.div(Math.sqrt(MSE), Arith.add(Math.sqrt(Arith.div(sumYPredict, list.size())), Math.sqrt(Arith
				.div(sumY, list.size()))));
		System.out.println("��ϳ̶ȣ�Խ����0Խ׼ȷ����" + u);
		return u;
	}



	@Override
	public double getWaveRatio(int x) {
		
		return getWaveRatio(this.getYear(),x);
	}
	
	
	
	
	
	
	public double getWaveRatio(int year,int days) {		
		
		
		if (days > 365) {
			days = days % 365 ==0?365:days % 365;
		}
		
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_YEAR, computeDay(year,days));
		
		nongli.setTime(cal.getTime());
		PvcountPo po = null;
				
		//Ԥ����Ĺ���ʱ��
		if((nongli.getMonth()==1 && nongli.getDay()<=10)||(nongli.getMonth()==12 && nongli.getDay() >=20 )){
			po = originalNongliDayMap.get(nongli.getYear()-1+""+nongli.getMMdd());
			if(po == null){				
				po = originalNongliDayMap.get(nongli.getYear()-2+""+nongli.getMMdd());				
			}
		}
		
		if(po == null){
			int WEEK_OF_YEAR = cal.get(Calendar.WEEK_OF_YEAR);
			int DAY_OF_WEEK = cal.get(Calendar.DAY_OF_WEEK);
			
			
			cal.set(Calendar.YEAR, this.getYear()-1);
			cal.set(Calendar.WEEK_OF_YEAR, WEEK_OF_YEAR);
			cal.set(Calendar.DAY_OF_WEEK, DAY_OF_WEEK);
			
			if(cal.get(Calendar.YEAR) >this.getYear()-1){
				cal.add(Calendar.DAY_OF_MONTH, -7);
			}
			
			nongli.setTime(cal.getTime());
			if((nongli.getMonth()==1 && nongli.getDay()<=10)){
				cal.add(Calendar.DAY_OF_MONTH, 14);
			}else if((nongli.getMonth()==12 && nongli.getDay() >=20 )){
				cal.add(Calendar.DAY_OF_MONTH, -14);
			}
			nongli.setTime(cal.getTime());
			String tmp = format.format(cal.getTime());
			po = originalDayMap.get(tmp);
		}
		
		Double d = 0d;
		if (movingDatas.size() > (365 - days)) {
			d = movingDatas.get(365 - days).getY();
		}
		if(po != null && d != 0)
			return Arith.div(po.getPvCount(), d);
		return 0;
	}
	
	public Coordinate getFutureDay(int year,int x) {
		
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_YEAR, computeDay(year,x));
		
		int xTemp = (year-this.getYear()+1)*365+x;
		Coordinate value = new Coordinate();
		value.setX( Integer.parseInt(format.format(cal.getTime())));
		value.setY(Arith.mul(getY(xTemp), getWaveRatio(year,x))) ; // Ԥ��ֵ*����ϵ��
		
		return value;
	}



	@Override
	public Double getY(int x) {
		return this.getDefaultEquation().getY(x);
	}
	@Override
	public List<Coordinate> getFurureMonth() {
		
		cal.set(Calendar.YEAR, this.getYear());
		
		List<Coordinate> monthlist = new ArrayList<Coordinate>();
		
		Map<Integer, Coordinate> monthMap = new HashMap<Integer, Coordinate>();
		
		List<Coordinate> daylist = getFutureDay();
		
		for(Coordinate c:daylist){
			cal.set(Calendar.DAY_OF_YEAR, Long.valueOf(c.getX()).intValue());
			int m = cal.get(Calendar.MONTH);
			
			Coordinate t = monthMap.get(m);
			if(t == null){
				t = new Coordinate();
				monthMap.put(m, t);
			}
			t.setX(Integer.parseInt(monthformat.format(cal.getTime())));
			t.setY(Arith.add(t.getX(), c.getY()));
			
		}
		monthlist.addAll(monthMap.values());
		return monthlist;
	}
	
	
	
	
	@Override
	public List<Coordinate> getFutureDay() {
		List<Coordinate> futrueList = new ArrayList<Coordinate>();
		for(int i=1;i<=365;i++){
			Coordinate value = new Coordinate();
			value.setX( computeCalDayStr(computeDay(this.getYear(),i) ));
			value.setY(Arith.mul(getY(i+365), getWaveRatio(i))) ; // Ԥ��ֵ*����ϵ��
			futrueList.add(value);
			System.out.println(value.getX() + ":" + value.getY());
		}
		return futrueList;
	}
	
	
	private int computeCalDayStr(int x){
		cal.set(Calendar.YEAR, this.getYear());
		cal.set(Calendar.DAY_OF_YEAR, x);
		return Integer.parseInt(format.format(cal.getTime()));
		
	}
	
	
	private int computeDay(int year,int x){
		if((year%400 == 0) || ((year % 4 == 0) && (year % 100 != 0))){//����
			if(x >=60){
				x+=1;
			}
		}
		return x;
	}
	
	
	
	@Override
	public void model(List<Coordinate> datas) {
		
		defaultEquation.compute(datas);
		
	}

	

}
