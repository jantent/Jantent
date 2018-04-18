package springboot.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springboot.modal.vo.UserVo;
import springboot.server.codec.HttpJsonRequest;

public class ClientHandler extends ChannelInboundHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserVo userVo = new UserVo();
        userVo.setPassword("23456");
        logger.info("发送的User为"+userVo.toString());
        HttpJsonRequest request = new HttpJsonRequest(null,userVo);
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(msg.getClass().getName());
        logger.info("接受到的User对象为："+msg.toString());
    }
}
