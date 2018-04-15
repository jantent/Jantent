package springboot.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;

/**
 * @author tangj
 * @date 2018/4/15 16:46
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务端收到消息");
    }

    private void writeResponse(HttpObject currentObj, ChannelHandlerContext ctx, ByteBuf byteBuf){

    }
}
