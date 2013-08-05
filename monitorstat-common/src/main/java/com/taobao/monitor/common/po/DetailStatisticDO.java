/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.common.po;

import java.util.Date;

/**
 * detail统计数据的封装类
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version      2010-6-1:下午11:04:22
 *
 */
public class DetailStatisticDO {
    
    /**
     * 这条统计记录对应的应用id 
     */
    private int appId;
    /**
     * 这条记录属于app下哪一个业务
     */
    private int groupId;
    /**
     * 具体统计项的id
     */
    private int keyId;
    /**
     * key对应的统计值
     */
    private int vaue;
    
    /**
     * group 下所有key对应的value之和
     */
    private int groupSum;
    /**
     * 这条记录的统计日期
     */
    private Date statisticDate;
    /**
     * 这条记录的创建日期
     */
    private Date createDate;
    /**
     * @return the appId
     */
    public int getAppId() {
        return appId;
    }
    /**
     * @param appId the appId to set
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }
    /**
     * @return the groupId
     */
    public int getGroupId() {
        return groupId;
    }
    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    /**
     * @return the keyId
     */
    public int getKeyId() {
        return keyId;
    }
    /**
     * @param keyId the keyId to set
     */
    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }
    /**
     * @return the vaue
     */
    public int getVaue() {
        return vaue;
    }
    /**
     * @param vaue the vaue to set
     */
    public void setVaue(int vaue) {
        this.vaue = vaue;
    }
    /**
     * @return the statisticDate
     */
    public Date getStatisticDate() {
        return statisticDate;
    }
    /**
     * @param statisticDate the statisticDate to set
     */
    public void setStatisticDate(Date statisticDate) {
        this.statisticDate = statisticDate;
    }
    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * @return the groupSum
     */
    public int getGroupSum() {
        return groupSum;
    }
    /**
     * @param groupSum the groupSum to set
     */
    public void setGroupSum(int groupSum) {
        this.groupSum = groupSum;
    }
    
    
    

}
