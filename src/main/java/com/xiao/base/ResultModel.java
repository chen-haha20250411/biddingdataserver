package com.xiao.base;

import java.io.Serializable;
import java.util.HashMap;

public class ResultModel implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -839177678463198140L;

	private boolean success;

	private String errcode;// 返回码

	private String msg;//

	private Object data;

	public ResultModel() {

	}

	public ResultModel(boolean success) {
		this.setSuccess(success);
	}

	public ResultModel(boolean success, String event) {
		this.tip(event, success);
	}

	public ResultModel tip(String event, boolean success) {
		this.setSuccess(success);
		this.setMsg(event);
		return this;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@SuppressWarnings("unchecked")
	public ResultModel putData(String key, Object value) {
		if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
		((HashMap<String, Object>) this.data).put(key, value);
		return this;
	}

	public Object getData() {
		return data;
	}

	public ResultModel setData(Object data) {
		this.data = data;
		return this;
	}

}
