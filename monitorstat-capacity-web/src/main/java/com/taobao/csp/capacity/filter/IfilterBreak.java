package com.taobao.csp.capacity.filter;

import java.util.List;

import com.taobao.csp.capacity.po.PvcountPo;

public interface IfilterBreak {

	//���˷���
	public List<PvcountPo> filter(List<PvcountPo> l);
}
