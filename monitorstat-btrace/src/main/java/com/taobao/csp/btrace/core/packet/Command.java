

package com.taobao.csp.btrace.core.packet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public abstract class Command implements Serializable {

	public static final int TF_CLASS_FLAG = 0x1d4d6f5e;
	
	public static final int EXIT = -1111;
	
	public static final int SUCCESS = 911;
	
	public static final int UNSUPPORTED = -1110;
	
	public static final int TRANSFORMER_CONFIG = 3838;
	
	public static final int TRANSFORMER_CLASS = 8383;
	
	//add by zhongting  用来标记单例的情况
	public static final int SINGLETON_CLASS = 8384;		
	
	public static final int REMOVE = -4;
	
	public static final int MESSAGE = 3333;
	
	public static final int FIELD_MESSAGE = 3334;

	private int commandCode;
	
	private int packetCode;

	public Command(int type) {
		this.commandCode = type;
	}
	
	public void writeMessage(ObjectOutput out) throws Exception{
		out.writeInt(TF_CLASS_FLAG);
		out.writeInt(commandCode);
		write(out);
		out.flush();
	}
	
	protected abstract void write(ObjectOutput out) throws IOException;

	protected abstract void read(ObjectInput in) throws IOException, ClassNotFoundException;

	public int getCommandCode() {
		return commandCode;
	}

	public int getPacketCode() {
		return packetCode;
	}

	
	

	

}
