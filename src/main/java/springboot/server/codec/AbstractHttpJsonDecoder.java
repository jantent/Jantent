package springboot.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springboot.util.GsonUtils;

import java.nio.charset.Charset;

/**
 * @author tangj
 * @date 2018/4/15 19:23
 */
public abstract class AbstractHttpJsonDecoder<T> extends MessageToMessageDecoder<T>{

    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpJsonDecoder.class);

    private Class<?> clazz;
    private boolean isPrint;
    private final static Charset UTF_8 = Charset.forName("UTF-8");

    protected AbstractHttpJsonDecoder(Class<?> clazz){
        this(clazz,false);
    }

    protected AbstractHttpJsonDecoder(Class<?> clazz,boolean isPrint){
        this.clazz = clazz;
        this.isPrint = isPrint;
    }

    public Object jsonDecode(ChannelHandlerContext ctx, ByteBuf body){
        String content = body.toString(UTF_8);
        if (isPrint){
            logger.info("the receive body is"+content);
        }
        Object result = GsonUtils.jsonToObject(content,clazz);
        return result;
    }

}
