
package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * @author xiaodu
 * @version 2011-8-25 ����05:09:17
 * 
 * modify by zhongting	2011-10-24 ����18:491:17
 * ����fieldName����߼�
 */
public class TransformerConfigCommand extends Command{
	
	//serialVersionUID ��������ȥʵ�֣���ͬ����ʵ�ֲ��ò�ͬ��serialVersionUID
	//private static final long serialVersionUID = -1606984675391858276L;

	private String id ;
	
	private String classPatternName;
	
	private String methodPatternName;
	
	private String methodTransformer;
	
	private String fieldName;
	
	private boolean transformer = false;

	private boolean isStatic = false;
	
	public TransformerConfigCommand() {
		super(Command.TRANSFORMER_CONFIG);
	}
	
	//�ڶ�����Server send transformer Commmond
	/**
	 * 
	 * @param tranformId			Ψһ��ʶ���к�
	 * @param classPatternName		����������ƣ�"/"�ָ
	 * @param methodPatternName		����ط�����������
	 * @param methodTransformer		methodAdapter���������ƣ�"."�ָ
	 * @param fieldName				�������������
	 * @param isStatic				Ҫ��ص������Ƿ���static
	 * @param transformer			�Ƿ���
	 */
	public TransformerConfigCommand(String tranformId, String classPatternName, String methodPatternName,
			String methodTransformer, String fieldName, boolean isStatic, boolean transformer) {
		super(Command.TRANSFORMER_CONFIG);
		this.id = tranformId;
		this.classPatternName = classPatternName;
		this.methodPatternName = methodPatternName;
		this.methodTransformer = methodTransformer;
		this.fieldName = fieldName;
		this.isStatic = isStatic;
		this.transformer = transformer;
	}


	public void read(ObjectInput in) throws IOException, ClassNotFoundException {
		classPatternName = in.readUTF();
		methodPatternName = in.readUTF();
		methodTransformer = in.readUTF();
		fieldName = in.readUTF();
		isStatic = in.readBoolean();
		transformer = in.readBoolean();
		id = in.readUTF();
	}

	public void write(ObjectOutput out) throws IOException {
		out.writeUTF(classPatternName);
		out.writeUTF(methodPatternName);
		out.writeUTF(methodTransformer);
		out.writeUTF(fieldName);
		out.writeBoolean(isStatic);
		out.writeBoolean(transformer);
		out.writeUTF(id);
	}

	public String getClassPatternName() {
		return classPatternName;
	}

	public void setClassPatternName(String classPatternName) {
		this.classPatternName = classPatternName;
	}

	public String getMethodPatternName() {
		return methodPatternName;
	}

	public void setMethodPatternName(String methodPatternName) {
		this.methodPatternName = methodPatternName;
	}

	public String getMethodTransformer() {
		return methodTransformer;
	}

	public void setMethodTransformer(String methodTransformer) {
		this.methodTransformer = methodTransformer;
	}

	public boolean isTransformer() {
		return transformer;
	}

	public void setTransformer(boolean transformer) {
		this.transformer = transformer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	
	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("classPatternName:").append(classPatternName).append(",");
		sb.append("methodPatternName:").append(methodPatternName).append(",");
		sb.append("methodTransformer:").append(methodTransformer).append(",fieldName:").append(fieldName).append("]");
		
		return sb.toString();
	}
	
	

}
