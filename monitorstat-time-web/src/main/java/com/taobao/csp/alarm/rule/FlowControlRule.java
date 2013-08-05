/**
 * High-Speed Service Framework (HSF)
 *
 * www.taobao.com
 *  (C) �Ա�(�й�) 2003-2011
 */
package com.taobao.csp.alarm.rule;

/**
 * �����������������ƹ��������ڿ��ƿ������������������ƵĹ���
 * 
 * <pre>
 * ����ʵ����
 * flowControl@&lt;flowControl&gt;
 *   &lt;localPreferredSwitch&gt;on&lt;/localPreferredSwitch&gt;
 *   &lt;threshold&gt;0.3&lt;/threshold&gt;
 *   &lt;exclusions&gt;172.23.*,172.19.*&lt;/exclusions&gt;
 * &lt;/flowControl&gt;
 * ���ͣ� ��ĳһ���񣬿������������ȵ��ò���
 * �����������ڵĿ��û���������ռ�����ַȫ�������ı���С��0.3ʱ�����������ȵ��ò���ʧЧ�����ÿ��������
 * �ù�����������εķ��������߲���Ч��172.23.*,172.19.*
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
     * �Ƿ����ñ��������ȵ��ò���
     * <p>
     * <ol>
     * <li>localPreferredSwitch == off ������</li>
     * <li>localPreferredSwitch == on ���������жϣ�</li>
     * <ul>
     * <li>���������õ�ַ����ռ�ܷ����ַ�����ı��� >= threshold ����</li>
     * <li>���������õ�ַ����ռ�ܷ����ַ�����ı��� < threshold ������</li>
     * </ul>
     * </ol>
     * </p>
     * 
     * @param allAmount
     *            �ܷ����ַ����
     * @param localAvailableAmount
     *            ���������õ�ַ����
     * @return true/false
     */
    public boolean isLocalPreferred(int allAmount, int localAvailableAmount) {
        if (SWITCH_OFF.equals(localPreferredSwitch)) { // Ĭ������£�false
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
     * ��ֵ ����Ϊ[0,1)��ֵ
     */
    public boolean validate() {
        if (threshold < 0 || threshold >= 1) {
            return false;
        }
        return true;
    }
}
