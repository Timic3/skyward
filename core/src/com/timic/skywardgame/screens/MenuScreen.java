package com.timic.skywardgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.timic.skywardgame.accounts.Accounts;

public class MenuScreen extends ScreenBase {
	private Stage stage;

	public MenuScreen(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		Table table = new Table();
		table.setFillParent(true);
		table.setRound(false);
		stage.addActor(table);
		
		final Label lWelcome = new Label("Welcome back, {{Username}}!", this.skin);
		lWelcome.setText("Welcome back, "+Accounts.loggedInUsername+"!");
		final TextButton tbPlay = new TextButton("Play", this.skin);
		final TextButton tbOptions = new TextButton("Options", this.skin);
		final TextButton tbQuit = new TextButton("Quit", this.skin);
		
		
		table.add(lWelcome);
		table.row();
		table.add(tbPlay).width(250).padTop(50);
		table.row();
		table.add(tbOptions).width(250).padTop(10);
		table.row();
		table.add(tbQuit).width(250).padTop(10);
		
		tbPlay.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		
		tbOptions.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new OptionsScreen(game));
			}
		});
		
		tbQuit.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1.0f, 0.33f, 0.33f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void hide() {
		Gdx.app.debug("Skyward", "Disposing of menu screen");
		stage.dispose();
	}
	
}
