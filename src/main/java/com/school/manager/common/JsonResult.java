package com.school.manager.common;

import java.io.Serializable;

/**
 * 
 * @Description  接口返回的统一格式
 * @author zhangxm 
 * @version v1.0
 * @since 2018年5月9日
 */
public class JsonResult<T> implements Serializable {

	private static final long serialVersionUID = 1717488731978480120L;
	private int code;
	private T data;
	private String msg;
	public JsonResult() {

	}
	
	public JsonResult(ResultType resultType) {
		this.code = resultType.getCode();
		this.msg = resultType.getInfo();
	}

	public JsonResult(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public JsonResult<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public JsonResult<T> setCode(ResultType resultType) {
		this.code = resultType.getCode();
		this.msg = resultType.getInfo();
		return this;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
