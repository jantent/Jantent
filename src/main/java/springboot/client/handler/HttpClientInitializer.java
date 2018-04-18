package springboot.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import springboot.client.codec.HttpJsonRequestEncoder;
import springboot.client.codec.HttpJsonResponseDecoder;
import springboot.client.handler.ClientHandler;
import springboot.modal.vo.UserVo;


public class HttpClientInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("http-decoder",new HttpResponseDecoder());
        ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
        ch.pipeline().addLast("json-decoder",new HttpJsonResponseDecoder(UserVo.class));

        ch.pipeline().addLast("http-encoder",new HttpRequestEncoder());
        ch.pipeline().addLast("json-encoder",new HttpJsonRequestEncoder());
        ch.pipeline().addLast("handler",new ClientHandler());
    }
}
