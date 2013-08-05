
package com.taobao.csp.btrace.core.script;

import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;
import com.taobao.csp.objectweb.asm.Opcodes;
import com.taobao.csp.objectweb.asm.Type;

/**
 * 获取filed 无法单独获取，必须要在某个方法被执行的时候获取
 * @author xiaodu
 * @version 2011-10-15 下午03:13:07
 */
public class ShowFiledMethod extends MethodAdapter{
	
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
	
	
	private Map<String,FieldInfo> fieldMap =null;
	
	private String fieldName;

	public ShowFiledMethod(MethodVisitor mv,String fieldName,Map<String,FieldInfo> fieldMap) {
		super(mv);
		this.fieldName = fieldName;
		this.fieldMap = fieldMap;
	}

	@Override
	public void visitCode() {
		
		if(fieldMap !=null&&fieldMap.get(fieldName)!=null){
			FieldInfo field = fieldMap.get(fieldName);
			this.visitVarInsn(Opcodes.ALOAD, 0);//将this 入栈
			this.visitFieldInsn(Opcodes.GETFIELD, field.getFieldClassName(), field.getFieldName(), field.getFieldType());//将此filed 入栈
			box( field.getFieldType());//转换基本类型为对象
			//visitMethodInsn(Opcodes.INVOKESTATIC, arg1, arg2, arg3);放入全局类中
			
		}
		
		super.visitCode();
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
