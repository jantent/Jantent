package springboot.service.netty;

/**
 * @author tangj
 * @date 2018/4/12 20:47
 */
public interface IMsgProcesser {
    String process(String msg) throws Exception;
}
