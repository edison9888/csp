
package com.taobao.csp.capacity.model.equation;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.monitor.common.util.Arith;

/**
 * 建立一元二次方程的建模类
 * @author xiaodu
 * @version 2011-4-12 下午01:23:25
 */
public class QuadraticEquation implements Equation {
	
	private double a; //常数
	
	private double b;//常数
	
	private double c;//常数
	
	public boolean isAvaliable() {
		if (a == 0 && b == 0 && c == 0) {
			return false;
		}
		return true;
	}
	

	@Override
	public void compute(List<Coordinate> datas) {
		
		
		//把datas拆分为x和y序列
		Double[] x = new Double[datas.size()];
		Double[] y = new Double[datas.size()];		
		
		for(int i = 0; i < datas.size(); i++) {
			
			x[i] = new Double(datas.get(i).getX());			//初始化移动平均后的x序列
			y[i] = datas.get(i).getY();			//初始化移动平均后的y序列
		}
		Double[][] left = combineX(x);	//正规方程组的左边
		Double[] right = combineY(x,y);	//正规方程组的右边
		Double[] result = caculate(left, right);	//得到的参数值
		a = result[2];
		b = result[1];
		c = result[0];
		System.out.println("方程式为 " + a + "x^2 + " + b + "x + " + c);
		
		
	}

	@Override
	public double getY(long x) {
		
		Double y = Arith.add(Arith.add(Arith.mul(a, Arith.pow(x, 2)), Arith.mul(b, x)), c);
		
		return y;
	}
	
	
	/**
	 * 组合正规方程组的左边x
	 * @param x	：x的序列
	 * 
	 * 组成原则,这里的数字代表次幂
	 * 【x个数, x, x2】
	 * 【x,    x2, x3】
	 * 【x2,   x3, x4】
	 * @return
	 * 返回正规方程组
	 */
	private  Double[][] combineX(Double[] x){
		
		Double[][] c = new Double[3][3];
		c[0][0] = (Double.valueOf(x.length));
		c[0][1] = sumX(x,1);	//x1的总和		这里的1、2、3代表的是幂
		c[0][2] = sumX(x,2);	//x2的总和
		c[1][0] = sumX(x,1);	//x1的总和
		c[1][1] = sumX(x,2);	//x2的总和
		c[1][2] = sumX(x,3);	//x3的总和
		c[2][0] = sumX(x,2);	//x2的总和
		c[2][1] = sumX(x,3);	//x3的总和
		c[2][2] = sumX(x,4);	//x4的总和
		
		return c;
	}
	/**
	 * 组合正规方程组的右边y
	 * @param y ：y的序列
	 * 
	 * 
	 * @return 组成原则,这里的数字代表次幂
	 * 【y0】
	 * 【y1】
	 * 【y2】
	 */
	private  Double[] combineY(Double[] x,Double[] y){
		
		Double[] c = new Double[3];
		c[0] = sumX2Y(x, 0, y);
		c[1] = sumX2Y(x, 1, y);
		c[2] = sumX2Y(x, 2, y);
		return c;
	}
	
	/**
	 * 求x的n次幂的和
	 * @param a
	 * @param n
	 * @return
	 */
	private  Double sumX(Double[] a, int n){
		
		
		Double sum = 0.0;
		for(Double d : a){
			
			sum = Arith.add(sum, Arith.pow(d, n));
		}
		return sum;
	}
	

	/**
	 * 求XnY的和,n为x的幂
	 * @param x
	 * @param y
	 * @return
	 */
	private  Double sumX2Y(Double[] x, int n,Double[] y){
			
			
			Double sum = 0.0;
			if(x.length == y.length) { 
				for(int i = 0; i< x.length; i++) {
					
					sum = Arith.add(sum, Arith.mul(Arith.pow(x[i], n), y[i]));
				}
			} else {
				
				System.out.println("出错：x序列和y序列的个数不匹配");
			}
			return sum;
		}
	
	/**解3阶矩阵
	 * 
	 * 	  x00 x01 x02    a0		y0
	 *  ｛x10 x11 x12｝｛a1｝= {y1}    
	 *    x20 x21 x22    a2		y1
	 *    
	 *   其中已知x和y,求参数a
	 * 
	 * @param result
	 * @return 方程式是c+bx+ax2;  c=result[0], b=result[1] , c=result[2]
	 */
	private Double[] caculate(Double[][] x, Double[] y) {
		
		if(x.length != 3 && y.length != 3) {
			
			System.out.println("传入参数出错");
//			return false;
		}
		//求出矩阵行列式 ，公式为主对角线三项的和-反对角线的和
		Double detA = Math.abs(
				Arith.mul(Arith.mul(x[0][0], x[1][1]),x[2][2]) + 
				Arith.mul(Arith.mul(x[0][1],x[1][2]),x[2][0]) + 
				Arith.mul(Arith.mul(x[0][2],x[1][0]),x[2][1]) - 
				Arith.mul(Arith.mul(x[0][2],x[1][1]),x[2][0]) - 
				Arith.mul(Arith.mul(x[1][2],x[2][1]),x[0][0]) - 
				Arith.mul(Arith.mul(x[2][2],x[1][0]),x[0][1]));
		System.out.println("detA:" + detA);
		
		//求余子式
		Double A00 = Arith.mul(x[1][1],x[2][2]) - Arith.mul(x[1][2],x[2][1]);
		Double A01 = -(Arith.mul(x[1][0],x[2][2]) - Arith.mul(x[1][2],x[2][0]));
		Double A02 = Arith.mul(x[1][0],x[2][1]) - Arith.mul(x[1][1],x[2][0]);
		Double A10 = -(Arith.mul(x[0][1],x[2][2]) - Arith.mul(x[0][2],x[2][1]));
		Double A11 = Arith.mul(x[0][0],x[2][2]) - Arith.mul(x[0][2],x[2][0]);
		Double A12 = -(Arith.mul(x[0][0],x[2][1]) - Arith.mul(x[0][1],x[2][0]));
		Double A20 = Arith.mul(x[0][1],x[1][2]) - Arith.mul(x[0][2],x[1][1]);
		Double A21 = -(Arith.mul(x[0][0],x[1][2]) - Arith.mul(x[0][2],x[1][0]));
		Double A22 = Arith.mul(x[0][0],x[1][1]) - Arith.mul(x[1][0],x[0][1]);
		
		//余子式转置后为A*,然后逆矩阵A^(-1) = A*/detA
		Double[][] B = new Double[3][3];	//最终的逆矩阵
		Double[] result = new Double[]{0.0, 0.0, 0.0};		//存储二次函数参数的结果数组
		if(detA != 0.0) {				//detA为0，不能用矩阵计算了
			B[0][0] = A00;
			B[0][1] = A10;
			B[0][2] = A20;
			B[1][0] = A01;
			B[1][1] = A11;
			B[1][2] = A21;
			B[2][0] = A02;
			B[2][1] = A12;
			B[2][2] = A22;
//			System.out.println(A22 + "  " + detA + "  " + B[2][2] + "   用手算"+ A22/ detA);
			
			for(int i = 0; i < 3; i++) {
				
				for(int j = 0; j < 3; j++) {
					
					result[i] = Arith.add(result[i], Arith.mul(B[i][j], y[j]));
					
				}
				result[i] = Arith.div(result[i], detA);
			}
			
		} else {
			
			System.out.println("出错：detA为0，不能用矩阵计算了。。。");
		};
		return result;
	}

}
