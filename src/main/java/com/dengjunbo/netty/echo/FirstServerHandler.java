package com.dengjunbo.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Date;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        String s = "恭喜"+ctx.name()+"连接服务器成功";
        byteBuf.writeBytes(s.getBytes(CharsetUtil.UTF_8));
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        System.out.println(new Date()+ ":服务器读到的数据 ->"+buffer.toString(CharsetUtil.UTF_8));

        System.out.println(new Date()+":服务端写出数据");
        ByteBuf byteBuf = getByteBuf(ctx);
        ctx.channel().writeAndFlush(byteBuf);
    }
    private static ByteBuf getByteBuf(ChannelHandlerContext ctx){
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] bytes = "你好，客户端".getBytes(CharsetUtil.UTF_8);
        buffer.writeBytes(bytes);
        return buffer;
    }
}
