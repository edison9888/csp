package com.taobao.csp.dataserver.memcache.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * java序列化
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-11-16
 */
public class JavaCodec {

	private static final Logger logger = Logger.getLogger(JavaCodec.class);
	/**
	 * 将对象序列化为字节
	 * 
	 * @param objContent
	 * @return
	 * @throws IOException
	 */
    public static byte[] encodeObject(final Object objContent) throws IOException {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream output = null;
        try {
            baos = new ByteArrayOutputStream(1024);
            output = new ObjectOutputStream(baos);
            output.writeObject(objContent);
        }
        catch (final IOException ex) {
            throw ex;

        }
        finally {
            if (output != null) {
                try {
                    output.close();
                    if (baos != null) {
                        baos.close();
                    }
                }
                catch (final IOException ex) {
                    logger.error("Failed to close stream.", ex);
                }
            }
        }
        return baos != null ? baos.toByteArray() : null;
    }
	
    /**
     * 反序列化
     * @param objContent
     * @return
     * @throws IOException
     */
    public static Object decodeObject(final byte[] objContent) throws IOException {
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(objContent);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        }
        catch (final IOException ex) {
            throw ex;
        }
        catch (final ClassNotFoundException ex) {
           logger.warn("Failed to decode object.", ex);
        }
        finally {
            if (ois != null) {
                try {
                    ois.close();
                    bais.close();
                }
                catch (final IOException ex) {
                    logger.error("Failed to close stream.", ex);
                }
            }
        }
        return obj;
    }
	
}
