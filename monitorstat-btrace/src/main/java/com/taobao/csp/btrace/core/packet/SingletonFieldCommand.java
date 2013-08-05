package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author zhongting.zy
 * 用来标记单例的命令的
 */
public class SingletonFieldCommand extends Command {

	private String id ;
	
	private String classPatternName;
	
	private String fieldName;
	
	private boolean transformer = false;

	private boolean isStatic = false;	
	
	public SingletonFieldCommand() {
		super(SINGLETON_CLASS);
	}
	
	/**
	 * @param tranformId			唯一标识序列号
	 * @param classPatternName		被监控类名称（"/"分割）
	 * @param fieldName				被监控属性名称
	 * @param isStatic				要监控的属性是否是static
	 * @param transformer			是否传输
	 */	
	public SingletonFieldCommand(String tranformId, String classPatternName, String fieldName, 
			boolean isStatic, boolean transformer) {
		super(SINGLETON_CLASS);
		this.id = tranformId;
		this.classPatternName = classPatternName;
		this.fieldName = fieldName;
		this.isStatic = isStatic;
		this.transformer = transformer;		
	}

	private static final long serialVersionUID = -2462840025613389469L;

	@Override
	public void write(ObjectOutput out) throws IOException {
		out.writeUTF(classPatternName);
		out.writeUTF(fieldName);
		out.writeBoolean(isStatic);
		out.writeBoolean(transformer);
		out.writeUTF(id);
	}

	@Override
	public void read(ObjectInput in) throws IOException,
			ClassNotFoundException {
		classPatternName = in.readUTF();
		fieldName = in.readUTF();
		isStatic = in.readBoolean();
		transformer = in.readBoolean();
		id = in.readUTF();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("classPatternName:").append(classPatternName).append(",");
		sb.append("fieldName:").append(fieldName).append("]");
		return sb.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassPatternName() {
		return classPatternName;
	}

	public void setClassPatternName(String classPatternName) {
		this.classPatternName = classPatternName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isTransformer() {
		return transformer;
	}

	public void setTransformer(boolean transformer) {
		this.transformer = transformer;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}	
	
}
