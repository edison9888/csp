
package com.taobao.csp.capacity.model.month;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.equation.Equation;
import com.taobao.csp.capacity.model.equation.LineEquation;
import com.taobao.csp.capacity.model.equation.QuadraticEquation;


/**
 * 模型工厂
 * @author xiaodu
 * @version 2011-3-31 下午01:32:40
 */
public class MonthTrendModelFactory {
	
	
	private static Set<Class> trendModelSet = new HashSet<Class>();
	
	public static synchronized void regisiter(Class classes){
		trendModelSet.add(classes);
	}
	
	static{
		regisiter(LineEquation.class);
		regisiter(QuadraticEquation.class);
	}
	
	
	/**
	 * 
	 * @param datas 需要输入 进行建模的数据 long[] 表示 [0] 表示 x轴 [1] 表示y轴
	 * list 必须至少需要24个值，按照顺序
	 * @return
	 */
	public static MonthTrendModel model(List<Coordinate> datas){
		
		BaseMonthTreandModel base = null;
		double dof = Double.MAX_VALUE;
		Equation m = null;
		for(Class c:trendModelSet){
			try {
				Object mm = c.newInstance();
				if(mm instanceof Equation){
					m = (Equation)mm;
					base = new BaseMonthTreandModel(datas,m);
					double t = base.degreeOfFitting();
					if(dof > t){
						dof = t;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return base;
	}

}
