
package com.taobao.jprof;

import java.util.Stack;

import junit.framework.TestCase;

/**
 * 
 * @author xiaodu
 * @version 2010-7-8 ÉÏÎç10:57:20
 */
public class JProfProfilerTest extends TestCase {

	public void testStart() {	
		long v = LongCompress.decode(12323454, 1, 0);		
		 this.assertEquals(1, LongCompress.getMethodId(v));
		 this.assertEquals(0, LongCompress.getStackNum(v));
		 this.assertEquals(12323454, LongCompress.getTime(v));
		 
		 
		 
		 JprofStack<Long> stack = new JprofStack<Long>();
		 
		 for(long i=0;i<=100000;i++){
			 stack.push(i);
		 }
		 long i=100000;
		while(stack.size()>0){
			this.assertEquals(i--,stack.pop().longValue());
		}
		 
		 
		 
			
			 
	}

	public void testEnd() {
		{
		long v = LongCompress.decode(43443561l, 44, 352);		
		 this.assertEquals(44, LongCompress.getMethodId(v));
		 this.assertEquals(352, LongCompress.getStackNum(v));
		
		 this.assertEquals(43443561l, LongCompress.getTime(v));
		}
		{
		 long v = LongCompress.decode(0, 44, 352);		
		 this.assertEquals(44, LongCompress.getMethodId(v));
		 this.assertEquals(352, LongCompress.getStackNum(v));
		
		 this.assertEquals(0, LongCompress.getTime(v));
		}
		
		{
			long v = LongCompress.decode(434332233l, 345, 61111);		
			 this.assertEquals(345, LongCompress.getMethodId(v));
			 this.assertEquals(61111, LongCompress.getStackNum(v));
			
			 this.assertEquals(434332233l, LongCompress.getTime(v));
			}
			{
			 long v = LongCompress.decode(1, 44000, 352);		
			 this.assertEquals(44000, LongCompress.getMethodId(v));
			 this.assertEquals(352, LongCompress.getStackNum(v));
			
			 this.assertEquals(1, LongCompress.getTime(v));
			}
		
	}

}
