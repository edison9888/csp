
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiaodu
 *
 * ����12:37:54
 */
public class IDGenerator {
	
	  private static AtomicLong id = new AtomicLong(0L);

      /**
       * �����µ�request ID
       */
      public static long getNextId() {
          if (id.longValue() > Long.MAX_VALUE - 1000) {
              id.getAndSet(0L);
          }
          return id.incrementAndGet();
      }

}
