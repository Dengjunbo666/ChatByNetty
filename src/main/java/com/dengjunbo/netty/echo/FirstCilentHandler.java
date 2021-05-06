package com.dengjunbo.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Date;

public class FirstCilentHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date()+":客户端准备写出数据，并等待连接");
        ByteBuf byteBuf = getByteBuf(ctx);
        ctx.channel().writeAndFlush(byteBuf);
        System.out.println(new Date()+"发送数据");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf)msg;
        System.out.println(new Date()+"：客户端接受服务器的数据 ->"+buffer.toString(CharsetUtil.UTF_8));
    }

    private static ByteBuf getByteBuf(ChannelHandlerContext ctx){
        ByteBuf buffer = ctx.alloc().buffer();
        String s = "你好，服务器";
        byte[] bytes = s.getBytes(CharsetUtil.UTF_8);
        buffer.writeBytes(bytes);
        return buffer;
    }
}
