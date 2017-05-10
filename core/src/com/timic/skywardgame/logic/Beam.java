package com.timic.skywardgame.logic;

public class Beam extends Entity {
	public static final int BEAM_WIDTH = 50;
	public static final int BEAM_HEIGHT = 50;
	public static final int BEAM_VELOCITY = 580;

	public Beam(float x, float y) {
		super(x, y, BEAM_WIDTH, BEAM_HEIGHT);
		velocity.y = BEAM_VELOCITY;
	}
	
	public void update(float delta) {
		position.add(0, velocity.y*delta);
		bounds.x = position.x;
		bounds.y = position.y-50;
	}

}
