package com.timic.skywardgame.logic;

public class Enemy extends Entity {
	public static final int ENEMY_WIDTH = 72;
	public static final int ENEMY_HEIGHT = 36;
	public static final int ENEMY_VELOCITY = 51;
	
	public int facing;
	
	public Enemy(float x, float y) {
		super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
		this.facing = 1;
		this.velocity.x = ENEMY_VELOCITY+y/100;
	}
	
	public void update(float delta) {
		bounds.y = position.y-30;
		bounds.x = position.x;
		bounds.width = ENEMY_WIDTH-40;
		position.add(velocity.x*delta, 0);
		if(position.x < 0) {
			velocity.x = -velocity.x;
			position.x = 0;
		}
		if(position.x > World.WORLD_WIDTH-ENEMY_WIDTH) {
			velocity.x = -velocity.x;
			position.x = World.WORLD_WIDTH-ENEMY_WIDTH;
		}
		if(velocity.x < 0)
			facing = 1;
		else if(velocity.x > 0)
			facing = -1;
	}
	
}
