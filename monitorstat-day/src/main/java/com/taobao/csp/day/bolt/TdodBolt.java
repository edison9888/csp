package com.taobao.csp.day.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import com.taobao.csp.day.tdod.TdodStatistics;

/***
 * tdodµÄ»ã×Übolt
 * 
 * @author youji.zj
 * @version 1.0
 *
 */
public class TdodBolt implements IRichBolt {

	public static Logger logger = Logger.getLogger(TdodBolt.class);
	
	private static final long serialVersionUID = 1L;
	
	private OutputCollector _collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		logger.debug("Bolt get");
		String appName = input.getStringByField("appName");
		int blockCount = input.getIntegerByField("blockCount");
		String collectTime = input.getStringByField("collectTime");
		
		TdodStatistics.getInstance().summarize(appName, blockCount, collectTime);
		
		_collector.ack(input);
	}

	@Override
	public void cleanup() {
		logger.info("Bolt clean up...");
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
