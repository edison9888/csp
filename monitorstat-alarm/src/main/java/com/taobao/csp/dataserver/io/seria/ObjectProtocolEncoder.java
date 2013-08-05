/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.seria;

import java.io.NotSerializableException;
import java.io.Serializable;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author xiaodu
 * 
 *         下午2:38:03
 */
public class ObjectProtocolEncoder implements ProtocolEncoder {

	private Kryo kryo = new Kryo();
	

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {

		if (!(message instanceof Serializable)) {
			throw new NotSerializableException();
		}
		Output output = new Output(new ByteArrayOutputStream());
		synchronized (kryo) {
			kryo.writeClassAndObject(output, message);
		}
		int c = output.toBytes().length;
		
		int capacity = c + 4;
		
		IoBuffer buffer = IoBuffer.allocate(capacity, false);
        buffer.setAutoExpand(true);
        
        buffer.putInt(-1);//预留一个版本,用于后面可能的序列化 修改
        
        buffer.putInt(c);
        
        buffer.put(output.toBytes());
        buffer.flip();
		out.write(buffer);

	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

}
