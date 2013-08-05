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
 * ServerListManager ֻҪ���� �������˵�server�б�,
 * 
 * @author xiaodu ����9:05:20
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
					logger.info("����cache server list size" + serverIps.size());
					for (ServerInfo s : serverIps) {
						logger.info("����cache server ��" + s.getServerInfo());
					}

					logger.info("׼����������б�  ,Ŀǰ�б��и���Ϊ:" + deadServer.size());

					Iterator<ServerInfo> it = deadServer.iterator();
					while (it.hasNext()) {
						ServerInfo s = it.next();
						boolean g = checkServerStat(s);
						if (g) {
							if (!serverIps.contains(s))
								serverIps.add(s);

							deadServer.remove(s);
							logger.info(s + " ����������� ���·��������б�");

							sortServers();

						} else {
							logger.info(s + " �������ʧ��");
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

			logger.info(server + "���������б� �ȴ��������...");
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
			logger.info("zk �ڵ��б����仯 ���÷����б�");
			deadServer.clear();

			List<ServerInfo> serverIpstmp = new CopyOnWriteArrayList<ServerInfo>();

			List<ServerInfo> tmp = new ArrayList<ServerInfo>();

			for (String server : serverList) {
				ServerInfo s = new ServerInfo(server);
				logger.info("�ڵ�" + s);
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
			logger.error("hash ͳһ�ɼ�GBK�ֽ�,��ȡGBK�ֽڱ���ʧ��!");
			hash = Util.hash(key.getBytes());
		}
		List<ServerInfo> tmp = serverIps;
		int size = tmp.size();
		if (size > 0) {
			int m = (int) hash % size;
			ServerInfo ipInfo = tmp.get(Math.abs(m));
			return ipInfo;
		} else {
			throw new NoServerException("zookeeper[" + ZKClient.get().getZkAddress() + "] �ڵ� "
					+ Constants.ZK_DS_ROOT_NODE + " ���ܷ���IP");
		}
	}

	public List<ServerInfo> selectServers() throws NoServerException {
		if (serverIps.size() == 0) {
			throw new NoServerException("zookeeper[" + ZKClient.get().getZkAddress() + "] �ڵ� "
					+ Constants.ZK_DS_ROOT_NODE + " ���ܷ���IP");
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
