package com.taobao.www.util;

public class HibernateDaoSupport {

	public HibernateDaoSupport()
	{
	}
    public HibernateTemplate getHibernateTemplate()
    {
    	return new HibernateTemplate();
    }
}
