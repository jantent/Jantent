package springboot.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.dto.Types;
import springboot.service.IOptionService;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OptionServiceImplTest {

    @Resource
    IOptionService optionService;

    @Test
    public void testInsertOption(){
        optionService.insertOption(Types.BLOCK_IPS.getType(),"123");
    }
}