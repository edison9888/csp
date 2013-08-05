
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.seria;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @author xiaodu
 *
 * ����2:29:57
 */
public class KryoObjectSeriaFactory implements ProtocolCodecFactory {
	
	private ProtocolEncoder encoder  ;
	private ProtocolDecoder decoder ;
	
	
	public KryoObjectSeriaFactory(){
		encoder = new ObjectProtocolEncoder();
		decoder = new ObjectProtocolDecoder();
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

}
