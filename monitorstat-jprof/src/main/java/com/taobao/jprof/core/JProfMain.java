/**
 * 
 */
package com.taobao.jprof.core;

import java.lang.instrument.Instrumentation;

import com.taobao.jprof.JProfManager;

/**
 * 
 * 只负责把数据产生，然后再做分析. 针对 for 循环可以适当<br>
 * 增加checkpoint
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
