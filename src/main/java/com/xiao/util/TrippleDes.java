package com.xiao.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;

@Component
public class TrippleDes {

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private static SecretKeyFactory skf;
    private static Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    static SecretKey key;

    public TrippleDes() throws Exception {
        myEncryptionKey = "dsakjfsdkjghsjkdhgksdhgkleshgksldfjhgksdfhgj";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }

    public boolean matches(String password, String hashPassword) {
        String encode = encode(password);
        return encode.equals(hashPassword);
    }

    public static String encode(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }



    public static String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }


    public static void main(String args[]) throws Exception {
        TrippleDes td = new TrippleDes();
//
//        String target = "18368013593";
//        String encrypted = td.encode(target);
        String decrypted = td.decrypt("bbCt84XjTOna5vj0symgvg==");
//        
//        System.out.println("String To Encrypt: " + target);
//        System.out.println("Encrypted String:" + encrypted);
        System.out.println("Decrypted String:" + decrypted);
//        decrypted = td.decrypt("fR9MMuKXbTC4Ak+NZ9G3Yw==");
//        System.out.println("Decrypted String:" + decrypted);
    	String cardNo2 = "330102198004221531";
    	System.out.println(cardNo2.substring(6, 10));
    	System.out.println(cardNo2.substring(10, 12));
    	System.out.println(cardNo2.substring(12, 14));
    	String birthday = cardNo2.substring(6, 11)+"-"+cardNo2.substring(11, 13)+"-"+cardNo2.substring(13, 15);
    }

}