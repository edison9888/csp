package com.taobao.csp.time.custom.ao;

import java.util.List;

import com.taobao.csp.time.custom.dao.ViewDao;
import com.taobao.csp.time.web.po.UserCustomNaviViewPo;

public class ViewAo {
	private static ViewAo viewAo = new ViewAo();
	private static ViewDao viewDao = new ViewDao();
	private ViewAo(){
	}
	public static ViewAo get(){
		return viewAo;
	}
	public boolean findViewUrl(final UserCustomNaviViewPo view){
		return viewDao.findViewUrl(view);
	}
	public boolean insertViewUrl(List<UserCustomNaviViewPo> list){
		return viewDao.insertViewUrl(list);
	}
	public boolean deleteViewUrl(List<String> viewMods){
		return viewDao.deleteViewUrl(viewMods);
	}
}
