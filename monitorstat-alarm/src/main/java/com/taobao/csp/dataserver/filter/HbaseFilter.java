package com.taobao.csp.dataserver.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.taobao.csp.common.ZKClient;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.filter.listen.AppKeyListen;
import com.taobao.csp.dataserver.filter.listen.AppScopeListen;
import com.taobao.csp.dataserver.filter.listen.GlobleAppListen;
import com.taobao.csp.dataserver.filter.listen.GlobleKeyListen;
import com.taobao.csp.dataserver.item.KeyScope;

public class HbaseFilter implements GlobleAppListen, GlobleKeyListen, AppKeyListen, AppScopeListen  {
	private static final Logger logger = Logger.getLogger("zookeeper");

	public final static String ZK_GLOBLE_KEY_ROOT = "/csp/hfilter/globlekey";   //全局过滤掉某个key
	public final static String ZK_GLOBLE_APP_ROOT = "/csp/hfilter/globleapp";	//全局过滤掉某个app
	public final static String ZK_APP_KEY_ROOT = "/csp/hfilter/appkey";			//app的某个key被过滤掉
	public final static String ZK_APP_SCOPE_ROOT = "/csp/hfilter/appscopeonly";	//app的所有key都采用APP级别，HOST级别被过滤

	private static long lastUpdateTime = 0l;	//上一次同步时间
	private HbaseFilter(){
		try {
			logger.info("HbaseFilter 初始化...");
			ZKClient.get().addReconnectCallBack(
					new ZKClient.ReconnectCallBack() {
						@Override
						public void doHandle() {
							logger.info("过滤器添加回调成功!");
							List<String> apps = watcherGlobleApp();
							List<String> keys = watcherGlobleKey();
							List<String> appKeyList = watcherAppKeyConfig();
							List<String> appScopeList = watcherAppScopeConfig();
							
							printList("gloable app", apps);
							printList("gloable key", keys);
							printList("appKeyList", appKeyList);
							printList("appScopeList", appScopeList);
						}
					});
			List<String> apps = watcherGlobleApp();
			List<String> keys = watcherGlobleKey();
			List<String> appKeyList = watcherAppKeyConfig();
			List<String> appScopeList = watcherAppScopeConfig();

			printList("gloable app", apps);
			printList("gloable key", keys);
			printList("appKeyList", appKeyList);
			printList("appScopeList", appScopeList);

		} catch (Exception e) {
			logger.error("Hbase插入过滤器初始化异常", e);
		}
		gloabKeyFilgerSet.add("mbean");
		gloabKeyFilgerSet.add("tbsession");
		gloabKeyFilgerSet.add("tp交易相关");
		gloabKeyFilgerSet.add("icdb操作相关");
	}
	private static HbaseFilter filter = new HbaseFilter();

	//全局APP过滤
	private Set<String> gloabAppFilgerSet = new HashSet<String>();
	//全局Key过滤
	private Set<String> gloabKeyFilgerSet = new HashSet<String>();
	private Map<String, Set<String>> appKeyMap = new HashMap<String, Set<String>>();
	private Set<String> appOnlySet = new HashSet<String>();
	
	public static HbaseFilter getHbaseFilter() {
		return filter;
	}

	public void resetHbaseFilter(Set<String> gloabKeyFilgerSet, 
			Set<String> gloabAppFilgerSet, Map<String, Set<String>> appKeyMap, Set<String> appOnlySet) {
		if(gloabKeyFilgerSet != null && gloabAppFilgerSet != null && appKeyMap != null && appOnlySet != null) {
			logger.info("更改HbaseFilter过滤器");
			this.gloabKeyFilgerSet = gloabKeyFilgerSet;
			this.gloabAppFilgerSet = gloabAppFilgerSet;
			this.appKeyMap = appKeyMap;
			this.appOnlySet = appOnlySet;
		}
		logger.info("gloabKeyFilgerSet=" + gloabKeyFilgerSet);
		logger.info("gloabAppFilgerSet=" + gloabAppFilgerSet);
		logger.info("appOnlySet=" + appOnlySet);
		logger.info("appOnlySet=" + appOnlySet);
	}

	public void setGloabAppFilgerSet(Set<String> gloabAppFilgerSet) {
		if(gloabAppFilgerSet != null) {
			logger.info("更改gloabAppFilgerSet过滤器");
			this.gloabAppFilgerSet = gloabAppFilgerSet;
			printList("gloable app", new ArrayList<String>(gloabAppFilgerSet));
		}
	}

	public void setAppOnlySet(Set<String> appOnlySet) {
		if(appOnlySet != null) {
			logger.info("更改appOnlySet过滤器");
			this.appOnlySet = appOnlySet;
			printList("appOnlySet", new ArrayList<String>(appOnlySet));
		}
	}

	public void setGloabKeyFilgerSet(Set<String> gloabKeyFilgerSet) {
		if(gloabKeyFilgerSet != null) {
			logger.info("更改singleKey过滤器");
			gloabKeyFilgerSet.add("mbean");
			gloabKeyFilgerSet.add("tbsession");
			gloabKeyFilgerSet.add("tp交易相关");
			gloabKeyFilgerSet.add("icdb操作相关");
			this.gloabKeyFilgerSet = gloabKeyFilgerSet;
			printList("gloable key", new ArrayList<String>(gloabKeyFilgerSet));
		}
	}

	public void setAppKeyMap(Map<String, Set<String>> appKeyMap) {
		if(appKeyMap != null) {
			logger.info("更改appKeyMap过滤器");
			this.appKeyMap = appKeyMap;
			printList("app-key 过滤器", new ArrayList<String>(appKeyMap.keySet()));
		}
	}

	/**
	 * 是否过滤掉key
	 * @param fullKey
	 * @param appName
	 * @param keyScope
	 * @return
	 */
	public boolean isHbase(String fullKey, String appName, KeyScope keyScope) {
		if(keyScope == null || keyScope.equals(KeyScope.ALL) || fullKey == null) {
			logger.error("Scope类型错误->keyScope=" + keyScope + ";fullKey=" + fullKey);
			return false;
		}
		if(keyScope.equals(KeyScope.APP)) {
			return isHbaseForApp(appName, fullKey);
		} else if(keyScope.equals(KeyScope.HOST)) {
			return isHbaseHost(appName, fullKey);
		}  
		return false;
	}	

	public boolean isHbaseForApp(String appName, String fullKey) {
		try {
			String keyPrefix = null;
			int length = fullKey.indexOf(Constants.S_SEPERATOR);
			if (length > 0) {
				keyPrefix = fullKey.substring(0, length);
			} else {
				keyPrefix = fullKey;
			}
			if (gloabKeyFilgerSet.contains(keyPrefix)
					|| gloabAppFilgerSet.contains(appName) || keyPrefix == null)
				return false;

			if (appKeyMap.containsKey(appName)) {
				Set<String> keySet = appKeyMap.get(appName);
				if (keySet.contains(keyPrefix)) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}

	private boolean isHbaseHost(String appName, String fullKey) {
		return !appOnlySet.contains(appName) && isHbaseForApp(appName, fullKey);
	}

	private List<String> watcherGlobleKey() {
		return ZKClient.get().list(ZK_GLOBLE_KEY_ROOT,new Watcher() {
			@Override
			public void process(WatchedEvent event) {

				switch (event.getType()) {
				case None:

					break;
				case NodeCreated:

					break;
				case NodeDeleted:

					break;
				case NodeDataChanged:

					break;
				case NodeChildrenChanged:
					logger.info(ZK_GLOBLE_KEY_ROOT+" watcherGlobleKey NodeChildrenChanged  ...");
					List<String> globleKeys = watcherGlobleKey();
					if(globleKeys!=null){
						filter.gloableKeyChange(globleKeys);
					} else {
						logger.info(ZK_GLOBLE_KEY_ROOT + "子节点为空");
					}
					break;
				default:
					break;
				}
			}
		});
	}

	private List<String> watcherGlobleApp() {
		return ZKClient.get().list(ZK_GLOBLE_APP_ROOT,new Watcher() {
			@Override
			public void process(WatchedEvent event) {

				switch (event.getType()) {
				case None:

					break;
				case NodeCreated:

					break;
				case NodeDeleted:

					break;
				case NodeDataChanged:

					break;
				case NodeChildrenChanged:
					logger.info(ZK_GLOBLE_APP_ROOT+" watcherGlobleApp NodeChildrenChanged  ...");
					List<String> globleApps = watcherGlobleApp();
					if(globleApps!=null){
						filter.gloableAppChange(globleApps);
					} else {
						logger.info(ZK_GLOBLE_APP_ROOT + "子节点为空");
					}
					break;
				default:
					break;
				}
			}
		});
	}

	private List<String> watcherAppKeyConfig() {
		return ZKClient.get().list(ZK_APP_KEY_ROOT,new Watcher() {
			@Override
			public void process(WatchedEvent event) {

				switch (event.getType()) {
				case None:

					break;
				case NodeCreated:

					break;
				case NodeDeleted:

					break;
				case NodeDataChanged:

					break;
				case NodeChildrenChanged:
					logger.info(ZK_APP_KEY_ROOT + " watcherAppKeyConfig NodeChildrenChanged  ...");
					List<String> configs = watcherAppKeyConfig();
					if(configs!=null){
						filter.appKeyChange(configs);
					} else {
						logger.info(ZK_APP_KEY_ROOT + "子节点为空");
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	private List<String> watcherAppScopeConfig() {
		return ZKClient.get().list(ZK_APP_SCOPE_ROOT,new Watcher() {
			@Override
			public void process(WatchedEvent event) {

				switch (event.getType()) {
				case None:

					break;
				case NodeCreated:

					break;
				case NodeDeleted:

					break;
				case NodeDataChanged:

					break;
				case NodeChildrenChanged:
					logger.info(ZK_APP_SCOPE_ROOT + " watcherAppScopeConfig NodeChildrenChanged  ...");
					List<String> configs = watcherAppScopeConfig();
					if(configs!=null){
						filter.appScopeChange(configs);
					} else {
						logger.info(ZK_APP_SCOPE_ROOT + "子节点为空");
					}
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public void gloableKeyChange(List<String> keys) {
		if(keys != null) {
			Set<String> keySet = new HashSet<String>(keys);
			this.setGloabKeyFilgerSet(keySet);
			lastUpdateTime = System.currentTimeMillis();
		}
	}

	@Override
	public void gloableKeyAdd(String Key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gloableKeyDelete(String Key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gloableAppChange(List<String> apps) {
		if(apps != null) {
			Set<String> keySet = new HashSet<String>(apps);
			this.setGloabKeyFilgerSet(keySet);			
		}
		lastUpdateTime = System.currentTimeMillis();
	}

	@Override
	public void gloableAppAdd(String app) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gloableAppDelete(String app) {
		// TODO Auto-generated method stub

	}

	public void printList(String msg, List<String> list) {
		try {
			logger.info("*****" + msg);
			if (list == null) {
				logger.info("list=null");
			} else {
				logger.info(Arrays.toString(list.toArray()));
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	public static long getLastUpdateTime() {
		return lastUpdateTime;
	}

	@Override
	public void appScopeChange(List<String> configs) {
		if(configs != null) {
			Set<String> appSet = new HashSet<String>(configs);
			this.setAppOnlySet(appSet);
		}
		lastUpdateTime = System.currentTimeMillis();
	}

	@Override
	public void appScopeAdd(String config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appScopeDelete(String config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appKeyChange(List<String> configs) {
		if(configs != null) {
			Map<String, Set<String>> map = new HashMap<String, Set<String>>();
			for(String config : configs) {
				logger.info("配置config=" +config);/*格式：appname;fullkey;*/
				String[] array = config.split(";");
				if(array.length == 2) {
					Set<String> keySet = map.get(array[0]);
					if(keySet == null) {
						keySet = new HashSet<String>();
						keySet.add(array[1]);
						map.put(array[0], keySet);
					}
				} else {
					logger.error("config配置不正确=" + config);
				}
			}
			this.setAppKeyMap(map);
			lastUpdateTime = System.currentTimeMillis();
		}
	}

	@Override
	public void appKeyAdd(String config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appKeyDelete(String config) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		String fullKey = "PV`wsdfsfsdf";
		String keyPrefix = null;
		int length = fullKey.indexOf(Constants.S_SEPERATOR);
		if (length > 0) {
			keyPrefix = fullKey.substring(0, length);
		} else {
			keyPrefix = fullKey;
		}
		System.out.println(keyPrefix);
		Set<String> gloabAppFilgerSet = new HashSet<String>();
		Set<String> gloabKeyFilgerSet = new HashSet<String>();
		Map<String, Set<String>> singleApp = new HashMap<String, Set<String>>();
		gloabAppFilgerSet.add("itemcenter");
		gloabKeyFilgerSet.add("PV-refer");

		//		singleApp.put("shopsystem", scopeSet);

//		HbaseFilter.getHbaseFilter().resetHbaseFilter(gloabKeyFilgerSet, gloabAppFilgerSet, singleApp);
//
//		System.out.println(HbaseFilter.getHbaseFilter().isHbase("HSF-Consumer`itemcenter", "itemcenter"));
//		System.out.println(HbaseFilter.getHbaseFilter().isHbase("PV-refer`itemcenter", "itemcenter"));
//		System.out.println(HbaseFilter.getHbaseFilter().isHbase("HSF-Consumer`itemcenter", "shopsystem"));
//		System.out.println(HbaseFilter.getHbaseFilter().isHbase("HSF-Consumer`itemcenter", "shopsystem"));
	}
	
}
