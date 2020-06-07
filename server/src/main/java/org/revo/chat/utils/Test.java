package org.revo.chat.utils;

import org.revo.chat.services.Impl.MD5PasswordEncoder;

import java.security.NoSuchAlgorithmException;

public class Test {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        MD5PasswordEncoder md5PasswordEncoder = new MD5PasswordEncoder();

        System.out.println(md5PasswordEncoder.encode("revo"));
        System.out.println(md5PasswordEncoder.encode("ashraf"));

    }
}

