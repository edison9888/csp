
package com.taobao.monitor.common.util;

import java.math.BigDecimal;

/**
 * 
 * @author xiaodu
 * @version 2010-5-31 ����04:03:00
 */
public class Arith {
	
	//Ĭ�ϳ������㾫��
    private static final int DEF_DIV_SCALE = 30;
    //����಻��ʵ����
    private Arith(){
    }
    
    public static double add(String v1,String v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * �ṩ��ȷ�ļӷ����㡣
     * @param v1 ������
     * @param v2 ����
     * @return ���������ĺ�
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /**
     * �ṩ��ȷ�ļ������㡣
     * @param v1 ������
     * @param v2 ����
     * @return ���������Ĳ�
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    /**
     * �ṩ��ȷ�ĳ˷����㡣
     * @param v1 ������
     * @param v2 ����
     * @return ���������Ļ�
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ��
     * С�����Ժ�10λ���Ժ�������������롣
     * @param v1 ������
     * @param v2 ����
     * @return ������������
     */
    public static double div(double v1,double v2){
        return div(v1,v2,DEF_DIV_SCALE);
    }

    /**
     * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ
     * �����ȣ��Ժ�������������롣
     * @param v1 ������
     * @param v2 ����
     * @param scale ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
     * @return ������������
     */
    public static double div(double v1,double v2,int scale){
    	
    	if(v2 == 0){
    		return 0;
    	}
    	
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * �ṩ��ȷ��С��λ�������봦��
     * @param v ��Ҫ�������������
     * @param scale С���������λ
     * @return ���������Ľ��
     */
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    
    /**
     * ��ȡ׼ȷ������
     * @param v ֵ
     * @param n ����Ļ
     * @param scale С���������λ
     * @return ����Ļֵ
     */
    public static double pow(double v,int n,int scale){ 
    	BigDecimal b = new BigDecimal(Double.toString(v));    	
    	BigDecimal big = b.pow(n);
    	 BigDecimal one = new BigDecimal("1");
         return big.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();  
    }
    
    /**
     * ��ȡ׼ȷ������ ʹ��Ĭ��С���㱣��10λ
     * @param v ֵ
     * @param n ����Ļ
     * @return ����Ļֵ
     */
    public static double pow(double v,int n){     	
         return pow(v,n,DEF_DIV_SCALE);  
    }
    

}
