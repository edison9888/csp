
package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * @author xiaodu
 * @version 2011-8-25 ÏÂÎç05:09:17
 */
public class TransformerClassCommand extends Command{
	
	private String id ;
	
	private String className;
	
	
	

	public TransformerClassCommand() {
		super(Command.TRANSFORMER_CLASS);
	}
	
	public TransformerClassCommand(String tranformId,String className) {
		super(Command.TRANSFORMER_CLASS);
		this.id = tranformId;
		this.className = className;
	}


	public void read(ObjectInput in) throws IOException, ClassNotFoundException {
		className = in.readUTF();
		id = in.readUTF();
	}

	public void write(ObjectOutput out) throws IOException {
		out.writeUTF(className);
		out.writeUTF(id);
	}

	

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("classPatternName:").append(className).append(",");
		sb.append("methodTransformer:").append("]");
		
		return sb.toString();
	}
	
	

}
