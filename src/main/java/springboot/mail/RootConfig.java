package springboot.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class RootConfig {
//    @Bean
//    public MailSender mailSender(){
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        //指定用来发送Email的邮件服务器主机名
//        mailSender.setHost("smtp.qq.com");
//        //默认端口，标准的SMTP端口
//        mailSender.setPort(587);
//        mailSender.setUsername("jantent@qq.com");
//        mailSender.setPassword("nkadjsazkdygbcgf");
//        return mailSender;
//    }
}
