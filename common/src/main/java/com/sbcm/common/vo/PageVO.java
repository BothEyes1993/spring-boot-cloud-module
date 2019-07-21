package com.sbcm.common.vo;


import java.io.Serializable;

public class PageVO implements Serializable {
	private static final long serialVersionUID = -8796732535010682247L;
	
	private Integer curPage;
	private Integer pageSize;
	private Integer totalCount;
	private Integer totalPage;
	private Integer start; // start表示当前页开始的记录数,start=每页行数*(当前页数-1)  
    private Integer end; // 当前页结束的记录行数

	public PageVO(){
		this.start=0;
		this.end=0;
		this.curPage=0;
		this.pageSize=10;
	}
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getStart() {
		return (this.curPage-1)*this.pageSize;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getEnd() {
		if (this.pageSize * (this.curPage) > this.totalCount) {  
            this.end = this.totalCount;  
        } else {  
            this.end = this.pageSize * (this.curPage);
        }
		return this.end;
	}
	public void setEnd(Integer end) {
		this.end = end;
	}
	public Integer getTotalPage() {
		if (this.totalCount % this.pageSize == 0) {
			return this.totalCount / this.pageSize;
		} else {
			return this.totalCount / this.pageSize + 1;
		}
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
}
