
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.item;

import java.io.Serializable;

import com.taobao.csp.dataserver.util.Util;

/**
 * @author xiaodu
 *
 * 下午8:22:52
 */
public enum ValueOperate implements Serializable{
	
	ADD
 {
		@Override
		public Object operate(Object src, Object dist) {
			try {
				return Util.add(src,dist);
			} catch (Exception e) {
				return src;
			}
		}
	},//加
	SUB {
		@Override
		public Object operate(Object src, Object dist) {
			try {
				return Util.sub(src,dist);
			} catch (Exception e) {
				return src;
			}
		}
	},//减
	MUL {
		@Override
		public Object operate(Object src, Object dist) {
			try {
				return Util.mul(src,dist);
			} catch (Exception e) {
				return src;
			}
		}
	},//乘
	DIV {
		@Override
		public Object operate(Object src, Object dist) {
			try {
				return Util.div(src,dist);
			} catch (Exception e) {
				return src;
			}
		}
	},//除
	REPLACE {
		@Override
		public Object operate(Object src, Object dist) {
			if(dist != null)
				return dist;
			return src;
		}
	}//代替
	,AVERAGE {
		@Override
		public Object operate(Object src, Object dist) {
			if(dist != null){
				try{
					return Util.average(src, dist);
				}catch (Exception e) {
				}
			}
			return src;
		}
	};//平均
	
	
	public abstract Object operate(Object src,Object dist);
	

}
