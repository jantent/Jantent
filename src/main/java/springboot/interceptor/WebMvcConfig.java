package springboot.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springboot.util.MyUtils;

import javax.annotation.Resource;

/**
 * @author tangj
 * @date 2018/1/22 20:50
 */
@Component
public class WebMvcConfig extends WebMvcConfigurerAdapter{
    @Resource
    private BaseInterceptor baseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ MyUtils.getUploadFilePath()+"upload/");
        super.addResourceHandlers(registry);
    }
}
