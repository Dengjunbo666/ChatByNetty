package com.dengjunbo.netty.handler;

import com.dengjunbo.netty.protocol.Packet;
import com.dengjunbo.netty.protocol.PacketCodeC;
import com.dengjunbo.netty.protocol.request.LoginRequesetPacket;
import com.dengjunbo.netty.protocol.response.LoginResponsePacket;
import com.dengjunbo.netty.protocol.response.MessageResponsePacket;
import com.dengjunbo.netty.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date()+":客户端开始登录");

        //创建登录对象
        LoginRequesetPacket loginRequesetPacket = new LoginRequesetPacket();
        loginRequesetPacket.setUserId(UUID.randomUUID().toString());
        loginRequesetPacket.setUsername("dengjunbo");
        loginRequesetPacket.setPassword("1q2w3e4r");
        //编码
        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequesetPacket);

        //写数据
        ctx.channel().writeAndFlush(buffer);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginResponsePacket){
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (loginResponsePacket.isSuccess()){
                LoginUtil.markAsLogin(ctx.channel());
                System.out.println(new Date()+":客户端登录成功");
            }else {
                System.out.println(new Date()+":客户端登录失败，原因："+loginResponsePacket.getReason());
            }
        }else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
        }

    }
}
