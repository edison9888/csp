package com.taobao.csp.capacity.model.month;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.equation.Equation;
import com.taobao.monitor.common.util.Arith;

/**
 * 
 * @author xiaodu
 * @version 2011-4-12 下午03:37:09
 */
public class BaseMonthTreandModel implements MonthTrendModel {
	private List<Coordinate> datas; // 原始数据

	private List<Coordinate> movingDatas;

	private Equation defaultEquation = null;

	public Equation getDefaultEquation() {
		return defaultEquation;
	}

	public void setDefaultEquation(Equation defaultEquation) {
		this.defaultEquation = defaultEquation;
	}

	/**
	 * 获取原始数据
	 * 
	 * @return
	 */
	public List<Coordinate> getDatas() {
		return datas;
	}

	public BaseMonthTreandModel(List<Coordinate> datas, Equation defaultEquation) {
		this.datas = datas;
		this.movingDatas = movingAverages(datas);
		this.defaultEquation = defaultEquation;
		model(this.movingDatas);
	}

	public void model(List<Coordinate> datas) {

		this.getDefaultEquation().compute(datas);
	}

	@Override
	public Double getY(int x) {
		return this.getDefaultEquation().getY(x);
	}

	/**
	 * 实现移动平均 ，利用12 作为移动周期
	 * 
	 * @param datas
	 *            list 必须等于23月
	 * @return 返回的是Double : double[0] = 移动平均后的月份号（同时也是模拟出来的曲线的x轴序列） 1.0~12.0
	 *         然后预测的下一年是从13.0开始的 double[1] = 移动平均后的y轴序列 double[2] = 波动系数
	 */
	public List<Coordinate> movingAverages(List<Coordinate> datas) {

		List<Coordinate> list = new ArrayList<Coordinate>();

		for (int i = datas.size() - 1; i > 0 && i >= datas.size() - 12; i--) { // 一共得到12个移动平均数
			Coordinate g = datas.get(i);
			List<Coordinate> sub = datas.subList(i - 11, i + 1); // 一共12个数字移动平均
			Double a = averages(sub);
			Coordinate r = new Coordinate();
			// r[0] = Double.parseDouble((datas.get(i)[0] + 11) + "");
			// //取出原来数据的x值加上11=得到模拟出来的曲线的x轴
			r.setX(g.getX() - 12) ; // 这是移动平均后关于波动系数的月份号，同时也是模拟出来的曲线的x轴序列，由于是以12作为周期的，所以当x
								// % 12为0的时候代表移动平均后的11月份， x%12 =
								// 1的时候代表是移动后的12月份，其他的代表取模后-1
			r.setY(a) ; // 移动平均后的值,也就是模拟曲线用的y轴
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
	 * 获取某个月份的波动系数
	 * 
	 * @param x
	 * @return
	 */
	public double getWaveRatio(int x) {
		if (x > 12) {
			x = x - 12;
		}
		Double d = movingDatas.get(12 - x).getY();
		Coordinate v = this.getDatas().get(this.getDatas().size() - 1 - (12 - x));

		return Arith.div(v.getY(), d);
	}

	/**
	 * 获取明年的12个月的值
	 * 
	 * @return 返回值为12个月的值 double[0] = 月份 ； double[1] = 月份对应的值
	 */
	public List<Coordinate> getFutureYear() {

		List<Coordinate> futrueList = new ArrayList<Coordinate>();
		for (int i = 13; i <= 24; i++) {
			Coordinate value = new Coordinate();
			value.setX( i - 12);
			value.setY(Arith.mul(getY(i), getWaveRatio(i))) ; // 预测值*波动系数
																	// //bodong这里是从0开始的
			futrueList.add(value);
			System.out.println(value.getX() + ":" + value.getY());
		}
		return futrueList;
	}

	// /**
	// * 获取此方程的 拟合度
	// * @return
	// */
	// public Double degreeOfFitting(){
	//	
	// double d = 0d;
	// double a = 0d;
	//		
	// double h = averages(getDatas());
	//		
	// for(Double[] l:getDatas()){
	// d = Arith.add(d, Arith.pow(Arith.sub(l[1], getY(l[0])), 2));
	// a = Arith.add(a, Arith.pow(Arith.sub(l[1], h), 2));
	// }
	// return Arith.sub(1, Arith.div(d, a));
	//	
	// }
	/**
	 * 获取此方程的 拟合度 是利用Theil不相等系数 U来表示 这里是用移动平均的到对应的年分的值和带入曲线拟合的值的一个比 u=
	 * [(∑(y-y^)2)(-2) / t]/[(∑(y)2)(-2)/t + (∑(y^)2)(-2)/t] t表示月份个数， y表示预测值，
	 * y^表示原始值， 数字代表次幂
	 * 
	 * @return
	 */
	public Double degreeOfFitting() {

		Double MSE = 0.0; // 平均预测误差平方和（mean squared error）:就是∑(y-y^)2开根号再除以个数
		Double chancha = 0.0; // 残差
		Double sumY = 0.0; // 保存真实y的值的平方和
		Double sumYPredict = 0.0; // 保存预测y的值的平方和

		List<Coordinate> list = movingAverages(getDatas());

		for (int i = 0; i < list.size(); i++) {

			// Double test = Arith.sub(y[i], cal(x[i],args));
			chancha = Arith.add(chancha, Arith.pow(Arith.sub(list.get(i).getY(), getY(Long.valueOf(list.get(i).getX()).intValue())), 2)); // ∑(y-y^)2
			sumY = Arith.add(sumY, Arith.pow(list.get(i).getY(), 2)); // 保存真实y的值的平方和
			sumYPredict = Arith.add(sumYPredict, getY(Long.valueOf(list.get(i).getX()).intValue() + 12));
		}

		MSE = Arith.div(chancha, list.size()); // ∑(y-y^)2开根号再除以个数
		// Theil不相等系数 U
		Double u = Arith.div(Math.sqrt(MSE), Arith.add(Math.sqrt(Arith.div(sumYPredict, list.size())), Math.sqrt(Arith
				.div(sumY, list.size()))));
		System.out.println("拟合程度（越靠近0越准确）：" + u);
		return u;

	}
}
