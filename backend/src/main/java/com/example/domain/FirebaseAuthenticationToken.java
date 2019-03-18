package com.example.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {
    private String credentials;
    private Object principal;

    public FirebaseAuthenticationToken(String token, Object details) {
        super((Collection)null);
        this.credentials = token;
        setDetails(details);
        setAuthenticated(false);
    }

    public FirebaseAuthenticationToken(String token, Object principal, Collection<? extends GrantedAuthority>
            authorities, Object details) {
        super(authorities);
        this.credentials = token;
        this.principal = principal;
        setDetails(details);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
