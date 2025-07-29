package com.xiao.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class PdfUtils {
	protected static Logger log = LoggerFactory.getLogger(PdfUtils.class);
	/**
     * 获取不带扩展名的文件名
     */
    public static String getFileNameNoSuffix(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 获取文件扩展名
     */
    public static String getSuffixNameName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    /**
     * File转MultipartFile
     *
     * @param mulFile 文件对象
     * @return Multipart文件对象
     */
    public static File multipartFileToFile(MultipartFile mulFile) throws IOException {
        InputStream ins = mulFile.getInputStream();
        String fileName = mulFile.getOriginalFilename();
        String prefix = getFileNameNoSuffix(fileName) + UUID.randomUUID().toString();
        String suffix = "." + getSuffixNameName(fileName);
        File toFile = File.createTempFile(prefix, suffix);
        OutputStream os = new FileOutputStream(toFile);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return toFile;
    }


    /**
     * 校验pdf文件是否包含js脚本
     **/
    public static boolean containsJavaScript(File file) throws IOException {

        RandomAccessFile is = new RandomAccessFile(file, "r");
        try {
            PDFParser parser = new PDFParser(is);
            parser.parse();
            PDDocument doc = parser.getPDDocument();
            String CosName = doc.getDocument().getTrailer().toString();
            if (CosName.contains("COSName{JavaScript}") || CosName.contains("COSName{JS}")) {
                return true;
            }
        } catch (Exception e) {
            log.error("PDF效验异常：" + e.getMessage());
            return true;
        } finally {
            is.close();
        }
        return false;
    }

}
