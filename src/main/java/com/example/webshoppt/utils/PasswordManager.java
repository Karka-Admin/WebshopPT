package com.example.webshoppt.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.SecureRandom;

public final class PasswordManager {
    public static boolean checkLogin() {
        return false;
    }

    public static boolean validatePassword(String password, String hashedPassword) {
        String[] hashParts = hashedPassword.split(":");
        int iterations = Integer.parseInt(hashParts[0]);
        byte[] salt = toBytes(hashParts[1]);
        byte[] hash = toBytes(hashParts[2]);
        byte[] enteredHashedPassword = null;
        int diff = 0;

        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            enteredHashedPassword = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();

            diff = hash.length ^ enteredHashedPassword.length;
            for (int i = 0; i < hash.length && i < enteredHashedPassword.length; i++) {
                diff |= hash[i] ^ enteredHashedPassword[i];
            }
        } catch (Exception valErr) {
            valErr.printStackTrace();
        }
        return diff == 0;
    }

    public static String generatePBKDF2WithHmacSHA1Password(String password) {
        int iterations = 1000;
        byte[] salt = generateSalt();
        byte[] hash = null;

        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, 512);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        } catch (Exception genErr) {
            genErr.printStackTrace();
        }

        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    public static String toHex(byte[] array) {
        BigInteger bigInteger = new BigInteger(1, array);
        String hex = bigInteger.toString(16);
        int paddingLength = (array.length * 2) - hex.length();

        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        }
        return hex;
    }

    public static byte[] toBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.nextBytes(salt);
        } catch (Exception saltErr) {
            saltErr.printStackTrace();
        }
        return salt;
    }
}
