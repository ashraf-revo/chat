package org.revo.chat.services;

import org.revo.chat.server.utils.Env;
import org.revo.chat.server.utils.Util;

import java.io.IOException;

public class EnvLoader {
    public static final Env env = getEnv("config.properties");

    private synchronized static Env getEnv(String file) {
        if (env != null) return env;
        try {
            return Util.loadEnv(file);
        } catch (IOException e) {
            return null;
        }
    }

    public static Env getEnv() {
        return env;
    }
}
