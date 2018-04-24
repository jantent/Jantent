package springboot.service;

/**
 * @author tangj
 * @date 2018/2/27 22:51
 */
public interface IRedisService {

     boolean set(String key,String value);

     String get(String key);

}
