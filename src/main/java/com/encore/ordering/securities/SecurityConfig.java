package com.encore.ordering.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity //스프링 시큐리티 커스텀 설정
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
{

    private final JwtAuthFilter authFilter;

    public SecurityConfig(JwtAuthFilter authFilter)
    {
        this.authFilter = authFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
                .csrf().disable()
                .cors().and() //CORS 활성화
                .httpBasic().disable()
                .authorizeRequests()
                    .antMatchers("/member/create", "/doLogin", "/items", "/item/*/image")
                        .permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션사용 안함
                .and()
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
