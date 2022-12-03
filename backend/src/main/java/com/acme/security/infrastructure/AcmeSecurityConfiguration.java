package com.acme.security.infrastructure;

import com.acme.security.domain.SecurityService;
import com.acme.security.domain.TokenRepository;
import com.acme.security.domain.TokenService;
import com.acme.user.domain.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class AcmeSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AcmeJwtTokenFilter acmeJwtTokenFilter) throws Exception {
        return http
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry
                        .requestMatchers(HttpMethod.GET, "/product").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user", "/user/login", "/user/refresh", "/user/logout", "/user/logout/all").permitAll()
                        .requestMatchers("/product").hasRole(UserRole.SELLER.getId())
                        .requestMatchers("/vendingmachine").hasRole(UserRole.BUYER.getId())
                        .anyRequest().authenticated()
                )
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(acmeJwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityService securityService(TokenService tokenService, TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        return new SecurityService(tokenService, tokenRepository, new PasswordEncoderImpl(passwordEncoder));
    }
}
