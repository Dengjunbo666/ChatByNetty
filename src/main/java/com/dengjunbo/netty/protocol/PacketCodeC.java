package com.dengjunbo.netty.protocol;

import com.dengjunbo.netty.protocol.command.Command;
import com.dengjunbo.netty.protocol.request.LoginRequesetPacket;
import com.dengjunbo.netty.protocol.request.MessageRequestPacket;
import com.dengjunbo.netty.protocol.response.LoginResponsePacket;
import com.dengjunbo.netty.protocol.response.MessageResponsePacket;
import com.dengjunbo.netty.serialize.Serializer;
import com.dengjunbo.netty.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

public class PacketCodeC {
    private static final int MAGIC_NUMBER = 0x12345678;
    private  final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private  final Map<Byte, Serializer> serializerMap;
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    public PacketCodeC() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequesetPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm() , serializer);
    }



    public ByteBuf encode(ByteBufAllocator byteBufAllocator,Packet packet){
        //1.创建ByteBuf对象
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        //2.序列化java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        //3.实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }
    public Packet decode(ByteBuf byteBuf){
        //跳过魔数
        byteBuf.skipBytes(4);
        //跳过版本号
        byteBuf.skipBytes(1);
        //序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        //指令
        byte command = byteBuf.readByte();
        //数据包长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }
    private Serializer getSerializer(byte serializeAlgorithm) {

        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }
}
