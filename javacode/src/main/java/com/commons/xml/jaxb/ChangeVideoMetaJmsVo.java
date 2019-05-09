/**
 * 
 */
package com.commons.xml.jaxb;

import com.sohu.spaces.videos.model.VideoMeta;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * @description videoInfoChangeTopic对象
 * @author maryyang
 * @date 2018年11月28日
 */

@XmlRootElement(name="changeVideoMeta", namespace="http://service.videos.spaces.sohu.com/")
@XmlType(propOrder = {"arg0","arg1","arg2"})
public class ChangeVideoMetaJmsVo implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer arg0;
    
    private VideoMeta arg1;
    
    private VideoMeta arg2;

    public ChangeVideoMetaJmsVo(){
    }
    
    public ChangeVideoMetaJmsVo(Integer arg0, VideoMeta arg1, VideoMeta arg2){
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
    

    public Integer getArg0() {
        return arg0;
    }

    public void setArg0(Integer arg0) {
        this.arg0 = arg0;
    }

    public VideoMeta getArg1() {
        return arg1;
    }

    public void setArg1(VideoMeta arg1) {
        this.arg1 = arg1;
    }

    public VideoMeta getArg2() {
        return arg2;
    }

    public void setArg2(VideoMeta arg2) {
        this.arg2 = arg2;
    }


}
