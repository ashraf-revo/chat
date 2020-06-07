package org.revo.chat.server.utils;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class UtilTest {
    @Test
    public void testReadEnvFile() {
        assertThat(true, is(true));
        try {
            Env env = Util.loadEnv("config.properties");
            assertThat(env.getPort(), is(9888));
            assertThat(env.getUsers(), Matchers.hasSize(2));
            assertThat(env.getUsers(), everyItem(hasProperty("username", notNullValue())));
            assertThat(env.getUsers(), everyItem(hasProperty("password", notNullValue())));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
