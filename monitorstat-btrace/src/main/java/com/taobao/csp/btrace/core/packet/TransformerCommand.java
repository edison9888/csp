//
//package com.taobao.csp.btrace.core.packet;
//
//import java.io.IOException;
//import java.io.ObjectInput;
//import java.io.ObjectOutput;
//
///**
// * 
// * @author xiaodu
// * @version 2011-8-25 ÏÂÎç05:09:17
// */
//public class TransformerCommand extends Command{
//	
//	private String id ;
//	
//	private String classPatternName;
//	
//	private String methodPatternName;
//	
//	private String methodTransformer;
//	
//	private boolean transformer = false;
//
//	public TransformerCommand() {
//		super(Command.TRANSFORMER);
//	}
//	
//	public TransformerCommand(String tranformId,String classPatternName,String methodPatternName,String methodTransformer,boolean transformer) {
//		super(Command.TRANSFORMER);
//		this.id = tranformId;
//		this.classPatternName = classPatternName;
//		this.methodPatternName = methodPatternName;
//		this.methodTransformer = methodTransformer;
//		this.transformer = transformer;
//	}
//
//
//	public void read(ObjectInput in) throws IOException, ClassNotFoundException {
//		classPatternName = in.readUTF();
//		methodPatternName = in.readUTF();
//		methodTransformer = in.readUTF();
//		transformer = in.readBoolean();
//	}
//
//	public void write(ObjectOutput out) throws IOException {
//		out.writeUTF(classPatternName);
//		out.writeUTF(methodPatternName);
//		out.writeUTF(methodTransformer);
//		out.writeBoolean(transformer);
//	}
//
//	public String getClassPatternName() {
//		return classPatternName;
//	}
//
//	public void setClassPatternName(String classPatternName) {
//		this.classPatternName = classPatternName;
//	}
//
//	public String getMethodPatternName() {
//		return methodPatternName;
//	}
//
//	public void setMethodPatternName(String methodPatternName) {
//		this.methodPatternName = methodPatternName;
//	}
//
//	public String getMethodTransformer() {
//		return methodTransformer;
//	}
//
//	public void setMethodTransformer(String methodTransformer) {
//		this.methodTransformer = methodTransformer;
//	}
//
//	public boolean isTransformer() {
//		return transformer;
//	}
//
//	public void setTransformer(boolean transformer) {
//		this.transformer = transformer;
//	}
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	@Override
//	public String toString() {
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append("[").append("classPatternName:").append(classPatternName).append(",");
//		sb.append("methodPatternName:").append(methodPatternName).append(",");
//		sb.append("methodTransformer:").append(methodTransformer).append("]");
//		
//		return sb.toString();
//	}
//	
//	
//
//}
