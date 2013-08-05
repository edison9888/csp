package com.taobao.monitor.web.report;

import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.web.ao.MonitorTradeAo;
import com.taobao.monitor.web.vo.MonitorVo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-9 上午10:45:45
 */
public class PhoneOut {

	private static Logger log = Logger.getLogger(PhoneOut.class);

	public static void sendPhone(String searchDate,String phoneList) {
		try {
			log.info("test");
			Map<Integer, MonitorVo> map = MonitorDayAo.get()
					.findMonitorCountMapByDate(searchDate);
			StringBuilder sb = new StringBuilder();
			log.info("test1");
			// List:7565 SS:22950 Detail:25366 create 创建笔数, pay 笔数 pay 金额
			sb.append("list");
			try {
				sb.append(map.get(2) == null ? " - " : (Long.parseLong(map
						.get(2).getApachePv()) / 10000)
						+ "");
			} catch (Exception e) {
			}
			sb.append("ss");
			try {
				sb.append(map.get(3) == null ? " - " : (Long
						.parseLong(map.get(3).getPvNum()) / 10000)
						+ "");
			} catch (Exception e) {
			}
			sb.append("item");
			try {
				
//				if(Long.parseLong(map	.get(1).getPvNum()) <100000){
//					return ;
//				}
				
				sb.append(map.get(1) == null ? " - " : (Long.parseLong(map
						.get(1).getApachePv()) / 10000)
						+ "");
			} catch (Exception e) {
			}
			sb.append("buy");
			try {
				sb.append(map.get(330) == null ? " - " : (Long.parseLong(map
						.get(330).getApachePv()) / 10000)
						+ "");
			} catch (Exception e) {
			}
			sb.append("order");
			sb.append(map.get(330) == null ? " - " : (map.get(330)
					.getCreateOrderCountNum() / 10000)
					+ "");
			sb.append("pay");
			sb.append(map.get(330) == null ? " - " : (map.get(330)
					.getPayOrderCountNum() / 10000)
					+ "");
			sb.append("amount");
			
			log.info("test2");
			long amount = MonitorTradeAo.get().sumAllAmountByTc(searchDate);
			log.info("test3");
			if(amount <100000000){
				return;
			}
			
			log.info("test4");
			sb.append((amount / 10000)+ "");
			MessageSend messageSender = MessageSendFactory.create(MessageSendType.Phone);
			log.info(sb.toString());
			
			messageSender.send(phoneList, "业务数据", sb.toString());
			
		} catch (Exception e) {
			log.error("", e);
		}

	}

}
