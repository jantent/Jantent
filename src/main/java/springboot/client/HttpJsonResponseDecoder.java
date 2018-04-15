package springboot.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import springboot.server.handler.codec.AbstractHttpJsonDecoder;
import springboot.server.handler.codec.HttpJsonResponse;

import java.util.List;

/**
 * @author tangj
 * @date 2018/4/15 21:48
 */
public class HttpJsonResponseDecoder extends AbstractHttpJsonDecoder<FullHttpResponse>{

    protected HttpJsonResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, false);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) throws Exception {
        out.add(new HttpJsonResponse(msg,jsonDecode(ctx,msg.content())));
    }
}
