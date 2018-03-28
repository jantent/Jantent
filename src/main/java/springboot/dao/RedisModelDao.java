package springboot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import springboot.modal.vo.RedisModel;

import javax.annotation.Resource;

@Component
public class RedisModelDao {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String,String> valOpsStr;

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    @Resource(name="redisTemplate")
    ValueOperations<Object,Object> valOps;

    public void setString(String key,String value){
        valOpsStr.set(key,value);
    }

    public String getString(String key){
        return valOpsStr.get(key);
    }

    public void save(RedisModel person){
        valOps.set("mystr",person);
    }

    public RedisModel get(){
        return (RedisModel) valOps.get("mystr");
    }
}
