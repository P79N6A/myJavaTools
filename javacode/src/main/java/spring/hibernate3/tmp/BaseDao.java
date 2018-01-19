/*
 * Copyright (c) 2012 Sohu. All Rights Reserved
 */
package com.sohu.ccm.heart.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * <p>
 * Description: 数据库操作实现类
 * </p>
 *
 * @author solidwang
 * @version 1.0
 * @Date 2012-10-12
 */
public class BaseDao extends HibernateDaoSupport implements Dao {
	private static Logger log = Logger.getLogger(BaseDao.class);

	/** 加载指定ID的持久化对象 */
	public Object loadById(Class<?> clazz, Serializable id) {
		return getHibernateTemplate().load(clazz, id);
	}

	/** 加载指定ID的持久化对象 */
	@Override
    public Object getById(Class<?> clazz, Long id) {
		return getHibernateTemplate().get(clazz, id);
	}

	/** 加载满足条件的持久化对象 */
	@Override
    public Object loadObject(String hql) {
		final String hql1 = hql;
		Object obj = null;
		List<?> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql1);
				return query.list();
			}
		});
		if (list.size() > 0)
			obj = list.get(0);
		return obj;
	}

	/** 装载指定类的所有持久化对象 */
	@Override
    public List<?> listAll(String clazz) {
		return getHibernateTemplate().find("from " + clazz + " as a order by a.id desc");
	}

	/** 查询指定类的满足条件的持久化对象 */
	@Override
    public List<?> query(String hql) {
		final String hql1 = hql;
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
            public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql1);
				return query.list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object execute(String hql) {
        final String hql1 = hql;
        return getHibernateTemplate().execute(new HibernateCallback(){

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql1);
                return query.executeUpdate();
            }

        });
    }

	/** 分页查询指定类的满足条件的持久化对象 */
	@Override
    public List<?> query(String hql, int pageNo, int pageSize) {
		final int pNo = pageNo;
		final int pSize = pageSize;
		final String hql1 = hql;
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
            public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql1);
				query.setMaxResults(pSize);
				query.setFirstResult((pNo - 1) * pSize);
				List<?> result = query.list();
				if (!Hibernate.isInitialized(result))
					Hibernate.initialize(result);
				return result;
			}
		});
	}

	/** 指定参数的分页查询满足条件的持久化对象 */
	@Override
    public List<?> query(String hql, int pageNo, int pageSize, Map<?, ?> map) {
		final int pNo = pageNo;
		final int pSize = pageSize;
		final String hql1 = hql;
		final Map<?, ?> map1 = map;
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
            public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql1);
				Iterator<?> it = map1.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					query.setParameter(key.toString(), map1.get(key));
				}
				query.setMaxResults(pSize);
				query.setFirstResult((pNo - 1) * pSize);
				List<?> result = query.list();
				if (!Hibernate.isInitialized(result))
					Hibernate.initialize(result);
				return result;
			}
		});
	}

	/** 保存或更新指定的持久化对象 */
	@Override
    public boolean saveOrUpdate(Object obj) {
		try {
			getHibernateTemplate().saveOrUpdate(obj);
			return true;
		} catch (DataAccessException e) {
			log.error("Saveorupdate  have an error:" + e);
		}
		return false;
	}

	/** 删除指定ID的持久化对象 */
	@Override
    public boolean delById(Class<?> clazz, Long id) {
		try {
			getHibernateTemplate().delete(getHibernateTemplate().load(clazz, id));
			return true;
		} catch (DataAccessException e) {
			log.error("Delbyid  have an error:" + e);
		}
		return false;
	}
	/**
	 * 批量保存或更新对象
	 */
	@Override
	public boolean saveOrUpdateAll(Collection<Object> collection) {
		try {
			getHibernateTemplate().saveOrUpdateAll(collection);
			return true;
		} catch (DataAccessException e) {
			log.error("Saveorupdateall  have an error:" + e);
		}
		return false;
	}
	/**
	 * 更新指定对象
	 */
	@Override
	public boolean update(Object obj) {
		try {
			getHibernateTemplate().update(obj);
			return true;
		} catch (DataAccessException e) {
			log.error("Update  have an error:" + e);
		}
		return false;
	}
	/**
	 * 保存指定对象
	 */
	@Override
	public Serializable save(Object obj) {
		return getHibernateTemplate().save(obj);
	}
	/**
	 * 删除指定对象
	 */
	@Override
	public boolean delete(Object obj) {
		try {
			getHibernateTemplate().delete(obj);
			return true;
		} catch (Exception e) {
			log.error("delete error", e);
		}
		return false;
	}
	/**
	 * 按照sql来查询
	 */
	@Override
	public List querySql(final String sql) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
            public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(sql);
				return query.list();
			}
		});
	}
	/**
	 * 执行指定sql
	 */
	@Override
	public boolean executeSql(final String sql) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				@Override
                public Object doInHibernate(Session session) throws HibernateException {
					session.createSQLQuery(sql).executeUpdate();
					return null;
				}
			});
			return true;
		} catch (Exception e) {
			log.error("Excute sql have an error:" + e);
		}
		return false;
	}
}
