package com.lyhorng.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for encryption and decryption operations
 * Note: For production use, consider using Spring Security's encryption utilities
 */
public class CipherUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 10000;

    private CipherUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Generate a random secure key
     */
    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    /**
     * Hash a password using PBKDF2
     */
    public static String hashPassword(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                Base64.getDecoder().decode(salt),
                ITERATIONS,
                KEY_LENGTH
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Generate a random salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Verify a password against a hash
     */
    public static boolean verifyPassword(String password, String salt, String hash) {
        String newHash = hashPassword(password, salt);
        return newHash.equals(hash);
    }

    /**
     * Encrypt a string using AES
     */
    public static String encrypt(String plainText, String secretKey) {
        try {
            SecretKey key = new SecretKeySpec(
                Base64.getDecoder().decode(secretKey),
                ALGORITHM
            );
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    /**
     * Decrypt an encrypted string
     */
    public static String decrypt(String encryptedText, String secretKey) {
        try {
            SecretKey key = new SecretKeySpec(
                Base64.getDecoder().decode(secretKey),
                ALGORITHM
            );
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            byte[] decryptedBytes = cipher.doFinal(
                Base64.getDecoder().decode(encryptedText)
            );
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}

