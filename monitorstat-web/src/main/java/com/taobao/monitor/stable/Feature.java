
package com.taobao.monitor.stable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @author xiaodu
 * @version 2011-6-16 обнГ05:08:56
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Feature {
	
	public Class<?>[] features() ;

}
