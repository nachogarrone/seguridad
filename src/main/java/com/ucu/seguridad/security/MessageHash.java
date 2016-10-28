package com.ucu.seguridad.security;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * Created by nachogarrone on 10/27/16.
 */
public class MessageHash {
    public static String encode(String text, String key) {
        return getEncrypt(key).encrypt(text);
    }

    public static String decode(String text, String key) {
        return getEncrypt(key).decrypt(text);
    }

    private static TextEncryptor getEncrypt(String key) {
        return Encryptors.queryableText("1234567812345678", "5c0744940b5c369b");
    }

}
