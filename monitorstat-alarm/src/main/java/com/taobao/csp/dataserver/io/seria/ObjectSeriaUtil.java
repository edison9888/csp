
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.seria;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;

/**
 * @author xiaodu
 *
 * ÉÏÎç10:21:46
 */
public class ObjectSeriaUtil {
	
	public static String protocol_Codec = "java";
	
	public static ProtocolCodecFactory getProtocolCodecFactory(){
		
		if("java".equals(protocol_Codec)){
			ObjectSerializationCodecFactory serial = new ObjectSerializationCodecFactory();
			serial.setDecoderMaxObjectSize(10485760);//10M
			serial.setEncoderMaxObjectSize(10485760);//10M
			return serial;
		}else if("kryo".equals(protocol_Codec)){
			return new KryoObjectSeriaFactory();
		}
		
		return new ObjectSerializationCodecFactory();
	}

}
