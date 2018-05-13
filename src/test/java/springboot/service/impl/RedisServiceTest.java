package springboot.service.impl;

import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import springboot.constant.WebConst;
import springboot.dto.MetaDto;
import springboot.dto.Types;
import springboot.modal.vo.ContentVo;
import springboot.service.ICommentService;
import springboot.service.IContentService;
import springboot.service.IMetaService;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author tangj
 * @date 2018/5/12 19:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private ValueOperations<String,Object> valueOperations;

    @Resource
    private TemplateEngine templateEngine;

    @Autowired
    private IContentService contentService;

    @Autowired
    private ICommentService commentService;

    @Resource
    private IMetaService metaService;

    @Test
    public void test(){
        Context context = new Context();
        PageInfo<ContentVo> articles = contentService.getContents(1, 10);
        List<MetaDto> categories = metaService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
        context.setVariable("categories", categories);
        context.setVariable("articles", articles);
        String html = templateEngine.process("themes/jantent/index",context);
        System.out.println(html);
    }
}