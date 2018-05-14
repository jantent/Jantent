package springboot.scheduletask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleTaskTest {

    @Resource
    private ScheduleTask scheduleTask;

    @Test
    public void testSendString(){
        scheduleTask.process();
    }
}