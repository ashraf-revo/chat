package org.revo.chat.services;

import org.revo.chat.server.utils.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsernameAndPasswordMatch(String username,String password);
}
