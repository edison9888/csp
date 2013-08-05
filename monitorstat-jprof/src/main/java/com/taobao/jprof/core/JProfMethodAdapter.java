package com.taobao.jprof.core;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.taobao.jprof.JProfMethodCache;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * 负责生成注入代码<br>
 * @author luqi
 *
 */
public class JProfMethodAdapter extends MethodAdapter {
	private int mMethodId = 0;

	public JProfMethodAdapter(MethodVisitor visitor, String fileName,
			String className, String methodName) {
		super(visitor);
		mMethodId = JProfMethodCache.Request();
		JProfMethodCache.UpdateFileName(mMethodId, fileName);
		JProfMethodCache.UpdateMethodName(mMethodId, className, methodName);
	}

	public void visitCode() {
		this.visitLdcInsn(mMethodId);
		this.visitMethodInsn(INVOKESTATIC, "com/taobao/jprof/JProfProfiler",
				"Start", "(I)V");

		super.visitCode();
	}

	public void visitLineNumber(final int line, final Label start) {
		JProfMethodCache.UpdateLineNum(mMethodId, line);
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
			this.visitLdcInsn(mMethodId);
			this.visitMethodInsn(INVOKESTATIC,
					"com/taobao/jprof/JProfProfiler", "End", "(I)V");
			break;
		default:
			break;
		}

		super.visitInsn(inst);
	}

}
