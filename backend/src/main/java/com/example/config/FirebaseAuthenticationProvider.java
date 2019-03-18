package com.example.config;

import com.example.domain.FirebaseAuthenticationToken;
import com.example.domain.User;
import com.example.repository.UserRepository;
import com.example.service.CustomUserDetailsService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;

public class FirebaseAuthenticationProvider implements AuthenticationProvider  {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationProvider.class);

    private String clientId;

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    public FirebaseAuthenticationProvider(ApplicationContext ctx) {
        this.userRepository = ctx.getBean(UserRepository.class);
        this.customUserDetailsService = ctx.getBean(CustomUserDetailsService.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (!supports(authentication.getClass())) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("This authentication provider does not support instances of type %s", authentication.getClass().getName()));
            }
            return null;
        }

        FirebaseAuthenticationToken firebaseAuthenticationToken = (FirebaseAuthenticationToken) authentication;

        FirebaseToken dataObjectFromToken  = null;
        try {
            dataObjectFromToken = FirebaseAuth.getInstance().verifyIdToken((String) firebaseAuthenticationToken
                    .getCredentials());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }

        // Get profile information from payload
        String email = dataObjectFromToken.getEmail();

        User userFromDB = userRepository.findByEmail(email);
        if (userFromDB == null) {
            User user = new User();
            user.setEmail(dataObjectFromToken.getEmail());
            user.setGoogleID(dataObjectFromToken.getUid());
            userRepository.save(user);
        } else {
            if (userFromDB.getGoogleID() == null) {
                userFromDB.setGoogleID(dataObjectFromToken.getUid());
                userRepository.save(userFromDB);
            }
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        return new FirebaseAuthenticationToken((String) firebaseAuthenticationToken
                .getCredentials(), userDetails,
                firebaseAuthenticationToken.getAuthorities(), authentication.getDetails());
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (FirebaseAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
