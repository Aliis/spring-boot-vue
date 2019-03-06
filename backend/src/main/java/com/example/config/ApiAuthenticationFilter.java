package com.example.config;

import com.example.domain.User;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
            //andmebaasi salvestab, aga http://localhost:8088/api/user/1 vastab 401.
            // Sisselogimisel 500 java.lang.IllegalArgumentException: Principal must not be null
            // A see juhtub aint muki.kuki@mail.ee kontoga.
            return null;
        }

        UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(
                            loginCredentials.getUserName(),
                            loginCredentials.getPassword());

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}