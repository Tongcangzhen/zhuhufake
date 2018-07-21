package xin.tongcangzhen.zhihufake.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xin.tongcangzhen.zhihufake.Interceptor.LoginStatusInterceptor;
import xin.tongcangzhen.zhihufake.Interceptor.PassportInterceptor;

@Component
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginStatusInterceptor loginStatusInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginStatusInterceptor).addPathPatterns("/user/*");
    }
}
