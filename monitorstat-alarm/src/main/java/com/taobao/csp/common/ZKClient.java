
/**
 * monitorstat-alarm
 */
package com.taobao.csp.common;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.filter.HbaseFilter;
import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

/**
 * @author xiaodu
 *
 * 上午9:35:55
 */
/**
 * @author xiaodu
 *
 * 上午9:35:55
 */
public class ZKClient implements ManagerListener{

	private static final Logger logger = Logger.getLogger("zookeeper");

	private  Kryo kryo = new Kryo();

	private ZooKeeper zookeeper = null;

	private Integer mutex;

	private AtomicBoolean connected = new AtomicBoolean(false);

	private String zkAddress = "";

	private DiamondManager diamondManager = new DefaultDiamondManager("DEFAULT_GROUP", "com.taobao.taokeeper.serverlist", this);

	private static ZKClient zk =null;

	private List<ReconnectCallBack> reconnectCallBackList = new CopyOnWriteArrayList<ReconnectCallBack>();

	public static synchronized ZKClient get() {

		if(zk  == null){
			zk = new ZKClient();
		}

		return zk;
	}

	private ZKClient(){
		init();
	}


	private synchronized boolean connectZk(){

		connected.set(false);

		if(zookeeper !=null){
			try {
				zookeeper.close();
			} catch (InterruptedException e) {
			}
		}

		try {
			logger.info("Connecting to  zookeeper server:" + zkAddress);
			mutex = new Integer(-1);
			synchronized (mutex) {
				zookeeper = new ZooKeeper(zkAddress, Constants.ZK_TIME_OUT, new ZookeeperWatcher());
				logger.info("Waiting for connection to be established ");
				mutex.wait();
				if(!connected.get()){
					logger.info("unable for connection to be established ");
					return false;
				}
			}

			Stat stat = zookeeper.exists(Constants.ZK_CSP_ROOT_NODE, null);
			if(stat == null){
				zookeeper.create(Constants.ZK_CSP_ROOT_NODE, "csp根节点".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			logger.info("Connected to zookeeper with sessionid: "
					+ zookeeper.getSessionId() + " and session timeout(ms): "
					+ Constants.ZK_TIME_OUT);

			return true;
		} catch (Exception e) {
			logger.error("Failed to connect to zookeeper:" + e.getMessage(),
					e);
			return false;
		}
	}


	private class ZookeeperWatcher implements Watcher{

		@Override
		public void process(WatchedEvent event) {

			switch(event.getState()){
			case SyncConnected : 
				synchronized (mutex) {
					connected.set(true);
					logger.info("zk connected is success");
					mutex.notify();
				}
				break;
			case Expired :
				synchronized (mutex) {
					connected.set(false);
					logger.info("zk connected is Expired");
					mutex.notify();
				}
				break;
			case Disconnected :
				synchronized (mutex) {
					connected.set(false);
					logger.info("zk connected is  Disconnected");
					mutex.notify();
				}
				break;
			}
		}

	}



	public void close(){

		if(zookeeper!= null){
			try {
				zookeeper.close();
			} catch (InterruptedException e) {
			}
			logger.info("zookeeper is closed ！");
		}
		if(diamondManager != null)
			diamondManager.close();
	}


	private synchronized void init(){
		zkAddress = diamondManager.getAvailableConfigureInfomation(10000);
		if(zkAddress == null){
			logger.error("can not findzk Address ,System.exit");
			System.exit(-1);
		}
		try {
			connectZk();
			logger.info("start zookeeper  ！");
		} catch (Exception e) {
			logger.error("start zookeeper  ！",e);
		}
	}


	private void checkconnection(){
		if(zookeeper.getState().isAlive()&&zookeeper.getState() == ZooKeeper.States.CONNECTED ){
		}else{
			boolean t = connectZk();
			if(t){
				if(reconnectCallBackList != null){
					for(ReconnectCallBack reconnectCallBack :reconnectCallBackList)
						reconnectCallBack.doHandle();
				}
			}
		}
	}



	public boolean isExists(String path) {
		checkconnection();
		try {
			Stat stat =  zookeeper.exists(path, null);
			if(stat != null){
				return true;
			}
		} catch (Exception e) {
			logger.error("isExists :"+path,e);
		}
		return false; 
	}


	public void setData(String path, Object obj) {

		checkconnection();

		logger.info("set path :"+path+" data:"+obj);
		Output output = new Output(new ByteArrayOutputStream());
		kryo.writeClassAndObject(output, obj);
		byte[] ser = output.toBytes();
		try {
			if(isExists(path)) {
				zookeeper.setData(path, ser, -1);
			} 
		} catch(Exception e) {
			logger.error("set path :"+path+" data:"+obj,e);
		}        
	}




	public void delete(String path) {

		checkconnection();

		logger.info("delete :"+path);
		try {
			zookeeper.delete(path,-1);
		} catch (Exception e) {
			logger.error("delete :"+path,e);
		}
	}


	public List<String> list(String path) {

		checkconnection();

		logger.info("list :"+path);
		try {
			if(!isExists(path)) {
				return new ArrayList<String>();
			} else {
				return zookeeper.getChildren(path, false);
			}
		} catch(Exception e) {
			logger.error("list :"+path,e);
		}  

		return new ArrayList<String>();
	}

	public List<String> list(String path,Watcher watcher) {

		checkconnection();

		logger.info("list and watcher :"+path);
		try {
			if(!isExists(path)) {
				return new ArrayList<String>();
			} else {
				return zookeeper.getChildren(path, watcher);
			}
		} catch(Exception e) {
			logger.error("list and watcher :"+path,e);
		}   

		return new ArrayList<String>();
	}


	public void mkdirEphemeral(String path) {
		mkdir(path,null,CreateMode.EPHEMERAL);
	}

	public void mkdirPersistent(String path) {
		mkdir(path,null,CreateMode.PERSISTENT);
	}

	public void mkdirEphemeral(String path,Object value) {
		mkdir(path,value,CreateMode.EPHEMERAL);
	}

	public void mkdirPersistent(String path,Object value) {
		mkdir(path,value,CreateMode.PERSISTENT);
	}



	private void mkdir(String path,Object value,CreateMode mode) {
		if(value == null){
			value = new Object();
		}
		checkconnection();
		logger.info("mkdir :"+path+" mode:"+mode);
		try {
			if(!isExists(path)) {
				String[] p = StringUtils.split(path,"/");
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<p.length;i++){
					String a = p[i];
					sb.append("/"+a);
					if(!isExists(sb.toString())){
						if(i!=p.length -1){
							zookeeper.create(sb.toString(), new byte[0],  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
						}else{
							Output output = new Output(new ByteArrayOutputStream());
							kryo.writeClassAndObject(output, value);
							byte[] ser = output.toBytes();
							zookeeper.create(sb.toString(), ser,  Ids.OPEN_ACL_UNSAFE, mode);
						}
					}
				}
			} 
		} catch(Exception e) {
			logger.error("mkdir :"+path+" mode:"+mode,e);
		}        
	}



	public Object getData(String path) {

		checkconnection();

		logger.info("getData :"+path);

		try {
			if(isExists(path)) {
				byte[] t = zookeeper.getData(path, false, null);
				return kryo.readClassAndObject(new Input(t));
			} else {
				return null;
			}
		} catch(Exception e) {
			logger.error("getData :"+path,e);
		}
		return null;
	}

	public Object getData(String path,Watcher watcher) {

		checkconnection();

		logger.info("getData and watcher:"+path);
		try {
			if(isExists(path)) {
				byte[] t = zookeeper.getData(path, watcher, null);
				if(t.length >0)
					return kryo.readClassAndObject(new Input(t));
				else 
					return null;
			} else {
				return null;
			}
		} catch(Exception e) {
			logger.error("getData and watcher:"+path,e);
		}
		return null;
	}

	public Executor getExecutor() {
		return null;
	}


	public void receiveConfigInfo(String configInfo) {
		zkAddress = configInfo;

		logger.info("diamond receiveConfigInfo:"+configInfo);

		init();
	}


	public  String getZkAddress(){
		return zkAddress;
	}


	public void addReconnectCallBack(ReconnectCallBack reconnectCallBack){
		this.reconnectCallBackList .add(reconnectCallBack);
	}

	public void removeReconnectCallBack(ReconnectCallBack reconnectCallBack){
		this.reconnectCallBackList .remove(reconnectCallBack);
	}

	public interface ReconnectCallBack{
		public void doHandle();
	}

	public static void main(String[] args) {
		//		ZKClient.get().mkdirPersistent(HbaseFilter.ZK_GLOBLE_APP_ROOT + "/tripagent");
		//		ZKClient.get().mkdirPersistent(HbaseFilter.ZK_GLOBLE_KEY_ROOT);
		//		ZKClient.get().mkdirPersistent(HbaseFilter.ZK_SINGLE_CONFIG_ROOT);
		//		ZKClient.get().delete("/csp/data_cache_server/10.232.135.197:16512");
		//		ZKClient.get().delete(HbaseFilter.ZK_GLOBLE_APP_ROOT + "/tripagent");
		Scanner Console=new Scanner(System.in); //实例化参数
		String input = "";
		while(!input.equals("end")) {
			System.out.print("请输入字符 :"); 
			input = Console.next(); //获取输入内容给 input
			String[] array = input.split(";");
			if(array[0].equalsIgnoreCase("add")) {
				ZKClient.get().mkdirPersistent(array[1]);
				System.out.println(input);				
			} else {
				ZKClient.get().delete(array[1]);
				System.out.println(input);	
			}
		}
	} 
}
