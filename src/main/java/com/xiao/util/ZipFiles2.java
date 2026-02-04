package com.xiao.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 压缩算法类 实现文件压缩，文件夹压缩，以及文件和文件夹的混合压缩
 * 
 * @author ljheee
 * 
 */
public class ZipFiles2 {

	/**
	 * 完成的结果文件--输出的压缩文件
	 */
	File targetFile;

	public ZipFiles2() {
	}

	public ZipFiles2(File target) {
		targetFile = target;
		if (targetFile.exists())
			targetFile.delete();
	}

	/**
	 * 压缩文件
	 * 
	 * @param srcfile
	 */
	public void zipFiles(File srcfile) {

		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(targetFile));

			if (srcfile.isFile()) {
				zipFile(srcfile, out, "");
			} else {
				File[] list = srcfile.listFiles();
				for (int i = 0; i < list.length; i++) {
					compress(list[i], out, "");
				}
			}
			// 调用删除文件
			delAllFiles(srcfile, null);
			// System.out.println("压缩完毕");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes("utf-8"));
        gzip.close();
        return out.toString("ISO-8859-1");
    }
	/**
	 * 压缩文件夹里的文件 起初不知道是文件还是文件夹--- 统一调用该方法
	 * 
	 * @param file
	 * @param out
	 * @param basedir
	 */
	private void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			this.zipDirectory(file, out, basedir);
		} else {
			this.zipFile(file, out, basedir);
		}
	}

	/**
	 * 压缩单个文件
	 * 
	 * @param srcfile
	 */
	public void zipFile(File srcfile, ZipOutputStream out, String basedir) {
		if (!srcfile.exists())
			return;

		byte[] buf = new byte[1024];
		FileInputStream in = null;

		try {
			int len;
			in = new FileInputStream(srcfile);
			out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			// targetFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.closeEntry();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 压缩文件夹
	 * 
	 * @param dir
	 * @param out
	 * @param basedir
	 */
	public void zipDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	public static void delAllFiles(File file, String singal) {
		if (!file.exists()) {
			// System.out.println("不存在该路径: "+file);
			return;
		}
		if (singal == null || "".equals(singal)) {
			singal = "-";
		}
		// System.out.println(singal+"目录 ["+file.getName()+"]中：");
		singal = singal + " -";
		File[] files = file.listFiles();
		if (files.length > 0) {
			for (File f : files) {
				if (f.isDirectory()) {// 如果是目录
					delAllFiles(f, singal); // 递归
					// System.out.println(singal+"目录 ["+f.getName()+"]已删除");
					f.delete(); // 删除该文件夹
				} else {
					// System.out.println(singal+" 文件《"+f.getName()+"》已删除");
					f.delete();
				} // else
			} // for
			file.delete();
		} // if
		else {
			// System.out.println("***该目录中无任何文件***");
		}
	}// static

	/**
	 * @param desFile
	 * @param fileList
	 * @return
	 */
	public static String getSubFiles(String desFile) {
		File file = new File(desFile);
		File[] files = file.listFiles();
		for (File fileIndex : files) {
			if (!fileIndex.exists()) {
				return "";
			} else if (fileIndex.isFile()) {
				if (fileIndex.getName().toLowerCase().endsWith(".xls") || fileIndex.getName().toLowerCase().endsWith(".xlsx")) {
					return fileIndex.getName();
				}
			} else {
				if (fileIndex.isDirectory()) {
					getSubFiles(fileIndex.getAbsolutePath());
				}
			}
		}
		return "";
	}

	
	 //根据value值获取到对应的一个key值
   public static String getKey(Map<String, String> jpxhmap,String value){
       String key = null;
       //Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
       for(String getKey: jpxhmap.keySet()){
           if(jpxhmap.get(getKey).equals(value)){
               key = getKey;
           }
       }
       return key;
       //这个key肯定是最后一个满足该条件的key.
   }
   /**
    * 判断是否为数字
    * @param str
    * @return
    */
   public static boolean isNumber(String str) {  
		Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]+)?$");
		if (str.indexOf(".") > 0) {// 判断是否有小数点
			if (str.indexOf(".") == str.lastIndexOf(".") && str.split("\\.").length == 2) { // 判断是否只有一个小数点
				return pattern.matcher(str.replace(".", "")).matches();
			} else {
				return false;
			}
		} else {
			return pattern.matcher(str).matches();
		}
   }
   
   public static boolean isInteger(String str) {  
       Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
       return pattern.matcher(str).matches();  
   }
	
   
	public static String unZipfile(String path){
		String fileName ="";
		String ourDirPath=path.substring(0,path.lastIndexOf("/"));
		File outDir = new File(ourDirPath);
	    if(!outDir.exists())     
	    	outDir.mkdirs(); 
	    ZipFile zip = null;
		try {
			zip = new ZipFile(new File(path), Charset.forName("GBK"));
			for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zip.getInputStream(entry);
				if (entry.isDirectory()) { // 处理压缩文件包含文件夹的情况
					File fileDir = new File(outDir +"/"+ zipEntryName);
					fileDir.mkdir();
					continue;
				}
				File file = new File(outDir, zipEntryName);
				if(!file.exists()){
					 file.getParentFile().mkdir();
				}
				file.createNewFile();
				OutputStream out = new FileOutputStream(file);
				byte[] buff = new byte[1024];
				int len;
				while ((len = in.read(buff)) > 0) {
					out.write(buff, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			//throw new RuntimeException("unzip error from FileUtil", e);
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//获取解压后文件夹中的excel文件
		fileName = ZipFiles2.getSubFiles(ourDirPath);
		return ourDirPath+"/"+fileName;
	}
   
	public static String getCellFormatValue(Cell cell){
        if(cell!=null){
        	cell.setCellType(Cell.CELL_TYPE_STRING);
        	String all=cell.getStringCellValue();
        	if(StringUtil.isEmpty(all)){
        		return "";
        	}
        	return all;
        }else{
        	return "";
        }
	}
	
	/**
	 *系统自动生成唯一的订单编号(order_no) 
	 */
	public synchronized static String buildOrderNo(String bank_no,String task_type){
		Calendar c1 = Calendar.getInstance();
		//取系统当前时间
        c1.setTime(new Date());
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
		String datetime = df.format(c1.getTime());//17位
		String order_no = bank_no+task_type+datetime;
		return order_no;
	}
	
	// 测试
	public static void main(String[] args) {
		File f = new File("E:\\apache-tomcat-8.5.27\\webapps\\merchant-admin\\allZip\\1526449146743");
		new ZipFiles2(new File("E:\\apache-tomcat-8.5.27\\webapps\\merchant-admin\\allZip\\1526449146743.zip"))
				.zipFiles(f);
		delAllFiles(f, null);
		// f.delete();
	}

}
