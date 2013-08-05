
package com.taobao.csp.btrace.core.script;

import java.util.Map;

import com.taobao.csp.btrace.core.FieldInfo;
import com.taobao.csp.btrace.core.client.BtraceClientFieldProfiler;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;
import com.taobao.csp.objectweb.asm.Opcodes;
import com.taobao.csp.objectweb.asm.Type;

/**
 * ��ȡfiled �޷�������ȡ������Ҫ��ĳ��������ִ�е�ʱ���ȡ
 * @author xiaodu
 * @version 2011-10-15 ����03:13:07
 * modify by zhongting
 * @version 2011-10-25 ����09:13:11
 */
public class ShowFieldMethod extends MethodAdapter implements Opcodes {

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

	public static final String JAVA_LANG_THREAD_LOCAL_GET = "get";
	public static final String JAVA_LANG_THREAD_LOCAL_GET_DESC = "()Ljava/lang/Object;";
	public static final String JAVA_LANG_THREAD_LOCAL_SET = "set";
	public static final String JAVA_LANG_THREAD_LOCAL_SET_DESC = "(Ljava/lang/Object;)V";	//����ΪObject������ֵΪvoid

	public static final String Btrace_Client_Field_Profiler = Type.getInternalName(BtraceClientFieldProfiler.class);

	private Map<String,FieldInfo> fieldMap =null;

	private String fieldName;
	private String methodName;	//���÷�������
	private boolean isStatic;	//�Ƿ��Ǿ�̬����
	
	public ShowFieldMethod(MethodVisitor mv, String methodName, String fieldName, Boolean isStatic, Map<String,FieldInfo> fieldMap) {
		super(mv);
		this.fieldName = fieldName;
		this.fieldMap = fieldMap;
		this.methodName = methodName;
		this.isStatic = isStatic;
	}

	@Override
	public void visitCode() {
		if(fieldMap != null && fieldMap.get(fieldName) != null){
			FieldInfo field = fieldMap.get(fieldName);
			
			this.visitVarInsn(Opcodes.ALOAD, 0);		//��this ��ջ����ȡ���������
			
			if(isStatic) {			//��̬�򷽷�
				this.visitFieldInsn(Opcodes.GETSTATIC, field.getFieldClassName(), field.getFieldName(), field.getFieldType());				
			} else {				//����filed��valueֵ���������ջ
				this.visitFieldInsn(Opcodes.GETFIELD, field.getFieldClassName(), field.getFieldName(), field.getFieldType());				
			}
			box( field.getFieldType());					//��ջ����ֵת��Ϊ��������
			visitLdcInsn(String.valueOf(field.getFieldClassName()));
			visitLdcInsn(String.valueOf(field.getFieldType()));
			visitLdcInsn(String.valueOf(methodName));
			visitLdcInsn(String.valueOf(fieldName));
			//��ѹ���ջ�е�������������start����
			super.visitMethodInsn(Opcodes.INVOKESTATIC, Btrace_Client_Field_Profiler, "start", 
					"(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
		}
		super.visitCode();
	}

	@Override
	public void visitEnd() {
		super.visitEnd();
		//��β��Ҳ���Լ�¼һ��Field��ֵ����ʶ���뷽��ǰ��ֵ�ı仯
		//����ʱ��Ҫע��
	}	

	public void visitInsn(int opcode) {
		//super.visitCode()��ĩβ������÷���ָ�������������ʱ�򣩣�����BtraceClientFieldProfiler��end����
		switch (opcode) {
		case IRETURN:
		case ARETURN:
		case FRETURN:                           
		case LRETURN:
		case DRETURN:
		case RETURN:		
		case Opcodes.ATHROW:
			if(fieldMap != null && fieldMap.get(fieldName)!= null){
				FieldInfo field = fieldMap.get(fieldName);
				this.visitVarInsn(Opcodes.ALOAD, 0);	
				
				if(isStatic) {			//��̬�򷽷�
					this.visitFieldInsn(Opcodes.GETSTATIC, field.getFieldClassName(), field.getFieldName(), field.getFieldType());				
				} else {				//����filed��valueֵ���������ջ
					this.visitFieldInsn(Opcodes.GETFIELD, field.getFieldClassName(), field.getFieldName(), field.getFieldType());				
				}				
				box( field.getFieldType());
				super.visitMethodInsn(INVOKESTATIC,
						Btrace_Client_Field_Profiler, "end", JAVA_LANG_THREAD_LOCAL_SET_DESC);
			}			
			break;
		default:
			break;
		}
		super.visitInsn(opcode);
	}	


	public void box(String desc) {
		int typeCode = desc.charAt(0);
		switch (typeCode) {
		case '[':
		case 'L':
			break;
		case 'Z':
			super.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_BOOLEAN,
					BOX_VALUEOF, 
					BOX_BOOLEAN_DESC);
			break;
		case 'C':
			super.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_CHARACTER,
					BOX_VALUEOF, 
					BOX_CHARACTER_DESC);
			break;
		case 'B':
			super.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_BYTE,
					BOX_VALUEOF, 
					BOX_BYTE_DESC);
			break;
		case 'S':
			super.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_SHORT,
					BOX_VALUEOF, 
					BOX_SHORT_DESC);
			break;
		case 'I':
			super.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_INTEGER,
					BOX_VALUEOF, 
					BOX_INTEGER_DESC);
			break;
		case 'J':
			super.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_LONG,
					BOX_VALUEOF, 
					BOX_LONG_DESC);
			break;
		case 'F':
			super.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_FLOAT,
					BOX_VALUEOF, 
					BOX_FLOAT_DESC);
			break;
		case 'D':
			super.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_DOUBLE,
					BOX_VALUEOF, 
					BOX_DOUBLE_DESC);
			break;                              
		}
	}
}
