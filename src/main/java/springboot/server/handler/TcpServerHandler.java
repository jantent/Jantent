package springboot.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author tangj
 * @date 2018/4/12 21:42
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<String>{

    private static final Logger logger = LoggerFactory.getLogger(TcpServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info(msg);
        String resp = "服务端已收到消息"+new Date().getTime();
        if (resp!=null){
            ctx.writeAndFlush(resp);
        }else {
            ctx.flush();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("执行数据操作失败",cause);
    }
}
