package com.taobao.csp.day.spout;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.taobao.csp.day.base.CustomizationFixedTheadPool;
import com.taobao.csp.day.base.FetcherListener;
import com.taobao.csp.day.base.Log;
import com.taobao.csp.day.tddl.TddlLog;
import com.taobao.csp.day.tddl.TddlTaskCenter;

/***
 * tddl的spout数据源
 * 
 * @author youji.zj 2012-08-20
 * @version 1.0
 */
public class TddlSpout implements IRichSpout {
	
	public static Logger logger = Logger.getLogger(TddlSpout.class);
	
	private static final long serialVersionUID = 1L;
	
	private SpoutOutputCollector _collector;
	
	private LinkedBlockingQueue<Object []> queue = new LinkedBlockingQueue<Object[]>(3000);
	
	/*** spout的编号，从0到spout的个数-1 ***/
	private int order;
	
	/*** tddlspout的总数 ***/
	private int spoutNum;
	
	public TddlSpout(int order, int spoutNum) {
		this.spoutNum = spoutNum;
		this.order = order;
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		_collector = collector;

		FetcherListener listener = new FetcherListener() {
			@Override
			public void onGenerateLogPo(List<Log> pos) {
				for (Log po : pos) {
					try {
						queue.put(po.getValues());
					} catch (InterruptedException e) {
						logger.warn(e);
					}
				}
			}
		};
		
		CustomizationFixedTheadPool pool = new CustomizationFixedTheadPool(6, this.order, this.spoutNum);
		pool.registerListener(listener);
		TddlTaskCenter.getInstance().registThreadPool(pool);
	}

	@Override
	public void close() {
		logger.info("TddlSpout close...");
	}

	@Override
	public void nextTuple() {
		Object[] ret;
		try {
			ret = queue.take();
		} catch (InterruptedException e) {
			logger.warn(e);
			return;
		}
		
		_collector.emit(new Values(ret));

	}

	@Override
	public void ack(Object msgId) {
		
	}

	@Override
	public void fail(Object msgId) {
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(TddlLog.getKeys()));
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
