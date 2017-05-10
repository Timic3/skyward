package com.timic.skywardgame.logic;

public class Platform extends Entity {
	public static final int PLATFORM_WIDTH = 70;
	public static final int PLATFORM_HEIGHT = 70;
	public static final int PLATFORM_VELOCITY = 75;
	
	int type;

	public Platform(float x, float y, int type) {
		super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
		this.type = type;
		if(type == 1)
			this.velocity.x = PLATFORM_VELOCITY+y/100;
	}
	
	public void update(float delta) {
		bounds.y = position.y;
		bounds.height = 25;
		if(type == 1) {
			position.add(velocity.x*delta, 0);
			bounds.x = position.x;
			if(position.x < 0) {
				velocity.x = -velocity.x;
				position.x = 0;
			}
			if(position.x > World.WORLD_WIDTH-PLATFORM_WIDTH*2) {
				velocity.x = -velocity.x;
				position.x = World.WORLD_WIDTH-PLATFORM_WIDTH*2;
			}
		}
	}
	
}
