package com.timic.skywardgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.timic.skywardgame.accounts.Accounts;
import com.timic.skywardgame.logic.Assets;
import com.timic.skywardgame.logic.World;
import com.timic.skywardgame.render.Renderer;

public class GameScreen extends ScreenBase {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private World world;
	private static Renderer renderer;
	
	public static int score = 0;
	public static boolean gameOver = false;
	boolean updateRequired = true;

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
		if(!gameOver) {
			Assets.font.setColor(1, 1, 1, 1);
			Assets.font.draw(batch, Integer.toString(score), 16, 800);
		} else {
			Assets.font.setColor(1, 0, 0, 1);
			Assets.font.draw(batch, "Game Over", 16, 800);
			Assets.font.draw(batch, "Final Score: "+score, 16, 760);
			if(updateRequired) {
				updateRequired = false;
				Accounts.updateScore(score);
			}
			if(Gdx.input.isKeyPressed(Keys.SPACE))
				game.setScreen(new LeaderboardScreen(game));
		}
		
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
		if(!gameOver)
			update(delta);
		draw();
	}
	
	@Override
	public void hide() {
		Gdx.app.debug("Skyward", "Disposing of game screen");
		batch.dispose();
		renderer.dispose();
		world.dispose();
		gameOver = false;
		score = 0;
		updateRequired = true;
	}

	public static Renderer getRenderer() {
		return renderer;
	}
	
}
