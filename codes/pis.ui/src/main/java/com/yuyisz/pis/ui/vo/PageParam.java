package com.yuyisz.pis.ui.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageParam {
	private int page = 0;
	private int pageSize=15;
	private List<String> properties = new ArrayList<>();//排序字段
	private String direction;	//排序方向，asc , desc
	private int draw;
	
	public int getDraw() {
		return draw;
	}



	public void setDraw(int draw) {
		this.draw = draw;
	}



	public int getPage() {
		return page;
	}



	public void setPage(int page) {
		this.page = page;
	}



	public int getPageSize() {
		return pageSize;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public List<String> getProperties() {
		return properties;
	}



	public void setProperties(String ... properties) {
		for(String property:properties) {
			this.properties.add(property);
		}
	}



	public String getDirection() {
		return direction;
	}



	public void setDirection(String direction) {
		this.direction = direction;
	}



	public PageRequest buildRequest() {
		Sort sort = null;
		if(this.properties!=null&&!this.properties.isEmpty()) {
			sort = new Sort(Direction.valueOf(this.direction), this.properties);
		}
		if(sort == null) {
			return new PageRequest(this.page,this.pageSize);
		}else {
			return new PageRequest(this.page,this.pageSize,sort);
		}
	}
}
