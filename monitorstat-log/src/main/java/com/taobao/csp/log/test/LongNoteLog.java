
/**
 * monitorstat-log
 */
package com.taobao.csp.log.test;

import com.taobao.csp.log.MonitorConstants;
import com.taobao.csp.log.NoteLog;


/**
 * @author xiaodu
 *
 * 
 */
public class LongNoteLog implements NoteLog<Long>{
	
	private long[] values = null;
	
	private int valueSize = 0;
	
	public LongNoteLog(int size){
		this.valueSize = size;
		values = new long[this.valueSize];
	}
	

	@Override
	public void addNote(Long[] v) {
		if(values == null){
			synchronized (this) {
				if(values == null){
					values  =new long[v.length];
				}
			}
		}
		
		synchronized (this) {
			for(int i=0;i<values.length&&i<v.length;i++){
				values[i]+=v[i];
			}
		}
	}


	public Long[] getValues() {
		Long[] tmp = new Long[values.length];
		synchronized (this) {
			for(int i=0;i<values.length;i++){
				tmp[i]=values[i];
				values[i] = 0;
			}
		}
		return tmp;
	}


	/* (non-Javadoc)
	 * @see com.taobao.csp.log.NoteLog#fillString(java.lang.StringBuilder)
	 */
	@Override
	public String getString() {
		StringBuilder sb = new StringBuilder();
		Long[] current = getValues();
		if(current != null){
			for(long k:current){
				long tmp = k;
				sb.append(tmp).append(MonitorConstants.SPLITTER_CHAR);
			}
		}
		return sb.toString();
	}


	/* (non-Javadoc)
	 * @see com.taobao.csp.log.NoteLog#getNoteLogType()
	 */
	@Override
	public int getNoteLogType() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
