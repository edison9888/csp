
package com.taobao.csp.btrace.core.script.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.objectweb.asm.AnnotationVisitor;
import com.taobao.csp.objectweb.asm.Attribute;
import com.taobao.csp.objectweb.asm.ClassAdapter;
import com.taobao.csp.objectweb.asm.ClassReader;
import com.taobao.csp.objectweb.asm.ClassVisitor;
import com.taobao.csp.objectweb.asm.ClassWriter;
import com.taobao.csp.objectweb.asm.FieldVisitor;
import com.taobao.csp.objectweb.asm.Label;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;
import com.taobao.csp.objectweb.asm.Opcodes;
import com.taobao.csp.objectweb.asm.Type;

/**
 * 
 * @author xiaodu
 * @version 2011-8-18 下午05:28:35
 */
public class TestClassAdapter extends ClassAdapter{

	public TestClassAdapter(ClassVisitor cv) {
		super(cv);
	}
	
	
	
	

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// TODO Auto-generated method stub
		super.visit(version, access, name, signature, superName, interfaces);
	}





	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		// TODO Auto-generated method stub
		return super.visitAnnotation(desc, visible);
	}





	@Override
	public void visitAttribute(Attribute attr) {
		// TODO Auto-generated method stub
		super.visitAttribute(attr);
	}





	@Override
	public void visitEnd() {
		// TODO Auto-generated method stub
		super.visitEnd();
	}





	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		// TODO Auto-generated method stub
		return super.visitField(access, name, desc, signature, value);
	}





	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		// TODO Auto-generated method stub
		super.visitInnerClass(name, outerName, innerName, access);
	}

	@Override
	public MethodVisitor visitMethod(final int access, String name, String desc, String signature, String[] exceptions) {
		
		MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
		
		return new MethodAdapter(v) {            

            public void visitCode() {
                String c = "java/lang/Exception";
                String d = "(Ljava/lang/String;)V";
                Label end = new Label();
                mv.visitTypeInsn(Opcodes.NEW, c);
                mv.visitInsn(Opcodes.DUP);
                mv.visitLdcInsn("代码注入强制中断程序 -利用Exception在Method开始就抛出异常!");
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                        c,
                        "<init>",
                        d);
                mv.visitInsn(Opcodes.ATHROW);
                mv.visitLabel(end);

            }
        };
		
		
	}





	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		// TODO Auto-generated method stub
		super.visitOuterClass(owner, name, desc);
	}





	@Override
	public void visitSource(String source, String debug) {
		// TODO Auto-generated method stub
		super.visitSource(source, debug);
	}



	 public static final String JAVA_LANG_OBJECT = 
	        Type.getInternalName(Object.class);

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		
		 String name = "ApacheLoadrunTask";
	        FileInputStream fis = new FileInputStream("D:\\taobao-SVNROOT\\coremonitor\\monitorstat-btrace\\target\\classes\\com\\taobao\\csp\\btrace\\core\\script\\Test.class");
	        ClassReader reader = new ClassReader(new BufferedInputStream(fis));
	        FileOutputStream fos = new FileOutputStream(name + ".class");
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
	        
	        reader.accept(new TestClassAdapter(writer), ClassReader.SKIP_FRAMES);
	        fos.write(writer.toByteArray());

	}

}
