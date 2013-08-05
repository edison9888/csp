
package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.taobao.csp.btrace.core.ProfilerInfo;

/**
 * 
 * @author xiaodu
 * @version 2011-8-17 ÏÂÎç02:47:36
 */
public class MessageCommand extends Command{
	
	private ProfilerInfo message = null;
	
	public MessageCommand(ProfilerInfo message){
		super(Command.MESSAGE);
		this.message = message;
	}

	public MessageCommand() {
		super(Command.MESSAGE);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3552388713665408017L;


	@Override
	public void read(ObjectInput in) throws IOException, ClassNotFoundException {
		this.message = (ProfilerInfo)in.readObject();
	}

	@Override
	public void write(ObjectOutput out) throws IOException {
		out.writeObject(message);
		
	}

	public ProfilerInfo getMessage() {
		return message;
	}

}
