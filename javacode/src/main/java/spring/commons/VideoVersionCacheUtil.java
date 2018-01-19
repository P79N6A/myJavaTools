package com.uhos.spaces.videos.util.ehcache;

import java.util.HashMap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.uhos.spaces.videos.model.VideoVersion;

public class VideoVersionCacheUtil {
    public Log log = LogFactory.getLog(VideoVersionCacheUtil.class);
    private Cache cache;
    private static VideoVersionCacheUtil insantce;

    public static VideoVersionCacheUtil getInstance() {
        if (insantce == null) {
            synchronized (VideoVersionCacheUtil.class) {
                if (insantce == null) {
                    insantce = new VideoVersionCacheUtil();
                }
            }
        }
        return insantce;
    }

    public VideoVersion getVideoVersion(Long id) {
        try {
            Element element = cache.get("obj#" + id);
            if (element != null) {
                VideoVersion videoVersion = (VideoVersion) element.getValue();
                return videoVersion;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("ehcache getVideoVersion:", e);
        }
        return null;
    }

    public void setVideoVersion(VideoVersion videoVersion) {
        try {
            if (videoVersion != null) {
                cache.put(new Element("obj#" + videoVersion.getId(), videoVersion));
            }
        } catch (Exception e) {
            log.error("ehcache setVideoVersion:", e);
        }
    }

    public VideoVersion getVideoVersionByType(Long vid, Integer verType) {
        try {
            Element element = cache.get("byType#" + vid + "_" + verType);
            if (element != null) {
                VideoVersion videoVersion = (VideoVersion) element.getValue();
                return videoVersion;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("ehcache getVideoVersion:", e);
        }
        return null;
    }

    public void setVideoVersionByType(VideoVersion videoVersion) {
        try {
            if (videoVersion != null) {
                cache.put(new Element("byType#" + videoVersion.getVideoId() + "_" + videoVersion.getVerType(),
                        videoVersion));
            }
        } catch (Exception e) {
            log.error("ehcache setVideoVersion:", e);
        }
    }

    public void setVideoVersionMap(Long videoId, HashMap<Integer, VideoVersion> vMap) {
        try {
            if (vMap != null) {
                cache.put(new Element("map#" + videoId, vMap));
            }
        } catch (Exception e) {
            log.error("ehcache setVideoVersionMap:", e);
        }
    }

    public HashMap<Integer, VideoVersion> getVideoVersionMap(Long videoId) {
        try {
            Element element = cache.get("map#" + videoId );
            if (element != null) {
                HashMap<Integer, VideoVersion> map = (HashMap<Integer, VideoVersion>) element.getValue();
                return map;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("ehcache getVideoVersionMap:", e);
        }
        return null;
    }
    
    public VideoVersion getVVByType(Long vid, Integer verType) {
        HashMap<Integer, VideoVersion> map= getVideoVersionMap(vid);
        if(map!=null){
            VideoVersion vv=map.get(verType);
            return vv;
        }else {
            return null;
        }
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
