package com.xiao.util;

import java.security.MessageDigest;

/**
 * 密码加密工具类
 * 兼容 MD5（用于密码验证）
 */
public class PasswordEncoder {

    private static final String MD5_DEFAULT_PASSWORD_HASH = "e10adc3949ba59abbe56e057f20f883e";

    /**
     * 验证密码（仅支持 MD5）
     *
     * @param rawPassword 原始密码
     * @param encodedPassword 数据库存储的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return false;
        }

        // MD5 格式验证
        if (encodedPassword.length() == 32) {
            String md5Hash = md5(rawPassword);
            return md5Hash.equals(encodedPassword);
        }

        return false;
    }

    /**
     * 使用 MD5 加密密码
     *
     * @param rawPassword 原始密码
     * @return MD5 哈希
     */
    public static String encodeMD5(String rawPassword) {
        return md5(rawPassword);
    }

    /**
     * 检查是否为默认密码 123456 的 MD5 哈希
     *
     * @param encodedPassword 加密后的密码
     * @return 是否为默认密码
     */
    public static boolean isDefaultPassword(String encodedPassword) {
        return MD5_DEFAULT_PASSWORD_HASH.equals(encodedPassword);
    }

    private static String md5(String origin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(origin.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return bytesToHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("MD5 encryption failed", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int n = b < 0 ? b + 256 : b;
            int d1 = n / 16;
            int d2 = n % 16;
            sb.append(hexDigits[d1]).append(hexDigits[d2]);
        }
        return sb.toString();
    }

    private static final char[] hexDigits = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
}
