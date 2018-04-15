package springboot.service.netty.impl;

import org.springframework.stereotype.Service;
import springboot.service.netty.IMsgProcesser;

/**
 * @author tangj
 * @date 2018/4/12 22:53
 */
@Service
public class MsgProcesser implements IMsgProcesser{
    @Override
    public String process(String msg) throws Exception {
        return "服务端接收到消息";
    }
}
