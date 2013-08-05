package com.taobao.csp.day.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.taobao.csp.day.sph.SphStatistics;
import com.taobao.csp.day.tddl.TddlLog;

/***
 * sph的汇总bolt
 * 
 * @author youji.zj
 * @version 1.0
 *
 */
public class SphBolt implements IRichBolt {

	public static Logger logger = Logger.getLogger(SphBolt.class);
	
	private static final long serialVersionUID = 1L;
	
	private OutputCollector _collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		logger.debug("SphBolt get");
		String appName = input.getStringByField("appName");
		String ip = input.getStringByField("ip");
		String blockKey = input.getStringByField("blockKey");
		String action = input.getStringByField("action");
		int blockCount = input.getIntegerByField("blockCount");
		String collectTime = input.getStringByField("collectTime");
		
		// 先全部存在内存中,线上测试后，如果内存不行，需要换其它方式
		SphStatistics.getInstance().summarize(appName, ip, blockKey, action, blockCount, collectTime);
		
		_collector.ack(input);
	}

	@Override
	public void cleanup() {
		logger.info("SphBolt clean up...");
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
