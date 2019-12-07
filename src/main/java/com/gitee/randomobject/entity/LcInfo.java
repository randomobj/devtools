package com.gitee.randomobject.entity;

import com.gitee.randomobject.annotation.Excel;

public class LcInfo {

	@Excel(name = "用户名",column = "a")
	private String username;
	@Excel(name = "密码",column = "b")
	private String password;
	@Excel(name = "邮箱密码",column = "c")
	private String emailpass;
	@Excel(name = "应用名称",column = "d")
	private String appName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailpass() {
		return emailpass;
	}

	public void setEmailpass(String emailpass) {
		this.emailpass = emailpass;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String toString() {
		return "LcInfo [username=" + username + ", password=" + password + ", emailpass=" + emailpass + ", appName="
				+ appName + "]";
	}
	
}
