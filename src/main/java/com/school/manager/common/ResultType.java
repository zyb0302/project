/**
 * 版权所属：东软望海科技有限公司
 * 作者：张晓明 
 * 版本：V1.0
 * 创建日期：2018年5月9日
 * 修改日期：2018年5月9日
 */
package com.school.manager.common;

/**
 * @Description  json返回结果code
 */
public enum ResultType {

	SUCCESS(1000, "成功"), //
	WRONG(3000, "未知错误"), //
	
	USER_NOT_EXIST(2000,"用户名不存在"),
	USER_NOT_LOGIN(2001,"该账户已停用"),
	USER_EXIST(2003,"用户名已存在"),
	PWSSWORD_ERROR(2001,"密码错误"),
	VCODE_ERROR(2002,"验证码错误"),
	PROJECT_EXIST(2005,"项目名称重复"),
	FILE_NULL(2011,"文件为空"),
	FILE_ERROR(2012,"上传文件错误"),
	NOT_CONFIRM(2013,"未授权"),
	NOT_EXIST_CONFIRM(2014,"授权记录不存在"),
	EXIST_CONFIRM(2015,"设备ID重复"),

	;

	private int code;
	private String info;

	private ResultType(int code, String info) {
		this.code = code;
		this.info = info;
	}

	public int getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}

}
