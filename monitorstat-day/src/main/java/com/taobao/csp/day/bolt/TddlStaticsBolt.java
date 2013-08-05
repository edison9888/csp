package com.taobao.csp.day.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.day.tddl.TddlSpecialStatics;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

/***
 * 负责统计summary数据的bolt
 * topology中该bolt,线程数需要设置成1
 * 
 * @author youji.zj
 * @version 1.0 2012-09-07
 *
 */
public class TddlStaticsBolt implements IRichBolt {

	public static Logger logger = Logger.getLogger(TddlStaticsBolt.class);
	
	private static final long serialVersionUID = 1L;
	
	private OutputCollector _collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String appName = input.getStringByField("appName");
		String dbFeature = input.getStringByField("dbFeature");
		int minResp = input.getIntegerByField("minResp");
		String minRespTime = input.getStringByField("minRespTime");
		int maxResp = input.getIntegerByField("maxResp");
		String maxRespTime = input.getStringByField("maxRespTime");
		
		TddlSpecialStatics.getInstance().summarize(appName, dbFeature, maxResp, maxRespTime, minResp, minRespTime);
		
		_collector.ack(input);
	}

	@Override
	public void cleanup() {
		logger.info("TddlStaticsBolt clean up...");
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
