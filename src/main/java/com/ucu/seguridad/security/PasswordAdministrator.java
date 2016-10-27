package com.ucu.seguridad.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by nachogarrone on 10/27/16.
 */
public class PasswordAdministrator {
    private static BCryptPasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(8);
    }

    public static String encode(String plainPassword) {
        return getBCrypt().encode(plainPassword);
    }

    public static boolean matches(String plainPassword, String hash) {
        return getBCrypt().matches(plainPassword, hash);
    }
}
