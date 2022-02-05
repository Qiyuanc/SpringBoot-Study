package com.qiyuan.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DruidConfig
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/17 14:15
 * @Version 1.0
 **/
@Configuration
public class DruidConfig {
    /*
       @ConfigurationProperties(prefix = "spring.datasource") 作用就是将 全局配置文件中
       前缀为 spring.datasource.druid的属性值注入到 com.alibaba.druid.pool.DruidDataSource 的同名参数中
     */
    // 其实在 yaml 中可以直接 spring.datasource.druid，我为啥要这样搞？
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    // SpringBoot 内置了 Servlet 容器，这个方法就是在 web.xml 中注册 Servlet 的替代！
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();
        // 设置后台界面的登录账号和密码，key 是固定的
        initParams.put("loginUsername","qiyuanc");
        initParams.put("loginPassword","0723");
        // 设置允许访问的对象
        initParams.put("allow","");
        // 设置禁止访问的对象
        //initParams.put("deny","192.168.1.1");

        bean.setInitParameters(initParams);
        return bean;
    }

    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        // exclusions 设置哪些请求不用过滤，从而不进行统计
        initParams.put("exclusions", "*.js,*.css,/druid/*,/jdbc/*");
        bean.setInitParameters(initParams);
        return bean;
    }
}
