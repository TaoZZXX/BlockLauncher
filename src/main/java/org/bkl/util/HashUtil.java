package org.bkl.util;

import org.bkl.log.Logger;
import org.bkl.log.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    private static final Logger log = LoggerFactory.getLogger(HashUtil.class.getName());
    private static final int BUFFER_SIZE = 8 * 1024;

    public static String computeHash(Path file, String algorithm) throws IOException, NoSuchAlgorithmException {
        if (file == null) {
            throw new IllegalArgumentException("file path is null");
        }
        if (algorithm == null || algorithm.isBlank()) {
            throw new IllegalArgumentException("algorithm is null or blank");
        }
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (InputStream in = Files.newInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = in.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        return bytesToHex(digest.digest());
    }

    public static boolean verifyHash(Path file, String expectedHex, String algorithm) {
        if (file == null) {
            log.info("verifyHash: file path is null");
            return false;
        }
        if (!Files.exists(file)) {
            log.info("verifyHash: file does not exist: " + file);
            return false;
        }
        if (expectedHex == null || expectedHex.isBlank()) {
            log.info("verifyHash: expected hash is null or blank");
            return false;
        }
        try {
            String actual = computeHash(file, algorithm);
            return actual.equalsIgnoreCase(expectedHex.trim());
        } catch (NoSuchAlgorithmException e) {
            log.info("verifyHash: unsupported algorithm " + algorithm + " - " + e.getMessage());
        } catch (IOException e) {
            log.info("verifyHash: io error - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("verifyHash: " + e.getMessage());
        }
        return false;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}