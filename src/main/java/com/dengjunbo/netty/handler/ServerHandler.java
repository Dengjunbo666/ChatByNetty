package com.dengjunbo.netty.handler;

import com.dengjunbo.netty.protocol.Packet;
import com.dengjunbo.netty.protocol.PacketCodeC;
import com.dengjunbo.netty.protocol.request.LoginRequesetPacket;
import com.dengjunbo.netty.protocol.request.MessageRequestPacket;
import com.dengjunbo.netty.protocol.response.LoginResponsePacket;
import com.dengjunbo.netty.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;
        //解码
        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);
        //判断是否是登录请求
        if (packet instanceof LoginRequesetPacket){
            LoginRequesetPacket loginRequesetPacket = (LoginRequesetPacket) packet;
            //登录校验
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());

            if (valid(loginRequesetPacket)){
                System.out.println(new Date()+"-"+loginRequesetPacket.getUsername()+"登录成功");
               loginResponsePacket.setSuccess(true);
            }else {
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
            }
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }else if (packet instanceof MessageRequestPacket){
            //处理消息
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复："+ messageRequestPacket.getMessage());;
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequesetPacket loginRequesetPacket){
        return true;
    }
}
