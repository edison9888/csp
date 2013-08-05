package com.taobao.csp.loadrun.core.fetch.analyser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/**
 * Io��Ϣ�ķ�����
 * @author youji.zj
 * @version 2012-12-17
 */
public class ThreadCountAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(ThreadCountAnalyser.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private IFetchTask task;
	
	private LoadrunTarget target;

	public ThreadCountAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
		this.target = target;
	}
	
	@Override
	public void analyse(String line) {
		/***
		 * ���Ϊ��������
		 */
		if (StringUtils.isBlank(line)) {
			return;
		}
		
		try {
			// �߳���һ��Ϊ��ʮ���߼��٣�����λ�������ܳ���6
			if (line.trim().length() > 6) {
				return;
			}
			
			double threadCount = Double.parseDouble(line.trim());
		
			Calendar calendar = Calendar.getInstance();

			long collectTime = transferDate(calendar.getTime(), target);
			ResultDetailKey pktoutKey = new ResultDetailKey(ResultDetailType.PERFORMANCE_INDEX, "ThreadCount", collectTime);
			task.putDetailData(pktoutKey, threadCount); 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
