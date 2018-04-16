package springboot.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import springboot.util.GsonUtils;

import java.nio.charset.Charset;

/**
 * @author tangj
 * @date 2018/4/15 18:02
 */
public abstract class AbstractHttpJsonEncoder<T> extends MessageToMessageEncoder<T>{
    final static Charset utf8 = Charset.forName("utf-8");

    protected ByteBuf jsonEncode(ChannelHandlerContext ctx,Object body){
        String jsonStr = GsonUtils.toJsonString(body);
        ByteBuf encodeBuf = Unpooled.copiedBuffer(jsonStr,utf8);
        return encodeBuf;
    }
}

