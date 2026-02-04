package com.xiao.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtil {

	private Configuration cfg;

	public boolean buildTemplate(Map<String,Object> root, String projectPath,String tempname) {
		String parentDir = projectPath.substring(0, projectPath.lastIndexOf(File.separator) );
//		System.out.println(tempdirpath);
        if (!new File(parentDir).exists()) {
            new File(parentDir).mkdirs();
        }
		try {
			// 初始化FreeMarker配置
			// 创建一个Configuration实例
			cfg  = new Configuration();
			cfg.setDefaultEncoding("utf-8");  
			// 设置FreeMarker的模版文件位置
			cfg.setClassForTemplateLoading(FreemarkerUtil.class, "/ftl");  
			Template template = cfg.getTemplate(tempname);
			
			Writer out = new OutputStreamWriter(new FileOutputStream(projectPath), "UTF-8");
			template.process(root, out);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
