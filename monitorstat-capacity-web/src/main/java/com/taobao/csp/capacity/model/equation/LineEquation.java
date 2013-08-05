
package com.taobao.csp.capacity.model.equation;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.monitor.common.util.Arith;

/**
 * 线性 模型方程
 * @author xiaodu
 * @version 2011-4-12 下午01:23:04
 */
public class LineEquation implements Equation{
	
	private double a; //常数
	
	private double b;//常数
	
	
	
	public  Double xy(List<Coordinate> list){
		Double s = 0.0;
		for(Coordinate l:list){
			s=Arith.add(s, Arith.mul(l.getX(), l.getY()));
		}
		
		return s;
	}
	
	public  Double x2(List<Coordinate> list){
		Double s = 0.0;
		for(Coordinate l:list){
			s=Arith.add(s, Arith.mul(l.getX(), l.getX()));
		}
		return s;
	}
	
	public  Double xSum(List<Coordinate> list){
		Double s = 0.0;
		for(Coordinate l:list){
			s=Arith.add(s,l.getX());
		}
		return s;
	}
	
	public Double ySum(List<Coordinate> list){
		Double s = 0.0;
		for(Coordinate l:list){
			s=Arith.add(s,l.getY());
		}
		return s;
	}

	@Override
	public void compute(List<Coordinate> datas) {
		int n = datas.size();
		a = Arith.div((n*xy(datas)-ySum(datas)*xSum(datas)), (n*x2(datas)-xSum(datas)*xSum(datas)));
		b = Arith.div(Arith.sub(ySum(datas), Arith.mul(a, xSum(datas))), n);
		
		System.out.println("线性方程为:y="+a+"x+"+b);
		
	}
	

	@Override
	public double getY(long x) {
		double y = Arith.add(b, Arith.mul(a, x));
		return y;
	}

}
