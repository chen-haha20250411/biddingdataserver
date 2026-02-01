package com.xiao.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultModel implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -839177678463198140L;

	// vue-element-admin 标准响应码：20000=成功，其他为错误码
	public static final int CODE_SUCCESS = 20000;
	public static final int CODE_ERROR = 50000;
	public static final int CODE_TOKEN_INVALID = 50008;
	public static final int CODE_TOKEN_EXPIRED = 50014;
	
	private boolean success;
	private int code; // vue-element-admin 响应码
	private String errcode;// 返回码
	private String msg;//
	private Object data;

	public ResultModel() {
		this.code = CODE_SUCCESS;
		this.success = true;
	}

	public ResultModel(boolean success) {
		this();
		this.setSuccess(success);
		this.setCode(success ? CODE_SUCCESS : CODE_ERROR);
	}

	public ResultModel(boolean success, String event) {
		this(success);
		this.setMsg(event);
	}

	public ResultModel(int code, String message, Object data) {
		this.code = code;
		this.success = code == CODE_SUCCESS;
		this.msg = message;
		this.data = data;
	}

	public ResultModel tip(String event, boolean success) {
		this.setSuccess(success);
		this.setCode(success ? CODE_SUCCESS : CODE_ERROR);
		this.setMsg(event);
		return this;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
		this.success = code == CODE_SUCCESS;
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
		this.code = success ? CODE_SUCCESS : CODE_ERROR;
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

	// 快速创建成功响应
	public static ResultModel success() {
		return new ResultModel(CODE_SUCCESS, "成功", null);
	}

	public static ResultModel success(String message) {
		return new ResultModel(CODE_SUCCESS, message, null);
	}

	public static ResultModel success(Object data) {
		return new ResultModel(CODE_SUCCESS, "成功", data);
	}

	public static ResultModel success(String message, Object data) {
		return new ResultModel(CODE_SUCCESS, message, data);
	}

	// 快速创建错误响应
	public static ResultModel error() {
		return new ResultModel(CODE_ERROR, "操作失败", null);
	}

	public static ResultModel error(String message) {
		return new ResultModel(CODE_ERROR, message, null);
	}

	public static ResultModel error(int code, String message) {
		return new ResultModel(code, message, null);
	}

	public static ResultModel error(int code, String message, Object data) {
		return new ResultModel(code, message, data);
	}

	// 快速创建失败响应（别名）
	public static ResultModel failure(String message) {
		return new ResultModel(CODE_ERROR, message, null);
	}
}
