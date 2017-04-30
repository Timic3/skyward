package com.timic.skywardgame.accounts;

public class Account {
	private String username;
	private String password;
	private int movement;
	
	public Account(String username, String password, int movement) {
		this.username = username;
		this.password = password;
		this.movement = movement;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getMovement() {
		return movement;
	}
	
}
