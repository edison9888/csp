/**
 * High-Speed Service Framework (HSF)
 *
 * www.taobao.com
 *  (C) 淘宝(中国) 2003-2011
 */
package com.taobao.csp.alarm.rule;

/**
 * 将具体的参数映射为一个key的接口
 * 
 * @author linxuan
 */
public interface Args2KeyCalculator {

    Object calculate(Object[] args);
}
