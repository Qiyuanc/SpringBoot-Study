package com.qiyuan.config;

import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @ClassName MyLocaleResolver
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/10/4 16:34
 * @Version 1.0
 **/
public class MyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 获取请求中的语言参数
        String language = request.getParameter("lang");
        // 创建一个默认的 locale 对象，没有设置就使用默认的
        Locale locale = Locale.getDefault();
        // 如果获取到了 国际化语言的参数
        if(!StringUtils.isEmpty(language)){
            // 将 zh_CN 分割为语言和地区
            String[] split = language.split("_");
            // 用 语言 和 地区 参数创建 locale 对象
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
