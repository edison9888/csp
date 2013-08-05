package com.taobao.csp.time.tair;




import java.util.ArrayList;
import java.util.List;


/**
 * 包装从tair中取到的数据
 * @author root
 *
 */
public class TairCacheResult {

	private boolean isSuccess = false;

	private String resultCode;
	
	private Object object;
	
	private final List<Object> objectList = new ArrayList<Object>();

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public List<Object> getObjectList() {
		return objectList;
	}

	@Override
	public String toString() {
		return "ItaoCacheResult [isSuccess=" + isSuccess + ", resultCode=" + resultCode + ", object=" + object
				+ ", objectList=" + objectList + "]";
	}
}
