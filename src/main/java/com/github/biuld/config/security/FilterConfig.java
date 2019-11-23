package com.github.biuld.config.security;

import com.github.biuld.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FilterConfig {

    private UserMapper userMapper;

    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistration() {

        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<JwtFilter>();

        //注入过滤器
        registration.setFilter(new JwtFilter(userMapper));
        //拦截规则
        registration.addUrlPatterns("/auth/*");
        //过滤器名称
        registration.setName("JwtFilter");
        //是否自动注册 false 取消Filter的自动注册
        registration.setEnabled(true);
        //过滤器顺序
        registration.setOrder(1);
        return registration;
    }

}
