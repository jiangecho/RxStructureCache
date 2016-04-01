package com.echo.fans.utils;

import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jiangecho on 16/3/31.
 * refer:http://www.cnblogs.com/coding-way/p/3682813.html
 */
public class AES256Utils {

    private static final String KEY = "Abcd1234";

    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

    public static String decrpt(File file) throws Exception {
        byte[] result = decrypt(FileUtils.readFileToByteArray(file));
        return new String(result);
    }

    public static void encrypt(File source, File encryptedFile) throws Exception {
        if (source == null || !source.exists() || encryptedFile == null) {
            Log.e("jyj", "error: " + source.getName() + " " + encryptedFile.getName());
            return;
        }
        byte[] result = encrypt(FileUtils.readFileToByteArray(source));
        FileUtils.writeByteArrayToFile(encryptedFile, result);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @return
     */
    public static byte[] decrypt(byte[] data) throws Exception {

        Key k = toKey(KEY);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");

        cipher.init(Cipher.DECRYPT_MODE, k);

        return cipher.doFinal(data);
    }

    /**
     * 加密
     *
     * @param data 待加密内容
     * @return
     */
    public static byte[] encrypt(byte[] data) throws Exception {

        Key k = toKey(KEY);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");

        cipher.init(Cipher.ENCRYPT_MODE, k);

        return cipher.doFinal(data);
    }

    private static Key toKey(String key) throws Exception {
        byte[] bytes = new byte[32];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
        byte[] keyBytes = KEY.getBytes();
        for (int i = 0; i < keyBytes.length && i < bytes.length; i++) {
            bytes[i] = keyBytes[i];
        }

        SecretKey secretKey = new SecretKeySpec(bytes, "AES");
        return secretKey;
    }
}
