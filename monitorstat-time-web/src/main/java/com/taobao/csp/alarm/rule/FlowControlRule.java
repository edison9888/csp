/**
 * High-Speed Service Framework (HSF)
 *
 * www.taobao.com
 *  (C) 淘宝(中国) 2003-2011
 */
package com.taobao.csp.alarm.rule;

/**
 * 描述：机房流量控制规则，是用于控制跨机房服务访问流量控制的规则。
 * 
 * <pre>
 * 配置实例：
 * flowControl@&lt;flowControl&gt;
 *   &lt;localPreferredSwitch&gt;on&lt;/localPreferredSwitch&gt;
 *   &lt;threshold&gt;0.3&lt;/threshold&gt;
 *   &lt;exclusions&gt;172.23.*,172.19.*&lt;/exclusions&gt;
 * &lt;/flowControl&gt;
 * 解释： 对某一服务，开启本机房优先调用策略
 * 但当本机房内的可用机器的数量占服务地址全部数量的比例小于0.3时，本机房优先调用策略失效，启用跨机房调用
 * 该规则对以下网段的服务消费者不生效：172.23.*,172.19.*
 * </pre>
 * 
 * @author yijiang
 * @since 1.4.8.5
 */
public class FlowControlRule  {

    public static final String FLOWCONTROL = "flowControl";
    public static final String TAG_LOCALPREFERRED = "localPreferredSwitch";
    public static final String TAG_THRESHOLD = "threshold";
    public static final String TAG_EXCLUSIONS = "exclusions";
    public static final String SWITCH_ON = "on";
    public static final String SWITCH_OFF = "off";

    private String localPreferredSwitch = SWITCH_OFF;
    private float threshold = 0F;

    public String getLocalPreferredSwitch() {
        return localPreferredSwitch;
    }

    public String getName() {
        return "FlowControlRule";
    }

    public String getRawRule() {
        throw new UnsupportedOperationException();
    }

    public float getThreshold() {
        return threshold;
    }

    /**
     * 是否启用本机房优先调用策略
     * <p>
     * <ol>
     * <li>localPreferredSwitch == off 不启用</li>
     * <li>localPreferredSwitch == on 进行以下判断：</li>
     * <ul>
     * <li>本机房可用地址数量占总服务地址数量的比例 >= threshold 启用</li>
     * <li>本机房可用地址数量占总服务地址数量的比例 < threshold 不启用</li>
     * </ul>
     * </ol>
     * </p>
     * 
     * @param allAmount
     *            总服务地址数量
     * @param localAvailableAmount
     *            本机房可用地址数量
     * @return true/false
     */
    public boolean isLocalPreferred(int allAmount, int localAvailableAmount) {
        if (SWITCH_OFF.equals(localPreferredSwitch)) { // 默认情况下，false
            return false;
        } else {
            if (allAmount == 0) {
                return false;
            }

            float value = (localAvailableAmount + 0.0F) / allAmount;
            if (value >= threshold) {
                return true;
            }
        }
        return false;
    }

    public void setLocalPreferredSwitch(String localPreferredSwitch) {
        this.localPreferredSwitch = localPreferredSwitch;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "FlowControlRule [localPreferredSwitch=" + localPreferredSwitch + ", threshold=" + threshold + "]";
    }

    /**
     * 阀值 必须为[0,1)的值
     */
    public boolean validate() {
        if (threshold < 0 || threshold >= 1) {
            return false;
        }
        return true;
    }
}
