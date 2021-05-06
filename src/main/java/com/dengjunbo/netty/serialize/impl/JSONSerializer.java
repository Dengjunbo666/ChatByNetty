package com.dengjunbo.netty.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.dengjunbo.netty.serialize.Serializer;
import com.dengjunbo.netty.serialize.SerializerAlogrithm;

public class JSONSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlogrithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
