/**
 * monitorstat-log
 */
package com.taobao.csp.log.test;

import java.util.concurrent.locks.ReentrantLock;

import com.taobao.csp.log.MonitorConstants;
import com.taobao.csp.log.NoteLog;

/**
 * @author xiaodu
 * 
 *         
 */
public class LongNoteLog1 implements NoteLog<Long> {

	private long[] values = null;

	private int valueSize = 0;

	private ReentrantLock timerLock = new ReentrantLock();

	public LongNoteLog1(int size) {
		this.valueSize = size;
		values = new long[this.valueSize];
	}

	@Override
	public void addNote(Long[] v) {
		if (values == null) {
			timerLock.lock();
			try {
				if (values == null) {
					values = new long[v.length];
				}
			} finally {
				timerLock.unlock();
			}

		}

		timerLock.lock();
		try {
			for (int i = 0; i < values.length && i < v.length; i++) {
				values[i] += v[i];
			}
		} finally {
			timerLock.unlock();
		}

	}

	public Long[] getValues() {
		Long[] tmp = new Long[values.length];
		timerLock.lock();
		try {
			for (int i = 0; i < values.length; i++) {
				tmp[i] = values[i];
				values[i] = 0;
			}
		} finally {
			timerLock.unlock();
		}

		return tmp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.taobao.csp.log.NoteLog#fillString(java.lang.StringBuilder)
	 */
	@Override
	public String getString() {
		StringBuilder sb = new StringBuilder();
		Long[] current = getValues();
		if (current != null) {
			for (long k : current) {
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
