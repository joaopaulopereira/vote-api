package com.voteapp.api.enums;

public enum StatusSurvey {

	CREATED(1, "Created"),
	SESSION_OPENED(2, "Session opened"),
	TIMED_OUT(3, "Timed out");	

	private int code;
	private String description;
	
	private StatusSurvey (int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static StatusSurvey toEnum(Integer code) {
		if (code == null) {
			return null;
		}
		
		for (StatusSurvey x : StatusSurvey.values()) {
			if (code.equals(x.getCode())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Invalid code: " + code);
	}
}
