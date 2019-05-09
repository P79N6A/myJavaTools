/**
 * 
 */
package com.commons.xml.jaxb;

import com.sohu.spaces.videos.model.VideoMeta;
import com.sohu.spaces.videos.vo.ChangeVideoMetaJmsVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;

/**
 * @description 获取VideoMetaChange的JAXBContext，单例
 */
public class VideoMetaChangeJAXBContext {
    
    private static Log logger = LogFactory.getLog(VideoMetaChangeJAXBContext.class);
    
    private static JAXBContext insantce = getInstance();

    public static JAXBContext getInstance() {
        if (insantce == null) {
            synchronized (JAXBContext.class) {
                if (insantce == null) {
                    try {
                        Long start = System.currentTimeMillis();
                        insantce = JAXBContext.newInstance(ChangeVideoMetaJmsVo.class,VideoMeta.class);
                        logger.info("VideoMetaChangeJAXBContext newInstance cost " + (System.currentTimeMillis() - start) + " ms");
                    } catch (Exception e) {
                       logger.error("VideoMetaChangeJAXBContext  create error," + e);
                    }
                }
            }
        }
        return insantce;
    }
}
