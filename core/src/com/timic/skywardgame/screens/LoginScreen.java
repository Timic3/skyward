package com.timic.skywardgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.timic.skywardgame.accounts.Accounts;
import com.timic.skywardgame.accounts.Accounts.Status;

public class LoginScreen extends ScreenBase {
	private Stage stage;

	public LoginScreen(Game game) {
		super(game);
	}
	
	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		Table table = new Table();
		table.setFillParent(true);
		table.setRound(false);
		table.align(Align.top);
		stage.addActor(table);
		
		final Image iTitle = new Image(new Texture(Gdx.files.internal("data/images/skyward.png")));
		final Label lUsername = new Label("Username", this.skin);
		final Label lPassword = new Label("Password", this.skin);
		final Label lRegister = new Label("Don't have an account? Register ", this.skin);
		final Label lHere = new Label("here", this.skin, "link-label");
		final TextField tfUsername = new TextField("", this.skin);
		final TextField tfPassword = new TextField("", this.skin);
		final TextButton tbLogin = new TextButton("Login", this.skin);
		final HorizontalGroup hgRegister = new HorizontalGroup();
		hgRegister.addActor(lRegister);
		hgRegister.addActor(lHere);
		lUsername.setAlignment(Align.center);
		lPassword.setAlignment(Align.center);
		tfUsername.setAlignment(Align.center);
		tfPassword.setAlignment(Align.center);
		tfPassword.setPasswordCharacter('\u2022');
		tfPassword.setPasswordMode(true);
		
		table.add(iTitle).padBottom(50).padTop(50).colspan(2).size(982*0.4f, 272*0.4f);
		table.row();
		table.add(lUsername).padBottom(5).colspan(2);
		table.row();
		table.add(tfUsername).width(300).padBottom(15).colspan(2);
		table.row();
		table.add(lPassword).padBottom(5).colspan(2);
		table.row();
		table.add(tfPassword).width(300).padBottom(15).colspan(2);
		table.row();
		table.add(tbLogin).padTop(15).colspan(2);
		table.row();
		table.add(hgRegister).padTop(15).colspan(2);
		
		tbLogin.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Status status = Accounts.authenticate(tfUsername.getText(), tfPassword.getText());
				if(status == Accounts.Status.SUCCESS)
					game.setScreen(new MenuScreen(game));
				else
					Accounts.friendlyError(stage, status);
			}
		});
		
		lHere.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new RegisterScreen(game));
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
		Gdx.app.debug("Skyward", "Disposing of login screen");
		stage.dispose();
	}
	
}
