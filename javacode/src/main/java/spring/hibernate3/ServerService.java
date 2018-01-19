/*
 * Copyright (c) 2013 uhos. All Rights Reserved
 */
package spring.hibernate3;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.uhos.ccm.heart.dao.ParentDao;
import com.uhos.ccm.heart.model.Server;


/**
 * <p>
 * Description:
 * </p>
 * @author guangchaowu
 * @version 1.0
 * @Date 2016年4月20日
 */
//@Service
//@Transactional
public class ServerService extends ParentDao<Server> {
    private final static Log log = LogFactory.getLog(ServerService.class);

    @Autowired
    private SessionFactory sessionFactory;

    public Server getByIp(String ip){
        List list = getHt().find("from Server where ip = '" + ip + "'");
        if(list == null || list.size() <= 0){
            return null;
        }
        return (Server) list.get(0);
    }

    public List<Server> getPages(){
        List<Server> list = getHt().find("from Server order by createdTime");
        return list;
    }

    public List<Server> getServers(int status){
        List<Server> list = getHt().find("from Server where status=" + status);
        return list;
    }

    public List<Server> getAutoStatusServers(){
        List<Server> list = getHt().find("from Server where status=1 or status=2");
        return list;
    }

    public List<Integer> getIdc(){
        List<Integer> list = getHt().find("select distinct(idc) from Server where idc is not null");
        return list;
    }

    //@Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateStatus(final String ip, final int status){
//        Query query = getHibernateTemplate().getSessionFactory().openSession().createQuery("update Server set status=" + status + " where ip='" + ip + "'");
//        query.executeUpdate();

//        Session session = super.getSession();
//        Query query = session.createQuery("update Server set status=" + status + " where ip='" + ip + "'");
//        query.executeUpdate();
//        releaseSession(session);

        Server server = getByIp(ip);
        if(server != null && ((server.getStatus() != null && server.getStatus().intValue() != status) || server.getStatus() == null)){
            log.info(ip + ",status:" + status);

            getHt().execute(new HibernateCallback<Integer>() {

                @Override
                public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                    Query query = session.createQuery("update Server set status=" + status + " where ip='" + ip + "'");
                    return query.executeUpdate();
                }
            });
        }

    }

}

