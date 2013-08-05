package com.taobao.csp.day.topology;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.InputDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.taobao.csp.day.bolt.GcBolt;
import com.taobao.csp.day.bolt.PinkAcessBolt;
import com.taobao.csp.day.config.SpoutCongig;
import com.taobao.csp.day.gc.GcLog;
import com.taobao.csp.day.spout.GcSpout;

public class GcTopology {
	
	public static Logger logger = Logger.getLogger(GcTopology.class);

	public static void main(String[] args) {
		
		TopologyBuilder builder = new TopologyBuilder();
		
		int spoutNum = SpoutCongig.getGcSpoutNum();
		List<String> sSpoutL = new ArrayList<String>();
		for (int i = 0; i < spoutNum; i++) {
			// i为spout的编号
			GcSpout spout = new GcSpout(i, spoutNum);
			String spoutId = "fcSpout" + i;
			
			// spout的并发数设置为1
			builder.setSpout(spoutId, spout, 1);
			sSpoutL.add(spoutId);
		}
		
		GcBolt bolt = new GcBolt();
		InputDeclarer inputDeclarer = builder.setBolt("gcBolt", bolt, 1);
		for (String spoutId : sSpoutL) {
			inputDeclarer.fieldsGrouping(spoutId, new Fields(GcLog.getGroupFields()));
		}

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
		} else {
			logger.error("please input topology name!!!");
		}
	}
}
