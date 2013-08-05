
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.taobao.monitor.common.po.ProductLine;

/**
 * @author xiaodu
 * 
 * 淘宝产品线的缓存
 *
 * 下午2:22:53
 */
public class TBProductCache {
	
	private static Logger logger = Logger.getLogger(TBProductCache.class);
	
	private Map<String,ProductLine> map = new HashMap<String, ProductLine>();
	
	private static TBProductCache cache = new TBProductCache();
	
	private TBProductCache(){
		map = getAllOpsAppName();
	}
	
	public static void reset(){
		cache.map = cache.getAllOpsAppName();
	}
	
	private Map<String,ProductLine> getAllOpsAppName() {
		
		
		Map<String,ProductLine> plMap = new HashMap<String, ProductLine>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String md5 = DigestUtils.md5Hex(sdf.format(new Date())+"taobao_daily");
		String url = "http://proxy.wf.taobao.org/dailymanage/searchapply.ashx?qt=1&qs=&sign="+md5;
		
		
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(new URL(url));
			List<Node> fristNodeList = doc.selectNodes("/applylist/apply");
			for(int i=0;i<fristNodeList.size();i++){
				Element fristNode = (Element)fristNodeList.get(i);//第一个节点  产品线
				Node applyname = fristNode.selectSingleNode("applyname");
				Node serviceType = fristNode.selectSingleNode("serviceType");
				Node oneProduct = fristNode.selectSingleNode("oneProduct");
				Node twoProduct = fristNode.selectSingleNode("twoProduct");
				Node tl = fristNode.selectSingleNode("TL");
				Node testTL = fristNode.selectSingleNode("testTL");
				Node SCM = fristNode.selectSingleNode("SCM");
				Node SQA = fristNode.selectSingleNode("SQA");
				Node UED = fristNode.selectSingleNode("UED");
				Node applyPic = fristNode.selectSingleNode("applyPic");
				Node PE = fristNode.selectSingleNode("PE");
				Node appOps = fristNode.selectSingleNode("appOps");
				Node appdesc = fristNode.selectSingleNode("appdesc");
//				String name = fristNode.attributeValue("name");
//				String fristid = fristNode.attributeValue("id");
				
				
				
				ProductLine pl = new ProductLine();
				pl.setName(serviceType.getStringValue());
				pl.setAppName(applyname.getStringValue());
				pl.setDevelopGroup(oneProduct.getStringValue());
				pl.setProductline(twoProduct.getStringValue());
				pl.setAppdesc(appdesc.getStringValue());
				pl.setApplyPic(applyPic.getStringValue());
				pl.setPe(PE.getStringValue());
				pl.setScm(SCM.getStringValue());
				pl.setSqa(SQA.getStringValue());
				pl.setTeamLeader(tl.getStringValue());
				pl.setTestTL(testTL.getStringValue());
				pl.setUed(UED.getStringValue());
				pl.setAppOps(appOps.getStringValue());
				
				plMap.put(pl.getAppName(), pl);
				
			}
		} catch (MalformedURLException e) {
			logger.error("",e);
		} catch (DocumentException e) {
			logger.error("",e);
		}
		
		return plMap;

	}
	
	
	
	public static ProductLine getProductLineByAppName(String appName){
		ProductLine line = cache.map.get(appName);
//		if(line ==null){
//			synchronized (TBProductCache.cache) {
//				line = cache.map.get(appName);
//				if(line ==null){
//					cache.map = cache.getAllOpsAppName();
//					line = cache.map.get(appName);
//				}
//			}
//		}
		
		if(line == null){
			line = new ProductLine();
			line.setName("未知");
			line.setAppName(appName);
			line.setDevelopGroup("未知");
			line.setProductline("未知");
		}
		return line;
	}
	
	public static void main(String[] args) {
////		TBProductCache.getProductLineByAppName("detail");
//		TBProductCache ss = new TBProductCache();
//		ss.getAllOpsAppName();
//		Map<String, ProductLine> map = ss.getAllOpsAppName();
//		for(Entry<String,ProductLine> entry : map.entrySet()) {
//			ProductLine product = entry.getValue();
//			String dev = product.getDevelopGroup();
//			if(dev == null) {
//				System.out.println(entry.getKey());	
//			}
//		}
//		System.out.println("over*********");
////		System.out.println(ss.getProductLineByAppName("file2_notify"));
		List<String> list = new ArrayList<String>();
		list.add("123");
		list.add(null);
		Collections.sort(list);
	}
	

}
