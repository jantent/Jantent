package springboot.service;

/**
 * @author tangj
 * @date 2018/2/27 22:51
 */
public interface IRedisService {

    public boolean set(String key,String value);

    public String get(String key);

}
