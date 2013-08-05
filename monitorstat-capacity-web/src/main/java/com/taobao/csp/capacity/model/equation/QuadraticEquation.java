
package com.taobao.csp.capacity.model.equation;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.monitor.common.util.Arith;

/**
 * ����һԪ���η��̵Ľ�ģ��
 * @author xiaodu
 * @version 2011-4-12 ����01:23:25
 */
public class QuadraticEquation implements Equation {
	
	private double a; //����
	
	private double b;//����
	
	private double c;//����
	
	public boolean isAvaliable() {
		if (a == 0 && b == 0 && c == 0) {
			return false;
		}
		return true;
	}
	

	@Override
	public void compute(List<Coordinate> datas) {
		
		
		//��datas���Ϊx��y����
		Double[] x = new Double[datas.size()];
		Double[] y = new Double[datas.size()];		
		
		for(int i = 0; i < datas.size(); i++) {
			
			x[i] = new Double(datas.get(i).getX());			//��ʼ���ƶ�ƽ�����x����
			y[i] = datas.get(i).getY();			//��ʼ���ƶ�ƽ�����y����
		}
		Double[][] left = combineX(x);	//���淽��������
		Double[] right = combineY(x,y);	//���淽������ұ�
		Double[] result = caculate(left, right);	//�õ��Ĳ���ֵ
		a = result[2];
		b = result[1];
		c = result[0];
		System.out.println("����ʽΪ " + a + "x^2 + " + b + "x + " + c);
		
		
	}

	@Override
	public double getY(long x) {
		
		Double y = Arith.add(Arith.add(Arith.mul(a, Arith.pow(x, 2)), Arith.mul(b, x)), c);
		
		return y;
	}
	
	
	/**
	 * ������淽��������x
	 * @param x	��x������
	 * 
	 * ���ԭ��,��������ִ������
	 * ��x����, x, x2��
	 * ��x,    x2, x3��
	 * ��x2,   x3, x4��
	 * @return
	 * �������淽����
	 */
	private  Double[][] combineX(Double[] x){
		
		Double[][] c = new Double[3][3];
		c[0][0] = (Double.valueOf(x.length));
		c[0][1] = sumX(x,1);	//x1���ܺ�		�����1��2��3���������
		c[0][2] = sumX(x,2);	//x2���ܺ�
		c[1][0] = sumX(x,1);	//x1���ܺ�
		c[1][1] = sumX(x,2);	//x2���ܺ�
		c[1][2] = sumX(x,3);	//x3���ܺ�
		c[2][0] = sumX(x,2);	//x2���ܺ�
		c[2][1] = sumX(x,3);	//x3���ܺ�
		c[2][2] = sumX(x,4);	//x4���ܺ�
		
		return c;
	}
	/**
	 * ������淽������ұ�y
	 * @param y ��y������
	 * 
	 * 
	 * @return ���ԭ��,��������ִ������
	 * ��y0��
	 * ��y1��
	 * ��y2��
	 */
	private  Double[] combineY(Double[] x,Double[] y){
		
		Double[] c = new Double[3];
		c[0] = sumX2Y(x, 0, y);
		c[1] = sumX2Y(x, 1, y);
		c[2] = sumX2Y(x, 2, y);
		return c;
	}
	
	/**
	 * ��x��n���ݵĺ�
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
	 * ��XnY�ĺ�,nΪx����
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
				
				System.out.println("����x���к�y���еĸ�����ƥ��");
			}
			return sum;
		}
	
	/**��3�׾���
	 * 
	 * 	  x00 x01 x02    a0		y0
	 *  ��x10 x11 x12����a1��= {y1}    
	 *    x20 x21 x22    a2		y1
	 *    
	 *   ������֪x��y,�����a
	 * 
	 * @param result
	 * @return ����ʽ��c+bx+ax2;  c=result[0], b=result[1] , c=result[2]
	 */
	private Double[] caculate(Double[][] x, Double[] y) {
		
		if(x.length != 3 && y.length != 3) {
			
			System.out.println("�����������");
//			return false;
		}
		//�����������ʽ ����ʽΪ���Խ�������ĺ�-���Խ��ߵĺ�
		Double detA = Math.abs(
				Arith.mul(Arith.mul(x[0][0], x[1][1]),x[2][2]) + 
				Arith.mul(Arith.mul(x[0][1],x[1][2]),x[2][0]) + 
				Arith.mul(Arith.mul(x[0][2],x[1][0]),x[2][1]) - 
				Arith.mul(Arith.mul(x[0][2],x[1][1]),x[2][0]) - 
				Arith.mul(Arith.mul(x[1][2],x[2][1]),x[0][0]) - 
				Arith.mul(Arith.mul(x[2][2],x[1][0]),x[0][1]));
		System.out.println("detA:" + detA);
		
		//������ʽ
		Double A00 = Arith.mul(x[1][1],x[2][2]) - Arith.mul(x[1][2],x[2][1]);
		Double A01 = -(Arith.mul(x[1][0],x[2][2]) - Arith.mul(x[1][2],x[2][0]));
		Double A02 = Arith.mul(x[1][0],x[2][1]) - Arith.mul(x[1][1],x[2][0]);
		Double A10 = -(Arith.mul(x[0][1],x[2][2]) - Arith.mul(x[0][2],x[2][1]));
		Double A11 = Arith.mul(x[0][0],x[2][2]) - Arith.mul(x[0][2],x[2][0]);
		Double A12 = -(Arith.mul(x[0][0],x[2][1]) - Arith.mul(x[0][1],x[2][0]));
		Double A20 = Arith.mul(x[0][1],x[1][2]) - Arith.mul(x[0][2],x[1][1]);
		Double A21 = -(Arith.mul(x[0][0],x[1][2]) - Arith.mul(x[0][2],x[1][0]));
		Double A22 = Arith.mul(x[0][0],x[1][1]) - Arith.mul(x[1][0],x[0][1]);
		
		//����ʽת�ú�ΪA*,Ȼ�������A^(-1) = A*/detA
		Double[][] B = new Double[3][3];	//���յ������
		Double[] result = new Double[]{0.0, 0.0, 0.0};		//�洢���κ��������Ľ������
		if(detA != 0.0) {				//detAΪ0�������þ��������
			B[0][0] = A00;
			B[0][1] = A10;
			B[0][2] = A20;
			B[1][0] = A01;
			B[1][1] = A11;
			B[1][2] = A21;
			B[2][0] = A02;
			B[2][1] = A12;
			B[2][2] = A22;
//			System.out.println(A22 + "  " + detA + "  " + B[2][2] + "   ������"+ A22/ detA);
			
			for(int i = 0; i < 3; i++) {
				
				for(int j = 0; j < 3; j++) {
					
					result[i] = Arith.add(result[i], Arith.mul(B[i][j], y[j]));
					
				}
				result[i] = Arith.div(result[i], detA);
			}
			
		} else {
			
			System.out.println("����detAΪ0�������þ�������ˡ�����");
		};
		return result;
	}

}
