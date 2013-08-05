package com.taobao.csp.dataserver.packet;

import java.io.Serializable;

public  class ResponsePacket implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public long requestId;
	
	private Object content;
	
	public ResponsePacket(long requestId){
		this.requestId = requestId;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}


	

}
