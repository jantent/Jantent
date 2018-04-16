package springboot.server.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author tangj
 * @date 2018/4/15 20:52
 */
public class HttpJsonRequestDecoder extends AbstractHttpJsonDecoder<FullHttpRequest>{

    public HttpJsonRequestDecoder(Class<?> clazz) {
        super(clazz);
    }

    protected HttpJsonRequestDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if (msg.decoderResult().isFailure()){
            sendError(ctx,HttpResponseStatus.BAD_REQUEST);
        }
        HttpJsonRequest request = new HttpJsonRequest(msg,jsonDecode(ctx,msg.content()));
        out.add(request);
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status, Unpooled.copiedBuffer("Failure: " + status.toString()
                + "\r\n", Charset.forName("utf-8")));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
