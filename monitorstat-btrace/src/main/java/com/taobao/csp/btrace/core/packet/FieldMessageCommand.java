package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.taobao.csp.btrace.core.FieldProfilerInfo;
/**
 * 
 * @author zhongting.zy
 * @version 2011-10-27 下午15:17:36
 */
public class FieldMessageCommand extends Command {

	private static final long serialVersionUID = -487138627461366106L;
	
	private FieldProfilerInfo message = null;
	
	public FieldMessageCommand(FieldProfilerInfo message) {
		super(Command.FIELD_MESSAGE);
		this.message = message;
	}
	
	public FieldMessageCommand() {
		super(Command.FIELD_MESSAGE);	
	}	

	//改变访问权限
	@Override
	public void write(ObjectOutput out) throws IOException {
		out.writeObject(message);
	}

	@Override
	public void read(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.message = (FieldProfilerInfo)in.readObject();
	}

	public FieldProfilerInfo getMessage() {
		return message;
	}

}
