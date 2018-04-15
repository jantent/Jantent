package springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tangj
 * @date 2018/4/12 22:36
 */
@Component
@ConfigurationProperties(prefix = "nettyserver")
public class NettyServerConfig {
    private String serviceIp;
    private String servicePort;

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }
}
