package org.revo.chat.services;

import org.junit.Before;
import org.junit.Test;
import org.revo.chat.services.Impl.MD5PasswordEncoder;

import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PasswordEncoderTest {
    private PasswordEncoder encoder;

    @Before
    public void init() {
        try {
            encoder = new MD5PasswordEncoder();
        } catch (NoSuchAlgorithmException e) {
            assertThat(e, nullValue());
        }
    }

    @Test
    public void whenUsingHashedPassword() {
        assertThat(encoder.match("revo", "5BC44117A33C524C1673456807CF7A3F"), is(true));
    }

    @Test
    public void whenUsingTextPassword() {
        assertThat(encoder.match("revo", "revo"), is(false));
    }

    @Test
    public void whenEncodeMD5Mach() {
        assertThat(encoder.encode("revo"), is("5BC44117A33C524C1673456807CF7A3F"));
    }
    @Test
    public void whenEncodeMD5NotMach() {
        assertThat(encoder.encode("revo"), not("revo"));
    }
}
