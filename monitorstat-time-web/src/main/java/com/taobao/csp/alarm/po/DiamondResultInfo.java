package com.taobao.csp.alarm.po;

/**
 * Created by IntelliJ IDEA.
 * Author: shuquan.ljh
 * Date: 12-7-31
 * Time: ÉÏÎç9:06
 */
public class DiamondResultInfo {

    private String id;
    private String lastModifedTime;
    private String content;
    private String group;
    private String lastId;
    private String dataId;
    private String opType;
    private String createdTime;
    private String srcUser;
    private String srcIp;

    public String getSrcUser() {
        return srcUser;
    }

    public void setSrcUser(final String srcUser) {
        this.srcUser = srcUser;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(final String srcIp) {
        this.srcIp = srcIp;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLastModifedTime() {
        return lastModifedTime;
    }

    public void setLastModifedTime(final String lastModifedTime) {
        this.lastModifedTime = lastModifedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(final String lastId) {
        this.lastId = lastId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(final String dataId) {
        this.dataId = dataId;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(final String opType) {
        this.opType = opType;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(final String createdTime) {
        this.createdTime = createdTime;
    }
}
