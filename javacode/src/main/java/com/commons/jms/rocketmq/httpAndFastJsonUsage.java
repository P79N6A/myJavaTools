package com.commons.jms.rocketmq;

import net.sf.json.JSON;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.sohu.tv.mq.dto.ClusterInfoDTO;
import com.sohu.tv.mq.dto.ClusterInfoDTOResult;
import com.sohu.tv.mq.serializable.MessageSerializer;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.common.utils.HttpTinyClient;
import org.apache.rocketmq.common.utils.HttpTinyClient.HttpResult;

public class httpAndFastJsonUsage {

    public static void usage() {
        List<String> paramValues = new ArrayList();
        paramValues.add("topic");
        paramValues.add(this.getTopic());
        paramValues.add("group");
        paramValues.add(this.group);
        paramValues.add("role");
        paramValues.add(String.valueOf(this.role()));
        paramValues.add("v");
        paramValues.add(Version.get());
        long times = 1L;

        ClusterInfoDTOResult clusterInfoDTOResult = null;

        try {
            HttpResult result = HttpTinyClient.httpGet("http://" + this.mqCloudDomain + "/cluster/info", (List)null, paramValues, "UTF-8", 3000L);
            if (200 == result.code) {
                // FastJson
                clusterInfoDTOResult = (ClusterInfoDTOResult)JSON.parseObject(result.content, ClusterInfoDTOResult.class);
                if (clusterInfoDTOResult.ok()) {
                    this.clusterInfoDTO = clusterInfoDTOResult.getResult();
                }
            } else {
                this.logger.error("http connetion err: code:{},info:{}", result.code, result.content);
            }
        } catch (Throwable var7) {
            this.logger.error("http err, topic:{},group:{}", new Object[]{this.topic, this.group, var7});
        }
    }
}
