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

import com.taobao.csp.day.bolt.SphBolt;
import com.taobao.csp.day.config.SpoutCongig;
import com.taobao.csp.day.sph.SphLog;
import com.taobao.csp.day.spout.SphSpout;
import com.taobao.csp.day.spout.TddlSpout;

public class SphTopology {
	
	public static Logger logger = Logger.getLogger(SphTopology.class);

	public static void main(String[] args) {
		
		TopologyBuilder builder = new TopologyBuilder();
		
		// 构造sph
		int spoutNum = SpoutCongig.getSphSpoutNum();
		List<String> sphSpoutL = new ArrayList<String>();
		for (int i = 0; i < spoutNum; i++) {
			// i为spout的编号
			SphSpout sphSpout = new SphSpout(i, spoutNum);
			String spoutId = "sphSpout" + i;
			
			// spout的并发数设置为1
			builder.setSpout(spoutId, sphSpout, 1);
			sphSpoutL.add(spoutId);
		}
		
		SphBolt sphBolt = new SphBolt();
		InputDeclarer inputDeclarer = builder.setBolt("sphBolt", sphBolt, 6);
		for (String spoutId : sphSpoutL) {
			inputDeclarer.fieldsGrouping(spoutId, new Fields(SphLog.getGroupFields()));
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
