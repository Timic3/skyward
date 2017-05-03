package com.timic.skywardgame.logic;

public class Hero extends Entity {
	public static final int HERO_WIDTH = 66;
	public static final int HERO_HEIGHT = 94;
	public static final float HERO_JUMP_VELOCITY = 581;
	public static final float HERO_MOVE_VELOCITY = 110;
	
	public static State state;
	public static int facing = 1;
	
	public enum State {
		JUMP,
		FALL,
		HIT
	}

	public Hero(float x, float y) {
		super(x, y, HERO_WIDTH, HERO_HEIGHT);
		// TODO Auto-generated constructor stub
		state = State.FALL;
	}
	
	public void update(float delta) {
		velocity.add(World.WORLD_GRAVITY.x*delta, World.WORLD_GRAVITY.y*delta);
		position.add(velocity.x*delta,velocity.y*delta);
		
		if(velocity.y > 0)
			if(state != State.JUMP)
				state = State.JUMP;
		if(velocity.y < 0)
			if(state != State.FALL)
				state = State.FALL;
		
		if(position.x+HERO_WIDTH < 0)
			position.x = World.WORLD_WIDTH;
		if(position.x > World.WORLD_WIDTH)
			position.x = -HERO_WIDTH;
		
		if(velocity.x < 0)
			facing = -1;
		else if(velocity.x > 0)
			facing = 1;
	}
	
	public void hitPlatform() {
		velocity.y = Hero.HERO_JUMP_VELOCITY;
		state = State.JUMP;
	}
	
}
