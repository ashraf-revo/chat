package org.revo.chat.services.Impl;

import org.revo.chat.server.utils.User;
import org.revo.chat.services.PasswordEncoder;
import org.revo.chat.services.UserService;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class InMemoryUserServiceImpl implements UserService {
    private PasswordEncoder encoder;
    private List<User> users;

    public InMemoryUserServiceImpl(List<User> users) {
        this.users = users;
        try {
            this.encoder = new MD5PasswordEncoder();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findByUsernameAndPasswordMatch(String username, String password) {
        return this.users.stream().filter(it -> it.getUsername().equalsIgnoreCase(username))
                .filter(it -> encoder.match(password, it.getPassword()))
                .findFirst();
    }
}
