package springboot.service.impl;

import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.modal.vo.ContentVo;
import springboot.service.IContentService;

import static org.junit.Assert.*;

/**
 * @author tangj
 * @date 2018/5/13 17:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentServcieImplTest {

    @Autowired
    private IContentService contentService;


    @Test
    public void  testConten(){
        PageInfo<ContentVo> contentVoPageInfo =  contentService.getContents(1,5);
        System.out.println(contentVoPageInfo);
    }
}