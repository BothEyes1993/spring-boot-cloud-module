package com.sbcm.common.vo;

import java.io.Serializable;
import java.util.Date;

public class BaseVO  implements Serializable {

	private static final long serialVersionUID = -826952;

	private Integer createBy;
	private Date createDate;
	private Integer lastUpdatedBy;
	private Date lastUpdatedDate;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
}
