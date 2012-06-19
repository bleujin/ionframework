package net.ion.framework.db.bean.test;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class CmsMember {
	private String userId;
	private String userNm;
	private String enrolDay;
	private String email;
	private String rduty;

	public CmsMember() {
	}

	public String getEmail() {
		return email;
	}

	public String getEnrolDay() {
		return enrolDay;
	}

	public String getRduty() {
		return rduty;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setRduty(String rduty) {
		this.rduty = rduty;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
