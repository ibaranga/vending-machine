package com.acme.security.domain;


public interface PasswordEncoder {
    String encode(String plainPassword);

    boolean matches(String plainPassword, String encodedPassword);
}
