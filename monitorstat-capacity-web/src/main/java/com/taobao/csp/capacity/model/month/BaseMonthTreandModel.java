package com.taobao.csp.capacity.model.month;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.equation.Equation;
import com.taobao.monitor.common.util.Arith;

/**
 * 
 * @author xiaodu
 * @version 2011-4-12 ����03:37:09
 */
public class BaseMonthTreandModel implements MonthTrendModel {
	private List<Coordinate> datas; // ԭʼ����

	private List<Coordinate> movingDatas;

	private Equation defaultEquation = null;

	public Equation getDefaultEquation() {
		return defaultEquation;
	}

	public void setDefaultEquation(Equation defaultEquation) {
		this.defaultEquation = defaultEquation;
	}

	/**
	 * ��ȡԭʼ����
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
	 * ʵ���ƶ�ƽ�� ������12 ��Ϊ�ƶ�����
	 * 
	 * @param datas
	 *            list �������23��
	 * @return ���ص���Double : double[0] = �ƶ�ƽ������·ݺţ�ͬʱҲ��ģ����������ߵ�x�����У� 1.0~12.0
	 *         Ȼ��Ԥ�����һ���Ǵ�13.0��ʼ�� double[1] = �ƶ�ƽ�����y������ double[2] = ����ϵ��
	 */
	public List<Coordinate> movingAverages(List<Coordinate> datas) {

		List<Coordinate> list = new ArrayList<Coordinate>();

		for (int i = datas.size() - 1; i > 0 && i >= datas.size() - 12; i--) { // һ���õ�12���ƶ�ƽ����
			Coordinate g = datas.get(i);
			List<Coordinate> sub = datas.subList(i - 11, i + 1); // һ��12�������ƶ�ƽ��
			Double a = averages(sub);
			Coordinate r = new Coordinate();
			// r[0] = Double.parseDouble((datas.get(i)[0] + 11) + "");
			// //ȡ��ԭ�����ݵ�xֵ����11=�õ�ģ����������ߵ�x��
			r.setX(g.getX() - 12) ; // �����ƶ�ƽ������ڲ���ϵ�����·ݺţ�ͬʱҲ��ģ����������ߵ�x�����У���������12��Ϊ���ڵģ����Ե�x
								// % 12Ϊ0��ʱ������ƶ�ƽ�����11�·ݣ� x%12 =
								// 1��ʱ��������ƶ����12�·ݣ������Ĵ���ȡģ��-1
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
	 * ��ȡĳ���·ݵĲ���ϵ��
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
	 * ��ȡ�����12���µ�ֵ
	 * 
	 * @return ����ֵΪ12���µ�ֵ double[0] = �·� �� double[1] = �·ݶ�Ӧ��ֵ
	 */
	public List<Coordinate> getFutureYear() {

		List<Coordinate> futrueList = new ArrayList<Coordinate>();
		for (int i = 13; i <= 24; i++) {
			Coordinate value = new Coordinate();
			value.setX( i - 12);
			value.setY(Arith.mul(getY(i), getWaveRatio(i))) ; // Ԥ��ֵ*����ϵ��
																	// //bodong�����Ǵ�0��ʼ��
			futrueList.add(value);
			System.out.println(value.getX() + ":" + value.getY());
		}
		return futrueList;
	}

	// /**
	// * ��ȡ�˷��̵� ��϶�
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
	 * ��ȡ�˷��̵� ��϶� ������Theil�����ϵ�� U����ʾ ���������ƶ�ƽ���ĵ���Ӧ����ֵ�ֵ�ʹ���������ϵ�ֵ��һ���� u=
	 * [(��(y-y^)2)(-2) / t]/[(��(y)2)(-2)/t + (��(y^)2)(-2)/t] t��ʾ�·ݸ����� y��ʾԤ��ֵ��
	 * y^��ʾԭʼֵ�� ���ִ������
	 * 
	 * @return
	 */
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
}
