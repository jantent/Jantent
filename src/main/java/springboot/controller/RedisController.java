package springboot.controller;

import org.springframework.web.bind.annotation.RestController;
import springboot.service.IRedisService;

import javax.annotation.Resource;

/**
 * @author tangj
 * @date 2018/2/27 23:05
 */
@RestController
public class RedisController {

    @Resource
    private IRedisService redisService;

    private String redisGet() {
        String name = redisService.get("name");
        return name;
    }
}
