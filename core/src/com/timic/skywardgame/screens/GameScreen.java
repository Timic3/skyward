package com.timic.skywardgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.timic.skywardgame.logic.World;
import com.timic.skywardgame.render.Renderer;

public class GameScreen extends ScreenBase {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private World world;
	private static Renderer renderer;

	public GameScreen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(560, 800); // 560, 800
		camera.position.set(560/2, 800/2, 0);
		world = new World();
		renderer = new Renderer(batch, world);
	}
	
	public void update(float delta) {
		world.update(delta);
	}
	
	public void draw() {
		Gdx.gl.glClearColor(0.0f, 0.5f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.render();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();
		batch.begin();
		// Uhm... 2D on-screen
		batch.end();
	}
	
	@Override
	public void render(float delta) {
		/*Gdx.gl.glClearColor(1.0f, 0.33f, 0.33f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batcher.setProjectionMatrix(camera.combined);
		batcher.enableBlending();
		batcher.begin();
		batcher.end();*/
		update(delta);
		draw();
	}
	
	@Override
	public void hide() {
		Gdx.app.debug("Skyward", "Disposing of game screen");
	}

	public static Renderer getRenderer() {
		return renderer;
	}
	
}
