package com.taobao.monitor.web.rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.taobao.monitor.common.util.Arith;

/**
 * 次类用于计算分数
 * 
 * @author xiaodu
 * @version 2010-8-19 上午10:05:49
 */
public class RatingCompute {

	public static double compute(RatingIndicatorValue value) {
		return compute(value.getIndicatorThresholdValue(), value.getIndicatorValue(), value.getIndicatorWeight());
	}

	public static int region(RatingIndicatorValue value) {
		List<RatingRegion> list = parse(value.getIndicatorThresholdValue());
		double indicatorValue = value.getIndicatorValue();
//		for (int i = 0; i < list.size(); i++) {
//			RatingRegion region = list.get(i);
//			if (region.getLowerLimit() != -1 && indicatorValue > region.getLowerLimit()
//					&& region.getUpperLimit() != -1 && indicatorValue <= region.getUpperLimit()) {
//				return i + 1;
//			} else if (region.getLowerLimit() == -1 && region.getUpperLimit() != -1
//					&& indicatorValue < region.getUpperLimit()) {// 落入这个区间
//				return i + 1;
//			} else if (region.getLowerLimit() != -1 && region.getUpperLimit() == -1
//					&& indicatorValue >= region.getLowerLimit()) {
//				return i + 1;
//			}
//		}
		
		
		
		
		
		RatingRegion region = list.get(0);
		if (region.getLowerLimit() != -1 && indicatorValue > region.getLowerLimit() && region.getUpperLimit() != -1
				&& indicatorValue <= region.getUpperLimit()) {
			return 1;
		} else if (region.getLowerLimit() == -1 && region.getUpperLimit() != -1
				&& indicatorValue <= region.getUpperLimit()) {// 落入这个区间
			return 1;
		} else if (region.getLowerLimit() != -1 && region.getUpperLimit() == -1
				&& indicatorValue > region.getLowerLimit()) {
			return 1;
		}

		region = list.get(1);
		if (region.getLowerLimit() != -1 && indicatorValue > region.getLowerLimit() && region.getUpperLimit() != -1
				&& indicatorValue < region.getUpperLimit()) {
			return 2;
		} else if (region.getLowerLimit() == -1 && region.getUpperLimit() != -1
				&& indicatorValue < region.getUpperLimit()) {// 落入这个区间
			return 2;
		} else if (region.getLowerLimit() != -1 && region.getUpperLimit() == -1
				&& indicatorValue > region.getLowerLimit()) {
			return 2;
		}

		region = list.get(2);
		if (region.getLowerLimit() != -1 && indicatorValue >= region.getLowerLimit() && region.getUpperLimit() != -1
				&& indicatorValue < region.getUpperLimit()) {
			return 3;
		} else if (region.getLowerLimit() == -1 && region.getUpperLimit() != -1
				&& indicatorValue < region.getUpperLimit()) {// 落入这个区间
			return 3;
		} else if (region.getLowerLimit() != -1 && region.getUpperLimit() == -1
				&& indicatorValue >= region.getLowerLimit()) {
			return 3;
		}
		
		return -1;
	}

	// [a,b), [b,c ), [c,d)
	// (a,b], ( b,c ), [c,d)
	public static double compute(String indicatorThresholdValue, double indicatorValue, int indicatorWeight) {
		List<RatingRegion> list = parse(indicatorThresholdValue);

		double w = Arith.div(indicatorWeight, 100, 3);

		if (list.size() != 3) {
			return 0d;
		}

		// for(RatingRegion region:list){
		//			
		// if(region.getLowerLimit()!=-1&&indicatorValue>=region.getLowerLimit()&&region.getUpperLimit()!=-1&&indicatorValue<region.getUpperLimit()){
		// double r = (region.getRatio()*w)*100;
		// return r;
		// }else
		// if(region.getLowerLimit()==-1&&region.getUpperLimit()!=-1&&indicatorValue<region.getUpperLimit()){//落入这个区间
		// double r = (region.getRatio()*w)*100;
		// return r;
		// }else
		// if(region.getLowerLimit()!=-1&&region.getUpperLimit()==-1&&indicatorValue>=region.getLowerLimit()){
		// double r = (region.getRatio()*w)*100;
		// return r;
		// }
		// }

		RatingRegion region = list.get(0);
		if (region.getLowerLimit() != -1 && indicatorValue > region.getLowerLimit() && region.getUpperLimit() != -1
				&& indicatorValue <= region.getUpperLimit()) {
			double r = (region.getRatio() * w) * 100;
			return r;
		} else if (region.getLowerLimit() == -1 && region.getUpperLimit() != -1
				&& indicatorValue <= region.getUpperLimit()) {// 落入这个区间
			double r = (region.getRatio() * w) * 100;
			return r;
		} else if (region.getLowerLimit() != -1 && region.getUpperLimit() == -1
				&& indicatorValue > region.getLowerLimit()) {
			double r = (region.getRatio() * w) * 100;
			return r;
		}

		region = list.get(1);
		if (region.getLowerLimit() != -1 && indicatorValue > region.getLowerLimit() && region.getUpperLimit() != -1
				&& indicatorValue < region.getUpperLimit()) {
			double r = (region.getRatio() * w) * 100;
			return r;
		} else if (region.getLowerLimit() == -1 && region.getUpperLimit() != -1
				&& indicatorValue < region.getUpperLimit()) {// 落入这个区间
			double r = (region.getRatio() * w) * 100;
			return r;
		} else if (region.getLowerLimit() != -1 && region.getUpperLimit() == -1
				&& indicatorValue > region.getLowerLimit()) {
			double r = (region.getRatio() * w) * 100;
			return r;
		}

		region = list.get(2);
		if (region.getLowerLimit() != -1 && indicatorValue >= region.getLowerLimit() && region.getUpperLimit() != -1
				&& indicatorValue < region.getUpperLimit()) {
			double r = (region.getRatio() * w) * 100;
			return r;
		} else if (region.getLowerLimit() == -1 && region.getUpperLimit() != -1
				&& indicatorValue < region.getUpperLimit()) {// 落入这个区间
			double r = (region.getRatio() * w) * 100;
			return r;
		} else if (region.getLowerLimit() != -1 && region.getUpperLimit() == -1
				&& indicatorValue >= region.getLowerLimit()) {
			double r = (region.getRatio() * w) * 100;
			return r;
		}

		return 0d;
	}

	/**
	 * {(1,2):0.5}
	 * 
	 * @param v
	 * @return
	 */
	private static List<RatingRegion> parse(String v) {
		List<String> list = split(v);

		List<RatingRegion> rList = new ArrayList<RatingRegion>();

		for (String str : list) {

			String[] tmp = str.split(":");

			double r = Double.parseDouble(tmp[1]);

			RatingRegion region = new RatingRegion();
			String[] vs = tmp[0].split(",");
			double u = Double.parseDouble(vs[0].trim().substring(1, vs[0].length()));
			double l = Double.parseDouble(vs[1].trim().substring(0, vs[1].length() - 1));

			region.setRatio(r);
			region.setUpperLimit(l);
			region.setLowerLimit(u);

			rList.add(region);
		}

		return rList;
	}

	/* 从{(0,45):1}{(45,75):0.5}{(75,-1):-1}中取出45 */
	public static double getThresholdValue(String indicatorThresholdValue) {
		return Double.parseDouble(indicatorThresholdValue.substring(indicatorThresholdValue.indexOf(",") + 1,
				indicatorThresholdValue.indexOf(")")));
	}

	/**
	 * 将读取到的{} 分割成不同的记录
	 * 
	 * @param line
	 * @return
	 */
	private static List<String> split(String line) {
		List<String> gcResultList = new ArrayList<String>();

		int len = line.length();
		char first = '{';
		char lase = '}';
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < len; i++) {
			if (line.charAt(i) == lase) {
				String result = "";
				char _result = ' ';
				while ((_result = stack.pop()) != first) {
					result = _result + result;
					;
				}
				gcResultList.add(result);
			} else {
				stack.add(line.charAt(i));
			}

		}

		return gcResultList;
	}

}
