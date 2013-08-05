
package com.taobao.jprof.core;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author xiaodu
 * @version 2010-9-28 ÏÂÎç01:43:07
 */
public class VelocityEngineMethodAdapter  extends MethodAdapter{
	
	private String className;
	private String methodName;
	public VelocityEngineMethodAdapter(MethodVisitor arg0, String fileName,
			String className, String methodName) {
		super(arg0);
		this.className = className;
		this.methodName = methodName;
	}

	public void visitCode() {
		this.visitVarInsn(Opcodes.ALOAD,1);
		this.visitLdcInsn(this.className);
		this.visitLdcInsn(this.methodName);
		this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/taobao/jprof/JProfMethodCache",
				"Request", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I");
		this.visitMethodInsn(INVOKESTATIC, "com/taobao/jprof/JProfProfiler",
				"Start", "(I)V");
		super.visitCode();
	}
	
	public void visitLineNumber(final int line, final Label start) {
		super.visitLineNumber(line, start);
	}
	
	
	public void visitInsn(int inst) {
		switch (inst) {
		case Opcodes.ARETURN:
		case Opcodes.DRETURN:
		case Opcodes.FRETURN:
		case Opcodes.IRETURN:
		case Opcodes.LRETURN:
		case Opcodes.RETURN:
		case Opcodes.ATHROW:
			this.visitVarInsn(Opcodes.ALOAD,1);
			this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/taobao/jprof/JProfMethodCache",
					"Request", "(Ljava/lang/String;)I");
			this.visitMethodInsn(INVOKESTATIC,
					"com/taobao/jprof/JProfProfiler", "End", "(I)V");
			break;
		default:
			break;
		}

		super.visitInsn(inst);
	}

}
