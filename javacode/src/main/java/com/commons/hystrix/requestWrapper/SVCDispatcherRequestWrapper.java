/*
 * Copyright (c) 2013 Sohu. All Rights Reserved
 */
package com.commons.hystrix.requestWrapper;

import com.sohu.spaces.cloud.SVCDispatcher;

/**
 * <p>
 * Description:
 * </p>
 * @author guangchaowu
 * @version 1.0
 * @Date 2018年6月8日
 */
public class SVCDispatcherRequestWrapper {

    public static String getSgcServer(final long clusterId, final String excludeServer) {
        SafeServiceRequest ssr = new SafeServiceRequest("getSgcServer", "SVCDispatcher", "getSgcServerPool") {

            @Override
            protected String run() throws Exception {
                return SVCDispatcher.getSgcServer(clusterId, excludeServer);
            }

        };
        return ssr.execute();
    }

    public static String getServer(final long clusterId, final String excludeServer) {
        SafeServiceRequest ssr = new SafeServiceRequest("getServer", "SVCDispatcher", "getServerPool") {

            @Override
            protected String run() throws Exception {
                return SVCDispatcher.getServer(clusterId, excludeServer);
            }

        };
        return ssr.execute();
    }

    public static String getHyServer(final long clusterId, final String excludeServer) {
        SafeServiceRequest ssr = new SafeServiceRequest("getHyServer", "SVCDispatcher", "getHyServerPool") {

            @Override
            protected String run() throws Exception {
                return SVCDispatcher.getHyServer(clusterId, excludeServer);
            }

        };
        return ssr.execute();
    }

    public static void main(String[] args) {
        try{
            long s = System.currentTimeMillis();
            String sgcServer = SVCDispatcher.getServer(1, "");
            System.out.println(">>>>>>>>>>>>>>>>>>"+sgcServer);
            System.out.println(">>>>>>>>>>>>>>>>>>"+(System.currentTimeMillis() - s));

            s = System.currentTimeMillis();
            sgcServer = SVCDispatcherRequestWrapper.getServer(1, "");
            System.out.println(">>>>>>>>>>>>>>>>>>"+sgcServer);
            System.out.println(">>>>>>>>>>>>>>>>>>"+(System.currentTimeMillis() - s));
        }catch(Throwable t){
            System.out.println(t);
        }
    }

}

