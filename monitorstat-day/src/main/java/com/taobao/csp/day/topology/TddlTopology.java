package com.taobao.csp.day.topology;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.InputDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.taobao.csp.day.bolt.TddlBolt;
import com.taobao.csp.day.bolt.TddlStaticsBolt;
import com.taobao.csp.day.config.SpoutCongig;
import com.taobao.csp.day.spout.TddlSpout;
import com.taobao.csp.day.tddl.TddlLog;

public class TddlTopology {
	
	public static Logger logger = Logger.getLogger(TddlTopology.class);

	public static void main(String[] args) {
		
		TopologyBuilder builder = new TopologyBuilder();
		
		// 构造tddl
		int spoutNum = SpoutCongig.getTddlSpoutNum();
		List<String> tddlSpoutL = new ArrayList<String>();
		for (int i = 0; i < spoutNum; i++) {
			// i为spout的编号
			TddlSpout tddlSpout = new TddlSpout(i, spoutNum);
			String spoutId = "tddlSpout" + i;
			
			// spout的并发数设置为1
			builder.setSpout(spoutId, tddlSpout, 1);
			tddlSpoutL.add(spoutId);
		}
		
		TddlBolt tddlBolt = new TddlBolt();
		InputDeclarer inputDeclarer = builder.setBolt("tddlBolt", tddlBolt, 1);
		for (String spoutId : tddlSpoutL) {
			inputDeclarer.fieldsGrouping(spoutId, new Fields(TddlLog.getGroupFields()));
		}
		
		// tddlStaticsBolt的并发数必须设置成1（surramy汇总做一次就可以了)
		TddlStaticsBolt tddlStaticsBolt = new TddlStaticsBolt();
		builder.setBolt("tddlStaticsBolt", tddlStaticsBolt, 1).allGrouping("tddlBolt");

		Config conf = new Config();

		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);

			try {
				StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				logger.error("AlreadyAliveException", e);
			} catch (InvalidTopologyException e) {
				logger.error("InvalidTopologyException", e);
			}
		}

		else {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());
//			Utils.sleep(100000);
//			cluster.killTopology("test");
//			cluster.shutdown();
		}
	}
}
