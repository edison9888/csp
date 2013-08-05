
package com.taobao.csp.btrace.core.script;

import com.taobao.csp.objectweb.asm.Label;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;
import com.taobao.csp.objectweb.asm.Opcodes;

/**
 * 
 * @author xiaodu
 * @version 2011-8-18 ����09:17:58
 */
public class InjectMethodException extends MethodAdapter{
	
	private int methodIndex=0;
	public InjectMethodException(MethodVisitor mv) {
		super(mv);
	}

	@Override
	public void visitCode() {
		 Label labelIf = new Label();
		 this.visitLdcInsn(methodIndex);
		 visitMethodInsn(Opcodes.INVOKESTATIC, "com/taobao/csp/btrace/core/BtraceRunTime", "preventClassMethodRun", "(I)Z");
		 visitJumpInsn(Opcodes.IFEQ, labelIf);
		
		 String c = "java/lang/Exception";
         String d = "(Ljava/lang/String;)V";
         Label end = new Label();
         this.visitTypeInsn(Opcodes.NEW, c);
         this.visitInsn(Opcodes.DUP);
         this.visitLdcInsn("����ע��ǿ���жϳ��� -����Exception��Method��ʼ���׳��쳣!");
         this.visitMethodInsn(Opcodes.INVOKESPECIAL,
                 c,
                 "<init>",
                 d);
         this.visitInsn(Opcodes.ATHROW);
         this.visitLabel(end);
         
         this.visitLabel(labelIf);
         
		
		super.visitCode();
	}




	@Override
	public void visitEnd() {
		super.visitEnd();
	}

	

}
