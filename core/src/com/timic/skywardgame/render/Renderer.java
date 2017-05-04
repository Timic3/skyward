package com.timic.skywardgame.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.timic.skywardgame.logic.Assets;
import com.timic.skywardgame.logic.Hero;
import com.timic.skywardgame.logic.Hero.State;
import com.timic.skywardgame.logic.Platform;
import com.timic.skywardgame.logic.World;

public class Renderer {
	private static final float FRUSTUM_WIDTH = 560;
	private static final float FRUSTUM_HEIGHT = 800;
	
	private World world;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	public Renderer(SpriteBatch batch, World world) {
		this.world = world;
		this.camera = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.camera.position.set(FRUSTUM_WIDTH/2, FRUSTUM_HEIGHT/2, 0);
		this.batch = batch;
	}
	
	public void render() {
		if(world.hero.position.y > camera.position.y)
			camera.position.y = world.hero.position.y;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		renderBackground();
		renderEntities();
	}
	
	public void renderBackground() {
		batch.disableBlending();
		batch.begin();
		batch.draw(Assets.backgroundRegion, camera.position.x-FRUSTUM_WIDTH/2, camera.position.y-FRUSTUM_HEIGHT/2, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.end();
	}
	
	public void renderEntities() {
		batch.enableBlending();
		batch.begin();
		renderPlatforms();
		renderHero();
		batch.end();
	}
	
	private void renderHero() {
		TextureRegion heroSprite = Assets.heroJump;
		int actualHeight = Hero.HERO_HEIGHT;
		if(world.hero.state == State.FALL) {
			actualHeight = 72;
			heroSprite = Assets.heroDuck;
		}
		int side = Hero.facing == -1 ? Hero.HERO_WIDTH : 0;
		batch.draw(heroSprite, world.hero.position.x+side, world.hero.position.y, Hero.HERO_WIDTH*Hero.facing, actualHeight);
	}
	
	private void renderPlatforms() {
		for(int i=0;i < world.platforms.size(); i++) {
			Platform platform = world.platforms.get(i);
			if(platform.position.y < camera.position.y/2 && i != 0)
				continue;
			if(platform.position.y > camera.position.y*2)
				break;
			batch.draw(Assets.snowHalfLeft, platform.position.x, platform.position.y, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(Assets.snowHalfRight, platform.position.x+70, platform.position.y, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
		}
	}
	
	public float getCameraY() {
		return this.camera.position.y;
	}
	
	public void dispose() {
		world.platforms.clear();
		camera = null;
		world = null;
	}
	
}
