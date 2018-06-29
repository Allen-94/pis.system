package com.yuyisz.pis.ui.module.alarm;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 告警转发配置
 * @author mengz
 *
 */
@Entity
@Table(name="t_alarm_forward_config")
public class AlarmForwardConfig {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(name="TYPE")
	private int type;              	      	//配置类型，1：邮件发送类型，2：短信转发 ，3：向上级转发
	@Column(name="CONFIG_NAME")
	private String configName;     	      	//配置名称
	@Column(name="GRADE")
	private AlarmGrade grade;	  	      	//转发告警级别
	@OneToMany(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER)
	private Set<DevResources> sources;   	//要转发的告警源
	@Column(name="CONFIG_CONTENT")
	private String configContent;	    	//配置内容，每种配置不相同，保存为字符串格式，各类型自己解析
	@OneToMany(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER)
	private Set<User> acceptor;		    	//接收者，可表示多个接收者，保存接收者ID，逗号分隔
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public AlarmGrade getGrade() {
		return grade;
	}
	public void setGrade(int value) {
		this.grade = AlarmGrade.getByValue(value);;
	}
	public Set<DevResources> getSources() {
		return sources;
	}
	public void setSources(Set<DevResources> sources) {
		this.sources = sources;
	}
	public void setGrade(AlarmGrade grade) {
		this.grade = grade;
	}
	public String getConfigContent() {
		return configContent;
	}
	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}
	public Set<User> getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(Set<User> acceptor) {
		this.acceptor = acceptor;
	}
	
	public String getAcceptorIds() {
		StringBuilder sb = new StringBuilder();
		if(acceptor!=null) {
			for(User user:acceptor) {
				sb.append(user.getUserId()+",");
			}
			return sb.subSequence(0, sb.length()-1).toString();
		}else {
			return "";
		}
		
	}
}
