package cn.powerr.mamabike;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MamabikeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MamabikeApplication.class, args);
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
        HttpMessageConverter<?> converters = fastConvert;
        return new HttpMessageConverters(converters);
    }
}
