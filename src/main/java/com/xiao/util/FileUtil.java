package com.xiao.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
	public static void main(String[] args) {
		String text = buffedInput("D:/planFile/发文/2021/5/24/1621839888542.xml");
		System.out.println(text);
	}
	
	/**
	 * @param fileUrl 文件绝对路径
	 * @return String 字符串
	 */
	public static String buffedInput(String fileUrl) {
		String text = "";
	    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileUrl))) {
	        byte[] bytes = new byte[2048];
	        int fileNumber = 0;
	        while ((fileNumber = bis.read(bytes)) != -1) {
	            text += new String(bytes, 0, fileNumber, "UTF-8");
	        }
	    } catch (IOException e) {
	        throw new RuntimeException("读取文件内容失败");
	    }
	    return text;
	}
	
	/**
	 * 将读取文件的字节转化为字符串
	 *
	 * @param fileUrl 文件绝对路径
	 * @return
	 * @throws IOException
	 */
	public static String fileInput(String fileUrl) {
		String text = "";
	    try {
	        text = new String(Files.readAllBytes(Paths.get(fileUrl)), "UTF-8");
	    } catch (Exception e) {
	        throw new RuntimeException("读取文件内容失败");
	    }
	    return text;
	}
	
	/**
     * 复制文件
     *
     * @param resource
     * @param target
     */
    public static void copyFile(File resource, File target) throws Exception {
        // 输入流 --> 从一个目标读取数据
        // 输出流 --> 向一个目标写入数据

//        long start = System.currentTimeMillis();

        // 文件输入流并进行缓冲
        FileInputStream inputStream = new FileInputStream(resource);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        // 文件输出流并进行缓冲
        FileOutputStream outputStream = new FileOutputStream(target);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        // 缓冲数组
        // 大文件 可将 1024 * 2 改大一些，但是 并不是越大就越快
        byte[] bytes = new byte[1024 * 2];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
        }
        // 刷新输出缓冲流
        bufferedOutputStream.flush();
        //关闭流
        bufferedInputStream.close();
        bufferedOutputStream.close();
        inputStream.close();
        outputStream.close();

//        long end = System.currentTimeMillis();
//
//        System.out.println("耗时：" + (end - start) / 1000 + " s");

    }
}
