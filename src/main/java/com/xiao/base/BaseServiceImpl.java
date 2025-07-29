package com.xiao.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * Function: 实现类（ 泛型：M 是 mapper 对象， T 是实体 ， ）
 *
 * date: 2016年8月29日 下午4:05:53
 * 杭州萨莫网络科技有限公司 All Rights Reserved
 * @author zhaozf
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

    public abstract BaseMapper<T> getMap();

	/**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected boolean retBool(int result) {
        return result >= 1;
    }

	@Override
	public boolean insert(T entity) {
		return retBool(this.getMap().insert(entity));
	}

	@Override
	public boolean deleteById(String id) {
		return retBool(this.getMap().deleteById(id));
	}

	@Override
	public boolean deleteByMap(Map<String, Object> columnMap) {
		return retBool(this.getMap().deleteByMap(columnMap));
	}

	@Override
	public boolean update(T entity) {
		return retBool(this.getMap().update(entity));
	}

	@Override
	public T queryById(String id) {
		return this.getMap().queryById(id);
	}

	@Override
	public List<T> queryByIds(List<String> idList) {
		return this.getMap().queryByIds(idList);
	}

	@Override
	public List<T> queryByMap(Map<String, Object> columnMap) {
		return this.getMap().queryByMap(columnMap);
	}

	@Override
	public int queryByCount(Map<String, Object> columnMap) {
		return this.getMap().queryByCount(columnMap);
	}



}
