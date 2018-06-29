package com.yuyisz.pis.ui.vo;

/*{
"smtp": "smtp.sina.cn",
"email": "17302602080@sina.cn",
"safeport": "25",
"emailusername": "17302602080@sina.cn",
"emailpassword": "zhaomeng198784"
}*/
public class AlarmForwardConfigContent {

	private String smtp;
	private String email;
	private int safeport;
	private String emailusername;
	private String emailpassword;
	public String getSmtp() {
		return smtp;
	}
	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getSafeport() {
		return safeport;
	}
	public void setSafeport(int safeport) {
		this.safeport = safeport;
	}
	public String getEmailusername() {
		return emailusername;
	}
	public void setEmailusername(String emailusername) {
		this.emailusername = emailusername;
	}
	public String getEmailpassword() {
		return emailpassword;
	}
	public void setEmailpassword(String emailpassword) {
		this.emailpassword = emailpassword;
	}
}
