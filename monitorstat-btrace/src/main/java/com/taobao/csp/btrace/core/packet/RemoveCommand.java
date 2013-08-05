
package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * @author xiaodu
 * @version 2011-8-25 ÏÂÎç05:09:17
 */
public class RemoveCommand extends Command{
	
	private String id;
	
	public RemoveCommand(String id){
		super(Command.REMOVE);
		this.id = id;
	}

	public RemoveCommand() {
		super(Command.REMOVE);
	}


	public void read(ObjectInput in) throws IOException, ClassNotFoundException {
		this.id = in.readUTF();
	}

	public void write(ObjectOutput out) throws IOException {
		out.writeUTF(this.id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
