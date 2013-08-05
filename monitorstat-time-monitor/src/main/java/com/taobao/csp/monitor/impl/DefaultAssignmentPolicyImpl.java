
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import java.util.List;

import com.taobao.csp.monitor.AssignmentPolicy;
import com.taobao.csp.monitor.JobInfo;

/**
 * @author xiaodu
 *
 * 下午4:11:45
 */
public class DefaultAssignmentPolicyImpl implements AssignmentPolicy{
	
	private String group;
	public DefaultAssignmentPolicyImpl(String group){
		this.group = group;
	}

	@Override
	public String assign(JobInfo job, List<String> executors) {
		
		if("CMX".equals(group.toUpperCase())){
			if(!"CM3".equals(job.getSite().toUpperCase())&&!"CM4".equals(job.getSite().toUpperCase())&&!"CM5".equals(job.getSite().toUpperCase())){
				int len = executors.size();
				long hash = hash(job.getJobID().getBytes());
				int index = (int)(hash%len);
				
				String eName = executors.get(Math.abs(index));//获取到 需要执行者的名称
				return eName;
			}
		}else{
			if(group.toUpperCase().equals(job.getSite().toUpperCase())){
				
				int len = executors.size();
				long hash = hash(job.getJobID().getBytes());
				int index = (int)(hash%len);
				
				String eName = executors.get(Math.abs(index));//获取到 需要执行者的名称
				return eName;
			}
		}
		return null;
	}
	
	
	private static final int MURMURHASH_M = 0x5bd1e995;
	
	 public static long hash(byte[] key) {
		    int len = key.length;
		    int h = 97 ^ len;
		    int index = 0;

		    while (len >= 4) {
		      int k = (key[index] & 0xff) | ((key[index + 1] << 8) & 0xff00)
		        | ((key[index + 2] << 16) & 0xff0000)
		        | (key[index + 3] << 24);

		      k *= MURMURHASH_M;
		      k ^= (k >>> 24);
		      k *= MURMURHASH_M;
		      h *= MURMURHASH_M;
		      h ^= k;
		      index += 4;
		      len -= 4;
		    }

		    switch (len) {
		      case 3:
		        h ^= (key[index + 2] << 16);

		      case 2:
		        h ^= (key[index + 1] << 8);

		      case 1:
		        h ^= key[index];
		        h *= MURMURHASH_M;
		    }

		    h ^= (h >>> 13);
		    h *= MURMURHASH_M;
		    h ^= (h >>> 15);
		    return ((long) h & 0xffffffffL);
		  }

}
