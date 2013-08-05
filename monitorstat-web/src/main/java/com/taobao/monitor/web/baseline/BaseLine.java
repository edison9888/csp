
package com.taobao.monitor.web.baseline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.baseline.core.AverageCompute;
import com.taobao.monitor.baseline.core.BaseLineValue;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 计算基线数据
 * 将在同一时间点内的数据，做平均
 * 
 * 如果存在在一个时间点内的
 * @author xiaodu
 * @version 2010-8-25 上午11:03:52
 */
public class BaseLine {
	/**
	 * 利用指数平均数 
	 * @param poMap
	 * @return
	 */
	public static List<BaseLineValue> expma(Map<String,List<KeyValuePo>> poMap){
		List<BaseLineValue> listPo = new ArrayList<BaseLineValue>();
		for(Map.Entry<String,List<KeyValuePo>> entry:poMap.entrySet()){
			String time = entry.getKey();
			List<KeyValuePo> listpo = entry.getValue();
			List<Double> vList = new ArrayList<Double>();
			for(KeyValuePo po:listpo){
				Map<Integer, Double> mapValue = po.getSiteValueMap();
				vList.addAll(mapValue.values());
			}			
			double[] vArray = new double[vList.size()];
			for(int i=0;i<vArray.length;i++)
				vArray[i]=vList.get(i);
			double nv = AverageCompute.m4(vArray);
			
			BaseLineValue bv = new BaseLineValue();
			bv.setTime(Integer.parseInt(time.replaceAll(":", ""))) ;
			bv.setValue(nv);
			listPo.add(bv);
		}
		
		Collections.sort(listPo, new ComparatorImpl());
		double[] vArray = new double[listPo.size()];
		for(int i=0;i<listPo.size();i++){
			vArray[i]=listPo.get(i).getValue();
		}
		
		
		double[] tmp_vArray = AverageCompute.expma(vArray);
		
		for(int i=0;i<tmp_vArray.length;i++){
			listPo.get(i).setValue(tmp_vArray[i]) ;
		}
		return listPo;
	}
	
	
	/**
	 * 利用 算术平均
	 * @param poMap
	 * @return
	 */
	public static List<BaseLineValue> average(Map<String,List<KeyValuePo>> poMap){
		List<BaseLineValue> listPo = new ArrayList<BaseLineValue>();
		for(Map.Entry<String,List<KeyValuePo>> entry:poMap.entrySet()){
			String time = entry.getKey();
			List<KeyValuePo> listpo = entry.getValue();
			List<Double> vList = new ArrayList<Double>();
			for(KeyValuePo po:listpo){
				Map<Integer, Double> mapValue = po.getSiteValueMap();
				vList.addAll(mapValue.values());
			}			
			double[] vArray = new double[vList.size()];
			for(int i=0;i<vArray.length;i++)
				vArray[i]=vList.get(i);
			
						
			double nv = AverageCompute.m4(vArray);
			
			BaseLineValue bv = new BaseLineValue();
			bv.setTime(Integer.parseInt(time.replaceAll(":", ""))) ;
			bv.setValue(nv);
			listPo.add(bv);
		}
		
		Collections.sort(listPo, new ComparatorImpl());
		
	
		return listPo;
	}
	
	/**
	 * 利用调和平均计算
	 * @param poMap
	 * @return
	 */
	public static List<BaseLineValue> compute1(Map<String,List<KeyValuePo>> poMap){
		List<BaseLineValue> listPo = new ArrayList<BaseLineValue>();
		for(Map.Entry<String,List<KeyValuePo>> entry:poMap.entrySet()){
			String time = entry.getKey();
			List<KeyValuePo> listpo = entry.getValue();
			List<Double> vList = new ArrayList<Double>();
			for(KeyValuePo po:listpo){
				Map<Integer, Double> mapValue = po.getSiteValueMap();
				vList.addAll(mapValue.values());
			}			
			double[] vArray = new double[vList.size()];
			for(int i=0;i<vArray.length;i++)
				vArray[i]=vList.get(i);
			
			
			double nv = AverageCompute.hm(vArray);
			
			BaseLineValue bv = new BaseLineValue();
			bv.setTime(Integer.parseInt(time.replaceAll(":", ""))) ;
			bv.setValue(nv);
			listPo.add(bv);
		}
		
		Collections.sort(listPo, new ComparatorImpl());
		
	
		return listPo;
	}
	
	
	public static class ComparatorImpl implements Comparator<BaseLineValue>{

		
		public int compare(BaseLineValue o1, BaseLineValue o2) {
			if(o1.getTime()>o2.getTime()){
				return 1;
			}else if(o1.getTime()<o2.getTime()){
				return -1;
			}
			return 0;
		}
		
	}
	
	

}
