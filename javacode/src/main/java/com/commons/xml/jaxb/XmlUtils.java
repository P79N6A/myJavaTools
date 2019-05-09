package com.commons.xml.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class XmlUtils {

    public static void main(String[] args) throws Exception {

        //---object to xml---------
        VideoMeta oldMeta = new VideoMeta();
        oldMeta.setId(111L);
        oldMeta.setUpServer("aaaa");
        VideoMeta newMeta = new VideoMeta();
        newMeta.setId(222L);
        newMeta.setUpServer("bbbb");

        JAXBContext context = VideoMetaChangeJAXBContext.getInstance();    // 获取上下文对象
        //logger.info("changeVideoMeta VideoMetaChangeJAXBContext cost " + (System.currentTimeMillis() - start) + "ms" + ",vid:" + oldInfo.getId());
        if (context != null) {
            //Long start2 = System.currentTimeMillis();
            Marshaller marshaller = context.createMarshaller(); // 根据上下文获取marshaller对象
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); //去掉 standalone="yes"
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化XML输出，有分行和缩进
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");  // 设置编码字符集
            String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            try {
                marshaller.setProperty("com.sun.xml.bind.xmlHeaders", header);
            } catch (PropertyException e) {
                marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", header);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(new ChangeVideoMetaJmsVo(2, oldMeta, newMeta), baos);
            //logger.info("changeVideoInfo marshal cost "+ (System.currentTimeMillis() - start2) + "ms" + ",vid:" + oldInfo.getId());
            String xmlObj = baos.toString("UTF-8");// 生成XML字符串
            Long start3 = System.currentTimeMillis();

            System.out.println(xmlObj);
        }

        //--xml to object------------------

        Unmarshaller unmarshaller = context.createUnmarshaller();
        ChangeVideoMetaJmsVo changeVideoMetaJmsVo = (ChangeVideoMetaJmsVo)unmarshaller.unmarshal(new File("/Users/flsx/IdeaProjects/videobase/spaces-vrs2video-service/src/main/resources/a.xml"));

        System.out.println(changeVideoMetaJmsVo.getArg0());
        System.out.println(changeVideoMetaJmsVo.getArg1().getId());

    }

}
