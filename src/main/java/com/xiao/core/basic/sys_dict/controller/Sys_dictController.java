package com.xiao.core.basic.sys_dict.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiao.base.BaseController;
import com.xiao.base.Page;
import com.xiao.base.ResultModel;
import com.xiao.constans.E;
import com.xiao.core.basic.operator.domain.Operator;
import com.xiao.core.basic.sys_dict.domain.SiDictTree;
import com.xiao.core.basic.sys_dict.domain.Sys_dict;
import com.xiao.core.basic.sys_dict.service.Sys_dictService;
import com.xiao.logannotation.CurrentUser;
import com.xiao.logannotation.LoginRequired;
import com.xiao.util.PatternUtil;
import com.xiao.util.StringUtil;
import com.xiao.util.ZipFiles;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin/sys_dict")
public class Sys_dictController extends BaseController{

	@Resource
	Sys_dictService sys_dictService;

	/**
	* 到主页面
	* @return
	*/
	@LoginRequired(remark="查询列表操作")
	@RequestMapping(value = "/toList")
	public ResultModel toList(@CurrentUser Operator oper,String dict_name,String dict_type, Integer limit, Integer currPageNo) {
		Page<Sys_dict> page=new Page<Sys_dict>(currPageNo,limit);
		//查询位置
		page.putQueryParam("dict_nameQuery", dict_name);
		page.putQueryParam("dict_type", dict_type);
		//查询总数
		int rowCount = sys_dictService.queryByCount(page.getQueryParams());
		page.setTotal(rowCount);
		List<Sys_dict> sys_dictList = sys_dictService.queryByMap(page.getQueryParams());
		//查询结果
		page.setList(sys_dictList);
		return new ResultModel(true,"返回list数据").setData(page);
	}


	/**
	 * 到主页面
	 * @return
	 */
	@LoginRequired(remark="查询所有信息列表操作")
	@RequestMapping(value = "/quyerAllList")
	public ResultModel quyerAllList(String dict_type) {
		Map<String,Object> map=new HashMap<>();
		map.put("dict_type", dict_type);
		List<Sys_dict> sys_dictList = sys_dictService.queryByMap(map);
		return new ResultModel(true,"返回list数据").setData(sys_dictList);
	}
	
	/**
	 * 查多级树结构
	 * @return
	 */
	@LoginRequired(remark="查询多级树结构")
	@RequestMapping(value = "/quyerDictTree")
	public ResultModel quyerDictTree(String dict_type) {
		Map<String,Object> map = getRootMap();
		map.clear();
		map.put("dict_type", dict_type);
		map.put("dict_stat", "1");
		List<Sys_dict> sys_dictList = sys_dictService.queryByMap(map);
		Map<String, List<Sys_dict>> sonMap = new HashMap<String, List<Sys_dict>>();//子节点
		Map<String, Sys_dict> treeMap = new HashMap<String, Sys_dict>();
		List<Sys_dict> sList = null;
		List<SiDictTree> treeList = new ArrayList<SiDictTree>();
		SiDictTree tree=null;
		for(Sys_dict s:sys_dictList){
			//设置子节点
			if(sonMap.get(s.getParent_id())==null){
				sList = new ArrayList<Sys_dict>();
			}else{
				sList = sonMap.get(s.getParent_id());
			}
			sList.add(s);
			sonMap.put(s.getParent_id(), sList);
			
			tree = new SiDictTree();
			if("0".equals(s.getParent_id()) || StringUtil.isEmpty(s.getParent_id())){
				tree.setValue(s.getDict_id());
				tree.setLabel(s.getDict_name());
				tree.setLevel("1");
				tree.setParentId("0");
				treeList.add(tree);
			}
			//显示用
			treeMap.put(s.getDict_id(), s);
		}
		setTreeDict(treeList, sonMap, tree);
		map.clear();
		map.put("treeList", treeList);
		map.put("treeMap", treeMap);
		return new ResultModel(true,"返回list数据").setData(map);
	}
	
	private List<SiDictTree> setTreeDict(List<SiDictTree> dataList,Map<String, List<Sys_dict>> map,SiDictTree tree){
		for(SiDictTree t:dataList){
			if(map.get(t.getValue())!=null){
				//有子节点
				List<Sys_dict> cList = map.get(t.getValue());
				List<SiDictTree> ctreeList = new ArrayList<SiDictTree>();
				for(Sys_dict s:cList){
					tree = new SiDictTree();
					if(t.getValue().equals(s.getParent_id())){
						tree.setValue(s.getDict_id());
						tree.setLabel(s.getDict_name());
						if("0".equals(t.getParentId())){
							tree.setLevel("2");
						}else{
							tree.setLevel("3");
						}
						ctreeList.add(tree);
					}
				}
				setTreeDict(ctreeList,map,tree);
				t.setChildren(ctreeList);
			}else{
				t.setChildren(null);
			}
		}
		return dataList;
	}
	
	/**
	 * 查询数据
	 * @return
	 */
	@LoginRequired(remark="查询列表操作")
	@RequestMapping(value = "/queryList")
	public ResultModel queryList(String dict_type) {
		Page<Sys_dict> page=new Page<Sys_dict>();
		//查询位置
		page.putQueryParam("dict_type", dict_type);
		//查询总数
		List<Sys_dict> sys_dictList = sys_dictService.queryByMap(page.getQueryParams());
		Map<String,String> dictMap = new HashMap<String,String>();
		for(Sys_dict dict:sys_dictList){
			dictMap.put(dict.getDict_id(), dict.getDict_name());
		}
		//查询结果
		page.setList(sys_dictList);
		page.putQueryParam("dictMap", dictMap);
		return new ResultModel(true,"返回list数据").setData(page);
	}

	/**
	* 删除
	*/
	@LoginRequired(remark="删除操作")
	@RequestMapping(value = "/deletevalue")
	public ResultModel deletevalue(@RequestParam @NotNull(message=E.E0) String dict_id) {
		try {
			sys_dictService.deleteById(dict_id);
			return new ResultModel(true, "删除成功");
		}catch (Exception e) {
			log.error("删除异常:"+e.getMessage(),e);
			return new ResultModel(false,"网络异常，操作失败");
		}
	}

	/**
	* 修改或保存
	* @param sys_dict
	*/
	@LoginRequired(remark="添加修改信息")
	@RequestMapping(value = "/insertOupdate")
	public ResultModel insertOupdate( Sys_dict sys_dict) {
		boolean flag=true;
		try {
			flag=sys_dictService.insertOupdate(sys_dict);
			if(flag){
				return new ResultModel(true,"保存成功");
			}else{
				return new ResultModel(false,"保存失败，请重试");
			}
		} catch (Exception e) {
			log.error("删除异常:"+e.getMessage(),e);
			return new ResultModel(false,"网络异常，操作失败");
		}
	}
	/**
	* 查询单条记录
	* @param dict_id
	*/
	@LoginRequired(remark="查询单条记录")
	@RequestMapping(value = "/queryById")
	public ResultModel queryById(@RequestParam @NotNull(message=E.E0) String dict_id) {
		Sys_dict sys_dict = sys_dictService.queryById(dict_id);
		return sendSuccessMessage("成功").setData(sys_dict);
	}

	@LoginRequired(remark="导入操作")
	@RequestMapping(value = "/uploadFile")
	public ResultModel uploadFile(@CurrentUser Operator oper, String header,String results) {
		JSONArray headerArray = JSONObject.parseArray(header);
		JSONArray resultsArray = JSONObject.parseArray(results);
		StringBuffer errorInfo = new StringBuffer();
		StringBuffer info = new StringBuffer();
		ArrayList<Sys_dict> sys_dictList = new ArrayList<Sys_dict>();
		for (int i = 0; i < resultsArray.size(); i++) {
			//errorInfo.append("第"+(i+1)+"行");
			JSONObject array = resultsArray.getJSONObject(i);
			Sys_dict sys_dict = new Sys_dict();
			//数据字典名称
			String dict_name = array.getString("数据字典名称");
			if(StringUtil.isEmpty(dict_name)){
				errorInfo.append("数据字典名称为必填,");
			}else{
				sys_dict.setDict_name(dict_name);
			}
			//该字段的父节点存储在静态变量类中
			String dict_type = array.getString("该字段的父节点存储在静态变量类中");
			if(StringUtil.isEmpty(dict_type)){
				errorInfo.append("该字段的父节点存储在静态变量类中为必填,");
			}else{
				sys_dict.setDict_type(dict_type);
			}
			//字典顺序
			String dict_order = array.getString("字典顺序");
			if(StringUtil.isEmpty(dict_order)){
				errorInfo.append("字典顺序为必填,");
			}else{
				sys_dict.setDict_order(dict_order);
			}
			
			//参数名称
			String param_name = array.getString("参数名称");
			if(StringUtil.isEmpty(param_name)){
				errorInfo.append("参数名称为必填,");
			}else{
				sys_dict.setParam_name(param_name);
			}
		
			//创建人
			String add_operid = array.getString("创建人");
			if(StringUtil.isEmpty(add_operid)){
				errorInfo.append("创建人为必填,");
			}else{
				sys_dict.setAdd_operid(add_operid);
			}
			
			if("".equals(errorInfo.toString())){
				sys_dictList.add(sys_dict);
			}else{
				info.append("第"+(i+1)+"行:");
				info.append(errorInfo+"\n");
				errorInfo.delete(0, errorInfo.length());
			}
		}
		if(!"".equals(info.toString())){
			return new ResultModel(false,info.toString());
		}
		if(!sys_dictList.isEmpty()){
			boolean flag = sys_dictService.insertList(sys_dictList);
			if(flag){
				return new ResultModel(true,"插入成功");
			}else{
				return new ResultModel(false,"插入失败");
			}
		}else{
			return new ResultModel(false,"Excel中未包含操作员信息");
		}
	}

	@SuppressWarnings("AlibabaAvoidPatternCompileInMethod")
	@LoginRequired(remark="导入信息操作")
	@RequestMapping(value = "/uploadFile2")
	public ResultModel uploadFile2(@RequestParam("file") MultipartFile file,@CurrentUser Operator oper) {
		Workbook bWorkbook = null;
		StringBuffer errorInfo = new StringBuffer();
		StringBuffer info = new StringBuffer();
		List<Sys_dict> sys_dictList = new ArrayList<Sys_dict>();
		if (file!=null && !file.isEmpty()) {
			try {
				String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				Matcher matcher= PatternUtil.EXCEL_PATTERN.matcher(suffix);
                if(!matcher.find()) {
                	 return new ResultModel(false,"文件格式不对");
                }else{
                	if(".xls".equals(suffix)){
        	        	bWorkbook = new HSSFWorkbook(file.getInputStream());
        	        }else if(".xlsx".equals(suffix)){
        	        	bWorkbook = new XSSFWorkbook(file.getInputStream());
        	        }
        			Sheet sheet = bWorkbook.getSheetAt(0);
        			Row row ;
        			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
        				//errorInfo.append("第"+(i+1)+"行");
        				row = sheet.getRow(i);
        				Sys_dict sys_dict = new Sys_dict();
    					//数据字典名称
						String dict_name = ZipFiles.getCellFormatValue(row.getCell(0));
						if(StringUtil.isEmpty(dict_name)){
        					errorInfo.append("数据字典名称为必填,");
        				}else{
        					sys_dict.setDict_name(dict_name);
        				}
    					//该字段的父节点存储在静态变量类中
						String dict_type = ZipFiles.getCellFormatValue(row.getCell(1));
						if(StringUtil.isEmpty(dict_type)){
        					errorInfo.append("该字段的父节点存储在静态变量类中为必填,");
        				}else{
        					sys_dict.setDict_type(dict_type);
        				}
    					//字典顺序
						String dict_order = ZipFiles.getCellFormatValue(row.getCell(2));
						if(StringUtil.isEmpty(dict_order)){
        					errorInfo.append("字典顺序为必填,");
        				}else{
        					sys_dict.setDict_order(dict_order);
        				}
    					//字典状态 0弃用 1启用
						String dict_stat = ZipFiles.getCellFormatValue(row.getCell(3));
						if(StringUtil.isEmpty(dict_stat)){
        					errorInfo.append("字典状态 0弃用 1启用为必填,");
        				}else{
        					sys_dict.setDict_stat(dict_stat);
        				}
    					//参数名称
						String param_name = ZipFiles.getCellFormatValue(row.getCell(4));
						if(StringUtil.isEmpty(param_name)){
        					errorInfo.append("参数名称为必填,");
        				}else{
        					sys_dict.setParam_name(param_name);
        				}
    					//添加时间
						String addtime = ZipFiles.getCellFormatValue(row.getCell(5));
						if(StringUtil.isEmpty(addtime)){
        					errorInfo.append("添加时间为必填,");
        				}else{
        					sys_dict.setAddtime(addtime);
        				}
    					//修改时间
						String edittime = ZipFiles.getCellFormatValue(row.getCell(6));
						if(StringUtil.isEmpty(edittime)){
        					errorInfo.append("修改时间为必填,");
        				}else{
        					sys_dict.setEdittime(edittime);
        				}
    					//创建人
						String add_operid = ZipFiles.getCellFormatValue(row.getCell(7));
						if(StringUtil.isEmpty(add_operid)){
        					errorInfo.append("创建人为必填,");
        				}else{
        					sys_dict.setAdd_operid(add_operid);
        				}
    					//修改人
						String edit_operid = ZipFiles.getCellFormatValue(row.getCell(8));
						if(StringUtil.isEmpty(edit_operid)){
        					errorInfo.append("修改人为必填,");
        				}else{
        					sys_dict.setEdit_operid(edit_operid);
        				}
        				if("".equals(errorInfo.toString())){
        					sys_dictList.add(sys_dict);
        				}else{
        					info.append("第"+(i+1)+"行:");
        					info.append(errorInfo+"\n");
        					errorInfo.delete(0, errorInfo.length());
        				}
        			}
        			if(!"".equals(info.toString())){
        				return new ResultModel(false,info.toString());
        			}
        			if(!sys_dictList.isEmpty()){
        				boolean flag = sys_dictService.insertList(sys_dictList);
        				if(flag){
        					return new ResultModel(true,"插入成功");
        				}else{
        					return new ResultModel(false,"插入失败");
        				}
        			}else{
        				return new ResultModel(false,"Excel中未包含信息");
        			}
                }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	log.info("==================上传失败===================");
				return new ResultModel(false,"网络异常，上传失败");
			}
		}else{
			return new ResultModel(false,"请上传导入文件");
		}
	}
}
