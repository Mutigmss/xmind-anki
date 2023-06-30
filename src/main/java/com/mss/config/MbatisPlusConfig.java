package com.mss.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.mss.mapper")
@Configuration
public class MbatisPlusConfig {

    /**
     * 3.4.0之后版本
     * @return
     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor(){
//        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//        return mybatisPlusInterceptor;
//
//    }
    @Bean
    public MybatisPlusInterceptor getMybatisPlusInterceptor() {

//        1.定义mybatisplus拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

//        2.添加具体拦截器,分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

}