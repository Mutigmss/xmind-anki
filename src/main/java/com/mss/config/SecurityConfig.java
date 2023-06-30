package com.mss.config;

import com.mss.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("mp").password("{noop}123456").roles("USER");
//    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public JwtAuthenticationFilter jwtAuthenticationFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()

                //不通过Session获取SecurityContext，前后端分离项目不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录界面获取验证码接口   允许匿名访问
//                .antMatchers("/captcha").anonymous()
//                .antMatchers("/system/user/hello").anonymous()
                // 对登录界面进行放行
//                .antMatchers("/system/user/login").permitAll()
//                .antMatchers("/system/user/perms/*").permitAll()
                .antMatchers("/**").permitAll()
//                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

//                  在校验用户名与密码前校验jwt,token令牌
                  http.addFilterBefore(jwtAuthenticationFilter,
                          UsernamePasswordAuthenticationFilter.class);
                // 允许跨域
                http.cors();
    }



}
