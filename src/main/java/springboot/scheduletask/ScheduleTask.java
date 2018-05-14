package springboot.scheduletask;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;


/**
 * @author tangj
 * @date 2018/5/2 22:59
 */
@Component
public class ScheduleTask {


    @Scheduled(fixedRate = 86400000)
    private void process(){
        long initm = Runtime.getRuntime().freeMemory();
        System.out.println("空闲内存为："+initm/(1024*1024));
        System.out.println("内存使用率为："+getMemery());

    }

    public static String getMemery() {

        OperatingSystemMXBean osmxb = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long totalvirtualMemory = osmxb.getTotalSwapSpaceSize(); // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        Double compare = (Double) (1 - freePhysicalMemorySize * 1.0 / totalvirtualMemory) * 100;
        String str = compare.intValue() + "%";
        return str;

    }
}
