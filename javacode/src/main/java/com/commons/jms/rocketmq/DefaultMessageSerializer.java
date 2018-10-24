package com.commons.jms.rocketmq;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class DefaultMessageSerializer<T> implements MessageSerializer<T> {
    public DefaultMessageSerializer() {
    }

    /*
      learn usage of protostuff
     */
    public byte[] serialize(T source) {
        VO<T> vo = new VO(source);
        LinkedBuffer buffer = LinkedBuffer.allocate(512);

        try {
            Schema<VO> schema = RuntimeSchema.getSchema(VO.class);
            return ProtostuffIOUtil.toByteArray(vo, schema, buffer);
        } catch (Exception var5) {
            throw var5;
        }
    }

    public T deserialize(byte[] bytes) {
        try {
            Schema<VO> schema = RuntimeSchema.getSchema(VO.class);
            VO vo = (VO)schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes, vo, schema);
            return vo != null && vo.getValue() != null ? vo.getValue() : null;
        } catch (Exception var4) {
            throw var4;
        }
    }
}
