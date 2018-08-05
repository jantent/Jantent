package springboot;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import springboot.config.HttpPortCfg;
import springboot.config.NettyServerConfig;

import javax.annotation.Resource;
import javax.sql.DataSource;


@SpringBootApplication
@EnableScheduling
@MapperScan("springboot.dao")
public class StartApplication {

    @Resource
    private HttpPortCfg httpPortCfg;

    @Resource
    private NettyServerConfig nettyConfig;

    public static void main(String[] args) throws Exception {

        SpringApplication app = new SpringApplication(StartApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

    }


    // datasource注入
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    //mybatis SQLSession注入
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }


    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.setSessionTimeout(60 * 60 * 12);//单位为S
            }
        };
    }

    //事务支持
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

//    // 配置8080端口自动转向8443
//    @Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(Integer.valueOf(httpPortCfg.getHttp_port()));
//        connector.setSecure(false);
//        connector.setRedirectPort(Integer.valueOf(httpPortCfg.getHttps_port()));
//
//        tomcat.addAdditionalTomcatConnectors(connector);
//        return tomcat;
//    }

}
