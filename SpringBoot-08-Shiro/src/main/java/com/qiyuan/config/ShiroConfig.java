package com.qiyuan.config;

import org.apache.catalina.User;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/22 13:44
 * @Version 1.0
 **/
@Configuration
public class ShiroConfig {
    // Shiro 需要的三个关键对象

    // 3. ShiroFilterFactoryBean 对应 Subject
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        // 创建 ShiroFilterFactoryBean 对象
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 和 SecurityManager 关联起来
        bean.setSecurityManager(securityManager);
        /* 权限类别
        *  anon：无需认证即可访问
        *  authc：必须认证才可访问
        *  user：必须有 记住我 功能才能访问
        *  perms:拥有对某个资源的权限才能访问
        *  roles：拥有某个角色权限才能访问
        * */
        // 设置内置过滤器
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 和 Druid 监控设置一样，在 Map 中设置好权限
        filterMap.put("/user/add","authc");
        filterMap.put("/user/update","authc");
        // 也可以使用通配符
        // filterMap.put("/user/*","authc");

        // 添加授权管理
        filterMap.put("/user/add","perms[user:add]");

        bean.setFilterChainDefinitionMap(filterMap);
        // 无权限前往登录页，同 SpringSecurity！
        bean.setLoginUrl("/toLogin");
        // 未授权前往 /unauth
        bean.setUnauthorizedUrl("/unauth");

        return bean;
    }

    // 2. DefaultWebSecurityManager 对应 SecurityManager
    @Bean(name = "securityManager")
    // 将 SecurityManager 也交给 Spring 管理
    // @Qualifier 相当于形参上的 @Autowired
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        // 创建 SecurityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 和 Realm 关联起来，通过参数让 Spring 把 Realm 对象放进来
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    // 1. 创建 Realm 对象 需要自定义类 对应 Reaml
    @Bean
    // @Bean 告诉 Spring，可以从这个方法中获取到一个对象，对象名就是方法名
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
