/**
 * High-Speed Service Framework (HSF)
 *
 * www.taobao.com
 *  (C) �Ա�(�й�) 2003-2011
 */
package com.taobao.csp.alarm.rule;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * HSF����·�ɹ�����
 * <p>
 * ����ı�����ʽΪһ����ַ��������ʽ�б���һ����ַ�����б��е�ĳһ������ʽ����ᱻѡ�У����򱻹��ˡ�
 * 
 * @param <M>
 *            ����ǩ������<br />
 *            ����ǩ�����������{@link Method}����Ҳ�����Ǹ�ʽΪ��������#��������1..._��������n���ַ���
 * 
 * @author linxuan
 */
public class RouteRule<M> {

    // ������������ҪΪargsRule���񣬴����interfaceRule��methodRule�еĹ���
    private Map<? extends Object, ? extends List<String>> keyedRules;

    private Object interfaceRule;// �ӿڼ�·�ɹ���keyedRules�е�key
    private Map<M, Object> methodRule; // ������·�ɹ���keyedRules�е�key
    private Map<M, Args2KeyCalculator> argsRule; // ������·�ɹ���calculate�����keyedRules�е�key

    /**
     * �ָ������壬�����߷����ָ����Ķ���<br />
     * Ҫ�������֣������������������У������ܸ�String.split(regex)����ֱ��ʹ�ö�����ת��
     */
    public static final String METHOD_SIGS_JOINT_MARK = "#";

    public static final String joinMethodSigs(Method m) {
        StringBuilder sb = new StringBuilder(m.getName());
        Class<?>[] paramTypes = m.getParameterTypes();
        for (Class<?> c : paramTypes) {
            sb.append(METHOD_SIGS_JOINT_MARK).append(c.getName());
        }
        return sb.toString();
    }

    public static final String joinMethodSigs(String methodName, String[] paramTypeStrs) {
        StringBuilder sb = new StringBuilder(methodName);
        for (String type : paramTypeStrs) {
            sb.append(METHOD_SIGS_JOINT_MARK).append(type);
        }
        return sb.toString();
    }

    public Map<M, Args2KeyCalculator> getArgsRule() {
        return argsRule;
    }

    public Object getInterfaceRule() {
        return interfaceRule;
    }

    public Map<? extends Object, ? extends List<String>> getKeyedRules() {
        return keyedRules;
    }

    public Map<M, Object> getMethodRule() {
        return methodRule;
    }

    public void setArgsRule(Map<M, Args2KeyCalculator> argsRule) {
        this.argsRule = argsRule;
    }

    public void setInterfaceRule(Object interfaceRule) {
        this.interfaceRule = interfaceRule;
    }

    public void setKeyedRules(Map<? extends Object, ? extends List<String>> keyedRules) {
        this.keyedRules = keyedRules;
    }

    public void setMethodRule(Map<M, Object> methodRule) {
        this.methodRule = methodRule;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString()).append("{");
        sb.append("\nkeyedRules=").append(this.keyedRules);
        sb.append(",\ninterfaceRule=").append(this.interfaceRule);
        sb.append(",\nmethodRule=").append(this.methodRule);
        sb.append(",\nargsRule=").append(this.argsRule).append("\n}");
        return sb.toString();
    }
}
