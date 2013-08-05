/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.seria;

import java.io.ByteArrayInputStream;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

/**
 * @author xiaodu
 * 
 *         ÏÂÎç2:38:35
 */
public class ObjectProtocolDecoder extends CumulativeProtocolDecoder {

	private Kryo kryo = new Kryo();

	private int maxObjectSize = 10* 1024 * 1024; // 10MB
	
	private ReentrantLock    lock      = new ReentrantLock();

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (!in.prefixedDataAvailable(4, maxObjectSize)) {
			return false;
		}
		
		out.write(readerObject(in));
		return true;
	}

	private Object readerObject(IoBuffer in) {
		int version = in.getInt();
		int length = in.getInt();
		byte[] bytes = new byte[length];
		in.get(bytes);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		lock.lock();
		try{
			Object obj = kryo.readClassAndObject(new Input(bais));
			return obj;
		}catch (Exception e) {
			in.clear();
		}finally{
			lock.unlock();
		}
		return new Object();

	}

	public int getMaxObjectSize() {
		return maxObjectSize;
	}

	public void setMaxObjectSize(int maxObjectSize) {
		this.maxObjectSize = maxObjectSize;
	}

}
