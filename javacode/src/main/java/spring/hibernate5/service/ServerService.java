/*
 * Copyright (c) 2013 Sohu. All Rights Reserved
 */
package com.sohu.ccm.heart.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sohu.ccm.heart.model.Server;


/**
 * <p>
 * Description:
 * </p>
 * @author guangchaowu
 * @version 1.0
 * @Date 2016年4月20日
 */
@Service
@Transactional
public class ServerService {
    private final static Log log = LogFactory.getLog(ServerService.class);

    @Autowired
    private ServerDao serverDao;

    public Server getByIp(String ip){
        String hql = "from Server where ip = '" + ip + "'";
        return serverDao.get(hql);
    }

    public List<Server> getPages(){
        String hql = "from Server order by createdTime";
        List<Server> list = serverDao.find(hql);
        return list;
    }

    public List<Server> getServers(int status){
        String hql = "from Server where status=" + status;
        List<Server> list = serverDao.find(hql);
        return list;
    }

    public List<Server> getAutoStatusServers(){
        String hql = "from Server where status=1 or status=2";
        List<Server> list = serverDao.find(hql);
        return list;
    }

    public List<Integer> getIdc(){
        String hql = "select distinct(idc) from Server where idc is not null";
        List<Integer> list = serverDao.find2(hql);
        return list;
    }

    //@Transactional(propagation=Propagation.REQUIRES_NEW)
    //@Transactional(rollbackFor = { Exception.class })
    public void updateStatus(final String ip, final int status) throws Exception{
//        Query query = getHibernateTemplate().getSessionFactory().openSession().createQuery("update Server set status=" + status + " where ip='" + ip + "'");
//        query.executeUpdate();

//        Session session = super.getSession();
//        Query query = session.createQuery("update Server set status=" + status + " where ip='" + ip + "'");
//        query.executeUpdate();
//        releaseSession(session);

        Server server = getByIp(ip);
        if(server != null && ((server.getStatus() != null && server.getStatus().intValue() != status) || server.getStatus() == null)){
            log.info(ip + ",status:" + status);
            String hql = "update Server set status=" + status + " where ip='" + ip + "'";
            serverDao.executeHql(hql);
//            hql = "update Server set status=" + status + " where ip='10.11.132.156'";
//            serverDao.executeHql(hql);
//            if(status == 20){
//                throw new RuntimeException("aaaa");
//            }else if(status == 21){
//                throw new Exception("bbbbb");
//            }
//            getHt().execute(new HibernateCallback<Integer>() {
//
//                @Override
//                public Integer doInHibernate(Session session) throws HibernateException, SQLException {
//                    Query query = session.createQuery();
//                    return query.executeUpdate();
//                }
//            });
        }

    }

}

