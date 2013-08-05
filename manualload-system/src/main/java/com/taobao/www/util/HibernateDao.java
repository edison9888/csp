package com.taobao.www.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.taobao.www.entity.AppConfig;

// import com.cn.hibernate.SessionFactory;

/**
 * @author xiongying
 * @Date 2006-02-25
 * @declare 关于HIBERNATE的相关方法的封装
 */
public class HibernateDao extends HibernateDaoSupport {
	
	private static final Logger logger = Logger.getLogger(HibernateDao.class);
	
	List list = new ArrayList();

	/**
	 * @param entityName
	 * @param id
	 * @return
	 */
	public Object get(Class entityName, Serializable id) {
		logger.info("get(Class entityName, int id) - start");
		Session session = null;
		Transaction tx = null;
		try {
			session = Hibernate_Util.getSession();// 最好是工具生成的SessionFactory，自己写比较麻烦，想我这种菜鸟还会出错
			tx = session.beginTransaction();
			Object object = session.get(entityName, id);
			tx.commit();

			logger.info("get(Class entityName, int id) - end");
			return object;
		} catch (Exception e) {
			logger.info("get(Class entityName, int id) throw exception = ", e);
			try {
				if (tx != null)
					tx.rollback();
			} catch (HibernateException ex) {
				throw new RuntimeException("tx.rollbacd() throw exception = "
						+ ex.toString());
			}
			throw new RuntimeException(
					"get(Class entityName, int id) throw exception = "
							+ e.toString());
		} finally {
			closeSession(session);
		}
	}

	/**
	 * @param object
	 * @return
	 */
	public boolean delete(Object object) {
		Session session = null;
		Transaction tx = null;
		logger.info("delete(Object object) - start");
		try {
			session = Hibernate_Util.getSession();
			tx = session.beginTransaction();
			session.delete(object);
			tx.commit();
			logger.info("delete(Object object) - end");
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			logger.info("delete(Object object) throw exception = ", e);
			return false;
		} finally {
			closeSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	public AppConfig getOneObjectById(int id) {
		Session session = null;
		String hql = "from AppConfig s where s.id=:appId";
		logger.info("delete(Object object) - start");
		AppConfig student = null;
		try {
			session = Hibernate_Util.getSession();
			List list = session.createQuery(hql).setParameter("appId", id)
					.list();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				student = (AppConfig) iterator.next();
			}
		} catch (Exception e) {
			logger.info(" get one object throw exception = ", e);
		} finally {
			closeSession(session);
		}
		return student;
	}

	public boolean deleteOneObjectById(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean result = false;
		String hql = "DELETE FROM AppConfig  WHERE id=:appId ";
		logger.info("delete(Object object) - start");
		try {
			session = Hibernate_Util.getSession();
			transaction = session.beginTransaction();
			Query q = session.createQuery(hql);
			q.setParameter("appId", id);
			q.executeUpdate();
			transaction.commit();
			result = true;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			logger.info("delete(Object object) throw exception = ", e);
		} finally {
			closeSession(session);
		}
		return result;
	}
	
	public boolean deleteOneAppMachinById(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean result = false;
		String hql = "DELETE FROM AppMachine  WHERE id=:appId ";
		logger.info("delete(Object object) - start");
		try {
			session = Hibernate_Util.getSession();
			transaction = session.beginTransaction();
			Query q = session.createQuery(hql);
			q.setParameter("appId", id);
			q.executeUpdate();
			transaction.commit();
			result = true;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			logger.info("delete(Object object) throw exception = ", e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	public boolean deleteMachineObjectByAppId(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean result = false;
		String hql = "DELETE FROM AppMachine  WHERE id=" + id;
		logger.info("delete(Object object) - start");
		try {
			session = Hibernate_Util.getSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.executeUpdate();
			transaction.commit();
			result = true;
		} catch (Exception e) {
			if(transaction != null){
				transaction.rollback();
			}
			logger.info("delete(Object object) throw exception = ", e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	private void closeSession(Session session) {
		try {
			if (session != null)
				Hibernate_Util.closeSession();
		} catch (HibernateException e) {
			logger.info("Hibernate_Util.closeSession() throw exception = "
					+ e.toString());
		}
	}

	/**
	 * @param object
	 * @return
	 * @throws HibernateException
	 */
	public boolean save(Object object) {
		Session session = null;
		Transaction tx = null;
		boolean result = false;
		logger.info("save(Object object) - start");
		try {
			session = Hibernate_Util.getSession();
			tx = session.beginTransaction();
			session.save(object);
			tx.commit();
			result = true;
		} catch (Exception e) {
			logger.info("save(Object object) throw exception = ", e);
			try {
				if (tx != null)
					tx.rollback();
			} catch (HibernateException ex) {
				logger.info("tx.rollbacd() throw exception = ", e);
			}
		} finally {
			closeSession(session);
		}
		return result;
	}

	/**
	 * @param object
	 * @return
	 * @throws HibernateException
	 */
	public boolean update(Object object) {
		Session session = null;
		Transaction tx = null;
		boolean result = false;
		logger.info("update(Object object) - start");
		try {
			session = Hibernate_Util.getSession();
			tx = session.beginTransaction();
			session.update(object);
			tx.commit();
			session.clear();
			result = true;
		} catch (Exception e) {
			logger.info("update(Object object) throw exception = ", e);
			try {
				if (tx != null)
					tx.rollback();
			} catch (HibernateException ex) {
				logger.info("tx.rollbacd() throw exception = ", e);
			}
		} finally {
			closeSession(session);
		}
		return result;
	}

	/**
	 * @param object
	 * @return
	 * @throws HibernateException
	 */
	public boolean saveOrUpdate(Object object) {
		Session session = null;
		Transaction tx = null;
		logger.info("saveOrUpdate(Object) - start");
		try {
			session = Hibernate_Util.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(object);
			tx.commit();

			logger.info("saveOrUpdate(Object) - end");
			return true;
		} catch (Exception e) {
			logger.info("saveOrUpdate(Object object) throw exception = ", e);
			try {
				if (tx != null)
					tx.rollback();
			} catch (HibernateException ex) {
				throw new RuntimeException("tx.rollbacd() throw exception = "
						+ ex.toString());
			}
			throw new RuntimeException(
					"saveOrUpdate(Object) throw exception = " + e.toString());
		} finally {
			closeSession(session);
		}
	}

	/**
	 * @param sql
	 * @param parameter
	 * @return
	 * @throws HibernateException
	 */
	public boolean update(String sql, Object[] parameter) {
		Session session = null;
		Transaction tx = null;
		logger.info("update(String sql, Object[] parameter) - start");
		try {
			session = Hibernate_Util.getSession();
			tx = session.beginTransaction();
			Query query = session.createQuery(sql);
			setQueryParameterValues(query, parameter);
			query.executeUpdate();
			tx.commit();

			logger.info("update(String sql, Object[] parameter) - end");
			return true;
		} catch (Exception e) {
			logger
					.error(
							"update(String sql, Object[] parameter) throw exception = ",
							e);
			try {
				if (tx != null)
					tx.rollback();
			} catch (HibernateException ex) {
				throw new RuntimeException("tx.rollbacd() throw exception = "
						+ ex.toString());
			}
			throw new RuntimeException(
					"update(String sql, Object[] parameter) throw exception = "
							+ e.toString());
		} finally {
			closeSession(session);
		}
	}

	/**
	 * @param sql
	 * @return
	 */
	public List find(String sql) {
		logger.info("find(String, String, int) - start");
		List list = new ArrayList();
		Session session = null;
		try {
			session = Hibernate_Util.getSession();
			Query query = session.createQuery(sql);
			list = query.list();
			logger.info("find(String, String, int) - end");
		} catch (Exception e) {
			logger.info("find(String, String, int) throw exception = ", e);
		} finally {
			closeSession(session);
		}
		return list;
	}

	/**
	 * 位置参数查询
	 * 
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public List find(String sql, Object[] parameter) {
		Session session = null;
		List list = new ArrayList();
		logger.info("find(String sql, Object[] object) - start");
		try {
			session = Hibernate_Util.getSession();
			Query query = session.createQuery(sql);
			setQueryParameterValues(query, parameter);
			list = query.list();
			logger.info("find(String sql, Object[] object) - end");
		} catch (Exception e) {
			logger.info(
					"find(String sql, Object[] object) throw exception = ", e);
		} finally {
			closeSession(session);
		}
		return list;
	}

	/**
	 * 命名参数查询
	 * 
	 * @param sql
	 * @param name
	 * @param parameter
	 * @return
	 */
	public List find(String sql, String[] name, Object[] parameter) {
		Session session = null;
		List list = new ArrayList();
		logger.info("find(String sql, String[] name, Object[] object) - start");
		try {
			session = Hibernate_Util.getSession();
			Query query = session.createQuery(sql);
			setQueryParameterValues(query, name, parameter);
			list = query.list();
			logger
					.info("find(String sql, String[] name, Object[] object) - end");

		} catch (Exception e) {
			logger
					.error(
							"find(String sql, String[] name, Object[] object) throw exception = ",
							e);
		} finally {
			closeSession(session);
		}
		return list;
	}

	/**
	 * @param sql
	 * @param firstSize
	 * @param pageSize
	 * @return
	 */
	public List findPage(String sql, int firstSize, int pageSize) {
		Session session = null;
		List list = new ArrayList();
		try {
			logger
					.info("findPage(String sql, int firstSize, int pageSize) - start");
			session = Hibernate_Util.getSession();
			Query query = session.createQuery(sql);
			query.setFirstResult(firstSize);
			query.setMaxResults(pageSize);
			list = query.list();
			logger
					.info("findPage(String sql, int firstSize, int pageSize)) - end");
		} catch (Exception e) {
			logger
					.error(
							"findPage(String sql, int firstSize, int pageSize) throw exception = ",
							e);
		} finally {
			closeSession(session);
		}
		return list;
	}

	/**
	 * 位置参数 分页查询
	 * 
	 * @param sql
	 * @param parameter
	 * @param firstSize
	 * @param pageSize
	 * @return
	 */
	public List findPage(String sql, Object[] parameter, int firstSize,
			int pageSize) {
		List list = new ArrayList();
		Session session = null;
		logger
				.info("findPage(String sql, Object[] object, int firstSize, int maxSize) - start");
		try {
			session = Hibernate_Util.getSession();
			Query query = session.createQuery(sql);
			setQueryParameterValues(query, parameter);
			query.setFirstResult(firstSize);
			query.setMaxResults(pageSize);
			list = query.list();
			logger
					.info("findPage(String sql, Object[] object, int firstSize, int maxSize) - end");
		} catch (Exception e) {
			logger
					.error(
							"findPage(String sql, Object[] object, int firstSize, int maxSize) throw exception = ",
							e);
		} finally {
			closeSession(session);
		}
		return list;
	}

	/**
	 * 命名参数 分页查询
	 * 
	 * @param sql
	 * @param name
	 * @param parameter
	 * @param firstSize
	 * @param pageSize
	 * @return
	 */
	public List findPage(String sql, String[] name, Object[] parameter,
			int firstSize, int pageSize) {
		Session session = null;
		Transaction tx = null;
		logger
				.info("findPage(String sql, String[] name , Object[] object, int firstSize, int maxSize) - start");
		try {
			session = Hibernate_Util.getSession();
			tx = session.beginTransaction();
			Query query = session.createQuery(sql);
			setQueryParameterValues(query, name, parameter);
			query.setFirstResult(firstSize);
			query.setMaxResults(pageSize);
			list = query.list();
			tx.commit();

			logger
					.info("findPage(String sql, String[] name , Object[] object, int firstSize, int maxSize) - end");
			return list;
		} catch (Exception e) {
			logger
					.error(
							"findPage(String sql, String[] name , Object[] object, int firstSize, int maxSize) throw exception = ",
							e);
			try {
				if (tx != null)
					tx.rollback();
			} catch (HibernateException ex) {
				throw new RuntimeException("tx.rollbacd() throw exception = "
						+ ex.toString());
			}
			throw new RuntimeException(
					"findPage(String sql, String[] name , Object[] object, int firstSize, int maxSize) throw exception = "
							+ e.toString());
		} finally {
			closeSession(session);
		}
	}

	 
	/**
	 * 位置参数设置
	 * 
	 * @param query
	 * @param parameter
	 */
	public void setQueryParameterValues(Query query, Object[] parameter) {
		logger
				.info("setQueryParameterValues(Query query, Object[] object) - start");
		try {
			if (parameter != null) {
				for (int i = 0; i < parameter.length; i++)
					query.setParameter(i, parameter[i]);
			}
			logger
					.info("setQueryParameterValues(Query query, Object[] object) - end");
		} catch (HibernateException e) {
			logger
					.error(
							"setQueryParameterValues(Query query, Object[] object) throw exception = ",
							e);
			throw new RuntimeException(
					"setQueryParameterValues(Query query, Object[] object) throw exception = "
							+ e.toString());
		}
	}

	/**
	 * 命名参数设置
	 * 
	 * @param query
	 * @param name
	 * @param parameter
	 */
	public void setQueryParameterValues(Query query, String[] name,
			Object[] parameter) {
		logger
				.info("setQueryParameterValues(Query query, String[] str , Object[] object) - start");
		try {
			if (name.length != parameter.length) {
				throw new IllegalArgumentException(
						"setQueryParameterValues(Query query, String[] name , Object[] parameter) = Length of paramNames array must match length of values array");
			}
			if (name != null && parameter != null) {
				for (int i = 0; i < name.length; i++)
					query.setParameter(name[i], parameter[i]);
			}
			logger
					.info("setQueryParameterValues(Query query, String[] str , Object[] object) - end");
		} catch (HibernateException e) {
			logger
					.error(
							"setQueryParameterValues(Query query, String[] str , Object[] object) throw exception = ",
							e);
			throw new RuntimeException(
					"setQueryParameterValues(Query query, String[] str , Object[] object) throw exception = "
							+ e.toString());
		}
	}

	/**
	 * @param parameter
	 * @param query
	 * @throws HibernateException
	 */
	public void setQueryParameter(Query query, Object[] parameter) {
		logger.info("setQueryParameter(Query query, Object[] object) - start");
		Object pValue = null;
		try {
			if (parameter != null) {
				for (int i = 0; i < parameter.length; i++) {
					pValue = parameter[i];
					if (pValue instanceof String) {
						query.setString(i, (String) pValue);
					} else if (pValue instanceof Integer) {
						query.setInteger(i, ((Integer) pValue).intValue());
					} else if (pValue instanceof Boolean) {
						query.setBoolean(i, ((Boolean) pValue).booleanValue());
					} else if (pValue instanceof Short) {
						query.setShort(i, ((Short) pValue).shortValue());
					} else if (pValue instanceof Long) {
						query.setLong(i, ((Long) pValue).longValue());
					} else if (pValue instanceof Float) {
						query.setFloat(i, ((Float) pValue).floatValue());
					} else if (pValue instanceof Double) {
						query.setDouble(i, ((Double) pValue).doubleValue());
					} else if (pValue instanceof BigDecimal) {
						query.setBigDecimal(i, (BigDecimal) pValue);
					} else if (pValue instanceof Byte) {
						query.setByte(i, ((Byte) pValue).byteValue());
					} else if (pValue instanceof java.sql.Date) {
						query.setDate(i, java.sql.Date.valueOf(pValue
								.toString()));
					} else if (pValue instanceof java.sql.Time) {
						query.setTime(i, java.sql.Time.valueOf(pValue
								.toString()));
					} else if (pValue instanceof java.sql.Timestamp) {
						query.setTimestamp(i, java.sql.Timestamp.valueOf(pValue
								.toString()));
					} else if (pValue instanceof java.util.Date) {
						query.setDate(i, java.sql.Date.valueOf(pValue
								.toString()));
					} else {
						// query.setObject(i, pValue);
					}
				}
			}
			logger
					.info("setQueryParameter(Query query, Object[] object) - end");
		} catch (HibernateException e) {
			logger.info("setQueryParameter(Query query, Object[] object)", e);
			throw new RuntimeException(
					"setQueryParameter(Query query, Object[] object) throw exception = "
							+ e.toString());
		}
	}
}
