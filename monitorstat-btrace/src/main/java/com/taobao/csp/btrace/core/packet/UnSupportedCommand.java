
package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.taobao.csp.btrace.core.client.TransformerProxy;

/**
 * 
 * @author xiaodu
 * @version 2011-8-17 ÏÂÎç03:10:35
 */
public class UnSupportedCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4947337333850853446L;

	public UnSupportedCommand() {
		super(Command.UNSUPPORTED);
	}

	@Override
	public void read(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
