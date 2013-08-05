
/**
 * monitorstat-log
 */
package com.taobao.csp.log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author xiaodu
 *
 * ÏÂÎç4:07:32
 */
public class NoteLogString implements NoteLog<String>{
	
	private int MAX_SIZE = 300;
	
	
	private LinkedList<String> linkString = null;

	private ReentrantLock timerLock = new ReentrantLock();

	public NoteLogString() {
		linkString = new LinkedList<String>();
	}

	@Override
	public void addNote(String[] v) {
		timerLock.lock();
		try {
			for (int i = 0; i <v.length; i++) {
				
				if(linkString.size() > MAX_SIZE){
					return ;
				}
				
				linkString.add(v[i]);
				
			}
		} finally {
			timerLock.unlock();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.taobao.csp.log.NoteLog#fillString(java.lang.StringBuilder)
	 */
	@Override
	public String getString() {
		StringBuilder sb = new StringBuilder();
		timerLock.lock();
		try {
			Iterator<String> it = linkString.iterator();
			while(it.hasNext()){
				sb.append(it.next()).append(MonitorConstants.SPLITTER_CHAR);
				it.remove();
			}
		} finally {
			timerLock.unlock();
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.log.NoteLog#getNoteLogType()
	 */
	@Override
	public int getNoteLogType() {
		return NoteLog.NoteStringType;
	}
	

}
