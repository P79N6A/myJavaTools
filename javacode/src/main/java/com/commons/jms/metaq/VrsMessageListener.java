package com.commons.jms.metaq;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.uhos.spaces.videos.model.ScreencapInfo;
import com.uhos.spaces.videos.service.ScreencapInfoService;
import com.uhos.spaces.videos.service.VideoInfoService;
import com.uhos.spaces.vrs.model.TvApplication;
import com.uhos.spaces.vrs.service.VrsApplicationService;
import com.taobao.metamorphosis.client.extension.spring.DefaultMessageListener;
import com.taobao.metamorphosis.client.extension.spring.MetaqMessage;

public class VrsMessageListener extends DefaultMessageListener<String> {
    private static Log log = LogFactory.getLog(VrsMessageListener.class);
    
    private VrsApplicationService vrsApplicationService;
    
    private ScreencapInfoService screencapInfoService;
    
    private VideoInfoService videoInfoService;
    
    public VideoInfoService getVideoInfoService() {
        return videoInfoService;
    }


    public void setVideoInfoService(VideoInfoService videoInfoService) {
        this.videoInfoService = videoInfoService;
    }


    public VrsApplicationService getVrsApplicationService() {
        return vrsApplicationService;
    }


    public void setVrsApplicationService(VrsApplicationService vrsApplicationService) {
        this.vrsApplicationService = vrsApplicationService;
    }


    public ScreencapInfoService getScreencapInfoService() {
        return screencapInfoService;
    }


    public void setScreencapInfoService(ScreencapInfoService screencapInfoService) {
        this.screencapInfoService = screencapInfoService;
    }


    @Override
    public void onReceiveMessages(MetaqMessage<String> arg0) {
        try{
            String msg = new String(arg0.getData());
            JSONObject jsonObject = JSONObject.fromObject(msg);
            JSONObject before =null;
            if(jsonObject.containsKey("fieldInfo_before")){
                before = jsonObject.getJSONObject("fieldInfo_before");
                if(before!=null){
                    if(before.containsKey("tv_effective")){
                        String effective=before.getString("tv_effective");
                        if(effective!=null&&effective.equals("1")){
                            if(jsonObject.containsKey("fieldInfo_after")){
                                JSONObject after = jsonObject.getJSONObject("fieldInfo_after");
                                if(after!=null){
                                    if(after.containsKey("tv_effective")){
                                        String effective1=after.getString("tv_effective");
                                        if(effective1!=null&&effective1.equals("0")){
                                            log.info(msg);                                           
                                            String strTvId = jsonObject.getString("tvId");
                                            if(!StringUtils.isEmpty(strTvId)){
                                                TvApplication tvApplication=vrsApplicationService.getTvApplicationByTvId(Long.parseLong(strTvId));
                                                if(tvApplication!=null){
                                                    long vid=tvApplication.getTvVerId();
                                                    List<ScreencapInfo> screencapInfos=screencapInfoService.getAllScreencapInfosBySrcVid(vid, 0, 10);
                                                    while(screencapInfos!=null&&screencapInfos.size()>0){
                                                        log.info("removeScreencapInfos vrsvid:"+vid+",szie:"+screencapInfos.size());
                                                        for(ScreencapInfo screencapInfo:screencapInfos){
                                                            videoInfoService.removeVideoInfo(screencapInfo.getVid());
                                                            screencapInfoService.removeScreencapInfo(screencapInfo.getId());
                                                        }
                                                        screencapInfos=screencapInfoService.getAllScreencapInfosBySrcVid(vid, 0, 10);
                                                    }
                                                }
                                                
                                            }
                                            
                                          
                                        }
                                    }
                                    
                                }
                                
                            }
                        }
                    }
                    
                }
                
            }
            
                        
            
        }catch(Exception ex){
            log.error(" onReceiveMessages ", ex);
        }
        
    }

}
