/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.common.vo;

/**
 * 这个类用于对取到的detail业务数据进行分装，方便页面的展现。做到展现需要的对象和底层对象隔离
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version      2010-6-10:上午10:13:23
 *
 */
public class DetailBusiData {
    
    private String name;
    
    private int groupId;
    
    private int keyId;
    
    private double numValue;
    
    private String strValue;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the numValue
     */
    public double getNumValue() {
        return numValue;
    }

    /**
     * @param numValue the numValue to set
     */
    public void setNumValue(double numValue) {
        this.numValue = numValue;
    }

    /**
     * @return the strValue
     */
    public String getStrValue() {
        return strValue;
    }

    /**
     * @param strValue the strValue to set
     */
    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }
    
    

}
