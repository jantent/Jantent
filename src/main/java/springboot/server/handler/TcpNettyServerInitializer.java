package springboot.server.handler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import springboot.server.handler.TcpServerHandler;


/**
 * @author tangj
 * @date 2018/4/12 21:16
 */
public class TcpNettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("StringDecoder",new StringDecoder());
        pipeline.addLast("StringEncoder",new StringEncoder());
        pipeline.addLast("handler",new TcpServerHandler());
    }
}
