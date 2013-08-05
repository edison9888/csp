
package com.taobao.csp.btrace.core.script.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.btrace.core.FieldInfo;
import com.taobao.csp.btrace.core.script.ShowFieldMethod;
import com.taobao.csp.btrace.core.script.ShowParametersMethod;
import com.taobao.csp.objectweb.asm.AnnotationVisitor;
import com.taobao.csp.objectweb.asm.Attribute;
import com.taobao.csp.objectweb.asm.ClassAdapter;
import com.taobao.csp.objectweb.asm.ClassReader;
import com.taobao.csp.objectweb.asm.ClassWriter;
import com.taobao.csp.objectweb.asm.FieldVisitor;
import com.taobao.csp.objectweb.asm.Label;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;
import com.taobao.csp.objectweb.asm.Opcodes;
import com.taobao.csp.objectweb.asm.Type;
import com.taobao.test.TestFieldThread2;

/**
 * 
 * @author xiaodu
 * @version 2011-8-18 上午11:18:10
 */
public class TestMethodAdapter extends MethodAdapter {
	private int methodIndex=0;

	public static final String JAVA_LANG_OBJECT = 
			Type.getInternalName(Object.class);

	public TestMethodAdapter(MethodVisitor arg0) {
		super(arg0);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {

		return super.visitAnnotation(arg0, arg1);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return super.visitAnnotationDefault();
	}

	@Override
	public void visitAttribute(Attribute arg0) {
		// TODO Auto-generated method stub
		super.visitAttribute(arg0);
	}

	@Override
	public void visitCode() {
		//		 Label labelIf = new Label();
		//		 this.visitLdcInsn(methodIndex);
		//		 visitMethodInsn(Opcodes.INVOKESTATIC, "com/taobao/csp/btrace/core/BtraceRunTime", "preventClassMethodRun", "(I)Z");
		//		 visitJumpInsn(Opcodes.IFEQ, labelIf);
		//		
		//		 String c = "java/lang/Exception";
		//         String d = "(Ljava/lang/String;)V";
		//         Label end = new Label();
		//         this.visitTypeInsn(Opcodes.NEW, c);
		//         this.visitInsn(Opcodes.DUP);
		//         this.visitLdcInsn("代码注入强制中断程序 -利用Exception在Method开始就抛出异常!");
		//         this.visitMethodInsn(Opcodes.INVOKESPECIAL,
		//                 c,
		//                 "<init>",
		//                 d);
		//         this.visitInsn(Opcodes.ATHROW);
		//         this.visitLabel(end);
		//         
		//         this.visitLabel(labelIf);

		super.visitCode();
	}

	@Override
	public void visitEnd() {
		// TODO Auto-generated method stub
		super.visitEnd();
	}

	@Override
	public void visitFieldInsn(int arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		super.visitFieldInsn(arg0, arg1, arg2, arg3);
	}

	@Override
	public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3, Object[] arg4) {
		// TODO Auto-generated method stub
		super.visitFrame(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void visitIincInsn(int arg0, int arg1) {
		// TODO Auto-generated method stub
		super.visitIincInsn(arg0, arg1);
	}

	@Override
	public void visitInsn(int arg0) {
		// TODO Auto-generated method stub
		super.visitInsn(arg0);
	}

	@Override
	public void visitIntInsn(int arg0, int arg1) {
		// TODO Auto-generated method stub
		super.visitIntInsn(arg0, arg1);
	}

	@Override
	public void visitJumpInsn(int arg0, Label arg1) {
		// TODO Auto-generated method stub
		super.visitJumpInsn(arg0, arg1);
	}

	@Override
	public void visitLabel(Label arg0) {
		// TODO Auto-generated method stub
		super.visitLabel(arg0);
	}

	@Override
	public void visitLdcInsn(Object arg0) {
		// TODO Auto-generated method stub
		super.visitLdcInsn(arg0);
	}

	@Override
	public void visitLineNumber(int arg0, Label arg1) {
		// TODO Auto-generated method stub
		super.visitLineNumber(arg0, arg1);
	}

	@Override
	public void visitLocalVariable(String arg0, String arg1, String arg2, Label arg3, Label arg4, int arg5) {




		super.visitLocalVariable(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2) {
		// TODO Auto-generated method stub
		super.visitLookupSwitchInsn(arg0, arg1, arg2);
	}

	@Override
	public void visitMaxs(int arg0, int arg1) {
		// TODO Auto-generated method stub
		super.visitMaxs(arg0, arg1);
	}

	@Override
	public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		super.visitMethodInsn(arg0, arg1, arg2, arg3);
	}

	@Override
	public void visitMultiANewArrayInsn(String arg0, int arg1) {
		// TODO Auto-generated method stub
		super.visitMultiANewArrayInsn(arg0, arg1);
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int arg0, String arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return super.visitParameterAnnotation(arg0, arg1, arg2);
	}

	@Override
	public void visitTableSwitchInsn(int arg0, int arg1, Label arg2, Label[] arg3) {
		// TODO Auto-generated method stub
		super.visitTableSwitchInsn(arg0, arg1, arg2, arg3);
	}

	@Override
	public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2, String arg3) {
		// TODO Auto-generated method stub
		super.visitTryCatchBlock(arg0, arg1, arg2, arg3);
	}

	@Override
	public void visitTypeInsn(int arg0, String arg1) {
		// TODO Auto-generated method stub
		super.visitTypeInsn(arg0, arg1);
	}

	@Override
	public void visitVarInsn(int arg0, int arg1) {
		// TODO Auto-generated method stub
		super.visitVarInsn(arg0, arg1);
	}

	public static void main(final String[] args) throws Exception {

		FileInputStream fis = new FileInputStream("D:\\EclipseworkSpace\\coremonitor\\monitorstat-btrace\\target\\classes\\com\\taobao\\test\\TestFieldThread2.class");
		ClassReader reader = new ClassReader(new BufferedInputStream(fis));
		ClassWriter writer =  new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS) {
			protected String getCommonSuperClass(String type1, String type2) {
				try {
					return super.getCommonSuperClass(type1, type2);                   
				} catch (LinkageError le) {
					return JAVA_LANG_OBJECT;
				} catch (RuntimeException re) {
					return JAVA_LANG_OBJECT;
				}
			}
		};
		//final LocalVariablesSorter.Memento externalState = new LocalVariablesSorter.Memento();

		reader.accept(new ClassAdapter(writer) {

			@Override
			public FieldVisitor visitField(int access, String name,
					String desc, String signature, Object value) {
				//添加列表
				return super.visitField(access, name, desc, signature, value);
			}

			public MethodVisitor visitMethod(int access, String name, String desc, 
					String signature, String[] exceptions) {

				if(!name.equals("<init>") && !name.equals("<clinit>")){

					Type[] types = Type.getArgumentTypes(desc);
					for(Type type:types){
						System.out.print(type.getDescriptor() + "=");
						System.out.println(type.getOpcode(Opcodes.ILOAD));;
					}

					MethodVisitor mv = super.visitMethod(access, name, desc, 
							signature, exceptions);
					// return new LocalVariablesSorter(access, desc, mv, externalState);

					//return new ShowParametersMethod(access,mv,"className",name,desc);

					Map<String,FieldInfo> fieldMap = new HashMap<String,FieldInfo>();
					FieldInfo info = new FieldInfo();
					//            			 info.setFieldClassName("com/taobao/csp/btrace/core/script/test/Test2");
					//            			 info.setFieldName("mm");
					//            			 info.setFieldType(Type.getDescriptor(String.class));
					//            			 fieldMap.put("mm", info);
					String fieldName = "strField";
					info.setFieldClassName("com/taobao/test/TestFieldThread2");
					info.setFieldName(fieldName);
					info.setFieldType(Type.getDescriptor(String.class));
					fieldMap.put(fieldName, info);
					return new ShowFieldMethod(mv, "increase", fieldName, true, fieldMap);
					//return new TestMethodAdapter(mv);
				}
				return super.visitMethod(access, name, desc, 
						signature, exceptions);
			}
		}, ClassReader.SKIP_FRAMES);

		
		//String name = "com.taobao.test.MutiDemo";
		String name = "com.taobao.test.TestFieldThread2";
		//反射方法
		CL cl =  new CL();
		Class clazz = cl.defind(name, writer.toByteArray());

		Method m =  clazz.getMethod("test",String.class);
		Object o = clazz.newInstance();
		m.invoke("123");  

		


		//反射线程
		//        TestFieldThread2 thread = new TestFieldThread2();
		//        thread.start();
	}


	public static class CL extends ClassLoader{
		public Class<?> defind(String name,byte[] b){
			return this.defineClass(name, b, 0, b.length);
		}
	}

}
