package com.taobao.www.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Hibernate_Util
{
	//hibernate配置文件的名称
	private static final String configFile="hibernate.cfg.xml";
	//hibernate配置对象
	private static final Configuration conf=new Configuration();
	//SessionFactory
	private static SessionFactory f=null;
	//Session
	private static Session session=null;
	
	//ThreadLocal(只能存放一个对象,让多线程同步访问该对象)
	private static final ThreadLocal<Session> sessionThread=new ThreadLocal();
   
	//ThreadLocal(只能存放一个对象,让多线程同步访问该对象)
	private static final ThreadLocal<Transaction> transactionThread=new ThreadLocal();
	
	//对该类对象方法调用进行日志记录
	private static final Log log=LogFactory.getLog(Hibernate_Util.class);
	
	static{
		conf.configure();
		f=conf.buildSessionFactory();
	}
	
	public Hibernate_Util()
	{
	}
	
	public synchronized static Session getSession() throws Exception
	{
	   session=sessionThread.get();	
	   try{
		 if(session==null)
		 {
		   if(f==null)
		   {
			 f=conf.buildSessionFactory();  
		   }
		   session=f.openSession();
		   //创建对象存放于ThreadLocal
		   sessionThread.set(session);
		   log.info("session创建成功!");
		 }
		 return session;
	   }catch(Exception e){
		  e.printStackTrace(); 
		  log.error("session创建失败！");
		  throw e;
	   }
	}
	public synchronized static void closeSession() 
	{
	   session=sessionThread.get();	
	   try{
		 if(session!=null)
		 {
		   session.close();
		   sessionThread.set(null);
		   log.info("session关闭成功!");
		 }
	   }catch(Exception e){
		  e.printStackTrace(); 
		  log.error("session创建失败！");
	   }
	}
	public synchronized static void startTransaction() 
	{
	   Transaction tran=transactionThread.get();	
	   try{
		 if(tran==null)
		 {
		    session=getSession();
		    tran=session.beginTransaction();
		    transactionThread.set(tran);
		    log.info("事务创建成功！");
		 }
	   }catch(Exception e){
		 e.printStackTrace();
		 log.error("事务创建失败");
	   }
	}
	public static void commitTransaction() 
	{
	   Transaction tran=transactionThread.get();	
	   try{
		 if(tran!=null&&!tran.wasCommitted()&&!tran.wasRolledBack())
		 {
		    tran.commit();
		    transactionThread.set(null);
		    log.info("事务提交成功！");
		 }
	   }catch(Exception e){
		 e.printStackTrace();
		 log.error("事务提交失败");
	   }
	}
	public static void rollbackTransaction() 
	{
	   Transaction tran=transactionThread.get();	
	   try{
		 if(tran!=null&&!tran.wasCommitted()&&!tran.wasRolledBack())
		 {
		    tran.rollback();
		    transactionThread.set(null);
		    log.info("事务回滚成功！");
		 }
	   }catch(Exception e){
		 e.printStackTrace();
		 log.error("事务回滚失败");
	   }
	}
	
}
