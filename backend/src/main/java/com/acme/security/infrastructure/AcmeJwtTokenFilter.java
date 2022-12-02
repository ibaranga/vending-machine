package com.acme.security.infrastructure;

import com.acme.security.domain.AccessToken;
import com.acme.security.domain.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
public class AcmeJwtTokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    public AcmeJwtTokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        getJwtStringFromAuthorizationHeader(request)
                .flatMap(tokenService::parseAccessToken)
                .ifPresent(this::setSecurityContext);

        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtStringFromAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authorizationHeader == null ?
                Optional.empty() :
                Optional.of(authorizationHeader.substring(authorizationHeader.indexOf("Bearer ") + 7).trim());
    }

    private void setSecurityContext(AccessToken token) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String role = "ROLE_%s".formatted(token.role());
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                token.principalId(),
                null,
                List.of(new SimpleGrantedAuthority(role))));
        SecurityContextHolder.setContext(context);
    }
}
