package springboot.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import springboot.dao.UserVoMapper;
import springboot.modal.vo.UserVo;
import springboot.server.codec.HttpJsonRequest;
import springboot.server.codec.HttpJsonResponse;

import javax.annotation.Resource;

/**
 * @author tangj
 * @date 2018/4/15 16:46
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpJsonRequest>{

    @Resource
    private UserVoMapper userDao;

    private static final  Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpJsonRequest msg) throws Exception {
        HttpRequest request = msg.getRequest();
        UserVo user = (UserVo)msg.getBody();
        userDao.insert(user);
        logger.info("服务端收到消息"+user);
        ctx.writeAndFlush(new HttpJsonResponse(null,user));
    }


}
