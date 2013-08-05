
package com.taobao.csp.btrace.core.script;


import com.taobao.csp.btrace.core.BtraceMethodCache;
import com.taobao.csp.btrace.core.BtraceUtils;
import com.taobao.csp.btrace.core.client.BtraceClientProfiler;
import com.taobao.csp.objectweb.asm.Label;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;
import com.taobao.csp.objectweb.asm.Opcodes;
import com.taobao.csp.objectweb.asm.Type;

/**
 * 
 * @author xiaodu
 * @version 2011-8-18 ����09:17:58
 */
public class ShowParametersMethod extends MethodAdapter implements Opcodes{
	
	  public static final String JAVA_LANG_THREAD_LOCAL =
	        Type.getInternalName(ThreadLocal.class);
	    public static final String JAVA_LANG_THREAD_LOCAL_GET = "get";
	    public static final String JAVA_LANG_THREAD_LOCAL_GET_DESC = "()Ljava/lang/Object;";
	    public static final String JAVA_LANG_THREAD_LOCAL_SET = "set";
	    public static final String JAVA_LANG_THREAD_LOCAL_SET_DESC = "(Ljava/lang/Object;)V";

	    public static final String JAVA_LANG_STRING =
	        Type.getInternalName(String.class);
	    public static final String JAVA_LANG_STRING_DESC = 
	        Type.getDescriptor(String.class);
	  
	    public static final String JAVA_LANG_NUMBER = 
	        Type.getInternalName(Number.class);
	    public static final String JAVA_LANG_BOOLEAN = 
	        Type.getInternalName(Boolean.class);
	    public static final String JAVA_LANG_CHARACTER = 
	        Type.getInternalName(Character.class);
	    public static final String JAVA_LANG_BYTE = 
	        Type.getInternalName(Byte.class);
	    public static final String JAVA_LANG_SHORT = 
	        Type.getInternalName(Short.class);
	    public static final String JAVA_LANG_INTEGER = 
	        Type.getInternalName(Integer.class);
	    public static final String JAVA_LANG_LONG = 
	        Type.getInternalName(Long.class);
	    public static final String JAVA_LANG_FLOAT = 
	        Type.getInternalName(Float.class);
	    public static final String JAVA_LANG_DOUBLE = 
	        Type.getInternalName(Double.class);

	    public static final String BOX_VALUEOF = "valueOf";
	    public static final String BOX_BOOLEAN_DESC = "(Z)Ljava/lang/Boolean;";
	    public static final String BOX_CHARACTER_DESC = "(C)Ljava/lang/Character;";
	    public static final String BOX_BYTE_DESC = "(B)Ljava/lang/Byte;";
	    public static final String BOX_SHORT_DESC = "(S)Ljava/lang/Short;";
	    public static final String BOX_INTEGER_DESC = "(I)Ljava/lang/Integer;";
	    public static final String BOX_LONG_DESC = "(J)Ljava/lang/Long;";
	    public static final String BOX_FLOAT_DESC = "(F)Ljava/lang/Float;";
	    public static final String BOX_DOUBLE_DESC = "(D)Ljava/lang/Double;";

	   
	    
	    
	    public static final String Btrace_Client_Profiler = Type.getInternalName(BtraceClientProfiler.class);
	    
	    public static final String Btrace_Utils = Type.getInternalName(BtraceUtils.class);
	    
	    public static final String JAVA_LANG_THROWABLE =
	        Type.getInternalName(Throwable.class);
	    
	    
	    private Label throwableStart = new Label();
	    private Label throwableend = new Label();
	
	
	
	private Type[] paramTypes;
	
	private Type typeReturn ;
	
	private int methodIndex = -1;
	
	private int access;
	
	public ShowParametersMethod(int access,MethodVisitor mv,String className,String methodName,String desc) {
		super(mv);
		this.paramTypes = Type.getArgumentTypes(desc);
		this.typeReturn = Type.getReturnType(desc);
		int index = BtraceMethodCache.request();
		this.methodIndex = index;
		BtraceMethodCache.setClassMethodName(index, className, methodName,desc);
		this.access = access;
	}
	
	

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		if(methodIndex != -1){
			if(paramTypes != null &&paramTypes.length>0&&index >0)
				BtraceMethodCache.setClassMethodParameters(methodIndex, name);
		}
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}
	
	

	@Override
	public void visitLineNumber(int line, Label start) {
		
		if(methodIndex != -1){
			BtraceMethodCache.setCodeLine(methodIndex, line);
		}
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		
		super.visitMethodInsn(opcode, owner, name, desc);
	}

	public int getMethodIndex() {
		return methodIndex;
	}

	public void setMethodIndex(int methodIndex) {
		this.methodIndex = methodIndex;
	}
	
	

	@Override
	public void visitCode() {
		
		 visitLabel(throwableStart);	//������
		 
		 push(this.getMethodIndex());	//����ָ��ż�����ֵ���������У�
		 //���þ�̬������start�Ĳ�������methodId��������ո���ջ��methodIndex,ֱ�Ӵ����˲���methodIndex
		 visitMethodInsn(Opcodes.INVOKESTATIC, Btrace_Client_Profiler, "start", "(I)V");
		 
		 if(paramTypes != null && paramTypes.length >0){
			 //������������
			 push(paramTypes.length);//���������С
			 //����һ�����������飬����������ֵѹ��ջ��
			 visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String");//������������
			 for(int i=0;i<paramTypes.length;i++){
				 visitInsn(DUP);	//����ջ����ֵ������������û��ѽ��
				 push(i);			//ѹ�뵱ǰ���������
				 int index = (access & ACC_STATIC) == 0 ? 1 : 0;
				 visitVarInsn(paramTypes[i].getOpcode(Opcodes.ILOAD), i+index);		//������������ջ
				 box(paramTypes[i]);
				 //���÷���
				 visitMethodInsn(Opcodes.INVOKESTATIC, Btrace_Utils, "toObjectString", "(Ljava/lang/Object;)Ljava/lang/String;");
				 visitInsn(Opcodes.AASTORE);	//�Ӳ���ջ�洢������
			 }
			 //��ѹ��ջ�е�ֵ���뵽ThreadProfilerData��
			 visitMethodInsn(Opcodes.INVOKESTATIC, Btrace_Client_Profiler, "setParamValues", "([Ljava/lang/String;)V");
		 }
		super.visitCode();
	}
	
	public void visitMaxs(int maxStack, int maxLocals) {
        visitLabel(throwableend);
        visitTryCatchBlock(throwableStart, throwableend, throwableend, JAVA_LANG_THROWABLE);
        
        visitInsn(ATHROW);
        super.visitMaxs(maxStack, maxLocals);
    }
	
	
	
	 public void push(int value) {
		 	//��ֵͬ�Ų�ͬ�ط����������������쳣��
	        if (value >= -1 && value <= 5) {
	            super.visitInsn(ICONST_0 + value);
	        } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
	            super.visitIntInsn(BIPUSH, value);
	        } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
	            super.visitIntInsn(SIPUSH, value);
	        } else {
	        	//LDC���ǳ����ص���˼
	            super.visitLdcInsn(Integer.valueOf(value));
	        }
	    }
	
	 public void box(Type type) {
	        box(type.getDescriptor());
	    }

	    public void box(String desc) {
	        int typeCode = desc.charAt(0);
	        switch (typeCode) {
	            case '[':
	            case 'L':
	                break;
	            case 'Z':
	                super.visitMethodInsn(INVOKESTATIC, JAVA_LANG_BOOLEAN,
	                                BOX_VALUEOF, 
	                                BOX_BOOLEAN_DESC);
	                break;
	            case 'C':
	                super.visitMethodInsn(INVOKESTATIC, JAVA_LANG_CHARACTER,
	                                BOX_VALUEOF, 
	                                BOX_CHARACTER_DESC);
	                break;
	            case 'B':
	                super.visitMethodInsn(INVOKESTATIC, JAVA_LANG_BYTE,
	                                BOX_VALUEOF, 
	                                BOX_BYTE_DESC);
	                break;
	            case 'S':
	                super.visitMethodInsn(INVOKESTATIC, JAVA_LANG_SHORT,
	                                BOX_VALUEOF, 
	                                BOX_SHORT_DESC);
	                break;
	            case 'I':
	                super.visitMethodInsn(INVOKESTATIC, JAVA_LANG_INTEGER,
	                                BOX_VALUEOF, 
	                                BOX_INTEGER_DESC);
	                break;
	            case 'J':
	                super.visitMethodInsn(INVOKESTATIC, JAVA_LANG_LONG,
	                                BOX_VALUEOF, 
	                                BOX_LONG_DESC);
	                break;
	            case 'F':
	                super.visitMethodInsn(INVOKESTATIC, JAVA_LANG_FLOAT,
	                                BOX_VALUEOF, 
	                                BOX_FLOAT_DESC);
	                break;
	            case 'D':
	                super.visitMethodInsn(INVOKESTATIC, JAVA_LANG_DOUBLE,
	                                BOX_VALUEOF, 
	                                BOX_DOUBLE_DESC);
	                break;                              
	        }
	    }
	@Override
	public void visitEnd() {
		super.visitEnd();
	}

	private void doReturn(int opcode){
		
		if(typeReturn != null&&typeReturn.getSize()>0){
			dupReturnValue(opcode);
			box(typeReturn);
			visitMethodInsn(Opcodes.INVOKESTATIC, Btrace_Utils, "toObjectString", "(Ljava/lang/Object;)Ljava/lang/String;");
			visitMethodInsn(Opcodes.INVOKESTATIC, Btrace_Client_Profiler, "setReturnValue", "(Ljava/lang/String;)V");
		}
	}
	

	 public void visitInsn(int opcode) {
	        switch (opcode) {
	            case IRETURN:
	            case ARETURN:
	            case FRETURN:                           
	            case LRETURN:
	            case DRETURN:
	            case RETURN:
	            	doReturn(opcode);
	                break;
	            default:                           
	                break;
	        }
	        
	        
	      switch (opcode) {
			case Opcodes.ARETURN:
			case Opcodes.DRETURN:
			case Opcodes.FRETURN:
			case Opcodes.IRETURN:
			case Opcodes.LRETURN:
			case Opcodes.RETURN:
			case Opcodes.ATHROW:
				this.visitLdcInsn(this.getMethodIndex());
				this.visitMethodInsn(INVOKESTATIC,
						Btrace_Client_Profiler, "end", "(I)V");
				break;
			default:
				break;
			}
	        
	        super.visitInsn(opcode);
	 }
	 
	 /**
	  * ���� ����ջ�� ����
	  * @param returnOpcode
	  */
	 public void dupReturnValue(int returnOpcode) {
	        switch (returnOpcode) {
	            case IRETURN:
	            case FRETURN:
	            case ARETURN:            
	                super.visitInsn(DUP);
	                return;
	            case LRETURN:
	            case DRETURN:
	                super.visitInsn(DUP2);
	                return;
	            case RETURN:
	                return;
	            default:
	                throw new IllegalArgumentException("not return");
	        }
	    }
	

}
