package com.xiao.util;

import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtil {

    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param content    加密的字符串
     * @param encryptKey key值
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes("utf-8"));
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(b);

    }

    /**
     * 解密
     *
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * 随机生成秘钥
     */
    public static void getKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            //要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = byteToHexString(b);
            System.out.println(s);
            System.out.println("十六进制密钥长度为" + s.length());
            System.out.println("二进制密钥的长度为" + s.length() * 4);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法。");
        }
    }

    /**
     * 使用指定的字符串生成秘钥
     */
    public static void getKeyByPass() {
        //生成秘钥
        String password = "testkey";
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            // kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
            //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。
            kg.init(128, new SecureRandom(password.getBytes()));
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = byteToHexString(b);
            System.out.println(s);
            System.out.println("十六进制密钥长度为" + s.length());
            System.out.println("二进制密钥的长度为" + s.length() * 4);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法。");
        }
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
    	try{
    		String str = "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\r\nTT+cy9NhMm1s6Dc05P+r/rlUg3yUmcjoISs=\r\n";
    		System.out.println(decrypt(str, "fd0e324f879f29ddf217bca778b381b6"));
    	 } catch (Exception e) {
           e.printStackTrace();
    	 }
//        String str = "9ad15d5a3401f354a2e227af89163dd40f3d72c9ecc734b1a5f514cd4e8b9b866075b5434c9cffadeb2e5fe4cf101713b5aed986fba21546d9ddd77e332c516c5caae42afdb92dba0feb4b6cfc252721d8def94fdb201e566d2b765c933f7ad4c84711a7c03f99d3b24682ac4ab04db48ac1b9dd5f66cdbb142003f7aaeb827b06c11be33e8fd94d34dc123400e2a21410b893504dfbc41fa0830254eca72db8798cf80a8f8cc440e14966c8d765b80053ce442bd3b68d1effb9524eb5e7cecd219e3aec8073dd6f9cf819ee92cfbd438a98fe25acb865c33f1f88275961573ae20dcac42c448866aa9210857fe45ad4f80be9625ea0e6afd94ff5f35bb6b42d8bdb842d82e331f5798b5446af95cf4b0990fb2d2c66d5a2453333496d105c92d8bc858ab0994b6cd59c7d4983fbe59992d991b1399f646ff5fc4166783f53c755fac02fb4537eed63dd21277abff8682cae9e1a100301fec00ef807a369fffef658431e28c9f0c138533616d8474157d768ce342977fb4b5a030443a9b4ad13451937979d26c50ecec8a24069088a30dc1a9790e6c424554cad06c9fd4abfa2088ad213b0b7c8241f3c3e6d289ed4246cdeba59d1f93686674736ae8fafad83d1e7a2c980505f80ae10c44973849abcbbc6d928d73c58d19f176c3184a89b78fb5b83a63b3e76d47e284b92c8eb4b5254bd168c5294f66d81be2e5142fa0224a6b452a13e920098ca7895fc0b521c053a70a902bb59fec2f910462f269fed171a317550f63de4beabf693b1e15e171d34921f0f454effcb54937a98d89eb5e965683d6cd993b0a53b89e016a7e47146f3fa9ea9d17452fa467bc9599b4ec569bec7c347c19886529b52486d767ca0521e6e024e601f613059b492694736149f5bbc8b048edaa34cf96a4a9784a372bd11ad1088c4f932129649195c63f74e99fd412f629f3b6ff79b4aa11dcf9fbf6531abc177e6547e5e46540551f1a39480";
//        try {
//            byte[] res = Decrypt(str.getBytes(), "$2a$10$OjSNXpOD3SRw.WGkO3CZV.Xda8URRslZQU016OWvcy7Mksb7Y/fiO".getBytes());
//            System.out.println(res);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String keycode = "bbd959e195699b4735da95d39c2e16c5";
//        String str="{\"total\":195,\"limit\":10,\"current\":1,\"listSize\":10,\"list\":[{\"state\":\"0\",\"telentName\":\"张三\",\"age\":\"38\",\"position\":\"首席技术官\",\"education\":\"本科\",\"isLeader\":\"1\",\"returnYear\":\"2021-05-26\",\"comeYear\":\"2021-01-26\",\"contactPhone\":\"13712345678\",\"personSex\":\"男\",\"personNation\":\"中国\",\"personSchool\":\"中国农业科学院\",\"personCompanyBefore\":\"日本负责人\",\"cname\":\"企业名称\",\"isWork\":0,\"cltime\":\"2011-05-26\",\"revenue\":\"3万元\"}]}";
//        try {
//            String res = encrypt(str, keycode);
//            System.out.println("res:" + res);
//
//            String res2 = decrypt(res, keycode);
//            System.out.println("res2:" + res2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //getKey();

    }


}