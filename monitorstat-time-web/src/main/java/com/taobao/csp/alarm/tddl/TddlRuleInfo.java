package com.taobao.csp.alarm.tddl;

import static com.taobao.wwnotify.web.StringUtil.isNotBlank;

/**
 * Created by IntelliJ IDEA.
 * Author: shuquan.ljh
 * Date: 12-8-9
 * Time: ÏÂÎç8:33
 */
public class TddlRuleInfo {

    private String targetIp;
    private String appId;
    private String appName;
    private String dbName;      //tcmain_mysql tc tcreadonly                   datasou
    private String dbGroupQuantity;
    private String dbGroupKeyFormat;

    public String getDbGroupKeyFormat() {
        return dbGroupKeyFormat;
    }

    public void setDbGroupKeyFormat(final String dbGroupKeyFormat) {
        this.dbGroupKeyFormat = dbGroupKeyFormat;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(final String targetIp) {
        this.targetIp = targetIp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(final String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(final String appName) {
        this.appName = appName;
    }

    public String getDbGroupQuantity() {
        return dbGroupQuantity;
    }

    public void setDbGroupQuantity(final String dbGroupQuantity) {
        this.dbGroupQuantity = dbGroupQuantity;
    }

    public boolean checkParms() {
        return isNotBlank(getAppId()) && isNotBlank(getAppName())
                && isNotBlank(getDbGroupKeyFormat()) && isNotBlank(getTargetIp()) && isNotBlank(getDbGroupQuantity());
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(final String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[")
                .append("targetIp:").append(getTargetIp())
                .append("appId:").append(getAppId())
                .append("appName:").append(getAppName())
                .append("dbName:").append(getDbName())
                .append("dbGroupQuantity:").append(getDbGroupQuantity())
                .append("dbGroupQuantity:").append(getDbGroupQuantity())
                .append("]");
        return sb.toString();
    }

}
