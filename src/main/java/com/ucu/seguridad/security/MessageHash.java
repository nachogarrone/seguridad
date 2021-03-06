package com.ucu.seguridad.security;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * Created by nachogarrone on 10/27/16.
 */
public class MessageHash {

    public static MessageBuilder encrypt(String plainText, String password) {
        String salt = KeyGenerators.string().generateKey();
        TextEncryptor encryptor = Encryptors.text(password, salt);
        return new MessageBuilder(salt, encryptor.encrypt(plainText));
    }

    public static String decrypt(String encryptedText, String password, String salt) {
        TextEncryptor encryptor = Encryptors.text(password, salt);
        return encryptor.decrypt(encryptedText);
    }

}
