package com.xiao.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlDownload {

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String savePath,String fileName) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为15秒
        conn.setConnectTimeout(15*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            boolean flag = saveDir.mkdirs();
            System.out.println("生成文件夹："+flag);
        }
        //String fileName = saveDir.getName(); //下载的文件名
        File file = new File(saveDir+ File.separator+fileName+".pdf");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
