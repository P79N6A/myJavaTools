package com.uhos.spaces.web.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.uhos.blog.web.util.RequestUtil;
import com.uhos.spaces.util.MD5;
import com.uhos.spaces.videos.model.Server;
import com.uhos.spaces.videos.service.ServerService;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
	private static Log log = LogFactory.getLog(SecurityInterceptor.class);
	private static final String PRIVATE_KEY = "s54!sjfl*3ldf";
	private static List<Server> allServers = new ArrayList<Server>();
	private static long lastTime;
	private static byte[] lock = new byte[0];
	private static final int REFRESHTIME = 10 * 60 * 1000;

	@Autowired
	private ServerService serverService;

	@Override
    public boolean  preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String clientIp = "";
		try {
			clientIp = RequestUtil.getClientIpAddress(request);
			String sip = request.getParameter("sip");
			String t = request.getParameter("t");
			String sig = request.getParameter("sig");
			log.info("clinetip:" + clientIp + "  requestip:" + sip + " try to access.");
			if(checkClientIp(clientIp, sip) && check(sip, t, sig)){
			//if(check(sip, t, sig)){
				return true;
			}else{
				log.warn("ip:" + clientIp + " access refused.");
				request.getRequestDispatcher("/WEB-INF/pages/videos/json_error.jsp").forward(request, response);
			}
		} catch (Exception e) {
			log.error("SecurityInterceptor, ip:" + clientIp, e);
		}
		return false ;
	}

	private boolean checkClientIp(String clientIp, String sip){
		try {
		    if(StringUtils.isNotBlank(sip)){
		        if(sip.startsWith("10.") || sip.startsWith("192.") || sip.equals("127.0.0.1")){
		            return true;
		        }
		    }

			if(StringUtils.isNotBlank(clientIp)){
				long now = System.currentTimeMillis();
				if(allServers.isEmpty() || ((now - lastTime) > REFRESHTIME)){
					synchronized (lock) {
						if(allServers.isEmpty() || ((now - lastTime) > REFRESHTIME)){
							lastTime = now;
							List<Server> encoderServerList = serverService.getServersByServerType(Server.SERVER_TYPE_CODE, 0, 500);
							List<Server> encoder2ServerList = serverService.getServersByServerType(Server.SERVER_TYPE_CODE2, 0, 500);
							List<Server> uploadServerList = serverService.getServersByServerType(Server.SERVER_TYPE_VIDEO_UPLOAD, 0, 500);
							List<Server> ktvRecEnServerList = serverService.getServersByServerType(Server.SERVER_TYPE_KTV_REC_CODE, 0, 500);
							List<Server> tempServers = new ArrayList<Server>();
							// allServers.clear();
							tempServers.addAll(encoderServerList);
							tempServers.addAll(encoder2ServerList);
							tempServers.addAll(uploadServerList);
							tempServers.addAll(ktvRecEnServerList);
							allServers = tempServers;
						}
					}
				}

				for(Server server : allServers){
					if(clientIp.equals(server.getIp()) || clientIp.equals(server.getOutIp())){
						return true;
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
		log.info("clientip:" + clientIp + " access refused.");
		return false;
	}

	private boolean check(String ip, String t, String sig){
		try{
			if(StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(t) && StringUtils.isNotBlank(sig)){
	            String cryptSig = MD5.crypt(ip + t + PRIVATE_KEY);
	            if(cryptSig.equals(sig) && checkTime(t)){
	            	return true;
	            }
			}else{
				log.warn("illegal request:" + ip + "#" + t + "#" + sig);
			}
		}catch (Exception e) {
			log.error("check request error", e);
		}
		log.info(ip + "#" + t + "#" + sig + " access refused, time or ip is not right");
		return false;
	}

	private boolean checkTime(String t){
		Long time = Long.parseLong(t);
		long now = System.currentTimeMillis();
		long period = (now - time) / 1000L;
		if(period <= 10 * 60){
			return true;
		}

		return false;
	}

}
