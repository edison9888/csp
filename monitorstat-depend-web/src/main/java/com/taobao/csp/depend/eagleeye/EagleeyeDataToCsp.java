//package com.taobao.csp.depend.eagleeye;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//import com.taobao.monitor.common.db.impl.center.CspDependInfoDao;
//import com.taobao.monitor.common.db.impl.center.EagleeyeDataDao;
//import com.taobao.monitor.common.po.CspAppDepAppPo;
//import com.taobao.monitor.common.po.CspDependAppInfo;
//import com.taobao.monitor.common.po.EagleeyeTopoTypeDO;
//import com.taobao.monitor.common.util.Utlitites;
//
///**
// * 
// * @author zhongting.zy ��ʱ��EagleEye���������ݷ������뵽CSP���������ݿ���
// */
//public class EagleeyeDataToCsp implements Job {
//
//  private static final Logger logger = Logger
//      .getLogger(EagleeyeDataToCsp.class);
//
//  private CspDependInfoDao cspDao = new CspDependInfoDao();
//  private EagleeyeDataDao eagleDao = new EagleeyeDataDao();
//
//  public static void main(String[] args) throws JobExecutionException {
//    EagleeyeDataToCsp csp = new EagleeyeDataToCsp();
//    csp.eagleeyeDataToCsp();
//  }
//
//  public void clearTable() {
//    logger.info("��ձ�dependdata_key_dependency������.........");
//    cspDao.clearTable();
//  }
//
//  /**
//   * ��EagleEye�����ݵ��뵽Csp��
//   */
//  public void eagleeyeDataToCsp() {
//
//    long ctime = System.currentTimeMillis();
//    logger.info("��ʼ����EagleEye�����ݣ�����Key��������ϵ");
//
//    long total = eagleDao.getCount();
//    logger.info("���µ�EagleEye����������Ϊ" + total);
//    int i = 0;
//    while (true) {
//      if (i * EagleeyeDataDao.LIMIT_SIZE < total
//          || (i * EagleeyeDataDao.LIMIT_SIZE >= total && i
//              * EagleeyeDataDao.LIMIT_SIZE - total < EagleeyeDataDao.LIMIT_SIZE)) {
//        List<EagleeyeTopoTypeDO> list = eagleDao.getAllTopoStringList(i
//            * EagleeyeDataDao.LIMIT_SIZE);
//        for (EagleeyeTopoTypeDO topo : list) {
//          analyseTopo(topo.getTopo(), topo.getTraceName());
//        }
//        logger.info(i++);
//      } else {
//        logger.info("����..." + i);
//        break;
//      }
//    }
//    long ctime2 = System.currentTimeMillis();
//    logger.info("����EagleEye�����ݽ�������ʱ" + (ctime2 - ctime));
//
//  }
//
//  /**
//   * �����������ݲ����뵽���ݿ�
//   * 
//   * @param topoStr
//   *          eagleeye���ݿ��д洢��һ��Trace���������ݣ�����������νṹ�� ���磺
//   *          "(0,browser|,http://waimai.taobao.com/confirm_order.htm|)(0.1,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.shop.service.ITakeoutOptionService:1.0.0|getTakeoutOptionByShopId)(0.2,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.cart.service.ICartService:1.0.0|queryCartList)(0.2.1,com.taobao.life.cart.service.ICartService:1.0.0|queryCartList,com.taobao.cart.service.CartServiceV1:1.0.0|queryMemberCartItems)(0.2.2,com.taobao.life.cart.service.ICartService:1.0.0|queryCartList,com.taobao.item.service.ItemQueryService:1.0.0|queryItemListByIdList)(0.3,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.cart.service.ICartService:1.0.0|updateItemQuantity)(0.3.1,com.taobao.life.cart.service.ICartService:1.0.0|updateItemQuantity,com.taobao.cart.service.CartServiceV1:1.0.0|updateMemberCartItemQuantity)(0.4,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.cart.service.ICartService:1.0.0|updateItemQuantity)(0.4.1,com.taobao.life.cart.service.ICartService:1.0.0|updateItemQuantity,com.taobao.cart.service.CartServiceV1:1.0.0|updateMemberCartItemQuantity)(0.5,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.cart.service.ICartService:1.0.0|updateItemQuantity)(0.5.1,com.taobao.life.cart.service.ICartService:1.0.0|updateItemQuantity,com.taobao.cart.service.CartServiceV1:1.0.0|updateMemberCartItemQuantity)(0.6,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.cart.service.ICartService:1.0.0|updateItemQuantity)(0.6.1,com.taobao.life.cart.service.ICartService:1.0.0|updateItemQuantity,com.taobao.cart.service.CartServiceV1:1.0.0|updateMemberCartItemQuantity)(0.7,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.order.service.IUserAddressService:1.0.0|getUserAddressById)(0.8,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.order.service.IUserAddressService:1.0.0|updateUserAddress)(0.9,http://waimai.taobao.com/confirm_order.htm|,com.taobao.life.order.service.IOrderManagerService:1.0.0|createOrder)(0.9.1,com.taobao.life.order.service.IOrderManagerService:1.0.0|createOrder,com.taobao.tc.service.TcTradeService:1.0.0|getOutOrderSeqIdByBuyerId)(0.9.2,com.taobao.life.order.service.IOrderManagerService:1.0.0|createOrder,com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0|createOrders)(0.9.2.1,com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0|createOrders,com.taobao.item.service.ItemQueryService:1.0.0-L0|queryItemAndSkuWithPVToText)(0.9.2.2,com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0|createOrders,com.taobao.item.service.ItemBidService:1.0.0|updateItemQuantity)(0.9.2.3,com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0|createOrders,com.taobao.item.service.ItemBidService:1.0.0|updateItemQuantity)(0.9.2.4,com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0|createOrders,com.taobao.item.service.ItemBidService:1.0.0|updateItemQuantity)(0.9.2.5,com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0|createOrders,com.taobao.item.service.ItemBidService:1.0.0|updateItemQuantity)(0.9.3,com.taobao.life.order.service.IOrderManagerService:1.0.0|createOrder,com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0|enableOrders)(0.9.4,com.taobao.life.order.service.IOrderManagerService:1.0.0|createOrder,com.taobao.cart.service.CartServiceV1:1.0.0|deleteMemberCartItems)"
//   */
//  private void analyseTopo(String topoStr, String url) {
//    try {
//      // topoStr = topoStr.toLowerCase(); //ͳһСд�����ݿ���
//      String[] topoArray = topoStr.split("\\)");
//      for (String str : topoArray) {
//        str = str.substring(1); // ȥ����(
//        String[] keyArray = str.split(",");
//        String parentKey = keyArray[1];
//        String curKey = keyArray[2];
//        if (parentKey != null) {
//          if(parentKey.endsWith("|")) {
//            parentKey = parentKey.replace("|", "");            
//          } else {
//            parentKey = parentKey.replace("|", "`");
//          }
//        }
//        if (curKey != null) {
//          if(curKey.endsWith("|")) {
//            curKey = curKey.replace("|", "");            
//          } else {
//            curKey = curKey.replace("|", "`");
//          }
//        }
//        cspDao.insertKeyRelToDependInfo(curKey, parentKey, url);
////        CspDependAppInfo info1 = new CspDependAppInfo("KEY");
////        CspDependAppInfo info2 = new CspDependAppInfo("KEY");
////
////        info1.setKeyname(curKey);
////        info2.setKeyname(parentKey);
////        cspDao.insertCspAppIdMap(info1);
////        cspDao.insertCspAppIdMap(info2);
//      }
//    } catch (Exception e) {
//      logger.error("", e);
//    }
//  }
//
//  /**
//   * ����App���������ϵ����������Ϊ��ǰʱ��ǰһ������ݡ�
//   */
//  public void bulidAppDepRelation() {
//
//    try {
//      String yesterday = Utlitites.getDateBefore(new Date(), 1, null);
//      List<CspAppDepAppPo> list = cspDao.getAppDepAppList(yesterday);
//
//      long ctime = System.currentTimeMillis();
//      logger.info("��ʼ����csp_app_depend_app�����ݣ�����ʱ��:" + yesterday);
//      logger.info("�˴δ����Ӧ�ü�¼����Ϊ��" + list.size());
//
//      HashMap<String, String> dependMap = cspDao.getAppDependTypeMap(0);
//
//      int iCounter = 0;
//      for (CspAppDepAppPo po : list) {
//        if (po.getDepAppType() == null
//            || dependMap.get(po.getDepAppType().toLowerCase()) == null) {
//          po.setDepAppType("δ֪");
//        }
//
//        if (po.getDepOpsName().equals(po.getOpsName()) || po.getOpsName().equals("δ֪") || po.getDepOpsName().equals("δ֪")) {
//          continue;
//        }
//        CspDependAppInfo infoDepMe = new CspDependAppInfo("APP");
//        infoDepMe.setAppname(po.getDepOpsName());
//        infoDepMe.setAppType(po.getDepAppType());
//
//        CspDependAppInfo infoMeDep = new CspDependAppInfo("APP");
//        infoMeDep.setAppname(po.getOpsName());
//        infoMeDep.setAppType("δ֪");
//
//        cspDao.insertCspAppIdMap(infoDepMe);
//        cspDao.insertCspAppIdMap(infoMeDep);
//        cspDao.insertAppRelToDependInfo(po);
//        logger.info(iCounter++);
//      }
//
//      long ctime2 = System.currentTimeMillis();
//      logger.info("����Ӧ�����ݽ�������ʱ" + (ctime2 - ctime));
//
//    } catch (Exception e) {
//      logger.error("", e);
//    }
//  }
//
//  @Override
//  public void execute(JobExecutionContext context) throws JobExecutionException {
//    /** ������� */
//    clearTable();
//    
//    /** ����HSF��key��App�Ĺ�ϵ */
//    bulidAppDepRelation();
//    
//    /** ��Eagleeye��key��ϵ���뵽Csp���ݿ��У�����key��ϵ */
//    //eagleeyeDataToCsp();
//  }
//}
