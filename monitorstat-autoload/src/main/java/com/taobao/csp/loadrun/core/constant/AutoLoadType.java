
package com.taobao.csp.loadrun.core.constant;

import java.io.Serializable;

import com.taobao.csp.loadrun.core.run.ApacheLoadrunTask;
import com.taobao.csp.loadrun.core.run.ApacheProxyLoadrunTask;
import com.taobao.csp.loadrun.core.run.BaseLoadrunTask;
import com.taobao.csp.loadrun.core.run.HsfLoadrunTask;
import com.taobao.csp.loadrun.core.run.HttploadLoadrunTask;
import com.taobao.csp.loadrun.core.run.NginxProxyLoadrunTask;

/***
 * 压测的类型
 * @author youji.zj 2012-06-20
 *
 */
public enum AutoLoadType implements Serializable{
	
	httpLoad(ResultKey.Qps,"用户数",HttploadLoadrunTask.class,16931,16930),
	apache(ResultKey.Tomcat_Pv,"分流量",ApacheLoadrunTask.class,16931,16930),
	hsf(ResultKey.Hsf_pv,"Ip倍数",HsfLoadrunTask.class,27734,27733),
	apacheProxy(ResultKey.Tomcat_Pv,"apache代理",ApacheProxyLoadrunTask.class,16931,16930),
	nginxProxy(ResultKey.Tomcat_Pv,"nginx代理",NginxProxyLoadrunTask.class,16931,16930);
	
	AutoLoadType(ResultKey qps,String desc,Class<? extends BaseLoadrunTask> clazz,int qpskeyId,int restKeyId){
		this.loadClass = clazz;
		this.qpsKey = qps;
		this.loadDesc = desc;
		this.qpsKeyId = qpskeyId;
		this.restKeyId = restKeyId;
	}
	
	/*** qps的key ***/
	public ResultKey qpsKey;
	
	/*** 压测类型对应的压测任务 ***/
	private Class<? extends BaseLoadrunTask> loadClass;

	/*** 压测类型的描述 ***/
	public String loadDesc;
	
	/*** qps的key对应在日报中查询的id ***/
	public int qpsKeyId;
	
	/*** 响应时间的key对应在日报中查询的id ***/
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
