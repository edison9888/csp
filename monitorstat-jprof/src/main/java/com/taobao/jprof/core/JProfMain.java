/**
 * 
 */
package com.taobao.jprof.core;

import java.lang.instrument.Instrumentation;

import com.taobao.jprof.JProfManager;

/**
 * 
 * ֻ��������ݲ�����Ȼ����������. ��� for ѭ�������ʵ�<br>
 * ����checkpoint
 * 
 * @author luqi
 * 
 */

public class JProfMain {

	public static void premain(String args, Instrumentation inst) {
		JProfManager.get();
		inst.addTransformer(new JProfTransformer());
	}
}
