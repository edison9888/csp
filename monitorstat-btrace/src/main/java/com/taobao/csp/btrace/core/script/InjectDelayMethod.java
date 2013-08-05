package com.taobao.csp.btrace.core.script;

import com.taobao.csp.btrace.core.client.SystemRuntime;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;
import com.taobao.csp.objectweb.asm.Opcodes;
import com.taobao.csp.objectweb.asm.Type;

public class InjectDelayMethod  extends MethodAdapter{
	
	
	public static final String System_Runtime= Type.getInternalName(SystemRuntime.class);
	
	private long delaytime;
	public InjectDelayMethod(MethodVisitor mv,long delaytime) {
		super(mv);
		this.delaytime = delaytime;
	}

	@Override
	public void visitCode() {
		if(delaytime>0){
			visitLdcInsn(Long.valueOf(delaytime));
			visitMethodInsn(Opcodes.INVOKESTATIC,System_Runtime, "await", "(L)V");
		}
		super.visitCode();
	}
	
	
	
}
