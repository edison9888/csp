
/**
 * monitorstat-alarm
 */
package com.taobao.csp.time.util;

import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.interpolation.LoessInterpolator;
import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.Arith;

/**
 * @author xiaodu
 *
 * 上午11:37:46
 */
public class DataSmoothing {
	
	private static final Logger logger = Logger.getLogger(DataSmoothing.class);
	
	/**
	 * 采用apache的lowess 算法，平滑数据
	 * @param xval
	 * @param yval
	 * @return
	 * @throws MathException
	 */
	public static double[] apacheLoess(double[] xval, double[] yval) throws MathException{
		LoessInterpolator loess = new LoessInterpolator();
		return loess.smooth(xval, yval);
	}
	
	
	public static double[] cspSmooth(double[] xval, double[] yval){
		
		return expma(yval);
	}
	
	
	/**
	 * 指数平均数指标
	 * 
	 *  * 利用expma 计算
	 * 
	 * expma = (当前值*2+上次expma*(n-1))/n-1
	 * 
	 * n为天数 系数  
	 * @param dist
	 * @return
	 */
	public static double[] expma(double[] dist){
		double[] tmp = new double[dist.length];
		double curExpma = 0;
		double preExpma = 0;
		int n=12;
		for(int i=0;i<dist.length;i++){
			double v = dist[i];
			if(i == 0){
				preExpma = v;
			}
			
			curExpma = Arith.div(Arith.add(Arith.mul(v, 2), Arith.mul(preExpma, (n-1))), (n+1),2);
			tmp[i] = curExpma;
			preExpma = curExpma;			
		}
		
		return tmp;
	}
	
	
	/**
	 * 次平均是将所有值 中占大多数的近似的值
	 * @param dist
	 * @return
	 */
	public static double m4(double[] dist){
		
		double average = 0;
		int size=0;
		
		for(int i=0;i<dist.length;i++){
			
			int tmp_size = 0;
			double tmp_average = 0;
			
			for(int j=0;j<dist.length;j++){
				
				double d = dist[i];
				if(d<=Arith.mul(1.2, dist[j])&&d>=Arith.mul(0.8, dist[j])){
					tmp_size++;
					tmp_average=Arith.add(dist[j], tmp_average);
				}
			}
			
			if(size<tmp_size){
				average= tmp_average;
				size = tmp_size;
			}else if(size == tmp_size){
				size = tmp_size;
				average=Arith.div(Arith.add(average, tmp_average), 2) ;
			}
			
		}
		
		if(size == 0){
			return 0;
		}
		return Arith.div(average,size,2);
	}
	
	
	
	


}
