package org.revo.chat.services.Impl;

import org.revo.chat.services.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5PasswordEncoder implements PasswordEncoder {
    private final MessageDigest md;

    public MD5PasswordEncoder() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("MD5");
    }

    @Override
    public boolean match(String password, String hex) {
        return this.encode(password).equals(hex);
    }

    @Override
    public String encode(String password) {
        md.update(password.getBytes());
        return byteArrayToHex(md.digest()).toUpperCase();
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
