
package com.taobao.csp.loadrun.core.constant;

import java.io.Serializable;

import com.taobao.csp.loadrun.core.run.ApacheLoadrunTask;
import com.taobao.csp.loadrun.core.run.ApacheProxyLoadrunTask;
import com.taobao.csp.loadrun.core.run.BaseLoadrunTask;
import com.taobao.csp.loadrun.core.run.HsfLoadrunTask;
import com.taobao.csp.loadrun.core.run.HttploadLoadrunTask;
import com.taobao.csp.loadrun.core.run.NginxProxyLoadrunTask;

/***
 * ѹ�������
 * @author youji.zj 2012-06-20
 *
 */
public enum AutoLoadType implements Serializable{
	
	httpLoad(ResultKey.Qps,"�û���",HttploadLoadrunTask.class,16931,16930),
	apache(ResultKey.Tomcat_Pv,"������",ApacheLoadrunTask.class,16931,16930),
	hsf(ResultKey.Hsf_pv,"Ip����",HsfLoadrunTask.class,27734,27733),
	apacheProxy(ResultKey.Tomcat_Pv,"apache����",ApacheProxyLoadrunTask.class,16931,16930),
	nginxProxy(ResultKey.Tomcat_Pv,"nginx����",NginxProxyLoadrunTask.class,16931,16930);
	
	AutoLoadType(ResultKey qps,String desc,Class<? extends BaseLoadrunTask> clazz,int qpskeyId,int restKeyId){
		this.loadClass = clazz;
		this.qpsKey = qps;
		this.loadDesc = desc;
		this.qpsKeyId = qpskeyId;
		this.restKeyId = restKeyId;
	}
	
	/*** qps��key ***/
	public ResultKey qpsKey;
	
	/*** ѹ�����Ͷ�Ӧ��ѹ������ ***/
	private Class<? extends BaseLoadrunTask> loadClass;

	/*** ѹ�����͵����� ***/
	public String loadDesc;
	
	/*** qps��key��Ӧ���ձ��в�ѯ��id ***/
	public int qpsKeyId;
	
	/*** ��Ӧʱ���key��Ӧ���ձ��в�ѯ��id ***/
	public int restKeyId;
	
	
	public String getLoadDesc() {
		return loadDesc;
	}


	public Class<? extends BaseLoadrunTask> getLoadClass(){
		return loadClass;
	}

	public ResultKey getQpsKey() {
		return qpsKey;
	}
	
	public int getQpsKeyIdByDay(){
		return qpsKeyId;
	}

}
