package springboot.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import springboot.client.handler.HttpClientInitializer;

import java.net.InetSocketAddress;

public class HttpClient {
    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new HttpClientInitializer());

            // 发起异步连接
            ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 3560));
            // 当客户端链路关闭
            future.channel().closeFuture().sync();
        }finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String args[])throws Exception{
        new HttpClient().start();
    }
}
