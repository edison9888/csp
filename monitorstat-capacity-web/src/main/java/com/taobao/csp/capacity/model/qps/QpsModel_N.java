
package com.taobao.csp.capacity.model.qps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.equation.Equation;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.Arith;

/**
 * qps 构建模型采用将 最近的60天的pv数据与高峰期的qps平均数据 通过线性拟合获取他的 公式
 * 
 * @author xiaodu
 * @version 2011-4-19 上午09:06:44
 */
public class QpsModel_N implements Equation{
	
	/**
	 * 采集数据的开始时间
	 */
	private Date endDataCollectTime;
	
	private Date startDataCollectTime;
	
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private double qps = 0;
	private double pv = 0;

	public QpsModel_N(Map<String,Double> collectPv,Map<String,Double> collectQps,Map<String ,Double> collectMachineNumber){
		
		Map<String,Double> mapPv = collectPv;
		Map<String,Double> mapQps = collectQps;
		Map<String,Double> mapMachine = collectMachineNumber;
		List<Coordinate> listC = new ArrayList<Coordinate>();
		
		for(Map.Entry<String,Double> entry:mapPv.entrySet()){
			String key = entry.getKey();
			Double pv = entry.getValue();
			Double qps = mapQps.get(key);
			Double machine = mapMachine.get(key);
			if(pv!=null&&qps!=null&&machine!=null ){
				Coordinate c = new Coordinate();
				c.setX(pv.longValue());
				c.setY(Arith.mul(qps, machine));
				listC.add(c);
			}
			
		}
		
		
		for(Coordinate c:listC){
			System.out.println(c.getX()+":"+c.getY());
		}
		
		compute(listC);
		
		
	}
	
	
	
	

	
	@Override
	public double getY(long x) {
		return Math.round(Arith.mul(Arith.div(qps, pv), x));
	}


	@Override
	public void compute(List<Coordinate> datas) {
		
		for(Coordinate c:datas){
			qps = Arith.add(qps, c.getY());
			pv = Arith.add(pv, c.getX());
		}
		
	}
	
	

}
