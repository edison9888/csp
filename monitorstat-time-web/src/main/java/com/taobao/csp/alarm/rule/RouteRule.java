/**
 * High-Speed Service Framework (HSF)
 *
 * www.taobao.com
 *  (C) 淘宝(中国) 2003-2011
 */
package com.taobao.csp.alarm.rule;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * HSF服务路由规则定义
 * <p>
 * 规则的表现形式为一个地址过滤正则式列表。若一个地址满足列表中的某一个正则式，则会被选中，否则被过滤。
 * 
 * @param <M>
 *            方法签名对象<br />
 *            方法签名对象可以是{@link Method}对象，也可以是格式为：方法名#参数类型1..._参数类型n的字符串
 * 
 * @author linxuan
 */
public class RouteRule<M> {

    // 规则索引，主要为argsRule服务，存放了interfaceRule和methodRule中的规则
    private Map<? extends Object, ? extends List<String>> keyedRules;

    private Object interfaceRule;// 接口级路由规则，keyedRules中的key
    private Map<M, Object> methodRule; // 方法级路由规则，keyedRules中的key
    private Map<M, Args2KeyCalculator> argsRule; // 参数级路由规则，calculate结果是keyedRules中的key

    /**
     * 分隔符定义，及工具方法分隔符的定义<br />
     * 要求即能区分（不能用在类名定义中），又能给String.split(regex)方法直接使用而不用转化
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
