package com.taobao.www.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;

public class HibernateTemplate
{
	public HibernateTemplate()
	{
	}
    public Session getSession() 
    {
      try{
		  return Hibernate_Util.getSession();
	   }catch (Exception e){
		 e.printStackTrace();
		 return null;
	   }	
    }
    public void closeSession() 
    {
      try{
		Hibernate_Util.closeSession();
	  }catch (Exception e) {
		e.printStackTrace();
	  }	
    }
    public void startTransaction() 
    {
       try{
		Hibernate_Util.startTransaction();
	   } catch (Exception e) {
		e.printStackTrace();
	   }	
    }
    public void commitTransaction()
    {
      try{
		Hibernate_Util.commitTransaction();
	  }catch (Exception e) {
		e.printStackTrace();
	  }	
    }
    public void rollbackTransaction()
    {
      try{
		Hibernate_Util.rollbackTransaction();
	  }catch (Exception e) {
		e.printStackTrace();
	  }	
    }
    //增(业务逻辑方法)
    public synchronized Serializable save(Object obj) throws Exception
    {
      Serializable id=null;  	
      try{
		Session session=this.getSession();
		id=session.save(obj);
	   }catch(Exception e){
	     e.printStackTrace();
	     throw e;
	   }
       return id;
    }
    //删除(业务逻辑方法)
    public synchronized void delete(Object obj) throws Exception
    {
		Session session=this.getSession();
		session.delete(obj);
    }
    public synchronized void deleteOrUpdate(String hql) throws Exception
    {
		Session session=this.getSession();
		Query q=session.createQuery(hql);
		q.executeUpdate();
    }
    //改
    public synchronized void update(Object obj) throws Exception
    {
		Session session=this.getSession();
		session.update(obj);
    }
    public synchronized void saveOrUpdate(Object obj) 
    {
		Session session=this.getSession();
		session.saveOrUpdate(obj);
    }
    //自由态（游离态）-->持久态
    public synchronized void merge(Object obj) throws Exception
    {
		Session session=this.getSession();
		session.merge(obj);
    }
    //得到一个对象
    public  synchronized Object get(Class c,Serializable id) 
    {
      Object obj=null;
      
		Session session=this.getSession();
		obj=session.get(c,id);
	   
	   return obj;
    }
    //得到一个对象(装载一个对象，比get效率高)
    public synchronized Object load(Class c,Serializable id) throws Exception
    {
      Object obj=null;
      try{
		Session session=this.getSession();
		obj=session.load(c,id);
	   }catch(Exception e){
	     e.printStackTrace();
	     throw e;
	   }
	   return obj;
    }
    //" ... where id=? and name=?"
    public synchronized List findObjects(String hql,Object... params) 
    {
      Session session=this.getSession();	
      Query q=session.createQuery(hql);
      if(params!=null&&params.length>0)
      {
    	  int i=0;
    	  for(Object obj:params)
    	  {
    		q.setString(i, obj.toString()); 
    		i++;
    	  }
      }
      return q.list();
    }
    //分页("select new org.itfuture")
    public synchronized List findObjects(String hql,int curpage,int pagerecord,Object... params) 
    {
      Session session=this.getSession();	
      Query q=session.createQuery(hql);
      if(params!=null&&params.length>0)
      {
    	  int i=0;
    	  for(Object obj:params)
    	  {
    		q.setString(i, obj.toString()); 
    		i++;
    	  }
      }
      q.setFirstResult((curpage-1)*pagerecord);
      q.setMaxResults(pagerecord);
      return q.list();
    }
    //分页("from Employee A,Dept B where A.dept.deptid=B.deptid")
    public synchronized List findObjects(String hql,int curpage,int pagerecord,Class voclass,Object... params) throws Exception
    {
      Session session=this.getSession();	
      Query q=session.createQuery(hql);
      if(params!=null&&params.length>0)
      {
    	  int i=0;
    	  for(Object obj:params)
    	  {
    		q.setString(i, obj.toString()); 
    		i++;
    	  }
      }
      q.setFirstResult((curpage-1)*pagerecord);
      q.setMaxResults(pagerecord);
      List<Object[]> list=q.list();
      
      List vos=new ArrayList();//盛放vo对象
      
      for(Object[] objs:list)
      {
    	 Object vo=voclass.newInstance(); 
    	 //copy po->vo
    	 for(Object po:objs)
    	 {
    		BeanUtils.copyProperties(vo,po); 
    	 }
    	 vos.add(vo);
      }
      return vos;
    }
    //聚合数("select max/count(A.deptnum) from Dept A")
    public synchronized Object getSerializable(String hql,Object... params) 
    {
    	Session session=this.getSession();
    	Query q=session.createQuery(hql);
    	if(params!=null&&params.length>0)
        {
        	  int i=0;
        	  for(Object obj:params)
        	  {
        		q.setString(i, obj.toString()); 
        		i++;
        	  }
        } 
    	Object obj=q.uniqueResult();//????
    	return obj;
    }
    public synchronized List findByCriteria(Class c,int curpage,int pagerecord,Criterion... conds)
    {
        Session session=this.getSession();
        Criteria criteria=session.createCriteria(c);
        
        for(Criterion cond:conds)
        {
          criteria.add(cond);	
        }
        criteria.setFirstResult((curpage-1)*pagerecord);
        criteria.setMaxResults(pagerecord);
        return criteria.list();
    }
    public synchronized int findByCriteria(Class c,Criterion... conds)
    {
        Session session=this.getSession();
        Criteria criteria=session.createCriteria(c);
        
        for(Criterion cond:conds)
        {
          criteria.add(cond);	
        }
        //Object obj=criteria.setProjection(Projections.rowCount()).uniqueResult();
        Object obj=criteria.setProjection(Projections.count("deptid")).uniqueResult();
        return Integer.parseInt(obj.toString());
    }
}
