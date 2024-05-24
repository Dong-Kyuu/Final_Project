package com.example.jhta_3team_finalproject;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.example.jhta_3team_finalproject.security.CustomAccessDeniedHandler;
import com.example.jhta_3team_finalproject.security.CustomUserDetailsService;
import com.example.jhta_3team_finalproject.security.LoginFailHandler;
import com.example.jhta_3team_finalproject.security.LoginSuccessHandler;

@EnableWebSecurity // 스프링과 시큐리티 결합
@Configuration // 스프링 IOC Container 에게 해당 클래스를 Bean 구성 Class임을 알려주는 것 입니다
public class secuityConfig {
    private DataSource dataSource;
    private LoginFailHandler loginFailHandler;
    private LoginSuccessHandler loginSuccessHandler;
    private CustomUserDetailsService customUserDetailsService;

    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public secuityConfig(LoginFailHandler loginFailHandler,
                         LoginSuccessHandler loginSuccessHandler,
                         DataSource dataSource,
                         CustomUserDetailsService customUserDetailsService,
                         CustomAccessDeniedHandler customAccessDeniedHandler) {

        this.loginSuccessHandler=loginSuccessHandler;
        this.loginFailHandler=loginFailHandler;
        this.dataSource=dataSource;
        this.customUserDetailsService=customUserDetailsService;
        this.customAccessDeniedHandler=customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 내가 만든 페이지로 이동합니다
        http.formLogin((fo) -> fo.loginPage("/user/login")
                .loginProcessingUrl("/user/loginProcess")
                .usernameParameter("id")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailHandler));





        http.logout((fo) -> fo.logoutSuccessUrl("/user/login")
                .logoutUrl("/user/logout")
                .invalidateHttpSession(true)
                .deleteCookies("remember-me","JSESSION_ID"));



        http.rememberMe((me) -> me.rememberMeParameter("remember-me")
                .userDetailsService(customUserDetailsService)
                .tokenValiditySeconds(2419200)
                .tokenRepository(tokenRepository())); //데이터 베이스에 토큰을 저장합니다


        http.authorizeHttpRequests(
                (au)->au
                        .requestMatchers("/user/list", "/user/info","/user/delete")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user/update")
                        .hasAnyAuthority("ROLE_ADMIN","ROLE_MEMBER")
                        .requestMatchers("/board/**","/comment/**")
                        .hasAnyAuthority("ROLE_ADMIN","ROLE_MEMBER")
                        .requestMatchers("/**").permitAll()
        );
        http.exceptionHandling((ex)->ex.accessDeniedHandler(customAccessDeniedHandler));


        return http.build();
    }



    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        //PersistentTokenRepository 의 구현체인 JdbcTokenRepositoryImpl 클래스 사용합니다
        JdbcTokenRepositoryImpl jdbcTokenRepository =new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);//import javax.sql.DataSource;
        return jdbcTokenRepository;
    }
}
