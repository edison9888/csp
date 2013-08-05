package com.taobao.csp.depend.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ProductLine;
import com.taobao.monitor.common.util.TBProductCache;

@Controller
@RequestMapping("/app_info.do")
public class AppInfoController {

	public static final Logger log = Logger.getLogger(AppInfoController.class);

	public static String ZK_MONITOR_APP_ROOT = "/csp/monitor/app";
	
	private final static Map allAppInfoMap = new HashMap();  //��Ҫ�̰߳�ȫ

	/** ��PE����ȡ */
	@RequestMapping(params = "method=allEffectiveAppInfo3")
	public void allEffectiveAppInfo3(HttpServletResponse response)
			throws IOException {

		// һ��idMap<appId,po>������id��ʼ�����
		Map<String, AppInfoPo> appNameMap = new HashMap<String, AppInfoPo>();
		// һ��nameMap<appName, id>������Ӧ��������
	//	Map<String, Integer> appNameIdMap = new HashMap<String, Integer>();
		// <��˾��`����, List<Ӧ����>>
		Map<String, List<String>> appListMap = new HashMap<String, List<String>>();

		Map<String, List<String>> groupListMap = new HashMap<String, List<String>>();
		// Ӧ�����б������Զ����
		List<String> appNameList = new ArrayList<String>();

		// ��˾���б�
		List<String> companyNameList = new ArrayList<String>();

		List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();

		for (AppInfoPo po : appList) {

			ProductLine pline = TBProductCache.getProductLineByAppName(po
					.getAppName());

			// ��һ����������
			String companyName = pline.getDevelopGroup();
			// companyName����Ϊnull����Ϊjson key����Ϊnull
			// �����null����ֵ���д���
			// companyName = isBlank(companyName) ? "is_blank" : companyName;
			po.setCompanyName(companyName);
			// �ڶ�������Ʒ��
			String groupName = pline.getProductline();
			// groupName = isBlank(groupName) ? "is_blank" : groupName;
			po.setGroupName(groupName);
			String appName = po.getAppName();
			//appName = isBlank(appName) ? "is_blank" : appName;
		//	po.setAppName(appName);
			appNameList.add(appName);

			// ��˾���б�
			if (!companyNameList.contains(companyName))
				companyNameList.add(companyName);

			// company group key
			String cgkey = companyName + '`' + groupName;
			List<String> perGroupAppList = appListMap.get(cgkey);
			if (perGroupAppList == null) {

				perGroupAppList = new ArrayList();
				appListMap.put(cgkey, perGroupAppList);
			}
			perGroupAppList.add(appName);

			// groupListMap
			List<String> groupList = groupListMap.get(companyName);
			if (groupList == null) {
				groupList = new ArrayList<String>();
				groupListMap.put(companyName, groupList);
			}
			if (!groupList.contains(groupName)) {
				groupList.add(groupName);
			}
			// net sf json key����Ϊ����
			appNameMap.put(po.getAppName(), po);
			//appNameIdMap.put(po.getAppName(), po.getAppId());
		}

		Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
		Collections.sort(companyNameList, cmp);
		allAppInfoMap.put("companyNameList", companyNameList);

		allAppInfoMap.put("groupListMap", groupListMap);
		allAppInfoMap.put("appListMap", appListMap);
		allAppInfoMap.put("appNameMap", appNameMap);
	//	map.put("appNameIdMap", appNameIdMap);
		allAppInfoMap.put("appNameList", appNameList);

		writeJSONToResponseJSONObject(response, allAppInfoMap);

	}

  /** ��PE����ȡ */
  @RequestMapping(params = "method=gotoIndexPage")
  public ModelAndView gotoIndexPage(HttpServletResponse response, String isClear)
      throws IOException {

	if(StringUtils.isNotBlank(isClear)) {
		allAppInfoMap.clear();
		log.info("����ȫ��Ӧ�ã�");
	}
	
    if(allAppInfoMap.size() == 0) {
      // һ��idMap<appId,po>������id��ʼ�����
      Map<String, AppInfoPo> appNameMap = new HashMap<String, AppInfoPo>();
      // һ��nameMap<appName, id>������Ӧ��������
    //  Map<String, Integer> appNameIdMap = new HashMap<String, Integer>();
      // <��˾��`����, List<Ӧ����>>
      Map<String, List<String>> appListMap = new HashMap<String, List<String>>();

      Map<String, List<String>> groupListMap = new HashMap<String, List<String>>();
      // Ӧ�����б������Զ����
      List<String> appNameList = new ArrayList<String>();

      // ��˾���б�
      List<String> companyNameList = new ArrayList<String>();

      List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();

      for (AppInfoPo po : appList) {

        ProductLine pline = TBProductCache.getProductLineByAppName(po
            .getAppName());

        // ��һ����������
        String companyName = pline.getDevelopGroup();
        // companyName����Ϊnull����Ϊjson key����Ϊnull
        // �����null����ֵ���д���
        // companyName = isBlank(companyName) ? "is_blank" : companyName;
        po.setCompanyName(companyName);
        // �ڶ�������Ʒ��
        String groupName = pline.getProductline();
        // groupName = isBlank(groupName) ? "is_blank" : groupName;
        po.setGroupName(groupName);
        String appName = po.getAppName();
        //appName = isBlank(appName) ? "is_blank" : appName;
      //  po.setAppName(appName);
        appNameList.add(appName);

        // ��˾���б�
        if (!companyNameList.contains(companyName))
          companyNameList.add(companyName);

        // company group key
        String cgkey = companyName + '`' + groupName;
        List<String> perGroupAppList = appListMap.get(cgkey);
        if (perGroupAppList == null) {

          perGroupAppList = new ArrayList();
          appListMap.put(cgkey, perGroupAppList);
        }
        perGroupAppList.add(appName);

        // groupListMap
        List<String> groupList = groupListMap.get(companyName);
        if (groupList == null) {
          groupList = new ArrayList<String>();
          groupListMap.put(companyName, groupList);
        }
        if (!groupList.contains(groupName)) {
          groupList.add(groupName);
        }
        // net sf json key����Ϊ����
        appNameMap.put(po.getAppName(), po);
        //appNameIdMap.put(po.getAppName(), po.getAppId());
      }

      Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
      Collections.sort(companyNameList, cmp);
      allAppInfoMap.put("companyNameList", companyNameList);

      allAppInfoMap.put("groupListMap", groupListMap);
      allAppInfoMap.put("appListMap", appListMap);
      allAppInfoMap.put("appNameMap", appNameMap);
    //  allAppInfoMap.put("appNameIdMap", appNameIdMap);
      allAppInfoMap.put("appNameList", appNameList);      
    }
    ModelAndView view = new ModelAndView("/index");
    view.addObject("allAppInfoMap", allAppInfoMap);
    view.addObject("selectDate", MethodUtil.getStringOfDate(MethodUtil.getYestoday()));
    return view;
  }	
	
	/**
	 *@author wb-lixing 2012-3-30 ����06:43:36
	 *@param companyName
	 */
	private boolean isBlank(String str) {
		if (str == null || str.trim().equals(""))
			return true;
		return false;

	}

	protected void writeJSONToResponseJSONObject(HttpServletResponse response,
			Object obj) throws IOException {

		JSONObject jsonObj = JSONObject.fromObject(obj);
		String jsonStr = jsonObj.toString();
		writeJSONToResponse(response, jsonStr);
	}

	protected void writeJSONToResponse(HttpServletResponse response, String str)
			throws IOException {
		response.setContentType("application/json");
		writeToResponse(response, str);
	}

	protected void writeToResponse(HttpServletResponse response, String str)
			throws IOException {
		PrintWriter out = response.getWriter();
		out.print(str);
		out.flush();
	}
}
