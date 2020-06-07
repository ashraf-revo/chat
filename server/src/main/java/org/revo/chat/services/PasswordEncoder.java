package org.revo.chat.services;

public interface PasswordEncoder {
    boolean match(String password, String hex);

    String encode(String password);
}
