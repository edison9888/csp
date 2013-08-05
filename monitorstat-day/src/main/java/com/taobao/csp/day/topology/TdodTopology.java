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

import com.taobao.csp.day.bolt.TdodBolt;
import com.taobao.csp.day.config.SpoutCongig;
import com.taobao.csp.day.spout.TdodSpout;
import com.taobao.csp.day.tdod.TdodLog;

public class TdodTopology {
	
	public static Logger logger = Logger.getLogger(TdodTopology.class);

	public static void main(String[] args) {
		
		TopologyBuilder builder = new TopologyBuilder();
		
		// 构造
		int spoutNum = SpoutCongig.getTdodSpoutNum();
		List<String> spoutL = new ArrayList<String>();
		for (int i = 0; i < spoutNum; i++) {
			// i为spout的编号
			TdodSpout spout = new TdodSpout(i, spoutNum);
			String spoutId = "tdodSpout" + i;
			
			// spout的并发数设置为1
			builder.setSpout(spoutId, spout, 1);
			spoutL.add(spoutId);
		}
		
		TdodBolt bolt = new TdodBolt();
		InputDeclarer inputDeclarer = builder.setBolt("tdodBolt", bolt, 6);
		for (String spoutId : spoutL) {
			inputDeclarer.fieldsGrouping(spoutId, new Fields(TdodLog.getGroupFields()));
		}

		Config conf = new Config();

		if (args != null && args.length > 0) {
			conf.setNumWorkers(2);

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
