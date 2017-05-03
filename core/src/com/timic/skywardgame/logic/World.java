package com.timic.skywardgame.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.timic.skywardgame.accounts.Accounts;
import com.timic.skywardgame.screens.GameScreen;

public class World {
	public static final int WORLD_WIDTH = 560;
	public static final int WORLD_HEIGHT = (int)Math.pow(10, 5);
	public static final Vector2 WORLD_GRAVITY = new Vector2(0, -612);
	
	public final Hero hero;
	public final List<Platform> platforms;
	
	public final Random seed;
	
	public int score;
	public int maxHeight;
	
	public World() {
		this.hero = new Hero(WORLD_WIDTH/2-Hero.HERO_WIDTH/2, 0);
		this.platforms = new ArrayList<Platform>();
		seed = new Random();
		
		generateSky();
		
		this.score = 0;
		this.maxHeight = 0;
	}
	
	public void update(float delta) {
		float acceleration = 0;
		if(Accounts.movement == 0) {
			if(Gdx.input.isKeyPressed(Keys.DPAD_LEFT))
				acceleration = 5f;
			if(Gdx.input.isKeyPressed(Keys.DPAD_RIGHT))
				acceleration = -5f;
		} else {
			// In beta - move with mouse
			if(Gdx.input.getX()-1 > hero.position.x+Hero.HERO_WIDTH/2)
				acceleration = -5f;
			else if(Gdx.input.getX()+2 < hero.position.x+Hero.HERO_WIDTH/2)
				acceleration = 5f;
			else
				acceleration = 0;
		}
		
		updateHero(delta, acceleration);
		updatePlatforms(delta);
		checkCollisions();
		if(maxHeight-450 > hero.position.y) {
			GameScreen.gameOver = true;
		}
	}
	
	private void generateSky() {
		float maxJumpHeight = (float)Math.pow(Hero.HERO_JUMP_VELOCITY, 2)/(2*-WORLD_GRAVITY.y);
		float y = maxJumpHeight/1.5f;
		while(y < WORLD_HEIGHT) {
			int type = seed.nextFloat() > 0.6f ? 1 : 0;
			float x = seed.nextFloat()*(WORLD_WIDTH-Platform.PLATFORM_WIDTH*2);
			
			Platform platform = new Platform(x, y, type);
			platforms.add(platform);
			
			y += (maxJumpHeight);
			y -= seed.nextFloat()*(maxJumpHeight/3);
		}
	}
	
	private void updateHero(float delta, float accelerationX) {
		if(hero.position.y <= 0)
			hero.hitPlatform();
		hero.velocity.x = -accelerationX*Hero.HERO_MOVE_VELOCITY;
		hero.update(delta);
		maxHeight = Math.max((int)hero.position.y, maxHeight);
		score = (int)(maxHeight/((float)Math.pow(Hero.HERO_JUMP_VELOCITY, 2)/(2*-WORLD_GRAVITY.y)));
		GameScreen.score = score;
	}
	
	private void updatePlatforms(float delta) {
		for(int i=0;i < platforms.size(); i++) {
			Platform platform = platforms.get(i);
			if(platform.position.y > GameScreen.getRenderer().getCameraY()*2)
				break;
			platform.update(delta);
		}
	}
	
	private void checkCollisions() {
		checkPlatformCollisions();
	}
	
	private void checkPlatformCollisions() {
		if(hero.velocity.y > 0) return;
		
		for(int i=0;i < platforms.size(); i++) {
			Platform platform = platforms.get(i);
			if(platform.position.y < GameScreen.getRenderer().getCameraY()/2 && i != 0)
				continue;
			if(platform.position.y > GameScreen.getRenderer().getCameraY()*2)
				break;
			if(hero.position.y+Hero.HERO_WIDTH/2 > platform.position.y) {
				if(hero.bounds.overlaps(platform.bounds)) {
					hero.hitPlatform();
					break;
				}
			}
		}
	}
	
	public void dispose() {
		
	}
	
}
