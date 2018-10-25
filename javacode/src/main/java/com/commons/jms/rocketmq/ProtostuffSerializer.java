package com.commons.jms.rocketmq;

package com.sohu.index.tv.mq.serializable;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.sohu.tv.mq.serializable.VO;
import java.util.concurrent.ConcurrentHashMap;

public class ProtostuffSerializer {
    private static ConcurrentHashMap<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap();

    public ProtostuffSerializer() {
    }

    public <T> byte[] serialize(T source) {
        VO<T> vo = new VO(source);
        LinkedBuffer buffer = LinkedBuffer.allocate(512);

        byte[] var5;
        try {
            Schema<VO> schema = getSchema(VO.class);
            var5 = this.serializeInternal(vo, schema, buffer);
        } catch (Exception var9) {
            throw new IllegalStateException(var9.getMessage(), var9);
        } finally {
            buffer.clear();
        }

        return var5;
    }

    public <T> T deserialize(byte[] bytes) {
        try {
            Schema<VO> schema = getSchema(VO.class);
            VO vo = (VO)this.deserializeInternal(bytes, schema.newMessage(), schema);
            return vo != null && vo.getValue() != null ? vo.getValue() : null;
        } catch (Exception var4) {
            throw new IllegalStateException(var4.getMessage(), var4);
        }
    }

    private <T> byte[] serializeInternal(T source, Schema<T> schema, LinkedBuffer buffer) {
        return ProtostuffIOUtil.toByteArray(source, schema, buffer);
    }

    private <T> T deserializeInternal(byte[] bytes, T result, Schema<T> schema) {
        ProtostuffIOUtil.mergeFrom(bytes, result, schema);
        return result;
    }

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema)cachedSchema.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            cachedSchema.put(clazz, schema);
        }

        return (Schema)schema;
    }
}
