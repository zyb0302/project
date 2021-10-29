package com.school.manager.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author : JACK
 * @create : 2020/2/22 20:57
 * @Description
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    // 多个拦截器组成一个拦截器链
    // addPathPatterns 用于添加拦截规则
    // excludePathPatterns 用户排除拦截

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CommonFilter())//添加拦截器
                .addPathPatterns("/**") //拦截所有请求
                .excludePathPatterns("/", "/index", "/login", "/userLogin","/member-add","/addMarketConfirm","/queryMarketConfirmByUserIdAndPhoneId",
                        "/firstIndex","/addUser","classpath:/resources/**",
                        "/css/**", "/js/**", "/layui/**", "/fonts/**", "/images/**", "/phone/**", "/imgs/**", "/plugin/**");//对应的不拦截的请求
    }
}
