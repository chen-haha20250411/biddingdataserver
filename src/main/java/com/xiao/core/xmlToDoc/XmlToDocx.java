package com.xiao.core.xmlToDoc;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.jodconverter.JodConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Value;

/*
* 其实docx属于zip的一种，这里只需要操作word/document.xml中的数据，其他的数据不用动
*
* @author
*
*/
public class XmlToDocx {

	@Value("${OpenOfficePath}")
	private static String OpenOfficePath;
/*
*
* @param xmlTemplate xml的文件名
* @param docxTemplate docx的路径和文件名
* @param xmlTemp 填充完数据的临时xml
* @param toFilePath 目标文件名
* @param map 需要动态传入的数据
* @throws IOException
* @throws TemplateException
*/
public static boolean toDocx(String xmlTemplate,String docxTemplate,String toFilePath,Map map) {
try {
String xmlTemp = System.currentTimeMillis()+".xml";
// 1.map是动态传入的数据
// 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
Writer w1 = new OutputStreamWriter(new FileOutputStream(xmlTemp), "UTF-8");
// 2.把map中的数据动态由freemarker传给xml
XmlTplUtil.process(xmlTemplate, map, w1);
// 3.把填充完成的xml写入到docx中
XmlToDocx xtd = new XmlToDocx();
File xmlFile = new File(xmlTemp);
xtd.outDocx(xmlFile, docxTemplate, toFilePath);
xmlFile.delete();
return true;
}catch (Exception e) {
e.printStackTrace();
return false;
}
}
/*
*
* @param documentFile 动态生成数据的docunment.xml文件
* @param docxTemplate docx的模板
* @param toFilePath 需要导出的文件路径
* @throws ZipException
* @throws IOException
*/

public void outDocx(File documentFile, String docxTemplate, String toFilePath) throws ZipException, IOException {

try {
String classPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().toString();
System.out.println(classPath+"ftl"+File.separator+docxTemplate);
File docxFile = new File(classPath+"ftl"+File.separator+docxTemplate);
ZipFile zipFile = new ZipFile(docxFile);
Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(toFilePath));
int len = -1;
byte[] buffer = new byte[1024];
while (zipEntrys.hasMoreElements()) {
ZipEntry next = zipEntrys.nextElement();
InputStream is = zipFile.getInputStream(next);
// 把输入流的文件传到输出流中 如果是word/document.xml由我们输入
zipout.putNextEntry(new ZipEntry(next.toString()));
if ("word/document.xml".equals(next.toString())) {
InputStream in = new FileInputStream(documentFile);
while ((len = in.read(buffer)) != -1) {
zipout.write(buffer, 0, len);
}
in.close();
} else {
while ((len = is.read(buffer)) != -1) {
zipout.write(buffer, 0, len);
}
is.close();
}
}
zipout.close();

} catch (Exception e) {
e.printStackTrace();
}
}


/**
 * 将源文件转换为目标文件的格式，此方法将自动进行识别文件类型，无需传入其他参数
 * @param source 源文件
 * @param target 目标文件
 * @throws OfficeException
 */
public synchronized static boolean convert(String source, String target){
	try {
		// 源文件目录
        File inputFile = new File(source);
        if (!inputFile.exists()) {
            System.out.println("源文件不存在！");
            return false;
        }
        // 输出文件目录
        File outputFile = new File(target);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().exists();
        }

        LocalOfficeManager officeManager = LocalOfficeManager.builder()
                .officeHome(OpenOfficePath)
                .portNumbers(8100)
                .processTimeout(3000)
                .maxTasksPerProcess(4)
                .install()
                .build();


        officeManager.start();

        JodConverter.convert(inputFile).to(outputFile).execute();

        officeManager.stop();
        return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
}

	public static boolean xmlToPdfSign(String xmlName,String docxName,String path,Map<String,Object> map){
		boolean flag =true;
		//生成docx
		flag = toDocx(xmlName, docxName, path, map);
		if(!flag){
			return flag;
		}
		//生成pfd
		String pathPDF2=path.replace(".doc", "2.pdf");
		flag = convert(path, pathPDF2);
		if(!flag){
			return flag;
		}
		//盖章
//		String pathPDF=path.replace(".doc", ".pdf");
//		flag = SignDemo.signToPdf(pathPDF2, pathPDF);
//		if(!flag){
//			return flag;
//		}
		return flag;
	}

}