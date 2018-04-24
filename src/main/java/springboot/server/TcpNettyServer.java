package springboot.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springboot.server.handler.TcpNettyServerInitializer;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author tangj
 * @date 2018/4/12 20:46
 */
public class TcpNettyServer {

    private static final Logger logger = LoggerFactory.getLogger(TcpNettyServer.class);

    private Thread thread = null;

    private int port;

    private String host;


    public TcpNettyServer(String host, int port){
        this.host = host;
        this.port = port;
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void startServer() throws Exception{
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        InetSocketAddress addr = null;

        if ("*".equalsIgnoreCase(host) || "0.0.0.0".equalsIgnoreCase(host)) {
            addr = new InetSocketAddress(port);
        } else {
            addr = new InetSocketAddress(InetAddress.getByName(host),
                    port);
        }
        final InetSocketAddress addr1 = addr;
        final String addrInfo = host+":"+port;
        thread = new Thread(){
            @Override
            public void run(){
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup,workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new TcpNettyServerInitializer());
                try {
                    ChannelFuture channelFuture = bootstrap.bind(addr1).sync();
                    logger.info("netty server start success!{"+addrInfo+"}");
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    logger.error("netty server start [fail] !{"+addrInfo+"}");
                    logger.error(e.getMessage(),e);
                }finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        };
        thread.start();
    }

    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            thread.interrupt();
        }
    }
}
