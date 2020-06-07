package org.revo.chat.services;

import org.junit.Before;
import org.junit.Test;
import org.revo.chat.server.utils.User;
import org.revo.chat.services.Impl.InMemoryUserServiceImpl;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserServiceTest {
    private UserService userService;

    @Before
    public void init() {
        User revo = new User("revo", "5BC44117A33C524C1673456807CF7A3F");
        User ashraf = new User("ashraf", "508924B0EAC2BA101ADA28841C931E44");
        userService = new InMemoryUserServiceImpl(Arrays.asList(revo, ashraf));
    }

    @Test
    public void findByValidUsernameAndValidPasswordMatch() {
        assertThat(userService.findByUsernameAndPasswordMatch("revo", "revo").isPresent(), is(true));
    }

    @Test
    public void findByValidUsernameAndNotValidPasswordMatch() {
        assertThat(userService.findByUsernameAndPasswordMatch("revo", "00000").isPresent(), is(false));
    }
}
