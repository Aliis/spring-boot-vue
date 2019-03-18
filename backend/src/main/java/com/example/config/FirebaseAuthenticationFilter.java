package com.example.config;

import com.example.domain.FirebaseAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FirebaseAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final long serialVersionUID = 1L;

    public FirebaseAuthenticationFilter(String
            defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        FirebaseCredentials firebaseCredentials;
        try {
            firebaseCredentials = new ObjectMapper().readValue(request.getInputStream(), FirebaseCredentials.class);
        } catch(Exception e) {
            throw new BadCredentialsException("");
        }

        if (firebaseCredentials == null) {
            return null;
        }

        Object details = this.authenticationDetailsSource.buildDetails(request);
        FirebaseAuthenticationToken authRequest = new FirebaseAuthenticationToken(firebaseCredentials.getUid(), details);
        Authentication authResult = getAuthenticationManager().authenticate(authRequest);
        return authResult;
    }
}
