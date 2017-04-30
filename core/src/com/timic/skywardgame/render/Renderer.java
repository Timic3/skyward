package com.timic.skywardgame.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.timic.skywardgame.logic.Assets;
import com.timic.skywardgame.logic.World;

public class Renderer {
	private static final float FRUSTUM_WIDTH = 560/55f;
	private static final float FRUSTUM_HEIGHT = 800/55f;
	
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
		// Render objects
		batch.enableBlending();
		batch.begin();
		renderRocket();
		batch.end();
	}
	
	private void renderRocket() {
		batch.draw(Assets.rocket, world.rocket.position.x+0.5f, world.rocket.position.y-0.5f, 1, 1);
	}
	
}
