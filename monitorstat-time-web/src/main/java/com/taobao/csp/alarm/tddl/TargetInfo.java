package com.taobao.csp.alarm.tddl;

/**
 * Created by IntelliJ IDEA.
 * Author: shuquan.ljh
 * Date: 12-7-30
 * Time: ÏÂÎç4:05
 */
public class TargetInfo {

    private String ip;
    private String appName;
    private String sshUserName;
    private String sshPassWord;

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(final String appName) {
        this.appName = appName;
    }

    public String getSshUserName() {
        return sshUserName;
    }

    public void setSshUserName(final String sshUserName) {
        this.sshUserName = sshUserName;
    }

    public String getSshPassWord() {
        return sshPassWord;
    }

    public void setSshPassWord(final String sshPassWord) {
        this.sshPassWord = sshPassWord;
    }

}
