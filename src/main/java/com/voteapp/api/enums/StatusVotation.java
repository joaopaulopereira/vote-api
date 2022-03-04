package com.voteapp.api.enums;

public enum StatusVotation {

	APPROVED(1, "Approved vote"),
	NOT_APPROVED(2, "Not approved vote"),
	TIED(3, "Tied vote");	

	private int code;
	private String description;
	
	private StatusVotation (int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static StatusVotation toEnum(Integer code) {
		if (code == null) {
			return null;
		}
		
		for (StatusVotation x : StatusVotation.values()) {
			if (code.equals(x.getCode())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Invalid code: " + code);
	}
}
