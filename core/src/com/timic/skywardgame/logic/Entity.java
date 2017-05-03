package com.timic.skywardgame.logic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
	public final Vector2 position;
	public final Rectangle bounds;
	public final Vector2 velocity;
	public final Vector2 acceleration;
	
	public Entity(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x, y-height/2, width, height);
		this.velocity = new Vector2();
		this.acceleration = new Vector2();
	}
	
}
