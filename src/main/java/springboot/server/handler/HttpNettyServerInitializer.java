package springboot.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import springboot.modal.vo.UserVo;
import springboot.server.handler.HttpServerHandler;
import springboot.server.codec.HttpJsonRequestDecoder;
import springboot.server.codec.HttpJsonResponseEncoder;

/**
 * @author tangj
 * @date 2018/4/15 15:27
 */
public class HttpNettyServerInitializer extends ChannelInitializer<SocketChannel>{

    /**
     * 服务端编解码器： 获取请求，最终解码为自定义的HttpJsonRequest对象

     (1) HttpRequestDecoder：请求消息解码器，转换为消息对象。

     (2) HttpObjectAggregator: 目的是将多个消息转换为单一的request或者response对象，最终得到的是FullHttpRequest对象

     (3) 需要自定义的解码器HttpJsonRequestDecoder，将FullHttpRequest转换为HttpJsonRequest对象
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
        ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65336));
        ch.pipeline().addLast("json-decoder",new HttpJsonRequestDecoder(UserVo.class));

        ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
        ch.pipeline().addLast("json-encoder",new HttpJsonResponseEncoder());
        ch.pipeline().addLast("handler",new HttpServerHandler());
    }
}
