package springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "httpportcfg")
public class HttpPortCfg {
    private String http_port;

    private String https_port;

    public String getHttp_port() {
        return http_port;
    }

    public void setHttp_port(String http_port) {
        this.http_port = http_port;
    }

    public String getHttps_port() {
        return https_port;
    }

    public void setHttps_port(String https_port) {
        this.https_port = https_port;
    }
}
