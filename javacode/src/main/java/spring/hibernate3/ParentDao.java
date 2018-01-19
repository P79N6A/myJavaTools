/*
 * Copyright (c) 2013 Sohu. All Rights Reserved
 */
package com.sohu.ccm.heart.dao;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * <p>
 * Description:
 * </p>
 * @author guangchaowu
 * @version 1.0
 * @Date 2016年4月19日
 */
public class ParentDao<T> extends HibernateDaoSupport {
    private final static Log log = LogFactory.getLog(ParentDao.class);

    //@Autowired
    //private HibernateTemplate ht;
    //@Autowired
    //private SessionFactory sessionFactory;

    public ParentDao(){
    }

    public T get(Class entityClass, Serializable id){
        return (T)getHibernateTemplate().get(entityClass, id);
    }

    public Serializable save(T entity){
        return getHibernateTemplate().save(entity);
    }

    public void update(T entity){
        getHibernateTemplate().update(entity);
    }

    public void delete(T entity){
        getHibernateTemplate().delete(entity);
    }

    public HibernateTemplate getHt(){
        return getHibernateTemplate();
    }

//    public Session getSession(){
//        return ht.getSessionFactory().getCurrentSession();
//    }

}

