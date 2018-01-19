/*
 * Copyright (c) 2013 uhos. All Rights Reserved
 */
package spring.hibernate5;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDaoImpl<T> implements BaseDao<T> {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    /**
     * 获得当前事物的session
     *
     * @return org.hibernate.Session
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession(); // hibernate 3.x版本后摒弃该方法
        // 从会话工厂获取一个session
        //return sessionFactory.openSession();
    }

    private void closeSession(Session s) {

        // s = this.getCurrentSession();
        if (s != null) {
            //s.close();
        }
    }

    @Override
    public Serializable save(T o) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            if (o != null) {
                Serializable save = s.save(o);
                return save;
            }
            return null;
        } finally {
            closeSession(s);
        }
    }

    @Override
    public T get(Class<T> c, Serializable id) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            return s.get(c, id);
        } finally {
            closeSession(s);
        }
    }

    @Override
    public T get(String hql) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            List<T> l = q.list();
            if (l != null && l.size() > 0) {
                return l.get(0);
            }
            return null;
        } finally {
            closeSession(s);
        }
    }

    @Override
    public T get(String hql, Map<String, Object> params) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            List<T> l = q.list();
            if (l != null && l.size() > 0) {
                return l.get(0);
            }
            return null;
        } finally {
            closeSession(s);
        }
    }

    @Override
    public void delete(T o) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            if (o != null) {
                s.delete(o);
            }
        } finally {
            closeSession(s);
        }
    }

    @Override
    public void update(T o) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            if (o != null) {
                s.update(o);
            }
        } finally {
            closeSession(s);
        }
    }

    @Override
    public void saveOrUpdate(T o) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            if (o != null) {
                s.saveOrUpdate(o);
            }
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List<T> find(String hql) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            return q.list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List find2(String hql) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            return q.list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List<T> find(String hql, Map<String, Object> params) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            return q.list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List<T> find(String hql, int page, int rows) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public Long count(String hql) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            return (Long) q.uniqueResult();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public Long count(String hql, Map<String, Object> params) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            return (Long) q.uniqueResult();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public int executeHql(String hql) {
        Session s = null;
        Transaction t = null;
        try {
            s = this.getCurrentSession();

//            boolean ac;
//            try {
//                Connection c = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
//                ac = c.getAutoCommit();
//                System.out.println("autoCommit:" + ac);
//                c.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            //t = s.beginTransaction();
            Query q = s.createQuery(hql);
            System.out.println("aaaaabbbbb" + s);
//            try {
//                ac = SessionFactoryUtils.getDataSource(sessionFactory).getConnection().getAutoCommit();
//                System.out.println("autoCommit:" + ac);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
             int rs = q.executeUpdate();
             s.flush();
             return rs;
        } finally {
            System.out.println("aaaaabbbbb2" + s);
            boolean ac;
//            try {
//                ac = SessionFactoryUtils.getDataSource(sessionFactory).getConnection().getAutoCommit();
//                System.out.println("autoCommit:" + ac);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            if(t!=null){
                t.commit();
            }
//            try {
//                ac = SessionFactoryUtils.getDataSource(sessionFactory).getConnection().getAutoCommit();
//                System.out.println("autoCommit:" + ac);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            System.out.println("aaaaabbbbb3" + s);
            closeSession(s);
            System.out.println("aaaaabbbbb4" + s);
        }
    }

    @Override
    public int executeHql(String hql, Map<String, Object> params) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            Query q = s.createQuery(hql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            return q.executeUpdate();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List<Object[]> findBySql(String sql) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            SQLQuery q = s.createSQLQuery(sql);
            return q.list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List<Object[]> findBySql(String sql, int page, int rows) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            SQLQuery q = s.createSQLQuery(sql);
            return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List<Object[]> findBySql(String sql, Map<String, Object> params) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            SQLQuery q = s.createSQLQuery(sql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            return q.list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public List<Object[]> findBySql(String sql, Map<String, Object> params, int page, int rows) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            SQLQuery q = s.createSQLQuery(sql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public int executeSql(String sql) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            SQLQuery q = s.createSQLQuery(sql);
            return q.executeUpdate();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public int executeSql(String sql, Map<String, Object> params) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            SQLQuery q = s.createSQLQuery(sql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            return q.executeUpdate();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public BigInteger countBySql(String sql) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            SQLQuery q = s.createSQLQuery(sql);
            return (BigInteger) q.uniqueResult();
        } finally {
            closeSession(s);
        }
    }

    @Override
    public BigInteger countBySql(String sql, Map<String, Object> params) {
        Session s = null;
        try {
            s = this.getCurrentSession();
            SQLQuery q = s.createSQLQuery(sql);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key, params.get(key));
                }
            }
            return (BigInteger) q.uniqueResult();
        } finally {
            closeSession(s);
        }
    }

    /**
     * 批量保存数据
     *
     * @param <T>
     * @param entitys
     *            要持久化的临时实体对象集合
     */
    @Override
    public void batchSave(List<T> entitys) throws SQLException {
        Session s = null;
        try {
            s = this.getCurrentSession();
            for (int i = 0; i < entitys.size(); i++) {
                s.save(entitys.get(i));
                if (i % 20 == 0) {
                    // 20个对象后才清理缓存，写入数据库
                    s.flush();
                    s.clear();
                }
            }
            // 最后清理一下----防止大于20小于40的不保存
            s.flush();
            s.clear();
        } finally {
            closeSession(s);
        }
    }

}
