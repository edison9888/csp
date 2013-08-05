package com.taobao.csp.capacity.bo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.dao.CapacityModelDao;
import com.taobao.csp.capacity.model.tree.Node;
import com.taobao.csp.capacity.model.tree.TraversalStrategy;
import com.taobao.csp.capacity.model.tree.Tree;
import com.taobao.csp.capacity.po.CapacityModelPo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/***
 * 应用级别预测模型
 * @author youji.zj
 *
 */
public class CapacityModelBo {
	
	private static Logger logger = Logger.getLogger(CapacityModelBo.class);
	
	private static Set<String> concHsf = new HashSet<String>();
	
	private static Set<String> concTair = new HashSet<String>();
	
	private final static DecimalFormat DF_LONG = new DecimalFormat("###,###");
	
	private CapacityModelDao capacityModelDao;
	
	static {
		concHsf.add("tradeplatform");
		concHsf.add("itemcenter");
		concHsf.add("shopcenter");
		concHsf.add("designcenter");
		concHsf.add("tradelogs");
		concHsf.add("timeoutcenter");
		concHsf.add("ump");
		concHsf.add("uicfinal");
		concHsf.add("delivery");
		
		// tmall
		concHsf.add("cartapi");
		concHsf.add("inventoryplatform");
		concHsf.add("tmallpromotion");
		
//		concTair.add("group_comm");
//		concTair.add("group_session");
//		concTair.add("group_snscomm_tair");
//		concTair.add("group1");
//		concTair.add("group2");
//		concTair.add("group3");
//		concTair.add("group-count");
//		concTair.add("groupitem");
//		concTair.add("group-kdbcomm");
//		concTair.add("group-market1");
//		concTair.add("group-market2");
//		concTair.add("group_kdbtc");
//		
//		concTair.add("mcomm_tair");
//		concTair.add("seckill-tair");
//		concTair.add("trade-tair");
	}
	
	public CapacityModelDao getCapacityModelDao() {
		return capacityModelDao;
	}

	public void setCapacityModelDao(CapacityModelDao capacityModelDao) {
		this.capacityModelDao = capacityModelDao;
	}
	
	public String collectData(double orders, String date) {
		StringBuffer sb = new StringBuffer();
		
		// prepare pv app data
		List<CapacityModelPo> modelData = capacityModelDao.findCapacityModelPoList();
		Map<String, Long> pvAppData = analyseModel(orders, modelData);
		Map<String, Long> datePvData = new HashMap<String, Long>();
		for (Map.Entry<String, Long> entry : pvAppData.entrySet()) {
			String appName = entry.getKey();
			long pv = capacityModelDao.findCapacityModelPv(appName, date);
			datePvData.put(appName, pv);
		}
		for (Map.Entry<String, Long> entry : pvAppData.entrySet()) {
			String resName = entry.getKey();
			Long value = entry.getValue();
			
			sb.append("<tr>");
			sb.append("<td>").append(resName).append("</td>");
			sb.append("<td>").append(DF_LONG.format(value)).append("</td>");
			
			int machineNum = CspCacheTBHostInfos.get().getIpsListByOpsName(resName).size();
			sb.append("<td>").append(machineNum).append("</td>");
			sb.append("<td>").append(DF_LONG.format(datePvData.get(resName))).append("</td>");
			sb.append("</tr>");
		}
		sb.append(";");
		
		// prepare hsf data
		Map<String, Long> hsfAppData = new HashMap<String, Long>();
		Map<String, Long> dataHsfData = new HashMap<String, Long>();
		Map<String, AppInfoPo> appInfoM = new HashMap<String, AppInfoPo>();
		for (String hsfApp : concHsf) {
			long invokeNum = capacityModelDao.findCapacityModelHsf(hsfApp, date);
			if (invokeNum == 0) {
				continue;
			}
			
			dataHsfData.put(hsfApp, invokeNum);
			
			// every hsf which needs to be analysed, build a tree
			Tree<AppTreeNodeData> tree = new Tree<AppTreeNodeData>(TraversalStrategy.DEPTH_FIRST);
			buildTree(tree, null, hsfApp, null, 0, pvAppData, appInfoM, date, 0);
			
			logger.info("compute hsf invoke:" + hsfApp);
			long predict = analyseHsfInvoke(tree, pvAppData, appInfoM);
			hsfAppData.put(hsfApp, predict);
		}
		
		for (Map.Entry<String, Long> entry : hsfAppData.entrySet()) {
			String resName = entry.getKey();
			Long value = entry.getValue();
			
			sb.append("<tr>");
			sb.append("<td>").append(resName).append("</td>");
			sb.append("<td>").append(DF_LONG.format(value)).append("</td>");
			
			int machineNum = CspCacheTBHostInfos.get().getIpsListByOpsName(resName).size();
			sb.append("<td>").append(machineNum).append("</td>");
			sb.append("<td>").append(DF_LONG.format(dataHsfData.get(resName))).append("</td>");
			sb.append("</tr>");
		}
		sb.append(";");
		
		// prepare tair data
		Map<String, Long> tairGroupData = new HashMap<String, Long>();
		for (Map.Entry<String, Long> entry : pvAppData.entrySet()) {
			String pvAppName = entry.getKey();
			long predict = entry.getValue();
			long dateActual = datePvData.get(pvAppName);
			
			Map<String, Long> pvTairInvoke = capacityModelDao.findCapacityModelTairInvoke(pvAppName, date, concTair);
			for (Map.Entry<String, Long> entry2 : pvTairInvoke.entrySet()) {
				String tairGroup = entry2.getKey();
				long invokeNum = entry2.getValue();
				long predicInvoke = invokeNum * predict / dateActual;
				
				if (tairGroupData.containsKey(tairGroup)) {
					predicInvoke += tairGroupData.get(tairGroup);
				}
				tairGroupData.put(tairGroup, predicInvoke);
			}
		}
		
		for (Map.Entry<String, Long> entry : hsfAppData.entrySet()) {
			String hsfAppName = entry.getKey();
			long predict = entry.getValue();
			long dateActual = dataHsfData.get(hsfAppName);
			
			Map<String, Long> pvTairInvoke = capacityModelDao.findCapacityModelTairInvoke(hsfAppName, date, concTair);
			for (Map.Entry<String, Long> entry2 : pvTairInvoke.entrySet()) {
				String tairGroup = entry2.getKey();
				long invokeNum = entry2.getValue();
				long predicInvoke = invokeNum * predict / dateActual;
				
				if (tairGroupData.containsKey(tairGroup)) {
					predicInvoke += tairGroupData.get(tairGroup);
				}
				tairGroupData.put(tairGroup, predicInvoke);
			}
		}
		
		for (Map.Entry<String, Long> entry : tairGroupData.entrySet()) {
			String resName = entry.getKey();
			Long value = entry.getValue();
			
			sb.append("<tr>");
			sb.append("<td>").append(resName).append("</td>");
			sb.append("<td>").append(DF_LONG.format(value)).append("</td>");
			
			sb.append("</tr>");
		}

		return sb.toString();
		
	}
	
	/***
	 * 计算各个页面系统数据的算法
	 * 1、计算from为订单的模型数据，得出结果集A加入最终结果集合，注意：from为订单的toRes，不能再有其它from
	 * 2、对剩下的模型数据，计算from为集合A中数据的，得出结果集B，加入最终结果集合
	 * 3、对剩下的模型数据，计算在存在于from中，却不存在于to中的数据，递增式加入结果集合
	 * 4、递归步骤3
	 * 
	 * 如果递归了20次还没有计算完，说明出现了环，抛出异常
	 * @param order
	 * @param modelData
	 * @param appData
	 * @return Map<String, Long>
	 */
	private Map<String, Long> analyseModel(double order, List<CapacityModelPo> modelData) {
		Map<String, Long> appData = new HashMap<String, Long>();
		
		// first step
		List<CapacityModelPo> tempL1 = new ArrayList<CapacityModelPo>();
		for (CapacityModelPo po : modelData) {
			String resFrom = po.getResFrom();
			if ("order".equals(resFrom)) {
				double ratio = po.getRelRatio();
				String resTo = po.getResTo();
				appData.put(resTo, (long)(ratio * order));
			} else {
				tempL1.add(po);
			}
		}
		
		// second step
		List<CapacityModelPo> tempL2 = new ArrayList<CapacityModelPo>();
		for (CapacityModelPo po : tempL1) {
			String resFrom = po.getResFrom();
			if (appData.containsKey(resFrom)) {
				double ratio = po.getRelRatio();
				String resTo = po.getResTo();
				long from = appData.get(resFrom);
				appData.put(resTo, (long)(ratio * from));
			} else {
				tempL2.add(po);
			}
		}
		
		// third step & forth step, if try 20 times not to the end, there must be something wrong
		int recurCount = 0;
		while (tempL2.size() > 0 && recurCount < 20) {
			tempL2 = analyseAssist(tempL2, appData, recurCount);
			recurCount++;
		}
		
		if (tempL2.size() > 0) {
			StringBuffer sb = new StringBuffer("exception:");
			for (CapacityModelPo po : tempL2) {
				sb.append(po.getResFrom()).append("->").append(po.getResTo()).append(".");
			}
			
			logger.error(sb.toString());
			// 发送旺旺消息报警异常
		}
		
		return appData;
	}
	
	private List<CapacityModelPo> analyseAssist(List<CapacityModelPo> remainData, Map<String, Long> appData, int recurCount) {
		List<CapacityModelPo> tempL = new ArrayList<CapacityModelPo>();
		
		Set<String> toS = new HashSet<String>();
		for (CapacityModelPo po : remainData) {
			toS.add(po.getResTo());
		}
		
		for (CapacityModelPo po : remainData) {
			String resFrom = po.getResFrom();
			if (toS.contains(resFrom) || !appData.containsKey(resFrom)) {
				tempL.add(po);
				continue;
			} 
			
			String resTo = po.getResTo();
			double ratio = po.getRelRatio();
			
			long origin = 0l;
			if (appData.containsKey(resTo)) {
				origin = appData.get(resTo);
			}
			appData.put(resTo, (long)(origin + ratio * appData.get(resFrom)));
		}
		
		return tempL;
	}
	
	/***
	 * 
	 * @param tree  已经构造好的树
	 * @param pvAppData 通过模型计算的前端应用调用次数
	 * @return
	 */
	private long analyseHsfInvoke(Tree<AppTreeNodeData> tree, Map<String, Long> pvAppData, Map<String, AppInfoPo> appInfoM) {
		long invokeNum = 0;
		
		if (tree.getNodes() == null || tree.getNodes().size() == 0) {
			logger.warn("tree is empty!!!!");
			return invokeNum;
		}
		
		Iterator<Node<AppTreeNodeData>> ite = tree.iterator();
		
		AAA:
		while (ite.hasNext()) {
			Node<AppTreeNodeData> node = ite.next();
			
			// 一条路径一条路径的算
			if (node.isLeaf() && "pv".equals(appInfoM.get(node.getIdentifier().getAppName()).getAppType())) {
				// 叶子节点一定是前面已经计算好的页面应用
				AppTreeNodeData data = node.getIdentifier();
				
				long path = pvAppData.get(data.getAppName());
				path = path * data.getInvokeNum() / data.getTotalProvieNum();
				
				Set<String> pathS = new LinkedHashSet<String>();
				pathS.add(data.getAppName());
				while (node.get_parent() != null) {
					node = node.get_parent();
					AppTreeNodeData nodeData = node.getIdentifier();
					if (pathS.contains(nodeData.getAppName())) {
						logger.warn("find a circle:" + pathS + "->" + nodeData.getAppName());
						continue AAA;
					} else {
						pathS.add(nodeData.getAppName());
					}
					if (node.get_parent() != null) {
						data = node.getIdentifier();
						path = path * data.getInvokeNum() / data.getTotalProvieNum();
					} else {
						invokeNum += path;
					}
				}
				logger.info("path:" + pathS);
			}
		}

		return invokeNum;
	}
	
	/***
	 * 构造树，构造一个节点，如果该节点有扩散节点则递归
	 * @param tree 构造中的树
	 * @param parent 父亲
	 * @param appName 应用名
	 * @param invokeAppName 调用的应用名
	 * @param invokeNum 调用次数
	 * @param pvAppData 通过模型计算的前端应用调用次数
	 * @param appInfoM 应用信息
	 * @param date 模型参照时间
	 * @param deep 模型的深度
	 */
	private void buildTree(Tree<AppTreeNodeData>tree, AppTreeNodeData parent, String appName, String invokeAppName, long invokeNum, 
			Map<String, Long> pvAppData, Map<String, AppInfoPo> appInfoM, String date, int deep) {
		
		
		AppInfoPo appInfoPo;
		if (!appInfoM.containsKey(appName)) {
			appInfoPo = AppInfoAo.get().getAppInfoByOpsName(appName);
			appInfoM.put(appName, appInfoPo);
		} else {
			appInfoPo = appInfoM.get(appName);
		}
		
		if (appInfoPo == null) {
			return;
		}
		
		long appTotalNum = 0;
		if (pvAppData.keySet().contains(appName)) {
			appTotalNum = capacityModelDao.findCapacityModelPv(appName, date);
			AppTreeNodeData node = new AppTreeNodeData(appName, appTotalNum, invokeAppName, invokeNum);
			tree.createNode(node, parent);
			return;
		}
		
		if ("pv".equals(appInfoPo.getAppType())) {
			return;
		}
		
		// the forth level is hsf app
		if (deep >= 5) {
			return;
		}
		
		appTotalNum = capacityModelDao.findCapacityModelHsf(appName, date);
		AppTreeNodeData node = new AppTreeNodeData(appName, appTotalNum, invokeAppName, invokeNum);
		tree.createNode(node, parent);
		
		long limit = appTotalNum / 100;
		Map<String, Long> hsfAppInvokeM = capacityModelDao.findCapacityModelHsfInvoke(appName, date, limit);
		
		for (Map.Entry<String, Long> entry : hsfAppInvokeM.entrySet()) {
			String invokeMeApp = entry.getKey();
			long invokeMeNum = entry.getValue();
			
			deep++;
			buildTree(tree, node, invokeMeApp, appName, invokeMeNum, pvAppData, appInfoM, date, deep);
		}
	}
}
