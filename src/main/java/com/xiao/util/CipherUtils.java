package com.xiao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class CipherUtils {
	private static Logger log = LoggerFactory.getLogger(CipherUtils.class);
	//初始化向量(IV)，aes 16位
    private static final String IV = "0123456789abcdef";

    public static String decrypt_AES(String key,String input) {
    	byte[] result = null;
    	byte[] encryptionKey = null;
        try {
        	encryptionKey = Hex.decodeHex(key.toCharArray());
        }catch (Exception e) {
        	e.printStackTrace();
        }
        try {
        	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey,"AES");
            //向量iv
            byte[] ivs = IV.getBytes("UTF-8");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivs);
            cipher.init(Cipher.DECRYPT_MODE,keySpec,ivParameterSpec);
            byte[] content = Hex.decodeHex(input.toCharArray());
            result = cipher.doFinal(content);  
            return new String(result,"utf-8");
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (InvalidKeyException e) {
            e.printStackTrace();
        }catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }catch (BadPaddingException e) {
            e.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        }
        return null;
    }
    
    /** 
     * 解密 
     * 使用对称密钥进行解密 
     * keyStr 密钥 
     */  
    public static void fileDecodeAES(String keyStr,String filepath) throws Exception{
        //读取保存密钥的文件
    	byte[] key = Hex.decodeHex(keyStr.toCharArray());
        //根据密钥文件构建一个AES密钥
        SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
        //实例化一个密码器CBC模式的
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //初始化密码器
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, new IvParameterSpec(IV.getBytes("UTF-8")));
        List files = getFiles( filepath);
        if(files==null||files.size()==0){
        	return ;
        }
        for(int i=0;i<files.size();i++){
        	String filename = (String)files.get(i);
            //解密文件流
            String fn = filename.substring(0,filename.indexOf("."));
            String suffix =filename.substring(filename.indexOf(".")+1,filename.length());
            if("zip".equalsIgnoreCase(suffix)){
            	String decodePath = fn+File.separator;
            	decompressZip(filename, fn);
            	File dcRoot = new File(decodePath);
        	    File [] dfiles = dcRoot.listFiles();  
        	    for(File f : dfiles){ 
        	    	decodeFile(f.getAbsolutePath(),cipher);
        	    }
            	
            }else{
            	decodeFile(filename,cipher);
            }
        }
    }
    private static void decodeFile(String filename,Cipher cipher){
    	//解密文件流
        String dir = filename.substring(0,filename.lastIndexOf(File.separator));
        String realName = filename.substring(filename.lastIndexOf(File.separator)+1,filename.length());
        String decodePath=dir+File.separator+"decode"+File.separator;
    	//加密文件流  
        try{
	        FileInputStream inputStream = new FileInputStream(filename);
	        File decodeRoot = new File(decodePath);
	        if(!decodeRoot.exists()) {
	        	decodeRoot.mkdirs();
            }
	        File decodeFile = new File(decodePath + realName);
	        decodeFile.createNewFile();
	        FileOutputStream outputStream = new FileOutputStream(decodeFile);
	        //以解密流写出文件  
	        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);  
	        byte[] buffer = new byte[1024];  
	        int r;  
	        while ((r = inputStream.read(buffer)) >= 0) {  
	            cipherOutputStream.write(buffer, 0, r);  
	        }  
	        cipherOutputStream.close();
	        inputStream.close();
	        outputStream.close();
        }catch(Exception e){
        	log.error("解密文件时发生错误："+e.toString());
        }
    }
    
    public static List<String> getFiles(String dir){  
        List<String> lstFiles = null;       
        if(lstFiles == null){  
            lstFiles = new ArrayList<String>();  
        }  
        File file = new File(dir);
        if(!file.isDirectory()){
        	lstFiles.add(file.getAbsolutePath());
        }else{
	        File [] files = file.listFiles();  
	        for(File f : files){  
	            if(f.isDirectory()){  
	                lstFiles.add(f.getAbsolutePath());  
	                lstFiles.addAll(getFiles(f.getAbsolutePath()));  
	            }else{   
	                String str =f.getAbsolutePath();  
	                lstFiles.add(str);  
	            }  
	        } 
        }
        return lstFiles;  
    } 
    
    public static void decompressZip(String zipFilePath, String saveFileDir) {
		if (isEndsWithZip(zipFilePath)) {
			File file = new File(zipFilePath);
			if (file.exists()) {
				InputStream is = null;
				// can read Zip archives
				ZipArchiveInputStream zais = null;
				try {
					saveFileDir = saveFileDir.endsWith(File.separator) ? saveFileDir : saveFileDir + File.separator;
					File saveRoot = new File(saveFileDir);
					// 文件对象创建后，若有重复的，先将其删除，全新创建。
					if (saveRoot.exists()) {
						delFiles(saveFileDir);
					}
					saveRoot.mkdirs();
					is = new FileInputStream(file);
					zais = new ZipArchiveInputStream(is);
					ArchiveEntry archiveEntry = null;
					while ((archiveEntry = zais.getNextEntry()) != null) {
						// 获取文件名
						String entryFileName = archiveEntry.getName();
						// 构造解压出来的文件存放路径
						String entryFilePath = saveFileDir + entryFileName;
						OutputStream os = null;
						try {
							// 把解压出来的文件写到指定路径
							File entryFile = new File(entryFilePath);
							os = new FileOutputStream(entryFile);
							byte[] buffer = new byte[1024 * 5];

							int length = -1;
							while ((length = zais.read(buffer)) != -1) {
								os.write(buffer, 0, length);
							}
							os.flush();
						} catch (IOException e) {
							throw new IOException(e);
						} finally {
							IOUtils.closeQuietly(os);
						}

					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					try {
						if (zais != null) {
							zais.close();
						}
						if (is != null) {
							is.close();
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
    public static void delFiles(String dir){  
        File file = new File(dir);
        if(!file.isDirectory()){
        	file.delete();
        }else{
	        File [] files = file.listFiles();  
	        for(File f : files){  
                String str =f.getAbsolutePath();  
                delFiles(str);  
	        }
	        file.delete();
        }
    }  
    public static boolean isEndsWithZip(String fileName) {
		boolean flag = false;
		if (fileName != null && !"".equals(fileName.trim())) {
			if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
				flag = true;
			}
		}
		return flag;
	}
}
