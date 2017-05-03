package com.timic.skywardgame.accounts;

public class Account {
	private String username;
	private String password;
	private int movement;
	private int score;
	
	public Account(String username, String password, int movement, int score) {
		this.username = username;
		this.password = password;
		this.movement = movement;
		this.score = score;
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
	
	public int getScore() {
		return score;
	}
	
}
