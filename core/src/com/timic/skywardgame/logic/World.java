package com.timic.skywardgame.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class World {
	public static final int WORLD_WIDTH = 560;
	public static final int WORLD_HEIGHT = (int)Math.pow(10, 5);
	public static final Vector2 WORLD_GRAVITY = new Vector2(0, -12);
	
	public final Hero hero;
	public final List<Platform> platforms;
	
	public final Random seed;
	
	public World() {
		this.hero = new Hero(WORLD_WIDTH/2-Hero.HERO_WIDTH/2, 0);
		this.platforms = new ArrayList<Platform>();
		seed = new Random();
		
		generateSky();
	}
	
	public void update(float delta, float accelerationX) {
		updateHero(delta, accelerationX);
	}
	
	private void updateHero(float delta, float accelerationX) {
		hero.velocity.x = -accelerationX*Hero.HERO_MOVE_VELOCITY;
		hero.update(delta);
	}
	
	private void generateSky() {
		float maxJumpHeight = (float)Math.pow(Hero.HERO_JUMP_VELOCITY, 2)/(2*-WORLD_GRAVITY.y);
		float y = maxJumpHeight;
		while(y < WORLD_HEIGHT) {
			float x = seed.nextFloat()*(WORLD_WIDTH-Platform.PLATFORM_WIDTH*2);
			
			Platform platform = new Platform(x, y);
			platforms.add(platform);
			
			y += (maxJumpHeight-0.5f);
			y -= seed.nextFloat()*(maxJumpHeight/3);
		}
	}
	
}
