package springboot.scheduletask;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import springboot.modal.vo.LogVo;
import springboot.service.ILogService;
import springboot.service.IMailService;
import springboot.service.IMetaService;
import springboot.util.DateKit;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.util.List;


/**
 * @author tangj
 * @date 2018/5/2 22:59
 */
@Component
public class ScheduleTask {

    @Resource
    ILogService logService;

    @Resource
    IMailService mailService;

    @Value("${spring.mail.username}")
    private String mailTo;

    // 延时8小时启动，定时24个小时启动
    @Scheduled(fixedRate = 86400000)
    public void process(){
        StringBuffer result = new StringBuffer();
        long totalMemory = Runtime.getRuntime().totalMemory();
        result.append("使用的总内存为："+totalMemory/(1024*1024)+"MB").append("\n");
        List<LogVo> logVoList = logService.getLogs(0,5);
        for (LogVo logVo:logVoList){
            result.append(" 时间: ").append(DateKit.formatDateByUnixTime(logVo.getCreated()));
            result.append(" 操作: ").append(logVo.getAction());
            result.append(" IP： ").append(logVo.getIp()).append("\n");
        }
        mailService.sendSimpleEmail(mailTo,"博客系统运行情况",result.toString());
    }

}
