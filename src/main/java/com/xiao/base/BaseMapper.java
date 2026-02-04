package com.xiao.base;

import java.util.List;
import java.util.Map;

/** 
 * Function: 基础mapper类
 *
 * date: 2016年8月29日 下午3:59:01 
 * 杭州萨莫网络科技有限公司 All Rights Reserved
 * @author zhaozf
 */  
public interface BaseMapper<T> {

	/**
	 * <p>
	 * 插入一条记录
	 * </p>
	 * 
	 * @param entity
	 *            实体对象
	 * @return boolean
	 */
	int insert(T entity);

	/**
	 * <p>
	 * 根据 ID 删除
	 * </p>
	 * 
	 * @param id
	 *            主键ID
	 * @return boolean
	 */
	int deleteById(String id);

	/**
	 * <p>
	 * 根据 columnMap 条件，删除记录
	 * </p>
	 * 
	 * @param columnMap
	 *            表字段 map 对象
	 * @return boolean
	 */
	int deleteByMap(Map<String, Object> columnMap);

	/**
	 * <p>
	 * 根据 whereEntity 条件，更新记录
	 * </p>
	 * 
	 * @param entity
	 *            实体对象
	 * @return boolean
	 */
	int update(T entity);

	/**
	 * <p>
	 * 根据 ID 查询 
	 * 主键 字符串型,页面等转型过来好处理
	 * </p>
	 * 
	 * @param id
	 *            主键ID
	 * @return T
	 */
	T queryById(String id);

	/**
	 * <p>
	 * 主键 字符串型,页面等转型过来好处理
	 * 查询（根据ID 批量查询）
	 * </p>
	 * 
	 * @param idList
	 *            主键ID列表
	 * @return List<T>
	 */
	List<T> queryByIds(List<String> idList);

	/**
	 * <p>
	 * 查询（根据 columnMap 条件）
	 * </p>
	 * 
	 * @param columnMap
	 *            表字段 map 对象
	 * @return List<T>
	 */
	List<T> queryByMap(Map<String, Object> columnMap);

	/**
	 * <p>
	 * 根据 entity 条件，查询总记录数
	 * </p>
	 * 
	 * @param columnMap
	 *            参数
	 * @return int
	 */
	int queryByCount(Map<String, Object> columnMap);
}
