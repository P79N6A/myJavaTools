/**
 *
 */
package com.commons.xml.jaxb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "video_meta")
public class VideoMeta implements Serializable {
    private static final long serialVersionUID = -4430706784377966557L;
    private Long id;// video id
    private String upServer;// up server ip
    private String enServer;// encoder server ip
    private String uploadIp;// user up ip
    private Integer nettype;// nettype
    private Integer uploadFrom;
    private Date upStartTime;
    private Date upFinishTime;
    private Date enStartTime;
    private Date enEndTime;
    private Date auStartTime;
    private Date auEndTime;
    private Long srcVideoSize;
    private Long videoLength;
    private Long videoSize;
    private Long userId;
    private Integer status;
    private String upmd5;// 上传md5
    private String srcFile;// 源文件路径
    private Integer ipcode;
    private Date watchTime;// 可观看时间
    private String srcIp;// 原视频所在服务器ip
    private String beforeMd5; //上传前文件md5
    private String uuid;
    private Integer rotation; //旋转角度 0 90 270
    private Integer framerate;//帧率
    
    
    /*
     * 扩展名
     */
    private String srcExtname;
    /*
     * 视频格式
     */
    private String srcFormat;
    /*
     * 码率
     */
    private Integer bitrate;
    /*
     * 分辨率
     */
    private String srcDefinition;
    /*
     * 二次转码ip
     */
    private String en2Ip;
    /*
     * 二次转码开始时间
     */
    private Date en2StartTime;
    /*
     * 二次转码结束时间
     */
    private Date en2EndTime;
    /*
     * 是否可以转超清
     */
    private Integer isHigh;
    /*
     * 预计转码时长
     */
    private Integer pretime;

    private String netName;// 移动上传网络类型

    private int enType;// ktv转码类型 默认为0

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "up_server")
    public String getUpServer() {
        return upServer;
    }

    public void setUpServer(String upServer) {
        this.upServer = upServer;
    }

    @Column(name = "en_server")
    public String getEnServer() {
        return enServer;
    }

    public void setEnServer(String enServer) {
        this.enServer = enServer;
    }

    @Column(name = "uploadip")
    public String getUploadIp() {
        return uploadIp;
    }

    public void setUploadIp(String uploadIp) {
        this.uploadIp = uploadIp;
    }

    @Column(name = "nettype")
    public Integer getNettype() {
        return nettype;
    }

    public void setNettype(Integer nettype) {
        this.nettype = nettype;
    }

    @Column(name = "uploadfrom")
    public Integer getUploadFrom() {
        return uploadFrom;
    }

    public void setUploadFrom(Integer uploadFrom) {
        this.uploadFrom = uploadFrom;
    }

    @Column(name = "up_start_time")
    public Date getUpStartTime() {
        return upStartTime;
    }

    public void setUpStartTime(Date upStartTime) {
        this.upStartTime = upStartTime;
    }

    @Column(name = "up_finish_time")
    public Date getUpFinishTime() {
        return upFinishTime;
    }

    public void setUpFinishTime(Date upFinishTime) {
        this.upFinishTime = upFinishTime;
    }

    @Column(name = "en_start_time")
    public Date getEnStartTime() {
        return enStartTime;
    }

    public void setEnStartTime(Date enStartTime) {
        this.enStartTime = enStartTime;
    }

    @Column(name = "au_start_time")
    public Date getAuStartTime() {
        return auStartTime;
    }

    public void setAuStartTime(Date auStartTime) {
        this.auStartTime = auStartTime;
    }

    @Column(name = "src_video_size")
    public Long getSrcVideoSize() {
        return srcVideoSize;
    }

    public void setSrcVideoSize(Long srcVideoSize) {
        this.srcVideoSize = srcVideoSize;
    }

    @Column(name = "videolength")
    public Long getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(Long videoLength) {
        this.videoLength = videoLength;
    }

    @Column(name = "videosize")
    public Long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Long videoSize) {
        this.videoSize = videoSize;
    }

    @Column(name = "userid")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "au_end_time")
    public Date getAuEndTime() {
        return auEndTime;
    }

    public void setAuEndTime(Date auEndTime) {
        this.auEndTime = auEndTime;
    }

    @Column(name = "en_end_time")
    public Date getEnEndTime() {
        return enEndTime;
    }

    public void setEnEndTime(Date enEndTime) {
        this.enEndTime = enEndTime;
    }

    @Column(name = "src_file")
    public String getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(String srcFile) {
        this.srcFile = srcFile;
    }

    @Column(name = "src_ip")
    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    @Column(name = "ipcode")
    public Integer getIpcode() {
        return ipcode;
    }

    public void setIpcode(Integer ipcode) {
        this.ipcode = ipcode;
    }

    @Column(name = "watch_time")
    public Date getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(Date watchTime) {
        this.watchTime = watchTime;
    }

    @Column(name = "net_name")
    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    @Column(name = "en_type")
    public int getEnType() {
        return enType;
    }

    public void setEnType(int enType) {
        this.enType = enType;
    }

    @Column(name = "src_extname")
    public String getSrcExtname() {
        return srcExtname;
    }

    public void setSrcExtname(String srcExtname) {
        this.srcExtname = srcExtname;
    }

    @Column(name = "src_format")
    public String getSrcFormat() {
        return srcFormat;
    }

    public void setSrcFormat(String srcFormat) {
        this.srcFormat = srcFormat;
    }

    @Column(name = "src_bitrate")
    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    @Column(name = "src_definition")
    public String getSrcDefinition() {
        return srcDefinition;
    }

    public void setSrcDefinition(String srcDefinition) {
        this.srcDefinition = srcDefinition;
    }

    @Column(name = "en2_ip")
    public String getEn2Ip() {
        return en2Ip;
    }

    public void setEn2Ip(String en2Ip) {
        this.en2Ip = en2Ip;
    }

    @Column(name = "en2_start_time")
    public Date getEn2StartTime() {
        return en2StartTime;
    }

    public void setEn2StartTime(Date en2StartTime) {
        this.en2StartTime = en2StartTime;
    }

    @Column(name = "en2_end_time")
    public Date getEn2EndTime() {
        return en2EndTime;
    }

    public void setEn2EndTime(Date en2EndTime) {
        this.en2EndTime = en2EndTime;
    }

    @Column(name = "is_high")
    public Integer getIsHigh() {
        return isHigh;
    }

    public void setIsHigh(Integer isHigh) {
        this.isHigh = isHigh;
    }

    @Column(name = "pretime")
    public Integer getPretime() {
        return pretime;
    }

    public void setPretime(Integer pretime) {
        this.pretime = pretime;
    }

    @Column(name = "upmd5")
    public String getUpmd5() {
        return upmd5;
    }

    public void setUpmd5(String upmd5) {
        this.upmd5 = upmd5;
    }

    @Column(name = "before_md5")
    public String getBeforeMd5() {
        return beforeMd5;
    }

    public void setBeforeMd5(String beforeMd5) {
        this.beforeMd5 = beforeMd5;
    }

    @Column(name = "uuid")
    public String getUuid() {
    
        return uuid;
    }

    public void setUuid(String uuid) {
    
        this.uuid = uuid;
    }
    @Column(name = "rotation")
	public Integer getRotation() {
		return rotation;
	}

	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}
	@Column(name = "src_framerate")
	public Integer getFramerate() {
        return framerate;
    }

    public void setFramerate(Integer framerate) {
        this.framerate = framerate;
    }
}
