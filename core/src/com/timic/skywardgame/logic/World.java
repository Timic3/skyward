package com.timic.skywardgame.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
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
	public final List<Enemy> enemies;
	public final List<Beam> beams;
	
	public final Random seed;
	
	public int score;
	public int maxHeight;
	
	private long fireDelay;
	
	public World() {
		this.hero = new Hero(WORLD_WIDTH/2-Hero.HERO_WIDTH/2, 0);
		this.platforms = new ArrayList<Platform>();
		this.enemies = new ArrayList<Enemy>();
		this.beams = new ArrayList<Beam>();
		
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
		
		if(Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if(System.nanoTime()-fireDelay >= 600_000_000L) {
				beams.add(new Beam(hero.position.x, hero.position.y+50));
				fireDelay = System.nanoTime();
			}
		}
		
		updateHero(delta, acceleration);
		updatePlatforms(delta);
		updateEnemies(delta);
		updateBeams(delta);
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
			
			if(y > 25 && seed.nextFloat() > 0.6f) {
				Enemy enemy = new Enemy(platform.position.x, platform.position.y+Enemy.ENEMY_HEIGHT*3);
				enemies.add(enemy);
			}
			
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
	
	private void updateEnemies(float delta) {
		for(int i=0;i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			if(enemy.position.y > GameScreen.getRenderer().getCameraY()*2)
				break;
			enemy.update(delta);
		}
	}
	
	private void updateBeams(float delta) {
		for(int i=0;i < beams.size(); i++) {
			Beam beam = beams.get(i);
			if(beam.position.y > GameScreen.getRenderer().getCameraY()*2)
				break;
			beam.update(delta);
		}
	}
	
	private void checkCollisions() {
		checkPlatformCollisions();
		checkEnemyCollisions();
		checkBeamCollisions();
	}
	
	private void checkEnemyCollisions() {
		for(int i=0;i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			if(enemy.position.y < GameScreen.getRenderer().getCameraY()/2 && i != 0)
				continue;
			if(enemy.position.y > GameScreen.getRenderer().getCameraY()*2)
				break;
			if(hero.bounds.overlaps(enemy.bounds)) {
				GameScreen.gameOver = true;
				break;
			}
		}
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
	
	private void checkBeamCollisions() {
		try {
			for(int i=0;i < beams.size(); i++) {
				for(int j=0;j < enemies.size(); j++) {
					Beam beam = beams.get(i);
					Enemy enemy = enemies.get(j);
					if(beam.bounds.overlaps(enemy.bounds)) {
						enemies.remove(j);
						beams.remove(i);
					}
				}
			}
		} catch(IndexOutOfBoundsException e) {
			
		}
	}
	
}
