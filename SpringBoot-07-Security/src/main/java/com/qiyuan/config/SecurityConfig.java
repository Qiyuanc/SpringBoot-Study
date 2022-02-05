package com.qiyuan.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/20 13:49
 * @Version 1.0
 **/
// AOP 拦截器
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    // 授权
    protected void configure(HttpSecurity http) throws Exception {
        // 链式编程
        // 请求授权的设置
        http.authorizeRequests()
                .antMatchers("/","/index").permitAll()
                .antMatchers("/level1/**").hasRole("VIP1")
                .antMatchers("/level2/**").hasRole("VIP2")
                .antMatchers("/level3/**").hasRole("VIP3");
        // 没有权限前往登录页面
        http.formLogin()
                .loginPage("/toLogin")
                // 登录需要的参数
                .usernameParameter("username")
                .passwordParameter("password")
                // 登录请求交给 SpringSecurity
                .loginProcessingUrl("/login");
        // 开启注销功能，注销成功回到首页
        http.logout().logoutSuccessUrl("/");
        // 关闭 csrf 防御
        http.csrf().disable();
        // 开启记住我功能
        http.rememberMe().rememberMeParameter("remeber");
    }

    @Override
    // 认证
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.jdbcAuthentication() 从数据库获取认证信息
        // 现在没有数据库，只能从内存中获取认证信息
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("qiyuanc1").password(new BCryptPasswordEncoder().encode("0723")).roles("VIP1")
                .and()
                .withUser("qiyuanc2").password(new BCryptPasswordEncoder().encode("0723")).roles("VIP1","VIP2")
                .and()
                .withUser("qiyuanc3").password(new BCryptPasswordEncoder().encode("0723")).roles("VIP1","VIP2","VIP3");
    }
}
