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

import com.taobao.csp.day.bolt.PinkAcessBolt;
import com.taobao.csp.day.config.SpoutCongig;
import com.taobao.csp.day.pinkie.PinkieLog;
import com.taobao.csp.day.spout.PinkieAcessSpout;

public class PinkieAccessTopology {
	
	public static Logger logger = Logger.getLogger(PinkieAccessTopology.class);

	public static void main(String[] args) {
		
		TopologyBuilder builder = new TopologyBuilder();
		
		int spoutNum = SpoutCongig.getPinkieAccessSpoutNum();
		List<String> sSpoutL = new ArrayList<String>();
		for (int i = 0; i < spoutNum; i++) {
			// i为spout的编号
			PinkieAcessSpout spout = new PinkieAcessSpout(i, spoutNum);
			String spoutId = "pinkieAccessSpout" + i;
			
			// spout的并发数设置为1
			builder.setSpout(spoutId, spout, 1);
			sSpoutL.add(spoutId);
		}
		
		PinkAcessBolt bolt = new PinkAcessBolt();
		InputDeclarer inputDeclarer = builder.setBolt("pinkieAccessBolt", bolt, 1);
		for (String spoutId : sSpoutL) {
			inputDeclarer.fieldsGrouping(spoutId, new Fields(PinkieLog.getGroupFields()));
		}

		Config conf = new Config();

		if (args != null && args.length > 0) {
			conf.setNumWorkers(14);

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
