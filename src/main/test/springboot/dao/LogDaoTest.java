package springboot.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.modal.vo.LogVo;
import springboot.service.ILogService;
import springboot.util.DateKit;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogDaoTest {

    @Autowired
    private JavaMailSender mailSender;

    @Resource
    private ILogService logService;
    @Test
    public void sendMail(){
        // 消息构造器
        SimpleMailMessage message = new SimpleMailMessage();
        // 发件人
        message.setFrom("jantent@qq.com");
        message.setTo("jantent@qq.com");//收件人
        message.setSubject("发送日志");//主题
        List<LogVo> logVoList = logService.getLogs(1,2);
        message.setText(logMessage(logVoList));//正文
        mailSender.send(message);
        System.out.println("邮件发送完毕");
    }



    @Test
    public void getLog(){
        List<LogVo> logVoList = logService.getLogs(1,2);
        System.out.println(logMessage(logVoList));
    }

    public String logMessage(List<LogVo> logVoList){
        StringBuffer logBuffer = new StringBuffer();
        for (LogVo logVo:logVoList){
            logBuffer.append(" IP为:"+logVo.getIp());
            logBuffer.append(" 操作为： "+logVo.getAction());
            logBuffer.append(" 时间为： "+ DateKit.formatDateByUnixTime(logVo.getCreated())).append("\n");

        }
       return logBuffer.toString();
    }
}