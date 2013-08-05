/**
 * 
 */
package com.taobao.jprof.core;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.taobao.jprof.JProfFilter;

/**
 * @author luqi
 * 
 */

class JProfTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		if (!JProfFilter.IsNeedInject(className)) {
			return classfileBuffer;
		}
		
		if (!JProfFilter.IsNotNeedInject(className)) {
			return classfileBuffer;
		}

		byte[] result = classfileBuffer;

		try {
			ClassReader reader = new ClassReader(classfileBuffer);
			ClassWriter writer = new JProfClassWriter(ClassWriter.COMPUTE_MAXS);
			ClassAdapter adapter = new JProfClassAdapter(writer, className);
			reader.accept(adapter, 0);
			
			result = writer.toByteArray();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return classfileBuffer;

		}
	}
}
