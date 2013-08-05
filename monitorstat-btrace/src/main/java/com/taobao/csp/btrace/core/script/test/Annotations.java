package com.taobao.csp.btrace.core.script.test;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.objectweb.asm.AnnotationVisitor;
import com.taobao.csp.objectweb.asm.ClassAdapter;
import com.taobao.csp.objectweb.asm.ClassReader;
import com.taobao.csp.objectweb.asm.ClassWriter;
import com.taobao.csp.objectweb.asm.Label;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;
import com.taobao.csp.objectweb.asm.Opcodes;
import com.taobao.csp.objectweb.asm.Type;


public class Annotations {

    public static void foo(final @NotNull
    String arg)
    {
        System.out.println(arg);
    }

    public static void main(final String[] args) throws Exception {
        System.out.println("Calling foo(null) results in a NullPointerException:");
        try {
            foo("1");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        final String n = Annotations.class.getName();
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassReader cr = new ClassReader(n);
        cr.accept(new ClassAdapter(cw) {

            public MethodVisitor visitMethod(
                final int access,
                final String name,
                final String desc,
                final String signature,
                final String[] exceptions)
            {
                final Type[] args = Type.getArgumentTypes(desc);
                MethodVisitor v = cv.visitMethod(access,
                        name,
                        desc,
                        signature,
                        exceptions);
                return new MethodAdapter(v) {

                    private final List<Integer> params = new ArrayList<Integer>();

                    public AnnotationVisitor visitParameterAnnotation(
                        final int parameter,
                        final String desc,
                        final boolean visible)
                    {
                        AnnotationVisitor av;
                        av = mv.visitParameterAnnotation(parameter,
                                desc,
                                visible);
                        if (desc.equals("LNotNull;")) {
                            params.add(new Integer(parameter));
                        }
                        return av;
                    }

                    public void visitCode() {
                        int var = (access & Opcodes.ACC_STATIC) == 0 ? 1 : 0;
                        for (int p = 0; p < params.size(); ++p) {
                            int param = params.get(p).intValue();
                            for (int i = 0; i < param; ++i) {
                                var += args[i].getSize();
                            }
                            String c = "java/lang/IllegalArgumentException";
                            String d = "(Ljava/lang/String;)V";
                            Label end = new Label();
                            mv.visitVarInsn(Opcodes.ALOAD, var);
                            mv.visitJumpInsn(Opcodes.IFNONNULL, end);
                            mv.visitTypeInsn(Opcodes.NEW, c);
                            mv.visitInsn(Opcodes.DUP);
                            mv.visitLdcInsn("Argument " + param
                                    + " must not be null");
                            mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                                    c,
                                    "<init>",
                                    d);
                            mv.visitInsn(Opcodes.ATHROW);
                            mv.visitLabel(end);
                        }
                    }
                };
            }
        }, 0);

        Class<?> c = new ClassLoader() {
            public Class<?> loadClass(final String name)
                    throws ClassNotFoundException
            {
                if (name.equals(n)) {
                    byte[] b = cw.toByteArray();
                    return defineClass(name, b, 0, b.length);
                }
                return super.loadClass(name);
            }
        }.loadClass(n);

        System.out.println();
        System.out.println("Calling foo(null) on the transformed class results in an IllegalArgumentException:");
        Method m = c.getMethod("foo", new Class<?>[] { String.class });
        try {
            m.invoke(null, new Object[] { "1" });
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace(System.out);
        }
    }
}
