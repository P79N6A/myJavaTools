/*
 * Copyright (c) 2013 Sohu. All Rights Reserved
 */
package com.db;
/**
 * <p>
 * Description: 
 * </p>
 * @author guangchaowu
 * @version 1.0
 * @Date 2018年2月6日
 */
public class MongoDbUtil {
    private static Logger log = LoggerFactory.getLogger(MongoDbUtil.class);
    private Log tokumxLog = LogFactory.getLog("Tokumx");
    private static final int singleUsedTime = 20;   
    private static final String MGDB_DB="videodb";
    private static final String MGDB_COLLECTION="user_video";
    private static final String MGDB_USER="videodb";
    private static final String MGDB_PWD="+NEq3J8btqab+E0";
    private static final int MGDB_PORT=27017;
    private static DB videoDb = null;
    private static DBCollection collection = null;
    
    public void init(){
        try {
            Properties pro=PropertyUtil.getResourceAsProperties("conf.properties");
            String ipStr = pro.getProperty("mongo.ip", "10.16.15.36");
            String name =  pro.getProperty("mongo.name", "videodb");
            String passport = pro.getProperty("mongo.passport", "+NEq3J8btqab+E0");
            String[] ips = ipStr.split(",");
            List<ServerAddress> addresses = new ArrayList<ServerAddress>();
            for(String ip:ips){
                log.info("MongoDbUtil ip:"+ip);
                ServerAddress address = new ServerAddress(ip,MGDB_PORT);
                addresses.add(address);
            }
            
            Mongo mongo = new Mongo(addresses);
            videoDb = mongo.getDB(MGDB_DB);
            videoDb.authenticate(name, passport.toCharArray());
            collection = videoDb.getCollection(MGDB_COLLECTION);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            log.error("mgdb host is not exist",e);
        } catch (MongoException e) {
            e.printStackTrace();
            log.error("mgdb getdb exception",e);
        }
    }

    