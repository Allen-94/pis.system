package com.yuyisz.pis.ui.module.alarm;

/**
 * 告警级别
 * @author 42254
 *
 */
public enum AlarmGrade {

	URGENT("urgent","",0), //紧急
	IMPORTANT("important","",1),//重要
	GENERAL("general","",2),//一般
	HINT("hint","",3);//提示
	
	String name; //名称
	String imgUrl;//对应的图标
	int value;	//值，存入数据库
	
	private AlarmGrade(String name,String imgUrl,int value) {
		this.value=value;
		this.name=name;
		this.imgUrl=imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static AlarmGrade getByValue(int v) {
		for(AlarmGrade grade:AlarmGrade.values()) {
			if(grade.value==v) {
				return grade;
			}
		}
		return null;
	}
}
