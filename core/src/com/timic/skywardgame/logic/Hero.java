package com.timic.skywardgame.logic;

public class Hero extends Entity {
	public static final int HERO_WIDTH = 66;
	public static final int HERO_HEIGHT = 92;
	public static final float HERO_JUMP_VELOCITY = 81;
	public static final float HERO_MOVE_VELOCITY = 110;
	

	public Hero(float x, float y) {
		super(x, y, HERO_WIDTH, HERO_HEIGHT);
		// TODO Auto-generated constructor stub
	}
	
	public void update(float delta) {
		position.add(velocity.x*delta,velocity.y*delta);
	}
	
}
