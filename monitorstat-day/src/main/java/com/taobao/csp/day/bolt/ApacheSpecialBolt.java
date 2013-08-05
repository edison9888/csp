package com.taobao.csp.day.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.taobao.csp.day.apache.ApacheSpecialStatistics;
import com.taobao.csp.day.tddl.TddlLog;

/***
 * apache special�Ļ���bolt
 * 
 * @author youji.zj
 * @version 1.0
 *
 */
public class ApacheSpecialBolt implements IRichBolt {

	public static Logger logger = Logger.getLogger(ApacheSpecialBolt.class);
	
	private static final long serialVersionUID = 1L;
	
	private OutputCollector _collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this._collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		logger.debug("ApacheSpecialBolt get");
		String appName = input.getStringByField("appName");
		String requestUrl = input.getStringByField("requestUrl");
		long requestNum = input.getLongByField("requestNum");
		long rt = input.getLongByField("rt");
		String httpCode = input.getStringByField("httpCode");
		String collectTime = input.getStringByField("collectTime");
		
		// ��ȫ�������ڴ���,���ϲ��Ժ�����ڴ治�У���Ҫ��������ʽ
		ApacheSpecialStatistics.getInstance().summarize(appName, requestUrl, httpCode, requestNum, rt, collectTime);
		
		_collector.ack(input);
	}

	@Override
	public void cleanup() {
		logger.info("ApacheSpecialBolt clean up...");
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
