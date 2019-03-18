package com.example.config;

import com.example.config.handlers.*;
import com.example.service.CustomUserDetailsService;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.example.repository.UserRepository;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import javax.servlet.Filter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new FirebaseAuthenticationProvider(getApplicationContext()));
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/logout").authenticated()
                .antMatchers("/api/user").authenticated();

        httpSecurity.exceptionHandling().authenticationEntryPoint(new ApiEntryPoint());
        httpSecurity.exceptionHandling().accessDeniedHandler(new ApiAccessDeniedHandler());
        httpSecurity.logout().logoutUrl("/api/logout").logoutSuccessHandler(new ApiLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
        httpSecurity.addFilterAfter(restLoginFilter("/api/login"), LogoutFilter.class);
        httpSecurity.addFilterAfter(fireBaseLoginFilter("/api/login/firebase"), LogoutFilter.class);
    }

    private Filter restLoginFilter(String url) throws Exception {
        ApiAuthenticationFilter filter = new ApiAuthenticationFilter(url, getApplicationContext());

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new ApiAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new ApiAuthFailureHandler());
        return filter;
    }

    private Filter fireBaseLoginFilter(String url) throws Exception {
        FirebaseAuthenticationFilter filter = new FirebaseAuthenticationFilter(url);

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new ApiAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new ApiAuthFailureHandler());
        return filter;
    }
}