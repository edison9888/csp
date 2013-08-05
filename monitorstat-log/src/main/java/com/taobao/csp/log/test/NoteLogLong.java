
/**
 * monitorstat-log
 */
package com.taobao.csp.log.test;

import java.util.concurrent.atomic.AtomicReference;

import com.taobao.csp.log.MonitorConstants;
import com.taobao.csp.log.NoteLog;


/**
 * @author xiaodu
 *
 * 
 */
public class NoteLogLong implements NoteLog<Long>{
	
	
	private final AtomicReference<long[]> values = new AtomicReference<long[]>();
	
	
	private int valueSize = 0;
	
	public int getValueSize(){
		return valueSize;
	}
	
	public NoteLogLong(int size){
		this.valueSize = size;
		long[] init = new long[this.valueSize];
		this.values.set(init);
	}
	

	@Override
	public void addNote(Long[] v) {
		
		if(v.length != this.valueSize){
			return ;
		}
		long[] current;
		long[] update = new long[this.valueSize];
		do {
			current = values.get();
			for(int i=0;i<this.valueSize;i++){
				update[i] = current[i] + v[i];
			}
		} while(! values.compareAndSet(current, update));
	}
	
	
	
	public long[] getValues(){
		long[] current = values.get();
		long[] update = new long[this.valueSize];
		
		do {
			current = values.get();
			for(int i=0;i<this.valueSize;i++){
				update[i] = current[i] + current[i];
			}
		} while(! values.compareAndSet(current, update));
		
		return current;
	}


	@Override
	public String getString() {
		StringBuilder sb = new StringBuilder();
		long[] current = getValues();
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
