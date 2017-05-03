package com.timic.skywardgame.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.timic.skywardgame.logic.Assets;
import com.timic.skywardgame.logic.Hero;
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
		batch.draw(Assets.heroStand, world.hero.position.x, world.hero.position.y, Hero.HERO_WIDTH, Hero.HERO_HEIGHT);
	}
	
	private void renderPlatforms() {
		//batch.draw(Assets.snowHalfLeft, world.platform.position.x, world.platform.position.y, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
		//batch.draw(Assets.snowHalfRight, world.platform.position.x+70, world.platform.position.y, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
		for(int i=0;i < world.platforms.size(); i++) {
			Platform platform = world.platforms.get(i);
			batch.draw(Assets.snowHalfLeft, platform.position.x, platform.position.y, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
			batch.draw(Assets.snowHalfRight, platform.position.x+70, platform.position.y, Platform.PLATFORM_WIDTH, Platform.PLATFORM_HEIGHT);
		}
	}
	
}
