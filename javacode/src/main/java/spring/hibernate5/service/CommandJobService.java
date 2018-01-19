/*
 * Copyright (c) 2013 Sohu. All Rights Reserved
 */
package com.sohu.ccm.heart.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sohu.ccm.heart.model.CommandJob;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author guangchaowu
 * @version 1.0
 * @Date 2016年4月25日
 */
@Service
@Transactional
public class CommandJobService {
    private final static Log log = LogFactory.getLog(CommandJobService.class);

    @Autowired
    private CommandJobDao commandJobDao;

    public CommandJob get(Serializable id){
        return commandJobDao.get(CommandJob.class, id);
    }

    public List<CommandJob> getCommandJob(long jsetid) {
        String hql = "from CommandJob where jsetid = " + jsetid;
        List<CommandJob> list = commandJobDao.find(hql);
        return list;
    }

    public void update(CommandJob entity){
        entity.setUpdatedTime(new Date());
        commandJobDao.update(entity);
    }

    public List<CommandJob> getPages() {
        String hql = "from CommandJob order by createdTime";
        List<CommandJob> list = commandJobDao.find(hql);
        return list;
    }

    public List<CommandJob> getByJSet(long jsetid) {
        String hql = "from CommandJob where jsetid = " + jsetid + " order by createdTime";
        List<CommandJob> list = commandJobDao.find(hql);
        return list;
    }

    public CommandJob getByMsgid(String msgid) {
        String hql = "from CommandJob where msgid = '" + msgid + "'";
        return commandJobDao.get(hql);
    }

    public void updateStatus(final long jobid, final int status){
        log.info(jobid + ",status:" + status);
//        Query query = getSession().createQuery("update CommandJob set status=" + status + " where id=" + jobid);
//        return query.executeUpdate();
        String hql = "update CommandJob set status=:status, updatedTime=:updatedTime  where id=:jobid";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        params.put("updatedTime", new Date());
        params.put("jobid", jobid);
        commandJobDao.executeHql(hql, params);

//        getHt().execute(new HibernateCallback<Integer>() {
//
//            @Override
//            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
//                Query query = session.createQuery();
//                query.setInteger("status", status);
//                query.setTimestamp("updatedTime", new Date());
//                query.setLong("jobid", jobid);
//                return query.executeUpdate();
//            }
//        });
    }

    public void updateStatusWithStartTime(final long jobid, final int status){
        log.info(jobid + ",status:" + status);

        String hql = "update CommandJob set status=:status, startTime=:startTime where id=:jobid";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        params.put("startTime", new Date());
        params.put("jobid", jobid);
        commandJobDao.executeHql(hql, params);

//        getHt().execute(new HibernateCallback<Integer>() {
//
//            @Override
//            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
//                Query query = session.createQuery();
//                query.setInteger("status", status);
//                query.setTimestamp("startTime", new Date());
//                query.setLong("jobid", jobid);
//                return query.executeUpdate();
//            }
//        });
    }

    public void updateStatusWithFinishTime(final long jobid, final int status){
        log.info(jobid + ",status:" + status);

        String hql = "update CommandJob set status=:status, finishTime=:finishTime where id=:jobid";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        params.put("finishTime", new Date());
        params.put("jobid", jobid);
        commandJobDao.executeHql(hql, params);

//        getHt().execute(new HibernateCallback<Integer>() {
//
//            @Override
//            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
//                Query query = session.createQuery();
//                query.setInteger("status", status);
//                query.setTimestamp("finishTime", new Date());
//                query.setLong("jobid", jobid);
//                return query.executeUpdate();
//            }
//        });
    }

    public Integer updateMsgid(final long jobid, final String msgid){

        String hql = "update CommandJob set msgid='" + msgid + "' where id=" + jobid;
        return commandJobDao.executeHql(hql);

//        return getHt().execute(new HibernateCallback<Integer>() {
//
//            @Override
//            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
//                Query query = session.createQuery();
//                return query.executeUpdate();
//            }
//        });
    }

}
