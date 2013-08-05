/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.common.po;

import java.util.Date;

/**
 * detailͳ�����ݵķ�װ��
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version      2010-6-1:����11:04:22
 *
 */
public class DetailStatisticDO {
    
    /**
     * ����ͳ�Ƽ�¼��Ӧ��Ӧ��id 
     */
    private int appId;
    /**
     * ������¼����app����һ��ҵ��
     */
    private int groupId;
    /**
     * ����ͳ�����id
     */
    private int keyId;
    /**
     * key��Ӧ��ͳ��ֵ
     */
    private int vaue;
    
    /**
     * group ������key��Ӧ��value֮��
     */
    private int groupSum;
    /**
     * ������¼��ͳ������
     */
    private Date statisticDate;
    /**
     * ������¼�Ĵ�������
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
