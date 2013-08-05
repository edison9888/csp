package com.taobao.csp.time.web.action;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.common.lang.StringUtil;
import com.taobao.monitor.common.po.RowAnalyseConfigWeb;

@Controller
@RequestMapping("/analyseconfig.do")
public class AnalyseConfigAction extends BaseController {
	private static Logger logger = Logger.getLogger(AnalyseConfigAction.class);
	public static final String KEY_PREFIX = "key_";
	/**
	 *	�û����ú�У�����ɵ�Json�Ƿ���ȷ
	 */
	@RequestMapping(params="method=testRegularString")
	public ModelAndView testRegularString (RowAnalyseConfigWeb webConfig, String line) {
		ModelAndView view = new ModelAndView("/config/analyse_template");
		boolean flag = true;
		String errorMsg = "";
		String result = "";
		//ֻУ�����ɵ������Ƿ���ȷ���Ƿ��ܽ��������Log��
		//����SCOPE��OPERATE�ĺϷ�����Ϣ��ǰ̨У�顣
		try {
			String operateString = webConfig.getLogConfigWeb();
			Pattern p = Pattern.compile("\\{([A-Za-z|\\d|_|-]+)}");
			Matcher m = p.matcher(webConfig.getLogConfigWeb());
			Map<Integer, String> indexMap = new HashMap<Integer, String>();
			int iIndex = 0;
			int valueNum = 0;
			int keyNum = 0;
			boolean isTimeExist = false;
			while (m.find()) {
				String key = m.group(0);
				String keySimple = m.group(1); //ƥ���ͬʱȥ������������{}
				operateString = operateString.replace(key,
						"([A-Za-z|\\d|_|.|-|:]+)");
				indexMap.put(1 + iIndex++, keySimple); //��������ƥ�䣬Ĭ�ϴ�1��ʼ��
				if(keySimple.startsWith(KEY_PREFIX)) {
					keyNum ++;
				} else {
					if(keySimple.equals("datetime"))
						isTimeExist = true;
					else
						valueNum ++;
				}
			}
			operateString = operateString.replaceAll(" ", "\\\\s+");
			
			if(!isTimeExist)
				throw new Exception("ʱ���ֶ�{datetime}û������");
			
			if(keyNum != webConfig.getKeyNumber()) {
				throw new Exception("������key�ֶθ��������ò���");
			}
			
			if(valueNum != webConfig.getValueNumber()) {
				throw new Exception("������value�ֶθ��������ò���");
			}
			
			if(StringUtil.isBlank(line)) {
				errorMsg += ";�������־Ϊ��";
				throw new Exception(errorMsg);
			} else if(line.startsWith("[")) {	//ȥ����־ǰ���[2012-07-12 16:12:52]
				int index = line.indexOf("]");
				if(index > 0 && index != line.length() - 1) {
					line = line.substring(index + 1,line.length()).trim();				
				}
			}			
			
			Pattern pValue = Pattern.compile(operateString);
			Matcher mValue = pValue.matcher(line);
			iIndex = 0;
			while (mValue.find()) {
				int iCount = mValue.groupCount();
				if(iCount != indexMap.size()) {
					errorMsg += String.format("group count��ƥ��,iCount=%d,indexMap.size()=%d", 
							iCount,indexMap.size());
					break;
				}
				Map<String, String> oneTimeRecordMap = new HashMap<String, String>();
				for(int i=1; i<=iCount; i++) {
					String value = mValue.group(i);
					String key = indexMap.get(i);
					oneTimeRecordMap.put(key, value);
					result += String.format("key=%s,value=%s", key, value) + "<br/>";
				}				
			}
			
		} catch (Exception e) {
			logger.error("",e);
			errorMsg += ";" + e.toString(); 
		}
		
		if(result.length() == 0) {
			errorMsg += ";��־�޷���������";
		}
		
		String jsonString = "";
		if(errorMsg.length() != 0) {
			logger.error("errorMsg=" + errorMsg);
			flag = false;
		} else {
			JSONObject jsonObj = JSONObject.fromObject(webConfig);
			jsonString = jsonObj.toString();
			flag = true;
		}
		view.addObject("flag", flag);
		view.addObject("errorMsg", errorMsg);
		view.addObject("result", result);
		view.addObject("jsonString", jsonString);
		view.addObject("webConfig", webConfig);
		view.addObject("line", line);
		return view;
	}
	
	@RequestMapping(params = "method=test")
	public ModelAndView test (String line) {
		ModelAndView view = new ModelAndView("/config/analyse_template");
		return view;
	}
}
