/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.taobao.csp.common.ZKClient;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.io.Heartbeat;
import com.taobao.csp.dataserver.io.ServerInfo;
import com.taobao.csp.dataserver.io.exception.NoServerException;
import com.taobao.csp.dataserver.util.Util;

/**
 * ServerListManager 只要负责 管理服务端的server列表,
 * 
 * @author xiaodu 下午9:05:20
 */
public class ServerListManager {

	private static final Logger logger = Logger.getLogger(ServerListManager.class);

	private static long LIVE_MAX_TIME = 60 * 1000;

	private List<ServerInfo> serverIps = new CopyOnWriteArrayList<ServerInfo>();

	private List<ServerInfo> deadServer = new CopyOnWriteArrayList<ServerInfo>();

	private Object lock = new Object();

	private Timer timer = new Timer();

	public ServerListManager() {
		resetServerList();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (lock) {
					logger.info("正常cache server list size" + serverIps.size());
					for (ServerInfo s : serverIps) {
						logger.info("正常cache server ：" + s.getServerInfo());
					}

					logger.info("准备检查死亡列表  ,目前列表中个数为:" + deadServer.size());

					Iterator<ServerInfo> it = deadServer.iterator();
					while (it.hasNext()) {
						ServerInfo s = it.next();
						boolean g = checkServerStat(s);
						if (g) {
							if (!serverIps.contains(s))
								serverIps.add(s);

							deadServer.remove(s);
							logger.info(s + " 心跳检查正常 重新返回正常列表");

							sortServers();

						} else {
							logger.info(s + " 心跳检查失败");
						}
					}
				}
			}
		}, 60000, 60000);
	}

	private void sortServers() {
		List<ServerInfo> tmp = new ArrayList<ServerInfo>();
		for (ServerInfo info : serverIps) {
			tmp.add(info);
		}
		Collections.sort(tmp);
		List<ServerInfo> t = new CopyOnWriteArrayList<ServerInfo>();
		t.addAll(tmp);

		serverIps = t;
	}

	private void deadServer(ServerInfo server) {
		synchronized (lock) {
			serverIps.remove(server);

			if (!deadServer.contains(server))
				deadServer.add(server);

			logger.info(server + "进入死亡列表 等待心跳检查...");
			sortServers();
		}
	}

	public void recoveryServer(ServerInfo server) {
		deadServer(server);
	}

	private boolean checkServerStat(ServerInfo server) {
		Object object = ZKClient.get().getData(Constants.ZK_DS_ROOT_NODE + "/" + server.getServerInfo());

		if (object == null) {
			return false;
		}

		if (object instanceof Heartbeat) {
			Heartbeat heart = (Heartbeat) object;
			long time = heart.getBeat();
			if (System.currentTimeMillis() - time > LIVE_MAX_TIME) {
				return false;
			} else {
				return true;
			}
		}

		return false;
	}

	private void resetServerList() {
		List<String> serverlist = ZKClient.get().list(Constants.ZK_DS_ROOT_NODE, new ServerListWatcher());
		handleServerList(serverlist);
	}

	public void handleServerList(List<String> serverList) {
		synchronized (lock) {
			logger.info("zk 节点列表发生变化 重置服务列表");
			deadServer.clear();

			List<ServerInfo> serverIpstmp = new CopyOnWriteArrayList<ServerInfo>();

			List<ServerInfo> tmp = new ArrayList<ServerInfo>();

			for (String server : serverList) {
				ServerInfo s = new ServerInfo(server);
				logger.info("节点" + s);
				tmp.add(s);
			}
			Collections.sort(tmp);
			serverIpstmp.addAll(tmp);
			serverIps = serverIpstmp;
		}
	}

	private class ServerListWatcher implements Watcher {

		@Override
		public void process(WatchedEvent event) {

			switch (event.getType()) {
				case NodeChildrenChanged:
					resetServerList();
					break;
			}
		}
	}

	public ServerInfo selectServer(String key) throws NoServerException {
		long hash = -1;
		try {
			hash = Util.hash(key.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			logger.error("hash 统一采集GBK字节,获取GBK字节编码失败!");
			hash = Util.hash(key.getBytes());
		}
		List<ServerInfo> tmp = serverIps;
		int size = tmp.size();
		if (size > 0) {
			int m = (int) hash % size;
			ServerInfo ipInfo = tmp.get(Math.abs(m));
			return ipInfo;
		} else {
			throw new NoServerException("zookeeper[" + ZKClient.get().getZkAddress() + "] 节点 "
					+ Constants.ZK_DS_ROOT_NODE + " 可能服务IP");
		}
	}

	public List<ServerInfo> selectServers() throws NoServerException {
		if (serverIps.size() == 0) {
			throw new NoServerException("zookeeper[" + ZKClient.get().getZkAddress() + "] 节点 "
					+ Constants.ZK_DS_ROOT_NODE + " 可能服务IP");
		}

		return serverIps;
	}

	public static void main(String[] args) {
		ServerListManager slm=new ServerListManager();
		
		try {
			slm.selectServers();
		} catch (NoServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
