
package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * @author xiaodu
 * @version 2011-8-17 обнГ02:47:36
 */
public class ExitCommand extends Command{

	protected ExitCommand() {
		super(Command.EXIT);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3552388713665408017L;


	@Override
	protected void read(ObjectInput in) throws IOException, ClassNotFoundException {
		
	}

	@Override
	protected void write(ObjectOutput out) throws IOException {
		
	}

	

}
