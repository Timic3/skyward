package com.timic.skywardgame.screens;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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

public class LeaderboardScreen extends ScreenBase {
	private Stage stage;

	public LeaderboardScreen(Game game) {
		super(game);
	}
	
	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		Table table = new Table();
		table.setFillParent(true);
		table.setRound(false);
		stage.addActor(table);
		
		final TextButton tbBack = new TextButton("Back", this.skin);
		
		LinkedHashMap<String, Integer> leaderboard = Accounts.getLeaderboard();
		
		LinkedHashMap<String, Integer> sorted = leaderboard.entrySet().stream()
        .sorted(
        		(x, y) -> y.getKey().compareTo(x.getKey())
        		)
        .collect(Collectors.toMap(
        		x -> x.getKey(),
        		x -> x.getValue(),
        		(x, y) -> x,
        		LinkedHashMap::new
        		)
        	);
		
		for(Entry<String, Integer> m : sorted.entrySet()) {
			String username = m.getKey();
			int score = m.getValue();
			final Label lRow = new Label(username+": "+score, this.skin);
			table.add(lRow);
			table.row();
		}
		table.row().padTop(100);
		table.add(tbBack);
		
		tbBack.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
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
		Gdx.app.debug("Skyward", "Disposing of leaderboard screen");
		stage.dispose();
	}

}
