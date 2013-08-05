
package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * @author xiaodu
 * @version 2011-8-25 обнГ05:09:17
 */
public class ClientCommand extends Command{
	
	

	protected ClientCommand(int type) {
		super(type);
	}


	protected void read(ObjectInput in) throws IOException, ClassNotFoundException {
		
	}


	@Override
	protected void write(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
