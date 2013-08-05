package com.taobao.monitor.common.ao.center;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.taobao.monitor.common.po.CspCallsRelationship;
import com.taobao.monitor.common.po.CspTimeKeyDependInfo;
import com.taobao.monitor.common.po.TreeGridBasePo;
import com.taobao.monitor.common.po.TreeGridData;


public class DependRelationAo {
  private final static DependRelationAo dependAo = new DependRelationAo();
  private DependRelationAo() {
  }
  
  public static DependRelationAo get() {
    return dependAo;
  }
  
//  public List<TreeGridData> getMultiRelationBySourceKey(String sourceKey) {
//    int i=1;
//    Map<String, List<CspCallsRelationship>> map = EagleeyeDataAo.get().findCallsRelationship(sourceKey);
//    List<TreeGridData> rootList = new ArrayList<TreeGridData>();
//    for(Entry<String, List<CspCallsRelationship>> entry : map.entrySet()) {
//      List<CspCallsRelationship> singleTopoList = entry.getValue();
//      String sourceUrl = entry.getKey(); //从查询处开始展示
//      //System.out.println(sourceUrl);
//      TreeGridData root = new TreeGridData();
//      Map<String, List<CspCallsRelationship>> keyNameMap = new HashMap<String, List<CspCallsRelationship>>();
//      for(CspCallsRelationship rate : singleTopoList) {
//        List<CspCallsRelationship> list = keyNameMap.get(rate.getOrigin()); 
//        if(list == null) {
//          list = new ArrayList<CspCallsRelationship>();
//          keyNameMap.put(rate.getOrigin(), list);
//        }
//        list.add(rate);
//      }
//      root.setId(i++);
//      root.setActuralRate("1");
//      root.setSourceUrl(sourceUrl);
//      root.setKeyName(sourceKey + "(<strong>发起调用的URL：" + sourceUrl + "</strong>)");
//      root.setRate("1");
//
//      List<CspCallsRelationship> firstSub = keyNameMap.get(sourceKey);
//      root.setChildren(new TreeGridData[firstSub.size()]);
//      setSingleTopoData(keyNameMap, root, sourceKey); 
//      rootList.add(root);
//    }
//    return rootList;
//  }
//  
  /**
   * 读取callrelationship表，直接生成表格需要的树
   * @param sourceKey
   * @return
   */
  public TreeGridData getSingleRelationBySourceKey(String sourceKey) {
    TreeGridData root = new TreeGridData();
    List<CspCallsRelationship> singleTopoList = EagleeyeDataAo.get().findCallsRelationshipBySourceUrl(sourceKey);
    String sourceUrl = sourceKey;
    Map<String, List<CspCallsRelationship>> keyNameMap = new HashMap<String, List<CspCallsRelationship>>();
    root.setKeyName(sourceUrl);
    preparedForSetSingleTopoData(root, singleTopoList, keyNameMap); 
    return root;
  }
  //把数据填入到TreeGridData中
  private void preparedForSetSingleTopoData(TreeGridData root, List<CspCallsRelationship> singleTopoList, 
      Map<String, List<CspCallsRelationship>> keyNameMap) {
    for(CspCallsRelationship rate : singleTopoList) {
      List<CspCallsRelationship> list = keyNameMap.get(rate.getOrigin()); 
      if(list == null) {
        list = new ArrayList<CspCallsRelationship>();
        keyNameMap.put(rate.getOrigin(), list);
      }
      list.add(rate);
    }
    root.setId(1);
    root.setActuralRate("1");
    root.setRate("1");
    
    List<CspCallsRelationship> firstSub = keyNameMap.get(root.getKeyName());
    if(firstSub != null) {
      root.setChildren(new TreeGridData[firstSub.size()]);
      setSingleTopoData(keyNameMap, root, root.getKeyName());      
    }
  }
  //生成一颗树
  private void setSingleTopoData(Map<String, List<CspCallsRelationship>> keyNameMap, TreeGridBasePo root, String curKey) {
    List<CspCallsRelationship> firstSub = keyNameMap.get(curKey);
    if(firstSub == null)
      return;
    if(firstSub.size() > 0) {
      TreeGridData parent = (TreeGridData)root;
      TreeGridData[] subArray = new TreeGridData[firstSub.size()];
      parent.setChildren(subArray);
      int i=0;
      for(CspCallsRelationship po: firstSub) {
        subArray[i] = new TreeGridData();
        subArray[i].setId(parent.getId()*10 + i);
        float result = Float.parseFloat(parent.getActuralRate())*po.getRate();
        int scale = 4;//设置位数   
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.   
        BigDecimal bd = new BigDecimal(result);   
        bd = bd.setScale(scale,roundingMode);   
        String acturalRate = bd.toString();
        bd = new BigDecimal(po.getRate());   
        bd = bd.setScale(scale,roundingMode);           
        
        String rate = bd.toString();
        
        subArray[i].setActuralRate(acturalRate);
        subArray[i].setKeyName(po.getTarget());
        subArray[i].setAppName(po.getTargetApp());  //添加应用名称
        subArray[i].setRate(rate);
        setSingleTopoData(keyNameMap, subArray[i], po.getTarget());
        i++;
      }
    }
  }
  
//  public String changeDependKeyToEagleeyeKey(String sourceKey) {
//    if(sourceKey == null)
//      sourceKey = "";
//    if(sourceKey.indexOf("http:") != 0) {  //不是url，即不是已"http:"开头的。
//      //com.taobao.item.service.ItemQueryService:1.0.0_queryItemById~lQA
//      //一般也就~lQ、lQA、LQ、LQA 、lA 几种
//      sourceKey = sourceKey.replace('_', '|');
//      int index = sourceKey.indexOf("~");
//      if(index > 0) {
//        sourceKey = sourceKey.substring(0, index);
//      }
//    } else {  //url的情况
//    //sourceKey = "http://design.taobao.com/common/modulePreview.htm";      
//    }
//	  //Eagleeyekey 就是depend格式的key
//    return sourceKey;
//  }
  
//  public String changEagleeyeKeyToTimeKey(String sourceKey) {
//    //HSF-provider`com.taobao.uic.common.service.userinfo.UicReadService:1.0.0`getBaseUserByUserId
//    //PV`http://detail.tmall.com/item.htm
//    if(sourceKey == null)
//      return sourceKey = "";
//    else {
//      sourceKey = sourceKey.trim();
//      sourceKey = sourceKey.replace('_', '`');      
//    } 
//    
//    if(sourceKey.startsWith("http:")) {
//      sourceKey = "PV`" + sourceKey;
//    } else
//      sourceKey = "HSF-provider`" + sourceKey;
//    return sourceKey;
//  }
  
  /**
   * 把CspTimeKeyDependInfo的一条拓扑转换成表格识别的格式
   * @param timeList
   * @return
   */
  public TreeGridData formatCspTimeListToSingleTreeGridData(List<CspTimeKeyDependInfo> timeList) {
    TreeGridData root = new TreeGridData();
    String sourceUrl = "";
    String sourceApp = "";
    List<CspCallsRelationship> singleTopoList = new ArrayList<CspCallsRelationship>();
    
    for(CspTimeKeyDependInfo cspTimeKeyDependInfo : timeList) {
      CspCallsRelationship po = new CspCallsRelationship();
      po.setOrigin(cspTimeKeyDependInfo.getKeyName());
      po.setOriginApp(cspTimeKeyDependInfo.getAppName());
      po.setRate(cspTimeKeyDependInfo.getRate());  //FIXME 未来在Time中也加入rate
      po.setSourceApp(cspTimeKeyDependInfo.getSourceAppName());
      po.setSourceUrl(cspTimeKeyDependInfo.getSourceKeyName());
      po.setTarget(cspTimeKeyDependInfo.getDependKeyName());
      po.setTargetApp(cspTimeKeyDependInfo.getDependAppName());
      sourceUrl = cspTimeKeyDependInfo.getSourceKeyName();
      sourceApp = cspTimeKeyDependInfo.getSourceAppName();
      singleTopoList.add(po);
    }
    Map<String, List<CspCallsRelationship>> keyNameMap = new HashMap<String, List<CspCallsRelationship>>();
    root.setKeyName(sourceUrl);
    root.setAppName(sourceApp);
    preparedForSetSingleTopoData(root, singleTopoList, keyNameMap); 
    return root;
  }
  
  /**
   * 处理Time告警key的格式，只处理HSF-provider`的情况
   * @param sourceKey
   * @return
   */
  public String changeTimeToEagleeyeKey(String sourceKey) {
    //HSF-provider`com.taobao.uic.common.service.userinfo.UicReadService:1.0.0`getBaseUserByUserId
    //PV`http://detail.tmall.com/item.htm
    if(sourceKey == null)
      return sourceKey = "";
    else 
      sourceKey = sourceKey.trim();
    
    if(sourceKey.startsWith("PV`")) {
      sourceKey = sourceKey.substring(sourceKey.indexOf('`') + 1);
    } else if(sourceKey.startsWith("HSF-provider`")) {
      //1、去掉标记的头HSF-provider` 2、去掉重载
      //com.taobao.item.service.ItemQueryService:1.0.0_queryItemById~lQA
      //一般也就~lQ、lQA、LQ、LQA 、lA 几种
      sourceKey = sourceKey.substring(sourceKey.indexOf('`') + 1);
      sourceKey = sourceKey.replace('`', '_');
//      int index = sourceKey.indexOf("~");
//      if(index > 0) {
//        sourceKey = sourceKey.substring(0, index);
//      }
    }
    return sourceKey;
  }
  
  public String changeDependHSFProvideToTimeKey(String dependKey) {
	    //http://buy.taobao.com/auction/json/get_extention_service.do
	    //IN_HSF-ProviderDetail_com.taobao.ump.top.service.PromotionDisplayTopService:1.0.0_findPromotionInfo
	    //->
	    //HSF-provider`com.taobao.item.service.ItemQueryService:1.0.0`queryItemById~lQA
	    if(dependKey == null)
	      dependKey = "";
	    if(dependKey.startsWith("http:")) {
	      dependKey = "PV`" + dependKey;
	    } else if(dependKey.startsWith("IN_HSF-ProviderDetail_")) {
	      dependKey = dependKey.substring("IN_HSF-ProviderDetail_".length());
	      dependKey = dependKey.replace("_", "`");
	      dependKey = "HSF-provider`" + dependKey;
	    }
	    return dependKey;
	  }
  
  public static void main(String[] args) {
  }
}
