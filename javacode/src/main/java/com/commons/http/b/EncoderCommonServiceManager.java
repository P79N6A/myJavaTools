/**
 *
 */
package com.uhos.spaces.videos.encoder.base.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.uhos.blog.exception.ServiceDaoException;
import com.uhos.blog.exception.ServiceException;
import com.uhos.spaces.videos.encoder.base.util.EncoderHelper;
import com.uhos.spaces.videos.encoder.base.util.GsonUtil;
import com.uhos.spaces.videos.model.VideoFile;
import com.uhos.spaces.videos.model.VideoMeta;

/**
 * @author guangchaowu
 */
public class EncoderCommonServiceManager extends BaseService {
    private static Logger logger = Logger.getLogger(EncoderCommonServiceManager.class);
    private static EncoderCommonServiceManager instance;
    private static final String UPDATE_VIDEOMETA = SERVICE_HOST_PRE + "/encoderCommon/updateVideoMeta.do";
    private static final String GET_EXTLOGO = SERVICE_HOST_PRE + "/encoderCommon/getExtlogo.do";
    private static final String SAVE_VIDEOVERSION = SERVICE_HOST_PRE + "/encoderCommon/saveVideoVersion.do";
    private static final String UPDATE_SERVER = SERVICE_HOST_PRE + "/encoderCommon/updateServer.do";
    private static final String SAVE_VIDEOFILE_LIST = SERVICE_HOST_PRE + "/encoderCommon/saveVideoFileList.do";
    private static final String SEND_TO_CDN = SERVICE_HOST_PRE + "/encoderCommon/sendToCDN.do";
    private static final String UPDATE_ENCODERSERVER_LOAD = SERVICE_HOST_PRE
            + "/encoderCommon/updateEncoderServerLoad.do";
    private static final String UPDATE_MD5INFO = SERVICE_HOST_PRE + "/encoderCommon/updateMd5Info.do";
    private static final String GET_ISSRC_CUTIMG = SERVICE_HOST_PRE + "/encoderCommon/isSrcCutImg.do";
    private static final String isFilterVideoScreenShot = SERVICE_HOST_PRE + "/encoderCommon/isFilterVideoScreenShot.do";

    public static EncoderCommonServiceManager getInstance() {
        if (instance == null) {
            synchronized (EncoderCommonServiceManager.class) {
                if (instance == null) {
                    instance = new EncoderCommonServiceManager();
                }
            }
        }
        return instance;
    }

    public void updateEncoderServerLoad(int type) throws ServiceDaoException, ServiceException {
        String ip = EncoderHelper.getLocalIP();
        Map map = new HashMap();
        map.put("ip", ip);
        map.put("type", type);
        boolean b = requestService(UPDATE_ENCODERSERVER_LOAD, map);
        if (!b) {
            logger.error("updateEncoderServerLoad error, ip:" + ip);
        }
        logger.info("updateEncoderServerLoad, ip:" + ip);
    }

    public void sendToCDN(String fids, int userLevel, int vertype) throws ServiceDaoException, ServiceException {
        Map map = new HashMap();
        map.put("fids", fids);
        map.put("userLevel", userLevel);
        map.put("vertype", vertype);
        requestService(SEND_TO_CDN, map);
    }

    public String saveVideoFileList(List<VideoFile> videoFileList) throws ServiceDaoException, ServiceException {
        String gson = GsonUtil.toGson(videoFileList);
        Map<String, String> map = new HashMap<String, String>();
        map.put("videoFileList", gson);
        return (String) getBasicObjectAsPost(SAVE_VIDEOFILE_LIST, map, String.class);
    }

    public Long saveVideoVersion(long videoId, int verType, String fileIds, int videoSize, long videoLength)
            throws ServiceDaoException, ServiceException {
        Map map = new HashMap();
        map.put("videoId", videoId);
        map.put("verType", verType);
        map.put("fileIds", fileIds);
        map.put("videoSize", videoSize);
        map.put("videoLength", videoLength);
        return (Long) getBasicObject(SAVE_VIDEOVERSION, map, Long.class);
    }

    public String getExtlogo(long vid, long userId) throws ServiceDaoException, ServiceException {
        Map map = new HashMap();
        map.put("vid", vid);
        map.put("userId", userId);
        return (String) getBasicObject(GET_EXTLOGO, map, String.class);
    }

    public void updateVideoMeta(long vid, Date enStartTime, Date enEndTime, String srcFile, int preTime,
            VideoMeta videoMeta) throws ServiceDaoException, ServiceException {
        videoMeta = new VideoMeta();
        String srcIp = EncoderHelper.getLocalIP();
        videoMeta.setId(vid);
        videoMeta.setEnStartTime(enStartTime);
        videoMeta.setEnEndTime(enEndTime);
        videoMeta.setSrcFile(srcFile);
        videoMeta.setSrcIp(srcIp);
        String videoMetaStr = GsonUtil.toGson(videoMeta, VideoMeta.class);
        // try {
        // videoMetaStr = URLEncoder.encode(videoMetaStr, "UTF-8");
        // } catch (UnsupportedEncodingException e) {
        // logger.error("UnsupportedEncodingException", e);
        // throw new ServiceException(e);
        // }
        Map map = new HashMap();
        map.put("videoMeta", videoMetaStr);
        map.put("preTime", preTime);
        requestService(UPDATE_VIDEOMETA, map);
    }

    public void updateServer(Integer preTime) throws ServiceDaoException, ServiceException {
        String ip = EncoderHelper.getLocalIP();
        Map map = new HashMap();
        map.put("ip", ip);
        map.put("preTime", preTime);
        requestService(UPDATE_SERVER, map);
    }

    public void updateMd5Info(int verType, int ktvEncode, long vid, String fileids, Integer videoType, Long videoSize,
            int vlength, String md5)
            throws ServiceDaoException, ServiceException {
        Map map = new HashMap();
        map.put("verType", verType);
        map.put("ktvEncode", ktvEncode);
        map.put("vid", vid);
        map.put("fileids", fileids);
        map.put("videoType", videoType);
        map.put("videoSize", videoSize);
        map.put("vlength", vlength);
        if (StringUtils.isNotBlank(md5)) {
            map.put("md5", md5);
        }
        requestService(UPDATE_MD5INFO, map);
    }

    public boolean isFilterVideoScreenShot(long vid) {
        Map map = new HashMap();
        map.put("id", vid);
        return requestService(isFilterVideoScreenShot, map);
    }
    
    /**
     * 返回当前视频是否是源截图
     * @param vidStr
     * @return
     * @throws ServiceDaoException
     * @throws ServiceException
     */
    public boolean isSrcCutImg(String vidStr)throws ServiceDaoException, ServiceException {
        if(StringUtils.isBlank(vidStr)){
            return false;
        }
        long vid = Long.parseLong(vidStr);
        if(vid <=0 ){
            return false;
        }
        Map map = new HashMap();
        map.put("vid", vid);
        return requestService(GET_ISSRC_CUTIMG, map);
    }

    public static void main(String[] args) throws ServiceDaoException, ServiceException {
        EncoderCommonServiceManager instance2 = EncoderCommonServiceManager.getInstance();
        instance2.updateMd5Info(1, 0, 52259818L, "123,456", 2, null, 44, null);
    }

}
