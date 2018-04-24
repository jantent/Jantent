package springboot.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
 *
 * @author tangj
 * @date 2018/4/24 23:16
 */
@Component
public class RedisPoolFactory {

    @Autowired
    private RedisConfig redisConfig;

    @Bean
    public JedisPoolConfig getRedisConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getMaxIdle());
        poolConfig.setMaxWaitMillis(redisConfig.getMaxWait());
        poolConfig.setMinIdle(redisConfig.getMinIdle());
        poolConfig.setMaxIdle(redisConfig.getMaxTotal());
        return poolConfig;
    }

    /**
     * Jedis配置
     *
     * @return
     */
    @Bean
    public JedisPool JedisPoolBean() {
        JedisPoolConfig jedisPoolConfig = getRedisConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout());
        return jedisPool;
    }
}
