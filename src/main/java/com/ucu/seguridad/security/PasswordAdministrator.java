package com.ucu.seguridad.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Properties;

/**
 * Created by nachogarrone on 10/27/16.
 */
public class PasswordAdministrator {

    private static BCryptPasswordEncoder getBCrypt() {
        String seed = getSeedProperty();
        return new BCryptPasswordEncoder(12, new SecureRandom(seed.getBytes()));
    }

    public static String encode(String plainPassword) {
        return getBCrypt().encode(plainPassword);
    }

    public static boolean matches(String plainPassword, String hash) {
        return getBCrypt().matches(plainPassword, hash);
    }

    private static String getSeedProperty() {
        String seed = "myDefaultSeed4321";
        InputStream inputStream;
        try {
            Properties prop = new Properties();
            String propFileName = "application.properties";

            inputStream = BCryptPasswordEncoder.class.getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                System.out.println("property file '" + propFileName + "' not found in the classpath");
            }

            seed = prop.getProperty("seed");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        System.out.println("Seed to use: " + seed);
        return seed;
    }
}
