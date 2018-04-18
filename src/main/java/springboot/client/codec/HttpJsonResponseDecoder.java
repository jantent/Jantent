package springboot.client.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import springboot.server.codec.AbstractHttpJsonDecoder;
import springboot.server.codec.HttpJsonResponse;

import java.util.List;

/**
 * @author tangj
 * @date 2018/4/15 21:48
 */
public class HttpJsonResponseDecoder extends AbstractHttpJsonDecoder<FullHttpResponse> {

    public HttpJsonResponseDecoder(Class<?> clazz) {
        super(clazz);
    }

    public HttpJsonResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, false);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) throws Exception {
        out.add(new HttpJsonResponse(msg,jsonDecode(ctx,msg.content())));
    }
}
