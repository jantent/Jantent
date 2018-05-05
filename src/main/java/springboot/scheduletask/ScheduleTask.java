package springboot.scheduletask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tangj
 * @date 2018/5/2 22:59
 */
@Component
public class ScheduleTask {

    AtomicInteger integer = new AtomicInteger(1);

    @Scheduled(fixedRate = 3000)
    private void process(){
        System.out.println("定时任务启动"+integer.incrementAndGet());
    }
}
