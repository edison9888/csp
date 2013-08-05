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
 * sph�Ļ���bolt
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
		
		// ��ȫ�������ڴ���,���ϲ��Ժ�����ڴ治�У���Ҫ��������ʽ
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
