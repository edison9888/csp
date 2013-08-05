package com.taobao.csp.day.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.taobao.csp.day.tddl.TddlLog;
import com.taobao.csp.day.tddl.TddlStatistics;

/***
 * tddl�Ļ���bolt
 * Ҳ��tddl����bolt
 * 
 * @author youji.zj
 * @version 1.0
 *
 */
public class TddlBolt implements IRichBolt {

	public static Logger logger = Logger.getLogger(TddlBolt.class);
	
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
		String dbName = input.getStringByField("dbName");
		String dbIp = input.getStringByField("dbIp");
		String dbPort = input.getStringByField("dbPort");
		String sql = input.getStringByField("sql");
		long executeSum = input.getLongByField("executeSum");
		long executeTime = input.getLongByField("executeTime");
		int maxResp = input.getIntegerByField("maxResp");
		String maxRespTime = input.getStringByField("maxRespTime");
		int minResp = input.getIntegerByField("minResp");
		String minRespTime = input.getStringByField("minRespTime");
		String collectTime = input.getStringByField("collectTime");
		
		// ��ȫ�������ڴ���,���ϲ��Ժ�����ڴ治�У���Ҫ��������ʽ
		TddlStatistics.getInstance().summarize(appName, dbFeature, dbName, dbIp, dbPort, sql, executeSum, executeTime, 
				maxResp, maxRespTime, minResp, minRespTime, collectTime);
		
		Object [] values = new Object[6];
		values[0] = appName;
		values[1] = dbFeature;
		values[2] = minResp;
		values[3] = minRespTime;
		values[4] = maxResp;
		values[5] = maxRespTime;
		_collector.emit(input, new Values(values));
		_collector.ack(input);
	}

	@Override
	public void cleanup() {
		logger.info("TddlBolt clean up...");
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(TddlLog.getStaticsKeys()));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}
