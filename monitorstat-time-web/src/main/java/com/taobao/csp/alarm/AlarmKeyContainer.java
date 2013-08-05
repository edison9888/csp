
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.taobao.csp.alarm.baseline.BaseLineProcessHandle;
import com.taobao.csp.alarm.check.AlarmKeyHandle;
import com.taobao.csp.common.ZKClient;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.CspKeyMode;

/**
 * @author xiaodu
 *
 * ÏÂÎç3:55:43
 */
public class AlarmKeyContainer implements Watcher {
	
	private static final Logger logger = Logger.getLogger(AlarmKeyContainer.class);
	
	private AlarmKeyHandle alarmKeyHandle = new AlarmKeyHandle();
	private String selfServerIp;
	
	private List<String> alarmServers = new ArrayList<String>();
	
	private static AlarmKeyContainer container = null;
	
	private BaseLineProcessHandle baselinehandle = new BaseLineProcessHandle(this);
	
	
	public synchronized static void startup(){
		if(container == null){
			container = new AlarmKeyContainer();
			container.readerNodeChild();
			container.activateNodeData();
			container.alarmKeyHandle.startup();
			container.baselinehandle.startup();
		}
	}
	
	
	private synchronized void  addMode(CspKeyMode mode){
		String m = mode.getAppName()+"_"+mode.getKeyName();
		if(isFit(m)){
			container.alarmKeyHandle.addMode(mode);
		}
	}
	
	public boolean isFit(String m){
		int len = alarmServers.size();
		long hash = hash(m.getBytes());
		int index = (int)(hash%len);
		String f = alarmServers.get(index);
		if(f.equals(selfServerIp)){
			return true;
		}
		return false;
	}
	
	
	private void updateMode(CspKeyMode mode){
		container.alarmKeyHandle.updateMode(mode);
	}
	private void removeMode(CspKeyMode mode){
		container.alarmKeyHandle.removeMode(mode);
	}
	
	
	public static void addMode(int id){
		String tmp = "addMode,"+id;
		try {
			ZKClient.get().setData("/csp/alarm_server", tmp);
		} catch (Exception e) {
		} 
	}
	
	public static void updateMode(int id){
		String tmp = "updateMode,"+id;
		try {
			ZKClient.get().setData("/csp/alarm_server", tmp);
		} catch (Exception e) {
		} 
	}
	public static void removeMode(int id){
		String tmp = "removeMode,"+id;
		try {
			ZKClient.get().setData("/csp/alarm_server", tmp);
		} catch (Exception e) {
		} 
	}
	
	
	private AlarmKeyContainer(){
		try {
			registerNode();	
		} catch (Exception e) {
			logger.error("ÎÞ·¨×¢²ázk", e);
		}
	}
	
	
	private void registerNode() throws Exception{
		InetAddress address = InetAddress.getLocalHost();
		selfServerIp = address.getHostAddress();
		ZKClient.get().mkdirEphemeral("/csp/alarm_server/"+address.getHostAddress());
	}
	
	
	
	private void readerAlarmMode(){
		
		container.alarmKeyHandle.clean();
		
		List<CspKeyMode> modeList = KeyAo.get().findAllKeyModes();
		for(CspKeyMode mode:modeList){
			addMode(mode);
		}
	}
	

	
	private synchronized void readerNodeChild(){
		try{
			alarmServers.clear();
			List<String>pathList = ZKClient.get().list("/csp/alarm_server", this);
			if(pathList != null&&pathList.size()>0){
				alarmServers.addAll(pathList);
				
				Collections.sort(pathList);
				
				readerAlarmMode();
			}else{
				logger.error("readerNodeChild£º"+pathList.size()+" /csp/alarm_server");
			}
		}catch (Exception e) {
			logger.error("readerNodeChild NodeDataChanged");
		}
	}
	
	
	private void activateNodeData(){
		try {
			 ZKClient.get().getData("/csp/alarm_server", this);
		} catch (Exception e) {
		}
	}
	
	
	private synchronized void readerNodeData(){
		try{
			String data = (String)ZKClient.get().getData("/csp/alarm_server", this);
			String[] g = data.split(",");
			if(g.length ==2){
				if("addMode".equals(g[0])){
					Integer id =Integer.parseInt(g[1]);
					CspKeyMode mode = KeyAo.get().getKeyMode(id);
					if(mode != null)
						addMode(mode);
				}
				if("updateMode".equals(g[0])){
					Integer id =Integer.parseInt(g[1]);
					CspKeyMode mode = KeyAo.get().getKeyMode(id);
					if(mode != null)
						updateMode(mode);
				}
				if("removeMode".equals(g[0])){
					Integer id =Integer.parseInt(g[1]);
					CspKeyMode mode = KeyAo.get().getKeyMode(id);
					if(mode != null)
						removeMode(mode);
				}
			}
		}catch (Exception e) {
			logger.error("readerNodeChild NodeDataChanged");
		}
	}
	
	

	public void process(WatchedEvent event) {
		switch (event.getType()) {
			case NodeChildrenChanged:
				readerNodeChild();
				logger.info("zk node " + event.getPath() + " NodeChildrenChanged");
			break;
			case NodeDataChanged:
				readerNodeData();
				logger.info("zk node " + event.getPath() + " NodeDataChanged");
				break;

		}
	}
	
	
	
	
	private static final int MURMURHASH_M = 0x5bd1e995;
	
	 private static long hash(byte[] key) {
		    int len = key.length;
		    int h = 97 ^ len;
		    int index = 0;

		    while (len >= 4) {
		      int k = (key[index] & 0xff) | ((key[index + 1] << 8) & 0xff00)
		        | ((key[index + 2] << 16) & 0xff0000)
		        | (key[index + 3] << 24);

		      k *= MURMURHASH_M;
		      k ^= (k >>> 24);
		      k *= MURMURHASH_M;
		      h *= MURMURHASH_M;
		      h ^= k;
		      index += 4;
		      len -= 4;
		    }

		    switch (len) {
		      case 3:
		        h ^= (key[index + 2] << 16);

		      case 2:
		        h ^= (key[index + 1] << 8);

		      case 1:
		        h ^= key[index];
		        h *= MURMURHASH_M;
		    }

		    h ^= (h >>> 13);
		    h *= MURMURHASH_M;
		    h ^= (h >>> 15);
		    return ((long) h & 0xffffffffL);
		  }
	 public static void main(String args[]){
		AlarmKeyContainer.startup(); 
	 }
}
