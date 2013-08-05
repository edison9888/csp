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

import com.taobao.csp.day.apache.ApacheSpecialLog;
import com.taobao.csp.day.bolt.ApacheSpecialBolt;
import com.taobao.csp.day.config.SpoutCongig;
import com.taobao.csp.day.spout.ApacheSpecialSpout;

public class ApacheSpecialTopology {
	
	public static Logger logger = Logger.getLogger(ApacheSpecialTopology.class);

	public static void main(String[] args) {
		
		TopologyBuilder builder = new TopologyBuilder();
		
		int spoutNum = SpoutCongig.getApacheRequestSpoutNum();
		List<String> spoutL = new ArrayList<String>();
		for (int i = 0; i < spoutNum; i++) {
			// i为spout的编号
			ApacheSpecialSpout spout = new ApacheSpecialSpout(i, spoutNum);
			String spoutId = "spout" + i;
			
			// spout的并发数设置为1
			builder.setSpout(spoutId, spout, 1);
			spoutL.add(spoutId);
		}
		
		ApacheSpecialBolt bolt = new ApacheSpecialBolt();
		InputDeclarer inputDeclarer = builder.setBolt("bolt", bolt, 1);
		for (String spoutId : spoutL) {
			inputDeclarer.fieldsGrouping(spoutId, new Fields(ApacheSpecialLog.getGroupFields()));
		}

		Config conf = new Config();

		if (args != null && args.length > 0) {
			conf.setNumWorkers(4);

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
