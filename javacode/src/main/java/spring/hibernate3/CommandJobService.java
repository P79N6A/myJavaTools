/*
 * Copyright (c) 2013 uhos. All Rights Reserved
 */
package spring.hibernate3;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.uhos.ccm.heart.dao.ParentDao;
import com.uhos.ccm.heart.model.CommandJob;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author guangchaowu
 * @version 1.0
 * @Date 2016年4月25日
 */
//@Service
public class CommandJobService extends ParentDao<CommandJob> {
    private final static Log log = LogFactory.getLog(CommandJobService.class);

    public List<CommandJob> getCommandJob(long jsetid) {
        return getHt().find("from CommandJob where jsetid = " + jsetid);
    }

    @Override
    public void update(CommandJob entity){
        entity.setUpdatedTime(new Date());
        getHt().update(entity);
    }

    public List<CommandJob> getPages() {
        List<CommandJob> list = getHt().find("from CommandJob order by createdTime");
        return list;
    }

    public List<CommandJob> getByJSet(long jsetid) {
        List<CommandJob> list = getHt().find("from CommandJob where jsetid = " + jsetid + " order by createdTime");
        return list;
    }

    public CommandJob getByMsgid(String msgid) {
        List<CommandJob> list = getHt().find("from CommandJob where msgid = '" + msgid + "'");
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    public void updateStatus(final long jobid, final int status){
        log.info(jobid + ",status:" + status);
//        Query query = getSession().createQuery("update CommandJob set status=" + status + " where id=" + jobid);
//        return query.executeUpdate();

        getHt().execute(new HibernateCallback<Integer>() {

            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("update CommandJob set status=:status, updatedTime=:updatedTime  where id=:jobid");
                query.setInteger("status", status);
                query.setTimestamp("updatedTime", new Date());
                query.setLong("jobid", jobid);
                return query.executeUpdate();
            }
        });
    }

    public void updateStatusWithStartTime(final long jobid, final int status){
        log.info(jobid + ",status:" + status);
        getHt().execute(new HibernateCallback<Integer>() {

            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("update CommandJob set status=:status, startTime=:startTime where id=:jobid");
                query.setInteger("status", status);
                query.setTimestamp("startTime", new Date());
                query.setLong("jobid", jobid);
                return query.executeUpdate();
            }
        });
    }

    public void updateStatusWithFinishTime(final long jobid, final int status){
        log.info(jobid + ",status:" + status);
        getHt().execute(new HibernateCallback<Integer>() {

            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("update CommandJob set status=:status, finishTime=:finishTime where id=:jobid");
                query.setInteger("status", status);
                query.setTimestamp("finishTime", new Date());
                query.setLong("jobid", jobid);
                return query.executeUpdate();
            }
        });
    }

    public Integer updateMsgid(final long jobid, final String msgid){
        return getHt().execute(new HibernateCallback<Integer>() {

            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("update CommandJob set msgid='" + msgid + "' where id=" + jobid);
                return query.executeUpdate();
            }
        });
    }

}
