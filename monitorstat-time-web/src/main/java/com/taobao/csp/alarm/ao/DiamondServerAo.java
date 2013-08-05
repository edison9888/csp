package com.taobao.csp.alarm.ao;

import com.alibaba.common.lang.StringUtil;
import com.taobao.csp.alarm.po.DiamondResultInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author: shuquan.ljh
 * Date: 12-7-31
 * Time: 上午9:00
 */
public class DiamondServerAo {
    private static final Logger logger = Logger.getLogger(DiamondServerAo.class);

    private static final String JSON_URL = "http://ops.jm.taobao.net/diamondops/history/queryHisConfigByDate.html?serverId=Diamond";

    private static final String JSON_URL_FOR_LAST_ID = "http://ops.jm.taobao.net/diamondops/history/queryHisConfigById.html?serverId=Diamond&id=";

    private static SimpleDateFormat simpleDateFormat;

    private static final String GROUP = "DEFAULT_GROUP";

    public DiamondServerAo() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public List<DiamondResultInfo> getInfo(String dataId) {
        StringBuilder jsonURL = new StringBuilder(JSON_URL);
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 3600 * 1000;
        jsonURL.append("&dataId=").append(dataId)
                .append("&group=").append(GROUP)
                .append("&startTime").append(simpleDateFormat.format(new Date(startTime)));
                jsonURL.append("&endTime=").append(simpleDateFormat.format(new Date(endTime)));
        String response = getResponseStringByURL(jsonURL.toString());

        if(StringUtil.isNotBlank(response)) {
            try {
                JSONArray jsonArray = JSONArray.fromObject(response);
                return getInfosFromJSONArray(jsonArray);
            } catch (Exception e) {
                logger.error(response + "JSON内容格式有误。");
            }
        }
        return null;
    }

    public String getRencentChangegReport(String dataId) {
        List<DiamondResultInfo> diamondResultInfoList = getInfo(dataId);

        if (diamondResultInfoList == null || diamondResultInfoList.size() == 0) {
            return "";
        }
        List<String> addList = new ArrayList<String>();
        List<String> delList = new ArrayList<String>();
        DiamondResultInfo resultInfo = diamondResultInfoList.get(0);
        if(StringUtil.isNotBlank(resultInfo.getLastId())) {
            DiamondResultInfo lastResultInfo = getLastModfiyResultByDataId(resultInfo.getLastId());
            if(lastResultInfo != null) {
                String[] currentContents = resultInfo.getContent().split(",");
                String[] lastVersionContents = lastResultInfo.getContent().split(",");
                List<String> currentContentList = Arrays.asList(currentContents);
                List<String> lastContentList = Arrays.asList(lastVersionContents);
                for (String content : currentContentList) {
                    if(!lastContentList.contains(content)) {
                        addList.add(content);
                    }
                }
                for(String content : lastContentList) {
                    if(!currentContentList.contains(content)) {
                        delList.add(content);
                    }
                }
            }

        }
        StringBuilder sb = new StringBuilder();
        sb.append("Diamond dataId:")
                .append(dataId)
                .append(" 最近一小时配置更新情况：")
                .append("修改时间:").append(resultInfo.getLastModifedTime())
                .append(" 增加内容:").append(addList.toString())
                .append(" 删除内容:").append(delList.toString())
                .append(" 执行人：").append(resultInfo.getSrcUser())
                .append(" 执行人IP:").append(resultInfo.getSrcIp())
                .append("</br>");

        return sb.toString();
    }

    public static void main(String[] args) {
        String result = new DiamondServerAo().getRencentChangegReport(null);

        System.out.println(result);
    }

    private DiamondResultInfo getLastModfiyResultByDataId(String dataId) {
        String URL = JSON_URL_FOR_LAST_ID + dataId;
        String response  = getResponseStringByURL(URL);
        if(StringUtil.isNotBlank(response)) {
            try {
                JSONObject jsonObject = JSONObject.fromObject(response);
                return getInfoFromJSON(jsonObject);

            } catch (Exception e) {
                logger.error(response + "JSON内容格式有误。" );
            }
        }
        return null;
    }

    private DiamondResultInfo getInfoFromJSON(JSONObject jsonObject) {
        if(jsonObject == null) {
            return null;
        }
        DiamondResultInfo diamondResultInfo = new DiamondResultInfo();
        String jsonString = jsonObject.toString();

        if (jsonString.contains("lastModifiedTime")) {
            String lastModifiedTime = simpleDateFormat.format(new Date(jsonObject.getLong("lastModifiedTime")));
            diamondResultInfo.setLastModifedTime(lastModifiedTime);
        }
        if (jsonString.contains("content")) {
            diamondResultInfo.setContent(jsonObject.getString("content"));
        }
        if (jsonString.contains("createdTime")) {
            String createdTime = simpleDateFormat.format(new Date(jsonObject.getLong("createdTime")));
            diamondResultInfo.setCreatedTime(createdTime);
        }
        if(jsonString.contains("lastId")) {
            diamondResultInfo.setLastId(jsonObject.getString("lastId"));
        }
        if (jsonString.contains("opType")) {
            diamondResultInfo.setOpType(jsonObject.getString("opType"));
        }
        if (jsonString.contains("srcUser")) {
            diamondResultInfo.setSrcUser(jsonObject.getString("srcUser"));
        }
        if (jsonString.contains("srcIp")) {
            diamondResultInfo.setSrcIp(jsonObject.getString("srcIp"));
        }
        return diamondResultInfo;
    }

    private List<DiamondResultInfo> getInfosFromJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        List<DiamondResultInfo> resultInfoList = new ArrayList<DiamondResultInfo>();
        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            DiamondResultInfo diamondResultInfo = getInfoFromJSON(jsonObject);
            resultInfoList.add(diamondResultInfo);
        }
        return resultInfoList;
    }

    private String getResponseStringByURL(String URL) {
        HttpClient httpClient = new HttpClient();
        GetMethod method = new GetMethod(URL);
        method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        method.addRequestHeader("Accept", "text/plain");
        try {
            if (httpClient.executeMethod(method) == HttpStatus.SC_OK) {
                return StringEscapeUtils.unescapeHtml(method.getResponseBodyAsString());
            }
        } catch (HttpException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
