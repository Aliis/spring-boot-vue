package com.example.config;

import com.example.domain.User;
import com.example.repository.UserRepository;
import com.example.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;


public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private UserRepository userRepository;

    public ApiAuthenticationFilter(String defaultFilterProcessesUrl, ApplicationContext ctx) {
        super(defaultFilterProcessesUrl);
        /*https://stackoverflow.com/questions/48107157/autowired-is-null-in-usernamepasswordauthenticationfilter-spring-boot*/
        this.userRepository = ctx.getBean(UserRepository.class);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {

        LoginCredentials loginCredentials;

        try {
            loginCredentials = new ObjectMapper().readValue(request.getInputStream(), LoginCredentials.class);
        } catch(Exception e) {
            throw new BadCredentialsException("");
        }

        User userFromDB = userRepository.findByEmail(loginCredentials.getUserName());
        if (userFromDB == null) {
            User user = new User();
            user.setEmail(loginCredentials.getUserName());
            user.setPassword(loginCredentials.getPassword());
            userRepository.save(user);
        }

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(
                        loginCredentials.getUserName(),
                        loginCredentials.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }
}
