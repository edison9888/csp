
package com.taobao.monitor.baseline.core;

import com.taobao.monitor.common.util.Arith;

/**
 * 

 * 
 * 
 * @author xiaodu
 * @version 2010-8-26 ����10:15:25
 */
public class AverageCompute {
	
	
	/**
	 * ��ƽ���ǽ�����ֵ ��ռ������Ľ��Ƶ�ֵ
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
	
	
	/**
	 * ����ƽ����
	 * @param dist
	 * @return
	 */
	public static double m(double[] dist){
		
		int n = dist.length;
		double c = 0;
		for(double v:dist){
			if(v==0){
				continue;
			}
			c=Arith.add(c, v);
		}
		if(n == 0){
			return 0;
		}
		return Arith.div(c,n, 2);		
	}	
	
	/**
	 * ָ��ƽ����ָ��
	 * 
	 *  * ����expma ����
	 * 
	 * expma = (��ǰֵ*2+�ϴ�expma*(n-1))/n-1
	 * 
	 * nΪ���� ϵ��  
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
	 * ָ��ƽ����ָ�� ����һ�ּ���
	 * 
	 *  * ����expma ����
	 * 
	 * expma = (v-preExpma)/n+preExpma;
	 * 
	 * nΪ���� ϵ��  
	 * @param dist
	 * @return
	 */
	public static double[] expma1(double[] dist){
		double[] tmp = new double[dist.length];
		double curExpma = 0;
		double preExpma = 0;
		int n=3;
		for(int i=0;i<dist.length;i++){
			double v = dist[i];
			if(i == 0){
				preExpma = v;
			}
			curExpma = (v-preExpma)/n+preExpma;
			tmp[i] = curExpma;
			preExpma = curExpma;			
		}
		
		return tmp;
	}
	
	/**
	 * ����ѧ�е���ƽ����������ƽ�������Ƕ������Գ���ϵ�ġ����������߲���ͬ��ǰ�ߺ�С�ڵ��ں���
	 * 
	 * ����ƽ����
	 * harmonic mean
	 * ��ʽ��Mh=n/(1/A1+1/A2+...+1/An)
	 * @param dist
	 * @return
	 */
	public static double hm(double[] dist){
		
		int n = dist.length;
		double c = 0;
		for(double v:dist){
			if(v==0){
				continue;
			}
			c=Arith.add(c, Arith.div(1, v));
		}
		if(c>0)
			return Arith.div(n, c, 2);
		else
			return 0;
	}
	

	
	
	
	public static void main(String[] args){
		
		System.out.println(Math.sqrt(9));
		
		
		double[] dist2 = new double[]{1,11111,11111,11111};
		System.out.println(AverageCompute.m(dist2));
		double[] dist1 = new double[]{3,11111,11111,11111,11111,11111,3333,333,3333,444,444};
		System.out.println(AverageCompute.hm(dist1));
		
		double[] dist = new double[]{1,2,1,3,2,4,1,2,4,2,1,4,0,3,2,5,1,1,2,0,2,3,4,5,2,3,2,3,50,50,50,50,50,50,1,1,50,50,50,1,2,3,2,3,1,2,4,3,2,1,4,32,1,2,3};
		
		double[] dist11 = new double[]{11,13,14,35,23,44,15,26,47,28,19,41,40,38,39,51,61,81,92,40,62,53,44,35,2,3,2,3,50,50,50,50,50,50,1,1,50,50,50,1,2,3,2,3,1,2,4,3,2,1,4,32,1,2,3};
		
		double[] dist12 = new double[]{1,13,14,35,23,44,15,26,47,28,19,41,40,38,39,51,61,81,92,40,62,53,44,35,2,};
		
		for(double d:dist12){
			System.out.print((int)d+",");
		}
		System.out.println("");
		for(double d:AverageCompute.expma(dist12)){
			System.out.print((int)d+",");
		}
		
		
	}

}
