package com.dengjunbo.netty.serialize;

import com.dengjunbo.netty.serialize.impl.JSONSerializer;

public interface Serializer {
    //json序列化
    byte JSON_SERIALIZER = 1;
    Serializer DEFAULT = new JSONSerializer();
    //序列化算法
    byte getSerializerAlgorithm();
    //java对象转化成二进制
    byte[] serialize(Object object);
    //二进制转换为java对象
    <T> T deserialize(Class<T> clazz,byte[]bytes);
}
