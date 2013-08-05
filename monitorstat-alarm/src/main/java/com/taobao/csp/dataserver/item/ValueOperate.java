
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.item;

import java.io.Serializable;

import com.taobao.csp.dataserver.util.Util;

/**
 * @author xiaodu
 *
 * ����8:22:52
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
	},//��
	SUB {
		@Override
		public Object operate(Object src, Object dist) {
			try {
				return Util.sub(src,dist);
			} catch (Exception e) {
				return src;
			}
		}
	},//��
	MUL {
		@Override
		public Object operate(Object src, Object dist) {
			try {
				return Util.mul(src,dist);
			} catch (Exception e) {
				return src;
			}
		}
	},//��
	DIV {
		@Override
		public Object operate(Object src, Object dist) {
			try {
				return Util.div(src,dist);
			} catch (Exception e) {
				return src;
			}
		}
	},//��
	REPLACE {
		@Override
		public Object operate(Object src, Object dist) {
			if(dist != null)
				return dist;
			return src;
		}
	}//����
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
	};//ƽ��
	
	
	public abstract Object operate(Object src,Object dist);
	

}
