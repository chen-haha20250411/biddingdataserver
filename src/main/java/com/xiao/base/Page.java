package com.xiao.base;

import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page<T> extends RowBounds implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/* 每页最多允许显示多少条 */
	private static final int maxSize = 15;
	/* 当前页 */
	private int current = 1;
	/* 下一页 */
	private int next;
	/* 当前多少条数据 */
	private int listSize = 0;
	/* 列表数据 */
	private List<T> list;

	private int total;

	private Map<String, Object> queryParams = null;

	public Page() {
		if (this.queryParams == null) {
			this.queryParams = new HashMap<>();
		}
	}

	/**
	 * 控制层初始化
	 *
	 * @param pageNow
	 * @param pageSize
	 */
	public Page(Integer pageNow, Integer pageSize) {
		super(offsetCurrent(current(pageNow), limit(pageSize)), limit(pageSize));
		this.setCurrent(current(current));
		if (this.queryParams == null) {
			this.queryParams = new HashMap<>();
		}
		this.queryParams.put("rowIndex", this.getOffset());
		this.queryParams.put("rowNum", this.getLimit());
	}

	public Page<T> putQueryParam(String key, Object value) {
		this.queryParams.put(key, value);
		return this;
	}

	public static int offsetCurrent(int current, int limit) {
		if (current > 0) {
			return (current - 1) * limit;
		}
		return 0;
	}

	public static int limit(Integer limit) {
		return (limit == null || limit==0 ) ? maxSize : limit;
	}

	public static int current(Integer current) {
		return (current == null || current <= 0) ? 1 : current;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		if (list == null || list.isEmpty()) {
			this.setNext(current);
			this.setListSize(0);
		} else {
			this.list = list;
			this.setNext(current + 1);
			this.setListSize(this.getList().size());
		}
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public Map<String, Object> getQueryParams() {
		return this.queryParams;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}


}
